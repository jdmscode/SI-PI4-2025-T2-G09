document.addEventListener("DOMContentLoaded", () => {
  const API_BASE_URL = "http://localhost:8080";

  function resolveImageUrl(path) {
    if (!path) return null;

    if (path.startsWith("http://") || path.startsWith("https://")) {
      return path;
    }

    if (path.startsWith("/")) {
      return `${API_BASE_URL}${path}`;
    }

    return `${API_BASE_URL}/${path}`;
  }

  const usuarioLogado = JSON.parse(localStorage.getItem("usuarioLogado"));
  if (!usuarioLogado) {
    window.location.href = "login.html";
    return;
  }

  function getAuthHeader() {
    if (!usuarioLogado.email || !usuarioLogado.senha) {
      return {};
    }
    const credenciaisBase64 = btoa(
      `${usuarioLogado.email}:${usuarioLogado.senha}`
    );
    return {
      Authorization: `Basic ${credenciaisBase64}`,
    };
  }

  // pega o postId da URL
  const params = new URLSearchParams(window.location.search);
  const rawPostId = params.get("postId");

    const postId = rawPostId ? Number(rawPostId) : null;

    if (!postId || Number.isNaN(postId)) {
      console.error("postId inválido na URL:", rawPostId);
      alert("Publicação inválida. Volte para o feed e tente novamente.");
      window.location.href = "home.html";
      return;
    }

  const postDetalheEl = document.getElementById("postDetalhe");
  const comentariosContainer = document.getElementById("comentariosContainer");
  const comentarioInput = document.getElementById("comentarioInput");
  const enviarComentarioBtn = document.getElementById("enviarComentarioBtn");
  const comentarioCountSpan = document.getElementById("comentarioCount");

  if (!postId) {
    alert("Post não informado.");
    window.location.href = "home.html";
    return;
  }

  // Carregar detalhes do post
  async function carregarPost() {
    try {
      const resp = await fetch(`${API_BASE_URL}/post/${postId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          ...getAuthHeader(),
        },
      });

      if (!resp.ok) {
        throw new Error(`Erro ao buscar post: ${resp.status}`);
      }

      const post = await resp.json();

      const dataCriacao = post.createdAt
        ? new Date(post.createdAt)
        : null;

      const dataFormatada = dataCriacao
        ? `${dataCriacao.toLocaleDateString("pt-BR")} ${dataCriacao.toLocaleTimeString(
            "pt-BR",
            { hour: "2-digit", minute: "2-digit" }
          )}`
        : "";

      const autorNome =
        post.autor?.nomeCompleto || post.nomeCompleto || "Autor desconhecido";

      //cria a URL da imagem
      const imgUrl = resolveImageUrl(post.imagemUrl);

      //monta o HTML da imagem se existir
      const imagemHtml = imgUrl
      ? `<img src="${imgUrl}" alt="Imagem da publicação" class="post-detalhe-imagem" />`
      : "";

      postDetalheEl.innerHTML = `
        <h2>${post.titulo}</h2>
        ${imagemHtml}
        <p class="post-detalhe-meta">
          <strong>Autor:</strong> ${autorNome}
          ${dataFormatada ? ` · <strong>Data:</strong> ${dataFormatada}` : ""}
        </p>
        <p class="post-detalhe-texto">${post.texto}</p>
        <p class="post-detalhe-reacoes">
          👍 ${post.qtdLikes ?? 0} &nbsp;&nbsp; 👎 ${post.qtdDislikes ?? 0}
        </p>
      `;
    } catch (erro) {
      console.error(erro);
      postDetalheEl.innerHTML =
        "<p>Erro ao carregar detalhes da publicação.</p>";
    }
  }

  // Carregar comentários
  async function carregarComentarios() {
    try {
      const resp = await fetch(
        `${API_BASE_URL}/posts/${postId}/comentarios`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            ...getAuthHeader(),
          },
        }
      );

      if (!resp.ok) {
        throw new Error(`Erro ao buscar comentários: ${resp.status}`);
      }

      const lista = await resp.json();

      comentariosContainer.innerHTML = "";

      if (!Array.isArray(lista) || lista.length === 0) {
        comentarioCountSpan.textContent = "0";
        comentariosContainer.innerHTML =
          "<p>Seja o primeiro a comentar!</p>";
        return;
      }

      comentarioCountSpan.textContent = lista.length.toString();

      lista.forEach((c) => {
        const card = document.createElement("article");
        card.classList.add("comentario-card");

        const autorNome =
          c.autor?.nomeCompleto || "Usuário";

        const dataCriacao = c.createdAt ? new Date(c.createdAt) : null;
        const dataFormatada = dataCriacao
          ? `${dataCriacao.toLocaleDateString("pt-BR")} ${dataCriacao.toLocaleTimeString(
              "pt-BR",
              { hour: "2-digit", minute: "2-digit" }
            )}`
          : "";

        const isDoUsuarioLogado =
          c.autor?.id === usuarioLogado.id ||
          c.autorId === usuarioLogado.id;

        // estrutura HTML básica
        card.innerHTML = `
          <div class="comentario-autor-linha">
            <span class="comentario-autor">${autorNome}</span>
            <span class="comentario-data">${dataFormatada}</span>
          </div>
          <div class="comentario-texto">${c.texto || ""}</div>
          <div class="comentario-acoes"></div>
        `;

        const acoesDiv = card.querySelector(".comentario-acoes");

        comentariosContainer.appendChild(card);
      });
    } catch (erro) {
      console.error(erro);
      comentariosContainer.innerHTML =
        "<p>Erro ao carregar comentários.</p>";
      comentarioCountSpan.textContent = "0";
    }
  }

  // Enviar novo comentário
  async function enviarComentario() {
    const texto = comentarioInput.value.trim();
    if (!texto) {
      alert("Digite um comentário antes de enviar.");
      return;
    }

    const payload = {
      texto: texto,
      autor: {
        id: usuarioLogado.id, // usa o ID do profissional logado
      },
    };

    try {
      const resp = await fetch(
        `${API_BASE_URL}/posts/${postId}/comentarios`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            ...getAuthHeader(),
          },
          body: JSON.stringify(payload),
        }
      );

      if (!resp.ok) {
        throw new Error(`Erro ao salvar comentário: ${resp.status}`);
      }

      comentarioInput.value = "";
      await carregarComentarios();
    } catch (erro) {
      console.error(erro);
      alert("Erro ao enviar comentário.");
    }
  }

  enviarComentarioBtn.addEventListener("click", (event) => {
    event.preventDefault();
    enviarComentario();
  });

  comentarioInput.addEventListener("keydown", (event) => {
    if (event.key === "Enter" && event.ctrlKey) {
      event.preventDefault();
      enviarComentario();
    }
  });

  carregarPost();
  carregarComentarios();
});

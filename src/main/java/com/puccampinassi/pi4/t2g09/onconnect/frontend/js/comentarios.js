document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "http://localhost:8081";

  const usuarioLogado = JSON.parse(localStorage.getItem("usuarioLogado"));
  if (!usuarioLogado) {
    window.location.href = "login.html";
    return;
  }

  const params = new URLSearchParams(window.location.search);
  const postId = params.get("postId");

  if (!postId) {
    alert("Post não informado.");
    window.location.href = "posts.html";
    return;
  }

  const postTituloSpan = document.getElementById("postTitulo");
  const postConteudoDiv = document.getElementById("postConteudo");
  const listaComentariosDiv = document.getElementById("listaComentarios");
  const formNovoComentario = document.getElementById("formNovoComentario");
  const textoComentarioInput = document.getElementById("textoComentario");

  // Carrega dados iniciais
  carregarPost();
  carregarComentarios();

  formNovoComentario.addEventListener("submit", async (e) => {
    e.preventDefault();

    const texto = textoComentarioInput.value.trim();
    if (!texto) {
      alert("Digite um comentário.");
      return;
    }

    const novoComentario = {
      texto,
      autor: {
        id: usuarioLogado.id,
      },
    };

    try {
      const resp = await fetch(`${API_BASE}/posts/${postId}/comentarios`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(novoComentario),
      });

      if (!resp.ok) {
        const erro = await resp.json().catch(() => ({}));
        console.error("Erro ao criar comentário:", erro);
        alert("Erro ao criar comentário.");
        return;
      }

      textoComentarioInput.value = "";
      carregarComentarios();
    } catch (err) {
      console.error(err);
      alert("Erro de conexão ao criar comentário.");
    }
  });

  async function carregarPost() {
    try {
      const resp = await fetch(`${API_BASE}/post`);
      if (!resp.ok) {
        postConteudoDiv.innerHTML =
          "<p>Erro ao carregar detalhes do post.</p>";
        return;
      }

      const posts = await resp.json();
      const post = posts.find((p) => String(p.id) === String(postId));

      if (!post) {
        postConteudoDiv.innerHTML = "<p>Post não encontrado.</p>";
        return;
      }

      postTituloSpan.textContent = post.titulo;

      const autorNome =
        post.autor && post.autor.nomeCompleto
          ? post.autor.nomeCompleto
          : "Autor desconhecido";

      postConteudoDiv.innerHTML = `
        <p class="card-text">${post.texto}</p>
        <p class="card-meta">
          Por <strong>${autorNome}</strong> • Likes: ${post.qtdLikes ?? 0} • Dislikes: ${post.qtdDislikes ?? 0}
        </p>
      `;
    } catch (err) {
      console.error(err);
      postConteudoDiv.innerHTML = "<p>Erro de conexão ao carregar post.</p>";
    }
  }

  async function carregarComentarios() {
    listaComentariosDiv.innerHTML = "<p>Carregando comentários...</p>";

    try {
      const resp = await fetch(`${API_BASE}/posts/${postId}/comentarios`);
      if (!resp.ok) {
        listaComentariosDiv.innerHTML =
          "<p>Erro ao carregar comentários.</p>";
        return;
      }

      const comentarios = await resp.json();

      if (!comentarios.length) {
        listaComentariosDiv.innerHTML =
          "<p>Não há comentários para este post ainda.</p>";
        return;
      }

      listaComentariosDiv.innerHTML = "";

      comentarios.forEach((comentario) => {
        const card = document.createElement("div");
        card.classList.add("card");

        const autorNome =
          comentario.autor && comentario.autor.nomeCompleto
            ? comentario.autor.nomeCompleto
            : "Autor desconhecido";

        card.innerHTML = `
          <p class="card-text">${comentario.texto}</p>
          <p class="card-meta">Por <strong>${autorNome}</strong></p>
        `;

        listaComentariosDiv.appendChild(card);
      });
    } catch (err) {
      console.error(err);
      listaComentariosDiv.innerHTML =
        "<p>Erro de conexão ao carregar comentários.</p>";
    }
  }
});

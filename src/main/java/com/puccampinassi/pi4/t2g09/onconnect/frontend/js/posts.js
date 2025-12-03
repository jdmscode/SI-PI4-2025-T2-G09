document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "http://localhost:8081";

  const usuarioLogado = JSON.parse(localStorage.getItem("usuarioLogado"));

  if (!usuarioLogado) {
    window.location.href = "login.html";
    return;
  }

  const nomeUsuarioSpan = document.getElementById("nomeUsuario");
  const formNovoPost = document.getElementById("formNovoPost");
  const listaPostsDiv = document.getElementById("listaPosts");

  nomeUsuarioSpan.textContent = usuarioLogado.nomeCompleto || "Usuário";

  carregarPosts();

  formNovoPost.addEventListener("submit", async (e) => {
    e.preventDefault();

    const titulo = document.getElementById("titulo").value.trim();
    const texto = document.getElementById("texto").value.trim();

    if (!titulo || !texto) {
      alert("Preencha título e texto.");
      return;
    }

    const novoPost = {
      titulo,
      texto,
      autor: {
        id: usuarioLogado.id,
      },
    };

    try {
      const resp = await fetch(`${API_BASE}/post`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(novoPost),
      });

      if (!resp.ok) {
        const erro = await resp.json().catch(() => ({}));
        console.error("Erro ao criar post:", erro);
        alert("Erro ao criar publicação.");
        return;
      }

      formNovoPost.reset();
      carregarPosts();
    } catch (err) {
      console.error(err);
      alert("Erro de conexão ao criar publicação.");
    }
  });

  async function carregarPosts() {
    listaPostsDiv.innerHTML = "<p>Carregando publicações...</p>";

    try {
      const resp = await fetch(`${API_BASE}/post`);
      if (!resp.ok) {
        listaPostsDiv.innerHTML = "<p>Erro ao carregar publicações.</p>";
        return;
      }

      const posts = await resp.json();

      if (!posts.length) {
        listaPostsDiv.innerHTML = "<p>Não há publicações cadastradas ainda.</p>";
        return;
      }

      listaPostsDiv.innerHTML = "";

      posts.forEach((post) => {
        const card = document.createElement("div");
        card.classList.add("card");

        const autorNome =
          post.autor && post.autor.nomeCompleto
            ? post.autor.nomeCompleto
            : "Autor desconhecido";

        card.innerHTML = `
          <h3>${post.titulo}</h3>
          <p class="card-text">${post.texto}</p>
          <p class="card-meta">
            Por <strong>${autorNome}</strong> • Likes: ${post.qtdLikes ?? 0} • Dislikes: ${post.qtdDislikes ?? 0}
          </p>
          <button class="secondary-btn" data-id="${post.id}">
            Ver comentários
          </button>
        `;

        const btnComentarios = card.querySelector("button");
        btnComentarios.addEventListener("click", () => {
          window.location.href = `comentarios.html?postId=${post.id}`;
        });

        listaPostsDiv.appendChild(card);
      });
    } catch (err) {
      console.error(err);
      listaPostsDiv.innerHTML =
        "<p>Erro de conexão ao carregar publicações.</p>";
    }
  }
});

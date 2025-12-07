document.addEventListener("DOMContentLoaded", () => {
  const usuarioLogado = JSON.parse(localStorage.getItem("usuarioLogado"));

  if (!usuarioLogado) {
    window.location.href = "index.html";
    return;
  }

  // ========= helper para montar o header Authorization: Basic ========

  function getAuthHeader() {
    // precisa ter sido salvo no login.js: user.email = email; user.senha = senha;
    if (!usuarioLogado.email || !usuarioLogado.senha) {
      return {};
    }

    const credenciaisBase64 = btoa(`${usuarioLogado.email}:${usuarioLogado.senha}`);
    return {
      Authorization: `Basic ${credenciaisBase64}`
    };
  }

  // Exibe o nome do usuário
  const nomeUsuarioElemento = document.getElementById("nomeUsuario");
  if (nomeUsuarioElemento) {
    nomeUsuarioElemento.textContent = usuarioLogado.nomeCompleto || "Usuário";
  }

  // ===================== LOGOUT =====================
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      localStorage.removeItem("usuarioLogado");
      window.location.href = "index.html";
    });
  }

  // ===================== CONFIGURAÇÕES DO FEED =====================

  const API_BASE_URL = "http://localhost:8080"; // ajusta se precisar
  const PAGE_SIZE = 10;

  const postsContainer = document.getElementById("postsContainer");
  const searchForm = document.getElementById("searchForm");
  const searchInput = document.getElementById("searchInput");
  const prevPageBtn = document.getElementById("prevPageBtn");
  const nextPageBtn = document.getElementById("nextPageBtn");
  const paginaAtualSpan = document.getElementById("paginaAtual");

  let paginaAtual = 0;
  let termoBuscaAtual = "";

    // ===================== HELPER PARA URL DE IMAGEM =====================

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

  // ===================== FUNÇÃO PARA ENVIAR REAÇÃO =====================

  async function enviarReacaoPost(postId, tipo) {
    const endpoint =
      tipo === "like"
        ? `/reacao/posts/${postId}/like/toggle`
        : `/reacao/posts/${postId}/deslike/toggle`;

    const url = `${API_BASE_URL}${endpoint}`;

    const options = {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        ...getAuthHeader()
      }
    };

    const resp = await fetch(url, options);

    if (!resp.ok) {
      throw new Error(`Erro ao enviar reação: ${resp.status}`);
    }

    return resp.json(); // { postId, likeCount } ou { postId, deslikeCount }
  }

  // ===================== FUNÇÃO PARA MONTAR O CARD DO POST =====================

  function criarCardPost(post) {
  const card = document.createElement("article");
  card.classList.add("post-card");

  // deixa o card como "base" para o footer absoluto
  card.style.position = "relative";
  card.style.paddingBottom = "40px"; // espaço pro footer não ficar em cima do texto

  // Título
  const titulo = document.createElement("h2");
  titulo.classList.add("post-titulo");
  titulo.textContent = post.titulo;

  // quando clicar no título, abre a página de comentários
  titulo.addEventListener("click", () => {
    if (post.id != null) {
      window.location.href = `comentarios.html?postId=${post.id}`;
    } else {
      console.error("Post sem ID:", post);
      alert("Não foi possível abrir os comentários: post sem ID.");
    }
  });

  // Meta
  const meta = document.createElement("div");
  meta.classList.add("post-meta");
  const data = new Date(post.createdAt);
  meta.textContent = `Por ${post.nomeCompleto} · ${data.toLocaleDateString(
    "pt-BR"
  )} ${data.toLocaleTimeString("pt-BR", {
    hour: "2-digit",
    minute: "2-digit"
  })}`;

  // Texto
  const texto = document.createElement("p");
  texto.classList.add("post-texto");
  const limiteCaracteres = 200;
  texto.textContent =
    post.texto.length > limiteCaracteres
      ? post.texto.slice(0, limiteCaracteres) + "..."
      : post.texto;

  // ===== IMAGEM DO POST =====
  const imgUrl = resolveImageUrl(post.imagemUrl);

  if (imgUrl) {
    const img = document.createElement("img");
    img.src = imgUrl;
    img.alt = "Imagem da publicação";
    img.classList.add("post-imagem");
    card.appendChild(img);
  }

  // ===== FOOTER DE REAÇÕES NO CANTO INFERIOR ESQUERDO =====
  const footer = document.createElement("div");
  footer.style.position = "absolute";
  footer.style.left = "16px";
  footer.style.bottom = "10px";
  footer.style.display = "flex";
  footer.style.alignItems = "center";
  footer.style.gap = "12px";
  footer.style.fontSize = "0.9rem";

  // Like
  const likeWrapper = document.createElement("span");
  likeWrapper.style.display = "inline-flex";
  likeWrapper.style.alignItems = "center";
  likeWrapper.style.gap = "4px";

  const likeBtn = document.createElement("span");
  likeBtn.textContent = "👍";
  likeBtn.style.cursor = "pointer";
  likeBtn.style.fontSize = "1.1rem";

  const likeCountSpan = document.createElement("span");
  likeCountSpan.textContent = post.qtdLikes;

  // Deslike
  const deslikeWrapper = document.createElement("span");
  deslikeWrapper.style.display = "inline-flex";
  deslikeWrapper.style.alignItems = "center";
  deslikeWrapper.style.gap = "4px";

  const deslikeBtn = document.createElement("span");
  deslikeBtn.textContent = "👎";
  deslikeBtn.style.cursor = "pointer";
  deslikeBtn.style.fontSize = "1.1rem";

  const deslikeCountSpan = document.createElement("span");
  deslikeCountSpan.textContent = post.qtdDislikes;

  // Eventos de clique
  likeBtn.addEventListener("click", async () => {
    try {
      likeBtn.style.pointerEvents = "none";
      deslikeBtn.style.pointerEvents = "none";

      const data = await enviarReacaoPost(post.id, "like");
      if (typeof data.likeCount === "number") {
        likeCountSpan.textContent = data.likeCount;
      }
    } catch (erro) {
      console.error(erro);
      alert("Erro ao registrar like. Tente novamente.");
    } finally {
      likeBtn.style.pointerEvents = "auto";
      deslikeBtn.style.pointerEvents = "auto";
    }
  });

  deslikeBtn.addEventListener("click", async () => {
    try {
      likeBtn.style.pointerEvents = "none";
      deslikeBtn.style.pointerEvents = "none";

      const data = await enviarReacaoPost(post.id, "deslike");
      if (typeof data.deslikeCount === "number") {
        deslikeCountSpan.textContent = data.deslikeCount;
      }
    } catch (erro) {
      console.error(erro);
      alert("Erro ao registrar deslike. Tente novamente.");
    } finally {
      likeBtn.style.pointerEvents = "auto";
      deslikeBtn.style.pointerEvents = "auto";
    }
  });

  // Monta wrappers
  likeWrapper.appendChild(likeBtn);
  likeWrapper.appendChild(likeCountSpan);

  deslikeWrapper.appendChild(deslikeBtn);
  deslikeWrapper.appendChild(deslikeCountSpan);

  footer.appendChild(likeWrapper);
  footer.appendChild(deslikeWrapper);

  // Monta card final
  card.appendChild(titulo);
  card.appendChild(meta);
  card.appendChild(texto);
  card.appendChild(footer);

  return card;
}



  // ===================== FUNÇÃO PARA CARREGAR O FEED =====================

  async function carregarFeed(pagina = 0, termoBusca = "") {
    if (!postsContainer) return;

    try {
      const params = new URLSearchParams();
      params.append("page", pagina);
      params.append("size", PAGE_SIZE);
      if (termoBusca && termoBusca.trim() !== "") {
        params.append("q", termoBusca.trim());
      }

      const url = `${API_BASE_URL}/feed?${params.toString()}`;

      const fetchOptions = {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          ...getAuthHeader()
        }
      };

      const response = await fetch(url, fetchOptions);

      if (!response.ok) {
        throw new Error(`Erro ao carregar feed: ${response.status}`);
      }

      const pageData = await response.json();

      postsContainer.innerHTML = "";

      if (!pageData.content || pageData.content.length === 0) {
        const vazio = document.createElement("p");
        vazio.classList.add("feed-vazio");
        vazio.textContent = termoBusca
          ? "Nenhuma postagem encontrada para essa busca."
          : "Ainda não há postagens.";
        postsContainer.appendChild(vazio);
      } else {
        pageData.content.forEach((post) => {
          const card = criarCardPost(post);
          postsContainer.appendChild(card);
        });
      }

      paginaAtual = pageData.number;

      if (paginaAtualSpan) {
        paginaAtualSpan.textContent = `Página ${
          paginaAtual + 1
        } de ${pageData.totalPages || 1}`;
      }

      if (prevPageBtn) {
        prevPageBtn.disabled = pageData.first;
      }

      if (nextPageBtn) {
        nextPageBtn.disabled = pageData.last;
      }
    } catch (erro) {
      console.error(erro);
      postsContainer.innerHTML = "";
      const erroElemento = document.createElement("p");
      erroElemento.classList.add("feed-erro");
      erroElemento.textContent =
        "Ocorreu um erro ao carregar o feed. Tente novamente mais tarde.";
      postsContainer.appendChild(erroElemento);
    }
  }

  // ===================== BUSCA =====================

  if (searchForm && searchInput) {
    searchForm.addEventListener("submit", (event) => {
      event.preventDefault();
      termoBuscaAtual = searchInput.value || "";
      carregarFeed(0, termoBuscaAtual);
    });
  }

  // ===================== PAGINAÇÃO =====================

  if (prevPageBtn) {
    prevPageBtn.addEventListener("click", () => {
      if (paginaAtual > 0) {
        carregarFeed(paginaAtual - 1, termoBuscaAtual);
      }
    });
  }

  if (nextPageBtn) {
    nextPageBtn.addEventListener("click", () => {
      carregarFeed(paginaAtual + 1, termoBuscaAtual);
    });
  }

  // ===================== INÍCIO =====================

  carregarFeed(0, "");
});

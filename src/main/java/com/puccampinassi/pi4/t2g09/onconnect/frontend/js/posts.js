document.addEventListener("DOMContentLoaded", () => {
  const API_BASE_URL = "http://localhost:8080";

  const usuarioLogado = JSON.parse(localStorage.getItem("usuarioLogado"));

  // se não estiver logado, manda para o login
  if (!usuarioLogado) {
    window.location.href = "login.html";
    return;
  }

  // helper para montar Authorization Basic
  function getAuthHeader() {
    if (!usuarioLogado.email || !usuarioLogado.senha) {
      return {};
    }

    const credenciaisBase64 = btoa(`${usuarioLogado.email}:${usuarioLogado.senha}`);
    return {
      Authorization: `Basic ${credenciaisBase64}`
    };
  }

  // botão de logout reaproveitado
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      localStorage.removeItem("usuarioLogado");
      window.location.href = "login.html";
    });
  }

  const form = document.getElementById("postForm");
  const tituloInput = document.getElementById("titulo");
  const textoInput = document.getElementById("texto");

  if (!form) return;

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const titulo = tituloInput.value.trim();
    const texto = textoInput.value.trim();

    if (!titulo || !texto) {
      alert("Preencha título e conteúdo da publicação.");
      return;
    }

    const payload = {
      titulo: titulo,
      texto: texto
    };

    try {
      const response = await fetch(`${API_BASE_URL}/post`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          ...getAuthHeader()
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const msg = `Erro ao criar publicação. Código ${response.status}`;
        console.error(msg);
        alert(msg);
        return;
      }

      const postCriado = await response.json();
      console.log("Post criado:", postCriado);

      alert("Publicação criada com sucesso!");
      // depois de criar, volta para o feed
      window.location.href = "home.html";

    } catch (erro) {
      console.error("Erro na requisição:", erro);
      alert("Ocorreu um erro ao criar a publicação. Tente novamente.");
    }
  });
});

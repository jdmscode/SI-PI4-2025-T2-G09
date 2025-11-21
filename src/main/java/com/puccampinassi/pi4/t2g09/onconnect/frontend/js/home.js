document.addEventListener("DOMContentLoaded", () => {
  const usuarioLogado = JSON.parse(localStorage.getItem("usuarioLogado"));

  if (!usuarioLogado) {
    // Se não estiver logado, volta pro login
    window.location.href = "login.html";
    return;
  }

  // Exibe o nome do usuário
  document.getElementById("nomeUsuario").textContent = usuarioLogado.nomeCompleto || "Usuário";

  // Logout
  document.getElementById("logoutBtn").addEventListener("click", () => {
    localStorage.removeItem("usuarioLogado");
    window.location.href = "login.html";
  });
});

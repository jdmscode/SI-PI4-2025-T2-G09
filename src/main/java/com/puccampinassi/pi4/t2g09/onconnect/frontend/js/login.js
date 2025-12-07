// Jhonatan

document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = document.getElementById("email").value;
  const senha = document.getElementById("senha").value;

  try {
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, senha })
    });

    if (response.ok) {
      const user = await response.json(); 

      // 👇 AQUI É A CORREÇÃO
      user.email = email;
      user.senha = senha;

      localStorage.setItem("usuarioLogado", JSON.stringify(user));

      alert("Login realizado com sucesso!");
      window.location.href = "home.html";
    } else {
      alert("Credenciais inválidas.");
    }
  } catch (error) {
    console.error("Erro na autenticação:", error);
    alert("Erro ao conectar-se ao servidor.");
  }
});

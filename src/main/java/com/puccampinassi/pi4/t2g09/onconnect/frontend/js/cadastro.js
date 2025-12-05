document.getElementById("cadastroForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const profissional = {
    nomeCompleto: document.getElementById("nomeCompleto").value,
    especialidade: document.getElementById("especialidade").value,
    cpf: document.getElementById("cpf").value,
    registroProfissional: document.getElementById("registroProfissional").value,
    instituicao: document.getElementById("instituicao").value,
    email: document.getElementById("email").value,
    senha: document.getElementById("senha").value,
  };

  try {
    const response = await fetch("http://localhost:8080/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(profissional)
    });

    if (response.ok) {
      alert("Cadastro realizado com sucesso!");
      window.location.href = "index.html";
    } else {
      const text = await response.text();
      alert("Erro ao cadastrar: " + text);
    }
  } catch (error) {
    console.error("Erro no cadastro:", error);
    alert("Falha na conexão com o servidor.");
  }
});

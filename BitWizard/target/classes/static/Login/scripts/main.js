document.getElementById("loginForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Previene il comportamento predefinito del form

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const loginData = new URLSearchParams();
    loginData.append("email", email);
    loginData.append("password", password);

    try {
        const response = await fetch("/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: loginData
        });

        if (response.ok) {
            // Login riuscito
            window.scrollTo({ top: 0, behavior: 'smooth' }); // Scroll verso l'alto
            Swal.fire({
                icon: 'success',
                title: 'Login avvenuto con successo!',
                text: 'Verrai reindirizzato alla tua pagina personale',
                confirmButtonText: 'OK'
            }).then(() => {
    			window.location.assign("paginaPersonale"); // Reindirizzamento alla pagina personale
			});
        } else {
            // Login fallito, mostra l'errore
            const errorMessage = await response.text();
            Swal.fire({
                icon: 'error',
                title: 'Errore di autenticazione',
                text: errorMessage || 'Credenziali non valide. Per favore riprova.',
                confirmButtonText: 'OK'
            });
        }
    } catch (error) {
        console.error("Errore:", error);
        Swal.fire({
            icon: 'error',
            title: 'Errore di rete',
            text: 'Non è stato possibile connettersi al server. Riprova più tardi.',
            confirmButtonText: 'OK'
        });
    }
});

// Funzione per caricare i dati dell'utente
async function caricaDatiUtente() {
    const response = await fetch('/api/utente/me');
    const data = await response.json();

    document.getElementById('nomeCognome').textContent = `${data.nome} ${data.cognome}`;
    document.getElementById('email').textContent = data.email;
    document.getElementById('descrizione').textContent = data.descrizione;
}

// Funzione per caricare i viaggi dell'utente
async function caricaViaggiUtente() {
    const response = await fetch('/api/viaggi/utente');
    const viaggi = await response.json();
    const container = document.getElementById('cardContainer');
    container.innerHTML = '';  // Svuota il contenitore prima di aggiungere nuove card

    viaggi.forEach(viaggio => {
        const card = createViaggioCard(viaggio);
        container.innerHTML += card;
    });
}

// Funzione per creare una card di Bootstrap per un viaggio
function createViaggioCard(viaggio) {
    const dataPartenza = new Date(viaggio.dataPartenza).toLocaleDateString();
    const dataRitorno = new Date(viaggio.dataRitorno).toLocaleDateString();

    return `
        <div class="col-md-4 viaggio-card" 
             data-partenza="${viaggio.dataPartenza}" 
             data-ritorno="${viaggio.dataRitorno}">
            <div class="viaggio">
                <img src="/api/viaggi/${viaggio.viaggioId}/immagine" class="card-img-top" alt="Immagine del viaggio">
                <div class="card-body">
                    <h5 class="card-title">${viaggio.nome}</h5>
                    <p class="card-text">
                        Luogo di Partenza: ${viaggio.luogoPartenza} <br>
                        Luogo di Arrivo: ${viaggio.luogoArrivo} <br>
                        Data di Partenza: <span>${dataPartenza}</span> <br>
                        Data di Ritorno: <span>${dataRitorno}</span> <br>
                    </p>
                </div>
            </div>
        </div>
    `;
}

// Funzione per filtrare i viaggi
function filtraViaggi(tipoViaggio) {
    const viaggioElements = document.querySelectorAll('.viaggio-card');
    const oggi = new Date();

    viaggioElements.forEach(viaggio => {
        const dataPartenza = new Date(viaggio.getAttribute('data-partenza'));
        const dataRitorno = new Date(viaggio.getAttribute('data-ritorno'));

        if (tipoViaggio === 'prenotati') {
            // Mostra solo i viaggi con data di partenza futura
            viaggio.style.display = (dataPartenza > oggi) ? 'block' : 'none';
        } else if (tipoViaggio === 'completati') {
            // Mostra solo i viaggi con data di ritorno passata
            viaggio.style.display = (dataRitorno < oggi) ? 'block' : 'none';
        } else {
            // Mostra tutti i viaggi
            viaggio.style.display = 'block';
        }
    });
}

// Funzione per gestire il logout
async function logout() {
    const response = await fetch('/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            // Aggiungi eventuali header di autenticazione se necessari
        },
    });

    if (response.ok) {
        // Logout avvenuto con successo
        window.location.href = '/'; // Reindirizza alla home page
    } else {
        // Gestisci eventuali errori
        console.error('Logout fallito');
        alert('Logout fallito, riprova.');
    }
}
// Funzione per gestire l'apertura immagini della galleria
function openModal(img) {
    const modal = document.getElementById("imageModal");
    const modalImg = document.getElementById("modalImage");

    modal.style.display = "flex";
    modalImg.src = img.src;
}

function closeModal() {
    document.getElementById("imageModal").style.display = "none";
}

// Carica i dati al caricamento della pagina
window.onload = function () {
    caricaDatiUtente();
    caricaViaggiUtente();
};

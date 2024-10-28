// Funzione per caricare i dati dell'utente
async function caricaDatiUtente() {
    const response = await fetch('/api/utente/me');
    const data = await response.json();

    document.getElementById('nomeCognome').textContent = data.nome + ' ' + data.cognome;
    document.getElementById('email').textContent = data.email;
    document.getElementById('descrizione').textContent = data.descrizione;
}

// Funzione per caricare i viaggi dell'utente
async function caricaViaggiUtente() {
    const response = await fetch('/api/viaggi/utente');
    const viaggi = await response.json();
    const container = document.getElementById('cardContainer');

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
        <div class="col-md-4">
            <div class="viaggio">
                <img src="/api/viaggi/${viaggio.viaggioId}/immagine" class="card-img-top" alt="Immagine del viaggio">
                <div class="card-body">
                    <h5 class="card-title">${viaggio.nome}</h5>
                    <p class="card-text">
                        Luogo di Partenza: ${viaggio.luogoPartenza} <br>
                        Luogo di Arrivo: ${viaggio.luogoArrivo} <br>
                        Data di Partenza: ${dataPartenza} <br>
                        Data di Ritorno: ${dataRitorno} <br>
                    </p>
                </div>
            </div>
        </div>
    `;
}

// Funzione per filtrare i viaggi
function filtraViaggi() {
    const tipoViaggio = document.getElementById('viaggiSelect').value;
    const viaggioElements = document.querySelectorAll('.viaggio');
    const oggi = new Date();

    viaggioElements.forEach(viaggio => {
        const dataRitorno = new Date(viaggio.querySelector('.dataRitorno').textContent);
        const dataPartenza = new Date(viaggio.querySelector('.dataPartenza').textContent);

        if (tipoViaggio === 'prenotati' && dataPartenza > oggi) {
            viaggio.style.display = 'block';
        } else if (tipoViaggio === 'completati' && dataRitorno < oggi) {
            viaggio.style.display = 'block';
        } else {
            viaggio.style.display = 'none';
        }
    });
}

// Carica i dati al caricamento della pagina
window.onload = function () {
    caricaDatiUtente();
    caricaViaggiUtente();
};

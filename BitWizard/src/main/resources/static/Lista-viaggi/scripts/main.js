async function caricaViaggi() {
    const response = await fetch('api/viaggi/lista'); // Endpoint che restituisce i viaggi dell'utente
    const viaggi = await response.json();
    const container = document.getElementById('cardContainer');

    // Svuota il contenitore prima di aggiungere le nuove card
    container.innerHTML = '';

    viaggi.forEach(viaggio => {
        container.innerHTML += createViaggioCard(viaggio);
    });
}

async function caricaOfferte() {
    const response = await fetch('api/viaggi/offerte'); // Endpoint che restituisce le offerte
    const offerte = await response.json();
    const container = document.getElementById('offerteCardsContainer');

    // Svuota il contenitore prima di aggiungere le nuove card
    container.innerHTML = '';

    offerte.forEach(offerta => {
        container.innerHTML += createOffertaCard(offerta);
    });
}

function createViaggioCard(viaggio) {
    // Formatta le date in maniera più leggibile (es. dd/mm/yyyy)
    const dataPartenza = new Date(viaggio.dataPartenza).toLocaleDateString();
    const dataRitorno = new Date(viaggio.dataRitorno).toLocaleDateString();

    // Crea la card HTML per il viaggio
    return `
        <div class="col-md-4">
            <div class="card mb-4">
                <img src="/api/viaggi/${viaggio.viaggioId}/immagine" class="card-img-top" alt="Immagine del viaggio">
                <div class="card-body">
                    <h5 class="card-title">${viaggio.nome}</h5>
                    <p class="card-text">
                        Luogo di Partenza: ${viaggio.luogoPartenza} <br>
                        Luogo di Arrivo: ${viaggio.luogoArrivo} <br>
                        Data di Partenza: ${dataPartenza} <br>
                        Data di Ritorno: ${dataRitorno} <br>
                        Prezzo: €${viaggio.prezzo.toFixed(2)}
                    </p>
                    <a href="/paginaViaggio/viaggio?viaggioId=${viaggio.viaggioId}" class="btn btn-primary">Dettagli del viaggio</a>
                </div>
            </div>
        </div>
    `;
}

function createOffertaCard(offerta) {
    // Crea la card HTML per l'offerta
    return `
        <div class="card">
            <img src="${offerta.img}" alt="${offerta.nome}">
            <p>${offerta.nome} <br>A partire da € ${offerta.prezzo}</p>
        </div>
    `;
}

// Carica i viaggi e le offerte quando la pagina è pronta
caricaViaggi();
caricaOfferte();

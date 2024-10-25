/**
 	*Logica per la Pagina iniziale 
 */

window.onload = function() {
    window.location.href = "../Pagina Iniziale/home.html";
};

 // Seleziona gli elementi
const cardsContainer = document.querySelector('.cards');
const leftBtn = document.querySelector('.nav-btn left-btn');
const rightBtn = document.querySelector('.nav-btn right-btn');

// Configura la larghezza della card e quante se ne vedono
const cardWidth = document.querySelector('.card').offsetWidth + 20; // Card width + gap
const visibleCards = 4;
const totalCards = document.querySelectorAll('.card').length;

let currentIndex = 0;

// Funzione per aggiornare la posizione del carosello
function updateCarousel() {
    const maxIndex = totalCards - visibleCards;
    if (currentIndex < 0) currentIndex = 0;
    if (currentIndex > maxIndex) currentIndex = maxIndex;
    
    // Scorre le card verso sinistra o destra
    cardsContainer.style.transform = `translateX(-${currentIndex * cardWidth}px)`;
}

// Eventi per il pulsante destro
rightBtn.addEventListener('click', () => {
    if (currentIndex < totalCards - visibleCards) {
        currentIndex++;
        updateCarousel();
    }
});

// Eventi per il pulsante sinistro
leftBtn.addEventListener('click', () => {
    if (currentIndex > 0) {
        currentIndex--;
        updateCarousel();
    }
});

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

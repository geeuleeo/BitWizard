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

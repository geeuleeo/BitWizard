// Funzione per caricare i dati dell'utente
async function caricaDatiUtente() {
    const response = await fetch('/api/utente/me');
    const data = await response.json();

    document.getElementById('nomeCognome').textContent = `${data.nome} ${data.cognome}`;
    document.getElementById('email').textContent = data.email;
    document.getElementById('descrizione').textContent = data.descrizione;
}

// Funzione principale per caricare i viaggi
async function caricaTuttiIViaggi() {
    try {
        await Promise.all([caricaViaggiUtente(), caricaViaggiCreatiUtente()]);
    } catch (error) {
        console.error('Errore nel caricamento dei viaggi:', error);
    }
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

// Funzione per caricare i viaggi creati dall'utente
async function caricaViaggiCreatiUtente() {
    const response = await fetch(`/api/viaggi/creatore/${0}`); // Sostituisci 0 con l'ID dell'utente loggato
    const viaggi = await response.json();
    const container = document.getElementById('viaggiCreatiContainer');

    viaggi.forEach(viaggio => {
        container.innerHTML += createViaggioCard(viaggio);
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
             <a href="/paginaViaggio/viaggio?viaggioId=${viaggio.viaggioId}" class="viaggio" </a>
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
        const creatoreId = viaggio.getAttribute('data-creatore'); // Assicurati che i viaggi abbiano questo attributo

        if (tipoViaggio === 'prenotati') {
            viaggio.style.display = (dataPartenza > oggi) ? 'block' : 'none';
        } else if (tipoViaggio === 'completati') {
            viaggio.style.display = (dataRitorno < oggi) ? 'block' : 'none';
        } else if (tipoViaggio === 'creati') {
            const utenteLoggatoId = 0; // Sostituisci con l'ID dell'utente loggato
            viaggio.style.display = (creatoreId == utenteLoggatoId) ? 'block' : 'none';
        } else {
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
        },
    });

    if (response.ok) {
        window.location.href = '/'; // Reindirizza alla home page
    } else {
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

function mostraListaAmici() {
    const listaAmiciContainer = document.getElementById('listaAmiciContainer');

    // Se il contenitore è visibile, nascondilo
    if (listaAmiciContainer.style.display === "block") {
        listaAmiciContainer.style.display = "none"; // Nascondi la lista
    } else {
        // Altrimenti, carica la lista amici e mostrala
        fetch(`/api/amicizia/trovaAmici`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => response.json())
        .then(amici => {
            console.log('Lista amici:', amici);
            let listaAmiciHtml = '<ul>';
            amici.forEach(amico => {
                const base64Image = amico.immagine ? `data:image/jpeg;base64,${amico.immagine}` : '/path/to/default/image.png';

                listaAmiciHtml += `
                    <li>
                        <button onclick="vaiAlProfilo(${amico.utenteId})" style="border: none; background: none;">
                            <img src="${base64Image}" alt="Immagine profilo di ${amico.nome}" style="width: 50px; height: 50px; border-radius: 50%; object-fit: cover;">
                            ${amico.nome} ${amico.cognome}
                        </button>
                    </li>`;
            });
            listaAmiciHtml += '</ul>';

            listaAmiciContainer.innerHTML = listaAmiciHtml;

            // Mostra il contenitore
            listaAmiciContainer.style.display = "block"; // Mostra la lista
        })
        .catch(error => {
            console.error('Errore durante il caricamento degli amici:', error);
        });
    }
}

// Funzione per reindirizzare al profilo dell'amico
function vaiAlProfilo(utenteId) {
    window.location.href = `/profiloUtente/${utenteId}`;
}

function gestisciRichiestaAmicizia(utenteInvianteId, accetta, buttonElement) {
    const notificaId = buttonElement.getAttribute("data-id");

    if (!notificaId) {
        console.error("Impossibile trovare notificaId");
        return;
    }

    const action = accetta ? 'accetta' : 'rifiuta';

    fetch(`/api/amicizia/${action}/${utenteInvianteId}/${notificaId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        if (response.ok) {
            alert(`Richiesta d'amicizia ${accetta ? 'accettata' : 'rifiutata'}`);
            caricaNotificheUtente();
        } else {
            alert('Errore nella gestione della richiesta');
        }
    })
    .catch(error => {
        console.error('Errore:', error);
        alert('Errore durante la gestione della richiesta');
    });
}

async function modificaUtente() {
    window.location.href = `/ModificaProfilo`;
}

// Carica i dati al caricamento della pagina
document.addEventListener("DOMContentLoaded", function () {
    caricaDatiUtente();
    caricaTuttiIViaggi();
    mostraListaAmici();

    const modificaBtn = document.getElementById('modificaBtn');
    if (modificaBtn) {
        modificaBtn.addEventListener('click', function(event) {
            event.preventDefault();
            modificaUtente();
        });
    }
});

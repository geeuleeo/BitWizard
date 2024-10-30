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
    const response = await fetch(`/api/viaggi/utente/iscritto/${0}`);
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
    const container = document.getElementById('cardContainer');
    container.innerHTML = '';

    viaggi.forEach(viaggio => {
        container.innerHTML += createViaggioCard(viaggio);
    });
}

// Funzione per caricare i viaggi creati dall'utente
async function caricaViaggiFinitiUtente() {
    const response = await fetch(`/api/viaggi/utente/finito/${0}`); // Sostituisci 0 con l'ID dell'utente loggato
    const viaggi = await response.json();
    const container = document.getElementById('cardContainer');
    container.innerHTML = '';

    viaggi.forEach(viaggio => {
        container.innerHTML += createViaggioCard(viaggio);
    });
}


// Funzione per creare una card di Bootstrap per un viaggio
function createViaggioCard(viaggio) {
    const dataPartenza = new Date(viaggio.dataPartenza).toLocaleDateString();
    const dataRitorno = new Date(viaggio.dataRitorno).toLocaleDateString();

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

// Function to render notifications
        async function caricaNotificheUtente() {
            const response = await fetch('/notifica/cerca'); // Endpoint che restituisce i dati dell'utente loggato
            const notifiche = await response.json();
            const container = document.getElementById('notificheContainer');
            container.innerHTML = ''; // Clear existing content

            // Crea una card HTML per ogni notifica
            notifiche.forEach(notifica => {
                const cardHtml = createNotificaCard(notifica);
                container.insertAdjacentHTML('beforeend', cardHtml);
                console.log(parseInt(notifica.notificaId));
            });
        }

        // Add event listener to the container (Event delegation)
        document.getElementById('notificheContainer').addEventListener('click', function(event) {
            if (event.target && event.target.matches('button.cancella-notifica')) {
                const notificaId = event.target.getAttribute('data-id'); // Usa l'attributo data-id per recuperare l'ID
                cancellaNotifica(Number(notificaId));
            }
        });

        // Funzione per cancellare la notifica
        function cancellaNotifica(notificaId) {
            fetch(`/notifica/cancella?notificaId=${notificaId}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    // Rimuovi la card della notifica dal DOM
                    const notificaElement = document.getElementById(`notifica-${notificaId}`);
                    caricaNotificheUtente();
                    if (notificaElement) {
                        notificaElement.remove();
                    }
                } else {
                    console.error('Errore nella cancellazione:', response.statusText);
                }
            })
            .catch(error => console.error('Errore nella cancellazione:', error));
        }

function createNotificaCard(notifica) {
            const dataInvio = new Date(notifica.data).toLocaleString('it-IT', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric'
            });

            // HTML della card con icona e pulsanti migliorati
            return `
                <div id="notificaCard" class="col-md-4" id="notifica-${notifica.notificaId}">
                    <div class="card mb-4 shadow-sm border-primary">
                        <div class="card-body">
                            <h5 class="card-title">
                                <i class="bi bi-person-plus-fill text-primary"></i> Notifica
                            </h5>
                            <p class="card-text">
                                ${notifica.testo} <br>
                                <small class="text-muted">Data: ${dataInvio}</small>
                            </p>
                            <button data-id="${notifica.notificaId}" class="btn btn-primary btn-block cancella-notifica">Segna come letto</button>
                        </div>
                    </div>
                </div>
                </div>
            `;
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

function mostraListaNotifiche() {
    const listaAmiciContainer = document.getElementById('notificheContainer');

    // Se il contenitore è visibile, nascondilo
    if (listaAmiciContainer.style.display === "block") {
        listaAmiciContainer.style.display = "none"; // Nascondi la lista
    } else {
		listaAmiciContainer.style.display = "block";
        caricaNotificheUtente();
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
            Swal.fire({
                icon: 'success',
                title: `Richiesta d'amicizia ${accetta ? 'accettata' : 'rifiutata'}`,
                confirmButtonText: 'OK'
            })
            caricaNotificheUtente();
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Errore nella gestione della richiesta',
                confirmButtonText: 'OK'
            })
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
    caricaViaggiUtente();

    const modificaBtn = document.getElementById('modificaBtn');
    if (modificaBtn) {
        modificaBtn.addEventListener('click', function(event) {
            event.preventDefault();
            modificaUtente();
        });
    }
});
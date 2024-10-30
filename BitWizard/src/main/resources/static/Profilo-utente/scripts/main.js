 // Funzione per caricare i dati dell'utente
        async function caricaDatiUtente() {
        	
        	const pathSegments = window.location.pathname.split('/');
        	const utenteId = pathSegments[pathSegments.length - 1];  // L'ultimo segmento dell'URL è l'ID utente

        	console.log(utenteId);
        	
        	document.getElementById('aggiungiAmicoBtn').setAttribute('data-utente-id', utenteId);
        	
            const response = await fetch(`/api/utente/profilo/${utenteId}`); // Endpoint che restituisce i dati dell'utente loggato
            const data = await response.json();
            
            const imgResponse = await fetch(`/api/immagini/utente/profilo/${utenteId}`);
    		const imgBlob = await imgResponse.blob();

    		// Crea un URL temporaneo per visualizzare l'immagine
    		const imgUrl = URL.createObjectURL(imgBlob);
    		document.getElementById('imgProfilo').src = imgUrl;

			document.getElementById('titolo').textContent = 'Profilo di ' + data.nome + ' ' + data.cognome;
            document.getElementById('nomeCognome').textContent = data.nome + ' ' + data.cognome;
            document.getElementById('email').textContent = data.email;
            document.getElementById('descrizione').textContent = data.descrizione;
        }

        // Funzione per caricare i viaggi dell'utente
        async function caricaViaggiFinitiUtente() {
        	const utenteIdTarget = document.getElementById('aggiungiAmicoBtn').getAttribute('data-utente-id');
            const response = await fetch(`/api/viaggi/utente/finito/${utenteIdTarget}`); // Endpoint che restituisce i viaggi dell'utente
            const viaggi = await response.json();
            const container = document.getElementById('cardContainer');
            container.innerHTML = '';

            viaggi.forEach(viaggio => {
                container.innerHTML += createViaggioCard(viaggio);
            });
        }
        
     // Funzione per caricare i viaggi dell'utente
        async function caricaViaggiCreatiUtente() {
        	const utenteIdTarget = document.getElementById('aggiungiAmicoBtn').getAttribute('data-utente-id');
            const response = await fetch(`/api/viaggi/creatore/${utenteIdTarget}`);
            const viaggi = await response.json();
            const container = document.getElementById('cardContainer');
            container.innerHTML = '';

            viaggi.forEach(viaggio => {
                container.innerHTML += createViaggioCard(viaggio);
            });
        }
     
        async function caricaViaggiIscrittiUtente() {
        	const utenteIdTarget = document.getElementById('aggiungiAmicoBtn').getAttribute('data-utente-id');
            const response = await fetch(`/api/viaggi/utente/iscritto/${utenteIdTarget}`);
            const viaggi = await response.json();
            const container = document.getElementById('cardContainer');
            container.innerHTML = '';

            viaggi.forEach(viaggio => {
                container.innerHTML += createViaggioCard(viaggio);
            });
        }

        // Funzione per creare una card di Bootstrap per un viaggio
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

function mostraListaAmici() {
    const listaAmiciContainer = document.getElementById('listaAmiciContainer');
    const utenteIdTarget = document.getElementById('aggiungiAmicoBtn').getAttribute('data-utente-id');

    // Se il contenitore è visibile, nascondilo
    if (listaAmiciContainer.style.display === "block") {
        listaAmiciContainer.style.display = "none"; // Nascondi la lista
    } else {
        // Altrimenti, carica la lista amici e mostrala
        fetch(`/api/amicizia/trovaAmiciNonLoggato/${utenteIdTarget}`, {
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
        
        async function verificaAmicizia() {
        	const utenteIdTarget = document.getElementById('aggiungiAmicoBtn').getAttribute('data-utente-id');
        	
        	console.log('utenteTargetId: ' + utenteIdTarget);
        	
            const response = await fetch(`/api/amicizia/controllo/${utenteIdTarget}`);
            if (response.ok) {
                const esisteAmicizia = await response.json();
                if (esisteAmicizia) {
                    // Nascondi il pulsante se esiste già un'amicizia
                    document.getElementById('aggiungiAmicoBtn').style.display = 'none';
                } else {
                    // Mostra il pulsante se non esiste un'amicizia
                    document.getElementById('aggiungiAmicoBtn').style.display = 'block';
                }
            } else {
                console.error("Errore nel controllo dell'amicizia.");
            }
        }

        // Funzione per reindirizzare al profilo dell'amico
        function vaiAlProfilo(utenteId) {
            window.location.href = `/profiloUtente/${utenteId}`;
        }
        
        function inviaRichiestaAmicizia() {
        	 const utenteIdTarget = document.getElementById('aggiungiAmicoBtn').getAttribute('data-utente-id');
                // Effettua una richiesta per inviare la richiesta di amicizia
                fetch(`/api/amicizia/inviaRichiesta/${utenteIdTarget}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    }
                })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                			icon: 'success',
                			title: 'Richiesta di amicizia inviata con successo!',
                			confirmButtonText: 'OK'
            			})
                        // Aggiorna lo stato del bottone, ad esempio per nascondere il pulsante di invio
                        document.getElementById('aggiungiAmicoBtn').style.display = 'none';
                    } else {
                        Swal.fire({
                			icon: 'error',
                			title: 'Errore durante l\'invio della richiesta di amicizia.',
                			confirmButtonText: 'OK'
            			})
                    }
                })
                .catch(error => {
                    console.error('Errore durante la richiesta:', error);
                    alert('Si è verificato un errore. Riprova più tardi.');
                });
        }

        // Chiamate per caricare i dati all'avvio
        caricaDatiUtente();
        caricaViaggiFinitiUtente();
        caricaViaggiCreatiUtente();
        caricaViaggiIscrittiUtente();
        verificaAmicizia();
 const viaggioId = new URLSearchParams(window.location.search).get('viaggioId');
    
    console.log(viaggioId);

    // Funzione per formattare le date
    function formatDate(dateString) {
        const options = { year: 'numeric', month: 'long', day: 'numeric' };
        const date = new Date(dateString);
        return date.toLocaleDateString('it-IT', options);
    }

    function aggiornaDettagliViaggio(viaggio) {
        document.getElementById('nomeViaggio').textContent = viaggio.nome || "Nome non disponibile";
        document.getElementById('luogoPartenza').textContent = viaggio.luogoPartenza || "Non disponibile";
        document.getElementById('luogoArrivo').textContent = viaggio.luogoArrivo || "Non disponibile";
        document.getElementById('dataPartenza').textContent = formatDate(viaggio.dataPartenza) || "Data non disponibile";
        document.getElementById('dataRitorno').textContent = formatDate(viaggio.dataRitorno) || "Data non disponibile";
        document.getElementById('prezzo').textContent = viaggio.prezzo ? viaggio.prezzo.toFixed(2) : "Prezzo non disponibile";
        document.getElementById('etaMin').textContent = viaggio.etaMin || "N/A";
        document.getElementById('etaMax').textContent = viaggio.etaMax || "N/A";
        document.getElementById('numPartMin').textContent = viaggio.numPartMin || "N/A";
        document.getElementById('numPartMax').textContent = viaggio.numPartMax || "N/A";
        document.getElementById('nomeCreatore').textContent = `${viaggio.nomeCreatore} ${viaggio.cognomeCreatore}`;
    }
    
    async function caricaDettagliViaggio(viaggioId) {
        console.log('Viaggio ID ricevuto: ', viaggioId);
        
        // Effettua la richiesta al server per ottenere i dettagli del viaggio
        const response = await fetch(`/viaggio/${viaggioId}`);
        
        if (!response.ok) {
            console.error('Errore nel caricamento del viaggio');
            return;
        }

        const viaggio = await response.json();
        
        console.log('id creatore' + viaggio.creatoreId);
        
        bottoneIscrizioneViaggio(viaggioId, viaggio.creatoreId);
        
        // Mostra le immagini del viaggio
        const immaginiViaggioDiv = document.getElementById('immaginiViaggio');
        immaginiViaggioDiv.innerHTML = '';  // Pulisci eventuali contenuti precedenti
        if (viaggio.immagini && viaggio.immagini.length > 0) {
            viaggio.immagini.forEach(immagine => {
                const img = document.createElement('img');
                img.src = `data:image/jpeg;base64,${immagine}`;
                img.alt = 'Immagine del viaggio';
                img.className = 'img-fluid col-md-4';  // Aggiungi classi di stile se necessario
                immaginiViaggioDiv.appendChild(img);
            });
        } else {
            console.log("Nessuna immagine disponibile per il viaggio.");
        }

        // Mostra i tag del viaggio
        const tagsViaggioDiv = document.getElementById('tagsViaggio');
        tagsViaggioDiv.innerHTML = '';  // Pulisci eventuali contenuti precedenti
        if (viaggio.tagDTOs && viaggio.tagDTOs.length > 0) {
            viaggio.tagDTOs.forEach(tag => {
                const tagElement = document.createElement('div');
                tagElement.className = 'badge badge-primary';  // Aggiungi classi di stile se necessario
                tagElement.textContent = tag.tipoTag;
                tagsViaggioDiv.appendChild(tagElement);
            });
        } else {
            console.log("Nessun tag trovato per il viaggio con ID:", viaggio.viaggioId);
        }
		
		 const listaPartecipanti = document.getElementById('listaPartecipanti');
            listaPartecipanti.innerHTML = '';

           if (viaggio.partecipanti && viaggio.partecipanti.length > 0) {
    		viaggio.partecipanti.forEach(partecipante => {
                    // Crea un elemento <div> per il partecipante
                    const partecipanteDiv = document.createElement('div');
                    partecipanteDiv.className = 'd-flex align-items-center mb-3';

                    // Crea l'immagine del partecipante come pulsante
                    const immaginePartecipante = document.createElement('img');
                    immaginePartecipante.className = 'rounded-circle me-3';
                    immaginePartecipante.style.width = '80px';
                    immaginePartecipante.style.height = '80px';
                    immaginePartecipante.alt = 'Immagine profilo utente';

                    // Imposta la sorgente dell'immagine in base alla presenza di utenteId
                    immaginePartecipante.src = partecipante.utenteId 
                        ? `/api/immagini/utente/profilo/${partecipante.utenteId}`
                        : `/immagini/default-profilo.png`;

                    // Aggiungi un evento al click per andare al profilo
                    immaginePartecipante.style.cursor = 'pointer';
                    immaginePartecipante.onclick = () => vaiAlProfilo(partecipante.utenteId);

                    // Crea un elemento <span> per il nome del partecipante
                    const nomePartecipante = document.createElement('span');
                    nomePartecipante.textContent = `${partecipante.nome} ${partecipante.cognome}`;

                    // Aggiungi l'immagine e il nome al div del partecipante
                    partecipanteDiv.appendChild(immaginePartecipante);
                    partecipanteDiv.appendChild(nomePartecipante);

                    // Aggiungi il div del partecipante alla lista dei partecipanti
                    listaPartecipanti.appendChild(partecipanteDiv);
                });
            } else {
                console.log("Nessun partecipante disponibile per il viaggio.");
            }
            
		 // Funzione per reindirizzare al profilo dell'amico
        function vaiAlProfilo(utenteId) {
            window.location.href = `/profiloUtente/${utenteId}`;
        }

        // Mostra l'immagine di copertina
        const copertinaImg = document.getElementById('immagineCopertina');
        if (viaggio.immagineCopertina) {
            copertinaImg.src = `data:image/jpeg;base64,${viaggio.immagineCopertina}`;
            copertinaImg.alt = 'Copertina del viaggio';
        } else {
            console.log("Nessuna immagine di copertina trovata.");
        }

        // Mostra l'immagine del creatore
        const immagineCreatore = document.getElementById('immagineCreatore');
        if (viaggio.immagineProfiloCreatore) {
            immagineCreatore.src = `data:image/jpeg;base64,${viaggio.immagineProfiloCreatore}`;
            immagineCreatore.alt = 'Immagine profilo del creatore';
        } else {
            console.log("Nessuna immagine del creatore trovata.");
        }

        // Mostra il nome del creatore
        document.getElementById('nomeCreatore').textContent = `${viaggio.nomeCreatore} ${viaggio.cognomeCreatore}`;
        
        aggiornaDettagliViaggio(viaggio);
    }
    
async function aggiornaPartecipanti(viaggioId) {
    try {
        // Effettua la richiesta per ottenere la lista aggiornata dei partecipanti
        const response = await fetch(`/viaggio/${viaggioId}/partecipanti`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (response.ok) {
            const partecipanti = await response.json();

            // Pulisci la lista dei partecipanti nel DOM
            const listaPartecipanti = document.getElementById('listaPartecipanti');
            listaPartecipanti.innerHTML = '';  // Rimuove i partecipanti precedenti

            if (partecipanti && partecipanti.length > 0) {
                // Aggiungi i nuovi partecipanti con foto e nome
                partecipanti.forEach(partecipante => {
                    // Crea un elemento <div> per il partecipante
                    const partecipanteDiv = document.createElement('div');
                    partecipanteDiv.className = 'd-flex align-items-center mb-3';

                    // Crea l'immagine del partecipante come pulsante
                    const immaginePartecipante = document.createElement('img');
                    immaginePartecipante.className = 'rounded-circle me-3';
                    immaginePartecipante.style.width = '80px';
                    immaginePartecipante.style.height = '80px';
                    immaginePartecipante.alt = 'Immagine profilo utente';

                    // Imposta la sorgente dell'immagine in base alla presenza di utenteId
                    immaginePartecipante.src = partecipante.utenteId 
                        ? `/api/immagini/utente/profilo/${partecipante.utenteId}`
                        : `/immagini/default-profilo.png`;

                    // Aggiungi un evento al click per andare al profilo
                    immaginePartecipante.style.cursor = 'pointer';
                    immaginePartecipante.onclick = () => vaiAlProfilo(partecipante.utenteId);

                    // Crea un elemento <span> per il nome del partecipante
                    const nomePartecipante = document.createElement('span');
                    nomePartecipante.textContent = `${partecipante.nome} ${partecipante.cognome}`;

                    // Aggiungi l'immagine e il nome al div del partecipante
                    partecipanteDiv.appendChild(immaginePartecipante);
                    partecipanteDiv.appendChild(nomePartecipante);

                    // Aggiungi il div del partecipante alla lista dei partecipanti
                    listaPartecipanti.appendChild(partecipanteDiv);
                });
            } else {
                console.log("Nessun partecipante disponibile per il viaggio.");
            }
        } else {
            console.error("Errore nel caricamento dei partecipanti.");
        }
    } catch (error) {
        console.error("Errore durante la richiesta:", error);
    }
}

    function setFormAction(viaggioId) {
        const form = document.getElementById('iscrivitiForm');
        form.action = `/api/viaggi/iscriviti/${viaggioId}`;
    }

    async function iscrivitiAlViaggio(viaggioId) {
        setFormAction(viaggioId);
        console.log('Id viaggio inviato per l iscrizione: ' + viaggioId);
        try {
            const response = await fetch(`/api/viaggi/iscriviti/${viaggioId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.ok) {
            	aggiornaPartecipanti(viaggioId);
                alert('Iscrizione avvenuta con successo!');     
            } else {
                alert('Errore durante l\'iscrizione');
            }
        } catch (error) {
            console.error('Errore durante la richiesta:', error);
            alert('Si è verificato un errore');
        }
    }

    async function annullaIscrivitiAlViaggio(viaggioId) {
        console.log('Id viaggio inviato per l\'annullamento iscrizione: ' + viaggioId);
        try {
            const response = await fetch(`/api/viaggi/iscriviti/rimuovi/${viaggioId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.ok) {
                aggiornaPartecipanti(viaggioId);
                alert('Annullamento Iscrizione avvenuto con successo!');     
            } else {
                alert('Errore durante l\'annullamento dell\'iscrizione');
            }
        } catch (error) {
            console.error('Errore durante la richiesta:', error);
            alert('Si è verificato un errore');
        }
    }

    // Aggiungi l'evento click per il pulsante di annullamento iscrizione
    document.getElementById('annullaIscrizioneBtn').addEventListener('click', function(event) {
        event.preventDefault();
        if (viaggioId) {
            annullaIscrivitiAlViaggio(viaggioId);
        } else {
            console.error('Errore: Viaggio ID non disponibile.');
        }
    });

    document.getElementById('iscrivitiBtn').addEventListener('click', function(event) {
        event.preventDefault();  // Evita il submit del form nativo
        if (viaggioId) {
            iscrivitiAlViaggio(viaggioId);
        } else {
            console.error('Errore: Viaggio ID non disponibile.');
        }
    });
    
    async function annullaViaggio(viaggioId) {
        setFormAction(viaggioId);
        console.log('Id viaggio inviato per l annullamento: ' + viaggioId);
        try {
            const response = await fetch(`/api/viaggi/annulla/${viaggioId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.ok) {
            	aggiornaPartecipanti(viaggioId);
                alert('Annullamento avvenuta con successo!');     
            } else {
                alert('Errore durante l\'annullamento');
            }
        } catch (error) {
            console.error('Errore durante la richiesta:', error);
            alert('Si è verificato un errore');
        }
    }
    
    async function modificaViaggio(viaggioId) {
        // Reindirizza l'utente alla pagina di modifica del viaggio
        window.location.href = `/ModificaViaggio?viaggioId=${viaggioId}`;
    }

    document.getElementById('modificaBtn').addEventListener('click', function(event) {
        event.preventDefault(); // Evita il comportamento predefinito del form
        
        console.log("id del viaggio da modificare: " + viaggioId)

        if (viaggioId) {
            modificaViaggio(viaggioId); // Funzione per reindirizzare l'utente alla pagina di modifica
        } else {
            console.error('Errore: Viaggio ID non disponibile per la modifica.');
        }
    });

        // Aggiungi l'evento per il pulsante di annullamento
        document.getElementById('annullaBtn').addEventListener('click', function(event) {
            event.preventDefault(); // Evita il comportamento predefinito del form
            if (viaggioId) {
                annullaViaggio(viaggioId); // Funzione per annullare il viaggio
            } else {
                console.error('Errore: Viaggio ID non disponibile per l\'annullamento.');
            }
        });

    // Carica i dettagli del viaggio quando la pagina viene caricata
    if (viaggioId) {
        caricaDettagliViaggio(viaggioId);  // Chiamata per caricare i dettagli del viaggio
    } else {
        console.error('Errore: Viaggio ID non disponibile.');
    }
    
    async function bottoneIscrizioneViaggio(viaggioId, creatoreId) {
    	console.log('viaggioId ricevuto da aggiorna bottone ' + viaggioId);
        try {
            // Effettua la richiesta al server per ottenere l'ID dell'utente loggato
            const response = await fetch(`/api/utente/session`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.ok) {
                const utenteLoggato = await response.json();  // Ottieni l'ID dell'utente loggato dal server

                console.log('ID utente loggato:', utenteLoggato);
                console.log('ID creatore del viaggio:', creatoreId);

                // Confronta gli ID convertendo entrambi in numeri (se necessario)
                if (parseInt(utenteLoggato) === parseInt(creatoreId)) {
                    // Nascondi il pulsante di iscrizione se l'utente è il creatore
                    document.getElementById('iscrivitiBtn').style.display = 'none';
                    // Mostra i pulsanti del creatore
                    document.getElementById('pulsantiCreatore').style.display = 'block';
                    document.getElementById('ChatDelViaggio').style.display = 'block';
                } else {
                	
                    // Mostra il pulsante di iscrizione se l'utente non è il creatore
                    document.getElementById('iscrivitiBtn').style.display = 'block';
                    // Nascondi i pulsanti del creatore
                    document.getElementById('pulsantiCreatore').style.display = 'none';
                    console.log('controllo iscrizione con viaggioId:', viaggioId);
                    // Passa correttamente viaggioId alla funzione verificaIscrizioneUtente
                    verificaIscrizioneUtente(viaggioId, utenteLoggato);
                }
            } else {
                console.error('Errore nel recupero della sessione utente');
            }
        } catch (error) {
            console.error('Errore durante la richiesta:', error);
        }
    }
    
    async function verificaIscrizioneUtente(viaggioId, utenteLoggato) {
        try {
            // Controlla se il viaggio è al completo
            const viaggioCompletoResponse = await fetch(`/viaggio/completo/${viaggioId}`);
            const isViaggioCompleto = await viaggioCompletoResponse.json();

            if (isViaggioCompleto) {
                console.log('Viaggio completo, nascondo il pulsante di iscrizione');
                document.getElementById('iscrivitiBtn').style.display = 'none';
                document.getElementById('messaggioCompleto').style.display = 'block'; // Mostra un messaggio "Il viaggio è completo"
                return; // Esci dalla funzione se il viaggio è completo
            } else {
                document.getElementById('messaggioCompleto').style.display = 'none'; // Nascondi il messaggio
            }

            // Controlla se il viaggio è terminato
            const viaggioTerminatoResponse = await fetch(`/viaggio/terminato/${viaggioId}`);
            const isViaggioTerminato = await viaggioTerminatoResponse.json();

            // Controlla se l'utente è già iscritto al viaggio
            const response = await fetch(`/viaggio/${viaggioId}/partecipanti`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.ok) {
                const partecipanti = await response.json();

                console.log('Partecipanti ricevuti:', partecipanti);
                console.log('Utente loggato:', utenteLoggato);

                // Controlla se l'utente è già iscritto
                const isUtenteIscritto = partecipanti.some(partecipante => parseInt(partecipante.utenteId) === parseInt(utenteLoggato));

                if (isUtenteIscritto) {
                    console.log('Utente iscritto');
                    document.getElementById('iscrivitiBtn').style.display = 'none';
                    document.getElementById('annullaIscrizioneBtn').style.display = 'block';
                    document.getElementById('ChatDelViaggio').style.display = 'block';
                    document.getElementById('messaggioForm').style.display = 'block';

                    // Mostra o nascondi il form di recensione in base allo stato del viaggio
                    if (isViaggioTerminato) {
                        document.getElementById('recensioneForm').style.display = 'block';
                        document.getElementById('mostraRecensioni').style.display = 'block';
                    } else {
                        document.getElementById('recensioneForm').style.display = 'none';
                        document.getElementById('mostraRecensioni').style.display = 'none';
                    }
                } else {
                    console.log('Utente non iscritto');
                    document.getElementById('iscrivitiBtn').style.display = 'block';
                    document.getElementById('annullaIscrizioneBtn').style.display = 'none';
                    document.getElementById('ChatDelViaggio').style.display = 'none';
                    document.getElementById('recensioneForm').style.display = 'none'; // Nascondi sempre il form di recensione se l'utente non è iscritto
                }

                // Mostra il pulsante per vedere le recensioni a tutti
                document.getElementById('mostraRecensioni').style.display = 'block';
            } else {
                console.error('Errore nel caricamento dei partecipanti.');
            }
        } catch (error) {
            console.error('Errore durante la richiesta:', error);
        }
    }
    
const utentiCache = {};

async function caricaMessaggi(viaggioId) {
    try {
        const response = await fetch(`/carica/messaggi/${viaggioId}`);
        if (!response.ok) throw new Error(`Errore nel caricamento dei messaggi: ${await response.text()}`);

        const nuoviMessaggi = await response.json();
        visualizzaMessaggi(nuoviMessaggi);

    } catch (error) {
        console.error("Errore nel caricamento dei messaggi:", error);
    }
}

async function visualizzaMessaggi(messaggi) {
    const container = document.querySelector('.chat-container');
    container.innerHTML = ''; // Svuota il contenitore

    const messaggiHTML = await Promise.all(messaggi.map(createMessaggioCard));
    container.innerHTML = messaggiHTML.reverse().join(''); // Inverti l'ordine per avere il più recente in fondo

    // Imposta lo scroll alla fine
    container.scrollTop = container.scrollHeight;
}

// Funzione per scorrere automaticamente alla fine ogni volta che viene aggiunto un nuovo messaggio
function scrollToBottom(container) {
    container.scrollTop = container.scrollHeight;
}

// Funzione per creare la card di un messaggio
async function createMessaggioCard(messaggio) {
    const dataInvio = new Date(messaggio.data).toLocaleString('it-IT', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    });

    let utenteNome = 'Anonimo';
    let utenteCognome = '';

    if (messaggio.utenteId && !utentiCache[messaggio.utenteId]) {
        try {
            const response = await fetch(`/api/utente/nome/${messaggio.utenteId}`);
            const utente = await response.json();
            utentiCache[messaggio.utenteId] = {
                nome: utente.nome || 'Anonimo',
                cognome: utente.cognome || '',
            };
        } catch (error) {
            console.error("Errore nel recupero dei dati dell'utente", error);
        }
    }

    if (utentiCache[messaggio.utenteId]) {
        utenteNome = utentiCache[messaggio.utenteId].nome;
        utenteCognome = utentiCache[messaggio.utenteId].cognome;
    }

    const immagineHTML = messaggio.immagineId
        ? `<img src="/api/immagini/immagine/${messaggio.immagineId}" class="img-fluid rounded mb-3" alt="Immagine messaggio">`
        : '';

    const immagineProfilo = messaggio.utenteId
        ? `<img src="/api/immagini/utente/profilo/${messaggio.utenteId}" class="rounded-circle me-3" alt="Immagine profilo utente" style="width: 50px; height: 50px;">`
        : `<img src="/immagini/default-profilo.png" class="rounded-circle me-3" alt="Immagine profilo di default" style="width: 50px; height: 50px;">`;

    return `
        <div class="d-flex mb-4 align-items-start">
            ${immagineProfilo}
            <div class="p-3 rounded shadow-sm" style="background-color: #f8f9fa; max-width: 75%;">
                <h6 class="mb-1">${utenteNome} ${utenteCognome}</h6>
                <small class="text-muted">${dataInvio}</small>
                <p class="mb-1">${messaggio.testo}</p>
                ${immagineHTML}
            </div>
        </div>
    `;
}

caricaMessaggi(viaggioId);
 	
 	document.getElementById('messaggioForm').addEventListener('submit', async function(event) {
 	    event.preventDefault();  // Previene il comportamento predefinito del form (il refresh della pagina)

 	    const form = document.getElementById('messaggioForm');
 	    const formData = new FormData(form);

 	    // Dati del messaggio
 	    const messaggioDTO = {
 	        testo: formData.get('testo'),
 	        viaggioId: viaggioId
 	    };

 	    // Aggiungi messaggioDTO come JSON nel FormData
 	    formData.append('messaggioDTO', new Blob([JSON.stringify(messaggioDTO)], {
 	        type: 'application/json'
 	    }));

 	    try {
 	        const response = await fetch('/crea/messaggio', {
 	            method: 'POST',
 	            body: formData
 	        });

 	        const result = await response.json();
 	        if (response.ok) {
 	            document.getElementById('result').innerText = 'Messaggio inviato con successo!';
 	            caricaMessaggi(viaggioId);
 	        } else {
 	            document.getElementById('result').innerText = 'Errore: ' + result.error;
 	        }
 	    } catch (error) {
 	        document.getElementById('result').innerText = 'Errore nella richiesta: ' + error;
 	    }
 	});
 	
 	document.getElementById('recensioneForm').addEventListener('submit', function(event) {
 	    event.preventDefault(); // Impedisce il comportamento predefinito del form

 	    const testo = document.getElementById('testoRecensione').value;
 	    const rating = document.querySelector('input[name="rating"]:checked').value;

 	    // Crea l'oggetto recensioneDTO che verrà inviato al controller
 	    const recensioneDTO = {
 	        testo: testo,
 	        rating: rating,
 	        viaggioId: viaggioId
 	    };

 	    // Invia i dati via AJAX con l'intestazione 'application/json'
 	    fetch('/recensione/crea', {
 	        method: 'POST',
 	        headers: {
 	            'Content-Type': 'application/json'
 	        },
 	        body: JSON.stringify(recensioneDTO) // Serializza l'oggetto recensioneDTO in JSON
 	    })
 	    .then(response => response.json())
 	    .then(data => {
 	        console.log(data); // Visualizza la risposta del server
 	        alert('Recensione inviata con successo!');
 	        caricaRecensioni(viaggioId);
 	    })
 	    .catch(error => console.error('Errore:', error));
 	});
 	
 	document.getElementById('mostraRecensioni').addEventListener('click', function() {
 		
 	    fetch(`/recensione/viaggio/${viaggioId}`)
 	    .then(response => response.json())
 	    .then(data => {
 	        const recensioniContainer = document.getElementById('recensioniContainer');
 	        recensioniContainer.innerHTML = '';

 	        if (data.length === 0) {
 	            recensioniContainer.innerHTML = '<p>Non ci sono recensioni per questo viaggio.</p>';
 	        } else {
 	        // Usa for...of per iterare sugli oggetti asincroni
 	           for (let messaggio of messaggi) {
 	               const messaggioCard = createMessaggioCard(messaggio);  // Attendi che la card venga creata
 	               container.innerHTML += messaggioCard;  // Aggiungi la card al container
 	           }
 	        }
 	    })
 	    .catch(error => console.error('Errore nel recuperare le recensioni:', error));
 	});
 	
 	document.getElementById('mostraRecensioni').addEventListener('click', function() {
 		console.log(viaggioId)
 	    // Chiama la funzione per caricare le recensioni, passando il viaggioId
 	    caricaRecensioni(viaggioId);
 	});
 	
 	async function caricaRecensioni(viaggioId) {
 	    try {
 	        const response = await fetch(`/recensione/viaggio/${viaggioId}`);
 	        const recensioni = await response.json();
 	        const recensioniContainer = document.getElementById('recensioneContainer');
 	        recensioniContainer.innerHTML = '';  // Pulisce il contenitore precedente

 	        if (recensioni.length === 0) {
 	            recensioniContainer.innerHTML = '<p>Non ci sono recensioni per questo viaggio.</p>';
 	        } else {
 	            for (let recensione of recensioni) {
 	                const cardHTML = await creaCardRecensione(recensione);
 	                recensioniContainer.innerHTML += cardHTML;  // Inserisce ogni card nel contenitore
 	            }
 	        }
 	    } catch (error) {
 	        console.error("Errore nel caricamento delle recensioni", error);
 	    }
 	}
 	
 	async function creaCardRecensione(recensione) {
 		
 		const dataInvio = new Date(recensione.data).toLocaleString('it-IT', {
    		day: '2-digit',
    		month: '2-digit',
    		year: 'numeric'
		});
 		
 		if (recensione.utenteId) {
            try {
                // Correggi l'interpolazione dell'URL con i backtick
                const response = await fetch(`/api/utente/nome/${recensione.utenteId}`);
                const utente = await response.json();
                utenteNome = utente.nome ? utente.nome : 'Anonimo';  // Verifica se l'utente ha un nome
                utenteCognome = utente.cognome ? utente.cognome : '';  // Verifica se l'utente ha un cognome
            } catch (error) {
                console.error('Errore nel recupero dei dati dell\'utente', error);
            }
        }
 		
 	    return `
 	        <div class="col-md-4">
 	            <div class="card mb-4">
 	                <img src="/api/immagini/utente/profilo/${recensione.utenteId}" class="card-img-top" alt="Immagine profilo utente">
 	                <div class="card-body">
 	                    <h5 class="card-title">${utenteNome} ${utenteCognome}</h5>
 	                    <p class="card-text">
 	                        ${recensione.testo} <br>
 	                        Rating: ${recensione.rating} <br>
 	                        <small class="text-muted">Data: ${dataInvio}</small>
 	                    </p>
 	                </div>
 	            </div>
 	        </div>
 	    `;
 	}
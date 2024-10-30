 async function fetchData(url) {
    const response = await fetch(url);

    if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText);
    }

    return await response.json();
    }

    function createViaggioCard(viaggio) {
	    const dataPartenza = new Date(viaggio.dataPartenza).toLocaleDateString();
	    const dataRitorno = new Date(viaggio.dataRitorno).toLocaleDateString();

	    return `
	      <div class="col-md-4">
	        <div class="card mb-4">
	          <img id="card-img" src="/api/viaggi/${viaggio.viaggioId}/immagine" class="card-img-top" alt="Immagine del viaggio">
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

	  function displayResults(results) {
	    const resultsContainer = document.getElementById('results');
	    resultsContainer.innerHTML = results.map(createViaggioCard).join('');
	  }

    async function getTags() {
        try {

            const userResponse = await fetch('/api/utente/session', {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!userResponse.ok) {
                throw new Error('Errore nel recupero dell\'ID utente');
            }


            const utenteId =  await userResponse.json();

            const response = await fetch(`/api/tags/get?utenteId=${utenteId}`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json'
                }
            });


            if (response.ok) {
                const tags = await response.json();

                if (tags.length > 0) {
                    const tagIds = tags.map(tag => tag.tagId);
                    const randomId = tagIds[Math.floor(Math.random() * tagIds.length)];

                    const viaggiResponse = await fetch(`/api/viaggi/filtra/tag?tagId=${randomId}`, {
                        method: 'GET',
                        credentials: 'include',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });

                    if (viaggiResponse.ok) {
                        const viaggi = await viaggiResponse.json();
                        const consigliatiDiv = document.getElementById('consigliati');
                        consigliatiDiv.innerHTML = '';

                        const risultatiDaMostrare = viaggi.slice(0, 3);

                        risultatiDaMostrare.forEach(viaggio => {
                            consigliatiDiv.innerHTML += createViaggioCard(viaggio);
                        })

                    } else {
                        console.error('Errore durante il recupero dei viaggi:', viaggiResponse.status);
                    }
                } else {

                    const randomDefaultId = [Math.floor(Math.random() * 7)];

                    const viaggiResponse = await fetch(`/api/viaggi/filtra/tag?tagId=${randomDefaultId}`, {
                        method: 'GET',
                        credentials: 'include',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });

                    if (viaggiResponse.ok) {
                        const viaggi = await viaggiResponse.json();
                        const consigliatiDiv = document.getElementById('consigliati');
                        consigliatiDiv.innerHTML = '';

                        const risultatiDaMostrare = viaggi.slice(0, 3);

                        risultatiDaMostrare.forEach(viaggio => {
                            consigliatiDiv.innerHTML += createViaggioCard(viaggio);
                        })

                    } else {
                        console.error('Errore durante il recupero dei viaggi:', viaggiResponse.status);
                    }


                }
            } else {
                console.error('Errore durante il recupero dei tag:', response.status);
            }
        } catch (error) {
            console.error('Errore durante la fetch:', error);
        }
    }
    
    async function bottoneLogin() {
        try {
            // Effettua la richiesta al server per ottenere l'ID dell'utente loggato
            const response = await fetch(`/api/utente/session`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.ok) {
                const utenteLoggato = await response.json(); // Ottieni l'ID dell'utente loggato dal server

                console.log('ID utente loggato:', utenteLoggato);

                if (utenteLoggato) {

                    document.getElementById('viaggiConsigliatiContainer').style.display = 'block';

					fetchRecommendedTrips();
					
                } else {

                }
            } else {
				
				const randomDefaultId = /*[Math.floor(Math.random() * 7)];*/ 3;

                    const viaggiResponse = await fetch(`/api/viaggi/filtra/tag?tagId=${randomDefaultId}`, {
                        method: 'GET',
                        credentials: 'include',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });

                    if (viaggiResponse.ok) {
                        const viaggi = await viaggiResponse.json();
                        const consigliatiDiv = document.getElementById('consigliati');
                        consigliatiDiv.innerHTML = '';

                        const risultatiDaMostrare = viaggi.slice(0, 3);

                        risultatiDaMostrare.forEach(viaggio => {
                            consigliatiDiv.innerHTML += createViaggioCard(viaggio);
                        })

                    } else {
                        console.error('Errore durante il recupero dei viaggi:', viaggiResponse.status);
                    }


            }
        } catch (error) {
            console.error('Errore durante la richiesta:', error);
        }
    }
    
    async function effettuaLogout() {
        try {
            const response = await fetch('/logout', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (response.ok) {
                const message = await response.text(); // Ottieni il messaggio di risposta dal server
                alert(message); // Mostra il messaggio di logout effettuato con successo
                window.location.href = '/';
            } else {
                alert('Errore durante il logout. Riprova.');
            }
        } catch (error) {
            console.error('Errore durante la richiesta di logout:', error);
        }
    }


    let currentPage = 0;
    const tripsPerPage = 3;
    let allTrips = [];

    async function fetchRecommendedTrips() {
        const url = 'api/amicizia/trovaViaggiConAmici';
        const container = document.getElementById('cardContainer');
        container.innerHTML = ""; // Clear previous results

        try {
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error('Error fetching trips');
            }

            allTrips = await response.json();
            const uniqueTrips = Array.from(new Set(allTrips.map(trip => trip.nome)))
                .map(name => allTrips.find(trip => trip.nome === name)); // Get unique trips

            displayTrips(uniqueTrips);
            createPaginationControls(uniqueTrips.length);
        } catch (error) {
            console.error('Error:', error);
            container.innerText = error.message;
        }
    }

    function displayTrips(trips) {
        const container = document.getElementById('cardContainer');
        const startIndex = currentPage * tripsPerPage;
        const endIndex = startIndex + tripsPerPage;
        const tripsToDisplay = trips.slice(startIndex, endIndex);

        container.innerHTML = tripsToDisplay.map(createViaggioCard).join('');
    }
    function createPaginationControls(totalTrips) {
        const paginationContainer = document.getElementById('paginationControls');
        paginationContainer.innerHTML = '';

        const totalPages = Math.ceil(totalTrips / tripsPerPage);

        if (currentPage > 0) {
		    paginationContainer.innerHTML += `<button class="btn btn-secondary" onclick="changePage(-1)"><img src="/assets/icon/freccia-sinistra.svg" alt="Previous"></button>`;
		}
		
		if (currentPage < totalPages - 1) {
		    paginationContainer.innerHTML += `<button class="btn btn-secondary" onclick="changePage(1)"><img src="/assets/icon/freccia-destra.svg" alt="Next"></button>`;
		}
    }

    function changePage(direction) {
        currentPage += direction;
        displayTrips(allTrips);
        createPaginationControls(allTrips.length);
    }
        
	bottoneLogin();
	
	getTags();

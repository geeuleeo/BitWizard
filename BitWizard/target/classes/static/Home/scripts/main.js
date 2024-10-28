/**
 * 
 */


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

function displayResults(results) {
	const resultsContainer = document.getElementById('results');
	resultsContainer.innerHTML = results.map(createViaggioCard).join('');
}

document.getElementById('filterByTag').addEventListener('submit', async (e) => {
	e.preventDefault();

	const tagId = document.getElementById('tagId').value;
	const container = document.getElementById('cardContainer');
	container.innerHTML = "";

	try {
		const result = await fetchData(`api/viaggi/filtra/tag?tagId=${tagId}`);
		result.forEach(viaggio => {
			container.innerHTML += createViaggioCard(viaggio);
		});
	} catch (error) {
		container.innerText = error.message;
	}
});


document.getElementById('filterByAge').addEventListener('submit', async (e) => {
	e.preventDefault();

	const minAge = document.getElementById('minAge').value;
	const maxAge = document.getElementById('maxAge').value;
	const container = document.getElementById('cardContainer');

	container.innerHTML = "";

	try {
		const result = await fetchData(`api/viaggi/filtra/eta?min=${minAge}&max=${maxAge}`);
		result.forEach(viaggio => {
			container.innerHTML += createViaggioCard(viaggio);
		});
	} catch (error) {
		container.innerText = error.message;
	}
});

document.getElementById('filterByDestination').addEventListener('submit', async (e) => {
	e.preventDefault();

	const destinazione = document.getElementById('destinazione').value;
	const container = document.getElementById('cardContainer');

	container.innerHTML = "";

	try {
		const result = await fetchData(`api/viaggi/filtra/destinazione?destinazione=${destinazione}`);
		result.forEach(viaggio => {
			container.innerHTML += createViaggioCard(viaggio);
		});
	} catch (error) {
		container.innerText = error.message;
	}
});

document.getElementById('filterByPartenza').addEventListener('submit', async (e) => {
	e.preventDefault();

	const partenza = document.getElementById('partenza').value;
	const container = document.getElementById('cardContainer');

	container.innerHTML = "";

	try {
		const result = await fetchData(`api/viaggi/filtra/partenza?partenza=${partenza}`);
		result.forEach(viaggio => {
			container.innerHTML += createViaggioCard(viaggio);
		});
	} catch (error) {
		container.innerText = error.message;
	}
});

document.getElementById('filterByPrezzo').addEventListener('submit', async (e) => {
	e.preventDefault();

	const minPrezzo = document.getElementById('minPrezzo').value;
	const maxPrezzo = document.getElementById('maxPrezzo').value;
	const container = document.getElementById('cardContainer');

	container.innerHTML = "";

	try {
		const result = await fetchData(`api/viaggi/filtra/prezzo?min=${minPrezzo}&max=${maxPrezzo}`);
		result.forEach(viaggio => {
			container.innerHTML += createViaggioCard(viaggio);
		});
	} catch (error) {
		container.innerText = error.message;
	}
});

async function getTags() {
	try {
		const response = await fetch('/api/tags/get', {
			method: 'GET',
			credentials: 'include',
			headers: {
				'Content-Type': 'application/json'
			}
		});

		// Controlla se la risposta è ok
		if (response.ok) {
			const tags = await response.json(); N


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
						const viaggioElement = document.createElement('div');
						viaggioElement.textContent = viaggio.nome + "// luogo partenza = " + viaggio.luogoPartenza + "//luogo arrivo = " + viaggio.luogoArrivo;
						consigliatiDiv.appendChild(viaggioElement);
					});
				} else {
					console.error('Errore durante il recupero dei viaggi:', viaggiResponse.status);
				}
			} else {
				console.error('Nessun tag trovato');
			}
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
				// Nascondi il pulsante di login
				document.getElementById('pulsanteLogin').style.display = 'none';

				document.getElementById('pulsanteAreaPersonale').style.display = 'block';

				// Mostra il pulsante di logout
				document.getElementById('pulsanteLogout').style.display = 'block';

				document.getElementById('pulsanteCreaViaggio').style.display = 'block';
			} else {
				// Mostra il pulsante di login
				document.getElementById('pulsanteLogin').style.display = 'block';

				document.getElementById('pulsanteAreaPersonale').style.display = 'none';
				// Nascondi il pulsante di logout
				document.getElementById('pulsanteLogout').style.display = 'none';

				document.getElementById('pulsanteCreaViaggio').style.display = 'none';
			}
		} else {
			console.error('Errore nel recupero della sessione utente');
			// Mostra il pulsante di login
			document.getElementById('pulsanteLogin').style.display = 'block';

			document.getElementById('pulsanteAreaPersonale').style.display = 'none';
			// Nascondi il pulsante di logout
			document.getElementById('pulsanteLogout').style.display = 'none';

			document.getElementById('pulsanteCreaViaggio').style.display = 'none';
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

bottoneLogin();

getTags();


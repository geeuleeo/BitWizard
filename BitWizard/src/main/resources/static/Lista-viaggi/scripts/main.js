
// Funzione per caricare i viaggi dell'utente
async function caricaViaggi() {
	const response = await fetch('api/viaggi/lista'); // Endpoint che restituisce i viaggi dell'utente
	const viaggi = await response.json();
	const container = document.getElementById('cardContainer');

	viaggi.forEach(viaggio => {
		container.innerHTML += createViaggioCard(viaggio);
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

caricaViaggi();
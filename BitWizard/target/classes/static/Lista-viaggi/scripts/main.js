caricaViaggi();
async function caricaViaggi(filtro = {}) {
	const response = await fetch('api/viaggi/lista'); // Endpoint che restituisce i viaggi dell'utente
	const viaggi = await response.json();

	// Se il filtro è vuoto, mostra tutti i viaggi
	if (Object.values(filtro).every(value => value === '' || value === null)) {
		const container = document.getElementById('cardContainer');
		container.innerHTML = ''; // Pulisci il contenitore esistente

		viaggi.forEach(viaggio => {
			container.innerHTML += createViaggioCard(viaggio);
		});
		return; // Esci dalla funzione
	}

	// Filtrare i viaggi in base ai parametri forniti
	const viaggiFiltrati = viaggi.filter(viaggio => {
		const { luogoPartenza, luogoArrivo, dataPartenza, dataRitorno } = filtro;

		const partenzaMatch = luogoPartenza ? viaggio.luogoPartenza.toLowerCase().includes(luogoPartenza.toLowerCase()) : true;
		const arrivoMatch = luogoArrivo ? viaggio.luogoArrivo.toLowerCase().includes(luogoArrivo.toLowerCase()) : true;
		const dataPartenzaMatch = dataPartenza ? new Date(viaggio.dataPartenza) >= new Date(dataPartenza) : true;
		const dataRitornoMatch = dataRitorno ? new Date(viaggio.dataRitorno) <= new Date(dataRitorno) : true;

		return partenzaMatch && arrivoMatch && dataPartenzaMatch && dataRitornoMatch;
	});

	const container = document.getElementById('cardContainer');
	container.innerHTML = ''; // Pulisci il contenitore esistente

	viaggiFiltrati.forEach(viaggio => {
		container.innerHTML += createViaggioCard(viaggio);
	});
}
document.getElementById('searchForm').addEventListener('submit', function(event) {
	event.preventDefault(); // Previene il comportamento predefinito del submit

	// Raccogliere i valori dei filtri
	const filtro = {
		luogoPartenza: document.getElementById('luogoPartenza').value.trim(),
		luogoArrivo: document.getElementById('luogoArrivo').value.trim(),
		dataPartenza: document.getElementById('dataPartenza').value,
		dataRitorno: document.getElementById('dataRitorno').value,
	};

	// Caricare i viaggi filtrati
	caricaViaggi(filtro);
});


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

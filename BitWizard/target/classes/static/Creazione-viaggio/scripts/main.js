/**
 * Logica per la pagina Creazione Viaggio
 */


// Funzione per ottenere i tag dal backend
async function getTags() {
	const response = await fetch('/api/tags/cerca');
	const tags = await response.json();
	console.log("Tag ricevuti dal backend:", tags);
	return tags;
}

// Visualizza i tag disponibili
function displayTags(tags) {
	const availableTagsDiv = document.getElementById('availableTags');
	tags.forEach(tag => {
		console.log("Tag caricato:", tag);
		const tagElement = document.createElement('div');
		tagElement.textContent = tag.tipoTag;
		tagElement.className = 'tag';
		tagElement.setAttribute('data-tag-id', tag.tagId);
		tagElement.onclick = () => selectTag(tagElement);
		availableTagsDiv.appendChild(tagElement);
	});
}

// Funzione per selezionare e rimuovere tag
function selectTag(tagElement) {
	console.log("Tag selezionato:", tagElement);
	const selectedTagsDiv = document.getElementById('selectedTags');
	const clonedTag = tagElement.cloneNode(true);
	clonedTag.onclick = () => removeTag(clonedTag);
	selectedTagsDiv.appendChild(clonedTag);
	tagElement.remove();
}

function removeTag(tagElement) {
	console.log("Tag rimosso:", tagElement);
	const availableTagsDiv = document.getElementById('availableTags');
	const clonedTag = tagElement.cloneNode(true);
	clonedTag.onclick = () => selectTag(clonedTag);
	availableTagsDiv.appendChild(clonedTag);
	tagElement.remove();
}

// Aggiungi i tag selezionati al FormData
function appendTagsToFormData(formData) {
	const selectedTagsDiv = document.getElementById('selectedTags');
	const tagElements = selectedTagsDiv.getElementsByClassName('tag');
	for (let i = 0; i < tagElements.length; i++) {
		const tagElement = tagElements[i];
		const tagId = tagElement.getAttribute('data-tag-id');
		const tagName = tagElement.textContent;
		if (tagId && tagName) {
			formData.append(`tags[${i}][tagId]`, tagId);
			formData.append(`tags[${i}][tipoTag]`, tagName);
		}
	}
}

// Funzione per l'anteprima dell'immagine di copertina
function handleCopertinaImage() {
	const copertinaInput = document.getElementById('immagineCopertina');
	const copertinaPreview = document.getElementById('copertinaPreview');
	copertinaPreview.innerHTML = ""; // Pulizia dell'anteprima precedente

	if (copertinaInput.files && copertinaInput.files[0]) {
		const reader = new FileReader();
		reader.onload = function(e) {
			const img = document.createElement('img');
			img.src = e.target.result;
			img.className = 'img-preview';
			copertinaPreview.appendChild(img);
		};
		reader.readAsDataURL(copertinaInput.files[0]);
	}
}

let selectedImages = [];

// Funzione per l'anteprima delle immagini selezionate
function handleImages() {
	const copertinaInput = document.getElementById('immagini');
	const copertinaPreview = document.getElementById('copertinaPreview');
	copertinaPreview.innerHTML = "";

	selectedImages = []; // Reset the array

	if (copertinaInput.files && copertinaInput.files.length > 0) {
		Array.from(copertinaInput.files).forEach((file) => {
			// Create image preview
			const reader = new FileReader();
			reader.onload = function(e) {
				const img = document.createElement('img');
				img.src = e.target.result;
				img.className = 'img-preview';
				copertinaPreview.appendChild(img);
			};
			reader.readAsDataURL(file);

			// Add file to selectedImages array
			selectedImages.push(file);
		});
	} else {
		console.log("Nessun file selezionato.");
	}
}

        
/*

	function handleMultipleImages() {
		console.log('handleMultipleImages called');

		const imageInput = document.getElementById('immagini');
		selectedImages = Array.from(imageInput.files);
		console.log('Selected Images:', selectedImages);

		const previewContainer = document.getElementById('previewContainer');
		previewContainer.innerHTML = ""; // Clear previous previews

		if (selectedImages.length > 0) {
			selectedImages.forEach((file, index) => {
				const reader = new FileReader();

				reader.onload = function(e) {
					const img = document.createElement('img');
					img.src = e.target.result;
					img.className = 'img-preview';
					previewContainer.appendChild(img);
					console.log(`Immagine ${index + 1} caricata: ${file.name}`);
				};

				reader.onerror = function() {
					console.error(`Errore nel caricamento dell'immagine ${file.name}`);
				};

				reader.readAsDataURL(file);
			});
		} else {
			console.log("Nessun file selezionato.");
		}
	}
*/

	// Funzione per inviare i dati tramite fetch
	function inviaDati(formData) {
		fetch('/api/viaggi/crea', {
			method: 'POST',
			body: formData
		})
			.then(response => {
				const feedbackMessage = document.getElementById('feedbackMessage');
				if (response.ok) {
					return response.json().then(data => {
						console.log("Viaggio creato con successo:", data);
						feedbackMessage.textContent = 'Viaggio creato con successo!';
						feedbackMessage.style.color = 'green';
						feedbackMessage.style.display = 'block';
						Swal.fire({
                			icon: 'success',
                			title: 'Viaggio creato con successo!',
                			text: 'Verrai reindirizzato alla tua pagina personale',
                			confirmButtonText: 'OK'
            			}).then(() => {
    						window.location.assign("paginaPersonale");
						});
						return data;
					});
				} else {
					return response.text().then(errorText => {
						feedbackMessage.textContent = 'Errore durante la creazione del viaggio: ' + errorText;
						feedbackMessage.style.color = 'red';
						feedbackMessage.style.display = 'block';
						throw new Error(errorText);
					});
				}
			})
			.catch(error => {
				console.error('Errore:', error.message);
				const feedbackMessage = document.getElementById('feedbackMessage');
				feedbackMessage.textContent = 'Si è verificato un errore. Riprova più tardi.';
				feedbackMessage.style.color = 'red';
				feedbackMessage.style.display = 'block';
			});
	}

// Funzione per inviare il form
function inviaForm() {
	const form = document.getElementById('creaViaggioForm');
	let formData = new FormData(form);
	const viaggioDTO = {
		nome: document.getElementById('nome').value,
		luogoPartenza: document.getElementById('luogoPartenza').value,
		luogoArrivo: document.getElementById('luogoArrivo').value,
		dataPartenza: document.getElementById('dataPartenza').value,
		dataRitorno: document.getElementById('dataRitorno').value,
		dataScadenza: document.getElementById('dataScadenza').value,
		numPartMin: document.getElementById('numPartMin').value,
		numPartMax: document.getElementById('numPartMax').value,
		prezzo: document.getElementById('prezzo').value,
		etaMin: document.getElementById('etaMin').value,
		etaMax: document.getElementById('etaMax').value,
		tags: getSelectedTags()
	};

	// Aggiungi il viaggioDTO al formData come JSON
	formData.append('viaggioDTO', new Blob([JSON.stringify(viaggioDTO)], { type: 'application/json' }));

	// Aggiungi l'immagine di copertina, se presente
	const immagineCopertina = document.getElementById('immagineCopertina').files[0];
	if (immagineCopertina) {
		formData.append('immagineCopertina', immagineCopertina);
	}

	console.log("Immagini selezionate per l'invio:", selectedImages);

	console.log('selectedImages in inviaForm:', selectedImages);

	if (selectedImages.length > 0) {
		selectedImages.forEach((file) => {
			console.log(`Aggiungendo l'immagine: ${file.name}`);
			formData.append('immagini', file);
		});
	} else {
		console.log("Nessuna immagine da inviare.");
	}

	// Log FormData entries
	for (let [key, value] of formData.entries()) {
		console.log(`${key}:`, value);
	}

	// Invia i dati
	inviaDati(formData);
}

// Funzione per raccogliere i tag selezionati
function getSelectedTags() {
	const selectedTagsDiv = document.getElementById('selectedTags');
	const tagElements = selectedTagsDiv.getElementsByClassName('tag');
	const selectedTags = [];
	for (let tagElement of tagElements) {
		const tagId = tagElement.getAttribute('data-tag-id');
		const tipoTag = tagElement.textContent;
		if (tagId && tipoTag) {
			selectedTags.push({ tagId: tagId, tipoTag: tipoTag });
		}
	}
	console.log("Tag selezionati:", selectedTags);
	return selectedTags;
}

// Inizializza la pagina caricando i tag
getTags().then(displayTags);


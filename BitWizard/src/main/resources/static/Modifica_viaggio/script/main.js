console.log('ciao');

	const urlParams = new URLSearchParams(window.location.search);
	const viaggioId = urlParams.get('viaggioId');
	console.log(viaggioId);

    document.addEventListener("DOMContentLoaded", function() {
        if (!window.viaggioDatiCaricati) {
            window.viaggioDatiCaricati = true; // Flag per segnare che i dati sono già stati caricati

            const urlParams = new URLSearchParams(window.location.search);
            const viaggioId = urlParams.get('viaggioId');
            caricaDatiViaggio(viaggioId);
        }
    });

    // Funzione per caricare i dati del viaggio da modificare
    function caricaDatiViaggio(viaggioId) {
        fetch(`/viaggio/${viaggioId}`)
            .then(response => response.json())
            .then(data => {
                // Popola i campi del form
                document.getElementById("viaggioId").value = viaggioId;
                document.getElementById("nome").value = data.nome;
                document.getElementById("luogoPartenza").value = data.luogoPartenza;
                document.getElementById("luogoArrivo").value = data.luogoArrivo;
                document.getElementById("dataPartenza").value = data.dataPartenza;
                document.getElementById("dataRitorno").value = data.dataRitorno;
                document.getElementById("dataScadenza").value = data.dataScadenza;
                document.getElementById("numPartMin").value = data.numPartMin;
                document.getElementById("numPartMax").value = data.numPartMax;
                document.getElementById("prezzo").value = data.prezzo;
                document.getElementById("etaMin").value = data.etaMin;
                document.getElementById("etaMax").value = data.etaMax;

                // Recupera i tag del viaggio
                const viaggiotags = data.tagDTOs;
                console.log("Tag associati al viaggio:", viaggiotags);

                // Carica i tag disponibili e poi visualizzali
                getTags().then(tags => displayTags(tags, viaggiotags));
            })
            .catch(error => console.error('Errore nel caricamento dei dati del viaggio:', error));
    }

    // Funzione per recuperare tutti i tag disponibili
    async function getTags() {
        const response = await fetch('/api/tags/cerca');
        const tags = await response.json();
        console.log("Tag ricevuti dal backend:", tags);
        return tags;
    }

    // Funzione per visualizzare i tag nelle sezioni corrette
    function displayTags(tags, viaggiotags) {
        const availableTagsDiv = document.getElementById('availableTags');
        const selectedTagsDiv = document.getElementById('selectedTags');

        // Svuota i contenitori prima di popolarli
        availableTagsDiv.innerHTML = '';
        selectedTagsDiv.innerHTML = '';

        const selectedTagIds = new Set(viaggiotags.map(tag => tag.tagId));

        // Visualizza i tag selezionati
        viaggiotags.forEach(tag => {
            const tagElement = document.createElement('div');
            tagElement.textContent = tag.tipoTag;
            tagElement.className = 'tag';
            tagElement.setAttribute('data-tag-id', tag.tagId);
            tagElement.onclick = () => deselectTag(tagElement);
            selectedTagsDiv.appendChild(tagElement);
        });

        // Visualizza solo i tag disponibili non già selezionati
        tags.forEach(tag => {
            if (!selectedTagIds.has(tag.tagId)) {
                const tagElement = document.createElement('div');
                tagElement.textContent = tag.tipoTag;
                tagElement.className = 'tag';
                tagElement.setAttribute('data-tag-id', tag.tagId);
                tagElement.onclick = () => selectTag(tagElement);
                availableTagsDiv.appendChild(tagElement);
            }
        });
    }

    // Funzione per selezionare un tag (spostarlo da availableTags a selectedTags)
    function selectTag(tagElement) {
        const selectedTagsDiv = document.getElementById('selectedTags');
        tagElement.remove();
        const clonedTag = tagElement.cloneNode(true);
        clonedTag.onclick = () => deselectTag(clonedTag);
        selectedTagsDiv.appendChild(clonedTag);
    }

    // Funzione per deselezionare un tag (spostarlo da selectedTags a availableTags)
    function deselectTag(tagElement) {
        const availableTagsDiv = document.getElementById('availableTags');
        tagElement.remove();
        const clonedTag = tagElement.cloneNode(true);
        clonedTag.onclick = () => selectTag(clonedTag);
        availableTagsDiv.appendChild(clonedTag);
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

    function inviaForm() {
        // Construct the viaggioDTO object from the form inputs
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

        // Send the JSON data using fetch
        fetch(`/api/viaggi/modifica/${viaggioId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(viaggioDTO)
        })
        .then(response => {
            const feedbackMessage = document.getElementById('feedbackMessage');
            if (response.ok) {
                return response.json().then(data => {
                    feedbackMessage.textContent = 'Viaggio modificato con successo!';
                    feedbackMessage.style.color = 'green';
                    feedbackMessage.style.display = 'block';
                    Swal.fire({
    					icon: 'success',
    					title: 'Viaggio modificato con successo!',
    					text: 'Verrai reindirizzato alla pagina del viaggio.',
    					confirmButtonText: 'OK'
					}).then(() => {
    					window.location.assign(`/paginaViaggio/viaggio?viaggioId=${viaggioId}`);
					});
                });
            } else {
                return response.text().then(errorText => {
                    feedbackMessage.textContent = 'Errore durante la modifica del viaggio: ' + errorText;
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
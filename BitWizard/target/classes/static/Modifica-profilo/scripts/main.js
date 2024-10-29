    
    document.addEventListener('DOMContentLoaded', () => {
        caricaDatiUtente();
    });
    
 // Funzione per inviare il form
    function inviaForm() {
        const form = document.getElementById('signup');
        
        // Crea un nuovo oggetto FormData
        let formData = new FormData(form);  // Assicurati di dichiarare formData qui

        // Aggiungi il DTO come JSON stringificato
        const utenteDTO = {
            nome: document.getElementById('nome').value,
            cognome: document.getElementById('cognome').value,
            email: document.getElementById('email').value,
            numeroTelefono: document.getElementById('numeroTelefono').value,
            dataNascita: document.getElementById('dataNascita').value,
            descrizione: document.getElementById('descrizione').value,
            tags: getSelectedTags()  // Usa la funzione getSelectedTags per raccogliere i tag selezionati
        };

        // Aggiungi il DTO JSON al FormData
        formData.append('utenteDTO', new Blob([JSON.stringify(utenteDTO)], {
            type: 'application/json'
        }));

        // Invia i dati
        inviaDati(formData);
    }

    // Funzione per inviare i dati tramite fetch
    function inviaDati(formData) {
        fetch('/api/utente/modifica', {
            method: 'PUT',
            body: formData
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return response.text().then(text => { throw new Error(text) });
            }
        })
        .then(data => {
            console.log("Utente creato con successo:", data);
            const successo = document.getElementById("successo");

            successo.innerText=("Modifica avvenuta con successo!");
			Swal.fire({
    					icon: 'success',
    					title: 'Profilo modificato con successo!',
    					text: 'Verrai reindirizzato alla tua pagina personale.',
    					confirmButtonText: 'OK'
					}).then(() => {
    					window.location.assign("paginaPersonale");
					});
        })
        .catch(error => {
            console.error('Errore:', error.message);
            const erroriDiv = document.getElementById("errori");
            erroriDiv.innerText= error.message;

        });
    }

    // Funzione per caricare i dati del viaggio da modificare
    function caricaDatiUtente() {
        fetch(`/api/utente/me`)
            .then(response => response.json())
            .then(data => {
                // Popola i campi del form
                document.getElementById("nome").value = data.nome;
                document.getElementById("cognome").value = data.cognome;
                document.getElementById("numeroTelefono").value = data.numeroTelefono;
                document.getElementById("email").value = data.email;
                document.getElementById("dataNascita").value = data.dataNascita;
                document.getElementById("descrizione").value = data.descrizione;

                // Recupera i tag del viaggio
                const utentetags = data.tags;
                console.log("Tag associati all'utente: ", utentetags);

                // Carica i tag disponibili e poi visualizzali
                getTags().then(tags => displayTags(tags, utentetags));
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
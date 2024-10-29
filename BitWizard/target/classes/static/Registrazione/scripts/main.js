/**
 * Logica per la pagina registrazione
 * 
 */

 // Funzione per ottenere i tag dal backend
async function getTags() {
    const response = await fetch('/api/tags/cerca');  // Modifica con l'endpoint corretto
    const tags = await response.json();
    console.log("Tag ricevuti dal backend:", tags);  // Controlla i dati ricevuti
    return tags;
}


function displayTags(tags) {
    const availableTagsDiv = document.getElementById('availableTags');

    tags.forEach(tag => {
        // Verifica che il campo dell'ID esista nel tag
        console.log("Tag caricato:", tag);  // Verifica se l'ID è corretto

        const tagElement = document.createElement('div');
        tagElement.textContent = tag.tipoTag;  // Supponendo che il campo "tipoTag" esista
        tagElement.className = 'tag';
        tagElement.setAttribute('data-tag-id', tag.tagId);  // Usa il campo "tagId" o quello corretto

        // Aggiungi evento per selezionare il tag
        tagElement.onclick = () => selectTag(tagElement);

        availableTagsDiv.appendChild(tagElement);
    });
}

    function selectTag(tagElement) {
        console.log("Tag selezionato:", tagElement);  // Log del tag selezionato
        
        const selectedTagsDiv = document.getElementById('selectedTags');
        const clonedTag = tagElement.cloneNode(true);
        clonedTag.onclick = () => removeTag(clonedTag);
        selectedTagsDiv.appendChild(clonedTag);
        
        tagElement.remove();
    }

    function removeTag(tagElement) {
        console.log("Tag rimosso:", tagElement);  // Log del tag rimosso
        
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
            password: document.getElementById('password').value,
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
        fetch('/api/utente/signup', {
            method: 'POST',
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
			Swal.fire({
                	icon: 'success',
                	title: 'Registrazione avvenuta con successo!',
                	text: 'Registrazione avvenuta con successo! Verrai reindirizzato alla pagina di login in 5 secondi.',
                	confirmButtonText: 'OK'
            	});

            //const bottoneLogin = document.getElementById("pulsanteLogin");
            setTimeout(() => {
                window.location.assign("login");
                //bottoneLogin.click();
            }, 5000);

        })
        .catch(error => {
            console.error('Errore:', error.message);
            const erroriDiv = document.getElementById("errori");
            erroriDiv.innerText= error.message;

        });
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
            selectedTags.push({
                tagId: tagId,
                tipoTag: tipoTag
            });
        }
    }

    console.log("Tag selezionati:", selectedTags);  // Verifica che i tag vengano correttamente raccolti
    return selectedTags;
}

    // Inizializza la pagina caricando i tag
    getTags().then(displayTags);


    
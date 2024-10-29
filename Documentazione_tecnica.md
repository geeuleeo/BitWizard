# Documentazione API - Sito Web Agenzia Viaggi
## Descrizione Generale
L'API offre endpoint per la gestione di viaggi, utenti, recensioni, notifiche, immagini e messaggi. Le funzionalità principali includono la creazione e modifica di viaggi, l'iscrizione degli utenti ai viaggi, la gestione del profilo utente, l'invio di notifiche e la gestione delle immagini.

---

## Endpoints Principali
- ### Gestione Viaggi
Permette di creare, modificare e recuperare informazioni sui viaggi.

/api/viaggi/crea (POST): Crea un nuovo viaggio.
/api/viaggi/modifica/{viaggioId} (PUT): Modifica i dettagli di un viaggio esistente.
/api/viaggi/annulla/{viaggioId} (PUT): Annulla un viaggio esistente.
/api/viaggi/iscriviti/{viaggioId} (POST): Permette all'utente di iscriversi a un viaggio.
/api/viaggi/iscriviti/rimuovi/{viaggioId} (DELETE): Rimuove un utente dai partecipanti di un viaggio.
/api/viaggi/lista (GET): Restituisce la lista di tutti i viaggi disponibili.
/api/viaggi/filtra/tag (GET): Filtra i viaggi per tag specifici.
/api/viaggi/filtra/prezzo (GET): Filtra i viaggi per fascia di prezzo.
/api/viaggi/{viaggioId}/immagine (GET e POST): Gestisce le immagini associate a un viaggio.

---

- ### Gestione Utenti
Fornisce endpoint per gestire il profilo utente, inclusa la modifica dei dettagli personali e delle immagini di profilo.

/api/utente/signup (POST): Registra un nuovo utente.
/api/utente/modifica (PUT): Modifica i dettagli dell'utente, inclusa l'immagine di profilo.
/api/utente/{utenteId} (GET): Recupera i dettagli di un utente specifico.
/api/utente/recensione/{utenteId} (GET): Ottiene le recensioni lasciate dall'utente.
/api/utente/session (GET): Recupera l'utente attualmente loggato.
/api/utente/profilo/{utenteId} (GET): Recupera il profilo dell'utente.

---

- ### Gestione Recensioni
Gestisce la creazione e visualizzazione delle recensioni sui viaggi.

/recensione/crea (POST): Crea una recensione per un viaggio.
/recensione/viaggio/{viaggioId} (GET): Ottiene tutte le recensioni per un determinato viaggio.

---

- ### Gestione Notifiche
Permette la creazione e la visualizzazione delle notifiche per eventi specifici.

/notifica/crea (POST): Crea una nuova notifica.
/notifica/cerca (GET): Cerca notifiche per l'utente loggato.
/notifica/cancella (DELETE): Cancella una notifica specifica.

---

- ### Gestione Immagini
Gestisce il caricamento e la visualizzazione delle immagini di profilo, di viaggio, e altre immagini legate alle entità.

/api/immagini/upload (POST): Carica un'immagine generica.
/api/immagini/utente/profilo/{utenteId} (GET): Ottiene l'immagine di profilo dell'utente.
/api/immagini/viaggio/immagine (GET): Ottiene l'immagine di copertina di un viaggio.

---

- ### Gestione Tag
Permette di gestire e ottenere i tag associati agli utenti e ai viaggi.

/api/tags/get (GET): Ottiene i tag associati a un utente specifico.
/api/tags/cerca (GET): Recupera tutti i tag disponibili.

---

- ### Gestione Messaggi
Consente agli utenti di inviare e recuperare messaggi nella chat del viaggio.

/crea/messaggio (POST): Invia un messaggio nella chat di un viaggio.
/carica/messaggi/{viaggioId} (GET): Recupera i messaggi di chat per un determinato viaggio.

---

## Componenti e Schemi
Gli oggetti JSON scambiati tramite le API si basano su schemi ben definiti per garantire la coerenza e la validità dei dati. Di seguito sono riportati alcuni dei principali componenti:

- ViaggioDTO: Rappresenta i dati di un viaggio, includendo dettagli come nome, luogo di partenza, età minima/massima e prezzo.
- UtenteDTO: Contiene le informazioni dell'utente, inclusi nome, cognome, email e immagine del profilo.
- RecensioneDTO: Rappresenta una recensione su un viaggio con dati come testo, rating e data.
- NotificaDTO: Contiene i dettagli di una notifica, come il testo e la data.
- CreazioneMessaggioDTO: Rappresenta un messaggio di chat inviato per un viaggio.
- TagDTO: Rappresenta un tag che può essere associato a viaggi o utenti.
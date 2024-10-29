# Documentazione Funzionamento Sito Web - Agenzia Viaggi

## Introduzione
Il sito web è una piattaforma per l’organizzazione e la partecipazione a viaggi di gruppo. Gli utenti possono registrarsi, esplorare viaggi disponibili, iscriversi, lasciare recensioni, e interagire con altri membri. Ogni viaggio è gestito da un organizzatore, e il sito include funzionalità per la gestione di tag, livelli, notifiche, e ruoli utente.

---

## Funzionalità Principali
- Registrazione e Gestione Utente
Gli utenti possono registrarsi fornendo informazioni personali come nome, cognome, email e password. Una volta registrati, possono aggiornare il proprio profilo aggiungendo foto, descrizioni personali e tag che indicano le loro preferenze di viaggio (es. mare, montagna, avventura).

- Stato del Viaggio: Ogni viaggio ha uno stato che indica la disponibilità (es. in attesa, confermato). Lo stato può essere aggiornato manualmente o automaticamente in base alla data e ai partecipanti.
- Tag: Ogni viaggio può essere etichettato con uno o più tag che ne descrivono la natura (es. escursione, relax), facilitando la ricerca per gli utenti.
- Iscrizione e Partecipazione ai Viaggi
Gli utenti possono iscriversi ai viaggi che trovano interessanti. L'iscrizione comporta la creazione di un record nella tabella Partecipanti_Viaggio, che tiene traccia della data di iscrizione e, se applicabile, della data di annullamento.

- Controlli sul numero di Partecipanti: Il sistema verifica che il numero massimo di partecipanti non sia stato superato.
- Storico Viaggi: Gli utenti possono visualizzare un elenco dei viaggi a cui hanno partecipato.
- Recensioni
Dopo aver partecipato a un viaggio, gli utenti possono lasciare una recensione che include un testo e una valutazione. Le recensioni sono visibili agli altri utenti nella pagina del viaggio, consentendo loro di farsi un'idea dell’esperienza.

- Chat del Viaggio
Ogni viaggio ha una chat dedicata dove i partecipanti possono comunicare tra di loro e con l'organizzatore. Questo spazio è utile per condividere dettagli e rispondere a domande.

- Sistema di Notifiche
Gli utenti ricevono notifiche per eventi importanti come l'approvazione dell'iscrizione a un viaggio, l'accettazione di una richiesta di amicizia, o il cambiamento di stato di un viaggio. Le notifiche sono presentate in un div con altezza massima per evitare di occupare tutto lo spazio della pagina.

- Tag e Preferenze
Il sito permette agli utenti di assegnare tag sia al proprio profilo che ai viaggi. Questo consente una maggiore personalizzazione e facilita la ricerca di viaggi in base ai propri interessi.

- Tag Utente: Gli utenti possono selezionare tag che indicano le loro preferenze personali.
Tag Viaggio: I viaggi sono classificati con tag che li descrivono, come avventura, mare, o montagna.
- Gestione delle Amicizie
Gli utenti possono inviare e accettare richieste di amicizia. Una volta accettata, l'amicizia consente agli utenti di vedere i viaggi a cui partecipano i propri amici e ricevere aggiornamenti su di essi.

- Gestione dei Ruoli e Sicurezza
Il sito implementa un sistema di controllo degli accessi basato sui ruoli (RBAC) che limita l'accesso a determinate funzionalità in base al ruolo dell'utente. Gli admin possono gestire il contenuto del sito e approvare i viaggi, mentre le agenzie possono creare e gestire viaggi.

### Funzionalità Avanzate
- Cache dei Messaggi
Per migliorare le prestazioni della chat del viaggio, il sito può utilizzare la cache per memorizzare temporaneamente i messaggi, riducendo il carico sul database e migliorando i tempi di caricamento.

- Soft Delete
Per evitare di eliminare permanentemente i dati, alcune entità come Utente e Viaggio utilizzano un sistema di "soft delete" attraverso un campo booleano deleted. Questo consente di nascondere gli elementi eliminati senza rimuoverli completamente dal database.

---

### Workflow Utente
- Registrazione: L'utente si registra e completa il proprio profilo.
- Esplorazione dei Viaggi: L'utente naviga tra i viaggi disponibili, utilizzando i tag per filtrare in base ai propri interessi.
- Iscrizione a un Viaggio: L'utente si iscrive a un viaggio, ricevendo una notifica di conferma.
- Partecipazione e Interazione: L'utente utilizza la chat per interagire con gli altri partecipanti e riceve notifiche su cambiamenti di stato.
- Recensione: Al termine del viaggio, l'utente lascia una recensione.
- Gestione delle Amicizie e Notifiche: L'utente invia richieste di amicizia, visualizza notifiche e interagisce con gli amici nella piattaforma.

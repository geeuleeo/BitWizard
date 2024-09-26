
# Documentazione Database - Agenzia Viaggi

## Descrizione Generale
Il database è strutturato per supportare una piattaforma di viaggi di gruppo, che consente la gestione di utenti, viaggi, recensioni e partecipazioni. Ogni utente può partecipare a più viaggi e lasciare recensioni sui viaggi a cui ha preso parte. Sono presenti tag per classificare viaggi e utenti, oltre a un sistema di livelli e ruoli per distinguere le autorizzazioni degli utenti.

---

## Tabelle Principali

### Utente
La tabella `Utente` memorizza le informazioni personali degli utenti e mantiene traccia delle loro attività nel sistema.

- **Id**: Identificativo unico dell'utente.
- **Name**: Nome dell'utente.
- **Cognome**: Cognome dell'utente.
- **Numero telefono**: Numero di telefono dell'utente.
- **Email**: Indirizzo email dell'utente (chiave primaria).
- **Password**: Password dell'utente.
- **Data nascita**: Data di nascita dell'utente.
- **Età**: Età dell'utente (calcolato automaticamente).
- **Descrizione**: Descrizione personale dell'utente.
- **Foto profilo**: Percorso dell'immagine di profilo.
- **Viaggi iscritti**: Query che restituisce i viaggi ai quali l'utente si è iscritto.
- **Viaggi creati**: Query che restituisce i viaggi organizzati dall'utente.
- **Storico viaggi**: Query che restituisce lo storico dei viaggi a cui ha partecipato.
- **Ruolo**: Relazione con la tabella `Ruolo` (molti-a-uno).
- **Punteggio**: Punteggio cumulativo dell'utente, basato sulle sue attività.
- **Livello**: Relazione con la tabella `Livelli` (molti-a-uno).
- **Tag**: Relazione con la tabella `Tag_Utente` (uno-a-molti).
- **Deleted**: Boolean per indicare se l'utente è stato eliminato.
- **Creato il**: Timestamp di creazione dell'utente.

Relazioni:
- Ogni utente può avere uno o più ruoli e livelli associati.
- Può essere collegato a più viaggi tramite la tabella `Partecipanti_Viaggio`.
- Può creare recensioni sui viaggi tramite la tabella `Recensioni`.

---

### Viaggio
La tabella `Viaggio` memorizza i dettagli di ogni viaggio, come la destinazione e i partecipanti.

- **Id**: Identificativo unico del viaggio.
- **Name**: Nome del viaggio.
- **Destinazione**: Destinazione del viaggio.
- **Data arrivo**: Data di arrivo.
- **Partenza luogo**: Luogo di partenza del viaggio.
- **Arrivo luogo**: Luogo di arrivo.
- **Numero partecipanti min/max**: Numero minimo e massimo di partecipanti.
- **Prezzo**: Prezzo del viaggio.
- **Età minima/massima**: Età minima e massima per partecipare.
- **Stato**: Relazione con la tabella `Stato` (molti-a-uno).
- **Creatore**: Relazione con la tabella `Utente` (uno-a-molti) che rappresenta l'organizzatore del viaggio.
- **Tag**: Relazione con la tabella `Tag_Viaggio` (uno-a-molti).
- **Deleted**: Boolean per indicare se il viaggio è stato cancellato.
- **Creato il**: Timestamp di creazione.
- **Immagini viaggio**: Relazione con la tabella `Immagini_Viaggio` per associare immagini al viaggio.

Relazioni:
- Un viaggio può avere più partecipanti tramite la tabella `Partecipanti_Viaggio`.
- Può essere recensito dagli utenti tramite la tabella `Recensioni`.
- Può essere associato a uno o più tag tramite la tabella `Tag_Viaggio`.
- Ogni viaggio ha uno stato che viene aggiornato in tempo reale tramite la tabella `Stato`.

---

### Tag
La tabella `Tag` serve per categorizzare i viaggi e gli utenti.

- **Tag_id**: Identificativo unico del tag.
- **Tipo_tag**: Tipo di tag (es. escursione, mare, relax).
- **Img_tag**: Percorso immagine associata al tag.

Relazioni:
- I tag possono essere assegnati sia agli utenti (tramite `Tag_Utente`) sia ai viaggi (tramite `Tag_Viaggio`).

---

### Partecipanti_Viaggio
Questa tabella gestisce la partecipazione degli utenti ai viaggi.

- **id_viaggio**: Riferimento al viaggio (collegato alla tabella `Viaggio`).
- **id_utente**: Riferimento all'utente (collegato alla tabella `Utente`).
- **Data iscrizione**: Data di iscrizione al viaggio.
- **Data annullamento**: Data di annullamento della partecipazione (se applicabile).
- **Chiave primaria composta**: La chiave primaria è composta da `id_viaggio` e `id_utente`.

Relazioni:
- Collega ogni utente a uno o più viaggi.
- Permette di tenere traccia delle iscrizioni e dei possibili annullamenti.

---

### Recensioni
Questa tabella memorizza le recensioni scritte dagli utenti per i viaggi.

- **id_viaggio**: Riferimento al viaggio recensito.
- **id_utente**: Riferimento all'utente che ha lasciato la recensione.
- **Testo**: Testo della recensione.
- **Rating**: Punteggio della recensione (es. stelle o valutazione numerica).
- **Data**: Data della recensione.
- **Chiave primaria composta**: La chiave primaria è composta dai campi `id_viaggio` e `id_utente`.

Relazioni:
- Collega ogni recensione a un viaggio e all'utente che l'ha scritta.

---

## Tabelle Ausiliarie

### Ruolo
La tabella `Ruolo` definisce i diversi ruoli che un utente può avere all'interno del sistema.

- **ruolo_id**: Identificativo unico del ruolo.
- **Descrizione**: Descrizione del ruolo (es. agenzia, privato).

Relazioni:
- Ogni utente può avere uno o più ruoli associati.

---

### Livelli
La tabella `Livelli` gestisce i livelli degli utenti, che possono aumentare in base alle attività svolte.

- **Icona_id**: Identificativo unico del livello.
- **Descrizione**: Descrizione del livello.
- **Requisiti**: Requisiti per ottenere il livello.

Relazioni:
- Ogni utente può avere un livello assegnato in base alle sue attività.

---

### Stato
La tabella `Stato` traccia lo stato dei viaggi (in attesa, confermato, completato, etc.).

- **Stato_id**: Identificativo unico dello stato.
- **Tipo_stato**: Tipo di stato (es. in attesa, confermato).
- **Timestamp**: Data e ora dell'ultimo aggiornamento dello stato.

Relazioni:
- Ogni viaggio ha uno stato che viene aggiornato in tempo reale.
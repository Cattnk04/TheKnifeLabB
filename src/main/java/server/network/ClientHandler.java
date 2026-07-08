package main.java.server.network;

import main.java.server.dao.*;
import main.java.server.security.PasswordService;
import main.java.server.service.*;
import main.java.shared.communication.*;
import main.java.shared.domain.Preferito;
import main.java.shared.domain.Recensione;
import main.java.shared.dto.*;
import main.java.server.main.ServerMain;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private UtenteService utenteService;
    private TipoCucinaService tipoCucinaService;
    private RistoranteService ristoranteService;
    private RecensioneService recensioneService;
    private PreferitiService preferitiService;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.utenteService = new UtenteService(new UtenteDAO(), new PasswordService());
        this.tipoCucinaService = new TipoCucinaService(new TipoCucinaDAO());
        this.ristoranteService = new RistoranteService(new RistoranteDAO());
        this.recensioneService = new RecensioneService(new RecensioneDAO());
        this.preferitiService = new PreferitiService(new PreferitiDAO());
    }

    //Metodo per gestire le richieste in arrivo dal client
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                Richiesta richiesta = (Richiesta) in.readObject();
                System.out.println("Richiesta ricevuta: " + richiesta.getTipoRichiesta());

                Risposta risposta = gestisciRichiesta(richiesta);

                out.writeObject(risposta);
                out.flush();

                System.out.println("Risposta inviata: " + risposta.getMsg());
            }
        } catch (EOFException e) {
            System.out.println("Client disconnesso");
        } catch (Exception e) {
            System.out.println("Errore nella connessione del client: " + e.getMessage());
        } finally {
            chiudiConnessione();
        }
    }

    //Metodo per chiudere la connessione con il client
    private void chiudiConnessione() {
        try {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }

            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }

    //Metodo per gestire le richieste in arrivo dal client
    public Risposta gestisciRichiesta(Richiesta richiesta) {
        if (richiesta == null || richiesta.getTipoRichiesta() == null) {
            return new Risposta(false, null, "Richiesta non valida");
        }

        //Tipi di richiesta
        switch (richiesta.getTipoRichiesta()) {
            case LOGIN:
                return gestisciLogin(richiesta.getContenuto());

            case REGISTER:
                return gestisciRegistrazione(richiesta.getContenuto());

            //DA CONTROLLARE
            case LOGOUT:
                return gestisciLogout(richiesta.getContenuto());

            case GET_UTENTE:
                return gestisciGetUtente(richiesta.getContenuto());

            case MODIFICA_UTENTE:
                return gestisciModificaUtente(richiesta.getContenuto());

            //RISTORANTE
            case GET_RISTORANTE:
                return gestisciGetRistorante(richiesta.getContenuto());

            case CREA_RISTORANTE:
                return gestisciCreaRistorante(richiesta.getContenuto());

            case AGGIORNA_RISTORANTE:
                return gestisciModificaRistorante(richiesta.getContenuto());

            case ELIMINA_RISTORANTE:
                return gestisciEliminaRistorante(richiesta.getContenuto());

            case CERCA_RISTORANTE:
                return gestisciCercaRistorante(richiesta.getContenuto());

            case ESISTE_RISTORANTE:
                return gestisciEsistenzaRistorante(richiesta.getContenuto());

            //RECENSIONI
            case GET_RECENSIONI_RISTORANTE:
                return gestisciGetRecensioniRistorante(richiesta.getContenuto());

            case GET_RECENSIONI_UTENTE:
                return gestisciGetRecensioniUtente(richiesta.getContenuto());

            case GET_RECENSIONI_BYEMAIL:
                return gestisciGetRecensioniEmail(richiesta.getContenuto());

            case SCRIVI_RECENSIONE:
                return gestisciScriviRecensione(richiesta.getContenuto());

            case MODIFICA_RECENSIONE:
                return gestisciModificaRecensione(richiesta.getContenuto());

            case ELIMINA_RECENSIONE:
                return gestisciEliminaRecensione(richiesta.getContenuto());

            case RISPONDI_RECENSIONE:
                return gestisciRispondiRecensione(richiesta.getContenuto());

            case MODIFICA_RISPOSTA:
                return gestisciModificaRisposta(richiesta.getContenuto());

            case RIEPILOGO_RECENSIONE:
                return gestisciRiepilogoRecensioni(richiesta.getContenuto());

            //PREFERITI
            case GET_PREFERITI:
                return gestisciGetPreferiti(richiesta.getContenuto());

            case AGGIUNGI_PREFERITO:
                return gestisciAggiungiPreferito(richiesta.getContenuto());

            case RIMUOVI_PREFERITO:
                return gestisciRimuoviPreferito(richiesta.getContenuto());

            case ESISTE_PREFERITO:
                return gestisciEsistenzaPreferiti(richiesta.getContenuto());

            case TOGGLE_PREFERITO:
                return gestisciTogglePreferito(richiesta.getContenuto());

            //TIPO CUCINA
            case GET_TIPO_CUCINA:
                return gestisciGetTipoCucina();

            //CHIUSURA SERVER
            case SHUTDOWN_SERVER:
                return gestisciShutdown();
            default:
                return new Risposta(false, null, "Tipo richiesta non supportato");
        }
    }

    //Metodi per gestire le richieste
    //UTENTE
    private Risposta gestisciLogin(Object contenuto) {
        if (!(contenuto instanceof LoginDTO loginDTO))
            return new Risposta(false, null, "Dati login non validi");

        boolean ok = utenteService.login(loginDTO);

        if (ok) return new Risposta(ok, loginDTO.getEmail(), "Login effettuato");
        else return new Risposta(ok, null, "Email o password errati");
    }

    private Risposta gestisciRegistrazione(Object contenuto) {
        if (!(contenuto instanceof RegistrazioneDTO registrazioneDTO))
            return new Risposta(false, null, "Dati registrazione non validi");

        boolean ok = utenteService.registraUtente(registrazioneDTO);

        if (ok) return new Risposta(true, null, "Registrazione effettuata");
        else return new Risposta(false, null, "Registrazione fallita");
    }

    private Risposta gestisciLogout(Object contenuto) {
        String email = (contenuto instanceof String e) ? e : "sconosciuto";
        System.out.println("Logout effettuato per l'utente: " + email);
        return new Risposta(true, null, "Logout effettuato");
    }

    private Risposta gestisciGetUtente(Object contenuto) {
        if (!(contenuto instanceof String email)) {
            return new Risposta(false, null, "Email non valida");
        }
        try {
            RegistrazioneDTO utente = utenteService.getUtente(email);
            if (utente != null) {
                return new Risposta(true, utente, "Utente trovato");
            } else {
                return new Risposta(false, null, "Utente non trovato");
            }
        } catch (Exception e) {
            return new Risposta(false, null, e.getMessage());
        }
    }

    private Risposta gestisciModificaUtente(Object contenuto) {
        if (!(contenuto instanceof RegistrazioneDTO dto)) {
            return new Risposta(false, null, "Dati modifica utente non validi");
        }
        try {
            boolean successo = utenteService.modificaUtente(dto);

            return new Risposta(
                    successo,
                    null,
                    successo
                            ? "Dati utente aggiornati correttamente"
                            : "Errore durante l'aggiornamento dei dati utente"
            );
        } catch (Exception e) {
            return new Risposta(false, null, e.getMessage());
        }
    }

    //RISTORANTE
    private Risposta gestisciGetRistorante(Object contenuto) {
        List<RistoranteDTO> ristoranti = ristoranteService.getTuttiRistoranti();
        if (!ristoranti.isEmpty()) return new Risposta(true, ristoranti, "Ristoranti trovati");
        else return new Risposta(false, null, "Nessun ristorante trovato");
    }

    //DA CONTROLLARE
    public Risposta gestisciCreaRistorante(Object contenuto) {
        if(!(contenuto instanceof RistoranteDTO dto)){
            return new Risposta(false, null, "Dati creazione ristorante non validi");
        }
        else {
            boolean creato = ristoranteService.creaRistorante(dto);
            if (creato) return new Risposta(true, null, "Ristorante creato con successo");
            else return new Risposta(false, null, "Impossibile creare il ristorante");
        }
    }

    //DA CONTROLLARE
    //bisogna capire come passare con il DTO il campo da modificare altrimenti si crea un metodo che
    // modifichi tutti i campi del ristorante come se lo creasse se vogliamo usare il DTO che abbiamo già creato
    public Risposta gestisciModificaRistorante(Object contenuto) {
        if(!(contenuto instanceof RistoranteDTO dto)){
            return new Risposta(false, null, "Dati per modificare il ristorante non validi");
        }
        else {
        }
        return new Risposta(true, null, "Metodo non ancora implementato");
    }

    //DA CONTROLLARE
    public Risposta gestisciEliminaRistorante(Object contenuto) {
        if(!(contenuto instanceof RistoranteDTO dto)){
            return new Risposta(false, null, "Dati per eliminare il ristorante non validi");
        }
        else {
            boolean eliminato = ristoranteService.cancellaRistorante(dto.getIdRistorante());
            if (eliminato) return new Risposta(true, null, "Ristorante eliminato con successo");
            else return new Risposta(false, null, "Impossibile cancellare il ristorante");
        }
    }

    //DA CONTROLLARE
    //bisogna cercare un ristorante specifico o ritorna tutti i ristoranti?
    public Risposta gestisciCercaRistorante(Object contenuto) {
        if (!(contenuto instanceof FiltroRicercaDTO filtro)) {
            return new Risposta(false, null, "Dati per ricerca del ristorante non validi");
        }
        List<RistoranteDTO> risultati = ristoranteService.cercaRistoranti(filtro);
        if (!risultati.isEmpty()) return new Risposta(true, risultati, "Ristoranti trovati");
        else return new Risposta(false, null, "Nessun ristorante trovato");
    }

    public Risposta gestisciEsistenzaRistorante(Object contenuto) {
        if (!(contenuto instanceof RistoranteDTO dto)) {
            return new Risposta(false, null, "Dati non validi");
        }
        boolean esiste = ristoranteService.esiste(dto.getNomeRistorante(), dto.getEmail());
        return new Risposta(true, esiste, esiste ? "Ristorante esistente" : "Ristorante non esistente");
    }

    //RECENSIONI

    //Recensioni del ristorante per il ristoratore
    private Risposta gestisciGetRecensioniRistorante(Object contenuto) {
        if(!(contenuto instanceof Integer idRistorante)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        else{
            List<Recensione> recensioni = recensioneService.getRecensioni(idRistorante);
            return new Risposta(true, recensioni, "Recensioni trovate");
        }
    }

    //Recensioni del ristorante per il cliente
    public Risposta gestisciGetRecensioniUtente(Object contenuto) {
        if(!(contenuto instanceof Integer idRistorante)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        else{
            List<Recensione> recensioni = recensioneService.getRecensioniRistorante(idRistorante);
            return new Risposta(true, recensioni, "Recensioni trovate");
        }
    }

    //Recensioni del ristorante dall'email per il cliente
    public Risposta gestisciGetRecensioniEmail(Object contenuto) {
        if(!(contenuto instanceof String email)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        else{
            List<Recensione> recensioni = recensioneService.getRecensioniUtente(email);
            return new Risposta(true, recensioni, "Recensioni trovate");
        }
    }

    private Risposta gestisciScriviRecensione(Object contenuto) {
        if(!(contenuto instanceof RecensioneDTO dto)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        else {
            try{
                RecensioneDTO recDto = (RecensioneDTO) contenuto;

                Recensione recensione = new Recensione(
                        recDto.getIdRistorante(),
                        recDto.getEmail(),
                        recDto.getValutazione(),
                        recDto.getRecensione(),
                        recDto.getRisposta()
                );

                boolean successo = recensioneService.creaRecensione(recensione);

                return new Risposta(
                        successo,
                        null,
                        successo
                                ? "Recensione creata correttamente"
                                : "Errore durante la creazione della recensione"
                );
            } catch (Exception e) {
                return new Risposta(false, null, e.getMessage());
            }
        }
    }

    private Risposta gestisciModificaRecensione(Object contenuto) {
        if (!(contenuto instanceof RecensioneDTO dto)) {
            return new Risposta(false, null, "Dati recensione non validi");
        }

        try {
            boolean testoAggiornato = recensioneService.modificaRecensione(
                    dto.getIdRistorante(),
                    dto.getEmail(),
                    dto.getRecensione()
            );

            boolean valutazioneAggiornata = recensioneService.modificaValutazione(
                    dto.getIdRistorante(),
                    dto.getEmail(),
                    dto.getValutazione()
            );

            boolean successo = testoAggiornato && valutazioneAggiornata;

            return new Risposta(
                    successo,
                    null,
                    successo
                            ? "Recensione modificata correttamente"
                            : "Errore durante la modifica della recensione"
            );

        } catch (IllegalArgumentException e) {
            return new Risposta(false, null, e.getMessage());
        } catch (Exception e) {
            return new Risposta(false, null, "Errore interno: " + e.getMessage());
        }
    }

    private Risposta gestisciEliminaRecensione(Object contenuto) {
        if(!(contenuto instanceof RecensioneDTO dto)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        else {
            try {
                RecensioneDTO recDto = (RecensioneDTO) contenuto;

                boolean successo = recensioneService.cancellaRecensione(
                        recDto.getIdRistorante(),
                        recDto.getEmail()
                );

                return new Risposta(
                        successo,
                        null,
                        successo
                                ? "Recensione eliminata correttamente"
                                : "Errore durante l'eliminazione della recensione"
                );

            } catch (Exception e) {
                return new Risposta(false, null, e.getMessage());
            }
        }
    }

    private Risposta gestisciRispondiRecensione(Object contenuto) {
        if(!(contenuto instanceof RecensioneDTO dto)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        try {
            boolean successo = recensioneService.rispostaRecensione(
                    dto.getIdRistorante(),
                    dto.getEmail(),
                    dto.getRisposta()
            );

            return new Risposta(
                    successo,
                    null,
                    successo ? "Risposta alla recensione inserita con successo"
                            : "Impossibile rispondere alla recensione"
            );

        } catch (IllegalArgumentException e) {
            return new Risposta(false, null, e.getMessage());
        }
    }

    public Risposta gestisciModificaRisposta(Object contenuto) {
        if(!(contenuto instanceof RecensioneDTO dto)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        try {
            boolean successo = recensioneService.aggiornaRisposta(
                    dto.getIdRistorante(),
                    dto.getEmail(),
                    dto.getRisposta()
            );
            return new Risposta(successo, null,
                    successo ? "Risposta aggiornata con successo"
                            : "Impossibile aggiornare la risposta"
            );

        } catch (Exception e) {
            return new Risposta(false, null, e.getMessage());
        }
    }

    public Risposta gestisciRiepilogoRecensioni(Object contenuto) {
        if (!(contenuto instanceof Integer idRistorante)) {
            return new Risposta(false, null, "Id ristorante non valido");
        }

        try {
            RiepilogoRecensioniDTO riepilogo = recensioneService.getRiepilogo(idRistorante);

            if (riepilogo == null) {
                return new Risposta(false, null, "Nessun riepilogo disponibile");
            }

            return new Risposta(true, riepilogo, "Riepilogo recensioni recuperato");

        } catch (Exception e) {
            return new Risposta(false, null, "Errore nel recupero del riepilogo: " + e.getMessage());
        }
    }

    //PREFERITI
    private Risposta gestisciGetPreferiti(Object contenuto){
        if(!(contenuto instanceof String email)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        else{
            List<Preferito> preferiti = preferitiService.getPreferitiUtente(email);
            return new Risposta(true, preferiti, "Preferiti trovate");
        }
    }

    private Risposta gestisciAggiungiPreferito(Object contenuto){
        if(!(contenuto instanceof PreferitiDTO dto)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        try {
            boolean successo = preferitiService.aggiungiPreferito(dto);

            return new Risposta(successo, null,
                    successo ? "Preferito aggiunto con successo"
                            : "Impossibile aggiungere il preferito"
            );
        } catch (Exception e) {
            return new Risposta(false, null, e.getMessage());
        }
    }

    private Risposta gestisciRimuoviPreferito(Object contenuto) {
        if (!(contenuto instanceof PreferitiDTO dto)) {
            return new Risposta(false, null, "Dati del preferito non validi");
        }

        try {
            boolean successo = preferitiService.rimuoviPreferito(dto);

            return new Risposta(successo, null,
                    successo ? "Preferito rimosso con successo"
                            : "Impossibile rimuovere il preferito"
            );

        } catch (Exception e) {
            return new Risposta(false, null, e.getMessage());
        }
    }

    private Risposta gestisciEsistenzaPreferiti(Object contenuto) {
        if (!(contenuto instanceof PreferitiDTO dto)) {
            return new Risposta(false, null, "Dati per verifica preferito non validi");
        }
        try {
            boolean esiste = preferitiService.esistePreferito(dto.getEmail(), dto.getIdRistorante());
            return new Risposta(true, esiste, esiste ? "Preferito esistente" : "Preferito non esistente");
        } catch (Exception e) {
            return new Risposta(false, null, "Errore interno: " + e.getMessage());
        }
    }

    private Risposta gestisciTogglePreferito(Object contenuto) {
        if (!(contenuto instanceof PreferitiDTO dto)) {
            return new Risposta(false, null, "Dati per toggle preferito non validi");
        }
        try {
            // Salvo lo stato precedente per poter calcolare il nuovo stato dopo il toggle
            boolean eraGiaPreferito = preferitiService.esistePreferito(dto.getEmail(), dto.getIdRistorante());

            boolean successo = preferitiService.togglePreferito(dto);

            if (!successo) {
                return new Risposta(false, null, "Impossibile aggiornare i preferiti");
            }

            boolean nuovoStato = !eraGiaPreferito; // true = ora è tra i preferiti
            String msg = nuovoStato ? "Aggiunto ai preferiti" : "Rimosso dai preferiti";
            return new Risposta(true, nuovoStato, msg);

        } catch (Exception e) {
            return new Risposta(false, null, "Errore interno: " + e.getMessage());
        }
    }

    //TIPO CUCINA
    public Risposta gestisciGetTipoCucina() {
        if (tipoCucinaService == null) {
            return new Risposta(false, null, "Errore nel recupero dei tipi di cucina");
        }
        List<TipoCucinaDTO> tipi = tipoCucinaService.getTipoCucina();
        return new Risposta(true, tipi, "Tipi di cucina recuperati");
    }


    private Risposta gestisciShutdown() {

        System.out.println("Richiesta di chiusura server ricevuta");
        ServerMain.stopServer();
        return new Risposta(
                true,
                null,
                "Server chiuso correttamente"
        );
    }
}

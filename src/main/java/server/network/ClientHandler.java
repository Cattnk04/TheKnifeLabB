package main.java.server.network;

import main.java.server.dao.*;
import main.java.server.security.PasswordService;
import main.java.server.service.*;
import main.java.shared.communication.*;
import main.java.shared.domain.Preferito;
import main.java.shared.domain.Recensione;
import main.java.shared.domain.Ristorante;
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

            case LOGOUT:
                return new Risposta(true, null, "Logout effettuato");
            //Non sono sicura sia giusto

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
        if (!(contenuto instanceof LoginDTO loginDTO)) {
            return new Risposta(false, null, "Dati login non validi");
        }

        boolean ok = utenteService.login(loginDTO);

        if (ok) {
            return new Risposta(true, loginDTO.getEmail(), "Login effettuato");
        }
        return new Risposta(false, null, "Email o password errati");
    }

    private Risposta gestisciRegistrazione(Object contenuto) {
        if (!(contenuto instanceof RegistrazioneDTO registrazioneDTO)) {
            return new Risposta(false, null, "Dati registrazione non validi");
        }

        boolean ok = utenteService.registraUtente(registrazioneDTO);

        if (ok) {
            return new Risposta(true, null, "Registrazione effettuata");
        }
        return new Risposta(false, null, "Registrazione fallita");
    }

    //RISTORANTE
    private Risposta gestisciGetRistorante(Object contenuto) {
        if(!(contenuto instanceof RistoranteDTO dto)){
            return new Risposta(false, null, "Dati ricerca non validi");
        }
        else{
            List<Ristorante> ristoranti = ristoranteService.getTuttiRistoranti();
            return new Risposta(true, ristoranti, "Ristoranti trovati");
        }
    }

    public Risposta gestisciCreaRistorante(Object contenuto) {
        return new Risposta(true, null, "Metodo non ancora implementato");
    }

    public Risposta gestisciModificaRistorante(Object contenuto) {
        return new Risposta(true, null, "Metodo non ancora implementato");
    }

    public Risposta gestisciEliminaRistorante(Object contenuto) {
        return new Risposta(true, null, "Metodo non ancora implementato");
    }

    public Risposta gestisciCercaRistorante(Object contenuto) {
        return new Risposta(true, null, "Metodo non ancora implementato");
    }

    public Risposta gestisciEsistenzaRistorante(Object contenuto) {
        return new Risposta(true, null, "Metodo non ancora implementato");
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

    private Risposta gestisciEsistenzaPreferiti(Object contenuto){
        return new Risposta(true, null, "Metodo non ancora implementato");
    }

    private Risposta gestisciTogglePreferito(Object contenuto){
        return new Risposta(true, null, "Metodo non ancora implementato");
    }

    //TIPO CUCINA
    public Risposta gestisciGetTipoCucina(){
        if(!(tipoCucinaService instanceof TipoCucinaService tipoCucinaService)){
            return new Risposta(false, null, "Errore nel recupero dei tipi di cucina");
        }
        else{
            List<TipoCucinaDTO> tipi = tipoCucinaService.getTipoCucina();
            return new Risposta(true, tipi, "Tipi di cucina recuperati");
        }
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

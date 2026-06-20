package main.java.server.network;

import main.java.server.dao.UtenteDAO;
import main.java.server.security.PasswordService;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.dto.LoginDTO;
import main.java.shared.dto.RegistrazioneDTO;
import main.java.server.main.ServerMain;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private UtenteService utenteService;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.utenteService = new UtenteService(new UtenteDAO(), new PasswordService());
    }

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

    public Risposta gestisciRichiesta(Richiesta richiesta) {
        if (richiesta == null || richiesta.getTipoRichiesta() == null) {
            return new Risposta(false, null, "Richiesta non valida");
        }

        switch (richiesta.getTipoRichiesta()) {
            case LOGIN:
                return gestisciLogin(richiesta.getContenuto());

            case REGISTER:
                return gestisciRegistrazione(richiesta.getContenuto());

            case LOGOUT:
                return new Risposta(true, null, "Logout effettuato");

            case GET_RISTORANTE:
                return gestisciGetRistorante(richiesta.getContenuto());

            case GET_RECENSIONI_RISTORANTE:
                return gestisciGetRecensioniRistorante(richiesta.getContenuto());

            case SCRIVI_RECENSIONE:
                return gestisciScriviRecensione(richiesta.getContenuto());

            case MODIFICA_RECENSIONE:
                return gestisciModificaRecensione(richiesta.getContenuto());

            case ELIMINA_RECENSIONE:
                return gestisciEliminaRecensione(richiesta.getContenuto());

            case AGGIUNGI_PREFERITO:
                return gestisciAggiungiPreferito(richiesta.getContenuto());

            case RIMUOVI_PREFERITO:
                return gestisciRimuoviPreferito(richiesta.getContenuto());

            case RISPONDI_RECENSIONE:
                return gestisciRispondiRecensione(richiesta.getContenuto());

            case SHUTDOWN_SERVER:
                return gestisciShutdown();
            default:
                return new Risposta(false, null, "Tipo richiesta non supportato");
        }
    }

    //Metodi per gestire le richieste
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

    private Risposta gestisciGetRistorante(Object contenuto) {
        return new Risposta(true, null, "Metodo non implementato");
    }

    private Risposta gestisciGetRecensioniRistorante(Object contenuto) {
        return new Risposta(false, null, "Non implementato");
    }

    private Risposta gestisciScriviRecensione(Object contenuto) {
        return new Risposta(false, null, "Non implementato");
    }

    private Risposta gestisciModificaRecensione(Object contenuto){
        return new Risposta(false, null, "Non implementato");
    }

    private Risposta gestisciEliminaRecensione(Object contenuti){
        return new Risposta(false, null, "Non implementato");
    }

    private Risposta gestisciAggiungiPreferito(Object contenuto){
        return new Risposta(false, null, "Non implementato");
    }

    private Risposta gestisciRimuoviPreferito(Object contenuto){
        return new Risposta(false, null, "Non implementato");
    }

    private Risposta gestisciRispondiRecensione(Object contenuto){
        return new Risposta(false, null, "Non implementato");
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

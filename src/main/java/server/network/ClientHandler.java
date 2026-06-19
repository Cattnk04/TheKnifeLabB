package main.java.server.network;

import main.java.server.dao.UtenteDAO;
import main.java.server.security.PasswordService;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.dto.LoginDTO;
import main.java.shared.dto.RegistrazioneDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private UtenteService utenteService;

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
        this.utenteService = new UtenteService(new UtenteDAO(), new PasswordService());
    }

    @Override
    public void run() {
        try{
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            while(true){
                Richiesta richiesta = (Richiesta) in.readObject();
                Risposta risposta = gestisciRichiesta(richiesta);

                out.writeObject(risposta);
                out.flush();
            }
        } catch(Exception e){
            System.out.println("errore nella connessione del client");
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

            default:
                return new Risposta(false, null, "Tipo richiesta non supportato");
        }
    }

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
}

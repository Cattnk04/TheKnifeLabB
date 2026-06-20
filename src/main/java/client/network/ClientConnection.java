package main.java.client.network;

import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 10000;

    // Invio generico richieste
    public static Risposta inviaRichiesta(Richiesta richiesta) {
        try (
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.writeObject(richiesta);
            out.flush();

            return (Risposta) in.readObject();

        } catch (Exception e) {
            System.out.println("Errore comunicazione con il server: " + e.getMessage());
            return new Risposta(false, null, "Impossibile comunicare con il server");
        }
    }

    //Il client chiede al server di spegnersi
    public static Risposta shutdownServer() {

        Richiesta richiesta = new Richiesta(
                TipoRichieste.SHUTDOWN_SERVER,
                null
        );

        return inviaRichiesta(richiesta);
    }
}
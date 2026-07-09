package main.java.client.network;

import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Classe di utilità responsabile della comunicazione di rete tra client e server.
 * <p>
 * Per ogni richiesta apre una nuova connessione TCP verso il server, invia
 * l'oggetto {@link Richiesta} serializzato e resta in attesa della relativa
 * {@link Risposta}. L'indirizzo del server è fissato a {@code localhost} sulla
 * porta 10000.
 * </p>
 */
public class ClientConnection {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 10000;

    /**
     * Invia una richiesta al server e restituisce la risposta ricevuta.
     * <p>
     * Apre una nuova connessione socket verso il server, invia la richiesta
     * serializzata e legge la risposta. La connessione viene chiusa
     * automaticamente al termine dell'operazione (try-with-resources).
     * </p>
     *
     * @param richiesta la {@link Richiesta} da inviare al server
     * @return la {@link Risposta} ricevuta dal server; in caso di errore di
     * comunicazione viene restituita una risposta di fallimento con messaggio esplicativo
     */
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

    /**
     * Invia al server una richiesta di arresto ({@code SHUTDOWN_SERVER}).
     *
     * @return la {@link Risposta} ricevuta dal server a seguito della richiesta di arresto
     */
    public static Risposta shutdownServer() {

        Richiesta richiesta = new Richiesta(
                TipoRichieste.SHUTDOWN_SERVER,
                null
        );

        return inviaRichiesta(richiesta);
    }
}
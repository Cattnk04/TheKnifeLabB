package main.java.server.main;

import main.java.server.network.ClientHandler;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Punto di ingresso del server applicativo.
 * <p>
 * Apre una {@link ServerSocket} sulla porta 10000 e accetta connessioni
 * in ingresso, delegando ogni client a un nuovo {@link ClientHandler}.
 */
public class ServerMain {

    /**
     * Socket di ascolto del server.
     */
    private static ServerSocket serverSocket;

    /**
     * Avvia il server e resta in ascolto di nuove connessioni client.
     *
     * @param args argomenti da riga di comando, non utilizzati
     */
    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(10000);
            System.out.println("Server aperto sulla porta 10000");

            while (!serverSocket.isClosed()) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client accettato");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (BindException e) {
            System.out.println("Porta 10000 gia' in uso. Chiudi il processo che la occupa o cambia porta.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Server terminato per errore:");
            e.printStackTrace();
        }
    }

    /**
     * Chiude la socket di ascolto del server, interrompendo l'accettazione
     * di nuove connessioni.
     */
    public static void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server chiuso correttamente");
            }
        } catch (Exception e) {
            System.out.println("Errore durante la chiusura del server");
        }
    }
}

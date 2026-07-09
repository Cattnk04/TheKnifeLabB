package main.java.server.main;

import main.java.server.network.ClientHandler;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 */
public class ServerMain {

    /**
     *
      */
    private static ServerSocket serverSocket;

    /**
     *
     * @param args
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
        } catch (Exception e) {
            System.out.println("Server terminato");
        }
    }

    /**
     *
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

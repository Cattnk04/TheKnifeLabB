package main.java.server.main;

import main.java.server.network.ClientHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String args[]) {
        try{
            ServerSocket serverSocket = new ServerSocket(10000);
            System.out.println("server aperto su porta 10000");
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("client accettato");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

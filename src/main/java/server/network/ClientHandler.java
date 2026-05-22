package main.java.server.network;

import com.sun.net.httpserver.Request;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
        try{
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            while(true){
                Richiesta richiesta = (Richiesta) in.readObject();
                Risposta risposta = gestisciRicheista(richiesta);
            }
        }catch(Exception e){
            System.out.println("errore nella connessione del client");
        }
    }
    public Risposta gestisciRicheista(Richiesta richiesta){
        switch(richiesta.getTipoRichiesta()){
            case TipoRichieste.LOGIN:
                //funzione per login
                break;
            /* DA FARE TUTTE LE FUNZIONI PER LE DIVERSE RICHIESTE*/
        }
        return null;
    }
}

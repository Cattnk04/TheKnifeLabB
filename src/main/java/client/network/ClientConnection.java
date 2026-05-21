package main.java.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
    Socket socket = null;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    public ClientConnection() throws IOException {
        socket = new Socket("localhost",10000);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }
}

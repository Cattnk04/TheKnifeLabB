package main.java.client.main;

import main.java.client.gui.menu.GuestGUI;
import main.java.client.network.ClientConnection;
import main.java.server.dao.UtenteDAO;
import main.java.server.security.PasswordService;
import main.java.server.service.UtenteService;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import javax.swing.*;

public class ClientMain {
    public static void main(String args[]) {
        UtenteDAO utenteDAO = new UtenteDAO();
        PasswordService passwordService = new PasswordService();
        UtenteService utenteService = new UtenteService(utenteDAO,  passwordService);
        JFrame frame = new JFrame("TheKnife");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);        frame.setSize(500, 500);
        frame.setContentPane(new GuestGUI(frame,utenteService));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                System.out.println("Chiusura client + richiesta shutdown server");

                try {
                    ClientConnection.shutdownServer();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                frame.dispose();
                System.exit(0);
            }
        });
    }
}

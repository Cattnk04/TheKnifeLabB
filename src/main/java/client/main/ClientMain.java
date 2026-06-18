package main.java.client.main;

import main.java.client.gui.menu.GuestGUI;
import main.java.client.gui.ricerca.FiltriRicercaGUI;
import main.java.server.dao.UtenteDAO;
import main.java.server.security.PasswordService;
import main.java.server.service.UtenteService;

import javax.swing.*;

public class ClientMain {
    public static void main(String args[]) {
        UtenteDAO utenteDAO = new UtenteDAO();
        PasswordService passwordService = new PasswordService();
        UtenteService utenteService = new UtenteService(utenteDAO,  passwordService);
        JFrame frame = new JFrame("TheKnife");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setContentPane(new GuestGUI(frame,utenteService));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}

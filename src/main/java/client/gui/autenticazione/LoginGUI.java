package main.java.client.gui.autenticazione;

import javax.swing.*;

public class LoginGUI extends JPanel {
    JFrame frame;
    public LoginGUI(JFrame frame) {
        this.frame = frame;
        JButton logo = new JButton("Logo");

        this.add(logo);
    }
}

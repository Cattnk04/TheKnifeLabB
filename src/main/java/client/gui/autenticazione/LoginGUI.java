package main.java.client.gui.autenticazione;

import main.java.client.gui.TemplateGUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginGUI extends TemplateGUI {

    public LoginGUI(JFrame frame) {
        super(frame);
        this.frame = frame;

        JButton registrazione = new JButton("Registrazione");
        registrazione.setFocusPainted(false);
        registrazione.setBorder(new LineBorder(Color.WHITE));
        registrazione.addActionListener(e ->{
            frame.setContentPane(new RegistrazioneGUI(frame));
            frame.revalidate();
            frame.repaint();
        });

    }
}

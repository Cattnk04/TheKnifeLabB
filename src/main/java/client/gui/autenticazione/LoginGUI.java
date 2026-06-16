package main.java.client.gui.autenticazione;

import jdk.dynalink.linker.GuardingDynamicLinkerExporter;
import main.java.client.gui.TemplateGUI;
import main.java.client.gui.menu.GuestGUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginGUI extends TemplateGUI {
    private JTextField campoEmail;
    private JPasswordField campoPassword;

    public LoginGUI(JFrame frame) {
        super(frame);
        this.frame = frame;

        //Creazione pannello verticale con BoxLayout (asse Y)
        JPanel pannelloCentrale = new JPanel();
        pannelloCentrale.setLayout(new BoxLayout(pannelloCentrale, BoxLayout.Y_AXIS));
        pannelloCentrale.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        Dimension dimensioneCampo = new Dimension(300, 40);

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoEmail = new JTextField();
        campoEmail.setPreferredSize(dimensioneCampo);
        campoEmail.setMinimumSize(dimensioneCampo);
        campoEmail.setMaximumSize(dimensioneCampo);
        campoEmail.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoPassword = new JPasswordField();
        campoPassword.setPreferredSize(dimensioneCampo);
        campoPassword.setMinimumSize(dimensioneCampo);
        campoPassword.setMaximumSize(dimensioneCampo);
        campoPassword.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton effettuaLogin = new JButton("Effettua Login");
        effettuaLogin.setPreferredSize(dimensioneCampo);
        effettuaLogin.setMinimumSize(dimensioneCampo);
        effettuaLogin.setMaximumSize(dimensioneCampo);
        effettuaLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        effettuaLogin.setFocusPainted(false);
        effettuaLogin.setBorder(new LineBorder(Color.WHITE));


        JButton registrazione = new JButton("Non sei registrato? Registrati qui!");
        registrazione.setPreferredSize(dimensioneCampo);
        registrazione.setMinimumSize(dimensioneCampo);
        registrazione.setMaximumSize(dimensioneCampo);
        registrazione.setAlignmentX(Component.CENTER_ALIGNMENT);
        registrazione.addActionListener(e ->{
            frame.setContentPane(new RegistrazioneGUI(frame));
            frame.revalidate();
            frame.repaint();
        });
        registrazione.setFocusPainted(false);
        registrazione.setBorder(new LineBorder(Color.WHITE));


        pannelloCentrale.add(emailLabel);
        pannelloCentrale.add(Box.createVerticalStrut(5));
        pannelloCentrale.add(campoEmail);
        pannelloCentrale.add(Box.createVerticalStrut(40));

        pannelloCentrale.add(passwordLabel);
        pannelloCentrale.add(Box.createVerticalStrut(5));
        pannelloCentrale.add(campoPassword);
        pannelloCentrale.add(Box.createVerticalStrut(40));

        pannelloCentrale.add(effettuaLogin);
        pannelloCentrale.add(Box.createVerticalStrut(40));
        pannelloCentrale.add(registrazione);

        //centratura del pannelloCentrale
        JPanel centroPannello = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centroPannello.add(pannelloCentrale);
        add(centroPannello, BorderLayout.CENTER);

        JButton home = new JButton("Home");
        home.setFocusPainted(false);
        home.setBorder(new LineBorder(Color.WHITE));
        home.addActionListener(e ->{
            frame.setContentPane(new GuestGUI(frame));
            frame.revalidate();
            frame.repaint();
        });
        pannello.add(home);

    }
}

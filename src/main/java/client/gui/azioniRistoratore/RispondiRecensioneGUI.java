package main.java.client.gui.azioniRistoratore;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.menu.RistoratoreGUI;
import main.java.server.service.UtenteService;

import javax.swing.*;

public class RispondiRecensioneGUI extends TemplateGUI {

    public RispondiRecensioneGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;
        String testoRecensione = "aggiungere query per ricevere la recensione del DB";
        JLabel lblTestoRecensione = new JLabel(testoRecensione);
        this.add(lblTestoRecensione);
        JTextArea tfRisposta = new JTextArea();
        tfRisposta.setEditable(true);
        tfRisposta.setSize(300, 300);

        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });
        pannello.add(visualizzaProfilo);

        JButton home = new JButton("Home");
        home.addActionListener(e -> {
            frame.setContentPane(new RistoratoreGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });
        pannello.add(home);
    }
}

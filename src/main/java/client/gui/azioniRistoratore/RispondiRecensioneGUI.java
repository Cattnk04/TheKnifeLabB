package main.java.client.gui.azioniRistoratore;

import main.java.client.gui.TemplateGUI;

import javax.swing.*;

public class RispondiRecensioneGUI extends TemplateGUI {

    public RispondiRecensioneGUI(JFrame frame) {
        super(frame);
        this.frame = frame;
        String testoRecensione = "aggiungere query per ricevere la recensione del DB";
        JLabel lblTestoRecensione = new JLabel(testoRecensione);
        this.add(lblTestoRecensione);
        JTextArea tfRisposta = new JTextArea();
        tfRisposta.setEditable(true);
        tfRisposta.setSize(300, 300);
    }
}

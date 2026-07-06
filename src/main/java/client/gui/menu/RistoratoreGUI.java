package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniRistoratore.AggiungiRistoranteGUI;
import main.java.server.service.UtenteService;

import javax.swing.*;
import java.awt.*;

public class RistoratoreGUI extends TemplateGUI {

    public RistoratoreGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;
        JPanel pannelloCentrale = new JPanel();
        JButton aggiungiRistorante = new JButton("Aggiungi ristorante");
        aggiungiRistorante.addActionListener(e -> {
            frame.setContentPane(new AggiungiRistoranteGUI(frame, email));
            frame.revalidate();
            frame.repaint();
        });
        // Solo per provare la gui che sto facendo
        pannelloCentrale.add(aggiungiRistorante);
    }
}

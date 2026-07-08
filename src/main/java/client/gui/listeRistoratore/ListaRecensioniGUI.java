package main.java.client.gui.listeRistoratore;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.menu.RistoratoreGUI;
import main.java.server.service.UtenteService;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;

public class ListaRecensioniGUI extends TemplateGUI {

    public ListaRecensioniGUI(JFrame frame, UtenteService utenteService, String email, RistoranteDTO ristorante){
        super(frame);
        this.frame = frame;

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

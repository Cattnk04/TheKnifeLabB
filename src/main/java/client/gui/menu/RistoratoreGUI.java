package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.ricerca.FiltriRicercaGUI;

import javax.swing.*;
import java.awt.*;

public class RistoratoreGUI extends TemplateGUI {
    public RistoratoreGUI(JFrame frame) {
        super(frame);
        JButton filtriRicerca = new JButton("Cambia panel in Filtri Ricerca GUI");
        frame.setTitle("Ristoratore");
        filtriRicerca.addActionListener(e -> {
            frame.setContentPane(new FiltriRicercaGUI(frame));
            frame.revalidate();
            frame.repaint();
        });
        this.add(filtriRicerca);
    }
}

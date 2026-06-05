package main.java.client.gui.ricerca;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.menu.RistoratoreGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.EventListener;

public class FiltriRicercaGUI extends TemplateGUI {
    public FiltriRicercaGUI(JFrame frame) {
        super(frame);
        this.frame = frame;
        JButton filtriRicerca = new JButton("cambia panel in Ristoratore GUI");
        filtriRicerca.addActionListener(e -> {
            azione();
        });
        this.add(filtriRicerca);
    }
    public void azione(){
        frame.setContentPane(new RistoratoreGUI(frame));
        frame.revalidate();
        frame.repaint();
    }
}

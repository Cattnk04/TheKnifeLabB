package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;

import javax.swing.*;

public class LoggatoGUI extends TemplateGUI {

    public LoggatoGUI(JFrame frame) {
        super(frame);
        this.frame = frame;

        visualizzaProfilo.addActionListener(e ->{
            frame.setContentPane(new VisualizzaProfiloGUI(frame));
            frame.revalidate();
            frame.repaint();})
        ;
    }
}

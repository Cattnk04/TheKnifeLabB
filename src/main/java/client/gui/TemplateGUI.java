package main.java.client.gui;

import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.menu.GuestGUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.Graphics;
import java.awt.FlowLayout;


public class TemplateGUI extends JPanel {
    protected JFrame frame;
    /*si usa protected per far sì che le sottoclassi che estendono
    TemplateGUI possano accedere agli elementi senza bisogno di getter
     */
    protected JPanel pannello;
    protected JButton visualizzaProfilo;
    public TemplateGUI(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        //creazione bottone logo --> poi da portare a icon
        JButton logo = new JButton("Logo");
        logo.setPreferredSize(new Dimension(150,60 ));
        logo.setFocusable(false);
        logo.setBorder(new LineBorder(Color.WHITE));

        //Creazione bottone profilo
        visualizzaProfilo = new JButton("Profilo");
        logo.setPreferredSize(new Dimension(150,60 ));
        visualizzaProfilo.setFocusable(false);
        visualizzaProfilo.setBorder(new LineBorder(Color.WHITE));

        //creazione pannello colorato
        pannello = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15)){ //centra il logo nel pannello
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pannello.setOpaque(false);
        pannello.add(logo);
        pannello.add(visualizzaProfilo);
        add(pannello, BorderLayout.NORTH);


    }
}

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
    protected JButton logout;

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
        visualizzaProfilo.setFocusable(false);
        visualizzaProfilo.setBorder(new LineBorder(Color.WHITE));

        //Creazione bottone logout
        logout = new JButton("Logout");
        logout.setFocusable(false);
        logout.setBorder(new LineBorder(Color.WHITE));
        logout.setVisible(false); // visibile solo nelle GUI di utenti loggati

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
        pannello.add(logout);
        add(pannello, BorderLayout.NORTH);


        //METODO GIUSTO: DA CAMBIARE TUTTI I BOTTONI
        //Creazione pannello colorato
        /*pannello = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLUE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pannello.setOpaque(false);

        //pannello centrato contenente solo il logo
        JPanel pannelloLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        pannelloLogo.setOpaque(false);
        pannelloLogo.add(logo);

        //Pannello a destra contenente i bottoni
        JPanel pannelloBottoniDX = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        pannelloBottoniDX.setOpaque(false);
        pannelloBottoniDX.add(visualizzaProfilo);
        pannelloBottoniDX.add(logout);

        //Pannello a sinistra per centrare il pannello centrale e se si vuole si possono aggiungere dei bottoni al suo interno
        JPanel pannelloBottoniSX = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        pannelloBottoniSX.setOpaque(false);

        pannello.add(pannelloBottoniSX, BorderLayout.WEST);
        pannello.add(pannelloLogo, BorderLayout.CENTER);
        pannello.add(pannelloBottoniDX, BorderLayout.EAST);

        add(pannello, BorderLayout.NORTH);

         */

    }
}

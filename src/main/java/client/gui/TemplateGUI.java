package main.java.client.gui;

import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.menu.GuestGUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.util.Objects;

/**
 *
 */
public class TemplateGUI extends JPanel {
    protected JFrame frame;
    protected JPanel pannello;
    protected JButton visualizzaProfilo;
    protected JButton logout;

    /**
     *
     * @param frame
     */
    public TemplateGUI(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        //immagine del logo nella navbar in alto
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.png"));
        Image immagine = icon.getImage().getScaledInstance(80, 80 , Image.SCALE_SMOOTH);
        ImageIcon logo1 = new ImageIcon(immagine);

        frame.setIconImage(immagine);

        //creazione label logo --> poi da portare a icon
        JLabel logo = new JLabel(logo1);
        logo.setPreferredSize(new Dimension(80,80 ));
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

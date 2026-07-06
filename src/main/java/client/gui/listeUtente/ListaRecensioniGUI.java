package main.java.client.gui.listeUtente;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.server.service.UtenteService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ListaRecensioniGUI extends TemplateGUI {
    JFrame frame;
    private final UtenteService utenteService;
    private final String email;

    public ListaRecensioniGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;
        this.utenteService = utenteService;
        this.email = email;

        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

        JPanel pannelloCentrale = new JPanel(new BorderLayout());

        // TODO: sostituire con la richiesta al server (es. TipoRichieste.RECUPERA_RECENSIONI)
        // e popolare un JList/JTable con i risultati (RegistrazioneDTO / RecensioneDTO ecc.)
        JLabel placeholder = new JLabel("Elenco recensioni non ancora disponibile", SwingConstants.CENTER);
        pannelloCentrale.add(placeholder, BorderLayout.CENTER);

        JButton indietro = new JButton("Torna al profilo");
        indietro.setFocusPainted(false);
        indietro.setBorder(new LineBorder(Color.WHITE));
        indietro.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

        JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pannelloBottoni.add(indietro);
        pannelloCentrale.add(pannelloBottoni, BorderLayout.SOUTH);

        add(pannelloCentrale, BorderLayout.CENTER);
    }
}

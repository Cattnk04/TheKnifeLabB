package main.java.client.gui.azioniRistoratore;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.menu.RistoratoreGUI;
import main.java.server.service.UtenteService;
import main.java.shared.dto.RecensioneDTO;

import javax.swing.*;

public class RispondiRecensioneGUI extends TemplateGUI {

    public RispondiRecensioneGUI(JFrame frame, UtenteService utenteService, String email, String nomeRistorante, RecensioneDTO recensione) {
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

        JPanel pannelloDatiRecensione = new JPanel();
        JLabel labelNomeRistorante = new JLabel("Ristorante: " + nomeRistorante);
        JLabel labelTesto = new JLabel(recensione.getRecensione());

        pannelloDatiRecensione.add(labelNomeRistorante);
        pannelloDatiRecensione.add(labelTesto);

        JPanel pannelloRisposta = new JPanel();
        JTextArea textAreaRisposta = new JTextArea();
        textAreaRisposta.setEditable(true);
        JTextField rispondiQui = new JTextField("Inserisci qui la risposta alla recensione scritta sopra");
        pannelloRisposta.add(rispondiQui);
        pannelloRisposta.add(textAreaRisposta);

        pannello.add(pannelloDatiRecensione);
        pannello.add(pannelloRisposta);
    }
}

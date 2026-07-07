package main.java.client.gui.azioniLoggato;

import main.java.client.gui.TemplateGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.dto.RecensioneDTO;

import javax.swing.*;
import java.awt.*;

public class ScriviRecensioneGUI extends TemplateGUI {

    private JSpinner spinnerStelle;
    private JTextArea testoRecensione;
    private JButton bottoneInvia;

    private final int idRistorante;
    private final String nomeRistorante;
    private final JFrame frame;

    public ScriviRecensioneGUI(JFrame frame, int idRistorante, String nomeRistorante) {
        super(frame);
        this.frame = frame;
        this.idRistorante = idRistorante;
        this.nomeRistorante = nomeRistorante;
        costruisciInterfaccia();
    }

    private void costruisciInterfaccia() {
        JPanel contenuto = new JPanel();
        contenuto.setLayout(new BoxLayout(contenuto, BoxLayout.Y_AXIS));
        contenuto.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        //Titolo
        JLabel titolo = new JLabel("Scrivi una recensione per il ristorante " + nomeRistorante);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenuto.add(titolo);
        contenuto.add(Box.createRigidArea(new Dimension(0, 15)));

        //Valutazione
        JLabel labelValutazione = new JLabel("Valutazione (1-5): ");
        labelValutazione.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenuto.add(labelValutazione);

        SpinnerNumberModel modelloStelle  = new SpinnerNumberModel(5, 1, 5, 1);
        spinnerStelle = new JSpinner(modelloStelle);
        spinnerStelle.setMaximumSize(new Dimension(70, 30));
        spinnerStelle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenuto.add(spinnerStelle);
        contenuto.add(Box.createRigidArea(new Dimension(0, 15)));

        //Campo di testo per la recensione
        JLabel labelTesto = new JLabel("Testo della recensione:");
        labelTesto.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenuto.add(labelTesto);

        testoRecensione = new JTextArea(6, 30);
        testoRecensione.setLineWrap(true);
        testoRecensione.setWrapStyleWord(true);
        JScrollPane scrollTesto = new JScrollPane(testoRecensione);
        scrollTesto.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenuto.add(scrollTesto);
        contenuto.add(Box.createRigidArea(new Dimension(0, 20)));

        //Bottone invio
        bottoneInvia = new JButton("Invia recesione");
        bottoneInvia.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottoneInvia.addActionListener(e -> inviaRecensione());
        contenuto.add(bottoneInvia );

        add(contenuto, BorderLayout.CENTER);
    }

    private void inviaRecensione() {
        int valutazione = (int) spinnerStelle.getValue();
        String testo = testoRecensione.getText().trim();

        if (testo.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Il testo della recensione non può essere vuoto.",
                    "Errore",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        RecensioneDTO dto = new RecensioneDTO(idRistorante, valutazione, testo);
        Richiesta richiesta = new Richiesta(TipoRichieste.SCRIVI_RECENSIONE, dto);

        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        if (risposta.getSuccesso()) {
            JOptionPane.showMessageDialog(frame,
                    risposta.getMsg(),
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
            testoRecensione.setText("");
        } else {
            JOptionPane.showMessageDialog(frame,
                    risposta.getMsg(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

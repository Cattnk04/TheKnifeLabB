package main.java.client.gui.azioniLoggato;

import main.java.client.gui.TemplateGUI;
import main.java.client.network.ClientConnection;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.dto.RecensioneDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
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
        JPanel contenuto = new JPanel(new GridBagLayout());
        contenuto.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints vincolo = new GridBagConstraints();
        vincolo.anchor = GridBagConstraints.WEST;

        // Titolo (centrato, su due colonne)
        JLabel titolo = new JLabel("Scrivi una recensione per il ristorante " + nomeRistorante);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 16));
        vincolo.gridx = 0;
        vincolo.gridy = 0;
        vincolo.gridwidth = 2;
        vincolo.anchor = GridBagConstraints.CENTER;
        vincolo.insets = new Insets(0, 10, 20, 10);
        contenuto.add(titolo, vincolo);

        // Valutazione
        vincolo.gridwidth = 1;
        vincolo.anchor = GridBagConstraints.WEST;
        vincolo.insets = new Insets(8, 10, 8, 10);
        vincolo.gridx = 0;
        vincolo.gridy = 1;
        contenuto.add(new JLabel("Valutazione (1-5): "), vincolo);

        SpinnerNumberModel modelloStelle = new SpinnerNumberModel(5, 1, 5, 1);
        spinnerStelle = new JSpinner(modelloStelle);
        spinnerStelle.setPreferredSize(new Dimension(70, 28));
        vincolo.gridx = 1;
        contenuto.add(spinnerStelle, vincolo);

        // Testo recensione
        vincolo.gridx = 0;
        vincolo.gridy = 2;
        vincolo.gridwidth = 2;
        contenuto.add(new JLabel("Testo della recensione:"), vincolo);

        testoRecensione = new JTextArea(8, 40);
        testoRecensione.setLineWrap(true);
        testoRecensione.setWrapStyleWord(true);
        JScrollPane scrollTesto = new JScrollPane(testoRecensione);

        vincolo.gridy = 3;
        vincolo.fill = GridBagConstraints.BOTH;
        contenuto.add(scrollTesto, vincolo);

        // Bottone invio (centrato)
        bottoneInvia = new JButton("Invia recensione");
        bottoneInvia.setFocusPainted(false);
        bottoneInvia.setBorder(new LineBorder(Color.WHITE));
        bottoneInvia.addActionListener(e -> inviaRecensione());

        vincolo.gridx = 0;
        vincolo.gridy = 4;
        vincolo.gridwidth = 2;
        vincolo.fill = GridBagConstraints.NONE;
        vincolo.anchor = GridBagConstraints.CENTER;
        vincolo.insets = new Insets(20, 10, 8, 10);
        contenuto.add(bottoneInvia, vincolo);

        // Centra il form nella pagina, coerente con lo stile delle altre GUI
        JPanel centro = new JPanel(new GridBagLayout());
        centro.add(contenuto);
        add(centro, BorderLayout.CENTER);
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
package main.java.client.gui.azioniLoggato;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.utils.DettagliRistoranteGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.dto.RecensioneDTO;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Schermata che permette a un utente autenticato di scrivere una nuova
 * recensione per un ristorante.
 * <p>
 * Mostra un form con la valutazione (1-5 stelle) e il testo della recensione,
 * inviando la richiesta al server al momento della conferma. Consente inoltre
 * di tornare alla schermata di dettaglio del ristorante ({@link DettagliRistoranteGUI}).
 * </p>
 */
public class ScriviRecensioneGUI extends TemplateGUI {
    private JSpinner spinnerStelle;
    private JTextArea testoRecensione;
    private JButton bottoneInvia;

    private final int idRistorante;
    private final String nomeRistorante;
    private final JFrame frame;
    private final String email;
    private final UtenteService utenteService;
    private final RistoranteDTO ristorante;

    /**
     * Costruisce la schermata per scrivere una recensione, memorizzando i
     * riferimenti al ristorante e all'utente e predisponendo l'interfaccia
     * del form.
     *
     * @param frame la finestra principale dell'applicazione
     * @param utenteService il service utilizzato per le operazioni sugli utenti
     * @param email l'email dell'utente che scrive la recensione
     * @param ristorante il {@link RistoranteDTO} del ristorante da recensire
     */
    public ScriviRecensioneGUI(JFrame frame, UtenteService utenteService, String email, RistoranteDTO ristorante) {
        super(frame);
        this.frame = frame;
        this.utenteService = utenteService;
        this.email = email;
        this.ristorante = ristorante;
        this.idRistorante = ristorante.getIdRistorante();
        this.nomeRistorante = ristorante.getNomeRistorante();

        costruisciInterfaccia();
    }

    /**
     * Costruisce e assembla i componenti grafici del form: titolo, selettore
     * della valutazione, area di testo per la recensione e i pulsanti "Invia
     * recensione" e "Indietro".
     */
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

        JButton indietro = new JButton("← Indietro");
        indietro.setFocusPainted(false);
        indietro.setBorder(new LineBorder(Color.WHITE));
        indietro.addActionListener(e -> {
            frame.setContentPane(new DettagliRistoranteGUI(frame, utenteService, email, ristorante));
            frame.revalidate();
            frame.repaint();

        });

        vincolo.gridy = 5;
        vincolo.insets = new Insets(10, 10, 10, 10);
        contenuto.add(indietro, vincolo);

        // Centra il form nella pagina, coerente con lo stile delle altre GUI
        JPanel centro = new JPanel(new GridBagLayout());
        centro.add(contenuto);
        add(centro, BorderLayout.CENTER);
    }

    /**
     * Convalida il testo della recensione (verificando che non sia vuoto) e
     * invia al server la richiesta di creazione della recensione con la
     * valutazione e il testo inseriti. Mostra un messaggio di esito e, in
     * caso di successo, svuota il campo di testo.
     */
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

        RecensioneDTO dto = new RecensioneDTO(email, idRistorante, valutazione, testo, null);
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
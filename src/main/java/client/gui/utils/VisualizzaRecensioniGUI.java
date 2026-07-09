package main.java.client.gui.utils;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.*;
import main.java.shared.domain.Recensione;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * GUI di sola visualizzazione delle recensioni di un ristorante.
 * Accessibile sia da utente ospite che da utente loggato (cliente o ristoratore):
 * mostra l'elenco delle recensioni esistenti, senza possibilità di scriverne.
 * Il tasto "Indietro" riporta a DettagliRistoranteGUI dello stesso ristorante.
 */
public class VisualizzaRecensioniGUI extends TemplateGUI {

    private final JFrame frame;
    private final UtenteService utenteService;
    private final String email; //null se utente ospite
    private final RistoranteDTO ristorante;

    private final DefaultListModel<Recensione> modelloLista;
    private final JList<Recensione> listaRecensioni;
    private final JLabel labelContatore;

    /**
     * Costruisce la schermata di sola visualizzazione delle recensioni di
     * un ristorante, caricandole dal server e mostrando il pulsante "Logout"
     * solo se l'utente è autenticato (email non nulla).
     *
     * @param frame la finestra principale dell'applicazione
     * @param utenteService il service utilizzato per le operazioni sugli utenti
     * @param email l'email dell'utente autenticato, oppure {@code null} se ospite
     * @param ristorante il {@link RistoranteDTO} di cui visualizzare le recensioni
     */
    public VisualizzaRecensioniGUI(JFrame frame, UtenteService utenteService,
                                   String email, RistoranteDTO ristorante) {
        super(frame);
        this.frame = frame;
        this.utenteService = utenteService;
        this.email = email;
        this.ristorante = ristorante;

        //rimuovo il bottone "Profilo" (come in DettagliRistoranteGUI)
        pannello.remove(visualizzaProfilo);
        pannello.revalidate();
        pannello.repaint();

        // Bottone logout visibile solo se utente loggato
        if (email != null) {
            logout.setVisible(true);
            logout.addActionListener(e -> {
                Richiesta richiestaLogout = new Richiesta(TipoRichieste.LOGOUT, email);
                ClientConnection.inviaRichiesta(richiestaLogout);

                frame.setContentPane(new LoginGUI(frame, utenteService));
                frame.revalidate();
                frame.repaint();
            });
        }

        //Lista recensioni
        this.modelloLista = new DefaultListModel<>();
        this.listaRecensioni = new JList<>(modelloLista);
        this.listaRecensioni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRecensioni.setCellRenderer(new RecensioneCellRenderer());
        this.listaRecensioni.setFixedCellHeight(100);

        JScrollPane scrollPane = new JScrollPane(listaRecensioni);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        this.labelContatore = new JLabel("Caricamento recensioni...");
        this.labelContatore.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel titolo = new JLabel(
                "Recensioni di " + ristorante.getNomeRistorante(),
                SwingConstants.CENTER
        );

        titolo.setFont(titolo.getFont().deriveFont(Font.BOLD,18f));

        JPanel centro = new JPanel(new BorderLayout());

        centro.add(titolo, BorderLayout.NORTH);
        centro.add(scrollPane, BorderLayout.CENTER);
        centro.add(labelContatore, BorderLayout.SOUTH);

        add(centro, BorderLayout.CENTER);

        JButton indietro = new JButton("Indietro");
        indietro.setFocusPainted(false);

        indietro.addActionListener(e -> {

            frame.setContentPane(
                    new DettagliRistoranteGUI(
                            frame,
                            utenteService,
                            email,
                            ristorante
                    )
            );

            frame.revalidate();
            frame.repaint();

        });

        JPanel pannelloSud = new JPanel();
        pannelloSud.add(indietro);

        add(pannelloSud, BorderLayout.SOUTH);

        caricaRecensioni();
    }

    /**
     * Recupera dal server le recensioni relative a questo ristorante e
     * aggiorna la lista visualizzata, mostrando un messaggio appropriato in
     * caso di errore di comunicazione, esito negativo o assenza di recensioni.
     */
    private void caricaRecensioni() {

        Richiesta richiesta = new Richiesta(
                TipoRichieste.GET_RECENSIONI_RISTORANTE,
                ristorante.getIdRistorante()
        );

        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);
        modelloLista.clear();

        if (risposta == null) {
            labelContatore.setText("Errore di comunicazione.");
            return;
        }
        if (!risposta.getSuccesso()) {
            labelContatore.setText(risposta.getMsg());
            return;
        }

        @SuppressWarnings("unchecked")
        List<Recensione> recensioni =
                (List<Recensione>) risposta.getContenuto();
        if (recensioni == null || recensioni.isEmpty()) {
            labelContatore.setText("Nessuna recensione presente.");
            return;
        }
        for (Recensione r : recensioni) {
            modelloLista.addElement(r);
        }

        labelContatore.setText(
                "Totale recensioni: " + recensioni.size()
        );
    }

    /**
     * Renderer personalizzato per mostrare le informazioni principali di
     * ogni recensione (valutazione in stelle e testo), in forma anonima.
     */
    private static class RecensioneCellRenderer extends JPanel implements ListCellRenderer<Recensione> {

        private final JLabel labelIntestazione = new JLabel();
        private final JLabel labelTesto = new JLabel();

        /**
         * Costruisce il renderer, impostando il layout e lo stile delle
         * etichette che mostrano l'intestazione (voto in stelle) e il testo della recensione.
         */
        public RecensioneCellRenderer() {
            setLayout(new GridLayout(2, 1));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            labelIntestazione.setFont(labelIntestazione.getFont().deriveFont(Font.BOLD, 13f));
            labelTesto.setFont(labelTesto.getFont().deriveFont(Font.PLAIN, 12f));
            labelTesto.setForeground(Color.DARK_GRAY);
            add(labelIntestazione);
            add(labelTesto);
        }

        /**
         * Costruisce e restituisce il componente grafico utilizzato per
         * visualizzare una singola cella della lista, mostrando la
         * valutazione in stelle e il testo della recensione (in forma anonima).
         *
         * @param list il {@link JList} in fase di rendering
         * @param recensione la {@link Recensione} da visualizzare per questa cella
         * @param index indice della cella nella lista
         * @param isSelected true se la cella è attualmente selezionata
         * @param cellHasFocus true se la cella ha il focus
         * @return il componente grafico da visualizzare per la cella
         */
        @Override
        public Component getListCellRendererComponent(JList<? extends Recensione> list, Recensione recensione,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            int voto = recensione.getValutazione();
            String testo = recensione.getRecensione();

            String stelle = "";

            for (int i = 0; i < voto; i++) {
                stelle += "★";
            }

            labelIntestazione.setText(
                    "Anonimo" + "   " + stelle + " (" + voto + "/5)"
            );

            labelTesto.setText(
                    "<html><body style='width:420px'>" +
                            testo +
                            "</body></html>"
            );

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                labelIntestazione.setForeground(list.getSelectionForeground());
                labelTesto.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                labelIntestazione.setForeground(Color.BLACK);
                labelTesto.setForeground(Color.DARK_GRAY);
            }
            setOpaque(true);

            return this;
        }
    }
}
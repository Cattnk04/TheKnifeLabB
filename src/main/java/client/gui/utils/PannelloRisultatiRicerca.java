package main.java.client.gui.utils;

import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Pannello che mostra l'elenco dei ristoranti trovati da una ricerca.
 * Ogni elemento della lista, se selezionato/doppio click, invoca la callback
 * onRistoranteSelezionato passando il RistoranteDTO corrispondente
 * (utile per aprire una GUI di dettaglio/prenotazione).
 */
public class PannelloRisultatiRicerca extends JPanel {

    private final DefaultListModel<RistoranteDTO> modelloLista;
    private final JList<RistoranteDTO> listaRistoranti;
    private final JLabel labelContatore;

    /**
     * Costruisce il pannello dei risultati di ricerca, predisponendo la lista
     * dei ristoranti, il relativo renderer, e i listener per l'apertura dei
     * dettagli (tramite doppio click o pulsante dedicato).
     *
     * @param onRistoranteSelezionato azione da eseguire quando l'utente
     * seleziona un ristorante (con doppio click o tramite il pulsante
     * "Visualizza dettagli"), ricevendo il {@link RistoranteDTO} selezionato
     */
    public PannelloRisultatiRicerca(Consumer<RistoranteDTO> onRistoranteSelezionato) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Risultati della ricerca"));

        this.modelloLista = new DefaultListModel<>();
        this.listaRistoranti = new JList<>(modelloLista);
        this.listaRistoranti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRistoranti.setCellRenderer(new RistoranteCellRenderer());
        this.listaRistoranti.setFixedCellHeight(70);

        // Selezione con doppio click -> apre i dettagli/prenotazione
        this.listaRistoranti.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    RistoranteDTO selezionato = listaRistoranti.getSelectedValue();
                    if (selezionato != null && onRistoranteSelezionato != null) {
                        onRistoranteSelezionato.accept(selezionato);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaRistoranti);
        scrollPane.setPreferredSize(new Dimension(500, 350));

        this.labelContatore = new JLabel("Nessuna ricerca effettuata");
        this.labelContatore.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(labelContatore, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Pulsante extra per aprire i dettagli anche con selezione singola + click
        JButton btnDettagli = new JButton("Visualizza dettagli");
        btnDettagli.addActionListener(e -> {
            RistoranteDTO selezionato = listaRistoranti.getSelectedValue();
            if (selezionato != null && onRistoranteSelezionato != null) {
                onRistoranteSelezionato.accept(selezionato);
            } else if (selezionato == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleziona prima un ristorante dalla lista.",
                        "Nessuna selezione",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pannelloBottoni.add(btnDettagli);
        add(pannelloBottoni, BorderLayout.SOUTH);
    }

    /**
     * Aggiorna la lista con i nuovi risultati di ricerca, aggiornando anche
     * l'etichetta con il conteggio dei ristoranti trovati.
     * Da chiamare nella callback passata a {@code PannelloRicercaRistorante}.
     *
     * @param risultati la lista di ristoranti da mostrare, o {@code null}/vuota
     * se la ricerca non ha prodotto risultati
     */
    public void aggiornaRisultati(List<RistoranteDTO> risultati) {
        modelloLista.clear();
        if (risultati == null || risultati.isEmpty()) {
            labelContatore.setText("Nessun ristorante trovato con questi filtri");
            return;
        }
        for (RistoranteDTO ristorante : risultati) {
            modelloLista.addElement(ristorante);
        }
        labelContatore.setText(risultati.size() + " ristorant" + (risultati.size() == 1 ? "e trovato" : "i trovati"));
    }

    /**
     * Renderer personalizzato per mostrare le informazioni principali del
     * ristorante (nome, città, nazione, delivery, prenotazione online) in
     * ogni riga della lista.
     */
    private static class RistoranteCellRenderer extends JPanel implements ListCellRenderer<RistoranteDTO> {

        private final JLabel labelNome = new JLabel();
        private final JLabel labelInfo = new JLabel();

        /**
         * Costruisce il renderer, impostando il layout e lo stile delle
         * etichette che mostrano il nome del ristorante e le sue informazioni sintetiche.
         */
        public RistoranteCellRenderer() {
            setLayout(new GridLayout(2, 1));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            labelNome.setFont(labelNome.getFont().deriveFont(Font.BOLD, 14f));
            labelInfo.setFont(labelInfo.getFont().deriveFont(Font.PLAIN, 12f));
            labelInfo.setForeground(Color.DARK_GRAY);
            add(labelNome);
            add(labelInfo);
        }

        /**
         * Costruisce e restituisce il componente grafico utilizzato per
         * visualizzare una singola cella della lista, mostrando nome e
         * informazioni sintetiche del ristorante.
         *
         * @param list il {@link JList} in fase di rendering
         * @param ristorante il {@link RistoranteDTO} da visualizzare per questa cella
         * @param index indice della cella nella lista
         * @param isSelected true se la cella è attualmente selezionata
         * @param cellHasFocus true se la cella ha il focus
         * @return il componente grafico da visualizzare per la cella
         */
        @Override
        public Component getListCellRendererComponent(JList<? extends RistoranteDTO> list, RistoranteDTO ristorante,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            labelNome.setText(ristorante.getNomeRistorante());

            StringBuilder info = new StringBuilder();
            info.append(ristorante.getCitta()).append(", ").append(ristorante.getNazione());

            // Adatta questi metodi in base ai getter reali del tuo DTO
            info.append(" · Delivery: ").append(ristorante.isDelivery() ? "Si" : "No");
            info.append(" · Prenotazione online: ").append(ristorante.isPrenotazioneOnline() ? "Si" : "No");

            labelInfo.setText(info.toString());

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                labelNome.setForeground(list.getSelectionForeground());
                labelInfo.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                labelNome.setForeground(Color.BLACK);
                labelInfo.setForeground(Color.DARK_GRAY);
            }
            setOpaque(true);

            return this;
        }
    }
}
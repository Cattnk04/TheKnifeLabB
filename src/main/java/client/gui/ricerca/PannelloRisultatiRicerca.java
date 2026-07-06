package main.java.client.gui.ricerca;

import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/*
 * Pannello che mostra l'elenco dei ristoranti trovati da una ricerca.
 * Ogni elemento della lista, se selezionato/doppio click, invoca la callback
 * onRistoranteSelezionato passando il RistoranteDTO corrispondente
 * (utile per aprire una GUI di dettaglio/prenotazione).
 */
public class PannelloRisultatiRicerca extends JPanel {

    private final DefaultListModel<RistoranteDTO> modelloLista;
    private final JList<RistoranteDTO> listaRistoranti;
    private final JLabel labelContatore;

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
     * Aggiorna la lista con i nuovi risultati di ricerca.
     * Da chiamare nella callback passata a PannelloRicercaRistorante.
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

    /*
     * Renderer personalizzato per mostrare le informazioni principali del ristorante
     * in ogni riga della lista.
     *
     * NOTA: adatta i nomi dei metodi getter (getNome, getCitta, ecc.) a quelli
     * effettivamente presenti in RistoranteDTO se diversi.
     */
    private static class RistoranteCellRenderer extends JPanel implements ListCellRenderer<RistoranteDTO> {

        private final JLabel labelNome = new JLabel();
        private final JLabel labelInfo = new JLabel();

        public RistoranteCellRenderer() {
            setLayout(new GridLayout(2, 1));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            labelNome.setFont(labelNome.getFont().deriveFont(Font.BOLD, 14f));
            labelInfo.setFont(labelInfo.getFont().deriveFont(Font.PLAIN, 12f));
            labelInfo.setForeground(Color.DARK_GRAY);
            add(labelNome);
            add(labelInfo);
        }

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
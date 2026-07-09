package main.java.client.gui.utils;

import main.java.server.service.UtenteService;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Pannello riutilizzabile che mostra a un ristoratore l'elenco dei propri
 * ristoranti, con la possibilità di selezionarne uno (anche tramite doppio
 * click) per eseguire un'azione (es. apertura della schermata di modifica).
 */
public class PannelloRistorantiRistoratore extends JPanel {
    private final DefaultListModel<RistoranteDTO> modelloLista;
    private final JList<RistoranteDTO> listaRistoranti;
    private final JLabel labelContatore;

    /**
     * Costruisce il pannello con la lista dei ristoranti, registrando un
     * listener per il doppio click che invoca il consumer fornito con il
     * ristorante selezionato.
     *
     * @param onRistoranteSelezionato azione da eseguire quando l'utente fa
     * doppio click su un ristorante della lista, ricevendo il {@link RistoranteDTO} selezionato
     */
    public PannelloRistorantiRistoratore(Consumer<RistoranteDTO> onRistoranteSelezionato) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("I tuoi ristoranti:"));

        this.modelloLista = new DefaultListModel<>();
        this.listaRistoranti = new JList<>(modelloLista);
        this.listaRistoranti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRistoranti.setCellRenderer(new RIstoranteCellRenderer());
        this.listaRistoranti.setFixedCellHeight(70);

        this.listaRistoranti.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent event){
                if (event.getClickCount() == 2){
                    RistoranteDTO selezionato = listaRistoranti.getSelectedValue();
                    if(selezionato != null && onRistoranteSelezionato != null){
                        onRistoranteSelezionato.accept(selezionato);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(listaRistoranti);
        scrollPane.setPreferredSize(new Dimension(500, 250));

        this.labelContatore = new JLabel("Nessun ristorante caricato");
        this.labelContatore.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        add(labelContatore, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    /**
     * Restituisce il ristorante attualmente selezionato nella lista.
     *
     * @return il {@link RistoranteDTO} selezionato, oppure {@code null} se
     * nessun elemento è selezionato
     */
    public RistoranteDTO getRistoranteSelezionato(){
        return listaRistoranti.getSelectedValue();
    }

    /**
     * Aggiorna la lista visualizzata con i ristoranti forniti (tipicamente
     * quelli associati al ristoratore) e aggiorna l'etichetta con il
     * conteggio dei risultati.
     *
     * @param risultati la lista di ristoranti da mostrare, o {@code null}/vuota
     * se il ristoratore non ha ancora ristoranti registrati
     */
    public void aggiornaRisultati(List<RistoranteDTO> risultati){
        modelloLista.clear();
        if(risultati == null || risultati.isEmpty()){
            labelContatore.setText("Non hai ancora nessun ristorante registrato!");
            return;
        }
        for(RistoranteDTO ristorante : risultati){
            modelloLista.addElement(ristorante);
        }
        labelContatore.setText(risultati.size() + "ristorant" + (risultati.size() == 1 ? "e" : "i") + " associat" + (risultati.size() == 1 ? "o" : "i"));
    }

    /**
     * Renderer personalizzato per visualizzare ciascuna cella della lista dei
     * ristoranti, mostrando id, nome e informazioni sintetiche (città,
     * nazione, delivery, prenotazione online).
     */
    private static class RIstoranteCellRenderer extends JPanel implements ListCellRenderer<RistoranteDTO> {
        private final JLabel labelNome = new JLabel();
        private final JLabel labelInfo = new JLabel();

        /**
         * Costruisce il renderer, impostando il layout e lo stile delle
         * etichette che mostrano il nome del ristorante e le sue informazioni sintetiche.
         */
        public RIstoranteCellRenderer(){
            setLayout(new GridLayout(2, 1));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            labelNome.setFont(labelNome.getFont().deriveFont(Font.BOLD, 14f));
            labelInfo.setFont(labelInfo.getFont().deriveFont(Font.BOLD, 12f));
            labelInfo.setForeground(Color.DARK_GRAY);
            add(labelNome);
            add(labelInfo);
        }

        /**
         * Costruisce e restituisce il componente grafico utilizzato per
         * visualizzare una singola cella della lista, mostrando id, nome e
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
        public Component getListCellRendererComponent(JList<? extends RistoranteDTO> list, RistoranteDTO ristorante, int index, boolean isSelected, boolean cellHasFocus) {
            labelNome.setText("#" + ristorante.getIdRistorante() + " - " + ristorante.getNomeRistorante());

            StringBuilder info = new StringBuilder();
            info.append(ristorante.getCitta()).append(", ").append(ristorante.getNazione());
            info.append(" · Delivery: ").append(ristorante.isDelivery() ? "Sì" : "No");
            info.append(" · Prenotazione online: ").append(ristorante.isPrenotazioneOnline() ? "Sì" : "No");

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

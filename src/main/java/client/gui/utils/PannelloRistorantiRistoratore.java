package main.java.client.gui.utils;

import main.java.server.service.UtenteService;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PannelloRistorantiRistoratore extends JPanel {
    private final DefaultListModel<RistoranteDTO> modelloLista;
    private final JList<RistoranteDTO> listaRistoranti;
    private final JLabel labelContatore;

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
    /*
    metodo che restituisce il ristorante attualmente selezionato dalla lista
     */
    public RistoranteDTO getRistoranteSelezionato(){
        return listaRistoranti.getSelectedValue();
    }

    /*
    metodo che aggiorna la lista con i ristoranti associati all'email del ristoratore
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

    /*
    renderer personalizzato
     */
    private static class RIstoranteCellRenderer extends JPanel implements ListCellRenderer<RistoranteDTO> {
        private final JLabel labelNome = new JLabel();
        private final JLabel labelInfo = new JLabel();

        public RIstoranteCellRenderer(){
            setLayout(new GridLayout(2, 1));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            labelNome.setFont(labelNome.getFont().deriveFont(Font.BOLD, 14f));
            labelInfo.setFont(labelInfo.getFont().deriveFont(Font.BOLD, 12f));
            labelInfo.setForeground(Color.DARK_GRAY);
            add(labelNome);
            add(labelInfo);
        }
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

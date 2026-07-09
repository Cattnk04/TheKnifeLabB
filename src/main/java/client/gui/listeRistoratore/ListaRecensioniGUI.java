package main.java.client.gui.listeRistoratore;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.azioniRistoratore.RispondiRecensioneGUI;
import main.java.client.gui.menu.RistoratoreGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.domain.Recensione;
import main.java.shared.dto.RecensioneDTO;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListaRecensioniGUI extends TemplateGUI {
    JFrame frame;

    private final DefaultListModel<RecensioneDTO> modelloLista;
    private JList<RecensioneDTO> listaRecensioni;
    private final JLabel labelContatore;

    public ListaRecensioniGUI(JFrame frame, UtenteService utenteService, String email, RistoranteDTO ristorante){
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

        JPanel pannelloCentrale = new JPanel(new BorderLayout(10,10));
        pannelloCentrale.setBorder(BorderFactory.createTitledBorder("Le recensioni del tuo ristorante"));

        this.modelloLista = new DefaultListModel<>();
        this.listaRecensioni = new JList<>(modelloLista);
        this.listaRecensioni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRecensioni.setFixedCellHeight(70);

        // Renderer personalizzato per mostrare voto, testo e stato della recensione
        this.listaRecensioni.setCellRenderer(new ListCellRenderer<RecensioneDTO>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends RecensioneDTO> list, RecensioneDTO recensione,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JPanel pannelloCella = new JPanel(new BorderLayout(5, 2));
                pannelloCella.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                JLabel labelVoto = new JLabel("Voto: " + recensione.getValutazione() + "/5");
                labelVoto.setFont(labelVoto.getFont().deriveFont(Font.BOLD));

                JLabel labelTesto = new JLabel(recensione.getRecensione());

                JLabel labelStato = new JLabel(recensione.getRisposta() != null ? "✔ Già risposta" : "In attesa di risposta");
                labelStato.setFont(labelStato.getFont().deriveFont(Font.ITALIC, 11f));

                pannelloCella.add(labelVoto, BorderLayout.NORTH);
                pannelloCella.add(labelTesto, BorderLayout.CENTER);
                pannelloCella.add(labelStato, BorderLayout.SOUTH);

                if (isSelected) {
                    pannelloCella.setBackground(list.getSelectionBackground());
                    labelVoto.setForeground(list.getSelectionForeground());
                    labelTesto.setForeground(list.getSelectionForeground());
                    labelStato.setForeground(list.getSelectionForeground());
                } else {
                    pannelloCella.setBackground(list.getBackground());
                }

                return pannelloCella;
            }
        });


        JScrollPane scrollPane = new JScrollPane(listaRecensioni);
        scrollPane.setPreferredSize(new Dimension(500, 350));

        this.labelContatore = new JLabel("Caricamento recensioni...");
        this.labelContatore.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        pannelloCentrale.add(labelContatore, BorderLayout.NORTH);
        pannelloCentrale.add(scrollPane, BorderLayout.CENTER);

        caricaRecensioni(ristorante);

        JPanel pannelloBottoniSotto = new JPanel(new BorderLayout(10,10));
        JButton rispondiRecensione = new JButton("Rispondi recensione");

        rispondiRecensione.addActionListener(e -> {
            RecensioneDTO selezionata = listaRecensioni.getSelectedValue();
            if(selezionata == null){
                mostraNessunaSelezione();
                return;
            }
            if(selezionata.getRisposta() != null){
                mostraGiaRisposta();
                return;
            }
            frame.setContentPane(new RispondiRecensioneGUI(frame, utenteService, email, ristorante, selezionata));
            frame.revalidate();
            frame.repaint();
        });
        pannelloBottoniSotto.add(rispondiRecensione);

        add(pannelloCentrale, BorderLayout.CENTER);
        add(rispondiRecensione, BorderLayout.SOUTH);
    }

    @SuppressWarnings("unchecked")
    private void caricaRecensioni(RistoranteDTO ristorante) {
        Richiesta richiesta = new Richiesta(TipoRichieste.GET_RECENSIONI_RISTORANTE, ristorante.getIdRistorante());
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        if (risposta == null || !risposta.getSuccesso()) {
            labelContatore.setText("Impossibile recuperare le recensioni");
            return;
        }

        List<Recensione> listaRecensioni = (List<Recensione>) risposta.getContenuto();

        modelloLista.clear();
        for(Recensione recensione : listaRecensioni){
            modelloLista.addElement(convertiInDTO(recensione));
        }

        if (listaRecensioni.isEmpty()) {
            labelContatore.setText("Nessuna recensione trovata");
        } else {
            labelContatore.setText("Recensioni trovate: " + listaRecensioni.size());
        }
    }
    private void mostraNessunaSelezione() {
        JOptionPane.showMessageDialog(this,
                "Seleziona prima una recensione dalla lista.",
                "Nessuna selezione",
                JOptionPane.INFORMATION_MESSAGE);
    }
    private void mostraGiaRisposta() {
        JOptionPane.showMessageDialog(this,
                "Hai già risposto a questa recensione",
                "Già risposto",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private RecensioneDTO convertiInDTO(Recensione r) {
        // Adatta i campi ai getter effettivi della tua classe Recensione/RecensioneDTO
        RecensioneDTO dto = new RecensioneDTO();
        dto.setIdRistorante(r.getIdRistorante());
        dto.setEmail(r.getEmail());
        dto.setValutazione(r.getValutazione());
        dto.setRecensione(r.getRecensione());
        dto.setRisposta(r.getRisposta());
        return dto;
    }

}

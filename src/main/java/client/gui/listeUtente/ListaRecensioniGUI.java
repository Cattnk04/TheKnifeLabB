package main.java.client.gui.listeUtente;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.domain.Recensione;
import main.java.shared.dto.RecensioneDTO;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 *
 */
public class ListaRecensioniGUI extends TemplateGUI {
    JFrame frame;
    private final UtenteService utenteService;
    private final String email;

    private final DefaultListModel<Recensione> modelloLista;
    private final JList<Recensione> listaRecensioni;
    private final JLabel labelContatore;
    private List<RistoranteDTO> tuttiRistoranti = new java.util.ArrayList<>();

    /**
     *
     * @param frame
     * @param utenteService
     * @param email
     */
    public ListaRecensioniGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;
        this.utenteService = utenteService;
        this.email = email;

        //rimuovo il bottone "Profilo" dato che siamo già in questa schermata
        pannello.remove(visualizzaProfilo);
        pannello.revalidate();
        pannello.repaint();

        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

        JPanel pannelloCentrale = new JPanel(new BorderLayout(10,10));
        pannelloCentrale.setBorder(BorderFactory.createTitledBorder("Le tue recensioni"));

        this.modelloLista = new DefaultListModel<>();
        this.listaRecensioni = new JList<>(modelloLista);
        this.listaRecensioni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaRecensioni.setCellRenderer(new RecensioneCellRenderer());
        this.listaRecensioni.setFixedCellHeight(70);

        JScrollPane scrollPane = new JScrollPane(listaRecensioni);
        scrollPane.setPreferredSize(new Dimension(500, 350));

        this.labelContatore = new JLabel("Caricamento recensioni...");
        this.labelContatore.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        pannelloCentrale.add(labelContatore, BorderLayout.NORTH);
        pannelloCentrale.add(scrollPane, BorderLayout.CENTER);

        JButton btnModifica = new JButton("Modifica recensione");
        btnModifica.addActionListener(e -> {
            Recensione selezionata = listaRecensioni.getSelectedValue();
            if (selezionata == null) {
                mostraNessunaSelezione();
                return;
            }
            apriModificaRecensione(selezionata);
        });

        JButton btnElimina = new JButton("Elimina recensione");
        btnElimina.addActionListener(e -> {
            Recensione selezionata = listaRecensioni.getSelectedValue();
            if (selezionata == null) {
                mostraNessunaSelezione();
                return;
            }
            eliminaRecensione(selezionata);
        });

        JButton indietro = new JButton("Torna al profilo");
        indietro.setFocusPainted(false);
        indietro.setBorder(new LineBorder(Color.WHITE));
        indietro.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

        JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pannelloBottoni.add(btnModifica);
        pannelloBottoni.add(btnElimina);
        pannelloBottoni.add(indietro);
        pannelloCentrale.add(pannelloBottoni, BorderLayout.SOUTH);

        add(pannelloCentrale, BorderLayout.CENTER);

        caricaRecensioni();
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    private void caricaRecensioni() {
        Richiesta richiesta = new Richiesta(TipoRichieste.GET_RECENSIONI_BYEMAIL, email);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        if (risposta == null || !risposta.getSuccesso()) {
            labelContatore.setText("Impossibile recuperare le recensioni");
            return;
        }

        List<Recensione> recensioni = (List<Recensione>) risposta.getContenuto();
        caricaNomiRistoranti();
        aggiornaRisultati(recensioni);
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    private void caricaNomiRistoranti() {
        Richiesta richiesta = new Richiesta(TipoRichieste.GET_RISTORANTE, null);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        tuttiRistoranti = new java.util.ArrayList<>();
        if (risposta != null && risposta.getSuccesso()) {
            tuttiRistoranti = (List<RistoranteDTO>) risposta.getContenuto();
        }
    }

    /**
     *
     * @param idRistorante
     * @return
     */
    private String trovaNomeRistorante(int idRistorante) {
        for (RistoranteDTO r : tuttiRistoranti) {
            if (r.getIdRistorante() == idRistorante) {
                return r.getNomeRistorante();
            }
        }
        return "Ristorante #" + idRistorante;
    }

    /**
     *
     * @param risultati
     */
    private void aggiornaRisultati(List<Recensione> risultati) {
        modelloLista.clear();
        if(risultati == null || risultati.isEmpty()){
            labelContatore.setText("Nessuna recensione");
            return;
        }
        for (Recensione recensione : risultati) {
            modelloLista.addElement(recensione);
        }
        labelContatore.setText(risultati.size() + " recension" + (risultati.size() == 1 ? "e scritta" : "i scritte"));
    }

    /**
     *
     */
    private void mostraNessunaSelezione() {
        JOptionPane.showMessageDialog(this,
                "Seleziona prima una recensione dalla lista.",
                "Nessuna selezione",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     *
     * @param recensione
     */
    private void apriModificaRecensione (Recensione recensione){
        JTextArea areaTesto = new JTextArea(recensione.getRecensione(),5,30);
        areaTesto.setLineWrap(true);
        areaTesto.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(areaTesto);

        JSpinner spinnerValutazione = new JSpinner(new SpinnerNumberModel(recensione.getValutazione(), 1, 5, 1));

        JPanel pannelloForm = new JPanel(new BorderLayout(10, 10));
        JPanel pannelloValutazione = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pannelloValutazione.add(new JLabel("Valutazione (1-5): "));
        pannelloValutazione.add(spinnerValutazione);

        pannelloForm.add(pannelloValutazione, BorderLayout.NORTH);
        pannelloForm.add(scrollPane, BorderLayout.CENTER);

        int scelta = JOptionPane.showConfirmDialog(this, pannelloForm, "Modifica recensione",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(scelta != JOptionPane.OK_OPTION){
            return;
        }

        String nuovoTesto = areaTesto.getText();
        int nuovaValutazione = (Integer) spinnerValutazione.getValue();

        RecensioneDTO dto = new RecensioneDTO(
                email,
                recensione.getIdRistorante(),
                nuovaValutazione,
                nuovoTesto,
                recensione.getRisposta()
        );

        Richiesta richiesta = new Richiesta(TipoRichieste.MODIFICA_RECENSIONE, dto);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        if(risposta != null && risposta.getSuccesso()) {
            caricaRecensioni();
        } else {
            JOptionPane.showMessageDialog(this,
                    risposta != null ? risposta.getMsg() : "Impossibile contattare il server",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @param recensione
     */
    private void eliminaRecensione(Recensione recensione) {
        int conferma = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare questa recensione?",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (conferma != JOptionPane.YES_OPTION) {
            return;
        }

        RecensioneDTO dto = new RecensioneDTO(
                email,
                recensione.getIdRistorante(),
                recensione.getValutazione(),
                recensione.getRecensione(),
                recensione.getRisposta()
        );

        Richiesta richiesta = new Richiesta(TipoRichieste.ELIMINA_RECENSIONE, dto);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        if (risposta != null && risposta.getSuccesso()) {
            caricaRecensioni();
        } else {
            JOptionPane.showMessageDialog(this,
                    risposta != null ? risposta.getMsg() : "Impossibile contattare il server",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     */
    private class RecensioneCellRenderer extends JPanel implements ListCellRenderer<Recensione> {

        private final JLabel labelRistorante = new JLabel();
        private final JLabel labelInfo = new JLabel();

        /**
         *
         */
        public RecensioneCellRenderer() {
            setLayout(new GridLayout(2, 1));
            setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            labelRistorante.setFont(labelRistorante.getFont().deriveFont(Font.BOLD, 14f));
            labelInfo.setFont(labelInfo.getFont().deriveFont(Font.PLAIN, 12f));
            labelInfo.setForeground(Color.DARK_GRAY);
            add(labelRistorante);
            add(labelInfo);
        }

        /**
         *
         * @param list The JList we're painting.
         * @param recensione The value returned by list.getModel().getElementAt(index).
         * @param index The cells index.
         * @param isSelected True if the specified cell was selected.
         * @param cellHasFocus True if the specified cell has the focus.
         * @return
         */
        @Override
        public Component getListCellRendererComponent(JList<? extends Recensione> list, Recensione recensione,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            String nomeRistorante = trovaNomeRistorante(recensione.getIdRistorante());
            labelRistorante.setText(nomeRistorante);

            String testo = recensione.getRecensione();
            if (testo != null && testo.length() > 60) {
                testo = testo.substring(0, 57) + "...";
            }

            String info = "Voto: " + recensione.getValutazione() + "/5 · " + testo;
            labelInfo.setText(info);

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                labelRistorante.setForeground(list.getSelectionForeground());
                labelInfo.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                labelRistorante.setForeground(Color.BLACK);
                labelInfo.setForeground(Color.DARK_GRAY);
            }
            setOpaque(true);

            return this;
        }
    }
}

package main.java.client.gui.listeUtente;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.domain.Preferito;
import main.java.shared.dto.PreferitiDTO;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Schermata che mostra a un utente cliente autenticato l'elenco dei
 * ristoranti aggiunti ai preferiti.
 * <p>
 * Permette di visualizzarne i dettagli (con doppio click o tramite
 * pulsante) e di rimuovere un ristorante dai preferiti.
 * </p>
 */
public class ListaPreferitiGUI extends TemplateGUI {
    JFrame frame;
    private final UtenteService utenteService;
    private final String email;

    private final DefaultListModel<RistoranteDTO> modelloLista;
    private final JList<RistoranteDTO> listaPreferiti;
    private final JLabel labelContatore;

    /**
     * Costruisce la schermata con l'elenco dei ristoranti preferiti
     * dell'utente, caricandoli dal server e predisponendo i pulsanti per
     * visualizzarne i dettagli o rimuoverli dai preferiti.
     *
     * @param frame la finestra principale dell'applicazione
     * @param utenteService il service utilizzato per le operazioni sugli utenti
     * @param email l'email dell'utente di cui visualizzare i preferiti
     */
    public ListaPreferitiGUI(JFrame frame, UtenteService utenteService, String email) {
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

        JPanel pannelloCentrale = new JPanel(new BorderLayout(10, 10));
        pannelloCentrale.setBorder(BorderFactory.createTitledBorder("I tuoi ristoranti preferiti"));

        this.modelloLista = new DefaultListModel<>();
        this.listaPreferiti = new JList<>(modelloLista);
        this.listaPreferiti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaPreferiti.setCellRenderer(new RistoranteCellRenderer());
        this.listaPreferiti.setFixedCellHeight(70);

        this.listaPreferiti.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    RistoranteDTO selezionato = listaPreferiti.getSelectedValue();
                    if (selezionato != null) {
                        apriDettaglioRistorante(selezionato);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaPreferiti);
        scrollPane.setPreferredSize(new Dimension(500, 350));

        this.labelContatore = new JLabel("Caricamento preferiti...");
        this.labelContatore.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        pannelloCentrale.add(labelContatore, BorderLayout.NORTH);
        pannelloCentrale.add(scrollPane, BorderLayout.CENTER);

        JButton btnDettagli = new JButton("Visualizza dettagli");
        btnDettagli.addActionListener(e -> {
            RistoranteDTO selezionato = listaPreferiti.getSelectedValue();
            if (selezionato != null) {
                apriDettaglioRistorante(selezionato);
            } else {
                mostraNessunaSelezione();
            }
        });

        JButton btnRimuovi = new JButton("Rimuovi dai preferiti");
        btnRimuovi.addActionListener(e -> {
            RistoranteDTO selezionato = listaPreferiti.getSelectedValue();
            if (selezionato == null) {
                mostraNessunaSelezione();
                return;
            }
            PreferitiDTO dto = new PreferitiDTO(email, selezionato.getIdRistorante());
            Richiesta richiesta = new Richiesta(TipoRichieste.RIMUOVI_PREFERITO, dto);
            Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

            if (risposta != null && risposta.getSuccesso()) {
                caricaPreferiti();
            } else {
                JOptionPane.showMessageDialog(this,
                        risposta != null ? risposta.getMsg() : "Impossibile contattare il server",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
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
        pannelloBottoni.add(btnDettagli);
        pannelloBottoni.add(btnRimuovi);
        pannelloBottoni.add(indietro);
        pannelloCentrale.add(pannelloBottoni, BorderLayout.SOUTH);

        add(pannelloCentrale, BorderLayout.CENTER);

        caricaPreferiti();
    }

    /**
     * Richiede al server l'elenco dei preferiti dell'utente e, se non è
     * vuoto, recupera anche l'elenco completo dei ristoranti per poter
     * ricostruire i dati completi dei ristoranti preferiti, aggiornando
     * infine la lista visualizzata.
     */
    @SuppressWarnings("unchecked")
    private void caricaPreferiti() {
        Richiesta richiestaPreferiti = new Richiesta(TipoRichieste.GET_PREFERITI, email);
        Risposta rispostaPreferiti = ClientConnection.inviaRichiesta(richiestaPreferiti);

        if (rispostaPreferiti == null || !rispostaPreferiti.getSuccesso()) {
            labelContatore.setText("Impossibile recuperare i preferiti");
            return;
        }

        List<Preferito> preferiti = (List<Preferito>) rispostaPreferiti.getContenuto();
        if (preferiti == null || preferiti.isEmpty()) {
            aggiornaRisultati(new ArrayList<>());
            return;
        }

        Richiesta richiestaRistoranti = new Richiesta(TipoRichieste.GET_RISTORANTE, null);
        Risposta rispostaRistoranti = ClientConnection.inviaRichiesta(richiestaRistoranti);

        if (rispostaRistoranti == null || !rispostaRistoranti.getSuccesso()) {
            labelContatore.setText("Impossibile recuperare i dati dei ristoranti");
            return;
        }

        List<RistoranteDTO> tuttiRistoranti = (List<RistoranteDTO>) rispostaRistoranti.getContenuto();

        List<RistoranteDTO> ristorantiPreferiti = new ArrayList<>();
        for (Preferito p : preferiti) {
            for (RistoranteDTO r : tuttiRistoranti) {
                if (r.getIdRistorante() == p.getIdRistorante()) {
                    ristorantiPreferiti.add(r);
                    break;
                }
            }
        }

        aggiornaRisultati(ristorantiPreferiti);
    }

    /**
     * Aggiorna la lista visualizzata con i risultati forniti e aggiorna
     * l'etichetta con il conteggio dei ristoranti preferiti trovati.
     *
     * @param risultati la lista di ristoranti preferiti da mostrare, o
     * {@code null}/vuota se non ce ne sono
     */
    private void aggiornaRisultati(List<RistoranteDTO> risultati) {
        modelloLista.clear();
        if (risultati == null || risultati.isEmpty()) {
            labelContatore.setText("Nessun ristorante preferito");
            return;
        }
        for (RistoranteDTO ristorante : risultati) {
            modelloLista.addElement(ristorante);
        }
        labelContatore.setText(risultati.size() + " ristorant" + (risultati.size() == 1 ? "e preferito" : "i preferiti"));
    }

    /**
     * Mostra un messaggio quando l'utente tenta di visualizzare i dettagli
     * o rimuovere un preferito senza aver selezionato un ristorante dalla lista.
     */
    private void mostraNessunaSelezione() {
        JOptionPane.showMessageDialog(this,
                "Seleziona prima un ristorante dalla lista.",
                "Nessuna selezione",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Apre (o, allo stato attuale, mostra un messaggio segnaposto per) la
     * schermata di dettaglio di un ristorante preferito.
     *
     * @param ristorante il {@link RistoranteDTO} di cui mostrare i dettagli
     */
    private void apriDettaglioRistorante(RistoranteDTO ristorante) {
        JOptionPane.showMessageDialog(this,
                "Apertura dettagli per: " + ristorante.getNomeRistorante(),
                "Dettagli ristorante",
                JOptionPane.INFORMATION_MESSAGE);
        // frame.setContentPane(new DettaglioRistoranteGUI(frame, utenteService, email, ristorante));
        // frame.revalidate();
        // frame.repaint();
    }

    /**
     * Renderer personalizzato per visualizzare ciascuna cella della lista
     * dei ristoranti preferiti, mostrando il nome del ristorante e alcune
     * informazioni sintetiche (città, nazione, delivery, prenotazione online).
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
         * visualizzare una singola cella della lista, mostrando il nome del
         * ristorante e le informazioni sintetiche (città, nazione, delivery,
         * prenotazione online).
         *
         * @param list The JList we're painting.
         * @param ristorante The value returned by list.getModel().getElementAt(index).
         * @param index The cells index.
         * @param isSelected True if the specified cell was selected.
         * @param cellHasFocus True if the specified cell has the focus.
         * @return il componente grafico da visualizzare per la cella
         */
        @Override
        public Component getListCellRendererComponent(JList<? extends RistoranteDTO> list, RistoranteDTO ristorante,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            labelNome.setText(ristorante.getNomeRistorante());

            StringBuilder info = new StringBuilder();
            info.append(ristorante.getCitta()).append(", ").append(ristorante.getNazione());
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
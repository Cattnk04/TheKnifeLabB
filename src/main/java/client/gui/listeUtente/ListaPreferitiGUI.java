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

public class ListaPreferitiGUI extends TemplateGUI {
    JFrame frame;
    private final UtenteService utenteService;
    private final String email;

    private final DefaultListModel<RistoranteDTO> modelloLista;
    private final JList<RistoranteDTO> listaPreferiti;
    private final JLabel labelContatore;

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

    /*
     * Recupera i preferiti (email -> idRistorante) dal server, poi recupera
     * tutti i ristoranti e filtra client-side quelli il cui id è tra i preferiti.
     *
     * NOTA: Con un endpoint dedicato per singolo id
     * (es. TipoRichieste.GET_RISTORANTE_BY_ID), questo metodo può essere
     * semplificato evitando di scaricare tutti i ristoranti ogni volta.
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

    private void mostraNessunaSelezione() {
        JOptionPane.showMessageDialog(this,
                "Seleziona prima un ristorante dalla lista.",
                "Nessuna selezione",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void apriDettaglioRistorante(RistoranteDTO ristorante) {
        JOptionPane.showMessageDialog(this,
                "Apertura dettagli per: " + ristorante.getNomeRistorante(),
                "Dettagli ristorante",
                JOptionPane.INFORMATION_MESSAGE);
        // frame.setContentPane(new DettaglioRistoranteGUI(frame, utenteService, email, ristorante));
        // frame.revalidate();
        // frame.repaint();
    }

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
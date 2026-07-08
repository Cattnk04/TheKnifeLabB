package main.java.client.gui.utils;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.azioniLoggato.ScriviRecensioneGUI;
import main.java.client.gui.menu.LoggatoGUI;
import main.java.client.gui.menu.GuestGUI;
import main.java.client.gui.utils.VisualizzaRecensioniGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.*;
import main.java.shared.domain.Recensione;
import main.java.shared.dto.*;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/*
 * GUI di dettaglio di un ristorante.
 * Mostra i dati del ristorante e, sotto, un pannello di azioni che varia in base
 * allo stato dell'utente:
 *  - ospite (email == null) -> nessuna azione di scrittura, solo invito al login
 *                               + possibilità di visualizzare le recensioni esistenti
 *  - utente cliente senza recensione -> bottone "Scrivi una recensione"
 *  - utente cliente con recensione già scritta -> messaggio informativo
 *  - utente ristoratore -> nessuna azione recensione (la GUI
 *                                            è condivisa anche per il ristoratore,
 *                                            che vede solo i dati del locale)
 *
 * In alto è presente un tasto "Indietro" che riporta l'utente alla schermata
 * precedente (LoggatoGUI se loggato, GuestGUI se ospite).
 */
public class DettagliRistoranteGUI extends TemplateGUI{

    private final JFrame frame;
    private final UtenteService utenteService;
    private final RistoranteDTO ristorante;
    private final String email; //null se utente ospite

    public DettagliRistoranteGUI(JFrame frame, UtenteService utenteService,
                                 String email, RistoranteDTO ristorante) {
        super(frame);
        this.frame = frame;
        this.utenteService = utenteService;
        this.email = email;
        this.ristorante = ristorante;

        //rimuovo il bottone "Profilo"
        pannello.remove(visualizzaProfilo);
        pannello.revalidate();
        pannello.repaint();

        // Bottone logout visibile solo se utente loggato (ospite -> resta nascosto)
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

        //Pannello dati ristorante
        JPanel pannelloDati = new JPanel(new GridBagLayout());
        GridBagConstraints vincolo = new GridBagConstraints();
        vincolo.insets = new Insets(8, 10, 8, 10);
        vincolo.anchor = GridBagConstraints.WEST;
        vincolo.fill = GridBagConstraints.HORIZONTAL;
        vincolo.weightx = 1.0;

        int riga = 0;
        riga = aggiungiRiga(pannelloDati, vincolo, riga, "Nome: ", ristorante.getNomeRistorante());
        riga = aggiungiRiga(pannelloDati, vincolo, riga, "Indirizzo: ",
                ristorante.getVia() + ", " + ristorante.getNumeroCivico());
        riga = aggiungiRiga(pannelloDati, vincolo, riga, "Città: ", ristorante.getCitta());
        riga = aggiungiRiga(pannelloDati, vincolo, riga, "Nazione: ", ristorante.getNazione());
        riga = aggiungiRiga(pannelloDati, vincolo, riga, "Fascia di prezzo: ",
                String.valueOf(ristorante.getFasciaPrezzo()));
        riga = aggiungiRiga(pannelloDati, vincolo, riga, "Delivery: ",
                ristorante.isDelivery() ? "Sì" : "No");
        riga = aggiungiRiga(pannelloDati, vincolo, riga, "Prenotazione online: ",
                ristorante.isPrenotazioneOnline() ? "Sì" : "No");
        riga = aggiungiRiga(pannelloDati, vincolo, riga, "Tipo cucina: ",
                recuperaNomeTipoCucina(ristorante.getTipoCucina()));

        JPanel centroDati = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centroDati.add(pannelloDati);

        //Pannello azioni (varia in base allo stato utente)
        JPanel pannelloAzioni = new JPanel();
        pannelloAzioni.setLayout(new BoxLayout(pannelloAzioni, BoxLayout.Y_AXIS));
        pannelloAzioni.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        if (email == null) {

            JLabel invito = new JLabel("Accedi per lasciare una recensione a questo ristorante.");
            invito.setAlignmentX(Component.CENTER_ALIGNMENT);
            pannelloAzioni.add(invito);

            pannelloAzioni.add(Box.createRigidArea(new Dimension(0,15)));

            JButton visualizza = new JButton("Visualizza recensioni");
            visualizza.setAlignmentX(Component.CENTER_ALIGNMENT);
            visualizza.addActionListener(e -> {
                frame.setContentPane(new VisualizzaRecensioniGUI(
                        frame,
                        utenteService,
                        null,
                        ristorante
                ));
                frame.revalidate();
                frame.repaint();
            });

            pannelloAzioni.add(visualizza);

            pannelloAzioni.add(Box.createRigidArea(new Dimension(0,20)));

            JButton indietro = new JButton("← Indietro");
            indietro.setAlignmentX(Component.CENTER_ALIGNMENT);

            indietro.addActionListener(e -> {
                frame.setContentPane(new GuestGUI(frame, utenteService));
                frame.revalidate();
                frame.repaint();
            });

            pannelloAzioni.add(indietro);

        }
        else {
            boolean isRistoratore = recuperaFlagRistoratore(email);

            if (!isRistoratore) {

                // PREFERITI
                boolean preferitoIniziale = isPreferito(email, ristorante.getIdRistorante());

                JButton bottonePreferiti = new JButton(preferitoIniziale ? "Rimuovi dai preferiti" : "Aggiungi ai preferiti");

                bottonePreferiti.setAlignmentX(Component.CENTER_ALIGNMENT);
                bottonePreferiti.setFocusPainted(false);
                bottonePreferiti.setBorder(new LineBorder(Color.WHITE));

                bottonePreferiti.addActionListener(e -> {

                    PreferitiDTO dto = new PreferitiDTO(email, ristorante.getIdRistorante());
                    Richiesta richiesta = new Richiesta(TipoRichieste.TOGGLE_PREFERITO, dto);

                    Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

                    if (risposta != null && risposta.getSuccesso()) {
                        boolean nuovoStato = (boolean) risposta.getContenuto();
                        bottonePreferiti.setText(
                                nuovoStato ? "Rimuovi dai preferiti" : "Aggiungi ai preferiti"
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                frame,
                                risposta != null ? risposta.getMsg() : "Errore di comunicazione", "Errore",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
                pannelloAzioni.add(bottonePreferiti);
                pannelloAzioni.add(Box.createRigidArea(new Dimension(0,15)));

                // RECENSIONE
                boolean recensioneEsistente = haGiaRecensito(email, ristorante.getIdRistorante());
                if (recensioneEsistente) {
                    JLabel giaRecensito = new JLabel("Hai già scritto una recensione."
                    );
                    giaRecensito.setAlignmentX(Component.CENTER_ALIGNMENT);
                    pannelloAzioni.add(giaRecensito);

                } else {

                    JButton scriviRecensione = new JButton("Scrivi una recensione");
                    scriviRecensione.setAlignmentX(Component.CENTER_ALIGNMENT);
                    scriviRecensione.setFocusPainted(false);
                    scriviRecensione.setBorder(new LineBorder(Color.WHITE));
                    scriviRecensione.addActionListener(e -> {
                        frame.setContentPane(
                                new ScriviRecensioneGUI(
                                        frame,
                                        ristorante.getIdRistorante(),
                                        ristorante.getNomeRistorante()
                                )
                        );
                        frame.revalidate();
                        frame.repaint();
                    });
                    pannelloAzioni.add(scriviRecensione);
                }
                pannelloAzioni.add(Box.createRigidArea(new Dimension(0,15)));

                // VISUALIZZA RECENSIONI
                JButton visualizzaRecensioni =
                        new JButton("Visualizza recensioni");

                visualizzaRecensioni.setAlignmentX(Component.CENTER_ALIGNMENT);
                visualizzaRecensioni.addActionListener(e -> {

                    frame.setContentPane(

                            new VisualizzaRecensioniGUI(
                                    frame,
                                    utenteService,
                                    email,
                                    ristorante
                            )

                    );

                    frame.revalidate();
                    frame.repaint();

                });

                pannelloAzioni.add(visualizzaRecensioni);
                pannelloAzioni.add(Box.createRigidArea(new Dimension(0,20)));

                // INDIETRO
                JButton indietro = new JButton("← Indietro");
                indietro.setAlignmentX(Component.CENTER_ALIGNMENT);
                indietro.addActionListener(e -> {

                    frame.setContentPane(
                            new LoggatoGUI(
                                    frame,
                                    utenteService,
                                    email
                            )
                    );

                    frame.revalidate();
                    frame.repaint();

                });
                pannelloAzioni.add(indietro);
            }
            else {

                JButton visualizzaRecensioni = new JButton("Visualizza recensioni");
                visualizzaRecensioni.setAlignmentX(Component.CENTER_ALIGNMENT);
                visualizzaRecensioni.setFocusPainted(false);

                visualizzaRecensioni.addActionListener(e -> {

                    frame.setContentPane(new VisualizzaRecensioniGUI(
                            frame,
                            utenteService,
                            email,
                            ristorante
                    ));

                    frame.revalidate();
                    frame.repaint();

                });

                pannelloAzioni.add(visualizzaRecensioni);

                pannelloAzioni.add(Box.createRigidArea(new Dimension(0,20)));

                JButton indietro = new JButton("← Indietro");
                indietro.setAlignmentX(Component.CENTER_ALIGNMENT);

                indietro.addActionListener(e -> {

                    frame.setContentPane(new LoggatoGUI(
                            frame,
                            utenteService,
                            email
                    ));

                    frame.revalidate();
                    frame.repaint();

                });

                pannelloAzioni.add(indietro);

            }
        }


        // Composizione layout finale
        JPanel centro = new JPanel(new BorderLayout());
        centro.add(centroDati, BorderLayout.NORTH);
        centro.add(pannelloAzioni, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    //Helper per aggiungere una riga al pannello dati
    private int aggiungiRiga(JPanel pannello, GridBagConstraints gbc,
                             int riga, String label, String valore) {

        gbc.gridy = riga;

        gbc.gridx = 0;
        gbc.weightx = 0;
        pannello.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        pannello.add(new JLabel(valore), gbc);

        return riga + 1;
    }

    // Verifica se l'utente loggato è un ristoratore, interrogando il server (come in VisualizzaProfiloGUI)
    private boolean recuperaFlagRistoratore(String email) {
        Richiesta richiestaDati = new Richiesta(TipoRichieste.GET_UTENTE, email);
        Risposta rispostaDati = ClientConnection.inviaRichiesta(richiestaDati);

        if (rispostaDati == null || !rispostaDati.getSuccesso()) {
            return false; // in assenza di dati si assume utente cliente
        }

        RegistrazioneDTO datiUtente = (RegistrazioneDTO) rispostaDati.getContenuto();
        return datiUtente.isRistoratore();
    }

    // Verifica se l'utente ha già scritto una recensione per questo ristorante
    private boolean haGiaRecensito(String email, Integer idRistorante) {
        if (idRistorante == null) return false;

        Richiesta richiesta = new Richiesta(TipoRichieste.GET_RECENSIONI_BYEMAIL, email);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        if (risposta == null || !risposta.getSuccesso()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        List<Recensione> recensioni = (List<Recensione>) risposta.getContenuto();

        return recensioni.stream()
                .anyMatch(r -> r.getIdRistorante() == idRistorante);
    }

    // Verifica se il ristorante è già tra i preferiti dell'utente
    private boolean isPreferito(String email, Integer idRistorante) {
        if (idRistorante == null) return false;

        PreferitiDTO dto = new PreferitiDTO(email, idRistorante);
        Richiesta richiesta = new Richiesta(TipoRichieste.ESISTE_PREFERITO, dto);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        return risposta != null && risposta.getSuccesso() && (boolean) risposta.getContenuto();
    }

    // Recupera il nome del tipo di cucina dato l'id, interrogando il server
    private String recuperaNomeTipoCucina(int idTipoCucina) {
        Richiesta richiesta = new Richiesta(TipoRichieste.GET_TIPO_CUCINA, null);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        if (risposta == null || !risposta.getSuccesso()) {
            return "Non disponibile";
        }

        @SuppressWarnings("unchecked")
        List<TipoCucinaDTO> tipiCucina = (List<TipoCucinaDTO>) risposta.getContenuto();

        return tipiCucina.stream()
                .filter(t -> t.getIdTipoCucina() == idTipoCucina)
                .map(t -> t.getTipoCucina())
                .findFirst()
                .orElse("Sconosciuto");
    }
}

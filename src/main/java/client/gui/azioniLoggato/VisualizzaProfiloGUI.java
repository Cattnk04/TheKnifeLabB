package main.java.client.gui.azioniLoggato;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.listeUtente.ListaPreferitiGUI;
import main.java.client.gui.listeUtente.ListaRecensioniGUI;
import main.java.client.gui.menu.LoggatoGUI;
import main.java.client.gui.menu.RistoratoreGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.dto.RegistrazioneDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Schermata di visualizzazione del profilo di un utente autenticato.
 * <p>
 * Recupera dal server i dati dell'utente e li mostra in sola lettura,
 * offrendo l'accesso alla modifica dei dati ({@link ModificaProfiloGUI})
 * e, per gli utenti non ristoratori, alle proprie recensioni
 * ({@link ListaRecensioniGUI}) e ai propri preferiti ({@link ListaPreferitiGUI}).
 * </p>
 */
public class VisualizzaProfiloGUI extends TemplateGUI {
    JFrame frame;
    private final UtenteService utenteService;
    private final String email;

    /**
     * Costruisce la schermata di visualizzazione del profilo, recuperando
     * dal server i dati dell'utente e mostrandoli insieme ai pulsanti di
     * navigazione appropriati in base al ruolo dell'utente.
     *
     * @param frame la finestra principale dell'applicazione
     * @param utenteService il service utilizzato per le operazioni sugli utenti
     * @param email l'email dell'utente il cui profilo viene visualizzato
     */
    public VisualizzaProfiloGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame =frame;
        this.utenteService = utenteService;

        this.email = email;

        //rimuovo il bottone "Profilo" dato che siamo già in questa schermata
        pannello.remove(visualizzaProfilo);
        pannello.revalidate();
        pannello.repaint();

        //aggiungo il bottone logout
        logout.setVisible(true);
        logout.addActionListener(e -> {
            Richiesta richiesta = new Richiesta(TipoRichieste.LOGOUT, email);
            ClientConnection.inviaRichiesta(richiesta);

            frame.setContentPane(new LoginGUI(frame, utenteService));
            frame.revalidate();
            frame.repaint();
        });

        //caricamento dei dati dell'utente
        Richiesta richiestaDati = new Richiesta(TipoRichieste.GET_UTENTE, email);
        Risposta rispostaDati = ClientConnection.inviaRichiesta(richiestaDati);

        if(rispostaDati == null || !rispostaDati.getSuccesso()){
            JOptionPane.showMessageDialog(
                    frame,
                    rispostaDati != null ? rispostaDati.getMsg() : "Impossibile contattare il server",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            frame.setContentPane(new LoggatoGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
            return;
        }

        RegistrazioneDTO datiUtente = (RegistrazioneDTO) rispostaDati.getContenuto();

        //composizione griglia
        JPanel pannelloCentrale = new JPanel(new GridBagLayout());
        GridBagConstraints vincoloGriglia = new GridBagConstraints();
        vincoloGriglia.insets = new Insets(12, 15, 12, 15);
        vincoloGriglia.anchor = GridBagConstraints.WEST;
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        //riga 0: email:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 0;
        pannelloCentrale.add(new JLabel("Email: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        pannelloCentrale.add(new JLabel(datiUtente.getEmail()), vincoloGriglia);

        //riga 1: nome:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 1;
        pannelloCentrale.add(new JLabel("Nome: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        pannelloCentrale.add(new JLabel(datiUtente.getNome()), vincoloGriglia);

        //riga 2: cognome:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 2;
        pannelloCentrale.add(new JLabel("Cognome: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        pannelloCentrale.add(new JLabel(datiUtente.getCognome()), vincoloGriglia);

        //riga 3: città:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 3;
        pannelloCentrale.add(new JLabel("Città: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        pannelloCentrale.add(new JLabel(datiUtente.getCitta()), vincoloGriglia);

        //riga 4: nazione:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 4;
        pannelloCentrale.add(new JLabel("Nazione: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        pannelloCentrale.add(new JLabel(datiUtente.getNazione()), vincoloGriglia);

        //riga 5: modifica dati (sempre visibile)
        JButton modificaDati = new JButton("Modifica Dati");
        modificaDati.setFocusPainted(false);
        modificaDati.setBorder(new LineBorder(Color.WHITE));
        modificaDati.addActionListener(e -> {
            frame.setContentPane(new ModificaProfiloGUI(frame, utenteService, email, datiUtente));
            frame.revalidate();
            frame.repaint();
        });

        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 5;
        vincoloGriglia.gridwidth = 2;
        vincoloGriglia.anchor = GridBagConstraints.CENTER;
        pannelloCentrale.add(modificaDati, vincoloGriglia);

        int rigaSuccessiva = 6;

        //riga 6 (solo se NON ristoratore): le mie recensioni
        if (!datiUtente.isRistoratore()) {
            JButton leMieRecensioni = new JButton("Le mie recensioni");
            leMieRecensioni.setFocusPainted(false);
            leMieRecensioni.setBorder(new LineBorder(Color.WHITE));
            leMieRecensioni.addActionListener(e -> {
                frame.setContentPane(new ListaRecensioniGUI(frame, utenteService, email));
                frame.revalidate();
                frame.repaint();
            });

            vincoloGriglia.gridx = 0;
            vincoloGriglia.gridy = rigaSuccessiva++;
            vincoloGriglia.gridwidth = 2;
            vincoloGriglia.anchor = GridBagConstraints.CENTER;
            pannelloCentrale.add(leMieRecensioni, vincoloGriglia);

            //i miei preferiti
            JButton iMieiPreferiti = new JButton("I miei preferiti");
            iMieiPreferiti.setFocusPainted(false);
            iMieiPreferiti.setBorder(new LineBorder(Color.WHITE));
            iMieiPreferiti.addActionListener(e -> {
                frame.setContentPane(new ListaPreferitiGUI(frame, utenteService, email));
                frame.revalidate();
                frame.repaint();
            });

            vincoloGriglia.gridx = 0;
            vincoloGriglia.gridy = rigaSuccessiva++;
            vincoloGriglia.gridwidth = 2;
            vincoloGriglia.anchor = GridBagConstraints.CENTER;
            pannelloCentrale.add(iMieiPreferiti, vincoloGriglia);
        }

        //centratura del pannello
        JPanel centroPannello = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centroPannello.add(pannelloCentrale);
        add(centroPannello, BorderLayout.CENTER);


        //Bottone per tornare alla home corretta in base al ruolo
        JButton home = new JButton("Home");
        home.setFocusPainted(false);
        home.setBorder(new LineBorder(Color.WHITE));
        home.addActionListener(e -> {
            if (datiUtente.isRistoratore()) {
                frame.setContentPane(new RistoratoreGUI(frame, utenteService, email));
            } else {
                frame.setContentPane(new LoggatoGUI(frame, utenteService, email));
            }
            frame.revalidate();
            frame.repaint();
        });

        pannello.add(home);
    }
}

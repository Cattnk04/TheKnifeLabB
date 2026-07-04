package main.java.client.gui.azioniLoggato;

import main.java.client.gui.TemplateGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.dto.RegistrazioneDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ModificaProfiloGUI extends TemplateGUI {
    JFrame frame;
    private final UtenteService utenteService;
    private final String email;
    private final RegistrazioneDTO datiUtente;

    private JTextField campoNome;
    private JTextField campoCognome;
    private JTextField campoCitta;
    private JTextField campoNazione;

    public ModificaProfiloGUI(JFrame frame, UtenteService utenteService, String email, RegistrazioneDTO datiUtente) {
        super(frame);
        this.frame = frame;
        this.utenteService = utenteService;
        this.email = email;
        this.datiUtente = datiUtente;

        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

        //composizione griglia
        JPanel pannelloCentrale = new JPanel(new GridBagLayout());
        GridBagConstraints vincoloGriglia = new GridBagConstraints();
        vincoloGriglia.insets = new Insets(12, 15, 12, 15);
        vincoloGriglia.anchor = GridBagConstraints.WEST;
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        //riga 0: email (non modificabile)
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 0;
        pannelloCentrale.add(new JLabel("Email: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        JLabel labelEmail = new JLabel(datiUtente.getEmail());
        pannelloCentrale.add(labelEmail, vincoloGriglia);

        //riga 1: nome
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 1;
        pannelloCentrale.add(new JLabel("Nome: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        campoNome = new JTextField(datiUtente.getNome(), 20);
        pannelloCentrale.add(campoNome, vincoloGriglia);

        //riga 2: cognome
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 2;
        pannelloCentrale.add(new JLabel("Cognome: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        campoCognome = new JTextField(datiUtente.getCognome(), 20);
        pannelloCentrale.add(campoCognome, vincoloGriglia);

        //riga 3: città
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 3;
        pannelloCentrale.add(new JLabel("Città: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        campoCitta = new JTextField(datiUtente.getCitta(), 20);
        pannelloCentrale.add(campoCitta, vincoloGriglia);

        //riga 4: nazione
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 4;
        pannelloCentrale.add(new JLabel("Nazione: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        campoNazione = new JTextField(datiUtente.getNazione(), 20);
        pannelloCentrale.add(campoNazione, vincoloGriglia);

        //riga 5: bottoni Salva / Annulla
        JButton salva = new JButton("Salva");
        salva.setFocusPainted(false);
        salva.setBorder(new LineBorder(Color.WHITE));
        salva.addActionListener(e -> salvaModifiche());

        JButton annulla = new JButton("Annulla");
        annulla.setFocusPainted(false);
        annulla.setBorder(new LineBorder(Color.WHITE));
        annulla.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

        JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pannelloBottoni.add(salva);
        pannelloBottoni.add(annulla);

        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 5;
        vincoloGriglia.gridwidth = 2;
        vincoloGriglia.anchor = GridBagConstraints.CENTER;
        pannelloCentrale.add(pannelloBottoni, vincoloGriglia);

        //centratura del pannello
        JPanel centroPannello = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centroPannello.add(pannelloCentrale);
        add(centroPannello, BorderLayout.CENTER);
    }

    private void salvaModifiche() {
        String nome = campoNome.getText().trim();
        String cognome = campoCognome.getText().trim();
        String citta = campoCitta.getText().trim();
        String nazione = campoNazione.getText().trim();

        if (nome.isEmpty() || cognome.isEmpty() || citta.isEmpty() || nazione.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Tutti i campi devono essere compilati",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        RegistrazioneDTO nuoviDati = new RegistrazioneDTO(
                nome,
                cognome,
                email,
                null,                       // password non modificata da questa schermata
                citta,
                nazione,
                datiUtente.isRistoratore()
        );

        Richiesta richiesta = new Richiesta(TipoRichieste.MODIFICA_UTENTE, nuoviDati);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        if (risposta != null && risposta.getSuccesso()) {
            JOptionPane.showMessageDialog(frame, "Dati aggiornati con successo!");
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    risposta != null ? risposta.getMsg() : "Impossibile contattare il server",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        frame.revalidate();
        frame.repaint();
    }
}

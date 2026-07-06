package main.java.client.gui.autenticazione;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.menu.GuestGUI;
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

public class RegistrazioneGUI extends TemplateGUI {
    JFrame frame;
    private final JTextField campoNome;
    private final JTextField campoCognome;
    private final JTextField campoCitta;
    private final JTextField campoNazione;
    private JLabel ristoratore;
    private final JRadioButton radioSi;
    private final JRadioButton radioNo;
    private final JTextField campoEmail;
    private final JPasswordField campoPassword;

    public RegistrazioneGUI(JFrame frame, UtenteService utenteService) {
        super(frame);
        this.frame = frame;

        //Creazione griglia per posizionare i componenti in righe e colonne
        JPanel pannelloCentrale = new JPanel(new GridBagLayout());
        GridBagConstraints vincoloGriglia = new GridBagConstraints();

        vincoloGriglia.insets = new Insets(10, 15, 10, 15); //spaziatura
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        Dimension dimensioneCampo = new Dimension(200, 40);

        //Componenti da inserire
        JLabel nomeLabel = new JLabel("Nome: ");
        campoNome = new JTextField();
        campoNome.setPreferredSize(dimensioneCampo);
        campoNome.setMinimumSize(dimensioneCampo);
        campoNome.setMaximumSize(dimensioneCampo);

        JLabel cognomeLabel = new JLabel("Cognome: ");
        campoCognome = new JTextField();
        campoCognome.setPreferredSize(dimensioneCampo);
        campoCognome.setMinimumSize(dimensioneCampo);
        campoCognome.setMaximumSize(dimensioneCampo);

        JLabel cittaLabel = new JLabel("Città: ");
        campoCitta = new JTextField();
        campoCitta.setPreferredSize(dimensioneCampo);
        campoCitta.setMinimumSize(dimensioneCampo);
        campoCitta.setMaximumSize(dimensioneCampo);

        JLabel nazioneLabel = new JLabel("Nazione: ");
        campoNazione = new JTextField();
        campoNazione.setPreferredSize(dimensioneCampo);
        campoNazione.setMinimumSize(dimensioneCampo);
        campoNazione.setMaximumSize(dimensioneCampo);

        JLabel ristoratoreLabel = new JLabel("Sei un ristoratore? ");
        radioSi = new JRadioButton("Si");
        radioSi.setPreferredSize(dimensioneCampo);
        radioSi.setMinimumSize(dimensioneCampo);
        radioSi.setMaximumSize(dimensioneCampo);

        radioNo = new JRadioButton("No");
        radioNo.setPreferredSize(dimensioneCampo);
        radioNo.setMinimumSize(dimensioneCampo);
        radioNo.setMaximumSize(dimensioneCampo);

        ButtonGroup gruppo = new ButtonGroup();
        gruppo.add(radioSi);
        gruppo.add(radioNo);

        JLabel emailLabel = new JLabel("Email: ");
        campoEmail = new JTextField();
        campoEmail.setPreferredSize(dimensioneCampo);
        campoEmail.setMinimumSize(dimensioneCampo);
        campoEmail.setMaximumSize(dimensioneCampo);

        JLabel passwordLabel = new JLabel("Password: ");
        campoPassword = new JPasswordField();
        campoPassword.setPreferredSize(dimensioneCampo);
        campoPassword.setMinimumSize(dimensioneCampo);
        campoPassword.setMaximumSize(dimensioneCampo);

        JButton registrazione = new JButton("Registrati");
        registrazione.setPreferredSize(dimensioneCampo);
        registrazione.setMinimumSize(dimensioneCampo);
        registrazione.setMaximumSize(dimensioneCampo);

        //Aggiunta listener al bottone registrazione
        registrazione.addActionListener(e ->{
            if (!radioSi.isSelected() && !radioNo.isSelected()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Devi specificare se si un ristoratore oppure no!",
                        "Campo obbligatorio",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            String nome = campoNome.getText().trim();
            String cognome = campoCognome.getText().trim();
            String citta = campoCitta.getText().trim();
            String nazione = campoNazione.getText().trim();
            String email = campoEmail.getText().trim();
            String password = new String(campoPassword.getPassword());
            boolean isRistoratore = radioSi.isSelected();

            RegistrazioneDTO dto = new RegistrazioneDTO(nome, cognome, email, password, citta, nazione, isRistoratore);
            Richiesta richiesta = new Richiesta(TipoRichieste.REGISTER, dto);
            Risposta risposta = ClientConnection.inviaRichiesta(richiesta);


            if (risposta != null && risposta.getSuccesso()) {

                // Login riuscito -> ora recuperiamo il ruolo dell'utente
                Richiesta richiestaUtente = new Richiesta(TipoRichieste.GET_UTENTE, email);
                Risposta rispostaUtente = ClientConnection.inviaRichiesta(richiestaUtente);

                if (rispostaUtente != null && rispostaUtente.getSuccesso()) {
                    RegistrazioneDTO datiUtente = (RegistrazioneDTO) rispostaUtente.getContenuto();

                    if (datiUtente.isRistoratore()) {
                        frame.setContentPane(new RistoratoreGUI(frame, utenteService, email));
                    } else {
                        frame.setContentPane(new LoggatoGUI(frame, utenteService, email));
                    }
                    frame.revalidate();
                    frame.repaint();

                } else {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Login riuscito ma impossibile recuperare i dati utente",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } else {
                String messaggioErrore = risposta != null
                        ? risposta.getMsg()
                        : "Impossibile contattare il server";

                JOptionPane.showMessageDialog(
                        frame,
                        messaggioErrore,
                        "Errore di login",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        //composizione griglia
        // nome - cognome
        vincoloGriglia.gridx = 0; //colonna 0 della griglia
        vincoloGriglia.gridy = 0; //riga 0 della griglia
        pannelloCentrale.add(nomeLabel, vincoloGriglia);
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 1;
        pannelloCentrale.add(campoNome, vincoloGriglia);

        vincoloGriglia.gridx = 1;
        vincoloGriglia.gridy = 0;
        pannelloCentrale.add(cognomeLabel, vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.gridy = 1;
        pannelloCentrale.add(campoCognome, vincoloGriglia);

        // città - nazione
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 2;
        pannelloCentrale.add(nazioneLabel, vincoloGriglia);
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 3;
        pannelloCentrale.add(campoNazione, vincoloGriglia);

        vincoloGriglia.gridx = 1;
        vincoloGriglia.gridy = 2;
        pannelloCentrale.add(cittaLabel, vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.gridy = 3;
        pannelloCentrale.add(campoCitta, vincoloGriglia);

        //ristoratore
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 4;
        vincoloGriglia.gridwidth = 2;   //per fare in modo che occupa due celle
        vincoloGriglia.anchor = GridBagConstraints.CENTER;
        pannelloCentrale.add(ristoratoreLabel, vincoloGriglia);

        JPanel radioPanel = new JPanel();
        radioPanel.add(radioSi);
        radioPanel.add(radioNo);
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 5;
        pannelloCentrale.add(radioPanel, vincoloGriglia);

        vincoloGriglia.gridwidth = 1;

        //email - password
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 6;
        pannelloCentrale.add(emailLabel, vincoloGriglia);
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 7;
        pannelloCentrale.add(campoEmail, vincoloGriglia);

        vincoloGriglia.gridx = 1;
        vincoloGriglia.gridy = 6;
        pannelloCentrale.add(passwordLabel, vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.gridy = 7;
        pannelloCentrale.add(campoPassword, vincoloGriglia);

        //bottone login
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 8;
        vincoloGriglia.gridwidth = 2;
        pannelloCentrale.add(registrazione, vincoloGriglia);

        vincoloGriglia.gridwidth = 1;

        //centratura
        JPanel centroPannello = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centroPannello.add(pannelloCentrale);
        add(centroPannello, BorderLayout.CENTER);

        //bottone che ti riporta alla home
        JButton home = new JButton("Home");
        home.setFocusPainted(false);
        home.setBorder(new LineBorder(Color.WHITE));
        home.addActionListener(e ->{
            frame.setContentPane(new GuestGUI(frame,utenteService));
            frame.revalidate();
            frame.repaint();
        });

        visualizzaProfilo.setVisible(false);

        pannello.add(home);



    }
}

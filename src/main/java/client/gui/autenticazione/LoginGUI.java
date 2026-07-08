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
import main.java.shared.dto.LoginDTO;
import main.java.shared.dto.RegistrazioneDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class LoginGUI extends TemplateGUI {
    private JTextField campoEmail;
    private JPasswordField campoPassword;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 10000;

    public LoginGUI(JFrame frame, UtenteService utenteService) {
        super(frame);
        this.frame = frame;

        //Creazione pannello verticale con BoxLayout (asse Y)
        JPanel pannelloCentrale = new JPanel();
        pannelloCentrale.setLayout(new BoxLayout(pannelloCentrale, BoxLayout.Y_AXIS));
        pannelloCentrale.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        Dimension dimensioneCampo = new Dimension(300, 40);

        JLabel titoloCentrale = new JLabel("Non sei registrato!");
        titoloCentrale.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel titoloCentrale2 = new JLabel("Puoi accedere, oppure registrarti!");
        titoloCentrale2.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoEmail = new JTextField();
        campoEmail.setPreferredSize(dimensioneCampo);
        campoEmail.setMinimumSize(dimensioneCampo);
        campoEmail.setMaximumSize(dimensioneCampo);
        campoEmail.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //CAMPO PASSWORD CON BOTTONE OCCHIO
        campoPassword = new JPasswordField();
        campoPassword.setPreferredSize(dimensioneCampo);
        campoPassword.setMinimumSize(dimensioneCampo);
        campoPassword.setMaximumSize(dimensioneCampo);

        JLayeredPane pannelloPassword = creaCampoPasswordConOcchio(campoPassword, dimensioneCampo);
        pannelloPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        /*campoPassword = new JPasswordField();
        campoPassword.setPreferredSize(dimensioneCampo);
        campoPassword.setMinimumSize(dimensioneCampo);
        campoPassword.setMaximumSize(dimensioneCampo);
        campoPassword.setAlignmentX(Component.CENTER_ALIGNMENT);*/


        JButton effettuaLogin = new JButton("Effettua Login");
        effettuaLogin.setPreferredSize(dimensioneCampo);
        effettuaLogin.setMinimumSize(dimensioneCampo);
        effettuaLogin.setMaximumSize(dimensioneCampo);
        effettuaLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        effettuaLogin.setFocusPainted(false);
        effettuaLogin.setBorder(new LineBorder(Color.WHITE));


        effettuaLogin.addActionListener(e -> {
            String email = campoEmail.getText().trim();
            String password = new String(campoPassword.getPassword());

            LoginDTO dto = new LoginDTO(email, password);
            Richiesta richiesta = new Richiesta(TipoRichieste.LOGIN, dto);
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



        JButton registrazione = new JButton("Non sei registrato? Registrati qui!");
        registrazione.setPreferredSize(dimensioneCampo);
        registrazione.setMinimumSize(dimensioneCampo);
        registrazione.setMaximumSize(dimensioneCampo);
        registrazione.setAlignmentX(Component.CENTER_ALIGNMENT);
        registrazione.addActionListener(e ->{
            frame.setContentPane(new RegistrazioneGUI(frame,utenteService));
            frame.revalidate();
            frame.repaint();
        });
        registrazione.setFocusPainted(false);
        registrazione.setBorder(new LineBorder(Color.WHITE));

        pannelloCentrale.add(titoloCentrale);
        pannelloCentrale.add(titoloCentrale2);
        pannelloCentrale.add(Box.createVerticalStrut(15));


        pannelloCentrale.add(emailLabel);
        pannelloCentrale.add(Box.createVerticalStrut(5));
        pannelloCentrale.add(campoEmail);
        pannelloCentrale.add(Box.createVerticalStrut(40));

        pannelloCentrale.add(passwordLabel);
        pannelloCentrale.add(Box.createVerticalStrut(5));
        //pannelloCentrale.add(campoPassword);
        pannelloCentrale.add(pannelloPassword);
        pannelloCentrale.add(Box.createVerticalStrut(40));

        pannelloCentrale.add(effettuaLogin);
        pannelloCentrale.add(Box.createVerticalStrut(10));
        pannelloCentrale.add(registrazione);

        //centratura del pannelloCentrale
        JPanel centroPannello = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centroPannello.add(pannelloCentrale);
        add(centroPannello, BorderLayout.CENTER);

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

    // ---- METODO HELPER PER IL CAMPO PASSWORD CON OCCHIO ----
    private JLayeredPane creaCampoPasswordConOcchio(JPasswordField campoPassword, Dimension dimensioneCampo) {
        char echoCharDefault = campoPassword.getEchoChar();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(dimensioneCampo);
        layeredPane.setMinimumSize(dimensioneCampo);
        layeredPane.setMaximumSize(dimensioneCampo);

        campoPassword.setBounds(0, 0, dimensioneCampo.width, dimensioneCampo.height);

        JButton bottoneOcchio = new JButton("\uD83D\uDC41"); // 👁
        bottoneOcchio.setFocusPainted(false);
        bottoneOcchio.setBorderPainted(false);
        bottoneOcchio.setContentAreaFilled(false);
        bottoneOcchio.setMargin(new Insets(0, 0, 0, 0));

        int dimBottone = dimensioneCampo.height - 10;
        bottoneOcchio.setBounds(
                dimensioneCampo.width - dimBottone - 5,
                (dimensioneCampo.height - dimBottone) / 2,
                dimBottone,
                dimBottone
        );

        bottoneOcchio.addActionListener(e -> {
            if (campoPassword.getEchoChar() != 0) {
                campoPassword.setEchoChar((char) 0); // mostra il testo
                bottoneOcchio.setText("\uD83D\uDE48"); // 🙈
            } else {
                campoPassword.setEchoChar(echoCharDefault); // nasconde di nuovo
                bottoneOcchio.setText("\uD83D\uDC41"); // 👁
            }
        });

        layeredPane.add(campoPassword, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(bottoneOcchio, JLayeredPane.PALETTE_LAYER);

        return layeredPane;
    }
}

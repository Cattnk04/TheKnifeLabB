package main.java.client.gui.azioniRistoratore;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.listeRistoratore.ListaRecensioniGUI;
import main.java.client.gui.menu.RistoratoreGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.dto.RecensioneDTO;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;

public class RispondiRecensioneGUI extends TemplateGUI {

    public RispondiRecensioneGUI(JFrame frame, UtenteService utenteService, String email, RistoranteDTO ristorante, RecensioneDTO recensione) {
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

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        add(centro, BorderLayout.CENTER);

        GridBagConstraints vincoloGriglia = new GridBagConstraints();
        vincoloGriglia.insets = new Insets(10, 10, 10, 10);
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelNomeRistorante = new JLabel("Ristorante: " + ristorante.getNomeRistorante());

        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 0;
        vincoloGriglia.gridwidth = 2;
        vincoloGriglia.anchor = GridBagConstraints.CENTER;
        centro.add(labelNomeRistorante, vincoloGriglia);

        JLabel labelRecensione = new JLabel("Recensione:");

        vincoloGriglia.gridy = 1;
        vincoloGriglia.gridwidth = 1;
        vincoloGriglia.gridx = 0;
        vincoloGriglia.anchor = GridBagConstraints.NORTHWEST;
        centro.add(labelRecensione, vincoloGriglia);

        JTextArea areaRecensione = new JTextArea(recensione.getRecensione(), 5, 35);
        areaRecensione.setLineWrap(true);
        areaRecensione.setWrapStyleWord(true);
        areaRecensione.setEditable(false);

        JScrollPane scrollRecensione = new JScrollPane(areaRecensione);

        vincoloGriglia.gridx = 1;
        vincoloGriglia.fill = GridBagConstraints.BOTH;
        vincoloGriglia.weightx = 1;
        vincoloGriglia.weighty = 0;
        centro.add(scrollRecensione, vincoloGriglia);

        JLabel labelRisposta = new JLabel("Risposta:");

        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 2;
        vincoloGriglia.weightx = 0;
        vincoloGriglia.weighty = 0;
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;
        centro.add(labelRisposta, vincoloGriglia);

        JTextArea textAreaRisposta = new JTextArea(5, 35);
        textAreaRisposta.setLineWrap(true);
        textAreaRisposta.setWrapStyleWord(true);

        JScrollPane scrollRisposta = new JScrollPane(textAreaRisposta);

        vincoloGriglia.gridx = 1;
        vincoloGriglia.fill = GridBagConstraints.BOTH;
        vincoloGriglia.weightx = 1;
        vincoloGriglia.weighty = 1;
        centro.add(scrollRisposta, vincoloGriglia);

        JButton bottoneRisposta = new JButton("Invia risposta");

        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 3;
        vincoloGriglia.gridwidth = 2;
        vincoloGriglia.weightx = 0;
        vincoloGriglia.weighty = 0;
        vincoloGriglia.fill = GridBagConstraints.NONE;
        vincoloGriglia.anchor = GridBagConstraints.CENTER;

        centro.add(bottoneRisposta, vincoloGriglia);

        bottoneRisposta.addActionListener(e -> {
            if (textAreaRisposta.getText().trim().isEmpty()) {
                mostraTestoNonValido();
                return;
            }

            recensione.setRisposta(textAreaRisposta.getText());

            Richiesta richiesta = new Richiesta(TipoRichieste.RISPONDI_RECENSIONE, recensione);
            Risposta risposta = ClientConnection.inviaRichiesta(richiesta);
            //DA SEMPRE ERRORE
            if(risposta != null){
                System.out.println("Successo: " + risposta.getSuccesso());
                System.out.println("Messaggio: " + risposta.getMsg());
            }

            if (risposta == null || !risposta.getSuccesso()) {
                mostraRispostaErrata();
                return;
            }

            frame.setContentPane(new ListaRecensioniGUI(frame, utenteService, email, ristorante));
            frame.revalidate();
            frame.repaint();
        });

    }
    private void mostraTestoNonValido() {
        JOptionPane.showMessageDialog(this,
                "Il testo della risposta alla recensione è vuoto",
                "Devi scrivere qualcosa",
                JOptionPane.INFORMATION_MESSAGE);
    }
    private void mostraRispostaErrata() {
        JOptionPane.showMessageDialog(this,
                "L'inserimento della risposta non è andato a buon fine",
                "Errore risposta server",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

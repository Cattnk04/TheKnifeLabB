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

        JPanel pannelloDatiRecensione = new JPanel();
        JLabel labelNomeRistorante = new JLabel("Ristorante: " + ristorante.getNomeRistorante());
        JLabel labelTesto = new JLabel(recensione.getRecensione());

        pannelloDatiRecensione.add(labelNomeRistorante);
        pannelloDatiRecensione.add(labelTesto);

        JPanel pannelloRisposta = new JPanel();
        JTextArea textAreaRisposta = new JTextArea();
        textAreaRisposta.setEditable(true);
        JTextField rispondiQui = new JTextField("Inserisci qui la risposta alla recensione scritta sopra");
        pannelloRisposta.add(rispondiQui);
        pannelloRisposta.add(textAreaRisposta);

        JPanel pannelloBottoni = new JPanel();
        JButton bottoneRisposta = new JButton("Invia risposta");
        bottoneRisposta.addActionListener(e -> {
            if(textAreaRisposta.getText().length() < 0){
                mostraTestoNonValido();
                return;
            } else {
                recensione.setRisposta(textAreaRisposta.getText());
                Richiesta richiesta = new Richiesta(TipoRichieste.RISPONDI_RECENSIONE, recensione);
                Risposta risposta = ClientConnection.inviaRichiesta(richiesta);
                if(!risposta.getSuccesso()){
                    mostraTestoNonValido();
                    return;
                } else {
                    frame.setContentPane(new ListaRecensioniGUI(frame, utenteService, email, ristorante));
                }
            }
        });
        pannello.add(pannelloDatiRecensione, BorderLayout.CENTER);
        pannello.add(pannelloRisposta, BorderLayout.CENTER);
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

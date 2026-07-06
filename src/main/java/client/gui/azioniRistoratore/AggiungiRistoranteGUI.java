package main.java.client.gui.azioniRistoratore;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.menu.RistoratoreGUI;
import main.java.server.service.UtenteService;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;

public class AggiungiRistoranteGUI extends TemplateGUI {
    private final JTextField campoNomeRistorante;
    private final JTextField campoCitta;
    private final JTextField campoNazione;
    private final JTextField campoVia;
    private final JTextField campoNumeroCivico;
    private final JTextField campoFasciaPrezzo;
    private final JComboBox campoTipoCucina;
    private final JRadioButton campoDelivery;
    private final JRadioButton campoPrenotazioneOnline;
    private final JButton aggiungiRistorante;

    public AggiungiRistoranteGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;
        //creazione pannello centrale
        JPanel pannelloCentrale = new JPanel();
        pannelloCentrale.setLayout(new BoxLayout(pannelloCentrale, BoxLayout.Y_AXIS));
        pannelloCentrale.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        Dimension dimensioneCampo = new Dimension(300, 40);

        campoNomeRistorante = new JTextField();

        campoNomeRistorante.setEditable(true);
        campoCitta = new JTextField();
        campoCitta.setEditable(true);
        campoNazione = new JTextField();
        campoNazione.setEditable(true);
        campoVia = new JTextField();
        campoVia.setEditable(true);
        campoNumeroCivico = new JTextField();
        campoNumeroCivico.setEditable(true);
        campoFasciaPrezzo = new JTextField();
        campoFasciaPrezzo.setEditable(true);
        campoDelivery = new JRadioButton("Servizio delivery");
        campoDelivery.setSelected(true);
        campoPrenotazioneOnline= new JRadioButton("Servizio prenotazione online");
        campoPrenotazioneOnline.setSelected(true);
        campoTipoCucina = new JComboBox();
        campoTipoCucina.setEditable(true);
        aggiungiRistorante = new JButton("Aggiungi Ristorante");
        aggiungiRistorante.addActionListener(e ->{
            //RistoranteDTO ristorante = new RistoranteDTO(campoNomeRistorante.getText(), email, campoCitta.getText(), campoNumeroCivico.getText(), campoFasciaPrezzo.getText(), );
        });
        //AGGIUNGERE QUERY PER PRENDERE I TIPI DI CUCINA


        pannelloCentrale.add(campoNomeRistorante);
        pannelloCentrale.add(campoCitta);
        pannelloCentrale.add(campoNazione);
        pannelloCentrale.add(campoVia);
        pannelloCentrale.add(campoNumeroCivico);
        pannelloCentrale.add(campoFasciaPrezzo);
        pannelloCentrale.add(campoDelivery);
        pannelloCentrale.add(campoPrenotazioneOnline);
        pannelloCentrale.add(campoTipoCucina);
        pannelloCentrale.add(aggiungiRistorante);



        this.add(pannelloCentrale, BorderLayout.CENTER);
        JButton button = new JButton("Aggiungi Ristorante");

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
    }
}

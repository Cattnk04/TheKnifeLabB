package main.java.client.gui.azioniRistoratore;

import main.java.client.gui.TemplateGUI;

import javax.swing.*;

public class AggiungiRistoranteGUI extends TemplateGUI {

    public AggiungiRistoranteGUI(JFrame frame) {
        super(frame);
        this.frame = frame;
        JTextField nomeRistorante= new JTextField();
        nomeRistorante.setEditable(true);
        JTextField citta = new JTextField();
        citta.setEditable(true);
        JTextField nazione = new JTextField();
        nazione.setEditable(true);
        JTextField via = new JTextField();
        via.setEditable(true);
        JTextField numeroCivico = new JTextField();
        numeroCivico.setEditable(true);
        JTextField fasciaPrezzo = new JTextField();
        fasciaPrezzo.setEditable(true);
        JRadioButton rbDelivery = new JRadioButton("Servizio delivery");
        rbDelivery.setSelected(true);
        JRadioButton rbPrenotazioneOnline = new JRadioButton("Servizio prenotazione online");
        rbPrenotazioneOnline.setSelected(true);
        JComboBox cbTipoCucina = new JComboBox();
        cbTipoCucina.setEditable(true);
        //AGGIUNGERE QUERY PER PRENDERE I TIPI DI CUCINA
        this.add(nomeRistorante);
        this.add(citta);
        this.add(nazione);
        this.add(via);
        this.add(numeroCivico);
        this.add(rbDelivery);
        this.add(rbPrenotazioneOnline);
        this.add(cbTipoCucina);

        JButton button = new JButton("Aggiungi Ristorante");
    }
}

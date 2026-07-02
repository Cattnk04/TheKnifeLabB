package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.server.service.UtenteService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LoggatoGUI extends TemplateGUI {
    private JTextField campoNomeRitstorante;
    private JTextField campoCitta;
    private JTextField campoNazione;
    private JComboBox campoFasciaPrezzo;
    private JRadioButton radioDeliverySi;
    private JRadioButton radioDeliveryNo;
    private JRadioButton radioDeliveryIndifferente;
    private JRadioButton radioPrenotazioneSi;
    private JRadioButton radioPrenotazioneNo;
    private JRadioButton radioPrenotazioneIndifferente;
    private JComboBox campoTipoCucina;


    private JPanel creaPannelloRicerca(){
        JPanel pannelloRicerca = new JPanel(new GridBagLayout());
        Border bordoTitolo = BorderFactory.createTitledBorder("Cerca il ristorante");
        Border margineInterno = BorderFactory.createEmptyBorder(20, 15, 20, 15); //valori per la distanza tra il bordo e i componenti centrali
        pannelloRicerca.setBorder(BorderFactory.createCompoundBorder(bordoTitolo, margineInterno));
        GridBagConstraints vincoloGriglia = new GridBagConstraints();
        vincoloGriglia.insets = new Insets(10, 10, 10, 10);
        vincoloGriglia.anchor = GridBagConstraints.WEST;
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        //popolamento righe e colonne
        //riga 0    nome ristorante: ....
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 0;
        vincoloGriglia.weightx = 0; //la colonna non si allarga
        pannelloRicerca.add(new JLabel("Inserisci il nome del ristorante: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoNomeRitstorante = new JTextField(20);
        pannelloRicerca.add(campoNomeRitstorante, vincoloGriglia);

        //riga 1    città: ...
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 1;
        vincoloGriglia.weightx = 0;
        pannelloRicerca.add(new JLabel("Inserisci la città: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoCitta = new JTextField(20);
        pannelloRicerca.add(campoCitta, vincoloGriglia);

        //riga 2    Nazione: ...
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 2;
        vincoloGriglia.weightx = 0;
        pannelloRicerca.add(new JLabel("Inserisci la nazione: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoNazione = new JTextField(20);
        pannelloRicerca.add(campoNazione, vincoloGriglia);

        //riga 3    FasciaPrezzo:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 3;
        vincoloGriglia.weightx = 0;
        pannelloRicerca.add(new JLabel("Prezzo massimo: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoFasciaPrezzo = new JComboBox<>();
        this.campoFasciaPrezzo.addItem("Qualsiasi prezzo");   // opzione qualsiasi prezzo
        for(int prezzo = 10; prezzo <= 100; prezzo+=10){
            this.campoFasciaPrezzo.addItem(prezzo);
        }
        pannelloRicerca.add(campoFasciaPrezzo, vincoloGriglia);

        //riga 4    servizio delivery
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 4;
        vincoloGriglia.weightx = 0;
        pannelloRicerca.add(new JLabel("Vuoi il servizio delivery?"), vincoloGriglia);
        //creazione pannello per i JRadio
        JPanel pannelloDelivery = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        this.radioDeliverySi = new JRadioButton("Si");
        this.radioDeliveryNo = new JRadioButton("No");
        this.radioDeliveryIndifferente = new JRadioButton("Indifferente");
        this.radioDeliveryIndifferente.setSelected(true);

        //creazione ButtonGroup che permette di avere la mutua esclusività
        ButtonGroup gruppoBottoniDelivery = new ButtonGroup();
        gruppoBottoniDelivery.add(radioDeliverySi);
        gruppoBottoniDelivery.add(radioDeliveryNo);
        gruppoBottoniDelivery.add(radioDeliveryIndifferente);

        pannelloDelivery.add(radioDeliverySi);
        pannelloDelivery.add(radioDeliveryNo);
        pannelloDelivery.add(radioDeliveryIndifferente);

        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        pannelloRicerca.add(pannelloDelivery, vincoloGriglia);

        //riga 5    prenotazione Online
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 5;
        vincoloGriglia.weightx = 0;
        pannelloRicerca.add(new JLabel("Vuoi effettuare la prenotazione online?"), vincoloGriglia);
        //creazione pannello per i JRadio
        JPanel pannelloPrenotazione = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        this.radioPrenotazioneSi = new JRadioButton("Si");
        this.radioPrenotazioneNo = new JRadioButton("No");
        this.radioPrenotazioneIndifferente = new JRadioButton("Indifferente");
        this.radioPrenotazioneIndifferente.setSelected(true);

        //creazione ButtonGroup che permette di avere la mutua esclusività
        ButtonGroup gruppoBottoniPrenotazione = new ButtonGroup();
        gruppoBottoniPrenotazione.add(radioPrenotazioneSi);
        gruppoBottoniPrenotazione.add(radioPrenotazioneNo);
        gruppoBottoniPrenotazione.add(radioPrenotazioneIndifferente);

        pannelloPrenotazione.add(radioPrenotazioneSi);
        pannelloPrenotazione.add(radioPrenotazioneNo);
        pannelloPrenotazione.add(radioPrenotazioneIndifferente);

        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        pannelloRicerca.add(pannelloPrenotazione, vincoloGriglia);

        //riga 6    tipo cucina
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 6;
        vincoloGriglia.weightx = 0;
        pannelloRicerca.add(new JLabel("Tipo di cucina: "), vincoloGriglia);
        this.campoTipoCucina = new JComboBox<>();
        this.campoTipoCucina.addItem("Qualsiasi");
        //caricaTipiCucina();

        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        pannelloRicerca.add(campoTipoCucina, vincoloGriglia);

        //bottone ricerca
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 7;
        vincoloGriglia.gridwidth = 2;
        vincoloGriglia.fill = GridBagConstraints.NONE;  //permette di non riempire orizzontalmente
        vincoloGriglia.anchor = GridBagConstraints.CENTER;
        pannelloRicerca.add(new JButton("Cerca"), vincoloGriglia);
        //DA AGGIUNGERE LISTENER AL BOTTONE RICERCA

        return pannelloRicerca;
    }

    public LoggatoGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;

        visualizzaProfilo.addActionListener(e ->{
            frame.setContentPane(new VisualizzaProfiloGUI(frame));
            frame.revalidate();
            frame.repaint();})
        ;

        this.add(creaPannelloRicerca());
    }
}

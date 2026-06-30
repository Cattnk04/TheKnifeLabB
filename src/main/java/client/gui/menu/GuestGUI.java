package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.dto.TipoCucinaDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.EventListener;
import javax.swing.JOptionPane;
import java.util.List;

public class GuestGUI extends TemplateGUI {
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
        pannelloRicerca.setBorder(BorderFactory.createTitledBorder("Cerca il ristorante:")); //titolo nel bordo del pannello
        BorderFactory.createLineBorder(Color.BLUE);
        BorderFactory.createEtchedBorder(); //bordo con effetto incassato
        GridBagConstraints vincoloGriglia = new GridBagConstraints();
        vincoloGriglia.insets = new Insets(10, 10, 10, 10);
        vincoloGriglia.anchor = GridBagConstraints.WEST;
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        //popolamento righe e colonne
        //riga 0    nome ristorante: ....
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 0;
        vincoloGriglia.weightx = 0; //la colonna non si allarga
        pannelloRicerca.add(new JLabel("inserisci il nome del ristorante: "), vincoloGriglia);
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


        return pannelloRicerca;
    }

    /*private void caricaTipiCucina(){
        Richiesta richiesta = new Richiesta(TipoRichieste.GET_TIPO_CUCINA, null);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);
        if (risposta != null && risposta.getSuccesso()) {
            List<TipoCucinaDTO> tipiCucina = (List<TipoCucinaDTO>) risposta.getContenuto();
            for(TipoCucinaDTO tipo: tipiCucina){
                campoTipoCucina.addItem(tipo);
            }
        } else {
            String messaggioErrore;
            if(risposta != null){
                messaggioErrore = risposta.getMsg();
            } else {
                messaggioErrore = "Impossibile contattare il server";
            }
            JOptionPane.showMessageDialog(this, messaggioErrore, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

     */



    public GuestGUI(JFrame frame, UtenteService utenteService) {
        super(frame);
        this.frame = frame;

        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new LoginGUI(frame,utenteService));
            frame.revalidate();
            frame.repaint();
        });

        this.add(creaPannelloRicerca());





        /*
        JLabel consigliati = new JLabel("Consigliati");
        consigliati.setFocusable(false);
        consigliati.setFont(new Font("Arial", Font.BOLD, 30));
        this.add(consigliati);
         */



        /*
        Popola le nazioni all'avvio
JComboBox<String> comboNazione = new JComboBox<>();
JComboBox<String> comboCitta = new JComboBox<>();

// --- Carica nazioni dal DB ---
try {
    DTO/DAO String query = "SELECT DISTINCT nazione FROM ristoranti"; // adatta alla tua tabella
    PreparedStatement ps = connection.prepareStatement(query);
    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
        comboNazione.addItem(rs.getString("nazione"));
    }
} catch (SQLException ex) {
    ex.printStackTrace();
}

// --- Quando cambia nazione, carica le città ---
comboNazione.addActionListener(e -> {
    String nazioneScelta = (String) comboNazione.getSelectedItem();
    comboCitta.removeAllItems();

    try {
        String query = "SELECT DISTINCT citta FROM ristoranti WHERE nazione = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, nazioneScelta);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            comboCitta.addItem(rs.getString("citta"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
});

panel.add(comboNazione);
panel.add(comboCitta);
         */


    }
}

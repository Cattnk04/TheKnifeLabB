package main.java.client.gui.azioniRistoratore;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.menu.RistoratoreGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.dto.RistoranteDTO;
import main.java.shared.dto.TipoCucinaDTO;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

/**
 *
 */
public class AggiungiRistoranteGUI extends TemplateGUI {
    private final JTextField campoNomeRistorante;
    private final JTextField campoCitta;
    private final JTextField campoNazione;
    private final JTextField campoVia;
    private final JTextField campoNumeroCivico;
    private final JTextField campoFasciaPrezzo;
    private final JComboBox<Object> campoTipoCucina;
    private final JRadioButton radioDeliverySi;
    private final JRadioButton radioDeliveryNo;
    private final JRadioButton radioPrenotazioneSi;
    private final JRadioButton radioPrenotazioneNo;
    private final JButton btnCrea;

    /**
     *
     * @param frame
     * @param utenteService
     * @param email
     */
    public AggiungiRistoranteGUI(JFrame frame, UtenteService utenteService, String email) {
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

        Border bordoTitolo = BorderFactory.createTitledBorder("Registra il tuo ristorante");
        Border margineInterno = BorderFactory.createEmptyBorder(20, 15, 20, 15); //valori per la distanza tra il bordo e i componenti centrali
        centro.setBorder(BorderFactory.createCompoundBorder(bordoTitolo, margineInterno));

        GridBagConstraints vincoloGriglia = new GridBagConstraints();
        vincoloGriglia.insets = new Insets(10, 10, 10, 10);
        vincoloGriglia.anchor = GridBagConstraints.WEST;
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        //popolamento righe e colonne
        //riga 0 - nome ristorante
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 0;
        vincoloGriglia.weightx = 0; //la colonna non si allarga
        centro.add(new JLabel("Inserisci il nome del ristorante: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoNomeRistorante = new JTextField();
        centro.add(campoNomeRistorante, vincoloGriglia);

        //riga 1 - città
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 1;
        vincoloGriglia.weightx = 0;
        centro.add(new JLabel("Inserisci la città: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoCitta = new JTextField(20);
        centro.add(campoCitta, vincoloGriglia);

        //riga 2 - Nazione
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 2;
        vincoloGriglia.weightx = 0;
        centro.add(new JLabel("Inserisci la nazione: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoNazione = new JTextField(20);
        centro.add(campoNazione, vincoloGriglia);

        //Riga 3 - via
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 3;
        vincoloGriglia.weightx = 0;
        centro.add(new JLabel("Inserisci la via: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoVia = new JTextField(20);
        centro.add(campoVia, vincoloGriglia);

        //Riga 4 - numero civico
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 4;
        vincoloGriglia.weightx = 0;
        centro.add(new JLabel("Inserisci il numero civico: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoNumeroCivico = new JTextField(3);
        centro.add(campoNumeroCivico, vincoloGriglia);

        //riga 5 - FasciaPrezzo
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 5;
        vincoloGriglia.weightx = 0;
        centro.add(new JLabel("Prezzo massimo: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoFasciaPrezzo = new JTextField(3);
        centro.add(campoFasciaPrezzo, vincoloGriglia);

        //riga 6 - servizio delivery
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 6;
        vincoloGriglia.weightx = 0;
        centro.add(new JLabel("Il tuo ristorante effettua il servizio delivery?"), vincoloGriglia);
        //creazione pannello per i JRadio del delivery
        JPanel pannelloDelivery = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        this.radioDeliverySi = new JRadioButton("Si");
        this.radioDeliveryNo = new JRadioButton("No");
        radioDeliverySi.setSelected(true);
        //raggruppo i bottoni della sezione delivery
        ButtonGroup gruppoBottoniDelivery = new ButtonGroup();
        gruppoBottoniDelivery.add(radioDeliverySi);
        gruppoBottoniDelivery.add(radioDeliveryNo);

        pannelloDelivery.add(radioDeliverySi);
        pannelloDelivery.add(radioDeliveryNo);

        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        centro.add(pannelloDelivery, vincoloGriglia);

        //riga 7 - servizio prenotazione online
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 7;
        vincoloGriglia.weightx = 0;
        centro.add(new JLabel("Il tuo ristorante permette le prenotazioni online?"), vincoloGriglia);
        //pannello per il gruppo bottoni per prenotazione online
        JPanel pannelloPrenotazione = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        this.radioPrenotazioneSi = new JRadioButton("Si");
        this.radioPrenotazioneNo = new JRadioButton("No");
        this.radioPrenotazioneSi.setSelected(true);

        //creazione ButtonGroup
        ButtonGroup gruppoBottoniPrenotazione = new ButtonGroup();
        gruppoBottoniPrenotazione.add(radioPrenotazioneSi);
        gruppoBottoniPrenotazione.add(radioPrenotazioneNo);

        pannelloPrenotazione.add(radioPrenotazioneSi);
        pannelloPrenotazione.add(radioPrenotazioneNo);

        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        centro.add(pannelloPrenotazione, vincoloGriglia);

        //riga 8 - tipo cucina
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 8;
        vincoloGriglia.weightx = 0;
        centro.add(new JLabel("Che tipo di cucina offre il tuo ristorante?: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        campoTipoCucina = new JComboBox<>();
        caricaTipiCucina();
        centro.add(campoTipoCucina, vincoloGriglia);

        //riga 9 - bottone creazione
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 9;
        vincoloGriglia.gridwidth = 2;
        vincoloGriglia.fill = GridBagConstraints.NONE;  //permette di non riempire orizzontalmente
        vincoloGriglia.anchor = GridBagConstraints.CENTER;
        this.btnCrea = new JButton("Crea ristorante");
        centro.add(btnCrea, vincoloGriglia);

        btnCrea.addActionListener(e -> eseguiCreazioneRistorante(email, utenteService));
    }

    /**
     *
     * @param email
     * @param utenteService
     */
    private void eseguiCreazioneRistorante(String email, UtenteService utenteService) {
        //creazione dei dati per il DTO da dare al service per effettuare la query sul DB
        String nomeRistorante = campoNomeRistorante.getText().trim();
        String citta = campoCitta.getText().trim();
        String nazione = campoNazione.getText().trim();
        String via = campoVia.getText().trim();
        int numeroCivico = Integer.parseInt(campoNumeroCivico.getText().trim());
        int fasciaPrezzo = Integer.parseInt(campoFasciaPrezzo.getText().trim());
        boolean delivery = this.radioDeliverySi.isSelected();
        boolean prenotazioneOnline = this.radioPrenotazioneSi.isSelected();
        int indiceSelezionato = campoTipoCucina.getSelectedIndex();
        int tipoCucina;
        TipoCucinaDTO tipoSelezionato = (TipoCucinaDTO) campoTipoCucina.getSelectedItem();
        tipoCucina = tipoSelezionato.getId(); // adatta al metodo reale del DTO

        RistoranteDTO dtoRistorante = new RistoranteDTO(nomeRistorante, email, citta, nazione,
                via, numeroCivico, fasciaPrezzo, delivery, prenotazioneOnline, tipoCucina);
        Richiesta richiesta = new Richiesta(TipoRichieste.CREA_RISTORANTE, dtoRistorante);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

        if(risposta != null && risposta.getSuccesso()) {
            frame.setContentPane(new RistoratoreGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        } else {
            String messaggioErrore = (risposta != null) ? risposta.getMsg() : "Impossibile contattare il server";
            JOptionPane.showMessageDialog(this, messaggioErrore, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     */
    private void caricaTipiCucina() {
        Richiesta richiesta = new Richiesta(TipoRichieste.GET_TIPO_CUCINA, null);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);
        if (risposta != null && risposta.getSuccesso()) {
            @SuppressWarnings("unchecked")
            java.util.List<TipoCucinaDTO> tipiCucina = (List<TipoCucinaDTO>) risposta.getContenuto();
            for (TipoCucinaDTO tipo : tipiCucina) {
                campoTipoCucina.addItem(tipo);
            }
        } else {
            String messaggioErrore;
            if (risposta != null) {
                messaggioErrore = risposta.getMsg();
            } else {
                messaggioErrore = "Impossibile contattare il server";
            }
            JOptionPane.showMessageDialog(this, messaggioErrore, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}

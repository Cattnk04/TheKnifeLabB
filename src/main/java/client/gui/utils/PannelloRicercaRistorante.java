package main.java.client.gui.utils;

import main.java.client.network.ClientConnection;
import main.java.shared.communication.*;
import main.java.shared.dto.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/*
 * Pannello riusabile che contiene il form di ricerca dei ristoranti.
 * Utilizzabile sia da utenti Guest che da Clienti loggati.
 */
public class PannelloRicercaRistorante extends JPanel {

    private JTextField campoNomeRitstorante;
    private JTextField campoCitta;
    private JTextField campoNazione;
    private JComboBox<Object> campoFasciaPrezzo;
    private JRadioButton radioDeliverySi;
    private JRadioButton radioDeliveryNo;
    private JRadioButton radioDeliveryIndifferente;
    private JRadioButton radioPrenotazioneSi;
    private JRadioButton radioPrenotazioneNo;
    private JRadioButton radioPrenotazioneIndifferente;
    private JComboBox<Object> campoTipoCucina;
    private JButton btnRicerca;

    // Callback invocata quando la ricerca ha successo, con la lista dei risultati
    private final Consumer<List<RistoranteDTO>> onRisultatiTrovati;

    public PannelloRicercaRistorante(Consumer<List<RistoranteDTO>> onRisultatiTrovati) {
        this.onRisultatiTrovati = onRisultatiTrovati;

        setLayout(new GridBagLayout());
        Border bordoTitolo = BorderFactory.createTitledBorder("Cerca il ristorante");
        Border margineInterno = BorderFactory.createEmptyBorder(20, 15, 20, 15); //valori per la distanza tra il bordo e i componenti centrali
        setBorder(BorderFactory.createCompoundBorder(bordoTitolo, margineInterno));

        GridBagConstraints vincoloGriglia = new GridBagConstraints();
        vincoloGriglia.insets = new Insets(10, 10, 10, 10);
        vincoloGriglia.anchor = GridBagConstraints.WEST;
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        //popolamento righe e colonne
        //riga 0 - nome ristorante:...
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 0;
        vincoloGriglia.weightx = 0; //la colonna non si allarga
        add(new JLabel("Inserisci il nome del ristorante: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoNomeRitstorante = new JTextField(20);
        add(campoNomeRitstorante, vincoloGriglia);

        //riga 1 - città:...
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 1;
        vincoloGriglia.weightx = 0;
        add(new JLabel("Inserisci la città: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoCitta = new JTextField(20);
        add(campoCitta, vincoloGriglia);

        //riga 2 - Nazione:...
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 2;
        vincoloGriglia.weightx = 0;
        add(new JLabel("Inserisci la nazione: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoNazione = new JTextField(20);
        add(campoNazione, vincoloGriglia);

        //riga 3 - FasciaPrezzo
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 3;
        vincoloGriglia.weightx = 0;
        add(new JLabel("Prezzo massimo: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        this.campoFasciaPrezzo = new JComboBox<>();
        this.campoFasciaPrezzo.addItem("Qualsiasi prezzo");   // opzione qualsiasi prezzo
        for (int prezzo = 10; prezzo <= 100; prezzo += 10) {
            this.campoFasciaPrezzo.addItem(prezzo);
        }
        add(campoFasciaPrezzo, vincoloGriglia);

        //riga 4 - servizio delivery
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 4;
        vincoloGriglia.weightx = 0;
        add(new JLabel("Vuoi il servizio delivery?"), vincoloGriglia);
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
        add(pannelloDelivery, vincoloGriglia);

        //riga 5 - prenotazione Online
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 5;
        vincoloGriglia.weightx = 0;
        add(new JLabel("Vuoi effettuare la prenotazione online?"), vincoloGriglia);
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
        add(pannelloPrenotazione, vincoloGriglia);

        //riga 6 - tipo cucina
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 6;
        vincoloGriglia.weightx = 0;
        add(new JLabel("Tipo di cucina: "), vincoloGriglia);
        this.campoTipoCucina = new JComboBox<>();
        this.campoTipoCucina.addItem("Qualsiasi");
        caricaTipiCucina();

        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 0;
        add(campoTipoCucina, vincoloGriglia);

        //bottone ricerca
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 7;
        vincoloGriglia.gridwidth = 2;
        vincoloGriglia.fill = GridBagConstraints.NONE;  //permette di non riempire orizzontalmente
        vincoloGriglia.anchor = GridBagConstraints.CENTER;
        this.btnRicerca = new JButton("Ricerca");
        add(btnRicerca, vincoloGriglia);

        btnRicerca.addActionListener(e -> eseguiRicerca());
    }

    private void eseguiRicerca() {
        Object itemFasciaPrezzo = campoFasciaPrezzo.getSelectedItem();
        Integer fasciaPrezzoMassima = (itemFasciaPrezzo instanceof Integer) ? (Integer) itemFasciaPrezzo : null;

        Integer delivery;
        if (radioDeliveryIndifferente.isSelected()) delivery = null;
        else if (radioDeliveryNo.isSelected()) delivery = 0;
        else delivery = 1;

        Integer prenotazioneOnline;
        if (radioPrenotazioneIndifferente.isSelected()) prenotazioneOnline = null;
        else if (radioPrenotazioneNo.isSelected()) prenotazioneOnline = 0;
        else prenotazioneOnline = 1;

        int indiceSelezionato = campoTipoCucina.getSelectedIndex();
        Integer tipoCucina;
        if (indiceSelezionato <= 0) {
            tipoCucina = null; // "Qualsiasi" -> nessun filtro
        } else {
            TipoCucinaDTO tipoSelezionato = (TipoCucinaDTO) campoTipoCucina.getSelectedItem();
            tipoCucina = tipoSelezionato.getId(); // adatta al metodo reale del DTO
        }

        String citta = campoCitta.getText().trim().isBlank() ? null : campoCitta.getText().trim();
        String nazione = campoNazione.getText().trim().isBlank() ? null : campoNazione.getText().trim();
        String nome = campoNomeRitstorante.getText().trim().isBlank() ? null : campoNomeRitstorante.getText().trim();

        FiltroRicercaDTO filtro = new FiltroRicercaDTO(
                nome, citta, nazione, fasciaPrezzoMassima, delivery, prenotazioneOnline, tipoCucina
        );

        Richiesta richiestaRicerca = new Richiesta(TipoRichieste.CERCA_RISTORANTE, filtro);
        Risposta rispostaRicerca = ClientConnection.inviaRichiesta(richiestaRicerca);

        if (rispostaRicerca != null && rispostaRicerca.getSuccesso()) {
            @SuppressWarnings("unchecked")
            List<RistoranteDTO> risultati = (List<RistoranteDTO>) rispostaRicerca.getContenuto();
            onRisultatiTrovati.accept(risultati);
        } else {
            String messaggioErrore = (rispostaRicerca != null) ? rispostaRicerca.getMsg() : "Impossibile contattare il server";
            JOptionPane.showMessageDialog(this, messaggioErrore, "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void caricaTipiCucina() {
        Richiesta richiesta = new Richiesta(TipoRichieste.GET_TIPO_CUCINA, null);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);
        if (risposta != null && risposta.getSuccesso()) {
            @SuppressWarnings("unchecked")
            List<TipoCucinaDTO> tipiCucina = (List<TipoCucinaDTO>) risposta.getContenuto();
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
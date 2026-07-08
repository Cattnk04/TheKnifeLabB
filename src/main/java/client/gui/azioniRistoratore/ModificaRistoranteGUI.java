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
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;

public class ModificaRistoranteGUI extends TemplateGUI {
    JFrame frame;
    private final UtenteService utenteService;
    private final String email;
    private final RistoranteDTO ristorante;

    private JTextField campoNomeRistorante;
    private JTextField campoEmail;
    private JTextField campoCitta;
    private JTextField campoNazione;
    private JTextField campoVia;
    private JFormattedTextField campoNumeroCivico;
    private JFormattedTextField campoFasciaPrezzo;
    private JRadioButton campoDelivery;
    private JRadioButton deliverySi;
    private JRadioButton deliveryNo;
    private JRadioButton campoPrenotazione;
    private JRadioButton prenotazioneSi;
    private JRadioButton prenotazioneNo;
    private JComboBox<TipoCucinaDTO> campoTipoCucina;



    public ModificaRistoranteGUI(JFrame frame, UtenteService utenteService, String email, RistoranteDTO ristorante) {
        super(frame);
        this.frame = frame;
        this.utenteService = utenteService;
        this.email = email;
        this.ristorante = ristorante;

        //visualizzaProfilo
        visualizzaProfilo.setVisible(true);
        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

        //Creazione griglia
        JPanel pannelloCentrale = new JPanel(new GridBagLayout());
        GridBagConstraints vincoloGriglia = new GridBagConstraints();
        vincoloGriglia.insets = new Insets(12, 15, 12, 15);
        vincoloGriglia.anchor = GridBagConstraints.WEST;
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        //riga 0: nome ristorante:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 0;
        pannelloCentrale.add(new JLabel("Nome del ristornate: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        campoNomeRistorante = new JTextField(ristorante.getNomeRistorante(), 20);
        pannelloCentrale.add(campoNomeRistorante, vincoloGriglia);

        //riga 1: email (non modificabile)
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 1;
        pannelloCentrale.add(new JLabel("Email: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        JLabel campoEmail = new JLabel(ristorante.getEmail());
        pannelloCentrale.add(campoEmail, vincoloGriglia);

        //riga 2: Città:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 2;
        pannelloCentrale.add(new JLabel("Città: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        campoCitta = new JTextField(ristorante.getCitta(), 20);
        pannelloCentrale.add(campoCitta, vincoloGriglia);

        //riga 3: Nazione:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 3;
        pannelloCentrale.add(new JLabel("Nazione: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        campoNazione = new JTextField(ristorante.getNazione(), 20);
        pannelloCentrale.add(campoNazione, vincoloGriglia);

        //riga 4: Via:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 4;
        pannelloCentrale.add(new JLabel("Via: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        campoVia = new JTextField(ristorante.getVia(), 20);
        pannelloCentrale.add(campoVia, vincoloGriglia);

        //riga 5: Numero civico:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 5;
        pannelloCentrale.add(new JLabel("Numero civico: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        NumberFormatter formatterCivico = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatterCivico.setValueClass(Integer.class);
        formatterCivico.setAllowsInvalid(false);
        formatterCivico.setMinimum(1);
        campoNumeroCivico = new JFormattedTextField(formatterCivico);
        campoNumeroCivico.setValue(ristorante.getNumeroCivico());
        pannelloCentrale.add(campoNumeroCivico, vincoloGriglia);

        //riga 6: Fascia Prezzo:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 6;
        pannelloCentrale.add(new JLabel("Fascia di prezzo: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        NumberFormatter formatterFasciaPrezzo = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatterFasciaPrezzo.setValueClass(Integer.class);
        formatterFasciaPrezzo.setAllowsInvalid(false);
        formatterFasciaPrezzo.setMinimum(1);
        formatterFasciaPrezzo.setMaximum(100);
        campoFasciaPrezzo = new JFormattedTextField(formatterFasciaPrezzo);
        campoFasciaPrezzo.setValue(ristorante.getFasciaPrezzo());
        pannelloCentrale.add(campoFasciaPrezzo, vincoloGriglia);

        //Riga 7: Servizio delivery:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 7;
        pannelloCentrale.add(new JLabel("Servizio delivery: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        deliverySi = new JRadioButton("Si");
        deliveryNo = new JRadioButton("No");

        ButtonGroup gruppoDelivery = new ButtonGroup();
        gruppoDelivery.add(deliverySi);
        gruppoDelivery.add(deliveryNo);
        //imposta il valore attuale
        if(ristorante.isDelivery()){
            deliverySi.setSelected(true);
        } else {
            deliveryNo.setSelected(true);
        }
        JPanel pannelloDelivery = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pannelloDelivery.add(deliverySi);
        pannelloDelivery.add(deliveryNo);

        pannelloCentrale.add(pannelloDelivery, vincoloGriglia);

        //riga 8: Prenotazione online:
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 8;
        pannelloCentrale.add(new JLabel("Prenotazione online: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        prenotazioneSi = new JRadioButton("Si");
        prenotazioneNo = new JRadioButton("No");

        ButtonGroup gruppoPrenotazione = new ButtonGroup();
        gruppoPrenotazione.add(prenotazioneSi);
        gruppoPrenotazione.add(prenotazioneNo);
        //imposta il valore attuale
        if(ristorante.isPrenotazioneOnline()){
            prenotazioneSi.setSelected(true);
        } else {
            prenotazioneNo.setSelected(true);
        }
        JPanel pannelloPrenotazioneOnline = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pannelloPrenotazioneOnline.add(prenotazioneSi);
        pannelloPrenotazioneOnline.add(prenotazioneNo);

        pannelloCentrale.add(pannelloPrenotazioneOnline, vincoloGriglia);

        //riga 9: Tipo di cucina
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 9;
        pannelloCentrale.add(new JLabel("Tipi di cucina: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        campoTipoCucina = new JComboBox<>();
        caricaTipiCucina();
        pannelloCentrale.add(campoTipoCucina, vincoloGriglia);

        //riga 10: bottone salva
        JButton salva = new JButton("Salva le modifiche");
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 10;
        vincoloGriglia.gridwidth = 2;
        pannelloCentrale.add(salva, vincoloGriglia);
        salva.addActionListener(e -> salvaModifiche());

        this.add(pannelloCentrale, BorderLayout.CENTER);

        JButton home = new JButton("Home");
        home.addActionListener(e -> {
            frame.setContentPane(new RistoratoreGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });
        pannello.add(home);

    }
    private void caricaTipiCucina() {
        Richiesta richiesta = new Richiesta(TipoRichieste.GET_TIPO_CUCINA, null);
        Risposta risposta = ClientConnection.inviaRichiesta(richiesta);
        if (risposta != null && risposta.getSuccesso()) {
            @SuppressWarnings("unchecked")
            java.util.List<TipoCucinaDTO> tipiCucina = (List<TipoCucinaDTO>) risposta.getContenuto();
            for (TipoCucinaDTO tipo : tipiCucina) {
                campoTipoCucina.addItem(tipo);
                if(tipo.getIdTipoCucina() == ristorante.getTipoCucina()){
                    campoTipoCucina.setSelectedItem(tipo);
                }
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

    private void salvaModifiche() {
        try{
            String nome = campoNomeRistorante.getText().trim();
            String citta = campoCitta.getText().trim();
            String nazione = campoNazione.getText().trim();
            String via = campoVia.getText().trim();
            int numeroCivico = (Integer) campoNumeroCivico.getValue();
            int fasciaPrezzo =  (Integer) campoFasciaPrezzo.getValue();
            boolean delivery = deliverySi.isSelected();
            boolean prenotazioneOnline = prenotazioneSi.isSelected();
            TipoCucinaDTO tipoSelezionato = (TipoCucinaDTO) campoTipoCucina.getSelectedItem();
            int idTipoCucina = tipoSelezionato.getIdTipoCucina();

            RistoranteDTO ristoranteModificato = new RistoranteDTO(
                    ristorante.getIdRistorante(),
                    nome,
                    ristorante.getEmail(),
                    citta,
                    nazione,
                    via,
                    numeroCivico,
                    fasciaPrezzo,
                    delivery,
                    prenotazioneOnline,
                    idTipoCucina
            );

            Richiesta richiesta = new Richiesta(TipoRichieste.AGGIORNA_RISTORANTE, ristoranteModificato);
            Risposta risposta = ClientConnection.inviaRichiesta(richiesta);

            if(risposta != null && risposta.getSuccesso()) {
                JOptionPane.showMessageDialog(this, risposta.getMsg(), "Successo", JOptionPane.INFORMATION_MESSAGE);
                frame.setContentPane(new RistoratoreGUI(frame, utenteService, email));
                frame.revalidate();
                frame.repaint();
            } else {
                String messaggioErrore = (risposta != null) ? risposta.getMsg() : "Impossibile contattare il server";
                JOptionPane.showMessageDialog(this, messaggioErrore, "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Controlla i valori numerici inseriti", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore imprevisto: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}

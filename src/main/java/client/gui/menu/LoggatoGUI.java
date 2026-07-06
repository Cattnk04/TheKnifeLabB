package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.ricerca.PannelloRicercaRistorante;
import main.java.client.gui.ricerca.PannelloRisultatiRicerca;
import main.java.server.service.UtenteService;

import javax.swing.*;
import java.awt.*;

public class LoggatoGUI extends TemplateGUI {

    public LoggatoGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;

        // Collega il bottone "profilo" (ereditato da TemplateGUI) alla GUI del profilo
        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

        JPanel contenutoCentrale = new JPanel(new BorderLayout(10, 10));

        // Pannello che mostrerà i risultati della ricerca
        PannelloRisultatiRicerca pannelloRisultati = new PannelloRisultatiRicerca(ristoranteSelezionato -> {
            // TODO: aprire la GUI di dettaglio ristorante (prenotazione, ordine delivery, recensioni...)
            // passando "ristoranteSelezionato" e "email"
        });

        // Pannello di ricerca: quando trova risultati, li passa al pannello dei risultati
        PannelloRicercaRistorante pannelloRicerca = new PannelloRicercaRistorante(risultati -> {
            pannelloRisultati.aggiornaRisultati(risultati);
        });

        contenutoCentrale.add(pannelloRicerca, BorderLayout.NORTH);
        contenutoCentrale.add(pannelloRisultati, BorderLayout.CENTER);

        this.add(contenutoCentrale, BorderLayout.CENTER);

        // Eventuali pulsanti extra solo per il cliente loggato:
        // es. "Le mie prenotazioni", "I miei preferiti", "I miei ordini"
    }
}
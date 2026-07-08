package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.utils.*;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.*;

import javax.swing.*;
import java.awt.*;

public class LoggatoGUI extends TemplateGUI {

    public LoggatoGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;

        visualizzaProfilo.setVisible(true);
        // Collega il bottone "profilo" (ereditato da TemplateGUI) alla GUI del profilo
        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

        logout.setVisible(true);
        logout.addActionListener(e -> {
            Richiesta richiesta = new Richiesta(TipoRichieste.LOGOUT, email);
            ClientConnection.inviaRichiesta(richiesta);

            frame.setContentPane(new LoginGUI(frame, utenteService));
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
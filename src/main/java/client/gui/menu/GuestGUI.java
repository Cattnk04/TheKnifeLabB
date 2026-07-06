package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.ricerca.PannelloRicercaRistorante;
import main.java.client.gui.ricerca.PannelloRisultatiRicerca;
import main.java.server.service.UtenteService;
import main.java.shared.communication.*;
import main.java.shared.dto.*;

import javax.swing.*;
import java.awt.*;

public class GuestGUI extends TemplateGUI {

    public GuestGUI(JFrame frame, UtenteService utenteService) {
        super(frame);
        this.frame = frame;

        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new LoginGUI(frame, utenteService));
            frame.revalidate();
            frame.repaint();
        });

        JPanel contenutoCentrale = new JPanel(new BorderLayout(10, 10));

        // Pannello che mostrerà i risultati della ricerca
        PannelloRisultatiRicerca pannelloRisultati = new PannelloRisultatiRicerca(ristoranteSelezionato -> {
            // TODO: aprire la GUI di dettaglio ristorante (prenotazione, ordine delivery, recensioni...)
            // passando "ristoranteSelezionato" e "email" (utile per sapere chi sta prenotando/ordinando)
        });

        // Pannello di ricerca: quando trova risultati, li passa al pannello dei risultati
        PannelloRicercaRistorante pannelloRicerca = new PannelloRicercaRistorante(risultati -> {
            pannelloRisultati.aggiornaRisultati(risultati);
        });

        contenutoCentrale.add(pannelloRicerca, BorderLayout.NORTH);
        contenutoCentrale.add(pannelloRisultati, BorderLayout.CENTER);

        this.add(contenutoCentrale, BorderLayout.CENTER);
    }
}
package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.utils.DettagliRistoranteGUI;
import main.java.client.gui.utils.PannelloRicercaRistorante;
import main.java.client.gui.utils.PannelloRisultatiRicerca;
import main.java.server.service.UtenteService;

import javax.swing.*;
import java.awt.*;

/**
 * GUI principale mostrata agli utenti non autenticati (ospiti), che permette
 * di cercare ristoranti tramite l'apposito pannello di ricerca e di visualizzarne
 * i risultati, accedendo ai dettagli di un ristorante selezionato. Offre inoltre
 * la possibilità di accedere alla schermata di login.
 *
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 */
public class GuestGUI extends TemplateGUI {

    /**
     * Costruisce la schermata principale per gli utenti ospiti, collegando il
     * pannello di ricerca dei ristoranti al pannello dei risultati e predisponendo
     * l'accesso alla schermata di login.
     *
     * @param frame la finestra principale dell'applicazione su cui viene mostrata la GUI
     * @param utenteService il servizio utilizzato per la gestione delle operazioni sugli utenti
     */
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
            frame.setContentPane(new DettagliRistoranteGUI(frame, utenteService, null, ristoranteSelezionato));
            frame.revalidate();
            frame.repaint();
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
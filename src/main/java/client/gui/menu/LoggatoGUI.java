package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.utils.DettagliRistoranteGUI;
import main.java.client.gui.utils.*;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.*;

import javax.swing.*;
import java.awt.*;

/**
 * GUI principale mostrata all'utente autenticato non ristoratore, che permette
 * di cercare ristoranti e visualizzarne i risultati, accedendo ai dettagli di
 * un ristorante selezionato. Offre inoltre i pulsanti per visualizzare il profilo
 * e per effettuare il logout.
 *
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 */
public class LoggatoGUI extends TemplateGUI {

    /**
     * Costruisce la schermata principale per l'utente loggato, collegando i pulsanti
     * per la visualizzazione del profilo e il logout, e predisponendo il pannello
     * di ricerca dei ristoranti collegato al pannello dei risultati.
     *
     * @param frame la finestra principale dell'applicazione su cui viene mostrata la GUI
     * @param utenteService il servizio utilizzato per la gestione delle operazioni sugli utenti
     * @param email l'email dell'utente attualmente autenticato
     */
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
            frame.setContentPane(new DettagliRistoranteGUI(frame, utenteService, email, ristoranteSelezionato));
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

        // Eventuali pulsanti extra solo per il cliente loggato:
        // es. "Le mie prenotazioni", "I miei preferiti", "I miei ordini"
    }
}
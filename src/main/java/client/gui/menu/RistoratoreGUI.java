package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.azioniRistoratore.*;
import main.java.client.gui.listeRistoratore.ListaRecensioniGUI;
import main.java.client.gui.utils.PannelloRistorantiRistoratore;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.*;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Catelli Elena, Pellegrini Gaia, Tancredi Giacomo, Rizzi Camilla
 * @version 1.1
 *
 * Schermata principale mostrata a un utente ristoratore autenticato.
 * <p>
 * Estende {@link TemplateGUI} mostrando l'elenco dei ristoranti del
 * ristoratore ({@link PannelloRistorantiRistoratore}) e i pulsanti per
 * aggiungere, modificare, eliminare un ristorante o visualizzarne le
 * recensioni. Abilita inoltre il pulsante "Logout" (nascosto di default
 * nel template).
 * </p>
 */
public class RistoratoreGUI extends TemplateGUI {

    /**
     * Pannello che mostra l'elenco dei ristoranti appartenenti al ristoratore.
     */
    private PannelloRistorantiRistoratore pannelloRistoranti;

    /**
     * Costruisce la schermata per il ristoratore autenticato, inizializzando
     * il pannello con i suoi ristoranti e i pulsanti per aggiungere, modificare,
     * eliminare un ristorante o visualizzarne le recensioni. Collega inoltre
     * i pulsanti "Profilo" e "Logout" ereditati dal template.
     *
     * @param frame la finestra principale dell'applicazione
     * @param utenteService il service utilizzato per le operazioni sugli utenti
     * @param email l'email del ristoratore attualmente autenticato
     */
    public RistoratoreGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;

        visualizzaProfilo.setVisible(true);
        logout.setVisible(true);
        logout.addActionListener(e -> {
            Richiesta richiesta = new Richiesta(TipoRichieste.LOGOUT, email);
            ClientConnection.inviaRichiesta(richiesta);

            frame.setContentPane(new LoginGUI(frame, utenteService));
            frame.revalidate();
            frame.repaint();
        });

        //Pannello per visualizzare i ristoranti del ristoratore
        //doppio click su un ristorante --> stessa azione del bottone modifica dati
        pannelloRistoranti = new PannelloRistorantiRistoratore(ristorante -> eseguiModificaRistorante(utenteService, ristorante, email));
        this.add(pannelloRistoranti, BorderLayout.CENTER);
        caricaRistoranti(email);

        //Creazione bottoni sotto il pannello
        JPanel pannelloBottoni = new JPanel();
        JButton aggiungiRistorante = new JButton("Aggiungi ristorante");
        aggiungiRistorante.addActionListener(e -> {
            frame.setContentPane(new AggiungiRistoranteGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });
        pannelloBottoni.add(aggiungiRistorante);

        JButton modificaRistorante = new JButton("Modifica dati");
        modificaRistorante.addActionListener(e -> {
            RistoranteDTO selezionato = pannelloRistoranti.getRistoranteSelezionato();
            if(selezionato == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona prima un ristorante dalla lista.");
                return;
            }
            eseguiModificaRistorante(utenteService, selezionato, email);
        });


        pannelloBottoni.add(modificaRistorante);

        JButton visualizzaRecensioniRistorante = new JButton("Visualizza recensioni");
        visualizzaRecensioniRistorante.addActionListener(e -> {
            RistoranteDTO selezionato = pannelloRistoranti.getRistoranteSelezionato();
            if(selezionato == null){
                JOptionPane.showMessageDialog(frame, "Seleziona prima un ristorante dalla lista.");
                return;
            }
            frame.setContentPane(new ListaRecensioniGUI(frame, utenteService, email, selezionato));
            frame.revalidate();
            frame.repaint();
        });

        pannelloBottoni.add(visualizzaRecensioniRistorante);

        JButton eliminaRistorante = new JButton("Elimina");
        eliminaRistorante.addActionListener(e -> {
            RistoranteDTO selezionato = pannelloRistoranti.getRistoranteSelezionato();
            if (selezionato == null){
                JOptionPane.showMessageDialog(frame, "Seleziona prima un ristorante.");
                return; //Evita la NullPointerException sotto
            }
            int conferma = JOptionPane.showConfirmDialog(frame, "Eliminare \"" + selezionato.getNomeRistorante() + "\"? L'operazione è irreversibile!",
                    "Conferma eliminazione", JOptionPane.YES_NO_OPTION);

            if (conferma == JOptionPane.YES_OPTION) {
                Richiesta richiesta = new Richiesta(TipoRichieste.ELIMINA_RISTORANTE, selezionato);
                Risposta risposta = (Risposta) ClientConnection.inviaRichiesta(richiesta);
                JOptionPane.showMessageDialog(frame, risposta.getMsg());
                if(risposta.getSuccesso()){
                    caricaRistoranti(email);
                }
            }
        });
        pannelloBottoni.add(eliminaRistorante);

        this.add(pannelloBottoni, BorderLayout.SOUTH);

        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });

    }

    /**
     * Apre la schermata di modifica di un ristorante selezionato.
     *
     * @param utenteService il service utilizzato per le operazioni sugli utenti
     * @param ristorante il {@link RistoranteDTO} del ristorante da modificare
     * @param email l'email del ristoratore proprietario
     */
    private void eseguiModificaRistorante(UtenteService utenteService, RistoranteDTO ristorante, String email) {
        frame.setContentPane(new ModificaRistoranteGUI(frame, utenteService, email, ristorante));
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Richiede al server l'elenco dei ristoranti appartenenti al ristoratore
     * e aggiorna il pannello dei risultati con i dati ricevuti.
     *
     * @param email l'email del ristoratore di cui caricare i ristoranti
     */
    private void caricaRistoranti(String email){
        Richiesta richiesta = new Richiesta(TipoRichieste.GET_RISTORANTI_BYEMAIL, email);
        Risposta risposta = (Risposta) ClientConnection.inviaRichiesta(richiesta);

        if(risposta.getSuccesso()){
            List<RistoranteDTO> ristoranti = (List<RistoranteDTO>) risposta.getContenuto();
            pannelloRistoranti.aggiornaRisultati(ristoranti);
        } else{
            pannelloRistoranti.aggiornaRisultati(null);
        }
    }
}

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

public class RistoratoreGUI extends TemplateGUI {

    private PannelloRistorantiRistoratore pannelloRistoranti;

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
       // pannelloRistoranti = new PannelloRistorantiRistoratore(ristorante -> eseguiModificaRistorante(utenteService, ristorante));
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
        /*modificaRistorante.addActionListener(e -> {
            RistoranteDTO selezionato = pannelloRistoranti.getRistoranteSelezionato();
            if(selezionato == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona prima un ristorante dalla lista.");
                return;
            }
            eseguiModificaRistorante(utenteService, selezionato);
        });

         */
        pannelloBottoni.add(modificaRistorante);

        JButton visualizzaRecensioniRistorante = new JButton("Visualizza recensioni");
        visualizzaRecensioniRistorante.addActionListener(e -> {
            RistoranteDTO selezionato = pannelloRistoranti.getRistoranteSelezionato();
            if(selezionato == null){
                JOptionPane.showMessageDialog(frame, "Seleziona prima un ristorante dalla lista.");
                return;
            }
            frame.setContentPane(new ListaRecensioniGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });
        pannelloBottoni.add(visualizzaRecensioniRistorante);

        JButton eliminaRistorante = new JButton("Elimina");
        eliminaRistorante.addActionListener(e -> {
            RistoranteDTO selezionato = pannelloRistoranti.getRistoranteSelezionato();
            if (selezionato == null){
                JOptionPane.showMessageDialog(frame, "Seleziona prima un ristorante.");
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
    //metodo dell'azione condivisa tra il bottone "modifica dati" e doppio click sulla lista
    /*private void eseguiModificaRistorante(UtenteService utenteService, RistoranteDTO ristorante) {
        frame.setContentPane(new ModificaRistoranteGUI(frame, utenteService, ristorante));
        frame.revalidate();
        frame.repaint();
    }

     */

    //metodo per caricare i ristoranti
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

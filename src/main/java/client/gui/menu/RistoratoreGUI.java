package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.azioniRistoratore.AggiungiRistoranteGUI;
import main.java.client.gui.azioniRistoratore.ModificaRistoranteGUI;
import main.java.client.gui.listeRistoratore.ListaRecensioniGUI;
import main.java.server.service.UtenteService;

import javax.swing.*;
import java.awt.*;

public class RistoratoreGUI extends TemplateGUI {

    public RistoratoreGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;

        //TODO creare pannello per visualizzare i propri ristoranti

        //Creazione bottoni da poi inserire sotto il pannello
        JPanel pannelloCentrale = new JPanel();
        JButton aggiungiRistorante = new JButton("Aggiungi ristorante");
        aggiungiRistorante.addActionListener(e -> {
            frame.setContentPane(new AggiungiRistoranteGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });
        pannelloCentrale.add(aggiungiRistorante);

        JButton modificaRistorante = new JButton("Modifica dati");
        modificaRistorante.addActionListener(e -> {
            frame.setContentPane(new ModificaRistoranteGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });
        pannelloCentrale.add(modificaRistorante);

        JButton visualizzaRecensioniRistorante = new JButton("Visualizza recensioni");
        visualizzaRecensioniRistorante.addActionListener(e -> {
            frame.setContentPane(new ListaRecensioniGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });
        pannelloCentrale.add(visualizzaRecensioniRistorante);

        JButton eliminaRistorante = new JButton("Elimina");
        //TODO dargli la funzionalità di eliminare il ristorante

        pannelloCentrale.add(eliminaRistorante);

        this.add(pannelloCentrale, BorderLayout.SOUTH);

        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new VisualizzaProfiloGUI(frame, utenteService, email));
            frame.revalidate();
            frame.repaint();
        });



    }
}

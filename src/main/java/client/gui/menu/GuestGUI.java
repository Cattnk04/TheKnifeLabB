package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.client.gui.autenticazione.LoginGUI;
import main.java.server.service.UtenteService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.EventListener;

public class GuestGUI extends TemplateGUI {
    private JTextField campoNomeRitstorante;

    /*
    private JPanel creaPannelloRicerca(){
        JPanel pannelloRicerca = new JPanel(new GridLayout());
        pannelloRicerca.setBorder(BorderFactory.createTitledBorder("Cerca il ristorante:")); //titolo nel bordo del pannello
        BorderFactory.createLineBorder(Color.BLUE);
        BorderFactory.createEtchedBorder(); //bordo con effetto incassato
        GridBagConstraints vincoloGriglia = new GridBagConstraints();
        vincoloGriglia.insets = new Insets(10, 10, 10, 10);
        vincoloGriglia.anchor = GridBagConstraints.WEST;
        vincoloGriglia.fill = GridBagConstraints.HORIZONTAL;

        //popolamento righe e colonne
        //riga 0    nome ristorante: ....
        vincoloGriglia.gridx = 0;
        vincoloGriglia.gridy = 0;
        vincoloGriglia.weightx = 0; //la colonna non si allarga
        pannello.add(new JLabel("inserisci il nome del ristorante: "), vincoloGriglia);
        vincoloGriglia.gridx = 1;
        vincoloGriglia.weightx = 1;
        this.campoNomeRitstorante = new JTextField(20);
        pannello.add(campoNomeRitstorante, vincoloGriglia);

        //

    }

     */

    public GuestGUI(JFrame frame, UtenteService utenteService) {
        super(frame);
        this.frame = frame;

        visualizzaProfilo.addActionListener(e -> {
            frame.setContentPane(new LoginGUI(frame,utenteService));
            frame.revalidate();
            frame.repaint();
        });





        /*
        JLabel consigliati = new JLabel("Consigliati");
        consigliati.setFocusable(false);
        consigliati.setFont(new Font("Arial", Font.BOLD, 30));
        this.add(consigliati);
         */



        /*
        Popola le nazioni all'avvio
JComboBox<String> comboNazione = new JComboBox<>();
JComboBox<String> comboCitta = new JComboBox<>();

// --- Carica nazioni dal DB ---
try {
    DTO/DAO String query = "SELECT DISTINCT nazione FROM ristoranti"; // adatta alla tua tabella
    PreparedStatement ps = connection.prepareStatement(query);
    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
        comboNazione.addItem(rs.getString("nazione"));
    }
} catch (SQLException ex) {
    ex.printStackTrace();
}

// --- Quando cambia nazione, carica le città ---
comboNazione.addActionListener(e -> {
    String nazioneScelta = (String) comboNazione.getSelectedItem();
    comboCitta.removeAllItems();

    try {
        String query = "SELECT DISTINCT citta FROM ristoranti WHERE nazione = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, nazioneScelta);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            comboCitta.addItem(rs.getString("citta"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
});

panel.add(comboNazione);
panel.add(comboCitta);
         */


    }
}

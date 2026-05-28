package main.java.client.gui.menu;

import main.java.client.gui.autenticazione.LoginGUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.EventListener;

public class GuestGUI extends JPanel {
    JFrame frame;
    Color bianco = new Color(255,255,255);
    public GuestGUI(JFrame frame) {
        this.frame = frame;
        JButton login = new JButton("Login");
        login.setFocusable(false);
        login.setBorder(new LineBorder(bianco));
        login.addActionListener(e -> {
            frame.setContentPane(new LoginGUI(frame));
            frame.revalidate();
            frame.repaint();
        });

        JButton logo = new JButton("Logo");
        logo.setFocusable(false);
        logo.setBorder(new LineBorder(bianco));

        JLabel consigliati = new JLabel("Consigliati");
        consigliati.setFocusable(false);
        consigliati.setBorder(new LineBorder(bianco));
        consigliati.setFont(new Font("Arial", Font.BOLD, 50));

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

        this.add(login);
        this.add(logo);
        this.add(consigliati);
    }
}

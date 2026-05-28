package main.java.client.main;

import main.java.client.gui.menu.RistoratoreGUI;
import main.java.client.gui.ricerca.FiltriRicercaGUI;

import javax.swing.*;

public class ClientMain {
    public static void main(String args[]) {
        JFrame frame = new JFrame("TheKnife");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setContentPane(new RistoratoreGUI(frame));
        frame.setVisible(true);
    }
}

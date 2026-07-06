package main.java.client.gui.menu;

import main.java.client.gui.TemplateGUI;
import main.java.server.service.UtenteService;

import javax.swing.*;
import java.awt.*;

public class RistoratoreGUI extends TemplateGUI {

    public RistoratoreGUI(JFrame frame, UtenteService utenteService, String email) {
        super(frame);
        this.frame = frame;
    }
}

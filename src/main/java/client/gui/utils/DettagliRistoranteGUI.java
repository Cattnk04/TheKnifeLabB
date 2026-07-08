package main.java.client.gui.utils;

import main.java.client.gui.azioniLoggato.VisualizzaProfiloGUI;
import main.java.client.gui.TemplateGUI;
import main.java.client.network.ClientConnection;
import main.java.server.service.UtenteService;
import main.java.shared.communication.Richiesta;
import main.java.shared.communication.Risposta;
import main.java.shared.communication.TipoRichieste;
import main.java.shared.domain.Recensione;
import main.java.shared.dto.RecensioneDTO;
import main.java.shared.dto.RegistrazioneDTO;
import main.java.shared.dto.RistoranteDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/*
 * GUI di dettaglio di un ristorante.
 * Mostra i dati del ristorante e, sotto, un pannello di azioni che varia in base
 * allo stato dell'utente:
 *  - ospite (email == null)              -> nessuna azione, solo invito al login
 *  - utente cliente senza recensione     -> bottone "Scrivi una recensione"
 *  - utente cliente con recensione già scritta -> messaggio informativo
 *  - utente ristoratore                  -> nessuna azione recensione (la GUI
 *                                            è condivisa anche per il ristoratore,
 *                                            che vede solo i dati del locale)
 *
 */
public class DettagliRistoranteGUI {

}

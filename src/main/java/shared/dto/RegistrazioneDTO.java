package main.java.shared.dto;

public class RegistrazioneDTO {
    public String nome;
    public String cognome;
    public String email;
    public String password;
    public String citta;
    public String nazione;
    public boolean ristoratore;
    public RegistrazioneDTO(String nome, String cognome, String email, String password,
                            String citta, String nazione, boolean ristoratore) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.citta = citta;
        this.nazione = nazione;
        this.ristoratore = ristoratore;
    }
}

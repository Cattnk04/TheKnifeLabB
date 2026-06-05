package main.java.shared.dto;

public class RegistrazioneDTO {
    private final String nome;
    private final String cognome;
    private final String email;
    private final String password;
    private final String citta;
    private final String nazione;
    private final boolean ristoratore;
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

    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getCitta() { return citta; }
    public String getNazione() { return nazione; }
    public boolean isRistoratore() { return ristoratore; }
}

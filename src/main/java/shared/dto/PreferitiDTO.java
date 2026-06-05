package main.java.shared.dto;

public class PreferitiDTO {

    private final String email;
    private final int idRistorante;

    public PreferitiDTO(String email, int idRistorante) {
        this.email = email;
        this.idRistorante = idRistorante;
    }

    public String getEmail() {
        return email;
    }

    public int getIdRistorante() {
        return idRistorante;
    }
}

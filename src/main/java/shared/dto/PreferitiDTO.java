package main.java.shared.dto;

import java.io.Serializable;

public class PreferitiDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

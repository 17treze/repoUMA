package it.tndigitale.a4gutente.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Distributore implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String denominazioneAzienda;
    private String comune;
    private String provincia;
    private String indirizzo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataInizio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataFine;

    public Long getId() {
        return id;
    }

    public Distributore setId(Long id) {
        this.id = id;
        return this;
    }

    public String getDenominazioneAzienda() {
        return denominazioneAzienda;
    }

    public void setDenominazioneAzienda(String denominazioneAzienda) {
        this.denominazioneAzienda = denominazioneAzienda;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public LocalDateTime getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }
}

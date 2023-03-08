package it.tndigitale.a4gistruttoria.dto;

import java.util.Date;

public class Alpeggio {
    private Long id;
    private Date dtFine;
    private Date dtInizio;
    private String cuaa;
    private Integer campagna;

    public Long getId() {
        return id;
    }

    public Alpeggio setId(Long id) {
        this.id = id;
        return this;
    }

    public Date getDtFine() {
        return dtFine;
    }

    public Alpeggio setDtFine(Date dtFine) {
        this.dtFine = dtFine;
        return this;
    }

    public Date getDtInizio() {
        return dtInizio;
    }

    public Alpeggio setDtInizio(Date dtInizio) {
        this.dtInizio = dtInizio;
        return this;
    }

    public String getCuaa() {
        return cuaa;
    }

    public Alpeggio setCuaa(String cuaa) {
        this.cuaa = cuaa;
        return this;
    }

    public Integer getCampagna() {
        return campagna;
    }

    public Alpeggio setCampagna(Integer campagna) {
        this.campagna = campagna;
        return this;
    }
}

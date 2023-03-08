package it.tndigitale.a4gistruttoria.dto;

public class DomandeCounterFilter {

    private String identificativoStatoLavSostegno;
    private String identificativoLavSostegno;
    private Integer campagna;

    public String getIdentificativoStatoLavSostegno() {
        return identificativoStatoLavSostegno;
    }

    public String getIdentificativoLavSostegno() {
        return identificativoLavSostegno;
    }

    public void setIdentificativoStatoLavSostegno(String identificativoStatoLavSostegno) {
        this.identificativoStatoLavSostegno = identificativoStatoLavSostegno;
    }

    public void setIdentificativoLavSostegno(String identificativoLavSostegno) {
        this.identificativoLavSostegno = identificativoLavSostegno;
    }

    public Integer getCampagna() {
        return campagna;
    }

    public DomandeCounterFilter setCampagna(Integer campagna) {
        this.campagna = campagna;
        return this;
    }
}

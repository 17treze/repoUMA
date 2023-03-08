package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.List;

public class DatiDomandaRicerca {

    private BigDecimal numeroDomanda;
    private String cuaa;
    private String companyDescription;
    private int year;
    private String state;
    private List<RelateModel> relates;

    public BigDecimal getNumeroDomanda() {
        return numeroDomanda;
    }

    public void setNumeroDomanda(BigDecimal numeroDomanda) {
        this.numeroDomanda = numeroDomanda;
    }

    public String getCuaa() {
        return cuaa;
    }

    public void setCuaa(String cuaa) {
        this.cuaa = cuaa;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<RelateModel> getRelates() {
        return relates;
    }

    public void setRelates(List<RelateModel> relates) {
        this.relates = relates;
    }
}

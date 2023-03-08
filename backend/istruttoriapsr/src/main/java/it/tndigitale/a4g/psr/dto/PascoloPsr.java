package it.tndigitale.a4g.psr.dto;

public class PascoloPsr {

    private Long idDomanda;
    private String cuaa;
    private String ragioneSociale;
    private String codPascolo;
    private String denominazione;
    private Long uba;

    public Long getIdDomanda() {
        return idDomanda;
    }

    public void setIdDomanda(Long idDomanda) {
        this.idDomanda = idDomanda;
    }

    public String getCuaa() {
        return cuaa;
    }

    public void setCuaa(String cuaa) {
        this.cuaa = cuaa;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getCodPascolo() {
        return codPascolo;
    }

    public void setCodPascolo(String codPascolo) {
        this.codPascolo = codPascolo;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public Long getUba() {
        return uba;
    }

    public void setUba(Long uba) {
        this.uba = uba;
    }
}

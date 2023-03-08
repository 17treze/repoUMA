package it.tndigitale.a4g.srt.dto;

import java.util.Date;

public class ImportoRichiesto {
    private String cuaa;
    private String misuraPSR;
    private int idDomanda;
    private Date dataDomanda;
    private float importoRichiesto;

    public String getCuaa() {
        return cuaa;
    }
    public void setCuaa(String cuaa) {
        this.cuaa = cuaa;
    }


    public String getMisuraPSR() {
        return misuraPSR;
    }

    public void setMisuraPSR(String misuraPSR) {
        this.misuraPSR = misuraPSR;
    }

    public int getIdDomanda() {
        return idDomanda;
    }

    public void setIdDomanda(int idDomanda) {
        this.idDomanda = idDomanda;
    }

    public Date getDataDomanda() {
        return dataDomanda;
    }

    public void setDataDomanda(Date dataDomanda) {
        this.dataDomanda = dataDomanda;
    }

    public float getImportoRichiesto() {
        return importoRichiesto;
    }

    public void setImportoRichiesto(float importoRichiesto) {
        this.importoRichiesto = importoRichiesto;
    }
}

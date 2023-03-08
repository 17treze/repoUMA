package it.tndigitale.a4g.psr.dto;

import java.util.LinkedList;
import java.util.List;

public class DettaglioPagamentoPsr11 implements DettaglioPagamentoPsr {
    private List<DettaglioSostegnoMantenimentoPsr> sostegniMantenimentoPsr = new LinkedList<>();
    private EsitoFinaleDettaglioPagamentoPsr11 esitoFinaleDettaglioPagamentoPsr;

    public List<DettaglioSostegnoMantenimentoPsr> getSostegniMantenimentoPsr() {
        return sostegniMantenimentoPsr;
    }

    public void setSostegniMantenimentoPsr(List<DettaglioSostegnoMantenimentoPsr> sostegniMantenimentoPsr) {
        this.sostegniMantenimentoPsr = sostegniMantenimentoPsr;
    }

    public void addSostegnoMantenimentoPsrs(DettaglioSostegnoMantenimentoPsr sostegnoMantenimentoPsr) {
        this.sostegniMantenimentoPsr.add(sostegnoMantenimentoPsr);
    }

    public EsitoFinaleDettaglioPagamentoPsr11 getEsitoFinaleDettaglioPagamentoPsr() {
        return esitoFinaleDettaglioPagamentoPsr;
    }

    public void setEsitoFinaleDettaglioPagamentoPsr(EsitoFinaleDettaglioPagamentoPsr11 esitoFinaleDettaglioPagamentoPsr) {
        this.esitoFinaleDettaglioPagamentoPsr = esitoFinaleDettaglioPagamentoPsr;
    }


}

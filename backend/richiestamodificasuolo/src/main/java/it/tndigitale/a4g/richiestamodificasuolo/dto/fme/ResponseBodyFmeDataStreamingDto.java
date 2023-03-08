package it.tndigitale.a4g.richiestamodificasuolo.dto.fme;

import java.io.Serializable;

public class ResponseBodyFmeDataStreamingDto implements Serializable {

    private static final long serialVersionUID = 7036404154534042342L;

    private Double area_suolo_sum;
    private Double area_dentro_upas_sum;
    private Double area_dentro_upas_max;
    private boolean lavorazione_in_upas;

    public Double getArea_suolo_sum() {
        return area_suolo_sum;
    }

    public void setArea_suolo_sum(Double area_suolo_sum) {
        this.area_suolo_sum = area_suolo_sum;
    }

    public Double getArea_dentro_upas_sum() {
        return area_dentro_upas_sum;
    }

    public void setArea_dentro_upas_sum(Double area_dentro_upas_sum) {
        this.area_dentro_upas_sum = area_dentro_upas_sum;
    }

    public Double getArea_dentro_upas_max() {
        return area_dentro_upas_max;
    }

    public void setArea_dentro_upas_max(Double area_dentro_upas_max) {
        this.area_dentro_upas_max = area_dentro_upas_max;
    }

    public boolean isLavorazione_in_upas() {
        return lavorazione_in_upas;
    }

    public void setLavorazione_in_upas(boolean lavorazione_in_upas) {
        this.lavorazione_in_upas = lavorazione_in_upas;
    }
}

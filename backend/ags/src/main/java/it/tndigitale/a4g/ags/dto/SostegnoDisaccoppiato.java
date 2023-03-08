package it.tndigitale.a4g.ags.dto;

import java.util.Objects;

public class SostegnoDisaccoppiato {

    private Boolean bpsRichiesto = Boolean.TRUE;
    private Boolean greeningRichiesto = Boolean.TRUE;
    private Boolean giovaneRichiesto;
    private Long superficieImpegnataLorda;
    private Float superficieImpegnataNetta;


    public Boolean getBpsRichiesto() {
        return bpsRichiesto;
    }

    public SostegnoDisaccoppiato setBpsRichiesto(Boolean bpsRichiesto) {
        this.bpsRichiesto = bpsRichiesto;
        return this;
    }

    public Boolean getGreeningRichiesto() {
        return greeningRichiesto;
    }

    public SostegnoDisaccoppiato setGreeningRichiesto(Boolean greeningRichiesto) {
        this.greeningRichiesto = greeningRichiesto;
        return this;
    }

    public Boolean getGiovaneRichiesto() {
        return giovaneRichiesto;
    }

    public SostegnoDisaccoppiato setGiovaneRichiesto(Boolean giovaneRichiesto) {
        this.giovaneRichiesto = giovaneRichiesto;
        return this;
    }

    public Long getSuperficieImpegnataLorda() {
        return superficieImpegnataLorda;
    }

    public SostegnoDisaccoppiato setSuperficieImpegnataLorda(Long superficieImpegnataLorda) {
        this.superficieImpegnataLorda = superficieImpegnataLorda;
        return this;
    }

    public Float getSuperficieImpegnataNetta() {
        return superficieImpegnataNetta;
    }

    public SostegnoDisaccoppiato setSuperficieImpegnataNetta(Float superficieImpegnataNetta) {
        this.superficieImpegnataNetta = superficieImpegnataNetta;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SostegnoDisaccoppiato that = (SostegnoDisaccoppiato) o;
        return Objects.equals(bpsRichiesto, that.bpsRichiesto) &&
                Objects.equals(greeningRichiesto, that.greeningRichiesto) &&
                Objects.equals(giovaneRichiesto, that.giovaneRichiesto) &&
                Objects.equals(superficieImpegnataLorda, that.superficieImpegnataLorda) &&
                Objects.equals(superficieImpegnataNetta, that.superficieImpegnataNetta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bpsRichiesto, greeningRichiesto, giovaneRichiesto, superficieImpegnataLorda, superficieImpegnataNetta);
    }

}

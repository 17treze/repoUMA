package it.tndigitale.a4g.psr.dto;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class ImpegnoRichiestoPSRSuperficie {

    private Long idDomanda;
    private Long idModulo;
    private LocalDate dataPres;
    private Long grafica;
    private String codDestinazione;
    private String dsDestinazione;
    private Long supImpegno;
    private Long supImpegnoNetta;
    private UbaAlpeggiatePsr ubaAlpeggiatePsr;
    private List<PascoloPsr> pascoliPsr = new LinkedList<>();

    public ImpegnoRichiestoPSRSuperficie() {
    }

    public ImpegnoRichiestoPSRSuperficie(Long idDomanda, Long idModulo, LocalDate dataPres, Long grafica,
                                         String codDestinazione, String dsDestinazione, Long supImpegno,
                                         Long supImpegnoNetta) {
        this.idDomanda = idDomanda;
        this.idModulo = idModulo;
        this.dataPres = dataPres;
        this.grafica = grafica;
        this.codDestinazione = codDestinazione;
        this.dsDestinazione = dsDestinazione;
        this.supImpegno = supImpegno;
        this.supImpegnoNetta = supImpegnoNetta;
    }

    public Long getIdDomanda() {
        return idDomanda;
    }

    public void setIdDomanda(Long idDomanda) {
        this.idDomanda = idDomanda;
    }

    public Long getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Long idModulo) {
        this.idModulo = idModulo;
    }

    public LocalDate getDataPres() {
        return dataPres;
    }

    public void setDataPres(LocalDate dataPres) {
        this.dataPres = dataPres;
    }

    public Long getGrafica() {
        return grafica;
    }

    public void setGrafica(Long grafica) {
        this.grafica = grafica;
    }

    public String getCodDestinazione() {
        return codDestinazione;
    }

    public void setCodDestinazione(String codDestinazione) {
        this.codDestinazione = codDestinazione;
    }

    public String getDsDestinazione() {
        return dsDestinazione;
    }

    public void setDsDestinazione(String dsDestinazione) {
        this.dsDestinazione = dsDestinazione;
    }

    public Long getSupImpegno() {
        return supImpegno;
    }

    public void setSupImpegno(Long supImpegno) {
        this.supImpegno = supImpegno;
    }

    public Long getSupImpegnoNetta() {
        return supImpegnoNetta;
    }

    public void setSupImpegnoNetta(Long supImpegnoNetta) {
        this.supImpegnoNetta = supImpegnoNetta;
    }

    public UbaAlpeggiatePsr getUbaAlpeggiatePsr() {
        return ubaAlpeggiatePsr;
    }

    public void setUbaAlpeggiatePsr(UbaAlpeggiatePsr ubaAlpeggiatePsr) {
        this.ubaAlpeggiatePsr = ubaAlpeggiatePsr;
    }

    public List<PascoloPsr> getPascoliPsr() {
        return pascoliPsr;
    }

    public void setPascoliPsr(List<PascoloPsr> pascoloPsrs) {
        this.pascoliPsr = pascoloPsrs;
    }

    public void addPascoliPsr(List<PascoloPsr> pascoloPsrs) {
        this.pascoliPsr.addAll(pascoloPsrs);
    }

}

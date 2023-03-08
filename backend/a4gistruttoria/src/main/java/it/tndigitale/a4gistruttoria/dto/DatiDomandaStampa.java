package it.tndigitale.a4gistruttoria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(Include.NON_EMPTY)
public class DatiDomandaStampa {

    private String tipoIstruttoria;
    private String modulo;
    private String annoCampagna;
    private String numeroDomanda;
    private String cuaa;
    private String denominazione;
    private String enteCompilatore;
    private String dtPresentazione;
    private String dataPresentazione;
    private String aggiornamentoFascicolo;
    private String archiviazioneDomanda;
    private String firmaDomanda;
    private InfoLiquidazioneDomandaStampa infoLiquidazione;
    private List<PassoLavorazioneStampa> passoLavStampa;
    private LocalDate data;


    public DatiDomandaStampa() {
    }

    public DatiDomandaStampa(DatiDomandaStampa datiDomandaStampa) {
        this.modulo = datiDomandaStampa.getModulo();
        this.numeroDomanda = datiDomandaStampa.getNumeroDomanda();
        this.enteCompilatore = datiDomandaStampa.getEnteCompilatore();
        this.dtPresentazione = datiDomandaStampa.getDtPresentazione();
        this.cuaa = datiDomandaStampa.getCuaa();
        this.denominazione = datiDomandaStampa.getDenominazione();
        this.passoLavStampa = datiDomandaStampa.getPassoLavStampa();
        this.annoCampagna = datiDomandaStampa.getAnnoCampagna();
        this.tipoIstruttoria = datiDomandaStampa.getTipoIstruttoria();
        this.data = datiDomandaStampa.getData();
        this.infoLiquidazione = datiDomandaStampa.getInfoLiquidazione();
        this.dataPresentazione = datiDomandaStampa.getDataPresentazione();
        this.aggiornamentoFascicolo = datiDomandaStampa.getAggiornamentoFascicolo();
        this.archiviazioneDomanda = datiDomandaStampa.getArchiviazioneDomanda();
        this.firmaDomanda = datiDomandaStampa.getFirmaDomanda();
    }

    public String getTipoIstruttoria() {
        return tipoIstruttoria;
    }

    public DatiDomandaStampa setTipoIstruttoria(String tipoIstruttoria) {
        this.tipoIstruttoria = tipoIstruttoria;
        return this;
    }

    public String getModulo() {
        return modulo;
    }

    public DatiDomandaStampa setModulo(String modulo) {
        this.modulo = modulo;
        return this;
    }

    public String getAnnoCampagna() {
        return annoCampagna;
    }

    public DatiDomandaStampa setAnnoCampagna(String annoCampagna) {
        this.annoCampagna = annoCampagna;
        return this;
    }

    public String getNumeroDomanda() {
        return numeroDomanda;
    }

    public DatiDomandaStampa setNumeroDomanda(String numeroDomanda) {
        this.numeroDomanda = numeroDomanda;
        return this;
    }

    public String getCuaa() {
        return cuaa;
    }

    public DatiDomandaStampa setCuaa(String cuaa) {
        this.cuaa = cuaa;
        return this;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public DatiDomandaStampa setDenominazione(String denominazione) {
        this.denominazione = denominazione;
        return this;
    }

    public String getEnteCompilatore() {
        return enteCompilatore;
    }

    public DatiDomandaStampa setEnteCompilatore(String enteCompilatore) {
        this.enteCompilatore = enteCompilatore;
        return this;
    }

    public String getDtPresentazione() {
        return dtPresentazione;
    }

    public DatiDomandaStampa setDtPresentazione(String dtPresentazione) {
        this.dtPresentazione = dtPresentazione;
        return this;
    }

    public String getDataPresentazione() {
        return dataPresentazione;
    }

    public DatiDomandaStampa setDataPresentazione(String dataPresentazione) {
        this.dataPresentazione = dataPresentazione;
        return this;
    }

    public String getAggiornamentoFascicolo() {
        return aggiornamentoFascicolo;
    }

    public DatiDomandaStampa setAggiornamentoFascicolo(String aggiornamentoFascicolo) {
        this.aggiornamentoFascicolo = aggiornamentoFascicolo;
        return this;
    }

    public String getArchiviazioneDomanda() {
        return archiviazioneDomanda;
    }

    public DatiDomandaStampa setArchiviazioneDomanda(String archiviazioneDomanda) {
        this.archiviazioneDomanda = archiviazioneDomanda;
        return this;
    }

    public String getFirmaDomanda() {
        return firmaDomanda;
    }

    public DatiDomandaStampa setFirmaDomanda(String firmaDomanda) {
        this.firmaDomanda = firmaDomanda;
        return this;
    }

    public InfoLiquidazioneDomandaStampa getInfoLiquidazione() {
        return infoLiquidazione;
    }

    public DatiDomandaStampa setInfoLiquidazione(InfoLiquidazioneDomandaStampa infoLiquidazione) {
        this.infoLiquidazione = infoLiquidazione;
        return this;
    }

    public List<PassoLavorazioneStampa> getPassoLavStampa() {
        return passoLavStampa;
    }

    public DatiDomandaStampa setPassoLavStampa(List<PassoLavorazioneStampa> passoLavStampa) {
        this.passoLavStampa = passoLavStampa;
        return this;
    }

    public LocalDate getData() {
        return data;
    }

    public DatiDomandaStampa setData(LocalDate data) {
        this.data = data;
        return this;
    }
}

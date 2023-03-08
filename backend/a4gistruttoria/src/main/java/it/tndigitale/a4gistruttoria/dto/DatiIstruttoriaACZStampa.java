package it.tndigitale.a4gistruttoria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonInclude(Include.NON_EMPTY)
public class DatiIstruttoriaACZStampa extends DatiDomandaStampa {

    private String aczUbaLat;
    private String aczUbaMac;
    private String aczUbaOvi;
    private String aczUbaTot;
    private String agratt;
    private String azcmpbov;
    private String azcmpovi;
    private String percrit;
    private List<PassoLavorazioneStampa> disciplinaFinanziaria;
    private String validitaIBAN;
    private String titolareDeceduto;
    private String listaNeraAGEA;
    private String importoMinimoLiquidabile300;
    private String certificazioneAntimafia;
    private String importoMinimoAntimafia;

    public DatiIstruttoriaACZStampa(DatiDomandaStampa datiDomandaStampa) {
        super(datiDomandaStampa);
    }

    public String getAczUbaLat() {
        return aczUbaLat;
    }

    public DatiIstruttoriaACZStampa setAczUbaLat(String aczUbaLat) {
        this.aczUbaLat = aczUbaLat;
        return this;
    }

    public String getAczUbaMac() {
        return aczUbaMac;
    }

    public DatiIstruttoriaACZStampa setAczUbaMac(String aczUbaMac) {
        this.aczUbaMac = aczUbaMac;
        return this;
    }

    public String getAczUbaOvi() {
        return aczUbaOvi;
    }

    public DatiIstruttoriaACZStampa setAczUbaOvi(String aczUbaOvi) {
        this.aczUbaOvi = aczUbaOvi;
        return this;
    }

    public String getAczUbaTot() {
        return aczUbaTot;
    }

    public DatiIstruttoriaACZStampa setAczUbaTot(String aczUbaTot) {
        this.aczUbaTot = aczUbaTot;
        return this;
    }

    public String getAgratt() {
        return agratt;
    }

    public DatiIstruttoriaACZStampa setAgratt(String agratt) {
        this.agratt = agratt;
        return this;
    }

    public String getAzcmpbov() {
        return azcmpbov;
    }

    public DatiIstruttoriaACZStampa setAzcmpbov(String azcmpbov) {
        this.azcmpbov = azcmpbov;
        return this;
    }

    public String getAzcmpovi() {
        return azcmpovi;
    }

    public DatiIstruttoriaACZStampa setAzcmpovi(String azcmpovi) {
        this.azcmpovi = azcmpovi;
        return this;
    }

    public String getPercrit() {
        return percrit;
    }

    public DatiIstruttoriaACZStampa setPercrit(String percrit) {
        this.percrit = percrit;
        return this;
    }

    public List<PassoLavorazioneStampa> getDisciplinaFinanziaria() {
        return disciplinaFinanziaria;
    }

    public DatiIstruttoriaACZStampa setDisciplinaFinanziaria(List<PassoLavorazioneStampa> disciplinaFinanziaria) {
        this.disciplinaFinanziaria = disciplinaFinanziaria;
        return this;
    }

    public String getValiditaIBAN() {
        return validitaIBAN;
    }

    public DatiIstruttoriaACZStampa setValiditaIBAN(String validitaIBAN) {
        this.validitaIBAN = validitaIBAN;
        return this;
    }

    public String getTitolareDeceduto() {
        return titolareDeceduto;
    }

    public DatiIstruttoriaACZStampa setTitolareDeceduto(String titolareDeceduto) {
        this.titolareDeceduto = titolareDeceduto;
        return this;
    }

    public String getListaNeraAGEA() {
        return listaNeraAGEA;
    }

    public DatiIstruttoriaACZStampa setListaNeraAGEA(String listaNeraAGEA) {
        this.listaNeraAGEA = listaNeraAGEA;
        return this;
    }

    public String getImportoMinimoLiquidabile300() {
        return importoMinimoLiquidabile300;
    }

    public DatiIstruttoriaACZStampa setImportoMinimoLiquidabile300(String importoMinimoLiquidabile300) {
        this.importoMinimoLiquidabile300 = importoMinimoLiquidabile300;
        return this;
    }

    public String getCertificazioneAntimafia() {
        return certificazioneAntimafia;
    }

    public DatiIstruttoriaACZStampa setCertificazioneAntimafia(String certificazioneAntimafia) {
        this.certificazioneAntimafia = certificazioneAntimafia;
        return this;
    }

    public String getImportoMinimoAntimafia() {
        return importoMinimoAntimafia;
    }

    public DatiIstruttoriaACZStampa setImportoMinimoAntimafia(String importoMinimoAntimafia) {
        this.importoMinimoAntimafia = importoMinimoAntimafia;
        return this;
    }
}

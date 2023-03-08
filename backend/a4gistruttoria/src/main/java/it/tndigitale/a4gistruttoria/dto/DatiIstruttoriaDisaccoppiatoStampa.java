package it.tndigitale.a4gistruttoria.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;

import java.util.List;

@JsonInclude(Include.NON_EMPTY)
public class DatiIstruttoriaDisaccoppiatoStampa extends DatiDomandaStampa {

    private List<PassoLavorazioneStampa> passoLavInterSostegno;
    private DatiIstruttoria datiIstruttoria;
    private String campione;
    private String accessoAllaRiserva;
    private List<EsitoControllo> esitoControlli;
    private String infoAgriAttivo;
    private String agriAttivo;
    private String impegnoTitoli;
    private String superficieMinima;
    private String importoFinale;
    private String importoRiduzioneRitardo;
    private String importoRiduzioneCapping;
    private String importoRiduzionePremioBase;
    private String importoRiduzioneGreening;
    private String importoRiduzioneGiovane;
    private String importoSanzioni;
    private String importoSanzioniPremioBase;
    private String importoSanzioniGreening;
    private String importoSanzioniGiovane;
    private String importoRiduzioni;


    public DatiIstruttoriaDisaccoppiatoStampa(DatiDomandaStampa datiDomandaStampa) {
        super(datiDomandaStampa);
    }


    public List<PassoLavorazioneStampa> getPassoLavInterSostegno() {
        return passoLavInterSostegno;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setPassoLavInterSostegno(List<PassoLavorazioneStampa> passoLavInterSostegno) {
        this.passoLavInterSostegno = passoLavInterSostegno;
        return this;
    }

    public DatiIstruttoria getDatiIstruttoria() {
        return datiIstruttoria;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setDatiIstruttoria(DatiIstruttoria datiIstruttoria) {
        this.datiIstruttoria = datiIstruttoria;
        return this;
    }

    public String getCampione() {
        return campione;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setCampione(String campione) {
        this.campione = campione;
        return this;
    }

    public String getAccessoAllaRiserva() {
        return accessoAllaRiserva;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setAccessoAllaRiserva(String accessoAllaRiserva) {
        this.accessoAllaRiserva = accessoAllaRiserva;
        return this;
    }

    public List<EsitoControllo> getEsitoControlli() {
        return esitoControlli;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setEsitoControlli(List<EsitoControllo> esitoControlli) {
        this.esitoControlli = esitoControlli;
        return this;
    }

    public String getInfoAgriAttivo() {
        return infoAgriAttivo;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setInfoAgriAttivo(String infoAgriAttivo) {
        this.infoAgriAttivo = infoAgriAttivo;
        return this;
    }

    public String getAgriAttivo() {
        return agriAttivo;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setAgriAttivo(String agriAttivo) {
        this.agriAttivo = agriAttivo;
        return this;
    }

    public String getImpegnoTitoli() {
        return impegnoTitoli;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImpegnoTitoli(String impegnoTitoli) {
        this.impegnoTitoli = impegnoTitoli;
        return this;
    }

    public String getSuperficieMinima() {
        return superficieMinima;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setSuperficieMinima(String superficieMinima) {
        this.superficieMinima = superficieMinima;
        return this;
    }

    public String getImportoFinale() {
        return importoFinale;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoFinale(String importoFinale) {
        this.importoFinale = importoFinale;
        return this;
    }

    public String getImportoRiduzioneRitardo() {
        return importoRiduzioneRitardo;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoRiduzioneRitardo(String importoRiduzioneRitardo) {
        this.importoRiduzioneRitardo = importoRiduzioneRitardo;
        return this;
    }

    public String getImportoRiduzioneCapping() {
        return importoRiduzioneCapping;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoRiduzioneCapping(String importoRiduzioneCapping) {
        this.importoRiduzioneCapping = importoRiduzioneCapping;
        return this;
    }

    public String getImportoRiduzionePremioBase() {
        return importoRiduzionePremioBase;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoRiduzionePremioBase(String importoRiduzionePremioBase) {
        this.importoRiduzionePremioBase = importoRiduzionePremioBase;
        return this;
    }

    public String getImportoRiduzioneGreening() {
        return importoRiduzioneGreening;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoRiduzioneGreening(String importoRiduzioneGreening) {
        this.importoRiduzioneGreening = importoRiduzioneGreening;
        return this;
    }

    public String getImportoRiduzioneGiovane() {
        return importoRiduzioneGiovane;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoRiduzioneGiovane(String importoRiduzioneGiovane) {
        this.importoRiduzioneGiovane = importoRiduzioneGiovane;
        return this;
    }

    public String getImportoSanzioni() {
        return importoSanzioni;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoSanzioni(String importoSanzioni) {
        this.importoSanzioni = importoSanzioni;
        return this;
    }

    public String getImportoSanzioniPremioBase() {
        return importoSanzioniPremioBase;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoSanzioniPremioBase(String importoSanzioniPremioBase) {
        this.importoSanzioniPremioBase = importoSanzioniPremioBase;
        return this;
    }

    public String getImportoSanzioniGreening() {
        return importoSanzioniGreening;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoSanzioniGreening(String importoSanzioniGreening) {
        this.importoSanzioniGreening = importoSanzioniGreening;
        return this;
    }

    public String getImportoSanzioniGiovane() {
        return importoSanzioniGiovane;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoSanzioniGiovane(String importoSanzioniGiovane) {
        this.importoSanzioniGiovane = importoSanzioniGiovane;
        return this;
    }

    public String getImportoRiduzioni() {
        return importoRiduzioni;
    }

    public DatiIstruttoriaDisaccoppiatoStampa setImportoRiduzioni(String importoRiduzioni) {
        this.importoRiduzioni = importoRiduzioni;
        return this;
    }
}

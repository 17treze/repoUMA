package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.time.LocalDateTime;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.*;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.AziendaAgricolaDto;
import org.springframework.format.annotation.DateTimeFormat;


public class SuoloDichiaratoLavorazioneDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -881257665590974429L;
    private Long id;
    private Long idRichiesta;
    private String statoRichiesta;
    private AziendaAgricolaDto aziendaAgricola;
    private Long campagna;
    private String codSezione;

    private TagDichiarato tipoSuoloDichiarato;
    private TagRilevato tipoSuoloRilevato;
    private Double areaOri;

    private String codiRileDichiarato;
    private String descRileDichiarato;
    private String codiProdRileDichiarato;
    private String descProdRileDichiarato;

    private String codiRilePrevalenteDich;
    private String descRilePrevalenteDich;

    private String codiRileRilevato;
    private String descRileRilevato;
    private String codiProdRileRilevato;
    private String descProdRileRilevato;

    private String codiRilePrevalenteRil;
    private String descRilePrevalenteRil;
    private EsitoLavorazioneDichiarato esito;

    private Long idLavorazione;
    private Long idPoligonoDichiarato;
    private Long numeroMessaggi;
    private Long numeroDocumenti;
    private Boolean visible;
    private Double[] extent;

    private String utenteLavorazione;
    private StatoLavorazioneSuolo statoLavorazione;

    private Boolean nuovaModificaBo;
    private Boolean nuovaModificaCaa;
    private Boolean visibileInOrtofoto;
    private TipoInterventoColturale tipoInterventoColturale;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime interventoInizio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime interventoFine;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdRichiesta() {
        return idRichiesta;
    }

    public void setIdRichiesta(Long idRichiesta) {
        this.idRichiesta = idRichiesta;
    }

    public String getStatoRichiesta() {
        return statoRichiesta;
    }

    public void setStatoRichiesta(String statoRichiesta) {
        this.statoRichiesta = statoRichiesta;
    }

    public AziendaAgricolaDto getAziendaAgricola() {
        return aziendaAgricola;
    }

    public void setAziendaAgricola(AziendaAgricolaDto aziendaAgricola) {
        this.aziendaAgricola = aziendaAgricola;
    }

    public Long getCampagna() {
        return campagna;
    }

    public void setCampagna(Long campagna) {
        this.campagna = campagna;
    }

    public String getCodSezione() {
        return codSezione;
    }

    public void setCodSezione(String codSezione) {
        this.codSezione = codSezione;
    }

    public TagDichiarato getTipoSuoloDichiarato() {
        return tipoSuoloDichiarato;
    }

    public void setTipoSuoloDichiarato(TagDichiarato tipoSuoloDichiarato) {
        this.tipoSuoloDichiarato = tipoSuoloDichiarato;
    }

    public TagRilevato getTipoSuoloRilevato() {
        return tipoSuoloRilevato;
    }

    public void setTipoSuoloRilevato(TagRilevato tipoSuoloRilevato) {
        this.tipoSuoloRilevato = tipoSuoloRilevato;
    }

    public Double getAreaOri() {
        return areaOri;
    }

    public void setAreaOri(Double areaOri) {
        this.areaOri = areaOri;
    }

    public String getCodiRileDichiarato() {
        return codiRileDichiarato;
    }

    public void setCodiRileDichiarato(String codiRileDichiarato) {
        this.codiRileDichiarato = codiRileDichiarato;
    }

    public String getDescRileDichiarato() {
        return descRileDichiarato;
    }

    public void setDescRileDichiarato(String descRileDichiarato) {
        this.descRileDichiarato = descRileDichiarato;
    }

    public String getCodiProdRileDichiarato() {
        return codiProdRileDichiarato;
    }

    public void setCodiProdRileDichiarato(String codiProdRileDichiarato) {
        this.codiProdRileDichiarato = codiProdRileDichiarato;
    }

    public String getDescProdRileDichiarato() {
        return descProdRileDichiarato;
    }

    public void setDescProdRileDichiarato(String descProdRileDichiarato) {
        this.descProdRileDichiarato = descProdRileDichiarato;
    }

    public String getCodiRilePrevalenteDich() {
        return codiRilePrevalenteDich;
    }

    public void setCodiRilePrevalenteDich(String codiRilePrevalenteDich) {
        this.codiRilePrevalenteDich = codiRilePrevalenteDich;
    }

    public String getDescRilePrevalenteDich() {
        return descRilePrevalenteDich;
    }

    public void setDescRilePrevalenteDich(String descRilePrevalenteDich) {
        this.descRilePrevalenteDich = descRilePrevalenteDich;
    }

    public String getCodiRileRilevato() {
        return codiRileRilevato;
    }

    public void setCodiRileRilevato(String codiRileRilevato) {
        this.codiRileRilevato = codiRileRilevato;
    }

    public String getDescRileRilevato() {
        return descRileRilevato;
    }

    public void setDescRileRilevato(String descRileRilevato) {
        this.descRileRilevato = descRileRilevato;
    }

    public String getCodiProdRileRilevato() {
        return codiProdRileRilevato;
    }

    public void setCodiProdRileRilevato(String codProdRileRilevato) {
        this.codiProdRileRilevato = codProdRileRilevato;
    }

    public String getDescProdRileRilevato() {
        return descProdRileRilevato;
    }

    public void setDescProdRileRilevato(String descProdRileRilevato) {
        this.descProdRileRilevato = descProdRileRilevato;
    }

    public String getCodiRilePrevalenteRil() {
        return codiRilePrevalenteRil;
    }

    public void setCodiRilePrevalenteRil(String codiRilePrevalenteRil) {
        this.codiRilePrevalenteRil = codiRilePrevalenteRil;
    }

    public String getDescRilePrevalenteRil() {
        return descRilePrevalenteRil;
    }

    public void setDescRilePrevalenteRil(String descRilePrevalenteRil) {
        this.descRilePrevalenteRil = descRilePrevalenteRil;
    }

    public Long getIdLavorazione() {
        return idLavorazione;
    }

    public void setIdLavorazione(Long idLavorazione) {
        this.idLavorazione = idLavorazione;
    }

    public Long getIdPoligonoDichiarato() {
        return idPoligonoDichiarato;
    }

    public void setIdPoligonoDichiarato(Long idPoligonoDichiarato) {
        this.idPoligonoDichiarato = idPoligonoDichiarato;
    }

    public Long getNumeroMessaggi() {
        return numeroMessaggi;
    }

    public void setNumeroMessaggi(Long numeroMessaggi) {
        this.numeroMessaggi = numeroMessaggi;
    }

    public Long getNumeroDocumenti() {
        return numeroDocumenti;
    }

    public void setNumeroDocumenti(Long numeroDocumenti) {
        this.numeroDocumenti = numeroDocumenti;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Double[] getExtent() {
        return extent;
    }

    public void setExtent(Double[] extent) {
        this.extent = extent;
    }

    public EsitoLavorazioneDichiarato getEsito() {
        return esito;
    }

    public void setEsito(EsitoLavorazioneDichiarato esito) {
        this.esito = esito;
    }

    public String getUtenteLavorazione() {
        return utenteLavorazione;
    }

    public void setUtenteLavorazione(String utenteLavorazione) {
        this.utenteLavorazione = utenteLavorazione;
    }

    public StatoLavorazioneSuolo getStatoLavorazione() {
        return statoLavorazione;
    }

    public void setStatoLavorazione(StatoLavorazioneSuolo statoLavorazione) {
        this.statoLavorazione = statoLavorazione;
    }

    public Boolean getNuovaModificaBo() {
        return nuovaModificaBo;
    }

    public void setNuovaModificaBo(Boolean nuovaModificaBo) {
        this.nuovaModificaBo = nuovaModificaBo;
    }

    public Boolean getNuovaModificaCaa() {
        return nuovaModificaCaa;
    }

    public void setNuovaModificaCaa(Boolean nuovaModificaCaa) {
        this.nuovaModificaCaa = nuovaModificaCaa;
    }

    public Boolean getVisibileInOrtofoto() {
        return visibileInOrtofoto;
    }

    public void setVisibileInOrtofoto(Boolean visibileInOrtofoto) {
        this.visibileInOrtofoto = visibileInOrtofoto;
    }

    public TipoInterventoColturale getTipoInterventoColturale() {
        return tipoInterventoColturale;
    }

    public void setTipoInterventoColturale(TipoInterventoColturale tipoInterventoColturale) {
        this.tipoInterventoColturale = tipoInterventoColturale;
    }

    public LocalDateTime getInterventoInizio() {
        return interventoInizio;
    }

    public void setInterventoInizio(LocalDateTime interventoInizio) {
        this.interventoInizio = interventoInizio;
    }

    public LocalDateTime getInterventoFine() {
        return interventoFine;
    }

    public void setInterventoFine(LocalDateTime interventoFine) {
        this.interventoFine = interventoFine;
    }

    @Override
    public String toString() {
        return String.format(
                "{id=%s,idRichiesta=%s,statoRichiesta=%s,aziendaAgricola=%s,campagna=%s, codSezione=%s, tipoSuoloDichiarato=%s,tipoSuoloRilevato=%s,codUsoDichiarato=%s,descCodUsoDichiarato=%s,"
                        + "codVarietaDichiarato=%s,descCodVarietaDichiarato=%s ,codUsoRilevato=%s ,descCodUsoRilevato=%s , codVarietaRilevato=%s,descCodVarietaRilevato=%s,idLavorazione=%s,visible=%s,extent=%s,"+
                        "utenteLavorazione=%s, statoLavorazione=%s, nuovaModificaBo=%s, nuovaModificaCaa=%s, visibileInOrtofoto=%s, tipoInterventoColturale=%s, interventoInizio=%s, interventoFine=%s}",
                id, idRichiesta, statoRichiesta, aziendaAgricola, campagna, codSezione, tipoSuoloDichiarato, tipoSuoloRilevato, codiRileDichiarato, descRileDichiarato, codiProdRileDichiarato,
                descProdRileDichiarato, codiRileRilevato, descRileRilevato, codiProdRileRilevato, descProdRileRilevato, idLavorazione, visible, extent, utenteLavorazione, statoLavorazione,
                nuovaModificaBo, nuovaModificaCaa,visibileInOrtofoto, tipoInterventoColturale, interventoInizio, interventoFine);
    }
}

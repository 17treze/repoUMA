package it.tndigitale.a4g.richiestamodificasuolo.dto.filter;

import java.io.Serializable;

import io.swagger.annotations.ApiParam;

public class SuoloDichiaratoLavorazioneFilter implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiParam(value = "idRichiesta modifica suolo")
    private Long idRichiesta;

    @ApiParam(value = "CUAA richiesta modifica suolo")
    private String cuaa;

    @ApiParam(value = "cod_sezione prevalente")
    private String comuneCatastale;

    @ApiParam(value = "Anno di campagna")
    private Long campagna;

    @ApiParam(value = "Visibile da ortofoto")
    private Boolean visibileOrtofoto;
        
    @ApiParam(value = "tipoSuoloRilevato")
    private String tipoSuoloRilevato;

    @ApiParam(value = "tipoSuoloDichiarato")
    private String tipoSuoloDichiarato;

    @ApiParam(value = "stato richiesta modifica suolo")
    private String statoRichiesta;

    @ApiParam(value = "IdLavorzione lavorazione suolo")
    private Long idLavorazione;

    @ApiParam(value = "stato Lavorazione suolo")
    private String statoLavorazione;

    @ApiParam(value = "profilo VITICOLO", defaultValue = "false")
    private Boolean isViticolo;

    public Long getIdRichiesta() {
        return idRichiesta;
    }

    public void setIdRichiesta(Long idRichiesta) {
        this.idRichiesta = idRichiesta;
    }

    public String getCuaa() {
        return cuaa;
    }

    public void setCuaa(String cuaa) {
        this.cuaa = cuaa;
    }

    public String getComuneCatastale() {
        return comuneCatastale;
    }

    public void setComuneCatastale(String comuneCatastale) {
        this.comuneCatastale = comuneCatastale;
    }

    public Long getCampagna() {
        return campagna;
    }

    public void setCampagna(Long campagna) {
        this.campagna = campagna;
    }

    public Boolean getVisibileOrtofoto() {
        return visibileOrtofoto;
    }

    public void setVisibileOrtofoto(Boolean visibileOrtofoto) {
        this.visibileOrtofoto = visibileOrtofoto;
    }
    
    public String getTipoSuoloRilevato() {
        return tipoSuoloRilevato;
    }

    public void setTipoSuoloRilevato(String tipoSuoloRilevato) {
        this.tipoSuoloRilevato = tipoSuoloRilevato;
    }

    public String getTipoSuoloDichiarato() {
        return tipoSuoloDichiarato;
    }

    public void setTipoSuoloDichiarato(String tipoSuoloDichiarato) {
        this.tipoSuoloDichiarato = tipoSuoloDichiarato;
    }

    public String getStatoRichiesta() {
        return statoRichiesta;
    }

    public void setStatoRichiesta(String statoRichiesta) {
        this.statoRichiesta = statoRichiesta;
    }

    public Long getIdLavorazione() {
        return idLavorazione;
    }

    public void setIdLavorazione(Long idLavorazione) {
        this.idLavorazione = idLavorazione;
    }

    public String getStatoLavorazione() {
        return statoLavorazione;
    }

    public void setStatoLavorazione(String statoLavorazione) {
        this.statoLavorazione = statoLavorazione;
    }

    public Boolean getIsViticolo() {
        return isViticolo;
    }

    public void setIsViticolo(Boolean isViticolo) {
        this.isViticolo = isViticolo;
    }

    @Override
    public String toString() {
        return String.format(
                "SuoloDichiaratoLavorazioneFilter [idRichiesta=%s, cuaa=%s, comuneCatastale=%s, campagna=%s,tipoSuoloRilevato=%s, tipoSuoloDichiarato=%s,stato=%s,idLavorazione=%s,statoLavorazione=%s,isViticolo=%s]",
                idRichiesta, cuaa, comuneCatastale, campagna, tipoSuoloRilevato, tipoSuoloDichiarato, statoRichiesta, idLavorazione, statoLavorazione, isViticolo);
    }

}

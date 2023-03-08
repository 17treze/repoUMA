package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

/**
 * The persistent class for the A4ST_RICHIESTA_MODIFICA_SUOLO database table.
 * 
 */
@Entity
@Table(name = "A4ST_RICHIESTA_MODIFICA_SUOLO")
public class RichiestaModificaSuoloModel extends EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "UTENTE")
	private String utente;

	@Column(name = "DATA_RICHIESTA")
	private LocalDateTime dataRichiesta;

	@Enumerated(EnumType.STRING)
	private TipoRichiestaModificaSuolo tipo;

	@Enumerated(EnumType.STRING)
	private StatoRichiestaModificaSuolo stato;

	@Enumerated(EnumType.STRING)
	private EsitoRichiestaModificaSuolo esito;

	@Column(name = "CUAA")
	private String cuaa;

	private String azienda;

	private Integer campagna;

	@Column(name = "ID_ISTANZA_RIESAME")
	private Long idIstanzaRiesame;

	@Column(name = "VISIBILE_IN_ORTOFOTO")
	private Boolean visibileInOrtofoto;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_INTERVENTO_COLTURALE")
	private TipoInterventoColturale tipoInterventoColturale;

	@Column(name = "INTERVENTO_INIZIO")
	private LocalDateTime interventoInizio;

	@Column(name = "INTERVENTO_FINE")
	private LocalDateTime interventoFine;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "richiestaModificaSuolo")
	private List<SuoloDichiaratoModel> suoloDichiaratoModel;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "richiestaModificaSuolo")
	private List<DocumentazioneRichiestaModificaSuoloModel> listaAllegati;

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public LocalDateTime getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(LocalDateTime dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public TipoRichiestaModificaSuolo getTipo() {
		return tipo;
	}

	public void setTipo(TipoRichiestaModificaSuolo tipo) {
		this.tipo = tipo;
	}

	public StatoRichiestaModificaSuolo getStato() {
		return stato;
	}

	public void setStato(StatoRichiestaModificaSuolo stato) {
		this.stato = stato;
	}

	public EsitoRichiestaModificaSuolo getEsito() {
		return esito;
	}

	public void setEsito(EsitoRichiestaModificaSuolo esito) {
		this.esito = esito;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getAzienda() {
		return azienda;
	}

	public void setAzienda(String azienda) {
		this.azienda = azienda;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public Long getIdIstanzaRiesame() {
		return idIstanzaRiesame;
	}

	public void setIdIstanzaRiesame(Long idIstanzaRiesame) {
		this.idIstanzaRiesame = idIstanzaRiesame;
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

	public List<SuoloDichiaratoModel> getSuoloDichiaratoModel() {
		return suoloDichiaratoModel;
	}

	public void setSuoloDichiaratoModel(List<SuoloDichiaratoModel> suoloDichiaratoModel) {
		this.suoloDichiaratoModel = suoloDichiaratoModel;
	}

	public List<DocumentazioneRichiestaModificaSuoloModel> getListaAllegati() {
		return listaAllegati;
	}

	public void setListaAllegati(List<DocumentazioneRichiestaModificaSuoloModel> listaAllegati) {
		this.listaAllegati = listaAllegati;
	}

}
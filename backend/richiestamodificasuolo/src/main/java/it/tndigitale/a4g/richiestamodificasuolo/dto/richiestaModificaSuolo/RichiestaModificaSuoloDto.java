package it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.EsitoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoRichiestaModificaSuolo;

public class RichiestaModificaSuoloDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String utente;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime data;
	private TipoRichiestaModificaSuolo tipo;
	private StatoRichiestaModificaSuolo stato;
	private EsitoRichiestaModificaSuolo esito;
	private AziendaAgricolaDto aziendaAgricola;
	private Integer campagna;
	private DatiAggiuntiviRichiestaModificaSuoloDto datiAggiuntivi;
	private List<ComuneCatastaleDto> sezioniCatastali;
	private Double[] extent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
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

	public AziendaAgricolaDto getAziendaAgricola() {
		return aziendaAgricola;
	}

	public void setAziendaAgricola(AziendaAgricolaDto aziendaAgricola) {
		this.aziendaAgricola = aziendaAgricola;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public DatiAggiuntiviRichiestaModificaSuoloDto getDatiAggiuntivi() {
		return datiAggiuntivi;
	}

	public void setDatiAggiuntivi(DatiAggiuntiviRichiestaModificaSuoloDto datiAggiuntvi) {
		this.datiAggiuntivi = datiAggiuntvi;
	}

	public List<ComuneCatastaleDto> getSezioniCatastali() {
		return sezioniCatastali;
	}

	public void setSezioniCatastali(List<ComuneCatastaleDto> sezioniCatastali) {
		this.sezioniCatastali = sezioniCatastali;
	}

	public Double[] getExtent() {
		return extent;
	}

	public void setExtent(Double[] extent) {
		this.extent = extent;
	}

}

package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.specification;

import java.io.Serializable;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

public class ValidazioneFascicoloFilter implements Serializable {
	private static final long serialVersionUID = 4178758546377197313L;
	
	private Long id;
	
	private StatoFascicoloEnum stato;
	
	private Integer annoValidazione;
	
	public ValidazioneFascicoloFilter(
			final Long id,
			final StatoFascicoloEnum stato,
			final Integer annoValidazione) {
		super();
		this.id = id;
		this.stato = stato;
		this.annoValidazione = annoValidazione;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public StatoFascicoloEnum getStato() {
		return stato;
	}

	public void setStato(StatoFascicoloEnum stato) {
		this.stato = stato;
	}

	public Integer getAnnoValidazione() {
		return annoValidazione;
	}

	public void setAnnoValidazione(Integer dataAggiornamento) {
		this.annoValidazione = dataAggiornamento;
	}
}

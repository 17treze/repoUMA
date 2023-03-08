package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name = "A4GT_FASCICOLO", uniqueConstraints = {@UniqueConstraint(columnNames={"ID_VALIDAZIONE", "CUAA"})})
public class FascicoloModel extends EntitaDominioFascicolo {
	private static final long serialVersionUID = 8421093257879552608L;

	@Column(name = "CUAA", length = 16, nullable = false)
	private String cuaa;
	
	@Column(name = "DT_AGGIORNAMENTO_FONTI_ESTERNE")
	private LocalDateTime dtAggiornamentoFontiEsterne;
	
	@OneToMany(mappedBy = "fascicolo", fetch = FetchType.LAZY)
	private List<MacchinaModel> macchine;
	
	@OneToMany(mappedBy = "fascicolo", fetch = FetchType.LAZY)
	private List<FabbricatoModel> fabbricati;

	public String getCuaa() {
		return cuaa;
	}

	public FascicoloModel setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

	public LocalDateTime getDtAggiornamentoFontiEsterne() {
		return dtAggiornamentoFontiEsterne;
	}

	public void setDtAggiornamentoFontiEsterne(LocalDateTime dtAggiornamentoFontiEsterne) {
		this.dtAggiornamentoFontiEsterne = dtAggiornamentoFontiEsterne;
	}

	public List<MacchinaModel> getMacchine() {
		return macchine;
	}

	public void setMacchine(List<MacchinaModel> macchine) {
		this.macchine = macchine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cuaa == null) ? 0 : cuaa.hashCode());
		result = prime * result + ((dtAggiornamentoFontiEsterne == null) ? 0 : dtAggiornamentoFontiEsterne.hashCode());
		result = prime * result + ((macchine == null) ? 0 : macchine.hashCode());
		result = prime * result + ((fabbricati == null) ? 0 : fabbricati.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FascicoloModel other = (FascicoloModel) obj;
		if (cuaa == null) {
			if (other.cuaa != null)
				return false;
		} else if (!cuaa.equals(other.cuaa))
			return false;
		if (dtAggiornamentoFontiEsterne == null) {
			if (other.dtAggiornamentoFontiEsterne != null)
				return false;
		} else if (!dtAggiornamentoFontiEsterne.equals(other.dtAggiornamentoFontiEsterne))
			return false;
		if (macchine == null) {
			if (other.macchine != null)
				return false;
		} else if (!macchine.equals(other.macchine))
			return false;
		if (fabbricati == null) {
			if (other.fabbricati != null)
				return false;
		} else if (!fabbricati.equals(other.fabbricati))
			return false;
		return true;
	}

	public List<FabbricatoModel> getFabbricati() {
		return fabbricati;
	}

	public void setFabbricati(List<FabbricatoModel> fabbricati) {
		this.fabbricati = fabbricati;
	}
}
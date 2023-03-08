package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
	private List<AllevamentoModel> allevamenti;

	public List<AllevamentoModel> getAllevamenti() {
		return allevamenti;
	}

	public void setAllevamenti(List<AllevamentoModel> allevamenti) {
		this.allevamenti = allevamenti;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(allevamenti, cuaa, dtAggiornamentoFontiEsterne);
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
		return Objects.equals(allevamenti, other.allevamenti) && Objects.equals(cuaa, other.cuaa)
				&& Objects.equals(dtAggiornamentoFontiEsterne, other.dtAggiornamentoFontiEsterne);
	}
}

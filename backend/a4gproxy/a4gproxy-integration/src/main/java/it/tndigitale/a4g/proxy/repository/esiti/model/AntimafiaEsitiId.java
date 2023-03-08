package it.tndigitale.a4g.proxy.repository.esiti.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AntimafiaEsitiId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="CUAA")
	private String cuaa;
	
	@Column(name="DATA_ELABORAZIONE")
	private LocalDateTime dtElaborazione;


	public AntimafiaEsitiId() {
		super();
	}


	public AntimafiaEsitiId(String cuaa, LocalDateTime dtElaborazione) {
		this.cuaa = cuaa;
		this.dtElaborazione = dtElaborazione;
	}

	public String getCuaa() {
		return cuaa;
	}

	public LocalDateTime getDtElaborazione() {
		return dtElaborazione;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cuaa == null) ? 0 : cuaa.hashCode());
		result = prime * result + ((dtElaborazione == null) ? 0 : dtElaborazione.hashCode());
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
		AntimafiaEsitiId other = (AntimafiaEsitiId) obj;
		if (cuaa == null) {
			if (other.cuaa != null)
				return false;
		} else if (!cuaa.equals(other.cuaa))
			return false;
		if (dtElaborazione == null) {
			if (other.dtElaborazione != null)
				return false;
		} else if (!dtElaborazione.equals(other.dtElaborazione))
			return false;
		return true;
	}
}

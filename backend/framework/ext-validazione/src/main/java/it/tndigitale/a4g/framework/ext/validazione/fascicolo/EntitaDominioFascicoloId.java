package it.tndigitale.a4g.framework.ext.validazione.fascicolo;

import java.io.Serializable;

public class EntitaDominioFascicoloId implements Serializable {
	private static final long serialVersionUID = 2641977970555740655L;
	
	protected Long id;
	protected Integer idValidazione;
	
	public EntitaDominioFascicoloId() {}
	
	public EntitaDominioFascicoloId(Long id, Integer idValidazione) {
		super();
		this.id = id;
		this.idValidazione = idValidazione;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idValidazione == null) ? 0 : idValidazione.hashCode());
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
		EntitaDominioFascicoloId other = (EntitaDominioFascicoloId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idValidazione == null) {
			if (other.idValidazione != null)
				return false;
		} else if (!idValidazione.equals(other.idValidazione))
			return false;
		return true;
	}
}

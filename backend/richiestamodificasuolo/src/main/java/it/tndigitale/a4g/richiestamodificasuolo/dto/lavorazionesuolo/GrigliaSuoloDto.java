package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;

public class GrigliaSuoloDto implements Serializable {

	private static final long serialVersionUID = -6987013288069015806L;

	private Long id;
	private boolean intersecaSuolo;
	private boolean confine;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isIntersecaSuolo() {
		return intersecaSuolo;
	}

	public void setIntersecaSuolo(boolean intersecaSuolo) {
		this.intersecaSuolo = intersecaSuolo;
	}

	public boolean isConfine() {
		return confine;
	}

	public void setConfine(boolean confine) {
		this.confine = confine;
	}

}

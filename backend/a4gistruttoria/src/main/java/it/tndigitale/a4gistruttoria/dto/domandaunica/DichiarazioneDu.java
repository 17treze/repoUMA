package it.tndigitale.a4gistruttoria.dto.domandaunica;

import it.tndigitale.a4gistruttoria.repository.model.Quadro;

import java.io.Serializable;
import java.util.Comparator;

public class DichiarazioneDu implements Serializable, Comparable<DichiarazioneDu> {

	private String codice;

	private String descrizione;

	private String valore;

	private Long idDomanda;

	private Long ordine;

	private Quadro quadro;

	public Quadro getQuadro() {
		return quadro;
	}

	public void setQuadro(Quadro quadro) {
		this.quadro = quadro;
	}

	public Long getOrdine() {
		return ordine;
	}

	public void setOrdine(Long ordine) {
		this.ordine = ordine;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	@Override
	public int compareTo(DichiarazioneDu o) {
		return Comparator.comparing(DichiarazioneDu::getQuadro).thenComparing(DichiarazioneDu::getOrdine).compare(this, o);
	}
}

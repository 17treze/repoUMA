package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The persistent class for the A4GD_COLTURA_INTERVENTO database table.
 * 
 */
@Entity
@Table(name = "A4GD_COLTURA_INTERVENTO")
@NamedQuery(name = "A4gdColturaIntervento.findAll", query = "SELECT a FROM A4gdColturaIntervento a")
public class A4gdColturaIntervento extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	
	private static final long serialVersionUID = 614283137171668150L;

	@Column(name = "COD_COLTURA_DIVERSA")
	private BigDecimal codColturaDiversa;

	@Column(name = "CODICE_COLTURA_3")
	private String codiceColtura3;

	@Column(name = "CODICE_COLTURA_5")
	private String codiceColtura5;

	@Column(name = "DESCRIZIONE_COLTURA")
	private String descrizioneColtura;

	private String leguminose;

	// bi-directional many-to-one association to InterventoModel
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_INTERVENTO")
	private InterventoModel intervento;
	
	@Column(name = "ANNO_INIZIO")
	private Integer annoInizio;
	
	@Column(name = "ANNO_FINE")
	private Integer annoFine;

	public BigDecimal getCodColturaDiversa() {
		return this.codColturaDiversa;
	}

	public void setCodColturaDiversa(BigDecimal codColturaDiversa) {
		this.codColturaDiversa = codColturaDiversa;
	}

	public String getCodiceColtura3() {
		return this.codiceColtura3;
	}

	public void setCodiceColtura3(String codiceColtura3) {
		this.codiceColtura3 = codiceColtura3;
	}

	public String getCodiceColtura5() {
		return this.codiceColtura5;
	}

	public void setCodiceColtura5(String codiceColtura5) {
		this.codiceColtura5 = codiceColtura5;
	}

	public String getDescrizioneColtura() {
		return this.descrizioneColtura;
	}

	public void setDescrizioneColtura(String descrizioneColtura) {
		this.descrizioneColtura = descrizioneColtura;
	}

	public String getLeguminose() {
		return this.leguminose;
	}

	public void setLeguminose(String leguminose) {
		this.leguminose = leguminose;
	}

	public InterventoModel getIntervento() {
		return this.intervento;
	}

	public void setIntervento(InterventoModel intervento) {
		this.intervento = intervento;
	}
	
	public Integer getAnnoInizio() {
		return annoInizio;
	}

	@SuppressWarnings("unused")
	private void setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
	}

	public Integer getAnnoFine() {
		return annoFine;
	}

	@SuppressWarnings("unused")
	private void setAnnoFine(Integer annoFine) {
		this.annoFine = annoFine;
	}
}
package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the A4GT_DICHIARAZIONE_DU database table.
 * 
 */
@Entity
@Table(name = "A4GT_DICHIARAZIONE_DU")
@NamedQuery(name = "DichiarazioneDomandaUnicaModel.findAll", query = "SELECT a FROM DichiarazioneDomandaUnicaModel a")
public class DichiarazioneDomandaUnicaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 2280400547963883580L;

	private String codice;

	private String descrizione;

	@Column(name = "VALORE_BOOL")
	private BigDecimal valoreBool;

	@Temporal(TemporalType.DATE)
	@Column(name = "VALORE_DATA")
	private Date valoreData;

	@Column(name = "VALORE_NUMERO")
	private BigDecimal valoreNumero;

	@Column(name = "VALORE_STRINGA")
	private String valoreStringa;

	@Column(name = "QUADRO")
	@Enumerated(EnumType.STRING)
	private Quadro quadro;

	// bi-directional many-to-one association to DomandaUnicaModel
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOMANDA")
	private DomandaUnicaModel domandaUnicaModel;

	@Column(name = "ORDINE")
	private Long ordine;

	public Quadro getQuadro() {
		return quadro;
	}

	public void setQuadro(Quadro quadro) {
		this.quadro = quadro;
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

	public BigDecimal getValoreBool() {
		return this.valoreBool;
	}

	public void setValoreBool(BigDecimal valoreBool) {
		this.valoreBool = valoreBool;
	}

	public Date getValoreData() {
		return this.valoreData;
	}

	public void setValoreData(Date valoreData) {
		this.valoreData = valoreData;
	}

	public BigDecimal getValoreNumero() {
		return this.valoreNumero;
	}

	public void setValoreNumero(BigDecimal valoreNumero) {
		this.valoreNumero = valoreNumero;
	}

	public String getValoreStringa() {
		return this.valoreStringa;
	}

	public void setValoreStringa(String valoreStringa) {
		this.valoreStringa = valoreStringa;
	}

	public DomandaUnicaModel getDomandaUnicaModel() {
		return this.domandaUnicaModel;
	}

	public void setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
	}

	public Long getOrdine() {
		return ordine;
	}

	public void setOrdine(Long ordine) {
		this.ordine = ordine;
	}
}
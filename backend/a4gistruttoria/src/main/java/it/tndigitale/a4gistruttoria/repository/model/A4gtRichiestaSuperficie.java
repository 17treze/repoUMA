package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * The persistent class for the A4GT_RICHIESTA_SUPERFICIE database table.
 * 
 */
@Entity
@Table(name="A4GT_RICHIESTA_SUPERFICIE")
@NamedQuery(name="A4gtRichiestaSuperficie.findAll", query="SELECT a FROM A4gtRichiestaSuperficie a")
public class A4gtRichiestaSuperficie extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CODICE_COLTURA_3")
	private String codiceColtura3;

	@Column(name="CODICE_COLTURA_5")
	private String codiceColtura5;

	@Lob
	@Column(name="INFO_CATASTALI")
	private String infoCatastali;

	@Lob
	@Column(name="INFO_COLTIVAZIONE")
	private String infoColtivazione;

	@Lob
	@Column(name="RIFERIMENTI_CARTOGRAFICI")
	private String riferimentiCartografici;

	@Column(name="SUP_RICHIESTA")
	private BigDecimal supRichiesta;

	@Column(name="SUP_RICHIESTA_NETTA")
	private BigDecimal supRichiestaNetta;

	//bi-directional many-to-one association to InterventoModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_INTERVENTO")
	private InterventoModel intervento;

	//bi-directional many-to-one association to DomandaUnicaModel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DOMANDA")
	private DomandaUnicaModel domandaUnicaModel;

	public A4gtRichiestaSuperficie() {
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

	public String getInfoCatastali() {
		return this.infoCatastali;
	}

	public void setInfoCatastali(String infoCatastali) {
		this.infoCatastali = infoCatastali;
	}

	public String getInfoColtivazione() {
		return this.infoColtivazione;
	}

	public void setInfoColtivazione(String infoColtivazione) {
		this.infoColtivazione = infoColtivazione;
	}

	public String getRiferimentiCartografici() {
		return this.riferimentiCartografici;
	}

	public void setRiferimentiCartografici(String riferimentiCartografici) {
		this.riferimentiCartografici = riferimentiCartografici;
	}

	public BigDecimal getSupRichiesta() {
		return this.supRichiesta;
	}

	public void setSupRichiesta(BigDecimal supRichiesta) {
		this.supRichiesta = supRichiesta;
	}

	public BigDecimal getSupRichiestaNetta() {
		return this.supRichiestaNetta;
	}

	public void setSupRichiestaNetta(BigDecimal supRichiestaNetta) {
		this.supRichiestaNetta = supRichiestaNetta;
	}

	public InterventoModel getIntervento() {
		return this.intervento;
	}

	public void setIntervento(InterventoModel intervento) {
		this.intervento = intervento;
	}

	public DomandaUnicaModel getDomandaUnicaModel() {
		return this.domandaUnicaModel;
	}

	public void setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
	}

}
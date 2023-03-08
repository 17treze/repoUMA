package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "A4GD_INTERVENTO")
@NamedQuery(name = "InterventoModel.findAll", query = "SELECT a FROM InterventoModel a")
public class InterventoModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = -6052248028480161393L;

	@Column(name = "IDENTIFICATIVO", nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	private Intervento identificativo;

	@Column(name = "CODICE_AGEA", length = 10)
	private String codiceAgea;

	// @Column(name = "DESCRIZIONE_BREVE", length = 500)
	// private String descrizioneBreve;

	// @Column(name = "DESCRIZIONE_LUNGA", length = 4000)
	// private String descrizioneLunga;

	// @Column(name = "DESCRIZIONE_SETTORE", length = 500)
	// private String descrizioneSettore;

	@Column(name = "IDENTIFICATIVO_INTERVENTO", nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	private CodiceInterventoAgs identificativoIntervento;

	@Column(name = "IDENTIFICATIVO_SETTORE", length = 50)
	private String identificativoSettore;

	@Column(length = 10)
	private String misura;

	@Enumerated(EnumType.STRING)
	private Sostegno sostegno;

	public Intervento getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(Intervento identificativo) {
		this.identificativo = identificativo;
	}
	
	public String getCodiceAgea() {
		return this.codiceAgea;
	}

	public void setCodiceAgea(String codiceAgea) {
		this.codiceAgea = codiceAgea;
	}

	public CodiceInterventoAgs getIdentificativoIntervento() {
		return this.identificativoIntervento;
	}

		public void setIdentificativoIntervento(CodiceInterventoAgs identificativoIntervento) {
		this.identificativoIntervento = identificativoIntervento;
	}

	public String getIdentificativoSettore() {
		return this.identificativoSettore;
	}

	public void setIdentificativoSettore(String identificativoSettore) {
		this.identificativoSettore = identificativoSettore;
	}

	public String getMisura() {
		return this.misura;
	}

	public void setMisura(String misura) {
		this.misura = misura;
	}

	public Sostegno getSostegno() {
		return this.sostegno;
	}

	public void setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
	}
}
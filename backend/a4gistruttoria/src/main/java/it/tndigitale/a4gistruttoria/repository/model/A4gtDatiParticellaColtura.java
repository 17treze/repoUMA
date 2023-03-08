package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "A4GT_DATI_PARTICELLA_COLTURA")
public class A4gtDatiParticellaColtura extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CODICE_COLTURA_3")
	private String codiceColtura3;

	@Lob
	@Column(name = "DATI_PARTICELLA")
	private String datiParticella;

	@Lob
	@Column(name = "INFO_CATASTALI")
	private String infoCatastali;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ISTRUTTORIA")
	private IstruttoriaModel istruttoria;
	
	public A4gtDatiParticellaColtura() {
	}

	public String getCodiceColtura3() {
		return this.codiceColtura3;
	}

	public void setCodiceColtura3(String codiceColtura3) {
		this.codiceColtura3 = codiceColtura3;
	}

	public String getDatiParticella() {
		return this.datiParticella;
	}

	public void setDatiParticella(String datiParticella) {
		this.datiParticella = datiParticella;
	}

	public String getInfoCatastali() {
		return this.infoCatastali;
	}

	public void setInfoCatastali(String infoCatastali) {
		this.infoCatastali = infoCatastali;
	}

	public IstruttoriaModel getIstruttoria() {
		return istruttoria;
	}

	public void setIstruttoria(IstruttoriaModel istruttoria) {
		this.istruttoria = istruttoria;
	}

}

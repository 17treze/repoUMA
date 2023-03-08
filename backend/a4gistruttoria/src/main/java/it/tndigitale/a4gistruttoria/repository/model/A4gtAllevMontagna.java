package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "A4GT_ALLEV_MONTAGNA")
@NamedQuery(name = "A4gtAlevMotagna.findAll", query = "SELECT a FROM A4gtAllevMontagna a")
public class A4gtAllevMontagna extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = -781018720320737891L;
	
	@Column(name = "COD_ALLEV")
	private String codiceAllevamento;
	@Column(name = "FLAG_MONTAGNA")
	private Boolean flagMotagna;
	
	public String getCodiceAllevamento() {
		return codiceAllevamento;
	}
	public void setCodiceAllevamento(String codiceAllevamento) {
		this.codiceAllevamento = codiceAllevamento;
	}
	public Boolean getFlagMotagna() {
		return flagMotagna;
	}
	public void setFlagMotagna(Boolean flagMotagna) {
		this.flagMotagna = flagMotagna;
	}


}

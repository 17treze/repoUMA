package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="A4GT_STRUMENTALI")
public class StrumentaliModel extends FabbricatoModel {
	private static final long serialVersionUID = 6348180238141586911L;

	@Column(name="SUPERFICIE_COPERTA", length = 20)
	private Long superficiecoperta;
	
	@Column(name="SUPERFICIE_SCOPERTA", length = 20)
	private Long superficieScoperta;

	public Long getSuperficiecoperta() {
		return superficiecoperta;
	}

	public StrumentaliModel setSuperficiecoperta(Long superficiecoperta) {
		this.superficiecoperta = superficiecoperta;
		return this;
	}

	public Long getSuperficieScoperta() {
		return superficieScoperta;
	}

	public StrumentaliModel setSuperficieScoperta(Long superficieScoperta) {
		this.superficieScoperta = superficieScoperta;
		return this;
	}
}

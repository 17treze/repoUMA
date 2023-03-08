package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "A4GD_MAPPING_FABBRICATI_AGS")
public class MappingFabbricatiAgsModel implements Serializable {

	private static final long serialVersionUID = -6489267374450280098L;

	@Id
	@Column(name = "ID")
	protected Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SOTTOTIPO")
	private SottotipoModel sottotipo;

	@Column(name = "CODICE_AGS", length = 10)
	private String codiceAgs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SottotipoModel getSottotipo() {
		return sottotipo;
	}

	public void setSottotipo(SottotipoModel sottotipo) {
		this.sottotipo = sottotipo;
	}

	public String getCodiceAgs() {
		return codiceAgs;
	}

	public void setCodiceAgs(String codiceAgs) {
		this.codiceAgs = codiceAgs;
	}

}

package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="TAB_AGRI_UMAL_FABBRICATI")
public class FabbricatoModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = 3501632049529793583L;

	@Column(name="COMUNE", length = 50)
	private String comune;

	@Column(name="PROVINCIA", length = 20)
	private String provincia;

	@Column(name="SIGLA_PROVINCIA", length = 2)
	private String siglaProvincia;

	@Column(name = "PARTICELLA", length= 10)
	private String particella;

	@Column(name = "SUBALTERNO")
	private String subalterno;

	@Column(name="IDENTIFICATIVO_AGS")
	private Long identificativoAgs;

	@Column(name="VOLUME")
	private Integer volume;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA")
	private RichiestaCarburanteModel richiestaCarburante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO")
	private FabbricatoGruppiModel tipoFabbricato;

	@OneToMany(mappedBy = "fabbricatoModel", fetch = FetchType.LAZY, targetEntity = FabbisognoFabbricatoModel.class)
	private List<FabbisognoModel> fabbisogni;

	public String getComune() {
		return comune;
	}
	public FabbricatoModel setComune(String comune) {
		this.comune = comune;
		return this;
	}
	public String getProvincia() {
		return provincia;
	}
	public FabbricatoModel setProvincia(String provincia) {
		this.provincia = provincia;
		return this;
	}
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public FabbricatoModel setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
		return this;
	}
	public String getParticella() {
		return particella;
	}
	public FabbricatoModel setParticella(String particella) {
		this.particella = particella;
		return this;
	}
	public Long getIdentificativoAgs() {
		return identificativoAgs;
	}
	public FabbricatoModel setIdentificativoAgs(Long identificativoAgs) {
		this.identificativoAgs = identificativoAgs;
		return this;
	}
	public RichiestaCarburanteModel getRichiestaCarburante() {
		return richiestaCarburante;
	}
	public FabbricatoModel setRichiestaCarburante(RichiestaCarburanteModel richiestaCarburante) {
		this.richiestaCarburante = richiestaCarburante;
		return this;
	}
	public List<FabbisognoModel> getFabbisogni() {
		return fabbisogni;
	}
	public FabbricatoModel setFabbisogni(List<FabbisognoModel> fabbisogni) {
		this.fabbisogni = fabbisogni;
		return this;
	}
	public Integer getVolume() {
		return volume;
	}
	public FabbricatoModel setVolume(Integer volume) {
		this.volume = volume;
		return this;
	}
	public String getSubalterno() {
		return subalterno;
	}
	public FabbricatoModel setSubalterno(String subalterno) {
		this.subalterno = subalterno;
		return this;
	}
	public FabbricatoGruppiModel getTipoFabbricato() {
		return tipoFabbricato;
	}
	public FabbricatoModel setTipoFabbricato(FabbricatoGruppiModel tipoFabbricato) {
		this.tipoFabbricato = tipoFabbricato;
		return this;
	}
}
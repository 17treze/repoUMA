package it.tndigitale.a4g.uma.dto.richiesta;

import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;

public class MacchinaDto {

	private Long id;
	private String descrizione;
	private String classe;
	private String marca;
	private TipoCarburante alimentazione;
	private String targa;
	private String possesso;
	private Long identificativoAgs;
	private Boolean isUtilizzata;


	public Boolean getIsUtilizzata() {
		return isUtilizzata;
	}
	public MacchinaDto setIsUtilizzata(Boolean isUtilizzata) {
		this.isUtilizzata = isUtilizzata;
		return this;
	}
	public Long getId() {
		return id;
	}
	public MacchinaDto setId(Long id) {
		this.id = id;
		return this;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public MacchinaDto setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}
	public String getClasse() {
		return classe;
	}
	public MacchinaDto setClasse(String classe) {
		this.classe = classe;
		return this;
	}
	public String getMarca() {
		return marca;
	}
	public MacchinaDto setMarca(String marca) {
		this.marca = marca;
		return this;
	}
	public TipoCarburante getAlimentazione() {
		return alimentazione;
	}
	public MacchinaDto setAlimentazione(TipoCarburante alimentazione) {
		this.alimentazione = alimentazione;
		return this;
	}
	public String getTarga() {
		return targa;
	}
	public MacchinaDto setTarga(String targa) {
		this.targa = targa;
		return this;
	}
	public String getPossesso() {
		return possesso;
	}
	public MacchinaDto setPossesso(String possesso) {
		this.possesso = possesso;
		return this;
	}
	public Long getIdentificativoAgs() {
		return identificativoAgs;
	}
	public MacchinaDto setIdentificativoAgs(Long identificativoAgs) {
		this.identificativoAgs = identificativoAgs;
		return this;
	}
}

package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.util.Date;

public class AllegatoFamiliariConviventi {

	private Long id;
	private Date dtPdfDicFamConv;
	private String codCarica;
	private String cfSoggettoImpresa;
	private boolean firmaDigitale;
	private String tipoFile;

	public Long getId() {
		return id;
	}

	public Date getDtPdfDicFamConv() {
		return dtPdfDicFamConv;
	}

	public String getCodCarica() {
		return codCarica;
	}

	public String getCfSoggettoImpresa() {
		return cfSoggettoImpresa;
	}

	public boolean isFirmaDigitale() {
		return this.firmaDigitale;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDtPdfDichFamConv(Date dtPdfDicFamConv) {
		this.dtPdfDicFamConv = dtPdfDicFamConv;
	}

	public void setCodCarica(String codCarica) {
		this.codCarica = codCarica;
	}

	public void setCfSoggettoImpresa(String cfSoggettoImpresa) {
		this.cfSoggettoImpresa = cfSoggettoImpresa;
	}

	public void setFirmaDigitale(boolean firmaDigitale) {
		this.firmaDigitale = firmaDigitale;
	}

	public String getTipoFile() {
		return tipoFile;
	}

	public void setTipoFile(String tipoFile) {
		this.tipoFile = tipoFile;
	}

}

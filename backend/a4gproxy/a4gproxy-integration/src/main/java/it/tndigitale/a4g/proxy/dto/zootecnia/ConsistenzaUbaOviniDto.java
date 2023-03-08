package it.tndigitale.a4g.proxy.dto.zootecnia;

public class ConsistenzaUbaOviniDto {

	private Long idAllevamentoBdn;
	private String codiceAzienda;
	private String codiceFiscaleDetentore;
	private String codiceSpecie;

	private Double oviniMaschi;
	private Double oviniFemmine;
	private Double oviniTotali;

	private Double capriniMaschi;
	private Double capriniFemmine;
	private Double capriniTotali;

	public Long getIdAllevamentoBdn() {
		return idAllevamentoBdn;
	}
	public ConsistenzaUbaOviniDto setIdAllevamentoBdn(Long idAllevamentoBdn) {
		this.idAllevamentoBdn = idAllevamentoBdn;
		return this;
	}
	public String getCodiceAzienda() {
		return codiceAzienda;
	}
	public ConsistenzaUbaOviniDto setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
		return this;
	}
	public String getCodiceFiscaleDetentore() {
		return codiceFiscaleDetentore;
	}
	public ConsistenzaUbaOviniDto setCodiceFiscaleDetentore(String codiceFiscaleDetentore) {
		this.codiceFiscaleDetentore = codiceFiscaleDetentore;
		return this;
	}
	public String getCodiceSpecie() {
		return codiceSpecie;
	}
	public ConsistenzaUbaOviniDto setCodiceSpecie(String codiceSpecie) {
		this.codiceSpecie = codiceSpecie;
		return this;
	}
	public Double getOviniMaschi() {
		return oviniMaschi;
	}
	public ConsistenzaUbaOviniDto setOviniMaschi(Double oviniMaschi) {
		this.oviniMaschi = oviniMaschi;
		return this;
	}
	public Double getOviniFemmine() {
		return oviniFemmine;
	}
	public ConsistenzaUbaOviniDto setOviniFemmine(Double oviniFemmine) {
		this.oviniFemmine = oviniFemmine;
		return this;
	}
	public Double getOviniTotali() {
		return oviniTotali;
	}
	public ConsistenzaUbaOviniDto setOviniTotali(Double oviniTotali) {
		this.oviniTotali = oviniTotali;
		return this;
	}
	public Double getCapriniMaschi() {
		return capriniMaschi;
	}
	public ConsistenzaUbaOviniDto setCapriniMaschi(Double capriniMaschi) {
		this.capriniMaschi = capriniMaschi;
		return this;
	}
	public Double getCapriniFemmine() {
		return capriniFemmine;
	}
	public ConsistenzaUbaOviniDto setCapriniFemmine(Double capriniFemmine) {
		this.capriniFemmine = capriniFemmine;
		return this;
	}
	public Double getCapriniTotali() {
		return capriniTotali;
	}
	public ConsistenzaUbaOviniDto setCapriniTotali(Double capriniTotali) {
		this.capriniTotali = capriniTotali;
		return this;
	}
}

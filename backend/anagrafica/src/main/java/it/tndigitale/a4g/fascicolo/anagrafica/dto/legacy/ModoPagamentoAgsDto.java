package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy;

public class ModoPagamentoAgsDto {
	
	private Long idAgs;
	private String iban;
	private String denominazioneIstituto;
	private String denominazioneFiliale;
	
	public Long getIdAgs() {
		return idAgs;
	}
	public ModoPagamentoAgsDto setIdAgs(Long idAgs) {
		this.idAgs = idAgs;
		return this;
	}
	public String getIban() {
		return iban;
	}
	public ModoPagamentoAgsDto setIban(String iban) {
		this.iban = iban;
		return this;
	}
	public String getDenominazioneIstituto() {
		return denominazioneIstituto;
	}
	public ModoPagamentoAgsDto setDenominazioneIstituto(String denominazioneIstituto) {
		this.denominazioneIstituto = denominazioneIstituto;
		return this;
	}
	public String getDenominazioneFiliale() {
		return denominazioneFiliale;
	}
	public ModoPagamentoAgsDto setDenominazioneFiliale(String denominazioneFiliale) {
		this.denominazioneFiliale = denominazioneFiliale;
		return this;
	}
	
}

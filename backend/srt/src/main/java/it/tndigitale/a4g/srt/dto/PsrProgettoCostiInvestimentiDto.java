package it.tndigitale.a4g.srt.dto;

import java.math.BigDecimal;

public class PsrProgettoCostiInvestimentiDto {
	
	private Integer idProgetto;
	private String descrizioneCodificaInvestimento;
	private String descrizioneDettaglioInvestimenti;
	private String descrizionePianoInvestimenti;
	private String descrizioneSettoriProduttivi;
	private BigDecimal costoInvestimento;
	private BigDecimal speseTecniche;
	private BigDecimal percentualeRichiesto;
	private BigDecimal contributoRichiesto;
	
	public Integer getIdProgetto() {
		return idProgetto;
	}
	public void setIdProgetto(Integer idProgetto) {
		this.idProgetto = idProgetto;
	}
	public String getDescrizioneCodificaInvestimento() {
		return descrizioneCodificaInvestimento;
	}
	public void setDescrizioneCodificaInvestimento(String descrizioneCodificaInvestimento) {
		this.descrizioneCodificaInvestimento = descrizioneCodificaInvestimento;
	}
	public String getDescrizioneDettaglioInvestimenti() {
		return descrizioneDettaglioInvestimenti;
	}
	public void setDescrizioneDettaglioInvestimenti(String descrizioneDettaglioInvestimenti) {
		this.descrizioneDettaglioInvestimenti = descrizioneDettaglioInvestimenti;
	}
	public String getDescrizionePianoInvestimenti() {
		return descrizionePianoInvestimenti;
	}
	public void setDescrizionePianoInvestimenti(String descrizionePianoInvestimenti) {
		this.descrizionePianoInvestimenti = descrizionePianoInvestimenti;
	}
	public String getDescrizioneSettoriProduttivi() {
		return descrizioneSettoriProduttivi;
	}
	public void setDescrizioneSettoriProduttivi(String descrizioneSettoriProduttivi) {
		this.descrizioneSettoriProduttivi = descrizioneSettoriProduttivi;
	}
	public BigDecimal getCostoInvestimento() {
		return costoInvestimento;
	}
	public void setCostoInvestimento(BigDecimal costoInvestimento) {
		this.costoInvestimento = costoInvestimento;
	}
	public BigDecimal getSpeseTecniche() {
		return speseTecniche;
	}
	public void setSpeseTecniche(BigDecimal speseTecniche) {
		this.speseTecniche = speseTecniche;
	}
	public BigDecimal getPercentualeRichiesto() {
		return percentualeRichiesto;
	}
	public void setPercentualeRichiesto(BigDecimal percentualeRichiesto) {
		this.percentualeRichiesto = percentualeRichiesto;
	}
	public BigDecimal getContributoRichiesto() {
		return contributoRichiesto;
	}
	public void setContributoRichiesto(BigDecimal contributoRichiesto) {
		this.contributoRichiesto = contributoRichiesto;
	}
	
	public static PsrProgettoCostiInvestimentiDto mapToDto(final Object[] result) throws Exception {
		PsrProgettoCostiInvestimentiDto psr = new PsrProgettoCostiInvestimentiDto();
		psr.setIdProgetto((Integer)result[0]);
		psr.setDescrizioneCodificaInvestimento((String)result[1]);
		psr.setDescrizioneDettaglioInvestimenti((String)result[2]);
		psr.setDescrizionePianoInvestimenti((String)result[3]);
		psr.setDescrizioneSettoriProduttivi((String)result[4]);
		psr.setCostoInvestimento((BigDecimal)result[5]);
		psr.setSpeseTecniche((BigDecimal)result[6]);
		psr.setPercentualeRichiesto((BigDecimal)result[7]);
		psr.setContributoRichiesto((BigDecimal)result[8]);
		return psr;
	}
}

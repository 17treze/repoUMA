package it.tndigitale.a4g.srt.dto;

import java.time.LocalDate;

public class PsrProgettoImpresaDescrizioneDto {
	
	private Integer idProgetto;
	private String cuaa;
	private String ragioneSociale;
	private String protocolloProgetto;
	private LocalDate dataProgetto;
	private String codiceStatoProgetto;
	private String statoProgetto;
	private String codiceOperazione;
	private String descrizioneNormativa;
	
	public Integer getIdProgetto() {
		return idProgetto;
	}
	public void setIdProgetto(Integer idProgetto) {
		this.idProgetto = idProgetto;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getProtocolloProgetto() {
		return protocolloProgetto;
	}
	public void setProtocolloProgetto(String protocolloProgetto) {
		this.protocolloProgetto = protocolloProgetto;
	}
	public LocalDate getDataProgetto() {
		return dataProgetto;
	}
	public void setDataProgetto(LocalDate dataProgetto) {
		this.dataProgetto = dataProgetto;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public String getCodiceStatoProgetto() {
		return codiceStatoProgetto;
	}
	public void setCodiceStatoProgetto(String codiceStatoProgetto) {
		this.codiceStatoProgetto = codiceStatoProgetto;
	}
	public String getStatoProgetto() {
		return statoProgetto;
	}
	public void setStatoProgetto(String statoProgetto) {
		this.statoProgetto = statoProgetto;
	}
	
	public String getCodiceOperazione() {
		return codiceOperazione;
	}
	public void setCodiceOperazione(String codiceOperazione) {
		this.codiceOperazione = codiceOperazione;
	}
	public String getDescrizioneNormativa() {
		return descrizioneNormativa;
	}
	public void setDescrizioneNormativa(String descrizioneNormativa) {
		this.descrizioneNormativa = descrizioneNormativa;
	}
	public static PsrProgettoImpresaDescrizioneDto mapToDto(Object[] result) throws Exception {
		PsrProgettoImpresaDescrizioneDto psr = new PsrProgettoImpresaDescrizioneDto();
		psr.setIdProgetto((Integer)result[0]);
		psr.setCuaa((String)result[1]);
		PsrProtocolloDataProgetto protData = PsrProtocolloDataProgetto.parseSegnaturaProgetto((String)result[2]);
		psr.setProtocolloProgetto(protData.getProtocollo());
		psr.setDataProgetto(protData.getData());
		psr.setRagioneSociale((String)result[3]);
		psr.setCodiceStatoProgetto(String.valueOf((char)result[4]));
		psr.setStatoProgetto((String)result[5]);
		psr.setCodiceOperazione((String)result[6]);
		psr.setDescrizioneNormativa((String)result[7]);
		return psr;
	}
}

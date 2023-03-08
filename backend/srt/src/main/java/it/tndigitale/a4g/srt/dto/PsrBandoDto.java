package it.tndigitale.a4g.srt.dto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

// nota: progetto = domanda 
public class PsrBandoDto {
	private Integer idBando;
	private String codiceEnte;
	private String cuaa;
	private String descrizioneBando;
	private String descrizioneNormativa;
	private Integer idProgetto;
	private String codiceStatoProgetto;
	private String statoProgetto;
	private String codiceOperazione;
	private String protocolloProgetto;
	private LocalDate dataProgetto;
	private LocalDateTime dataTermineRendicontazione;
	
	public Integer getIdBando() {
		return idBando;
	}
	public void setIdBando(Integer idBando) {
		this.idBando = idBando;
	}
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public String getDescrizioneBando() {
		return descrizioneBando;
	}
	public void setDescrizioneBando(String descrizione) {
		this.descrizioneBando = descrizione;
	}
	public Integer getIdProgetto() {
		return idProgetto;
	}
	public void setIdProgetto(Integer idProgetto) {
		this.idProgetto = idProgetto;
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
	public void setCodiceOperazione(String codice) {
		this.codiceOperazione = codice;
	}
	public LocalDate getDataProgetto() {
		return dataProgetto;
	}
	public void setDataProgetto(LocalDate dataProgetto) {
		this.dataProgetto = dataProgetto;
	}
	public String getDescrizioneNormativa() {
		return descrizioneNormativa;
	}
	public void setDescrizioneNormativa(String descrizioneNormativa) {
		this.descrizioneNormativa = descrizioneNormativa;
	}
	public String getProtocolloProgetto() {
		return protocolloProgetto;
	}
	public void setProtocolloProgetto(String protocolloProgetto) {
		this.protocolloProgetto = protocolloProgetto;
	}
	public LocalDateTime getDataTermineRendicontazione() {
		return dataTermineRendicontazione;
	}
	public void setDataTermineRendicontazione(LocalDateTime dataTermineRendicontazione) {
		this.dataTermineRendicontazione = dataTermineRendicontazione;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public void setDataScadenzaBando(final LocalDateTime dataScadenza) {
        this.setDataScadenzaBando(dataScadenza);
	}

	public static PsrBandoDto mapToDto(final Object[] result) throws Exception {
		PsrBandoDto psr = new PsrBandoDto();
		psr.setIdBando((Integer)result[0]);
		psr.setCodiceEnte((String)result[1]);
		psr.setDescrizioneBando((String)result[2]);
		psr.setIdProgetto((Integer)result[3]);
		
		PsrProtocolloDataProgetto protData = PsrProtocolloDataProgetto.parseSegnaturaProgetto((String)result[4]);
		psr.setProtocolloProgetto(protData.getProtocollo());
		psr.setDataProgetto(protData.getData());
		Timestamp ts = (Timestamp)result[6];
		if (ts != null) {
			psr.setDataTermineRendicontazione(ts.toLocalDateTime());
		}
		psr.setCodiceOperazione((String)result[7]);
		psr.setDescrizioneNormativa((String)result[8]);
		char codiceStatoProgetto = (char)result[9];
		psr.setCodiceStatoProgetto(String.valueOf(codiceStatoProgetto));
		psr.setStatoProgetto((String)result[10]);
		return psr;
	}
	
}

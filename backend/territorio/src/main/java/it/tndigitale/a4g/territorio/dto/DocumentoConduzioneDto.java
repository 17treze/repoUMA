package it.tndigitale.a4g.territorio.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class DocumentoConduzioneDto  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private byte[] contratto;
    private Long idTipoDocumento;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dataInizioValidita;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dataFineValidita;
    
	public byte[] getContratto() {
		return contratto;
	}
	public void setContratto(byte[] contratto) {
		this.contratto = contratto;
	}
	public Long getIdTipoDocumento() {
		return idTipoDocumento;
	}
	public void setIdTipoDocumento(Long idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}
	public LocalDate getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(LocalDate dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public LocalDate getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(LocalDate dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

}

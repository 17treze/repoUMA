package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.annotations.ApiModelProperty;

public class IscrizioneRepertorioEconomicoDto implements Serializable {
	private static final long serialVersionUID = 1453865690738808843L;

//	@ApiModelProperty(value = "Numero di iscrizione al registro", required = true)
//    private String numero;

    @ApiModelProperty(value = "Data di iscrizione", required = true)
    private LocalDate dataIscrizione;

    private Long codiceRea;

    private String provinciaRea;

    private boolean cessata;

	public LocalDate getDataIscrizione() {
		return dataIscrizione;
	}

	public void setDataIscrizione(LocalDate dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public Long getCodiceRea() {
		return codiceRea;
	}

	public void setCodiceRea(Long codiceRea) {
		this.codiceRea = codiceRea;
	}

	public String getProvinciaRea() {
		return provinciaRea;
	}

	public void setProvinciaRea(String provinciaRea) {
		this.provinciaRea = provinciaRea;
	}

	public boolean isCessata() {
		return cessata;
	}

	public void setCessata(boolean cessata) {
		this.cessata = cessata;
	}
}

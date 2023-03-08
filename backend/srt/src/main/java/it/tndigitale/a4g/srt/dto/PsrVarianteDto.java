package it.tndigitale.a4g.srt.dto;

import java.time.LocalDate;

public class PsrVarianteDto {
	private Integer idVariante;
	private LocalDate dataInserimento;
	private LocalDate dataModifica;
	private Boolean approvata;
	private LocalDate dataApprovazione;
	private Boolean annullata;
    private String codTipo;

	public Integer getIdVariante() {
		return idVariante;
	}

	public void setIdVariante(Integer idVariante) {
		this.idVariante = idVariante;
	}

	public LocalDate getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(LocalDate dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public LocalDate getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(LocalDate dataModifica) {
		this.dataModifica = dataModifica;
	}

	public Boolean getApprovata() {
		return approvata;
	}

	public void setApprovata(Boolean approvata) {
		this.approvata = approvata;
	}

	public LocalDate getDataApprovazione() {
		return dataApprovazione;
	}

	public void setDataApprovazione(LocalDate dataApprovazione) {
		this.dataApprovazione = dataApprovazione;
	}

	public Boolean getAnnullata() {
		return annullata;
	}

	public void setAnnullata(Boolean annullata) {
		this.annullata = annullata;
	}

    public String getCodTipo() {
        return codTipo;
    }

    public void setCodTipo(String codTipo) {
        this.codTipo = codTipo;
    }
}

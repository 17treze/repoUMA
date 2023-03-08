package it.tndigitale.a4gistruttoria.dto.lavorazione;

import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

public class DatiPassoLavorazione {

	private Long idTransizione;
	private TipologiaPassoTransizione passo;
	private Boolean esito;
	private String codiceEsito;
	private DatiInput datiInput = new DatiInput();
	private DatiOutput datiOutput = new DatiOutput();
	private DatiSintesi datiSintesi = new DatiSintesi(); 
	
	public Long getIdTransizione() {
		return idTransizione;
	}
	public TipologiaPassoTransizione getPasso() {
		return passo;
	}
	public Boolean getEsito() {
		return esito;
	}
	public String getCodiceEsito() {
		return codiceEsito;
	}
	public DatiInput getDatiInput() {
		return datiInput;
	}
	public DatiOutput getDatiOutput() {
		return datiOutput;
	}
	public DatiSintesi getDatiSintesi() {
		return datiSintesi;
	}
	public void setIdTransizione(Long idTransizione) {
		this.idTransizione = idTransizione;
	}
	public void setPasso(TipologiaPassoTransizione passo) {
		this.passo = passo;
	}
	public void setEsito(Boolean esito) {
		this.esito = esito;
	}
	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}
	public void setDatiInput(DatiInput datiInput) {
		this.datiInput = datiInput;
	}
	public void setDatiOutput(DatiOutput datiOutput) {
		this.datiOutput = datiOutput;
	}
	public void setDatiSintesi(DatiSintesi datiSintesi) {
		this.datiSintesi = datiSintesi;
	}
}

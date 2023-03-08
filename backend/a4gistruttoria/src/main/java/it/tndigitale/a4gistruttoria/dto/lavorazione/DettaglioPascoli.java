package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DettaglioPascoli {

	private String codicePascolo;
	private String descPascolo;
	private String tipoPascolo;
	private String esitoMan;
	private BigDecimal supNettaPascolo;
	private DatiIstruttoriaPascoli datiIstruttoriaPascoli;
	private List<VariabileCalcolo> datiInput;
	private List<VariabileCalcolo> datiOutput;
	private List<ControlloFrontend> listaEsitiPascolo;

	public DettaglioPascoli() {
		datiInput = new ArrayList<>();
		datiOutput = new ArrayList<>();
		listaEsitiPascolo = new ArrayList<>();
	}

	public String getCodicePascolo() {
		return codicePascolo;
	}

	public DettaglioPascoli setCodicePascolo(String codicePascolo) {
		this.codicePascolo = codicePascolo;
		return this;
	}

	public String getDescPascolo() {
		return descPascolo;
	}

	public DettaglioPascoli setDescPascolo(String descPascolo) {
		this.descPascolo = descPascolo;
		return this;
	}

	public String getTipoPascolo() {
		return tipoPascolo;
	}

	public DettaglioPascoli setTipoPascolo(String tipoPascolo) {
		this.tipoPascolo = tipoPascolo;
		return this;
	}

	public String getEsitoMan() {
		return esitoMan;
	}

	public DettaglioPascoli setEsitoMan(String esitoMan) {
		this.esitoMan = esitoMan;
		return this;
	}

	public BigDecimal getSupNettaPascolo() {
		return supNettaPascolo;
	}

	public DettaglioPascoli setSupNettaPascolo(BigDecimal supNettaPascolo) {
		this.supNettaPascolo = supNettaPascolo;
		return this;
	}

	public DatiIstruttoriaPascoli getDatiIstruttoriaPascoli() {
		return datiIstruttoriaPascoli;
	}

	public DettaglioPascoli setDatiIstruttoriaPascoli(DatiIstruttoriaPascoli datiIstruttoriaPascoli) {
		this.datiIstruttoriaPascoli = datiIstruttoriaPascoli;
		return this;
	}

	public List<VariabileCalcolo> getDatiInput() {
		return datiInput;
	}

	public DettaglioPascoli setDatiInput(List<VariabileCalcolo> datiInput) {
		this.datiInput = datiInput;
		return this;
	}

	public List<VariabileCalcolo> getDatiOutput() {
		return datiOutput;
	}

	public DettaglioPascoli setDatiOutput(List<VariabileCalcolo> datiOutput) {
		this.datiOutput = datiOutput;
		return this;
	}

	public List<ControlloFrontend> getListaEsitiPascolo() {
		return listaEsitiPascolo;
	}

	public DettaglioPascoli setListaEsitiPascolo(List<ControlloFrontend> listaEsitiPascolo) {
		this.listaEsitiPascolo = listaEsitiPascolo;
		return this;
	}
}

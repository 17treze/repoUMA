package it.tndigitale.a4g.proxy.dto.persona;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class UnitaLocaleDto implements Serializable {

	@ApiModelProperty(value = "Identificativo unita' locale", required = true)
    private String identificativo;

	private List<String> destinazioniUso;
	
	private LocalDate dataApertura;
	
	private LocalDate dataCessazione;
	
	private LocalDate dataDenunciaCessazione;
	
	private String causaleCessazione;
	
	private String attivita;
	
	private LocalDate dataDenunciaInizioAttivita;
	
	private String settoreMerceologico;
	
    @ApiModelProperty(value = "Domicilio dell'unita' locale")
    private IndirizzoDto indirizzo;
    
    @ApiModelProperty(value = "Attivita ateco della ditta")
    private List<AttivitaDto> attivitaAteco;
    
    @ApiModelProperty(value = "Indirizzo Pec dell'unita' locale")
    private String indirizzoPec;
    
    @ApiModelProperty(value = "Recapito telefonico dell'unita' locale")
    private String telefono;

	public String getIdentificativo() {
		return identificativo;
	}

	public UnitaLocaleDto setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
		return this;
	}

	public List<String> getDestinazioniUso() {
		return destinazioniUso;
	}

	public UnitaLocaleDto setDestinazioniUso(List<String> destinazioniUso) {
		this.destinazioniUso = destinazioniUso;
		return this;
	}

	public LocalDate getDataApertura() {
		return dataApertura;
	}

	public UnitaLocaleDto setDataApertura(LocalDate dataApertura) {
		this.dataApertura = dataApertura;
		return this;
	}

	public LocalDate getDataCessazione() {
		return dataCessazione;
	}

	public UnitaLocaleDto setDataCessazione(LocalDate dataCessazione) {
		this.dataCessazione = dataCessazione;
		return this;
	}

	public LocalDate getDataDenunciaCessazione() {
		return dataDenunciaCessazione;
	}

	public UnitaLocaleDto setDataDenunciaCessazione(LocalDate dataDenunciaCessazione) {
		this.dataDenunciaCessazione = dataDenunciaCessazione;
		return this;
	}

	public String getCausaleCessazione() {
		return causaleCessazione;
	}

	public UnitaLocaleDto setCausaleCessazione(String causaleCessazione) {
		this.causaleCessazione = causaleCessazione;
		return this;
	}

	public String getAttivita() {
		return attivita;
	}

	public UnitaLocaleDto setAttivita(String attivita) {
		this.attivita = attivita;
		return this;
	}

	public LocalDate getDataDenunciaInizioAttivita() {
		return dataDenunciaInizioAttivita;
	}

	public UnitaLocaleDto setDataDenunciaInizioAttivita(LocalDate dataDenunciaInizioAttivita) {
		this.dataDenunciaInizioAttivita = dataDenunciaInizioAttivita;
		return this;
	}

	public String getSettoreMerceologico() {
		return settoreMerceologico;
	}

	public UnitaLocaleDto setSettoreMerceologico(String settoreMerceologico) {
		this.settoreMerceologico = settoreMerceologico;
		return this;
	}

	public IndirizzoDto getIndirizzo() {
		return indirizzo;
	}

	public UnitaLocaleDto setIndirizzo(IndirizzoDto indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}

	public List<AttivitaDto> getAttivitaAteco() {
		return attivitaAteco;
	}

	public UnitaLocaleDto setAttivitaAteco(List<AttivitaDto> attivitaAteco) {
		this.attivitaAteco = attivitaAteco;
		return this;
	}

	
	public String getIndirizzoPec() {
		return indirizzoPec;
	}

	public UnitaLocaleDto setIndirizzoPec(String indirizzoPec) {
		this.indirizzoPec = indirizzoPec;
		return this;
	}

	public String getTelefono() {
		return telefono;
	}

	public UnitaLocaleDto setTelefono(String telefono) {
		this.telefono = telefono;
		return this;
	}


}

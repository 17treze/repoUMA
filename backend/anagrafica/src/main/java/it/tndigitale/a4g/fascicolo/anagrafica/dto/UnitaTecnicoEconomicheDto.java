package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.UnitaTecnicoEconomicheModel;

@ApiModel(description = "Unita tecnico economiche")
public class UnitaTecnicoEconomicheDto  implements Serializable {

	
//	@ApiModelProperty(value = "Identificativo modo pagamento")
	
	private String identificativoUte;

	private LocalDate dataApertura;

	private LocalDate dataCessazione;

	private LocalDate dataDenunciaCessazione;

	private String causaleCessazione;

	private String attivita;

	private LocalDate dataDenunciaInizioAttivita;

	private String settoreMerceologico;

	private String telefono;

	private String indirizzoPec;

	private String toponimo;

	private String via;

	private String numeroCivico;

	private String comune;

	private String cap;

	private String codiceIstatComune;

	private String frazione;

	private String provincia;
	
	private String indirizzo;
	
	private List<DestinazioniUsoDto> destinazioniUso;
	
	private List<AttivitaAtecoDto> attivitaAteco;

	public String getIdentificativoUte() {
		return identificativoUte;
	}
	public void setIdentificativoUte(String identificativoUte) {
		this.identificativoUte = identificativoUte;
	}
	public LocalDate getDataApertura() {
		return dataApertura;
	}
	public void setDataApertura(LocalDate dataApertura) {
		this.dataApertura = dataApertura;
	}
	public LocalDate getDataCessazione() {
		return dataCessazione;
	}
	public void setDataCessazione(LocalDate dataCessazione) {
		this.dataCessazione = dataCessazione;
	}
	public LocalDate getDataDenunciaCessazione() {
		return dataDenunciaCessazione;
	}
	public void setDataDenunciaCessazione(LocalDate dataDenunciaCessazione) {
		this.dataDenunciaCessazione = dataDenunciaCessazione;
	}
	public String getCausaleCessazione() {
		return causaleCessazione;
	}
	public void setCausaleCessazione(String causaleCessazione) {
		this.causaleCessazione = causaleCessazione;
	}
	public String getAttivita() {
		return attivita;
	}
	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}
	public LocalDate getDataDenunciaInizioAttivita() {
		return dataDenunciaInizioAttivita;
	}
	public void setDataDenunciaInizioAttivita(LocalDate dataDenunciaInizioAttivita) {
		this.dataDenunciaInizioAttivita = dataDenunciaInizioAttivita;
	}
	public String getSettoreMerceologico() {
		return settoreMerceologico;
	}
	public void setSettoreMerceologico(String settoreMerceologico) {
		this.settoreMerceologico = settoreMerceologico;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getIndirizzoPec() {
		return indirizzoPec;
	}
	public void setIndirizzoPec(String indirizzoPec) {
		this.indirizzoPec = indirizzoPec;
	}
	public String getToponimo() {
		return toponimo;
	}
	public void setToponimo(String toponimo) {
		this.toponimo = toponimo;
	}
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public String getNumeroCivico() {
		return numeroCivico;
	}
	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getCodiceIstatComune() {
		return codiceIstatComune;
	}
	public void setCodiceIstatComune(String codiceIstatComune) {
		this.codiceIstatComune = codiceIstatComune;
	}
	public String getFrazione() {
		return frazione;
	}
	public void setFrazione(String frazione) {
		this.frazione = frazione;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public List<DestinazioniUsoDto> getDestinazioniUso() {
		return destinazioniUso;
	}
	public void setDestinazioniUso(List<DestinazioniUsoDto> destinazioniUso) {
		this.destinazioniUso = destinazioniUso;
	}
	public List<AttivitaAtecoDto> getAttivitaAteco() {
		return attivitaAteco;
	}
	public void setAttivitaAteco(List<AttivitaAtecoDto> attivitaAteco) {
		this.attivitaAteco = attivitaAteco;
	}
	
	public static List<UnitaTecnicoEconomicheDto> build(final List<UnitaTecnicoEconomicheModel> input) {
		List<UnitaTecnicoEconomicheDto> output = new ArrayList<UnitaTecnicoEconomicheDto>();
		if(input == null || input.isEmpty()) {
			return output;
		}
		
		input.forEach(uteModel -> {
			output.add(UnitaTecnicoEconomicheDto.mapper(uteModel));
		});
		
		return output;
	}
	
	private static UnitaTecnicoEconomicheDto mapper(UnitaTecnicoEconomicheModel input) {
		if(input == null) {
			return null;
		}
		UnitaTecnicoEconomicheDto dto = new UnitaTecnicoEconomicheDto();
		dto.setAttivita(input.getAttivita());
		dto.setIdentificativoUte(input.getIdentificativoUte());
		dto.setIndirizzo(input.getToponimo() + "; " + input.getVia() +"; " + input.getNumeroCivico() +"; " + input.getComune()+"; " + input.getCap()+"; " + input.getCodiceIstatComune()+"; " + input.getFrazione()+"; " + input.getProvincia() );
		
		dto.setCap(input.getCap());
		dto.setCausaleCessazione(input.getCausaleCessazione());
		dto.setCodiceIstatComune(input.getCodiceIstatComune());
		dto.setComune(input.getComune());
		dto.setDataApertura(input.getDataApertura());
		dto.setDataCessazione(input.getDataCessazione());
		dto.setDataDenunciaCessazione(input.getDataDenunciaCessazione());
		dto.setDataDenunciaInizioAttivita(input.getDataDenunciaInizioAttivita());
		dto.setFrazione(input.getFrazione());
		dto.setIndirizzoPec(input.getIndirizzoPec());
		dto.setNumeroCivico(input.getNumeroCivico());
		dto.setProvincia(input.getProvincia());
		dto.setSettoreMerceologico(input.getSettoreMerceologico());
		dto.setTelefono(input.getTelefono());
		dto.setToponimo(input.getToponimo());
		dto.setVia(input.getVia());
		dto.setAttivitaAteco(AttivitaAtecoDto.build(input.getAttivitaAteco()));
		dto.setDestinazioniUso(DestinazioniUsoDto.build(input.getDestinazioneUso()));
		
		
		return dto;
	}
}

package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name = "A4GT_UNITA_TECNICO_ECONOMICHE")
public class UnitaTecnicoEconomicheModel extends EntitaDominioFascicolo {

	private static final long serialVersionUID = 2261567843022158179L;

	@Column(name = "IDENTIFICATIVO_UTE", length = 20)
	private String identificativoUte;

	@Column(name = "DATA_APERTURA")
	private LocalDate dataApertura;

	@Column(name = "DATA_CESSAZIONE")
	private LocalDate dataCessazione;

	@Column(name = "DATA_DENUNCIA_CESSAZIONE")
	private LocalDate dataDenunciaCessazione;

	@Column(name = "CAUSALE_CESSAZIONE", length = 4000)
	private String causaleCessazione;

	@Column(name = "ATTIVITA", length = 4000)
	private String attivita;

	@Column(name = "DATA_DENUNCIA_INIZIO_ATTIVITA")
	private LocalDate dataDenunciaInizioAttivita;

	@Column(name = "SETTORE_MERCEOLOGICO", length = 100)
	private String settoreMerceologico;

	@Column(name = "TELEFONO", length = 100)
	private String telefono;

	@Column(name = "INDIRIZZO_PEC", length = 100)
	private String indirizzoPec;

	@Column(name = "TOPONIMO", length = 100)
	private String toponimo;

	@Column(name = "VIA", length = 100)
	private String via;

	@Column(name = "NUMERO_CIVICO", length = 100)
	private String numeroCivico;

	@Column(name = "COMUNE", length = 100)
	private String comune;

	@Column(name = "CAP", length = 100)
	private String cap;

	@Column(name = "CODICE_ISTAT_COMUNE", length = 100)
	private String codiceIstatComune;

	@Column(name = "FRAZIONE", length = 100)
	private String frazione;

	@Column(name = "PROVINCIA", length = 100)
	private String provincia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERSONA", referencedColumnName = "ID")
	@JoinColumn(name = "PERSONA_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private PersonaModel persona;
	
	@OneToMany(mappedBy = "unitaTecnicoEconomiche", fetch = FetchType.LAZY)
	private List<DestinazioneUsoModel> destinazioneUso;
	
	@OneToMany(mappedBy = "unitaTecnicoEconomiche", fetch = FetchType.LAZY)
	private List<AttivitaAtecoModel> attivitaAteco;

	public String getIdentificativoUte() {
		return identificativoUte;
	}

	public UnitaTecnicoEconomicheModel setIdentificativoUte(String identificativoUte) {
		this.identificativoUte = identificativoUte;
		return this;
	}

	public LocalDate getDataApertura() {
		return dataApertura;
	}

	public UnitaTecnicoEconomicheModel setDataApertura(LocalDate dataApertura) {
		this.dataApertura = dataApertura;
		return this;
	}

	public LocalDate getDataCessazione() {
		return dataCessazione;
	}

	public UnitaTecnicoEconomicheModel setDataCessazione(LocalDate dataCessazione) {
		this.dataCessazione = dataCessazione;
		return this;
	}

	public LocalDate getDataDenunciaCessazione() {
		return dataDenunciaCessazione;
	}

	public UnitaTecnicoEconomicheModel setDataDenunciaCessazione(LocalDate dataDenunciaCessazione) {
		this.dataDenunciaCessazione = dataDenunciaCessazione;
		return this;
	}

	public String getCausaleCessazione() {
		return causaleCessazione;
	}

	public UnitaTecnicoEconomicheModel setCausaleCessazione(String causaleCessazione) {
		this.causaleCessazione = causaleCessazione;
		return this;
	}

	public String getAttivita() {
		return attivita;
	}

	public UnitaTecnicoEconomicheModel setAttivita(String attivita) {
		this.attivita = attivita;
		return this;
	}

	public LocalDate getDataDenunciaInizioAttivita() {
		return dataDenunciaInizioAttivita;
	}

	public UnitaTecnicoEconomicheModel setDataDenunciaInizioAttivita(LocalDate dataDenunciaInizioAttivita) {
		this.dataDenunciaInizioAttivita = dataDenunciaInizioAttivita;
		return this;
	}

	public String getSettoreMerceologico() {
		return settoreMerceologico;
	}

	public UnitaTecnicoEconomicheModel setSettoreMerceologico(String settoreMerceologico) {
		this.settoreMerceologico = settoreMerceologico;
		return this;
	}

	public String getTelefono() {
		return telefono;
	}

	public UnitaTecnicoEconomicheModel setTelefono(String telefono) {
		this.telefono = telefono;
		return this;
	}

	public String getIndirizzoPec() {
		return indirizzoPec;
	}

	public UnitaTecnicoEconomicheModel setIndirizzoPec(String indirizzoPec) {
		this.indirizzoPec = indirizzoPec;
		return this;
	}

	public String getToponimo() {
		return toponimo;
	}

	public UnitaTecnicoEconomicheModel setToponimo(String toponimo) {
		this.toponimo = toponimo;
		return this;
	}

	public String getVia() {
		return via;
	}

	public UnitaTecnicoEconomicheModel setVia(String via) {
		this.via = via;
		return this;
	}

	public String getNumeroCivico() {
		return numeroCivico;
	}

	public UnitaTecnicoEconomicheModel setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
		return this;
	}

	public String getComune() {
		return comune;
	}

	public UnitaTecnicoEconomicheModel setComune(String comune) {
		this.comune = comune;
		return this;
	}

	public String getCap() {
		return cap;
	}

	public UnitaTecnicoEconomicheModel setCap(String cap) {
		this.cap = cap;
		return this;
	}

	public String getCodiceIstatComune() {
		return codiceIstatComune;
	}

	public UnitaTecnicoEconomicheModel setCodiceIstatComune(String codiceIstatComune) {
		this.codiceIstatComune = codiceIstatComune;
		return this;
	}

	public String getFrazione() {
		return frazione;
	}

	public UnitaTecnicoEconomicheModel setFrazione(String frazione) {
		this.frazione = frazione;
		return this;
	}

	public String getProvincia() {
		return provincia;
	}

	public UnitaTecnicoEconomicheModel setProvincia(String provincia) {
		this.provincia = provincia;
		return this;
	}

	public PersonaModel getPersona() {
		return persona;
	}

	public UnitaTecnicoEconomicheModel setPersona(PersonaModel persona) {
		this.persona = persona;
		return this;
	}

	public List<DestinazioneUsoModel> getDestinazioneUso() {
		return destinazioneUso;
	}

	public UnitaTecnicoEconomicheModel setDestinazioneUso(List<DestinazioneUsoModel> destinazioneUso) {
		this.destinazioneUso = destinazioneUso;
		return this;
	}

	public List<AttivitaAtecoModel> getAttivitaAteco() {
		return attivitaAteco;
	}

	public UnitaTecnicoEconomicheModel setAttivitaAteco(List<AttivitaAtecoModel> attivitaAteco) {
		this.attivitaAteco = attivitaAteco;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects
				.hash(identificativoUte, dataApertura, dataCessazione, dataDenunciaCessazione, causaleCessazione, attivita,
						dataDenunciaInizioAttivita, settoreMerceologico, telefono, indirizzoPec, toponimo, via, numeroCivico,
						comune, cap, codiceIstatComune, frazione, provincia, persona);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj
				.getClass())
			return false;
		UnitaTecnicoEconomicheModel other = (UnitaTecnicoEconomicheModel) obj;
		return Objects
				.equals(identificativoUte, other.identificativoUte)
				&& Objects
				.equals(dataApertura, other.dataApertura)
				&& Objects
				.equals(dataCessazione, other.dataCessazione)
				&& Objects
				.equals(dataDenunciaCessazione, other.dataDenunciaCessazione)
				&& Objects
				.equals(causaleCessazione, other.causaleCessazione)
				&& Objects
				.equals(attivita, other.attivita)
				&& Objects
				.equals(dataDenunciaInizioAttivita, other.dataDenunciaInizioAttivita)
				&& Objects
				.equals(settoreMerceologico, other.settoreMerceologico)
				&& Objects
				.equals(telefono, other.telefono)
				&& Objects
				.equals(indirizzoPec, other.indirizzoPec)
				&& Objects
				.equals(toponimo, other.toponimo)
				&& Objects
				.equals(via, other.via)
				&& Objects
				.equals(numeroCivico, other.numeroCivico)
				&& Objects
				.equals(comune, other.comune)
				&& Objects
				.equals(cap, other.cap)
				&& Objects
				.equals(codiceIstatComune, other.codiceIstatComune)
				&& Objects
				.equals(frazione, other.frazione)
				&& Objects
				.equals(provincia, other.provincia)
				&& Objects
				.equals(persona, other.persona);
	}


}

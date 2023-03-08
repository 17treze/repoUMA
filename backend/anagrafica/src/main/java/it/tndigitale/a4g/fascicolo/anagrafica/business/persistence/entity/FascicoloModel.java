package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.Id;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaAziendaException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaCAAException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloInValidazioneException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloSospesoException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloStatoReadOnlyException;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.support.CustomCollectors;

@Entity
@Table(name = "A4GT_FASCICOLO", uniqueConstraints = {@UniqueConstraint(columnNames={"ID_VALIDAZIONE", "CUAA"})})
public class FascicoloModel extends EntitaDominioFascicolo {
	private static final long serialVersionUID = 1105409802238650520L;

	// Necessario per fare i cast:
	// https://itellity.wordpress.com/2014/04/29/class-cast-exception-with-hibernate-and-javassist-cgilib/
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "ID_PERSONA", referencedColumnName = "ID")
	@JoinColumn(nullable = false, name = "PERSONA_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private PersonaModel persona;

	@Column(name = "CUAA", length = 16, nullable = false)
	private String cuaa;

	@Column(name = "DENOMINAZIONE", length = 100)
	private String denominazione;

	@Column(name = "STATO", length = 30)
	@Enumerated(EnumType.STRING)
	private StatoFascicoloEnum stato;

	@Column(name = "ORGANISMO_PAGATORE", length = 30)
	@Enumerated(EnumType.STRING)
	private OrganismoPagatoreEnum organismoPagatore;

	@OneToMany(mappedBy = "fascicolo", fetch = FetchType.LAZY)
	private List<DetenzioneModel> detenzioni;

	@Column(name = "DATA_APERTURA")
	private LocalDate dataApertura;

	@OneToMany(mappedBy = "fascicolo", fetch = FetchType.LAZY)
	private List<ModoPagamentoModel> modoPagamentoList;

	@Column(name = "DATA_MODIFICA")
	private LocalDate dataModifica;

	@Column(name = "UTENTE_MODIFICA")
	private String utenteModifica;

	@Column(name = "DATA_VALIDAZIONE")
	private LocalDate dataValidazione;

	@Column(name = "UTENTE_VALIDAZIONE", length = 200)
	private String utenteValidazione;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "SCHEDA_VALIDAZIONE_FIRMATA", nullable = true)
	private byte[] schedaValidazioneFirmata;

	@Column(name = "ID_PROTOCOLLO")
	private String idProtocollo;

	@Column(name = "DT_PROTOCOLLAZIONE")
	private LocalDate dtProtocollazione;

	@Column(name = "DT_AGGIORNAMENTO_FONTI_ESTERNE")
	private LocalDateTime dtAggiornamentoFontiEsterne;

	@Column(name = "DT_CHIUSURA_TRASFERIMENTO_OP")
	private LocalDate dtChiusuraTrasferimentoOp;

	@OneToOne(mappedBy = "fascicolo", fetch = FetchType.LAZY)
	private DocumentoIdentitaModel documentoIdentita;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "SCHEDA_VALIDAZIONE", nullable = true)
	private byte[] schedaValidazione;
		
    @Column(name = "ID_SCHEDA_VALIDAZIONE")
	private Long idSchedaValidazione;
    
	@Transient
	private boolean ignoreStateOnUpdate = false;
	@Transient
	private StatoFascicoloEnum statoPrecedente = StatoFascicoloEnum.IN_AGGIORNAMENTO;

	public StatoFascicoloEnum getStatoPrecedente() {
		return statoPrecedente;
	}

	public void setStatoPrecedente(StatoFascicoloEnum statoPrecedente) {
		this.statoPrecedente = statoPrecedente;
	}

	public PersonaModel getPersona() {
		return persona;
	}

	public FascicoloModel setPersona(PersonaModel persona) {
		this.persona = persona;
		return this;
	}

	public String getCuaa() {
		return cuaa;
	}

	public FascicoloModel setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public FascicoloModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public StatoFascicoloEnum getStato() {
		return stato;
	}

	public FascicoloModel setStato(StatoFascicoloEnum stato) {
		if (this.stato != null && !this.stato.equals(stato) &&	
				!(this.statoPrecedente.equals(StatoFascicoloEnum.SOSPESO) ||
						this.statoPrecedente.equals(StatoFascicoloEnum.CHIUSO))) {
			this.ignoreStateOnUpdate = true;
		}
		this.stato = stato;
		return this;
	}

	public FascicoloModel ripristinaStato(StatoFascicoloEnum stato) {
		if (this.stato != null && !this.stato.equals(stato)) {
			this.ignoreStateOnUpdate = true;
		}
		this.stato = stato;
		return this;
	}

	public List<DetenzioneModel> getDetenzioni() {
		return detenzioni;
	}

	public FascicoloModel setDetenzioni(List<DetenzioneModel> detenzioni) {
		this.detenzioni = detenzioni;
		return this;
	}

	public OrganismoPagatoreEnum getOrganismoPagatore() {
		return organismoPagatore;
	}

	public FascicoloModel setOrganismoPagatore(OrganismoPagatoreEnum organismoPagatore) {
		this.organismoPagatore = organismoPagatore;
		return this;
	}

	public LocalDate getDataApertura() {
		return dataApertura;
	}

	public FascicoloModel setDataApertura(LocalDate dataApertura) {
		this.dataApertura = dataApertura;
		return this;
	}

	public FascicoloModel add(DetenzioneModel detenzione) {
		if (detenzione == null)
			return this;
		if (getDetenzioni() == null)
			setDetenzioni(new ArrayList<>());

		getDetenzioni().add(detenzione);
		return this;
	}

	public LocalDate getDataModifica() {
		return dataModifica;
	}

	public FascicoloModel setDataModifica(LocalDate dataModifica) {
		this.dataModifica = dataModifica;
		return this;
	}

	public String getUtenteModifica() {
		return utenteModifica;
	}

	public FascicoloModel setUtenteModifica(final String userName) {
		this.utenteModifica = userName;
		return this;
	}

	public List<ModoPagamentoModel> getModoPagamentoList() {
		return modoPagamentoList;
	}

	public FascicoloModel setModoPagamentoList(List<ModoPagamentoModel> modoPagamentoList) {
		this.modoPagamentoList = modoPagamentoList;
		return this;
	}

	public LocalDate getDataValidazione() {
		return dataValidazione;
	}

	public FascicoloModel setDataValidazione(LocalDate dataValidazione) {
		this.dataValidazione = dataValidazione;
		return this;
	}

	public String getUtenteValidazione() {
		return utenteValidazione;
	}

	public FascicoloModel setUtenteValidazione(String utenteValidazione) {
		this.utenteValidazione = utenteValidazione;
		return this;
	}

	public byte[] getSchedaValidazioneFirmata() {
		return schedaValidazioneFirmata;
	}

	public FascicoloModel setSchedaValidazioneFirmata(byte[] schedaValidazioneFirmata) {
		this.schedaValidazioneFirmata = schedaValidazioneFirmata;
		return this;
	}

	public String getIdProtocollo() {
		return idProtocollo;
	}

	public void setIdProtocollo(String idProtocollo) {
		this.ignoreStateOnUpdate = true;
		this.ignoreValidazioneCheck();
		this.idProtocollo = idProtocollo;
	}

	public LocalDate getDtProtocollazione() {
		return dtProtocollazione;
	}

	public void setDtProtocollazione(LocalDate dtProtocollazione) {
		this.ignoreStateOnUpdate = true;
		this.ignoreValidazioneCheck();
		this.dtProtocollazione = dtProtocollazione;
	}

	public LocalDateTime getDtAggiornamentoFontiEsterne() {
		return dtAggiornamentoFontiEsterne;
	}

	public void setDtAggiornamentoFontiEsterne(LocalDateTime dtAggiornamentoFontiEsterne) {
		this.dtAggiornamentoFontiEsterne = dtAggiornamentoFontiEsterne;
	}

	public LocalDate getDtChiusuraTrasferimentoOp() {
		return dtChiusuraTrasferimentoOp;
	}

	public void setDtChiusuraTrasferimentoOp(LocalDate dtChiusuraTrasferimentoOp) {
		this.dtChiusuraTrasferimentoOp = dtChiusuraTrasferimentoOp;
	}
	public DocumentoIdentitaModel getDocumentoIdentita() {
		return documentoIdentita;
	}

	public FascicoloModel setDocumentoIdentita(DocumentoIdentitaModel documentoIdentita) {
		this.documentoIdentita = documentoIdentita;
		return this;
	}
	
	public byte[] getSchedaValidazione() {
		return schedaValidazione;
	}

	public void setSchedaValidazione(byte[] schedaValidazione) {
		this.schedaValidazione = schedaValidazione;
	}

	public Long getIdSchedaValidazione() {
		return idSchedaValidazione;
	}

	public void setIdSchedaValidazione(Long idSchedaValidazione) {
		this.idSchedaValidazione = idSchedaValidazione;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FascicoloModel that = (FascicoloModel) o;
		return Objects.equals(persona, that.persona) &&
				Objects.equals(cuaa, that.cuaa) &&
				Objects.equals(detenzioni, that.detenzioni) &&
				Objects.equals(organismoPagatore, that.organismoPagatore) &&
				Objects.equals(dataApertura, that.dataApertura) &&
				Objects.equals(dataModifica, that.dataModifica) &&
				Objects.equals(utenteModifica, that.utenteModifica) &&
				Objects.equals(idProtocollo, that.idProtocollo) &&
				Objects.equals(dtProtocollazione, that.dtProtocollazione) &&
				Arrays.equals(schedaValidazioneFirmata, that.schedaValidazioneFirmata) &&
				Objects.equals(dtAggiornamentoFontiEsterne, that.dtAggiornamentoFontiEsterne) &&
				Objects.equals(dtChiusuraTrasferimentoOp, that.dtChiusuraTrasferimentoOp) &&
				Objects.equals(schedaValidazione, that.schedaValidazione) &&
				Objects.equals(idSchedaValidazione, that.idSchedaValidazione);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(schedaValidazioneFirmata);
		result = prime * result + Objects.hash(persona, cuaa, detenzioni, organismoPagatore, dataApertura, utenteModifica, dataModifica, idProtocollo, dtProtocollazione, dtAggiornamentoFontiEsterne, dtChiusuraTrasferimentoOp, schedaValidazione, idSchedaValidazione);
		return result;
	}

	@PreUpdate
	@PreRemove
	public void preventUpdateInValidazione() throws FascicoloStatoReadOnlyException {
		if (ignoreStateOnUpdate) {
			return;
		}
		if (stato.equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
			throw new FascicoloAllaFirmaAziendaException();
		} else if (stato.equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
			throw new FascicoloAllaFirmaCAAException();
		} else if (stato.equals(StatoFascicoloEnum.IN_VALIDAZIONE)) {
			throw new FascicoloInValidazioneException();
		} else if (statoPrecedente.equals(StatoFascicoloEnum.SOSPESO)) {
			throw new FascicoloSospesoException();
		}
		if (this.getIdValidazione() == 0 && stato.equals(StatoFascicoloEnum.VALIDATO)) {
			stato = StatoFascicoloEnum.IN_AGGIORNAMENTO;
		}
	}

	@PostLoad
	public void impostaStatoPrecedente() {
		this.statoPrecedente = this.stato;
	}

	@Transient
	public Optional<MandatoModel> getMandatoCorrente() {
		var today = LocalDate.now();
//		Il mandato dovrebbe essere considerato corrente se dataInizio <= today e (dataFine null o dataFine >= today)
		return detenzioni.stream().filter(MandatoModel.class::isInstance).filter(mandato -> {
			if (mandato.getDataInizio().isBefore(today) || mandato.getDataInizio().isEqual(today)) {
				Optional<LocalDate> dataFineOpt = Optional.ofNullable(mandato.getDataFine());
				return dataFineOpt.isPresent() ? dataFineOpt.get().isAfter(today) : Boolean.TRUE;
			}
			return false;
		})
				.map(MandatoModel.class::cast)
				.collect(CustomCollectors.collectOne());
	}
}

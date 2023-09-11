package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="TAB_AGRI_UMAL_DICHIARAZIONE_CONSUMI")
public class DichiarazioneConsumiModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA", referencedColumnName = "ID")
	private RichiestaCarburanteModel richiestaCarburante;

	@Column(name="CF_RICHIEDENTE", nullable = false, length = 16)
	private String cfRichiedente;

	@Column(name="DATA_PRESENTAZIONE", nullable = false)
	private LocalDateTime dataPresentazione;

	@Column(name="DATA_PROTOCOLLAZIONE")
	private LocalDateTime dataProtocollazione;

	@Column(name="PROTOCOLLO", length = 50)
	private String protocollo;

	@Column(name = "STATO", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private StatoDichiarazioneConsumi stato;

	@Column(name="DATA_CONDUZIONE")
	private LocalDateTime dataConduzione;

	@Column(name = "MOTIVAZIONE_ACCISA", length = 100, nullable = true)
	private String motivazioneAccisa;

	@Lob
	@Column(name="DOCUMENTO")
	private byte[] documento;

	@Column(name="FIRMA", nullable = true)
	private Boolean firma;

	// Denominazione dello sportello che al momento della creazione/protocollazione della domanda detiene il fascicolo
	@Column(name="ENTE_PRESENTATORE", length = 100)
	private String entePresentatore;

	@OneToMany(mappedBy = "dichiarazioneConsumi", fetch = FetchType.LAZY)
	private List<ClienteModel> clienti;

	@OneToMany(mappedBy = "dichiarazioneConsumi", fetch = FetchType.LAZY)
	private List<ConsuntivoConsumiModel> consuntivi;

	public RichiestaCarburanteModel getRichiestaCarburante() {
		return richiestaCarburante;
	}
	public DichiarazioneConsumiModel setRichiestaCarburante(RichiestaCarburanteModel richiestaCarburante) {
		this.richiestaCarburante = richiestaCarburante;
		return this;
	}
	public String getCfRichiedente() {
		return cfRichiedente;
	}
	public DichiarazioneConsumiModel setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
		return this;
	}
	public LocalDateTime getDataPresentazione() {
		return dataPresentazione;
	}
	public DichiarazioneConsumiModel setDataPresentazione(LocalDateTime dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
		return this;
	}
	public LocalDateTime getDataProtocollazione() {
		return dataProtocollazione;
	}
	public DichiarazioneConsumiModel setDataProtocollazione(LocalDateTime dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
		return this;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public DichiarazioneConsumiModel setProtocollo(String protocollo) {
		this.protocollo = protocollo;
		return this;
	}
	public StatoDichiarazioneConsumi getStato() {
		return stato;
	}
	public DichiarazioneConsumiModel setStato(StatoDichiarazioneConsumi stato) {
		this.stato = stato;
		return this;
	}
	public LocalDateTime getDataConduzione() {
		return dataConduzione;
	}
	public DichiarazioneConsumiModel setDataConduzione(LocalDateTime dataConduzione) {
		this.dataConduzione = dataConduzione;
		return this;
	}
	public List<ClienteModel> getClienti() {
		return clienti;
	}
	public DichiarazioneConsumiModel setClienti(List<ClienteModel> clienti) {
		this.clienti = clienti;
		return this;
	}
	public List<ConsuntivoConsumiModel> getConsuntivi() {
		return consuntivi;
	}
	public DichiarazioneConsumiModel setConsuntivi(List<ConsuntivoConsumiModel> consuntivi) {
		this.consuntivi = consuntivi;
		return this;
	}
	public String getMotivazioneAccisa() {
		return motivazioneAccisa;
	}
	public DichiarazioneConsumiModel setMotivazioneAccisa(String motivazioneAccisa) {
		this.motivazioneAccisa = motivazioneAccisa;
		return this;
	}
	public byte[] getDocumento() {
		return documento;
	}
	public DichiarazioneConsumiModel setDocumento(byte[] documento) {
		this.documento = documento;
		return this;
	}
	public Boolean getFirma() {
		return firma;
	}
	public DichiarazioneConsumiModel setFirma(Boolean firma) {
		this.firma = firma;
		return this;
	}
	public String getEntePresentatore() {
		return entePresentatore;
	}
	public DichiarazioneConsumiModel setEntePresentatore(String entePresentatore) {
		this.entePresentatore = entePresentatore;
		return this;
	}
}

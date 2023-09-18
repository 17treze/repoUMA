
package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name="TAB_AGRI_UMAL_RICHIESTA_CARBURANTE")
public class RichiestaCarburanteModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -238853354125405813L;

	@Column(name="CUAA", nullable = false, length = 16)
	private String cuaa;

	@Column(name="CAMPAGNA", nullable = false, length = 4)
	private Long campagna;

	@Column(name="DATA_PRESENTAZIONE", nullable = false)
	private LocalDateTime dataPresentazione;

	@Column(name="CF_RICHIEDENTE", nullable = false, length = 16)
	private String cfRichiedente;

	@Column(name="PROTOCOLLO", length = 50)
	private String protocollo;

	@Column(name="DATA_PROTOCOLLAZIONE")
	private LocalDateTime dataProtocollazione;

	@Column(name = "STATO", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private StatoRichiestaCarburante stato;

	@Column(name="GASOLIO", length = 7)
	private Integer gasolio;

	@Column(name="BENZINA", length = 7)
	private Integer benzina;

	@Column(name="GASOLIO_SERRE", length = 7)
	private Integer gasolioSerre;

	@Column(name="GASOLIO_TERZI", length = 7)
	private Integer gasolioTerzi;

	@Column(name="NOTE", length = 200)
	private String note;

//	@Lob
//	@Column(name="DOCUMENTO")
//	private byte[] documento;

	@Column(name="DENOMINAZIONE", length = 200)
	private String denominazione;

	@Column(name="FIRMA", nullable = true)
	private Boolean firma;

	// Denominazione dello sportello che al momento della creazione/protocollazione della domanda detiene il fascicolo
	@Column(name="ENTE_PRESENTATORE", length = 100)
	private String entePresentatore;
	
	@Column(name="NOME_FILE", nullable = false, length = 50)
	private String nomeFile;

	@OneToMany(mappedBy = "richiestaCarburante", fetch = FetchType.LAZY)
	private List<UtilizzoMacchinariModel> macchine;

	@OneToMany(mappedBy = "richiestaCarburante", fetch = FetchType.LAZY)
	private List<FabbisognoModel> fabbisogni;

	@OneToMany(mappedBy = "richiestaCarburante", fetch = FetchType.LAZY)
	private List<PrelievoModel> prelievi;

	@OneToMany(mappedBy = "richiestaCarburante", fetch = FetchType.LAZY)
	private List<FabbricatoModel> fabbricati;

	@OneToMany(mappedBy = "richiestaCarburante", fetch = FetchType.LAZY)
	private List<SuperficieMassimaModel> superficiMassime;

	@OneToOne(mappedBy = "richiestaCarburante", fetch = FetchType.LAZY, optional = true)
	private DichiarazioneConsumiModel dichiarazioneConsumi;

	public String getCfRichiedente() {
		return this.cfRichiedente;
	}
	public RichiestaCarburanteModel setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
		return this;
	}
	public String getCuaa() {
		return this.cuaa;
	}
	public RichiestaCarburanteModel setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getProtocollo() {
		return this.protocollo;
	}
	public RichiestaCarburanteModel setProtocollo(String protocollo) {
		this.protocollo = protocollo;
		return this;
	}
	public Long getCampagna() {
		return campagna;
	}
	public RichiestaCarburanteModel setCampagna(Long campagna) {
		this.campagna = campagna;
		return this;
	}
	public LocalDateTime getDataPresentazione() {
		return dataPresentazione;
	}
	public RichiestaCarburanteModel setDataPresentazione(LocalDateTime dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
		return this;
	}
	public LocalDateTime getDataProtocollazione() {
		return dataProtocollazione;
	}
	public RichiestaCarburanteModel setDataProtocollazione(LocalDateTime dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
		return this;
	}
	public StatoRichiestaCarburante getStato() {
		return stato;
	}
	public RichiestaCarburanteModel setStato(StatoRichiestaCarburante stato) {
		this.stato = stato;
		return this;
	}
	public List<UtilizzoMacchinariModel> getMacchine() {
		return macchine;
	}
	public RichiestaCarburanteModel setMacchine(List<UtilizzoMacchinariModel> macchine) {
		this.macchine = macchine;
		return this;
	}
	public List<FabbisognoModel> getFabbisogni() {
		return fabbisogni;
	}
	public RichiestaCarburanteModel setFabbisogni(List<FabbisognoModel> fabbisogni) {
		this.fabbisogni = fabbisogni;
		return this;
	}
	public Integer getGasolio() {
		return gasolio;
	}
	public RichiestaCarburanteModel setGasolio(Integer gasolio) {
		this.gasolio = gasolio;
		return this;
	}
	public Integer getBenzina() {
		return benzina;
	}
	public RichiestaCarburanteModel setBenzina(Integer benzina) {
		this.benzina = benzina;
		return this;
	}
	public Integer getGasolioSerre() {
		return gasolioSerre;
	}
	public RichiestaCarburanteModel setGasolioSerre(Integer gasolioSerre) {
		this.gasolioSerre = gasolioSerre;
		return this;
	}
	public Integer getGasolioTerzi() {
		return gasolioTerzi;
	}
	public RichiestaCarburanteModel setGasolioTerzi(Integer gasolioTerzi) {
		this.gasolioTerzi = gasolioTerzi;
		return this;
	}
	public String getNote() {
		return note;
	}
	public RichiestaCarburanteModel setNote(String note) {
		this.note = note;
		return this;
	}

	public String getNomeFile() {
		return nomeFile;
	}
	public RichiestaCarburanteModel setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
		return this;
	}
	
//	public byte[] getDocumento() {
//		return documento;
//	}
//	public RichiestaCarburanteModel setDocumento(byte[] documento) {
//		this.documento = documento;
//		return this;
//	}
	public List<PrelievoModel> getPrelievi() {
		return prelievi;
	}
	public RichiestaCarburanteModel setPrelievi(List<PrelievoModel> prelievi) {
		this.prelievi = prelievi;
		return this;
	}
	public List<FabbricatoModel> getFabbricati() {
		return fabbricati;
	}
	public RichiestaCarburanteModel setFabbricati(List<FabbricatoModel> fabbricati) {
		this.fabbricati = fabbricati;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public RichiestaCarburanteModel setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public Boolean getFirma() {
		return firma;
	}
	public RichiestaCarburanteModel setFirma(Boolean firma) {
		this.firma = firma;
		return this;
	}
	public List<SuperficieMassimaModel> getSuperficiMassime() {
		return superficiMassime;
	}
	public void setSuperficiMassime(List<SuperficieMassimaModel> superficiMassime) {
		this.superficiMassime = superficiMassime;
	}
	public String getEntePresentatore() {
		return entePresentatore;
	}
	public RichiestaCarburanteModel setEntePresentatore(String entePresentatore) {
		this.entePresentatore = entePresentatore;
		return this;
	}
	public DichiarazioneConsumiModel getDichiarazioneConsumi() {
		return dichiarazioneConsumi;
	}
	public RichiestaCarburanteModel setDichiarazioneConsumi(DichiarazioneConsumiModel dichiarazioneConsumi) {
		this.dichiarazioneConsumi = dichiarazioneConsumi;
		return this;
	}

}
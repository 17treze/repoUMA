package it.tndigitale.a4g.uma.dto;

import java.time.LocalDateTime;

import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;

public class DomandaUmaDto {

	private Long id;
	private String cuaa;
	private Long campagna;
	private String stato;
	private String denominazione;
	private String protocollo;
	private LocalDateTime dataPresentazione;
	private LocalDateTime dataProtocollazione;
	// denominazione dello sportello che *ha creato o protocollato* la domanda uma - NON quello che ne detiene il mandato!
	private String ente; 
	private TipoDocumentoUma tipo;

	public DomandaUmaDto() {}

	public DomandaUmaDto(Long id, Long campagna, StatoRichiestaCarburante stato,String cuaa, String denominazione, String protocollo, LocalDateTime dataPresentazione, LocalDateTime dataProtocollazione, String ente) {
		this.id = id;
		this.cuaa = cuaa;
		this.campagna = campagna;
		this.stato = stato.name();
		this.denominazione = denominazione;
		this.protocollo = protocollo;
		this.dataPresentazione = dataPresentazione;
		this.dataProtocollazione = dataProtocollazione;
		this.ente = ente;
	}

	public DomandaUmaDto(Long id, Long campagna, StatoDichiarazioneConsumi stato,String cuaa, String denominazione, String protocollo, LocalDateTime dataPresentazione, LocalDateTime dataProtocollazione, String ente) {
		this.id = id;
		this.cuaa = cuaa;
		this.campagna = campagna;
		this.stato = stato.name();
		this.denominazione = denominazione;
		this.protocollo = protocollo;
		this.dataPresentazione = dataPresentazione;
		this.dataProtocollazione = dataProtocollazione;
		this.ente = ente;
	}

	public DomandaUmaDto(Long campagna, String cuaa, LocalDateTime dataPresentazione) {
		this.cuaa = cuaa;
		this.campagna = campagna;
		this.dataPresentazione = dataPresentazione;
	}

	public DomandaUmaDto(Long id, String cuaa, String denominazione, String ente) {
		this.id = id;
		this.cuaa = cuaa;
		this.denominazione = denominazione;
		this.ente = ente;
	}

	public Long getId() {
		return id;
	}
	public DomandaUmaDto setId(Long id) {
		this.id = id;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public DomandaUmaDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public Long getCampagna() {
		return campagna;
	}
	public DomandaUmaDto setCampagna(Long campagna) {
		this.campagna = campagna;
		return this;
	}
	public String getStato() {
		return stato;
	}
	public DomandaUmaDto setStato(String stato) {
		this.stato = stato;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public DomandaUmaDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public DomandaUmaDto setProtocollo(String protocollo) {
		this.protocollo = protocollo;
		return this;
	}
	public LocalDateTime getDataPresentazione() {
		return dataPresentazione;
	}
	public DomandaUmaDto setDataPresentazione(LocalDateTime dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
		return this;
	}
	public LocalDateTime getDataProtocollazione() {
		return dataProtocollazione;
	}
	public DomandaUmaDto setDataProtocollazione(LocalDateTime dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
		return this;
	}
	public String getEnte() {
		return ente;
	}
	public DomandaUmaDto setEnte(String ente) {
		this.ente = ente;
		return this;
	}
	public TipoDocumentoUma getTipo() {
		return tipo;
	}
	public DomandaUmaDto setTipo(TipoDocumentoUma tipo) {
		this.tipo = tipo;
		return this;
	}
}

package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_REVOCA_IMMEDIATA")
public class RevocaImmediataModel extends EntitaDominio {

	private static final long serialVersionUID = 2261567843022158179L;

	@Column(name = "CODICE_FISCALE", length = 16, nullable = false , unique = false)
	private String codiceFiscale;
	
	@Column(name = "DATA_SOTTOSCRIZIONE", nullable = false)
	private LocalDate dataSottoscrizione;
	
	@Column(name = "STATO", length = 30, nullable = false)
	@Enumerated(EnumType.STRING)
	private StatoRevocaImmediata stato;
	
	@Column(name = "ID_PROTOCOLLO", length = 255, nullable = false)
	private String idProtocollo;
	
	@Column(name = "DT_PROTOCOLLO", nullable = false)
	private LocalDate dtProtocollo;
	
	@Column(name = "CAUSA_RICHIESTA", nullable = true)
	private String causaRichiesta;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "RICHIESTA_FIRMATA", nullable = true)
	private byte[] richiestaFirmata;

	@Column(name = "DATA_VALUTAZIONE", nullable = false)
	private LocalDate dataValutazione;
	
	@Column(name = "MOTIVAZIONE_RIFIUTO", length = 4000, nullable = false , unique = false)
	private String motivazioneRifiuto;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="ID_MANDATO", referencedColumnName = "ID" )
    @JoinColumn(name="MANDATO_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private MandatoModel mandato;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SPORTELLO_DICHIARATO")
	private SportelloModel sportello;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public RevocaImmediataModel setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public LocalDate getDataSottoscrizione() {
		return dataSottoscrizione;
	}

	public RevocaImmediataModel setDataSottoscrizione(LocalDate dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
		return this;
	}

	public StatoRevocaImmediata getStato() {
		return stato;
	}

	public RevocaImmediataModel setStato(StatoRevocaImmediata stato) {
		this.stato = stato;
		return this;
	}

	public MandatoModel getMandato() {
		return mandato;
	}

	public RevocaImmediataModel setMandato(MandatoModel mandato) {
		this.mandato = mandato;
		return this;
	}
	
	public SportelloModel getSportello() {
		return sportello;
	}

	public RevocaImmediataModel setSportello(SportelloModel sportello) {
		this.sportello = sportello;
		return this;
	}

	public String getIdProtocollo() {
		return idProtocollo;
	}

	public RevocaImmediataModel setIdProtocollo(String idProtocollo) {
		this.idProtocollo = idProtocollo;
		return this;
	}

	public LocalDate getDtProtocollo() {
		return dtProtocollo;
	}

	public RevocaImmediataModel setDtProtocollo(LocalDate dtProtocollo) {
		this.dtProtocollo = dtProtocollo;
		return this;
	}

	public String getCausaRichiesta() {
		return causaRichiesta;
	}

	public void setCausaRichiesta(final String causaRichiesta) {
		this.causaRichiesta = causaRichiesta;
	}

	public byte[] getRichiestaFirmata() {
		return richiestaFirmata;
	}

	public void setRichiestaFirmata(byte[] richiestaFirmata) {
		this.richiestaFirmata = richiestaFirmata;
	}
	
	public String getMotivazioneRifiuto() {
		return motivazioneRifiuto;
	}

	public void setMotivazioneRifiuto(String motivazioneRifiuto) {
		this.motivazioneRifiuto = motivazioneRifiuto;
	}

	public LocalDate getDataValutazione() {
		return dataValutazione;
	}

	public void setDataValutazione(LocalDate dataValutazione) {
		this.dataValutazione = dataValutazione;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(richiestaFirmata);
		result = prime * result + Objects.hash(causaRichiesta, codiceFiscale, dataSottoscrizione, dataValutazione,
				dtProtocollo, idProtocollo, mandato, sportello, motivazioneRifiuto, stato);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RevocaImmediataModel other = (RevocaImmediataModel) obj;
		return Objects.equals(causaRichiesta, other.causaRichiesta)
				&& Objects.equals(codiceFiscale, other.codiceFiscale)
				&& Objects.equals(dataSottoscrizione, other.dataSottoscrizione)
				&& Objects.equals(dataValutazione, other.dataValutazione)
				&& Objects.equals(dtProtocollo, other.dtProtocollo) && Objects.equals(idProtocollo, other.idProtocollo)
				&& Objects.equals(mandato, other.mandato)
				&& Objects.equals(sportello, other.sportello)
				&& Objects.equals(motivazioneRifiuto, other.motivazioneRifiuto)
				&& Arrays.equals(richiestaFirmata, other.richiestaFirmata) && stato == other.stato;
	}

	
}

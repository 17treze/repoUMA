package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaAziendaException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloAllaFirmaCAAException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloChiusoException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloInValidazioneException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloSospesoException;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloStatoReadOnlyException;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

@Entity
@Table(name = "A4GT_DOCUMENTO_IDENTITA")
public class DocumentoIdentitaModel extends EntitaDominioFascicolo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -891985292096161506L;

	@Column(name = "CODICE_FISCALE", length = 16, nullable = false)
	private String codiceFiscale;
	
	@Column(name = "NUMERO",  length = 100, nullable = false)
	private String numero;
	
	@Column(name = "TIPOLOGIA", length = 100, nullable = false)
	private String tipologia;
	
    @Column(name = "DATA_RILASCIO", nullable = false)
    private LocalDate dataRilascio;

    @Column(name = "DATA_SCADENZA")
    private LocalDate dataScadenza;
    
    @Lob
    @Column(name = "DOCUMENTO", nullable = false)
	private byte[] documento;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="FASCICOLO_ID", referencedColumnName = "ID" )
    @JoinColumn(name="FASCICOLO_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private FascicoloModel fascicolo;

	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public DocumentoIdentitaModel setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getNumero() {
		return numero;
	}

	public DocumentoIdentitaModel setNumero(String numero) {
		this.numero = numero;
		return this;
	}

	public String getTipologia() {
		return tipologia;
	}

	public DocumentoIdentitaModel setTipologia(String tipologia) {
		this.tipologia = tipologia;
		return this;
	}

	public LocalDate getDataRilascio() {
		return dataRilascio;
	}

	public DocumentoIdentitaModel setDataRilascio(LocalDate dataRilascio) {
		this.dataRilascio = dataRilascio;
		return this;
	}

	public LocalDate getDataScadenza() {
		return dataScadenza;
	}

	public DocumentoIdentitaModel setDataScadenza(LocalDate dataScadenza) {
		this.dataScadenza = dataScadenza;
		return this;
	}

	public byte[] getDocumento() {
		return documento;
	}

	public DocumentoIdentitaModel setDocumento(byte[] documento) {
		this.documento = documento;
		return this;
	}

	public FascicoloModel getFascicolo() {
		return fascicolo;
	}

	public DocumentoIdentitaModel setFascicolo(FascicoloModel fascicolo) {
		this.fascicolo = fascicolo;
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(documento);
		result = prime * result + Objects.hash(codiceFiscale, dataRilascio, dataScadenza, fascicolo, numero, tipologia);
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
		DocumentoIdentitaModel other = (DocumentoIdentitaModel) obj;
		return Objects.equals(codiceFiscale, other.codiceFiscale) && Objects.equals(dataRilascio, other.dataRilascio)
				&& Objects.equals(dataScadenza, other.dataScadenza) && Arrays.equals(documento, other.documento)
				&& Objects.equals(fascicolo, other.fascicolo) && Objects.equals(numero, other.numero)
				&& Objects.equals(tipologia, other.tipologia);
	}

	@PreUpdate
	@PreRemove
	public void preventUpdateInValidazione() throws FascicoloStatoReadOnlyException {
	    if (fascicolo.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
	    	throw new FascicoloAllaFirmaAziendaException();
	    } else if (fascicolo.getStato().equals(StatoFascicoloEnum.ALLA_FIRMA_CAA)) {
	    	throw new FascicoloAllaFirmaCAAException();
	    } else if (fascicolo.getStato().equals(StatoFascicoloEnum.IN_VALIDAZIONE)) {
	    	throw new FascicoloInValidazioneException();
	    } else if (fascicolo.getStatoPrecedente().equals(StatoFascicoloEnum.SOSPESO)) {
	    	throw new FascicoloSospesoException();
		} else if (fascicolo.getStatoPrecedente().equals(StatoFascicoloEnum.CHIUSO)) {
			throw new FascicoloChiusoException();
	    }
	    if (this.fascicolo.getIdValidazione() == 0 && this.fascicolo.getStato().equals(StatoFascicoloEnum.VALIDATO)) {
		    	this.fascicolo.setStato(StatoFascicoloEnum.IN_AGGIORNAMENTO);
	    }
	}
	
	@PrePersist
	public void updateStatoFascicolo() {
		if (this.fascicolo.getIdValidazione() == 0 && this.fascicolo.getStato().equals(StatoFascicoloEnum.VALIDATO)) {
	    	this.fascicolo.setStato(StatoFascicoloEnum.IN_AGGIORNAMENTO);
	    }
	}
}

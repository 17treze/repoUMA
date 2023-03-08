package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "A4GT_MODO_PAGAMENTO")
public class ModoPagamentoModel extends EntitaDominioFascicolo {
	private static final long serialVersionUID = -3034546771084462014L;

	@Column(name = "IBAN", length = 30, nullable = false , unique = true)
	private String iban;
	
	@Column(name = "BIC",  length = 11, nullable = true)
	private String bic;
	
	@Column(name = "DENOMINAZIONE_ISTITUTO", length = 400, nullable = true)
	private String denominazioneIstituto;
	
	@Column(name = "DENOMINAZIONE_FILIALE", length = 400, nullable = true)
	private String denominazioneFiliale;
	
	@Column(name = "CITTA_FILIALE", length = 200, nullable = true)
	private String cittaFiliale; 
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="ID_FASCICOLO", referencedColumnName = "ID" )
    @JoinColumn(name="FASCICOLO_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
	private FascicoloModel fascicolo;

	public String getIban() {
		return iban;
	}

	public ModoPagamentoModel setIban(String iban) {
		this.iban = iban;
		return this;
	}

	public String getBic() {
		return bic;
	}

	public ModoPagamentoModel setBic(String bic) {
		this.bic = bic;
		return this;
	}

	public String getDenominazioneIstituto() {
		return denominazioneIstituto;
	}

	public void setDenominazioneIstituto(String denominazioneIstituto) {
		this.denominazioneIstituto = denominazioneIstituto;
	}

	public String getDenominazioneFiliale() {
		return denominazioneFiliale;
	}

	public void setDenominazioneFiliale(String denominazioneFiliale) {
		this.denominazioneFiliale = denominazioneFiliale;
	}

	public String getCittaFiliale() {
		return cittaFiliale;
	}

	public void setCittaFiliale(String cittaFiliale) {
		this.cittaFiliale = cittaFiliale;
	}

	public FascicoloModel getFascicolo() {
		return fascicolo;
	}

	public ModoPagamentoModel setFascicolo(FascicoloModel fascicolo) {
		this.fascicolo = fascicolo;
		return this;
	}
 
	@Override
	public int hashCode() {
		return Objects.hash(bic, cittaFiliale, denominazioneFiliale, denominazioneIstituto, fascicolo, iban);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModoPagamentoModel other = (ModoPagamentoModel) obj;
		return Objects.equals(bic, other.bic) && Objects.equals(cittaFiliale, other.cittaFiliale)
				&& Objects.equals(denominazioneFiliale, other.denominazioneFiliale)
				&& Objects.equals(denominazioneIstituto, other.denominazioneIstituto)
				&& Objects.equals(fascicolo, other.fascicolo) && Objects.equals(iban, other.iban);
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

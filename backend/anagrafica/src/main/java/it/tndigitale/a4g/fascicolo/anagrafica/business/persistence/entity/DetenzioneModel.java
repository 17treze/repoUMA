package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "A4GT_DETENZIONE")
public abstract class DetenzioneModel extends EntitaDominioFascicolo {
    private static final long serialVersionUID = -7861541135228155507L;

    @Column(name = "DATA_INIZIO", nullable = false)
    private LocalDate dataInizio;

    @Column(name = "DATA_FINE")
    private LocalDate dataFine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_FASCICOLO", referencedColumnName = "ID")
    @JoinColumn(name="FASCICOLO_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE")
    private FascicoloModel fascicolo;

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public DetenzioneModel setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
        return this;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public DetenzioneModel setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
        return this;
    }

    public FascicoloModel getFascicolo() {
        return fascicolo;
    }

    public DetenzioneModel setFascicolo(FascicoloModel fascicolo) {
        this.fascicolo = fascicolo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetenzioneModel that = (DetenzioneModel) o;
        return Objects.equals(dataInizio, that.dataInizio) &&
                Objects.equals(dataFine, that.dataFine) &&
                Objects.equals(fascicolo, that.fascicolo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataInizio, dataFine, fascicolo);
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
	    if(this.fascicolo.getIdValidazione() == 0 && this.fascicolo.getStato().equals(StatoFascicoloEnum.VALIDATO)) {
		    	this.fascicolo.setStato(StatoFascicoloEnum.IN_AGGIORNAMENTO);
	    }
	}
}

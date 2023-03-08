package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_FASCICOLO_ORGANIZZAZIONE")
public class FascicoloOrganizzazioneModel extends EntitaDominio {

	private static final long serialVersionUID = 7152937672557253754L;

	@Column(name = "DT_INIZIO_ASSOCIAZIONE")
    private LocalDate dataInizioAssociazione;

    @Column(name = "DT_FINE_ASSOCIAZIONE")
    private LocalDate dataFineAssociazione;
    
    @Column(name = "DT_INSERIMENTO_ASSOCIAZIONE")
    private LocalDateTime dataInserimentoAssociazione;

    @Column(name = "DT_CANCELLAZIONE_ASSOCIAZIONE")
    private LocalDateTime dataCancellazioneAssociazione;
    
    @Column(name = "UTENTE_INSERIMENTO")
    private String utenteInserimento;

    @Column(name = "UTENTE_CANCELLAZIONE")
    private String utenteCancellazione;
    
    @Column(name = "CUAA", length = 16, nullable = false)
	private String cuaa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ORGANIZZAZIONE_ID", referencedColumnName = "ID")
    private OrganizzazioneModel organizzazione;
   
    public LocalDate getDataInizioAssociazione() {
		return dataInizioAssociazione;
	}

	public FascicoloOrganizzazioneModel setDataInizioAssociazione(LocalDate dataInizioAssociazione) {
		this.dataInizioAssociazione = dataInizioAssociazione;
		return this;
	}

	public LocalDate getDataFineAssociazione() {
		return dataFineAssociazione;
	}

	public FascicoloOrganizzazioneModel setDataFineAssociazione(LocalDate dataFineAssociazione) {
		this.dataFineAssociazione = dataFineAssociazione;
		return this;
	}

	public LocalDateTime getDataInserimentoAssociazione() {
		return dataInserimentoAssociazione;
	}

	public FascicoloOrganizzazioneModel setDataInserimentoAssociazione(LocalDateTime dataInserimentoAssociazione) {
		this.dataInserimentoAssociazione = dataInserimentoAssociazione;
		return this;
	}

	public LocalDateTime getDataCancellazioneAssociazione() {
		return dataCancellazioneAssociazione;
	}

	public FascicoloOrganizzazioneModel setDataCancellazioneAssociazione(LocalDateTime dataCancellazioneAssociazione) {
		this.dataCancellazioneAssociazione = dataCancellazioneAssociazione;
		return this;
	}

	public OrganizzazioneModel getOrganizzazione() {
		return organizzazione;
	}

	public FascicoloOrganizzazioneModel setOrganizzazione(OrganizzazioneModel organizzazione) {
		this.organizzazione = organizzazione;
		return this;
	}
	
	public String getUtenteInserimento() {
		return utenteInserimento;
	}

	public void setUtenteInserimento(String utenteInserimento) {
		this.utenteInserimento = utenteInserimento;
	}

	public String getUtenteCancellazione() {
		return utenteCancellazione;
	}

	public void setUtenteCancellazione(String utenteCancellazione) {
		this.utenteCancellazione = utenteCancellazione;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(cuaa, dataCancellazioneAssociazione, dataFineAssociazione, dataInizioAssociazione,
				dataInserimentoAssociazione, organizzazione, utenteCancellazione, utenteInserimento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FascicoloOrganizzazioneModel other = (FascicoloOrganizzazioneModel) obj;
		return Objects.equals(cuaa, other.cuaa)
				&& Objects.equals(dataCancellazioneAssociazione, other.dataCancellazioneAssociazione)
				&& Objects.equals(dataFineAssociazione, other.dataFineAssociazione)
				&& Objects.equals(dataInizioAssociazione, other.dataInizioAssociazione)
				&& Objects.equals(dataInserimentoAssociazione, other.dataInserimentoAssociazione)
				&& Objects.equals(organizzazione, other.organizzazione)
				&& Objects.equals(utenteCancellazione, other.utenteCancellazione)
				&& Objects.equals(utenteInserimento, other.utenteInserimento);
	}

}

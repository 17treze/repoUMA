package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo;

@Entity
@Table(name = "A4GT_ATTIVITA_ATECO")
public class AttivitaAtecoModel extends EntitaDominioFascicolo {
	private static final long serialVersionUID = -5716288739831740313L;

	private String codice;

    private String descrizione;
   
    @Enumerated(EnumType.STRING)
    private ImportanzaAttivita importanza;

	@Column(name = "FONTE", length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private FonteDatoAnagrafico fonte;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SEDE", referencedColumnName = "ID", nullable = false)
	@JoinColumn(name = "SEDE_ID_VALIDAZIONE", referencedColumnName = "ID_VALIDAZIONE", nullable = false)
	private PersonaModel personaModel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UNITA_TECNICO_ECONOMICHE")
	@JoinColumn(name = "UNITA_TECNICO_ECONOMICHE_IDVAL")
	private UnitaTecnicoEconomicheModel unitaTecnicoEconomiche;

	public String getCodice() {
        return codice;
    }

    public AttivitaAtecoModel setCodice(String codice) {
        this.codice = codice;
        return this;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public AttivitaAtecoModel setDescrizione(String descrizione) {
        this.descrizione = descrizione;
        return this;
    }

	public FonteDatoAnagrafico getFonte() {
		return fonte;
	}

	public AttivitaAtecoModel setFonte(FonteDatoAnagrafico fonte) {
		this.fonte = fonte;
		return this;
	}

	private PersonaModel getPersonaModel() {
		return personaModel;
	}

	public AttivitaAtecoModel setPersonaModel(PersonaModel personaModel) {
		this.personaModel = personaModel;
		return this;
	}

	public ImportanzaAttivita getImportanza() {
		return importanza;
	}

	public void setImportanza(ImportanzaAttivita importanza) {
		this.importanza = importanza;
	}
	
	public UnitaTecnicoEconomicheModel getUnitaTecnicoEconomiche() {
		return unitaTecnicoEconomiche;
	}

	public void setUnitaTecnicoEconomiche(UnitaTecnicoEconomicheModel unitaTecnicoEconomiche) {
		this.unitaTecnicoEconomiche = unitaTecnicoEconomiche;
	}
}

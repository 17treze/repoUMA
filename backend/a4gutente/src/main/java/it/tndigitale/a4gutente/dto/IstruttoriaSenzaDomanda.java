package it.tndigitale.a4gutente.dto;

import static it.tndigitale.a4gutente.utility.ListSupport.intersect;
import static it.tndigitale.a4gutente.utility.ListSupport.isNotEmpty;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gutente.exception.ValidationException;

@ApiModel("Rappresenta il modello utilizzato per creare una istruttoria senza domanda, per l'aggiornamento abilitazione utente (profili e sedi")
public class IstruttoriaSenzaDomanda {

    @ApiParam(value = "Identificativo dell'utente da aggiornare e storicizzare")
    @JsonIgnore
    private Long idUtente;
    @ApiParam(value = "Lista degli identificativi degli enti (sedi) selezionati")
    private List<Long> sedi;
    @ApiParam(value = "Lista i profili da attivare")
    private List<Long> profili;
    @ApiParam(value = "Lista dei profili da disattivare")
    private List<Long> profiliDaDisattivare;
    @ApiParam(value = "Eventuale motivazione della modifica")
    private String variazioneRichiesta;
    @ApiParam(value = "Eventuale motivazione di una disattivazione profili")
    private MotivazioneDisattivazione motivazioneDisattivazione;

    public Long getIdUtente() {
        return idUtente;
    }

    public IstruttoriaSenzaDomanda setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
        return this;
    }

    public List<Long> getSedi() {
        return sedi;
    }

    public IstruttoriaSenzaDomanda setSedi(List<Long> sedi) {
        this.sedi = sedi;
        return this;
    }

    public List<Long> getProfili() {
        return profili;
    }

    public IstruttoriaSenzaDomanda setProfili(List<Long> profili) {
        this.profili = profili;
        return this;
    }

	public String getVariazioneRichiesta() {
		return variazioneRichiesta;
	}

	public void setVariazioneRichiesta(String variazioneRichiesta) {
		this.variazioneRichiesta = variazioneRichiesta;
	}

    public List<Long> getProfiliDaDisattivare() {
        return profiliDaDisattivare;
    }

    public IstruttoriaSenzaDomanda setProfiliDaDisattivare(List<Long> profiliDaDisattivare) {
        this.profiliDaDisattivare = profiliDaDisattivare;
        return this;
    }

    public MotivazioneDisattivazione getMotivazioneDisattivazione() {
        return motivazioneDisattivazione;
    }

    public IstruttoriaSenzaDomanda setMotivazioneDisattivazione(MotivazioneDisattivazione motivazioneDisattivazione) {
        this.motivazioneDisattivazione = motivazioneDisattivazione;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IstruttoriaSenzaDomanda that = (IstruttoriaSenzaDomanda) o;
        return Objects.equals(idUtente, that.idUtente) &&
                Objects.equals(sedi, that.sedi) &&
                Objects.equals(profili, that.profili) &&
                Objects.equals(profiliDaDisattivare, that.profiliDaDisattivare) &&
                Objects.equals(variazioneRichiesta, that.variazioneRichiesta) &&
                motivazioneDisattivazione == that.motivazioneDisattivazione;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUtente, sedi, profili, profiliDaDisattivare, variazioneRichiesta, motivazioneDisattivazione);
    }

    public void valida() {
        Boolean isNotEmptyProfiliDaDisattivare = isNotEmpty(this.profiliDaDisattivare);
        if (
                isNotEmptyProfiliDaDisattivare &&
                isNotEmpty(this.profili) &&
                isNotEmpty(intersect(this.profili, this.profiliDaDisattivare))
        ) {
            throw new ValidationException("Nei profili da disattivare esiste un elemento presente nei profili da attivare");
        }
        if (isNotEmptyProfiliDaDisattivare &&
            isEmpty(this.motivazioneDisattivazione)) {
            throw new ValidationException("Sono presenti profili da disattivare - specificare il motivo della disattivazione");
        }
        if (!isNotEmptyProfiliDaDisattivare &&
            this.motivazioneDisattivazione != null) {
            throw new ValidationException("Non è possibile specificare un motivo di disattivazione se non ci sono profili disattivati");
        }
    }
}

package it.tndigitale.a4gutente.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.support.StringSupport;
import it.tndigitale.a4gutente.exception.ValidationException;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@ApiModel("Rappresenta il modello di una richiesta di rfiuto di una domanda utente")
public class RichiestaDomandaRifiuta {

    @ApiParam(value="Identificativo della domanda", required = true)
    @NotNull
    @JsonIgnore
    private Long idDomanda;
    @ApiParam(value="Testo della mail", required = true)
    @NotNull
    private String testoMail;
    @ApiParam(value="Motivazione rifiuto", required = true)
    @NotNull
    private String motivazioneRifiuto;
    @ApiParam(value="Eventuali note")
    private String note;

    public Long getIdDomanda() {
        return idDomanda;
    }

    public RichiestaDomandaRifiuta setIdDomanda(Long idDomanda) {
        this.idDomanda = idDomanda;
        return this;
    }

    public String getTestoMail() {
        return testoMail;
    }

    public RichiestaDomandaRifiuta setTestoMail(String testoMail) {
        this.testoMail = testoMail;
        return this;
    }

    public String getMotivazioneRifiuto() {
        return motivazioneRifiuto;
    }

    public RichiestaDomandaRifiuta setMotivazioneRifiuto(String motivazioneRifiuto) {
        this.motivazioneRifiuto = motivazioneRifiuto;
        return this;
    }

    public String getNote() {
        return note;
    }

    public RichiestaDomandaRifiuta setNote(String note) {
        this.note = note;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RichiestaDomandaRifiuta that = (RichiestaDomandaRifiuta) o;
        return Objects.equals(idDomanda, that.idDomanda) &&
                Objects.equals(testoMail, that.testoMail) &&
                Objects.equals(motivazioneRifiuto, that.motivazioneRifiuto) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDomanda, testoMail, motivazioneRifiuto, note);
    }

    public void isValid() {
        if (StringSupport.isEmptyOrNullTrim(this.testoMail) || idDomanda == null 
        		|| StringSupport.isEmptyOrNullTrim(this.motivazioneRifiuto)) {
            throw new ValidationException("RichiestaDomandaRifiuta non valida: Testo della mail, identificativo domanda e testo motivazione rifiuto obbligatori");
        }
    }
}

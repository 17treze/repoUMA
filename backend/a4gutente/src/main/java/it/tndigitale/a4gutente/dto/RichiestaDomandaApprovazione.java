package it.tndigitale.a4gutente.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.support.StringSupport;
import it.tndigitale.a4gutente.exception.ValidationException;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@ApiModel("Rappresenta il modello di una richiesta di approvazione di una domanda utente")
public class RichiestaDomandaApprovazione {

    @ApiParam(value="Identificativo della domanda", required = true)
    @NotNull
    @JsonIgnore
    private Long idDomanda;
    @ApiParam(value="Testo della mail", required = true)
    @NotNull
    private String testoMail;
    @ApiParam(value="Eventuali note")
    private String note;

    public Long getIdDomanda() {
        return idDomanda;
    }

    public RichiestaDomandaApprovazione setIdDomanda(Long idDomanda) {
        this.idDomanda = idDomanda;
        return this;
    }

    public String getTestoMail() {
        return testoMail;
    }

    public RichiestaDomandaApprovazione setTestoMail(String testoMail) {
        this.testoMail = testoMail;
        return this;
    }

    public String getNote() {
        return note;
    }

    public RichiestaDomandaApprovazione setNote(String note) {
        this.note = note;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RichiestaDomandaApprovazione that = (RichiestaDomandaApprovazione) o;
        return Objects.equals(idDomanda, that.idDomanda) &&
                Objects.equals(testoMail, that.testoMail) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDomanda, testoMail, note);
    }

    public void isValid() {
        if (StringSupport.isEmptyOrNullTrim(this.testoMail) || this.idDomanda == null) {
            throw new ValidationException("RichiestaDomandaApprovazione non valida: Testo della mail e identificativo domanda obbligatori");
        }
    }

}

package it.tndigitale.a4g.soc.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Objects;

@ApiModel(description = "Rappresenta il modello di un debito")
public class Debito {

    @ApiModelProperty(value = "Importo del debito")
    private BigDecimal importo;
    @ApiModelProperty(value = "Descrizione del capitolo")
    private String descrizioneCapitolo;

    public BigDecimal getImporto() {
        return importo;
    }

    public Debito setImporto(BigDecimal importo) {
        this.importo = importo;
        return this;
    }

    public String getDescrizioneCapitolo() {
        return descrizioneCapitolo;
    }

    public Debito setDescrizioneCapitolo(String descrizioneCapitolo) {
        this.descrizioneCapitolo = descrizioneCapitolo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debito debito = (Debito) o;
        return Objects.equals(importo, debito.importo) &&
                Objects.equals(descrizioneCapitolo, debito.descrizioneCapitolo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(importo, descrizioneCapitolo);
    }
}

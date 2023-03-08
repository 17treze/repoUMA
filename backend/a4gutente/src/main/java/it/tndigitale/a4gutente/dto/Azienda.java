package it.tndigitale.a4gutente.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel(description = "Rappresenta il modello per rappresentare un azienda associata all'utente")
public class Azienda {

    @ApiModelProperty(value = "Identificativo dell'azienda")
    private Long id;
    @ApiModelProperty(value = "Data di aggiornamento dell'azienda")
    private LocalDateTime dataAggiornamento;
    @ApiModelProperty(value = "Identificativo Carica dell'azienda")
    private Long idCarica;
    @ApiModelProperty(value = "Caa dell'azienda")
    private String cuaa;

    public Long getId() {
        return id;
    }

    public Azienda setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDateTime getDataAggiornamento() {
        return dataAggiornamento;
    }

    public Azienda setDataAggiornamento(LocalDateTime dataAggiornamento) {
        this.dataAggiornamento = dataAggiornamento;
        return this;
    }

    public Long getIdCarica() {
        return idCarica;
    }

    public Azienda setIdCarica(Long idCarica) {
        this.idCarica = idCarica;
        return this;
    }

    public String getCuaa() {
        return cuaa;
    }

    public Azienda setCuaa(String cuaa) {
        this.cuaa = cuaa;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Azienda azienda = (Azienda) o;
        return Objects.equals(id, azienda.id) &&
                Objects.equals(dataAggiornamento, azienda.dataAggiornamento) &&
                Objects.equals(idCarica, azienda.idCarica) &&
                Objects.equals(cuaa, azienda.cuaa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataAggiornamento, idCarica, cuaa);
    }

}

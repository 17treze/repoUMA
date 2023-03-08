package it.tndigitale.a4g.soc.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.framework.exception.ValidationException;

import java.math.BigDecimal;

@ApiModel(description = "Rappresenta il filtro per ricerca l'import liquidato di una domanda")
public class ImportoLiquidatoFilter {

    @ApiParam(value = "Numero della domanda", required = true)
    private BigDecimal numeroDomanda;
    @ApiParam(value = "Anno della campagna", required = false)
    private Integer anno;
    @ApiParam(value = "Tipo della domanda", required = true)
    private TipoDomanda tipoDomanda;
    @ApiParam(value = "Id elenco di liquidazione")
    private Long idElencoLiquidazione;
    @ApiParam(value = "CUAA", required = true)
    private String cuaa;
    @ApiParam(value = "Tipo di pagamento", required = false)
    private TipoPagamento tipoPagamento;

    public String getCuaa() {
		return cuaa;
	}

	public ImportoLiquidatoFilter setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

	public BigDecimal getNumeroDomanda() {
        return numeroDomanda;
    }

    public ImportoLiquidatoFilter setNumeroDomanda(BigDecimal numeroDomanda) {
        this.numeroDomanda = numeroDomanda;
        return this;
    }

    public Integer getAnno() {
        return anno;
    }

    public ImportoLiquidatoFilter setAnno(Integer anno) {
        this.anno = anno;
        return this;
    }

    public TipoDomanda getTipoDomanda() {
        return tipoDomanda;
    }

    public ImportoLiquidatoFilter setTipoDomanda(TipoDomanda tipoDomanda) {
        this.tipoDomanda = tipoDomanda;
        return this;
    }

    public Long getIdElencoLiquidazione() {
        return idElencoLiquidazione;
    }

    public ImportoLiquidatoFilter setIdElencoLiquidazione(Long idElencoLiquidazione) {
        this.idElencoLiquidazione = idElencoLiquidazione;
        return this;
    }

    public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

    public void isValid() {
        if (numeroDomanda == null
        		|| cuaa == null
        		|| tipoDomanda == null) {
            throw new ValidationException("Oggetto non valido: numero domanda, anno, tipo domanda sono obbligatori");
        }
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anno == null) ? 0 : anno.hashCode());
		result = prime * result + ((cuaa == null) ? 0 : cuaa.hashCode());
		result = prime * result + ((idElencoLiquidazione == null) ? 0 : idElencoLiquidazione.hashCode());
		result = prime * result + ((numeroDomanda == null) ? 0 : numeroDomanda.hashCode());
		result = prime * result + ((tipoDomanda == null) ? 0 : tipoDomanda.hashCode());
		result = prime * result + ((tipoPagamento == null) ? 0 : tipoPagamento.hashCode());
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
		ImportoLiquidatoFilter other = (ImportoLiquidatoFilter) obj;
		if (anno == null) {
			if (other.anno != null)
				return false;
		} else if (!anno.equals(other.anno))
			return false;
		if (cuaa == null) {
			if (other.cuaa != null)
				return false;
		} else if (!cuaa.equals(other.cuaa))
			return false;
		if (idElencoLiquidazione == null) {
			if (other.idElencoLiquidazione != null)
				return false;
		} else if (!idElencoLiquidazione.equals(other.idElencoLiquidazione))
			return false;
		if (numeroDomanda == null) {
			if (other.numeroDomanda != null)
				return false;
		} else if (!numeroDomanda.equals(other.numeroDomanda))
			return false;
		if (tipoDomanda != other.tipoDomanda)
			return false;
		if (tipoPagamento != other.tipoPagamento)
			return false;
		return true;
	}

}

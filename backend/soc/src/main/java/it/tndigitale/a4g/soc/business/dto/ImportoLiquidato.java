package it.tndigitale.a4g.soc.business.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.framework.support.ListSupport;

@ApiModel(description = "Rappresenta l'importo liquidato all'azienda agricola dal sistema contabile")
public class ImportoLiquidato {
    @ApiModelProperty(value = "Tipo Bilancio")
    private String tipoBilancio;
		
	@ApiModelProperty(value = "Anno esercizio")
    private Integer anno;

	@ApiModelProperty(value = "Progressivo credito")
    private Long progressivo;
	
	@ApiModelProperty(value = "Incassato netto")
    private BigDecimal incassatoNetto;

    @ApiModelProperty(value = "id Elenco Liquidazione")
    private Long idElencoLiquidazione;

    @ApiModelProperty(value = "Casuale/tipologia dell'importo")
    private CausaleImporto causale;

    @ApiModelProperty(value = "Data esecuzione pagamento")
    private LocalDateTime dataEsecuzionePagamento;

    @ApiModelProperty(value = "Lista dei debiti relativi all'importo liquidato")
    private List<Debito> debiti;
    
    @ApiModelProperty(value = "Numero Autorizzazione")
    private String numeroAutorizzazione;
    @ApiModelProperty(value = "Data Autorizzazione")
    private LocalDateTime dataAutorizzazione;
    @ApiModelProperty(value = "Progressivo Pagamento")
    private Long progressivoPagamento;
    
    @ApiModelProperty(value = "Tipo del pagamento")
    private TipoPagamento tipoPagamento;
    
    @ApiModelProperty(value = "Numero domanda / ID Progetto SOC")
    private String numeroDomanda;

    @ApiModelProperty(value = "Codice Prodotto")
    private String codiceProdotto;
    
    @ApiModelProperty(value = "Totale dei debiti recuperati")
    public BigDecimal getTotaleRecuperato() {
		BigDecimal totaleRecuperato = null;
		List<Debito> debitiList = getDebiti();
		if (ListSupport.isNotEmpty(debitiList)) {
			totaleRecuperato = debitiList.stream()
					.map((debito -> debito.getImporto()))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
        return totaleRecuperato;
    }

    public String getTipoBilancio() {
        return tipoBilancio;
    }

    public ImportoLiquidato setTipoBilancio(String tipoBilancio) {
        this.tipoBilancio = tipoBilancio;
        return this;
    }

    public Integer getAnno() {
        return anno;
    }

    public ImportoLiquidato setAnno(Integer anno) {
        this.anno = anno;
        return this;
    }

    public Long getProgressivo() {
        return progressivo;
    }

    public ImportoLiquidato setProgressivo(Long progressivo) {
        this.progressivo = progressivo;
        return this;
    }

    public BigDecimal getIncassatoNetto() {
        return incassatoNetto;
    }

    public ImportoLiquidato setIncassatoNetto(BigDecimal incassatoNetto) {
        this.incassatoNetto = incassatoNetto;
        return this;
    }

    public List<Debito> getDebiti() {
        return debiti;
    }

    public ImportoLiquidato setDebiti(List<Debito> debiti) {
        this.debiti = debiti;
        return this;
    }

	public Long getIdElencoLiquidazione() {
		return idElencoLiquidazione;
	}

	public ImportoLiquidato setIdElencoLiquidazione(Long idElencoLiquidazione) {
		this.idElencoLiquidazione = idElencoLiquidazione;
		return this;
	}

	public LocalDateTime getDataEsecuzionePagamento() {
		return dataEsecuzionePagamento;
	}

	public ImportoLiquidato setDataEsecuzionePagamento(LocalDateTime dataEsecuzionePagamento) {
		this.dataEsecuzionePagamento = dataEsecuzionePagamento;
		return this;
	}

	public CausaleImporto getCausale() {
		return causale;
	}

	public ImportoLiquidato setCausale(CausaleImporto causale) {
		this.causale = causale;
		return this;
	}

	public String getNumeroAutorizzazione() {
		return numeroAutorizzazione;
	}

	public ImportoLiquidato setNumeroAutorizzazione(String numeroAutorizzazione) {
		this.numeroAutorizzazione = numeroAutorizzazione;
		return this;
	}

	public LocalDateTime getDataAutorizzazione() {
		return dataAutorizzazione;
	}

	public ImportoLiquidato setDataAutorizzazione(LocalDateTime dataAutorizzazione) {
		this.dataAutorizzazione = dataAutorizzazione;
		return this;
	}

	public Long getProgressivoPagamento() {
		return progressivoPagamento;
	}

	public ImportoLiquidato setProgressivoPagamento(Long progressivoPagamento) {
		this.progressivoPagamento = progressivoPagamento;
		return this;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public ImportoLiquidato setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
		return this;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public ImportoLiquidato setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
		return this;
	}

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public ImportoLiquidato codiceProdotto(String codiceprodotto) {
        this.codiceProdotto = codiceprodotto;
        return this;
    }

    @Override
	public int hashCode() {
		return Objects
			.hash(anno, causale, dataAutorizzazione, dataEsecuzionePagamento, debiti, idElencoLiquidazione, incassatoNetto, numeroAutorizzazione, progressivo, progressivoPagamento, tipoBilancio, codiceProdotto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj
			.getClass())
			return false;
		ImportoLiquidato other = (ImportoLiquidato) obj;
		return Objects
			.equals(anno, other.anno) && causale == other.causale && Objects
				.equals(dataAutorizzazione, other.dataAutorizzazione) && Objects
					.equals(dataEsecuzionePagamento, other.dataEsecuzionePagamento)
				&& Objects
					.equals(debiti, other.debiti)
				&& Objects
					.equals(idElencoLiquidazione, other.idElencoLiquidazione)
				&& Objects
					.equals(incassatoNetto, other.incassatoNetto)
				&& Objects
					.equals(numeroAutorizzazione, other.numeroAutorizzazione)
				&& Objects
					.equals(progressivo, other.progressivo)
				&& Objects
					.equals(progressivoPagamento, other.progressivoPagamento)
				&& Objects
					.equals(tipoBilancio, other.tipoBilancio)
                && Objects
                .equals(codiceProdotto, other.codiceProdotto);
	}
}

package it.tndigitale.a4gistruttoria.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.specification.RiservaNazionaleEnum;
import it.tndigitale.a4gistruttoria.repository.specification.YesNoEnum;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@ApiModel(description = "Rappresenta il modello dei filtri di ricerca delle istruttorie di pagamento per la domanda unica")
public class IstruttoriaDomandaUnicaFilter implements Serializable {

	private static final long serialVersionUID = -8972470613918097937L;

	@ApiParam(value="Anno della campagna delle istruttorie da ricercare", required = false)
	private Integer campagna;
	
	@ApiParam(value="getSostegno a cui fanno riferimento le istruttorie da ricercare", required = false)
	private Sostegno sostegno;
	
	@ApiParam(value="Tipo di istruttorie da ricercare", required = false)
	private TipoIstruttoria tipo;
	
	@ApiParam(value="Stato delle istruttorie da ricercare", required = false)
	private StatoIstruttoria stato;

	@ApiParam(value="Stato delle domande da ricercare", required = false)
	private StatoDomanda statoDomanda;
	
	@ApiParam(value="CUAA delle istruttorie da ricercare", required = false)
	private String cuaa;
	
	@ApiParam(value="Ragione sociale/denominazione delle istruttorie da ricercare", required = false)
	private String ragioneSociale;
	
	@ApiParam(value="Numero domanda relativa alle istruttorie da ricercare", required = false)
	private Long numeroDomanda;
	
	@ApiParam(value="Filtro istruttoria bloccata YES/NO", required = false)
	private YesNoEnum istruttoriaBloccata;
	
	@ApiParam(value="Filtro errore calcolo YES/NO", required = false)
	private YesNoEnum erroreCalcolo;
	
	@ApiParam(value="Filtro 'giovane' YES/NO", required = false)
	private YesNoEnum giovane;
	
	@ApiParam(value="Filtro 'campione' YES/NO", required = false)
	private YesNoEnum campione;
	
	@ApiParam(value="Filtro 'pascoli' YES/NO", required = false)
	private YesNoEnum pascoli;
	
	@ApiParam(value="Filtro selezione 'riserva nazionale'", required = false)
	private RiservaNazionaleEnum riservaNazionale;
	
	@ApiParam(value="Filtro selezione 'interventi' prende in input una lista di codici AGEA", required = false)
	private List<String> interventi;

	@ApiParam(value = "Filtro delle anomalie")
	private YesNoEnum anomalie;

	@ApiParam(value="Filtro delle anomalie con livelloAnomalia INFO e codice in codiciAnomalieInfo", required = false)
	private List<String> codiciAnomalieInfo;

	@ApiParam(value="Filtro delle anomalie con livelloAnomalia WARNING e codice in codiciAnomalieWarning", required = false)
	private List<String> codiciAnomalieWarning;

	@ApiParam(value="Filtro delle anomalie con livelloAnomalia ERROR e codice in codiciAnomalieError", required = false)
	private List<String> codiciAnomalieError;
	
	@ApiParam(value="Filtro 'integrazione' YES=Integrate/NO=Integrabili", required = false)
	private YesNoEnum integrazione;

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public Sostegno getSostegno() {
		return sostegno;
	}

	public void setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
	}

	public TipoIstruttoria getTipo() {
		return tipo;
	}

	public void setTipo(TipoIstruttoria tipo) {
		this.tipo = tipo;
	}

	public StatoIstruttoria getStato() {
		return stato;
	}

	public void setStato(StatoIstruttoria stato) {
		this.stato = stato;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public Long getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public YesNoEnum getIstruttoriaBloccata() {
		return istruttoriaBloccata;
	}

	public void setIstruttoriaBloccata(YesNoEnum istruttoriaBloccata) {
		this.istruttoriaBloccata = istruttoriaBloccata;
	}

	public YesNoEnum getErroreCalcolo() {
		return erroreCalcolo;
	}

	public void setErroreCalcolo(YesNoEnum erroreCalcolo) {
		this.erroreCalcolo = erroreCalcolo;
	}

	public YesNoEnum getGiovane() {
		return giovane;
	}

	public void setGiovane(YesNoEnum giovane) {
		this.giovane = giovane;
	}

	public YesNoEnum getCampione() {
		return campione;
	}

	public void setCampione(YesNoEnum campione) {
		this.campione = campione;
	}

	public YesNoEnum getPascoli() {
		return pascoli;
	}

	public void setPascoli(YesNoEnum pascoli) {
		this.pascoli = pascoli;
	}

	public RiservaNazionaleEnum getRiservaNazionale() {
		return riservaNazionale;
	}

	public void setRiservaNazionale(RiservaNazionaleEnum riservaNazionale) {
		this.riservaNazionale = riservaNazionale;
	}

	public List<String> getInterventi() {
		return interventi;
	}

	public void setInterventi(List<String> interventi) {
		this.interventi = interventi;
	}

	public YesNoEnum getAnomalie() {
		return anomalie;
	}

	public IstruttoriaDomandaUnicaFilter setAnomalie(YesNoEnum anomalie) {
		this.anomalie = anomalie;
		return this;
	}

	public List<String> getCodiciAnomalieInfo() {
		return codiciAnomalieInfo;
	}

	public IstruttoriaDomandaUnicaFilter setCodiciAnomalieInfo(List<String> codiciAnomalieInfo) {
		this.codiciAnomalieInfo = codiciAnomalieInfo;
		return this;
	}

	public List<String> getCodiciAnomalieWarning() {
		return codiciAnomalieWarning;
	}

	public IstruttoriaDomandaUnicaFilter setCodiciAnomalieWarning(List<String> codiciAnomalieWarning) {
		this.codiciAnomalieWarning = codiciAnomalieWarning;
		return this;
	}

	public List<String> getCodiciAnomalieError() {
		return codiciAnomalieError;
	}

	public IstruttoriaDomandaUnicaFilter setCodiciAnomalieError(List<String> codiciAnomalieError) {
		this.codiciAnomalieError = codiciAnomalieError;
		return this;
	}

	public StatoDomanda getStatoDomanda() {
		return statoDomanda;
	}

	public void setStatoDomanda(StatoDomanda statoDomanda) {
		this.statoDomanda = statoDomanda;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		IstruttoriaDomandaUnicaFilter that = (IstruttoriaDomandaUnicaFilter) o;
		return Objects.equals(campagna, that.campagna) &&
				sostegno == that.sostegno &&
				tipo == that.tipo &&
				stato == that.stato &&
			    statoDomanda == that.statoDomanda &&
				Objects.equals(cuaa, that.cuaa) &&
				Objects.equals(ragioneSociale, that.ragioneSociale) &&
				Objects.equals(numeroDomanda, that.numeroDomanda) &&
				istruttoriaBloccata == that.istruttoriaBloccata &&
				erroreCalcolo == that.erroreCalcolo &&
				giovane == that.giovane &&
				campione == that.campione &&
				pascoli == that.pascoli &&
				riservaNazionale == that.riservaNazionale &&
				Objects.equals(interventi, that.interventi) &&
				Objects.equals(anomalie, that.anomalie) &&
				Objects.equals(codiciAnomalieInfo, that.codiciAnomalieInfo) &&
				Objects.equals(codiciAnomalieWarning, that.codiciAnomalieWarning) &&
				Objects.equals(codiciAnomalieError, that.codiciAnomalieError);
	}

	@Override
	public int hashCode() {
		return Objects.hash(campagna, sostegno, tipo, stato, cuaa, ragioneSociale, numeroDomanda, istruttoriaBloccata, erroreCalcolo, giovane, campione, pascoli, riservaNazionale, interventi, anomalie, codiciAnomalieInfo, codiciAnomalieWarning, codiciAnomalieError, statoDomanda);
	}

	public YesNoEnum getIntegrazione() {
		return integrazione;
	}

	public void setIntegrazione(YesNoEnum integrazione) {
		this.integrazione = integrazione;
	}
}

package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * creazione capo secondo il modello bdn
 * @author s.caccia
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Deprecated // usare CapoDto
public class CapoBdn {
	
	private String intervento;
	
	@JsonProperty("capoId")
	private String capoId;
	@JsonProperty("codice")
	private String codice;
	@JsonProperty("sesso")
	private String sesso;
	@JsonProperty("dtNascita")
	private Date dtNascita;
	@JsonProperty("razzaCodice")
	private String razzaCodice;
	@JsonProperty("dtInizioDetenzione")
	private Date dtInizioDetenzione;
	@JsonProperty("dtFineDetenzione")
	private Date dtFineDetenzione;
	@JsonProperty("aziendaCodice")
	private String aziendaCodice;
	@JsonProperty("allevId")
	private String allevId;
	@JsonProperty("cuaa")
	private String cuaa;
	private String dataCsv;
	private CapoMacellatoBdn capoMacellatoBdn;
	private CapoLatteBdn capoLatteBdn;
	private CapoOvicaprinoBdn capoOvicaprinoBdn;
	
	@JsonProperty("cuaa")
	public String getCuaa() {
		return cuaa;
	}

	@JsonProperty("cuaa")
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}


	public String getIntervento() {
		return intervento;
	}

	public void setIntervento(String intervento) {
		this.intervento = intervento;
	}

	public String getCapoId() {
		return capoId;
	}

	public void setCapoId(String capoId) {
		this.capoId = capoId;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}


	public String getRazzaCodice() {
		return razzaCodice;
	}

	public void setRazzaCodice(String razzaCodice) {
		this.razzaCodice = razzaCodice;
	}

	public String getAziendaCodice() {
		return aziendaCodice;
	}

	public void setAziendaCodice(String aziendaCodice) {
		this.aziendaCodice = aziendaCodice;
	}

	public String getAllevId() {
		return allevId;
	}

	public void setAllevId(String allevId) {
		this.allevId = allevId;
	}

	public Date getDtNascita() {
		return dtNascita;
	}

	public void setDtNascita(Date dtNascita) {
		this.dtNascita = dtNascita;
	}

	public Date getDtInizioDetenzione() {
		return dtInizioDetenzione;
	}

	public void setDtInizioDetenzione(Date dtInizioDetenzione) {
		this.dtInizioDetenzione = dtInizioDetenzione;
	}

	public Date getDtFineDetenzione() {
		return dtFineDetenzione;
	}

	public void setDtFineDetenzione(Date dtFineDetenzione) {
		this.dtFineDetenzione = dtFineDetenzione;
	}

	public String getDataCsv() {
		return dataCsv;
	}

	public void setDataCsv(String dataCsv) {
		this.dataCsv = dataCsv;
	}

	public CapoMacellatoBdn getCapoMacellatoBdn() {
		return capoMacellatoBdn;
	}

	public void setCapoMacellatoBdn(CapoMacellatoBdn capoMacellatoBdn) {
		this.capoMacellatoBdn = capoMacellatoBdn;
	}

	public CapoLatteBdn getCapoLatteBdn() {
		return capoLatteBdn;
	}

	public void setCapoLatteBdn(CapoLatteBdn capoLatteBdn) {
		this.capoLatteBdn = capoLatteBdn;
	}

	public CapoOvicaprinoBdn getCapoOvicaprinoBdn() {
		return capoOvicaprinoBdn;
	}

	public void setCapoOvicaprinoBdn(CapoOvicaprinoBdn capoOvicaprinoBdn) {
		this.capoOvicaprinoBdn = capoOvicaprinoBdn;
	}

}
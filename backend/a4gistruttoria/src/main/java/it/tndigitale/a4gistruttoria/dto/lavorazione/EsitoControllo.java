package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoControllo.LivelloControllo;

@JsonInclude(Include.NON_EMPTY)
public class EsitoControllo {

	private TipoControllo tipoControllo;
	private Boolean esito;
	private BigDecimal valNumber;
	private String valString;
	private String pascolo;
	private LivelloControllo livelloControllo;

	public TipoControllo getTipoControllo() {
		return tipoControllo;
	}

	public EsitoControllo(TipoControllo tipoControllo, Boolean esito) {
		this.tipoControllo = tipoControllo;
		this.esito = esito;
		this.livelloControllo = this.esito != null && this.tipoControllo.getLivelloPositivo() != null && this.tipoControllo.getLivelloNegativo() != null
				? this.esito ? tipoControllo.getLivelloPositivo() : tipoControllo.getLivelloNegativo()
				: null;
	}

	public EsitoControllo(TipoControllo tipoControllo, String valString) {
		this.esito = true;
		this.tipoControllo = tipoControllo;
		this.valString = valString;
		this.livelloControllo = tipoControllo.getLivelloPositivo();
	}

	public EsitoControllo(TipoControllo tipoControllo, Boolean esito, String valString) {
		this.esito = esito;
		this.tipoControllo = tipoControllo;
		this.valString = valString;
		this.livelloControllo = this.esito != null && this.tipoControllo.getLivelloPositivo() != null && this.tipoControllo.getLivelloNegativo() != null
				? this.esito ? tipoControllo.getLivelloPositivo() : tipoControllo.getLivelloNegativo()
				: null;
	}

	public EsitoControllo(TipoControllo tipoControllo) {
		this.tipoControllo = tipoControllo;
	}

	public EsitoControllo() {

	}

	public BigDecimal getValNumber() {
		return valNumber;
	}

	public void setValNumber(BigDecimal valNumber) {
		this.valNumber = valNumber;
	}

	public String getValString() {
		return valString;
	}

	public void setValString(String valString) {
		this.valString = valString;
	}

	public Boolean getEsito() {
		return esito;
	}

	public void setEsito(Boolean esito) {
		this.esito = esito;
	}

	public static void addElement(Map<TipoControllo, EsitoControllo> map, TipoControllo tipo, Boolean esito) {
		map.put(tipo, new EsitoControllo(tipo, esito));
	}

	public String recuperaValoreString() {
		String val = "";

		if (this.getValString() != null && !this.getValString().isEmpty())
			val = this.getValString();
		else if (this.getEsito() != null)
			val = this.getEsito().equals(false) ? "NO" : "SI";
		else if (this.getValNumber() != null)
			val = this.getValNumber().toString();
		return val;
	}

	public LivelloControllo getLivelloControllo() {
		return livelloControllo;
	}

	public void setLivelloControllo(LivelloControllo livelloControllo) {
		this.livelloControllo = livelloControllo;
	}

	public String getPascolo() {
		return pascolo;
	}

	public void setPascolo(String pascolo) {
		this.pascolo = pascolo;
	}

}

package it.tndigitale.a4gistruttoria.repository.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DomandeCollegateExportBdnaCsv {
	
	@JsonProperty("CUAA")
	private String cuaa;
	@JsonProperty("STATO_AMF")
	private String stato;
	@JsonProperty("PROTOCOLLO_AMF")
	private String idProtocollo;
	@JsonProperty("TIPO_DOMANDA_COLLEGATA")
	private String tipoDomanda;
	@JsonProperty("CAMPAGNA")
	private Integer campagna;
	@JsonProperty("IMPORTO_RICHIESTO")
	private BigDecimal importoRichiesto;
	@JsonProperty("PROTOCOLLO_BDNA")
	private String protocollo;
	@JsonProperty("STATO_BDNA")
	private String statoBdna;


	@JsonProperty("DATA_INIZIO_COMPILAZIONE_AMF")
	private String dtInizioCompilazione;
	@JsonProperty("DATA_PROTOCOLLAZIONE_AMF")
	private String dtProtocollazione;
	@JsonProperty("DATA_CARICAMENTO_BDNA")
	private String dtBdnaOp;
	@JsonProperty("DATA_ESITO_BDNA")
	private String dtBdna;
	@JsonProperty("DATA_INIZIO_SILENZIO_ASSENSO")
	private String dtInizioSilenzioAssenso;
	@JsonProperty("DATA_FINE_SILENZIO_ASSENSO")
	private String dtFineSilenzioAssenso;
	@JsonProperty("DATA_INIZIO_ESITO_NEGATIVO")
	private String dtInizioEsitoNegativo;
	@JsonProperty("DATA_FINE_ESITO_NEGATIVO")
	private String dtFineEsitoNegativo;

	public String getCuaa() {
		return cuaa;
	}

	public DomandeCollegateExportBdnaCsv setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

	public String getStato() {
		return stato;
	}

	public DomandeCollegateExportBdnaCsv setStato(String stato) {
		this.stato = stato;
		return this;
	}

	public String getIdProtocollo() {
		return idProtocollo;
	}

	public DomandeCollegateExportBdnaCsv setIdProtocollo(String idProtocollo) {
		this.idProtocollo = idProtocollo;
		return this;
	}

	public String getTipoDomanda() {
		return tipoDomanda;
	}

	public DomandeCollegateExportBdnaCsv setTipoDomanda(String tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
		return this;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public DomandeCollegateExportBdnaCsv setCampagna(Integer campagna) {
		this.campagna = campagna;
		return this;
	}

	public BigDecimal getImportoRichiesto() {
		return importoRichiesto;
	}

	public DomandeCollegateExportBdnaCsv setImportoRichiesto(BigDecimal importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
		return this;
	}

	public String getProtocollo() {
		return protocollo;
	}

	public DomandeCollegateExportBdnaCsv setProtocollo(String protocollo) {
		this.protocollo = protocollo;
		return this;
	}

	public String getStatoBdna() {
		return statoBdna;
	}

	public DomandeCollegateExportBdnaCsv setStatoBdna(String statoBdna) {
		this.statoBdna = statoBdna;
		return this;
	}

	public String getDtInizioCompilazione() {
		return dtInizioCompilazione;
	}

	public DomandeCollegateExportBdnaCsv setDtInizioCompilazione(String dtInizioCompilazione) {
		this.dtInizioCompilazione = dtInizioCompilazione;
		return this;
	}

	public String getDtProtocollazione() {
		return dtProtocollazione;
	}

	public DomandeCollegateExportBdnaCsv setDtProtocollazione(String dtProtocollazione) {
		this.dtProtocollazione = dtProtocollazione;
		return this;
	}

	public String getDtBdnaOp() {
		return dtBdnaOp;
	}

	public DomandeCollegateExportBdnaCsv setDtBdnaOp(String dtBdnaOp) {
		this.dtBdnaOp = dtBdnaOp;
		return this;
	}

	public String getDtBdna() {
		return dtBdna;
	}

	public DomandeCollegateExportBdnaCsv setDtBdna(String dtBdna) {
		this.dtBdna = dtBdna;
		return this;
	}

	public String getDtInizioSilenzioAssenso() {
		return dtInizioSilenzioAssenso;
	}

	public DomandeCollegateExportBdnaCsv setDtInizioSilenzioAssenso(String dtInizioSilenzioAssenso) {
		this.dtInizioSilenzioAssenso = dtInizioSilenzioAssenso;
		return this;
	}

	public String getDtFineSilenzioAssenso() {
		return dtFineSilenzioAssenso;
	}

	public DomandeCollegateExportBdnaCsv setDtFineSilenzioAssenso(String dtFineSilenzioAssenso) {
		this.dtFineSilenzioAssenso = dtFineSilenzioAssenso;
		return this;
	}

	public String getDtInizioEsitoNegativo() {
		return dtInizioEsitoNegativo;
	}

	public DomandeCollegateExportBdnaCsv setDtInizioEsitoNegativo(String dtInizioEsitoNegativo) {
		this.dtInizioEsitoNegativo = dtInizioEsitoNegativo;
		return this;
	}

	public String getDtFineEsitoNegativo() {
		return dtFineEsitoNegativo;
	}

	public DomandeCollegateExportBdnaCsv setDtFineEsitoNegativo(String dtFineEsitoNegativo) {
		this.dtFineEsitoNegativo = dtFineEsitoNegativo;
		return this;
	}
}

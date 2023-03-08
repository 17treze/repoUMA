package it.tndigitale.a4g.ags.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CatastoPianoColture {

	private String codiceFiscale;
	private Long catComuneCo;
	private String catComuneDs;
	private String codNazionale;
	private String sezione;
	private String comune;
	private Long foglio;
	private String particella;
	private String sub;
	private String titoloPosDe;
	private float percPossesso;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dataInizioCond;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dataFineCond;
	private BigDecimal supGis;
	private String codUtilizzo;
	private String codColtura;
	private String codVarieta;
	private String deColtura;
	private BigDecimal supDichiarata;
	private Long tipoDocumento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date scadenzaDocDt;
	private BigDecimal idParticella;
	private String protocolloDoc;
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public Long getCatComuneCo() {
		return catComuneCo;
	}
	public void setCatComuneCo(Long catComuneCo) {
		this.catComuneCo = catComuneCo;
	}
	public String getCatComuneDs() {
		return catComuneDs;
	}
	public void setCatComuneDs(String catComuneDs) {
		this.catComuneDs = catComuneDs;
	}
	public String getCodNazionale() {
		return codNazionale;
	}
	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public Long getFoglio() {
		return foglio;
	}
	public void setFoglio(Long foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getTitoloPosDe() {
		return titoloPosDe;
	}
	public void setTitoloPosDe(String titoloPosDe) {
		this.titoloPosDe = titoloPosDe;
	}
	public float getPercPossesso() {
		return percPossesso;
	}
	public void setPercPossesso(float percPossesso) {
		this.percPossesso = percPossesso;
	}
	public Date getDataInizioCond() {
		return dataInizioCond;
	}
	public void setDataInizioCond(Date dataInizioCond) {
		this.dataInizioCond = dataInizioCond;
	}
	public Date getDataFineCond() {
		return dataFineCond;
	}
	public void setDataFineCond(Date dataFineCond) {
		this.dataFineCond = dataFineCond;
	}
	public BigDecimal getSupGis() {
		return supGis;
	}
	public void setSupGis(BigDecimal supGis) {
		this.supGis = supGis;
	}
	public String getCodUtilizzo() {
		return codUtilizzo;
	}
	public void setCodUtilizzo(String codUtilizzo) {
		this.codUtilizzo = codUtilizzo;
	}
	public String getCodColtura() {
		return codColtura;
	}
	public void setCodColtura(String codColtura) {
		this.codColtura = codColtura;
	}
	public String getCodVarieta() {
		return codVarieta;
	}
	public void setCodVarieta(String codVarieta) {
		this.codVarieta = codVarieta;
	}
	public String getDeColtura() {
		return deColtura;
	}
	public void setDeColtura(String deColtura) {
		this.deColtura = deColtura;
	}
	public BigDecimal getSupDichiarata() {
		return supDichiarata;
	}
	public void setSupDichiarata(BigDecimal supDichiarata) {
		this.supDichiarata = supDichiarata;
	}
	public Long getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(Long tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public Date getScadenzaDocDt() {
		return scadenzaDocDt;
	}
	public void setScadenzaDocDt(Date scadenzaDocDt) {
		this.scadenzaDocDt = scadenzaDocDt;
	}
	public BigDecimal getIdParticella() {
		return idParticella;
	}
	public void setIdParticella(BigDecimal idParticella) {
		this.idParticella = idParticella;
	}
	public String getProtocolloDoc() {
		return protocolloDoc;
	}
	public void setProtocolloDoc(String protocolloDoc) {
		this.protocolloDoc = protocolloDoc;
	}
}

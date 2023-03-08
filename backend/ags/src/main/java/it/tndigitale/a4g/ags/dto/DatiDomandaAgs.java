/**
 * 
 */
package it.tndigitale.a4g.ags.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author A2AC0147
 *
 */
public class DatiDomandaAgs {

	private BigDecimal pkid;
	private BigDecimal idDomanda;
	private BigDecimal idSoggetto;
	private BigDecimal idModulo;
	private BigDecimal idDomandaRettificata;
	private String scoStato;
	private Date dtInsert;
	private Date dtDelete;
	private BigDecimal groupId;

	public BigDecimal getPkid() {
		return pkid;
	}

	public void setPkid(BigDecimal pkid) {
		this.pkid = pkid;
	}

	public BigDecimal getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(BigDecimal idDomanda) {
		this.idDomanda = idDomanda;
	}

	public BigDecimal getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(BigDecimal idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public BigDecimal getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(BigDecimal idModulo) {
		this.idModulo = idModulo;
	}

	public BigDecimal getIdDomandaRettificata() {
		return idDomandaRettificata;
	}

	public void setIdDomandaRettificata(BigDecimal idDomandaRettificata) {
		this.idDomandaRettificata = idDomandaRettificata;
	}

	public String getScoStato() {
		return scoStato;
	}

	public void setScoStato(String scoStato) {
		this.scoStato = scoStato;
	}

	public Date getDtInsert() {
		return dtInsert;
	}

	public void setDtInsert(Date dtInsert) {
		this.dtInsert = dtInsert;
	}

	public Date getDtDelete() {
		return dtDelete;
	}

	public void setDtDelete(Date dtDelete) {
		this.dtDelete = dtDelete;
	}

	public BigDecimal getGroupId() {
		return groupId;
	}

	public void setGroupId(BigDecimal groupId) {
		this.groupId = groupId;
	}

}

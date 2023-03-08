package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "A4GT_CONF_ISTR_ZOOTECNIA")
@NamedQuery(name = "ConfigurazioneIstruttoriaZootecniaModel.findAll", query = "SELECT a FROM ConfigurazioneIstruttoriaZootecniaModel a")
public class ConfigurazioneIstruttoriaZootecniaModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 3248483660360876480L;

	@Column(name = "ANNO_CAMPAGNA")
	private Integer campagna;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_APERTURA_DOMANDA")
	private Date dtAperturaDomanda;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CHIUSURA_DOMANDA")
	private Date dtChiusuraDomanda;

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public Date getDtAperturaDomanda() {
		return this.dtAperturaDomanda;
	}

	public void setDtAperturaDomanda(Date dtAperturaDomanda) {
		this.dtAperturaDomanda = dtAperturaDomanda;
	}

	public Date getDtChiusuraDomanda() {
		return this.dtChiusuraDomanda;
	}

	public void setDtChiusuraDomanda(Date dtChiusuraDomanda) {
		this.dtChiusuraDomanda = dtChiusuraDomanda;
	}
}

package it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo;

import java.io.Serializable;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoInterventoColturale;


public class DatiAggiuntiviRichiestaModificaSuoloDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean visibileOrtofoto;
	private TipoInterventoColturale tipoInterventoColturale;
	private PeriodoInterventoRichiestaModificaSuoloDto periodoIntervento;

	public Boolean getVisibileOrtofoto() {
		return visibileOrtofoto;
	}

	public void setVisibileOrtofoto(Boolean visibileOrtofoto) {
		this.visibileOrtofoto = visibileOrtofoto;
	}

	public TipoInterventoColturale getTipoInterventoColturale() {
		return tipoInterventoColturale;
	}

	public void setTipoInterventoColturale(TipoInterventoColturale tipoInterventoColturale) {
		this.tipoInterventoColturale = tipoInterventoColturale;
	}

	public PeriodoInterventoRichiestaModificaSuoloDto getPeriodoIntervento() {
		return periodoIntervento;
	}

	public void setPeriodoIntervento(PeriodoInterventoRichiestaModificaSuoloDto periodoIntervento) {
		this.periodoIntervento = periodoIntervento;
	}
}

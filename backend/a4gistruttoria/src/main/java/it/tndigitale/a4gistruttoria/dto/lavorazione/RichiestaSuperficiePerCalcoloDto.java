package it.tndigitale.a4gistruttoria.dto.lavorazione;

import it.tndigitale.a4gistruttoria.dto.domandaunica.RichiestaSuperficie;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;

public class RichiestaSuperficiePerCalcoloDto extends RichiestaSuperficie {

	private static final long serialVersionUID = -1990091994446806657L;
	
	private CodiceInterventoAgs codiceInterventoAgs;
	private Long annoCampagna;

	public CodiceInterventoAgs getCodiceInterventoAgs() {
		return codiceInterventoAgs;
	}

	public void setCodiceInterventoAgs(CodiceInterventoAgs codiceInterventoAgs) {
		this.codiceInterventoAgs = codiceInterventoAgs;
	}

	public Long getAnnoCampagna() {
		return annoCampagna;
	}

	public void setAnnoCampagna(Long annoCampagna) {
		this.annoCampagna = annoCampagna;
	}
}

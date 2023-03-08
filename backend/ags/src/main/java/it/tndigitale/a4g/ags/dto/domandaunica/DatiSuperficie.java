package it.tndigitale.a4g.ags.dto.domandaunica;

import java.util.List;

public class DatiSuperficie {

	public static enum InterventoSuperficie {
		SOIA,
		PROTEAGINOSA,
		FRUMENTO_DURO,
		LEGUMINOSA,
		// ?
		RISO,
		BARBABIETOLA,
		
		POMODORO,
		OLIVETO,
		OLIVETO_PENDENZA,
		OLIVETO_QUALITA;
		
	}
	
	public static class SuperficieImpegnataSuperficie extends SuperficieImpegnata {
		private InterventoSuperficie intervento;

		public InterventoSuperficie getIntervento() {
			return intervento;
		}

		public void setIntervento(InterventoSuperficie intervento) {
			this.intervento = intervento;
		}
		
		
	}
	
	
	private List<SuperficieImpegnataSuperficie> superficiImpegnate;


	public List<SuperficieImpegnataSuperficie> getSuperficiImpegnate() {
		return superficiImpegnate;
	}


	public void setSuperficiImpegnate(List<SuperficieImpegnataSuperficie> superficiImpegnate) {
		this.superficiImpegnate = superficiImpegnate;
	}

}

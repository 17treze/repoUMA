package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.List;

public class DettaglioDatiTotaliSuperficiImpegnateACS {

	private Double supRichiesta;
	private Double supRichiestaNetta;
	
	private List<DettaglioDatiSuperficeImpegnataACS> superficiImpegnate;

	public Double getSupRichiesta() {
		return supRichiesta;
	}

	public void setSupRichiesta(Double supRichiesta) {
		this.supRichiesta = supRichiesta;
	}

	public Double getSupRichiestaNetta() {
		return supRichiestaNetta;
	}

	public void setSupRichiestaNetta(Double supRichiestaNetta) {
		this.supRichiestaNetta = supRichiestaNetta;
	}

	public List<DettaglioDatiSuperficeImpegnataACS> getSuperficiImpegnate() {
		return superficiImpegnate;
	}

	public void setSuperficiImpegnate(List<DettaglioDatiSuperficeImpegnataACS> superficiImpegnate) {
		this.superficiImpegnate = superficiImpegnate;
	}	
}

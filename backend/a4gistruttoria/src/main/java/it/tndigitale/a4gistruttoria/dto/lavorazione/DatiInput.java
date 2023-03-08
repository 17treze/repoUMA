package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class DatiInput extends DatiCalcoli {

	private List<EsitoControllo> controlli;
	
	public DatiInput() {
		super();
		this.controlli = new ArrayList<>();
	
	}

	public List<EsitoControllo> getControlli() {
		return controlli;
	}

	public void setControlli(List<EsitoControllo> controlli) {
		if (controlli != null) {
			this.controlli = controlli;
		}
	}

}

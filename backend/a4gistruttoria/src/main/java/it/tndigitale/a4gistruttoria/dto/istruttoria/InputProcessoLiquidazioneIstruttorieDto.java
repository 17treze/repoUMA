package it.tndigitale.a4gistruttoria.dto.istruttoria;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;

@ApiModel(description = "Rappresenta il modello dei dati da passare in input per liquidare le istruttorie")
public class InputProcessoLiquidazioneIstruttorieDto extends InputProcessoIstruttorieDto {

	private static final long serialVersionUID = -2302735484816260714L;

	@ApiParam(value="Anno della campagna delle istruttorie da liquidare", required = true)
	private Integer campagna;
	
	@ApiParam(value="Sostegno da liquidare", required = true)
	private Sostegno sostegno;
	public Integer getCampagna() {
		return campagna;
	}
	
	public Sostegno getSostegno() {
		return sostegno;
	}
	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}
	
	public void setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
	}

}

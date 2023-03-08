package it.tndigitale.a4gistruttoria.dto;

public class ProcessoAnnoCampagnaDomandaDto extends Processo {

	private static final long serialVersionUID = -3004368076245487239L;

	private Integer campagna;

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}
}

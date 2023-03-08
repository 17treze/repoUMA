package it.tndigitale.a4g.fascicolo.anagrafica.dto.caa;

import java.util.List;

public class SportelloFascicoloDto {

	private Integer identificativoSportello;
	private List<String> cuaaList;

	public Integer getIdentificativoSportello() {
		return identificativoSportello;
	}
	public SportelloFascicoloDto setIdentificativoSportello(Integer identificativoSportello) {
		this.identificativoSportello = identificativoSportello;
		return this;
	}
	public List<String> getCuaaList() {
		return cuaaList;
	}
	public SportelloFascicoloDto setCuaaList(List<String> cuaaList) {
		this.cuaaList = cuaaList;
		return this;
	}
}

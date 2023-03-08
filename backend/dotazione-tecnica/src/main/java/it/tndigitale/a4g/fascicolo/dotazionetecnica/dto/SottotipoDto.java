package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import java.util.List;

public class SottotipoDto extends TipologiaDto {

	private List<TipologiaDto> tipologie;

	private List<ClasseFunzionaleDto> classiFunzionali;

	public List<TipologiaDto> getTipologie() {
		return tipologie;
	}

	public SottotipoDto setTipologie(List<TipologiaDto> tipologie) {
		this.tipologie = tipologie;
		return this;
	}

	public List<ClasseFunzionaleDto> getClassiFunzionali() {
		return classiFunzionali;
	}

	public SottotipoDto setClassiFunzionali(List<ClasseFunzionaleDto> classiFunzionali) {
		this.classiFunzionali = classiFunzionali;
		return this;
	}

}

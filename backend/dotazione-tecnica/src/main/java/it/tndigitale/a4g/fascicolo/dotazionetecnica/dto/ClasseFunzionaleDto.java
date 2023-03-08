package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import java.util.List;

public class ClasseFunzionaleDto extends TipologiaDto {

	private List<TipologiaDto> sottotipologie;

	public List<TipologiaDto> getTipologie() {
		return sottotipologie;
	}

	public ClasseFunzionaleDto setTipologie(List<TipologiaDto> sottotipologie) {
		this.sottotipologie = sottotipologie;
		return this;
	}
}

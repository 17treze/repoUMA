package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafia;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;

public class DomandeCollegateExportBdna {
	
	private DichiarazioneAntimafia dichiarazioneAntimafia;
	
	private List<A4gtDomandeCollegate> domandeCollegate;

	public DichiarazioneAntimafia getDichiarazioneAntimafia() {
		return dichiarazioneAntimafia;
	}

	public void setDichiarazioneAntimafia(DichiarazioneAntimafia dichiarazioneAntimafia) {
		this.dichiarazioneAntimafia = dichiarazioneAntimafia;
	}

	public List<A4gtDomandeCollegate> getDomandeCollegate() {
		return domandeCollegate;
	}

	public void setDomandeCollegate(List<A4gtDomandeCollegate> domandeCollegate) {
		this.domandeCollegate = domandeCollegate;
	}
}

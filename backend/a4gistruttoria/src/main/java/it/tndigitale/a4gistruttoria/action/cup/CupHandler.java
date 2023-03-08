package it.tndigitale.a4gistruttoria.action.cup;

import it.tndigitale.a4gistruttoria.dto.AnagraficaAzienda;
import it.tndigitale.a4gistruttoria.dto.cup.DatiCUP;

public class CupHandler {
	
	private final DatiCUP datiCUP;
	private final AnagraficaAzienda anagraficaAzienda;
	
	public CupHandler(DatiCUP datiCUP, AnagraficaAzienda anagraficaAzienda) {
		super();
		this.datiCUP = datiCUP;
		this.anagraficaAzienda = anagraficaAzienda;
	}

	public DatiCUP getDatiCUP() {
		return datiCUP;
	}

	public AnagraficaAzienda getAnagraficaAzienda() {
		return anagraficaAzienda;
	}

}

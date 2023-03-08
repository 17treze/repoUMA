package it.tndigitale.a4gistruttoria.component;

import it.tndigitale.a4gistruttoria.service.businesslogic.iotialia.IoItaliaMessaggioByStato;

public interface IoItaliaInvioMessaggioFactory {

	public IoItaliaMessaggioByStato getIoItaliaInvioMessaggioByStato(String nomeQualificatore);
}

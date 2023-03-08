package it.tndigitale.a4gutente.service;

import it.tndigitale.a4gutente.dto.DatiAutenticazione;
import it.tndigitale.a4gutente.dto.Firma;

public interface IFirmaService {

	Firma firma(String modulo, DatiAutenticazione datiAutenticazione, byte[] documento) throws Exception;

}
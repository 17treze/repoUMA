package it.tndigitale.a4g.proxy.bdn.repository.interfaces;

import java.math.BigDecimal;
import java.util.List;

import it.tndigitale.a4g.proxy.bdn.dto.MovimentazionePascoloDO;
import it.tndigitale.a4g.proxy.bdn.dto.istruttoria.MovimentazionePascoloOviniDto;

public interface MovimentazionePascoloDAO {
	public MovimentazionePascoloDO getMovimentazionePascoloUscita(String cuaa, int annoCampagna, MovimentazionePascoloDO bdnData);

	public MovimentazionePascoloDO getMovimentazionePascoloIngresso(String cuaa, int annoCampagna, MovimentazionePascoloDO bdnData);

	void insert(MovimentazionePascoloDO movimentazione, String cuaa, int annoCampagna);

	public void closeRecordById(MovimentazionePascoloDO dataObject);

	void closeRecordByCuaa(String cuaa, String tipoMovimento, int annoCampagna);

	/**
	 * Carica i dati di movimenti pascolo 2015 per l algoritmo man 4 di istruttoria
	 * 
	 * @param annoCampagna
	 * @param codicePascolo
	 * @param codiceFiscale
	 * @return
	 */
	public List<MovimentazionePascoloOviniDto> getMovimentazionePascoloOviniPerIstruttoria(BigDecimal annoCampagna, String codicePascolo, String codiceFiscale);

	/**
	 * Carica i dati di movimenti pascolo 2015 per l algoritmo man 4 di istruttoria per codice pascolo
	 * 
	 * @param annoCampagna
	 * @param codicePascolo
	 * @param codiceFiscale
	 * @return
	 */

	public List<MovimentazionePascoloOviniDto> getMovimentazionePascoloOviniPerIstruttoriaPerCodPascolo(BigDecimal annoCampagna, String codicePascolo);

}

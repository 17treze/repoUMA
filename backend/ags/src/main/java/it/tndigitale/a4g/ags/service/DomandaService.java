package it.tndigitale.a4g.ags.service;

import java.util.List;

import javax.persistence.NoResultException;

import it.tndigitale.a4g.ags.dto.*;
import it.tndigitale.a4g.ags.dto.domandaunica.DatiRicevibilita;
import it.tndigitale.a4g.ags.model.DomandeCollegatePsrFilter;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author A.Siravo
 *
 */
public interface DomandaService {

	/**
	 * Conteggio domande presentate in AGS afferenti ad un certo anno, e settore (pac e tipo).
	 * 
	 * @param datiSettore
	 *            informazioni di settore per la ricerca delle domande;
	 * @return numero domande protocollate che soddisfano i criteri di ricerca su anno, pac e tipo.
	 */
	public Long countDomande(DatiSettore datiSettore, String moduloRitiroTotale);

	/**
	 * Metodo che esegue la movimentazione di una domanda nel sistema AGS, storicizza lo stato attuale della domanda inserisce un nuovo record con lo stato passato come parametro successivamente
	 * scrive nelle tabelle di log di movimento per tenere traccia del cambio di stato
	 * 
	 * @param numeroDomanda
	 * @param tipoMovimento
	 * @return
	 */
	public String eseguiMovimentazioneDomanda(Long numeroDomanda, String tipoMovimento) throws NoResultException;

	/**
	 * Metodo per l'estrazione domande a Superficie non ancora liquidate con importo richiesto
	 * 
	 * @param domandeCollegatePsrFilter
	 * @return
	 */
	public List<DomandaPsr> getDomandePsr(DomandeCollegatePsrFilter domandeCollegatePsrFilter);

	public CalcoloSostegnoAgs getCalcoloSostegnoDomandaPrecedente(String cuaa, String codCalcolo, String movLiquidazione);

	public Boolean checkExistDomandaPerSettore(String codicePac, String tipoDomanda, Long anno, String cuaa);

	public boolean forzaMovimentazioneDomanda(Long numeroDomanda, String tipoMovimento) throws Exception;

	/**
	 * Metodo per la ricerca dei dati di ricevibilità dal sistema AGS in base a dei criteri. Consente di ottenere sia le informazioni generali che i dati di protocollazione
	 * 
	 * @param domandaFilter
	 *            criteri di ricerca
	 * @return Lista valorizzata con le informazioni delle domande recuperate dal sistema AGS
	 */
	public List<DomandaUnica> getDomande(DomandaFilter domandaFilter) throws NoResultException;

	/**
	 * Metodo per la ricerca dei dati di ricevibilità dal sistema AGS in base all'anno di campagna. Consente di ottenere sia le informazioni generali che i dati di protocollazione
	 *
	 * @param campagna anno campagna di riferimento
	 * @return Lista valorizzata con le informazioni delle domande recuperate dal sistema AGS
	 */
	public List<Long> getDomandeProtocollate(Integer campagna) throws NoResultException;

    /**
	 * Metodo per il controllo della ricevibilità della domanda ed esportazione dei dati ad A4g.
	 * @param numeroDomanda
	 * @return
	 * @throws NoResultException
	 */
	public DatiRicevibilita ricevi(Long numeroDomanda) throws Exception;

	/* vOK
	DomandaUnica domandaUnica = serviceDomanda.getDomandaUnica(numeroDomanda, Arrays.asList("all"));
	serviceDomanda.verificaCriteriRicevibilita(domandaUnica);
	serviceDomanda.aggiornaStatoDomanda(domandaUnica);
	return domandaUnica; */

	@Transactional
	DomandaUnica riceviDomanda(Long numeroDomanda) throws NoResultException;

	public DomandaUnica getDomandaUnica(Long numeroDomanda) throws NoResultException;

	public DomandaUnica getDomandaUnica(Long numeroDomanda, List<String> expandParams) throws NoResultException;

	// public void verificaCriteriRicevibilita(DomandaUnica domandaUnica);

	// public void aggiornaStatoDomanda(DomandaUnica domandaUnica);

	/**
	 * Metodo per il recupero dei dati di ricevibilità dal sistema AGS in base ad i criteri di ricerca. Consente di ottenere sia le informazioni generali che i dati di protocollazione
	 * 
	 * @param domandaUnicaFilter
	 *            criteri di ricerca
	 * @return DTO valorizzato con le InfoGeneraliDomanda recuperate dal sistema AGS
	 */
	public List<DomandaUnica> getDomandeUniche(DomandaUnicaFilter domandaUnicaFilter) throws NoResultException;

}

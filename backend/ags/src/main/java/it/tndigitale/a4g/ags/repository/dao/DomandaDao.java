package it.tndigitale.a4g.ags.repository.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import it.tndigitale.a4g.ags.dto.CalcoloSostegnoAgs;
import it.tndigitale.a4g.ags.dto.ControlliPresentazione;
import it.tndigitale.a4g.ags.dto.DatiComune;
import it.tndigitale.a4g.ags.dto.DatiErede;
import it.tndigitale.a4g.ags.dto.DomandaPsr;
import it.tndigitale.a4g.ags.dto.DomandaUnica;
import it.tndigitale.a4g.ags.dto.DomandaUnicaFilter;
import it.tndigitale.a4g.ags.dto.InfoEleggibilitaParticella;
import it.tndigitale.a4g.ags.dto.InfoGeneraliDomanda;
import it.tndigitale.a4g.ags.dto.SintesiRichieste;
import it.tndigitale.a4g.ags.dto.SostegniSuperficie;
import it.tndigitale.a4g.ags.dto.domandaunica.DatiPascolo;
import it.tndigitale.a4g.ags.dto.domandaunica.DichiarazioniDomandaUnica;
import it.tndigitale.a4g.ags.dto.domandaunica.SostegniAllevamento;
import it.tndigitale.a4g.ags.model.DomandeCollegatePsrFilter;

/**
 * @author A.Siravo
 *
 */
public interface DomandaDao {

	/**
	 * Metodo per il recupero da sistema AGS dei dati identificativi della domanda. Non recupera i dati di protocollazione.
	 * 
	 * @param numeroDomanda
	 *            identificativo della domanda per la quale procedere al recupero delle informazioni
	 * @return DTO valorizzato con le informazioni recuperate dal sistema AGS
	 */
	public InfoGeneraliDomanda getInfoGeneraliDomanda(Long numeroDomanda);

	/**
	 * Metodo per il recupero da sistema AGS dei dati di protocollazione della domanda. Non recupera i dati identificativi.
	 * 
	 * @param numeroDomanda
	 *            identificativo della domanda per la quale procedere al recupero dei dati di protocollazione
	 * @return DTO valorizzato con i dati recuperati dal sistema AGS
	 */
	public ControlliPresentazione getDatiProtocolloDomanda(Long numeroDomanda);

	/**
	 * Metodo per il conteggio di domande di finanziamento protocollate nel sistema AGS.
	 * 
	 * @param annoRiferimento
	 *            anno di campagna di ricerca; es: 2018.
	 * @param codicePac
	 *            codice PAC di ricerca ; es: PAC1420.
	 * @param tipoDomanda
	 *            tipo di domanda di ricerca; es: DU.
	 * @return DTO di domanda valorizzato con i dati recuperati dal sistema AGS.
	 */

	public Long countDomande(BigDecimal annoRiferimento, String codicePac, String tipoDomanda, String moduloRitiroTotale);

	/**
	 * Metodo che esegue la movimentazione di una domanda nel sistema AGS, storicizza lo stato attuale della domanda inserisce un nuovo record con lo stato passato come parametro successivamente
	 * scrive nelle tabelle di log di movimento per tenere traccia del cambio di stato
	 * 
	 * @param numeroDomanda
	 * @param tipoMovimento
	 * @return
	 */
	public String eseguiMovimentazioneDomanda(Long numeroDomanda, String tipoMovimento) throws NoResultException;

    SintesiRichieste getSintesiRichiesteDomandaUnica(Long numeroDomanda, InfoGeneraliDomanda infoGeneraliDomanda);

    public SintesiRichieste getSintesiRichiesteDomandaUnica(Long numeroDomanda);

	public List<DatiPascolo> getDatiPascolo(Long numeroDomanda);

	public List<DichiarazioniDomandaUnica> getDichiarazioniDomanda(Long numeroDomanda, Integer campagna) throws NoResultException;

	public List<SostegniAllevamento> getSostegniAllevamento(Long numeroDomanda);

	public List<SostegniSuperficie> getSostegniSuperficie(Long numeroDomanda) throws NoResultException;

	public List<InfoEleggibilitaParticella> getInfoEleggibilitaPartRichieste(Long numeroDomanda) throws NoResultException;

	public List<DomandaPsr> getDomandePsr(DomandeCollegatePsrFilter domandeCollegatePsrFilter);

	public DatiComune getDatiComune(String codIstat);

	public CalcoloSostegnoAgs getCalcoloSostegnoDomandaUnica(String cuaa, String codCalcolo, String movLiquidazione);

	public Date getDataMorte(Long idDomanda);

	public Long getIbanValido(Long idDomanda);

	public Long getDomandaSospesaAgea(Long idDomanda);

	public Long getDomandaSospesaAgeaBySoggetto(String cuaa);

	public String getIban(Long idDomanda);

	public String getIbanErede(String cuaa);

	public Boolean checkExistDomandaPerSettore(String codicePac, String tipoDomanda, Long anno, String cuaa);

	public DatiErede getDatiErede(Long numeroDomanda);

	public String forzaMovimentazioneDomanda(Long numeroDomanda, String tipoMovimento) throws Exception;

	/**
	 * Metodo per l'estrazione della lista degli id domanda che si trovano in uno specifico stato nel vecchio sistema
	 * 
	 * @param annoRiferimento
	 *            anno di campagna di ricerca; es: 2018.
	 * @param settore
	 *            settore della domanda nel vecchio sistema; es: PI2014
	 * @param stati
	 *            stati della domanda nel vecchio sistema: es: 000015
	 * @param moduloRitiroTotale
	 *            modulo ritiro totale che non deve essere considerato
	 * @return
	 */
	public List<Long> getListaDomandePerStato(Integer annoRiferimento, String settore, List<String> stati, String moduloRitiroTotale);

	/**
	 * Metodo per l'estrazione della lista degli id domanda che si trovano nello stato PROTOCOLLATO nel vecchio sistema
	 *
	 * @param campagna
	 *            anno di campagna di ricerca; es: 2018.
	 * @param moduloRitiroTotale
	 *            modulo ritiro totale che non deve essere considerato
	 * @return
	 */
	public List<Long> getListaDomandeProtocollate(Integer campagna, String moduloRitiroTotale);

	public String getIbanFascicolo(Long idDomanda);

	/**
	 * Metodo per l'estrazione della lista delle InfoGeneraliDomanda che si trovano in uno specifico stato nel vecchio sistema
	 * 
	 * @param campagna
	 *            anno di campagna di ricerca; es: 2018.
	 * @param cuaa
	 *            cuaa del richiedente
	 * @param stati
	 *            stati della domanda 
	 * @return
	 */
	public List<DomandaUnica> getListaDomande(DomandaUnicaFilter domandaUnicaFilter);

}

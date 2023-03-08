package it.tndigitale.a4gistruttoria.service;


import java.math.BigDecimal;
import java.net.ConnectException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.dto.CalcoloSostegnoAgs;
import it.tndigitale.a4gistruttoria.dto.Capo;
import it.tndigitale.a4gistruttoria.dto.CuaaDenominazione;
import it.tndigitale.a4gistruttoria.dto.DatiDomandaIbanErrato;
import it.tndigitale.a4gistruttoria.dto.DatiErede;
import it.tndigitale.a4gistruttoria.dto.Domanda;
import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.dto.FiltroRicercaDomandeIstruttoria;
import it.tndigitale.a4gistruttoria.dto.InfoDomandaDU;
import it.tndigitale.a4gistruttoria.dto.InfoIstruttoriaDomanda;
import it.tndigitale.a4gistruttoria.dto.InfoLiquidabilita;
import it.tndigitale.a4gistruttoria.dto.Pagina;
import it.tndigitale.a4gistruttoria.dto.Paginazione;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDuEsito;
import it.tndigitale.a4gistruttoria.dto.domandaunica.RichiestaSuperficie;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DomandaUnicaDettaglio;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;

public interface DomandeService {

	List<StatoDomanda> elencoStatiPossibili();

	AgricoltoreSIAN recuperaInfoAgricoltoreSIAN(BigDecimal numeroDomanda) throws Exception;

	DomandaUnica recuperaSostegniDomandaDU(DomandaUnicaModel domanda) throws ConnectException, SQLException;

	InfoDomandaDU findByCuaaIntestatarioAndAnnoRiferimento(String cuaa, Integer anno);

	InfoIstruttoriaDomanda recuperaInfoIstruttoriaDomanda(DomandaUnicaModel domanda) throws ConnectException;

	CalcoloSostegnoAgs getDatiCalcoloSostegnoAgs(DomandaUnicaModel domanda) throws ConnectException, NoResultException;

	DomandaUnicaDettaglio getDomandaDettaglio(Long id) throws Exception;

	Pagina<RichiestaSuperficie> getParticelleDomanda(Long id, Paginazione paramPaginazione, String paramList) throws Exception;

	boolean recuperaEsitoSigeco(BigDecimal numeroDomanda);

	Boolean isPascoliImpegnati(Long idDomanda) throws Exception;

	void salvaDatiFiltratiDomanda(Long idDomanda) throws Exception;

	List<CuaaDenominazione> getCuaaDomandeCampagna(FiltroRicercaDomandeIstruttoria filter);

	RisultatiPaginati<String> getCuaaDomandeFiltrati(String statoSostegno, Sostegno sostegno, Integer annoCampagna, String cuaa, TipoIstruttoria tipo, it.tndigitale.a4g.framework.pagination.model.Paginazione paginazione, it.tndigitale.a4g.framework.pagination.model.Ordinamento ordinamento);

	RisultatiPaginati<String> getRagioneSocialeDomandeFiltrati(String statoSostegno, Sostegno sostegno, Integer annoCampagna, String ragioneSociale, TipoIstruttoria tipo, it.tndigitale.a4g.framework.pagination.model.Paginazione paginazione, it.tndigitale.a4g.framework.pagination.model.Ordinamento ordinamento);

	InfoLiquidabilita recuperaInfoLiquidabilita(Long numeroDomanda) throws ConnectException;

	List<String> getCodiciPascoli(Integer annoCampagna, String cuaa);

	byte[] getVerbaleLiquidazione(String codiceElenco);

	List<DomandaUnica> recuperaDomandeUniche(List<Long> numeroDomandaList);

	// transazionale
	void elaboraDomandaPerIstruttoria(DomandaUnicaModel d, StringBuilder outErrore);

	List<A4gtRichiestaSuperficie> getRichiesteSuperficiePerIntervento(Long idDomanda, CodiceInterventoAgs intervento) throws Exception;


	/**
	 * Restituisce gli esiti della richiesta allevamento DU tramite id
	 * 
	 * @param idRichiestaAllevamentoEsito
	 * @return
	 * @throws Exception
	 */
	RichiestaAllevamDuEsito getRichiestaAllevamDuEsito(Long idRichiestaAllevamentoEsito) throws Exception;

	/**
	 * Modifica il capo
	 * 
	 * @param id
	 * @param capoRichiesto
	 * @return
	 */
	Capo modificaCapo(Long id, Capo capoRichiesto);

	DomandaUnicaModel getDomandaAnnoPrecedente(DomandaUnicaModel domanda);

	boolean checkDomandaPerSettore(Integer anno, String cuaa) throws CalcoloSostegnoException;

	/**
	 * Per le domande di ritiro parziale si deve considerare la data di protocollazione originaria.
	 * Se la data di protocollazione originaria è null la domanda non è un ritiro parziale.
	 * 
	 */
	LocalDateTime getDataProtocollazioneDomanda(DomandaUnicaModel domanda);


	boolean recuperaFlagConvSigeco(BigDecimal numeroDomanda);

	/**
	 * Recupera le informazioni della domanda 
	 * @param id= id della domnda
	 * @return La domanda
	 */
	Domanda getDomanda(Long id);
	
	/**
	 * Aggiorna una domanda unica
	 * @param domanda: dati della domanda
	 * @return La domanda aggiornata
	 * @throws Exception
	 */
	Domanda aggiornaDomanda(Domanda domanda) throws Exception;

	/**
	 * Aggiorna i dati di un erede
	 * @param erede: dati dell'erede
	 * @return erede aggiornato
	 * @throws Exception
	 */
	DatiErede aggiornaErede(DatiErede erede) throws Exception;

	/**
	 * Crea un nuovo erede associato ad una domanda
	 * @param idDomanda: id domanda unica
	 * @param idDomanda: dati dell'erede
	 * @return l'erede creato
	 * @throws Exception
	 */
	DatiErede creaErede(Long idDomanda , DatiErede datiErede) throws Exception;

	/**
	 * Annulla l'istruttoria in corso di una domanda.
	 * La domanda può essere annullata solo se non ci sono pagamenti ad essa associata.
	 * In caso positivo, vengono eliminati tutti i dati di istruttoria associati alla stessa
	 * e la domanda viene posta in stato PROTOCOLLATA in AGS.
	 * @param id
	 * @throws Exception
	 */
	void annullaIstruttoriaDomanda(Long id) throws Exception;
	
	/**
	 * Verifica se la domanda ha dei sostegni in pagamento / pagati
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Boolean checkDomandaInPagamento(Long id) throws Exception;

	/**
	 * Recupera l'importo delle sanzioni per l'anno precedente da A4G
	 * per le domande di saldo o integrazione
	 * @param domanda
	 * @param isGiovaneAgricoltore
	 * @return
	 */
	Float getSanzioneAnnoPrecedente(DomandaUnicaModel domanda, boolean isGiovaneAgricoltore);

	boolean checkDomandaAnnoPrecedenteIstruttoriaSostegnoConclusa(DomandaUnicaModel domanda, Sostegno sostegno);

	List<DatiDomandaIbanErrato> getDatiDomandeIbanErrato();
	
}

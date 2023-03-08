package it.tndigitale.a4gistruttoria.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.AccoppiatoSuperficie;
import it.tndigitale.a4gistruttoria.dto.InformazioniColtivazione;
import it.tndigitale.a4gistruttoria.dto.Particella;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioDatiSuperficeImpegnataACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioDatiTotaliSuperficiImpegnateACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioSuperficiImpegnateACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.RiferimentiCartografici;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DatiASDatiSuperficiImpegnate extends DatiDettaglioAbstract {

	private static final Logger logger = LoggerFactory.getLogger(DatiASDatiSuperficiImpegnate.class);

	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;

	@Autowired
	private ObjectMapper objectMapper;

	public void recupera(AccoppiatoSuperficie accoppiatoSuperficie) {
		// Recupero tutte le richieste superficie per la domanda
		List<A4gtRichiestaSuperficie> richieste = daoRichiestaSuperficie.findByDomanda(accoppiatoSuperficie.getIdDomanda());
		
		// Imposto l'oggetto
		DettaglioSuperficiImpegnateACS dettaglio = new DettaglioSuperficiImpegnateACS();
		accoppiatoSuperficie.setDatiSuperficiImpegnate(dettaglio);
		dettaglio.setSupRichiesta(0.0d);
		dettaglio.setSupRichiestaNetta(0.0d);
		
		// Valorizzo tutti i dettagli per ciascun intervento
		dettaglio.setM8(creaDettaglioSuperficiImpegnateIntervento(richieste, dettaglio, CodiceInterventoAgs.SOIA));
		dettaglio.setM9(creaDettaglioSuperficiImpegnateIntervento(richieste, dettaglio, CodiceInterventoAgs.GDURO));
		dettaglio.setM10(creaDettaglioSuperficiImpegnateIntervento(richieste, dettaglio, CodiceInterventoAgs.CPROT));
		dettaglio.setM11(creaDettaglioSuperficiImpegnateIntervento(richieste, dettaglio, CodiceInterventoAgs.LEGUMIN));
		dettaglio.setM14(creaDettaglioSuperficiImpegnateIntervento(richieste, dettaglio, CodiceInterventoAgs.POMOD));
		dettaglio.setM15(creaDettaglioSuperficiImpegnateIntervento(richieste, dettaglio, CodiceInterventoAgs.OLIO));
		dettaglio.setM16(creaDettaglioSuperficiImpegnateIntervento(richieste, dettaglio, CodiceInterventoAgs.OLIVE_PEND75));
		dettaglio.setM17(creaDettaglioSuperficiImpegnateIntervento(richieste, dettaglio, CodiceInterventoAgs.OLIVE_DISC));
	}

	protected DettaglioDatiTotaliSuperficiImpegnateACS creaDettaglioSuperficiImpegnateIntervento(
			List<A4gtRichiestaSuperficie> richieste, DettaglioSuperficiImpegnateACS dettaglio, CodiceInterventoAgs codiceInterventoAgs) {
		// Se esistono richieste per l'intervento in oggetto, procedo a valorizzare il dettaglio
		if (richieste.stream().anyMatch(p -> p.getIntervento().getIdentificativoIntervento().equals(codiceInterventoAgs))) {
			DettaglioDatiTotaliSuperficiImpegnateACS result = new DettaglioDatiTotaliSuperficiImpegnateACS();
			List<DettaglioDatiSuperficeImpegnataACS> elencoSuperficiImpegnate = new ArrayList<>();
			result.setSuperficiImpegnate(elencoSuperficiImpegnate);
			result.setSupRichiesta(0.0d);
			result.setSupRichiestaNetta(0.0d);
			richieste.stream()
				.filter(p -> p.getIntervento().getIdentificativoIntervento().equals(codiceInterventoAgs))
				.forEach(item -> {
					DettaglioDatiSuperficeImpegnataACS si = new DettaglioDatiSuperficeImpegnataACS();
					elencoSuperficiImpegnate.add(si);
					si.setSupRichiesta(getDoubleFromBigDecimalVal(item.getSupRichiesta()));
					si.setSupRichiestaNetta(getDoubleFromBigDecimalVal(item.getSupRichiestaNetta()));
					try {
						si.setDatiCatastali(objectMapper.readValue(item.getInfoCatastali(), Particella.class));
						si.setDatiColtivazione(objectMapper.readValue(item.getInfoColtivazione(), InformazioniColtivazione.class));
						si.setRiferimentiCartografici(objectMapper.readValue(item.getRiferimentiCartografici(), RiferimentiCartografici.class));
					} catch (Exception e) {
						logger.debug("caricaElencoDettaglioDatiTotaliSuperficiImpegnateACS: errore caricando i dati della superficie {} per la domanda {}",
								item.getId(), item.getDomandaUnicaModel().getId(), e);
						throw new RuntimeException("Errore caricando i dati della superficie richiesta per la domanda {}".concat(item.getDomandaUnicaModel().getId().toString()));
					}
					result.setSupRichiesta(Double.sum(result.getSupRichiesta(),getDoubleFromBigDecimalVal(item.getSupRichiesta())));
					result.setSupRichiestaNetta(Double.sum(result.getSupRichiestaNetta(),getDoubleFromBigDecimalVal(item.getSupRichiestaNetta())));
			});
			
			// Aggiungo inoltre i valori delle superfici per intervento alla superficie totale
			dettaglio.setSupRichiesta(Double.sum(dettaglio.getSupRichiesta(),result.getSupRichiesta()));
			dettaglio.setSupRichiestaNetta(Double.sum(dettaglio.getSupRichiestaNetta(),result.getSupRichiestaNetta()));
			return result;
		}
		return null;
	}
	
	private Double getDoubleFromBigDecimalVal(BigDecimal val) {
		return Optional.ofNullable(val).map(BigDecimal::doubleValue).orElse(0.0);
	}

	@Override
	protected StatoIstruttoria adjustStatoLavorazioneSostegno(StatoIstruttoria stato) {
		// I dati delle superfici vengono recuperati dai risultati dell'algoritmo di calcolo, quindi dai passi
		// calcolo ok se l'algoritmo è andato a buon fine, altrimenti (quindi se non ammissibile) da calcolo ko
		if (StatoIstruttoria.NON_AMMISSIBILE.equals(stato))
			return StatoIstruttoria.CONTROLLI_CALCOLO_KO;
		else 
			return StatoIstruttoria.CONTROLLI_CALCOLO_OK;
	}
}

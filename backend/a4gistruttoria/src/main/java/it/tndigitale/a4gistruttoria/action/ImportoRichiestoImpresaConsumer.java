package it.tndigitale.a4gistruttoria.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.ImportaDatiStrutturaliHandler;
import it.tndigitale.a4gistruttoria.repository.dao.DomandeCollegateDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;
import it.tndigitale.a4gistruttoria.util.AntimafiaDomandeCollegateHelper;
import it.tndigitale.a4gistruttoria.util.StatoDomandaCollegata;
import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

@Component
public class ImportoRichiestoImpresaConsumer implements Consumer<ImportaDatiStrutturaliHandler> {

	private static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	private static Logger logger = LoggerFactory.getLogger(ImportoRichiestoImpresaConsumer.class);

	@Autowired
	private DomandeCollegateDao domandeCollegateDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void accept(ImportaDatiStrutturaliHandler importaDatiStrutturaliHandler) {
		A4gtDomandeCollegate a4gtDomandeCollegate = new A4gtDomandeCollegate();
		BigDecimal importoRichiesto = importaDatiStrutturaliHandler.getImportoRichiesto().get("importoRichiesto") != null
				? BigDecimal.valueOf(importaDatiStrutturaliHandler.getImportoRichiesto().get("importoRichiesto").asDouble()) : null;
		Date dataDomanda;
		try {
			dataDomanda = importaDatiStrutturaliHandler.getImportoRichiesto().get("dataDomanda") != null
					? new SimpleDateFormat(JSON_DATE_FORMAT).parse(importaDatiStrutturaliHandler.getImportoRichiesto().get("dataDomanda").asText()) : null;
		} catch (ParseException e) {
			logger.error("Errore nell' importazione dei dati strutturali", e);
			throw new RuntimeException();
		} catch (Exception e) {
			logger.error("Errore non previsto", e);
			throw new RuntimeException();
		}
		// confronto gli importi ricevuti dal servizio con l'importo in input, se strettamente maggiori procedo con l'elaborazione dei dati
		if (Objects.nonNull(importoRichiesto) && importoRichiesto.compareTo(importaDatiStrutturaliHandler.getImporto()) > 0
		/* && dataDomanda.compareTo(importaDatiStrutturaliHandler.getDataPresentazione()) < 0 */) {
			Long idDomanda = importaDatiStrutturaliHandler.getImportoRichiesto().get("idDomanda") != null ? importaDatiStrutturaliHandler.getImportoRichiesto().get("idDomanda").asLong() : null;
			a4gtDomandeCollegate.setIdDomanda(idDomanda);
			a4gtDomandeCollegate.setCuaa(importaDatiStrutturaliHandler.getCuaa());
			a4gtDomandeCollegate.setTipoDomanda(TipoDomandaCollegata.PSR_STRUTTURALI_EU);
			// controllo dell'esistenza di un dato PSR superficie acquisito in precedenza
			Optional<A4gtDomandeCollegate> a4gtDomandeCollegateFound = domandeCollegateDao.findOne(Example.of(a4gtDomandeCollegate));

			DomandaCollegata domandaCollegata = new DomandaCollegata();
			domandaCollegata.setCuaa(importaDatiStrutturaliHandler.getCuaa());
			domandaCollegata
					.setMisurePsr(importaDatiStrutturaliHandler.getImportoRichiesto().get("misuraPSR") != null ? importaDatiStrutturaliHandler.getImportoRichiesto().get("misuraPSR").asText() : null);
			domandaCollegata.setIdDomanda(idDomanda);
			domandaCollegata.setImportoRichiesto(importoRichiesto);
			domandaCollegata.setDtDomanda(dataDomanda);

			// controllo dell'esistenza di un dato PSR superficie acquisito in precedenza
			// se il dato è stato acquisito in precedenza e l'importo è cambiato procedo con l'inserimento del nuovo dato altrimenti non faccio nulla

			A4gtDomandeCollegate domandaCollegataOutput;

			if (!a4gtDomandeCollegateFound.isPresent()) {
				domandaCollegataOutput = new A4gtDomandeCollegate();
				domandaCollegataOutput.setTipoDomanda(TipoDomandaCollegata.PSR_STRUTTURALI_EU);
				domandaCollegataOutput.setCuaa(domandaCollegata.getCuaa());
				domandaCollegataOutput.setIdDomanda(domandaCollegata.getIdDomanda());
				domandaCollegataOutput.setImportoRichiesto(domandaCollegata.getImportoRichiesto());
				domandaCollegataOutput.setMisurePsr(domandaCollegata.getMisurePsr());

				domandaCollegataOutput.setDtDomanda(domandaCollegata.getDtDomanda());
				domandaCollegataOutput.setStatoBdna(StatoDomandaCollegata.NON_CARICATO);
				domandeCollegateDao.save(domandaCollegataOutput);
				domandaCollegata = new DomandaCollegata();
				BeanUtils.copyProperties(domandaCollegataOutput, domandaCollegata);
				domandaCollegata.setStatoBdna(domandaCollegataOutput.getStatoBdna().toString());
				domandaCollegata.setTipoDomanda(domandaCollegataOutput.getTipoDomanda().toString());
				importaDatiStrutturaliHandler.setDomandaCollegata(domandaCollegata);
			} else {
				// BRIAMCNT009
				domandaCollegataOutput = a4gtDomandeCollegateFound.get();
				if (domandaCollegataOutput.getImportoRichiesto().compareTo(domandaCollegata.getImportoRichiesto()) != 0) {
					domandaCollegataOutput.setImportoRichiesto(domandaCollegata.getImportoRichiesto());

					AntimafiaDomandeCollegateHelper.resetDomandaCollegata(domandaCollegataOutput);

					domandaCollegataOutput.setDtDomanda(domandaCollegata.getDtDomanda());
					domandaCollegataOutput.setStatoBdna(StatoDomandaCollegata.NON_CARICATO);
					domandeCollegateDao.save(domandaCollegataOutput);
					domandaCollegata = new DomandaCollegata();
					BeanUtils.copyProperties(domandaCollegataOutput, domandaCollegata);
					domandaCollegata.setStatoBdna(domandaCollegataOutput.getStatoBdna().toString());
					domandaCollegata.setTipoDomanda(domandaCollegataOutput.getTipoDomanda().toString());
					importaDatiStrutturaliHandler.setDomandaCollegata(domandaCollegata);
				}
			}

			logger.debug("Acquisizione del dato di dettaglio e Dettaglio PSR Strutturali EU avvenuto con successo");
		}
	}

}

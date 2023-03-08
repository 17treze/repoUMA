/**
 * 
 */
package it.tndigitale.a4gistruttoria.action;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.IstruttoriaAntimafiaChainHandler;
import it.tndigitale.a4gistruttoria.util.IstruttoriaAntimafiaStatoEnum;

/**
 * @author S.DeLuca
 *
 */
@Component
public class StateConsumer implements Consumer<IstruttoriaAntimafiaChainHandler> {

	private static final Logger logger = LoggerFactory.getLogger(StateConsumer.class);

	@Override
	public void accept(IstruttoriaAntimafiaChainHandler istruttoriaAntimafiaChain) {
		try {
			String cuaa = istruttoriaAntimafiaChain.getCuaa();
			if (!istruttoriaAntimafiaChain.getIsParixOK()) {
				if (istruttoriaAntimafiaChain.getIsSiapOK()) {
					// TODO solo per la validazione della storia, dopo passare a livello DEBUG
					logger.info("ISTRUTTORIA ANTIMAFIA RESPONSE - AZIENDA: ".concat(cuaa).concat("STATO: ").concat("DCM_02"));
					istruttoriaAntimafiaChain.getIstruttoriaAntimafiaEsito().setStato(IstruttoriaAntimafiaStatoEnum.DCM_02.getStato());
				} else {
					// TODO solo per la validazione della storia, dopo passare a livello DEBUG
					logger.info("ISTRUTTORIA ANTIMAFIA RESPONSE - AZIENDA: ".concat(cuaa).concat("STATO: ").concat("DCM_01"));
					istruttoriaAntimafiaChain.getIstruttoriaAntimafiaEsito().setStato(IstruttoriaAntimafiaStatoEnum.DCM_01.getStato());
				}
			} else if (!istruttoriaAntimafiaChain.getIsSiapOK() && istruttoriaAntimafiaChain.getAreSoggettiOK()) {
				logger.info("ISTRUTTORIA ANTIMAFIA RESPONSE - AZIENDA: ".concat(cuaa).concat("STATO: ").concat("DCM_03"));
				istruttoriaAntimafiaChain.getIstruttoriaAntimafiaEsito().setStato(IstruttoriaAntimafiaStatoEnum.DCM_03.getStato());
			} else if (!istruttoriaAntimafiaChain.getIsSiapOK() && !istruttoriaAntimafiaChain.getAreSoggettiOK()) {
				logger.info("ISTRUTTORIA ANTIMAFIA RESPONSE - AZIENDA: ".concat(cuaa).concat("STATO: ").concat("DCM_04"));
				istruttoriaAntimafiaChain.getIstruttoriaAntimafiaEsito().setStato(IstruttoriaAntimafiaStatoEnum.DCM_04.getStato());
			} else if (istruttoriaAntimafiaChain.getIsSiapOK() && !istruttoriaAntimafiaChain.getAreSoggettiOK()) {
				logger.info("ISTRUTTORIA ANTIMAFIA RESPONSE - AZIENDA: ".concat(cuaa).concat("STATO: ").concat("DCM_05"));
				istruttoriaAntimafiaChain.getIstruttoriaAntimafiaEsito().setStato(IstruttoriaAntimafiaStatoEnum.DCM_05.getStato());
			} else if (istruttoriaAntimafiaChain.getIsParixOK() && istruttoriaAntimafiaChain.getIsSiapOK() && istruttoriaAntimafiaChain.getAreSoggettiOK()) {
				logger.info("ISTRUTTORIA ANTIMAFIA RESPONSE - AZIENDA: ".concat(cuaa).concat("STATO: ").concat("C_01"));
				istruttoriaAntimafiaChain.getIstruttoriaAntimafiaEsito().setStato(IstruttoriaAntimafiaStatoEnum.C_01.getStato());
			}
		} catch (Exception e) {
			logger.error("istruttoria antimafia - errore durante lo step di recupero dati da siap", e);
			throw new RuntimeException("istruttoria antimafia - errore durante lo step di recupero dati da siap", e);
		}
	}

}

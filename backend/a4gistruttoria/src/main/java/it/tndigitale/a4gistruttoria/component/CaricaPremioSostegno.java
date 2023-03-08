package it.tndigitale.a4gistruttoria.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public abstract class CaricaPremioSostegno {

	private static final Logger logger = LoggerFactory.getLogger(CaricaPremioSostegno .class);
	
	@Autowired
	private IstruttoriaComponent istruttoriaComponent;
	@Autowired
	protected ObjectMapper objectMapper;

	private static final List<StatoIstruttoria> STATI_ATTESA = Arrays.asList(StatoIstruttoria.RICHIESTO, StatoIstruttoria.CONTROLLI_CALCOLO_KO,
			StatoIstruttoria.CONTROLLI_CALCOLO_OK, StatoIstruttoria.INTEGRATO);

	private static final List<StatoIstruttoria> STATI_ANNULLANO_PREMIO = Arrays.asList(StatoIstruttoria.NON_AMMISSIBILE, StatoIstruttoria.NON_LIQUIDABILE);
	
	public Double getPremio(DomandaUnicaModel domanda) {
		Sostegno sostegno = getSostegno();
		logger.debug("getPremio: Cerco le istruttorie per la domanda {} sul sostegno {}", domanda.getId(), sostegno);
		// mi interessa l'ultima ISTRUTTORIA
		IstruttoriaModel istruttoria =
				istruttoriaComponent.getUltimaIstruttoria(domanda, sostegno);
		logger.debug("getPremio: Trovata istruttoria {} sul sostegno {}", istruttoria.getId(), sostegno);
		return getPremio(istruttoria);
	}
	
	public Double getPremio(IstruttoriaModel istruttoria) {
		if (istruttoria != null) {
			if (isIstruttoriaInLavorazione(istruttoria)) {
				logger.debug("getPremio: istruttoria {} in lavorazione per il sostegno {}", istruttoria.getId(),
						istruttoria.getSostegno());
				return getImportoIstruttoriaInLavorazione(istruttoria);
			}
			if (isIstruttoriaAnnullata(istruttoria)) {
				logger.debug("getPremio: istruttoria {} non ammessa per il sostegno {}", istruttoria.getId(),
						istruttoria.getSostegno());
				return getImportoIstruttoriaAnnullata(istruttoria);
			}
			logger.debug("getPremio: calcolo premio per istruttoria {} del sostegno {} e stato {}", istruttoria.getId(),
					istruttoria.getSostegno(),
					istruttoria.getStato().getStatoIstruttoria());
			return getImportoPremioCalcolato(istruttoria);
		}
		return null;		
	}
	
	public static boolean isStatoInAttesa(StatoIstruttoria stato) {
		return STATI_ATTESA.contains(stato);
	}

	public static boolean isIstruttoriaInLavorazione(IstruttoriaModel istruttoria) {
		return isStatoInAttesa(istruttoria.getStato());
	}
	
	public static boolean isStatoAnnullaImporto(StatoIstruttoria stato) {
		return STATI_ANNULLANO_PREMIO.contains(stato);
	}

	public static boolean isIstruttoriaAnnullata(IstruttoriaModel istruttoria) {
		return isStatoAnnullaImporto(istruttoria.getStato());
	}

	protected Double getImportoPremioCalcolato(IstruttoriaModel istruttoria) {
		TipoVariabile variabile = getVariabileCalcoloPremioSostegno();
		Map<TipoVariabile, BigDecimal> importiCalcolati = getImportoPremioCalcolato(istruttoria, variabile);
		return Optional.ofNullable(importiCalcolati.get(variabile)).map(BigDecimal::doubleValue).orElse(null);
	}

	protected Map<TipoVariabile, BigDecimal> getImportoPremioCalcolato(IstruttoriaModel istruttoria, TipoVariabile... variabili) {
		TransizioneIstruttoriaModel transizioneCalcoloPremio = getTransizioneCalcolo(istruttoria);
		logger.debug("getImportoPremioCalcolato: transizioneCalcoloPremio {} stato 1 {} e stato 2 {} ", transizioneCalcoloPremio.getId(),
				transizioneCalcoloPremio.getA4gdStatoLavSostegno1().getIdentificativo(),
				transizioneCalcoloPremio.getA4gdStatoLavSostegno2().getIdentificativo());
		PassoTransizioneModel passo = getPassoCalcolo(transizioneCalcoloPremio);
		DatiOutput variabiliOutputFinali;
		try {
			variabiliOutputFinali = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);
		} catch (IOException e) {
			logger.error("Errore leggendo i dati di calcolo del passo {} con dati output {}", passo.getId(), passo.getDatiOutput(), e);
			throw new RuntimeException("Errore leggendo i dati di calcolo del passo", e);
		}
		List<TipoVariabile> nomeVariabiliCalcolo = Arrays.asList(variabili);
		return variabiliOutputFinali.getVariabiliCalcolo().stream()
			.filter(v -> nomeVariabiliCalcolo.contains(v.getTipoVariabile()))
			.peek(vc -> logger.debug("vc tipo {} e valore {}", vc.getTipoVariabile(), vc.getValNumber()))
			.collect(Collectors.toMap(VariabileCalcolo::getTipoVariabile, VariabileCalcolo::getValNumber));
	}
	
	protected TransizioneIstruttoriaModel getTransizioneCalcolo(IstruttoriaModel istruttoria) {
		return istruttoria.getTransizioni().stream()
				// prendo le transizioni di calcolo ok
				.filter(t -> StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria().equals(t.getA4gdStatoLavSostegno1().getIdentificativo()))
				// l'ultima
				.max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione))
				.get();
	}
	
	protected PassoTransizioneModel getPassoCalcolo(TransizioneIstruttoriaModel transizioneCalcoloPremio) {
		TipologiaPassoTransizione passoCalcolo = getPassoCalcolo();
		return transizioneCalcoloPremio.getPassiTransizione().stream()
			.filter(p -> passoCalcolo.equals(p.getCodicePasso()))
			.findAny().get();
		
	}
	
	protected Double getImportoIstruttoriaAnnullata(IstruttoriaModel istruttoria) {
		return 0D;
	}
	protected Double getImportoIstruttoriaInLavorazione(IstruttoriaModel istruttoria) {
		return null;
	}
	
	protected abstract Sostegno getSostegno();
	
	protected abstract TipologiaPassoTransizione getPassoCalcolo();
	
	protected abstract TipoVariabile getVariabileCalcoloPremioSostegno();
}

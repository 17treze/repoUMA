package it.tndigitale.a4gistruttoria.action.acs;

import it.tndigitale.a4gistruttoria.component.acs.ControlloCompatibilitaColturaIntervento;
import it.tndigitale.a4gistruttoria.component.acs.ControlloRegioni;
import it.tndigitale.a4gistruttoria.dto.InfoEleggibilitaParticella;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ParticellaColtura;
import it.tndigitale.a4gistruttoria.dto.lavorazione.RichiestaSuperficiePerCalcoloDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static it.tndigitale.a4gistruttoria.util.ConversioniCalcoli.convertiMetriQuadriInEttaro;

public abstract class CalcoloSostegnoAccoppiatoSuperficieConsumer
		implements BiConsumer<RichiestaSuperficiePerCalcoloDto, CalcoloAccoppiatoHandler> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloSostegnoAccoppiatoSuperficieConsumer.class);

	@Autowired
	ControlloRegioni controlloRegioni;

	@Autowired
	ControlloCompatibilitaColturaIntervento compatibilitaColturaIntervento;

	@Override
	public void accept(RichiestaSuperficiePerCalcoloDto richiesta, CalcoloAccoppiatoHandler info) {
		calcolaOutput(richiesta, info, getMisura());
	}

	/**
	 * Specifica il suffisso della misura che viene implementata
	 * @return
	 */
	public abstract String getMisura();

	/**
	 * Genera le variabili di output a partire dall'input e dai controlli richiesti
	 */
	protected void calcolaOutput(RichiestaSuperficiePerCalcoloDto richiesta, CalcoloAccoppiatoHandler info,
                                 String suffissoMisura) {
		logger.debug("calcolaOutput: elaboro la richiesta {} per la misura {}", richiesta.getId(), suffissoMisura);
		MapVariabili variabiliOutput = info.getVariabiliOutput();
		ParticellaColtura particellaColtura = 
				caricaParticellaColtura(variabiliOutput, TipoVariabile.valueOf("ACSCTRLREG_".concat(suffissoMisura)), richiesta);
		if (particellaColtura.getValBool() == null) {
			boolean isControlloRegioni = controlloRegioni.checkControlloRegioni(
					richiesta.getInfoCatastali().getCodNazionale(), richiesta.getCodiceInterventoAgs());
			particellaColtura.setValBool(isControlloRegioni);
			logger.debug("Particella: {} isControlloRegioni: {} per misura {}", richiesta.getInfoCatastali().getIdParticella(),
					isControlloRegioni, suffissoMisura);
		}
		particellaColtura = 
				caricaParticellaColtura(variabiliOutput, TipoVariabile.valueOf("ACSCTRLCOLT_".concat(suffissoMisura)), richiesta);
		if (particellaColtura.getValBool() == null) {
			boolean isCompatibilitaColturaIntervento = compatibilitaColturaIntervento.checkColturaIntervento(
					richiesta.getAnnoCampagna().intValue(), richiesta.getCodiceColtura3(), richiesta.getCodiceInterventoAgs());
			particellaColtura.setValBool(isCompatibilitaColturaIntervento);
			logger.debug("Particella: {} coltura: {} isCompatibilitaColturaIntervento: {} per misura {}",
					richiesta.getInfoCatastali().getIdParticella(), richiesta.getCodiceColtura3(), isCompatibilitaColturaIntervento, suffissoMisura);
		}
		particellaColtura = 
				caricaParticellaColtura(variabiliOutput, TipoVariabile.valueOf("ACSCTRLCOORD_".concat(suffissoMisura)), richiesta);
		if (particellaColtura.getValBool() == null) {
			float superficieAnCoord = getSuperficieAnomaliaCoordinamento(richiesta, info);
				particellaColtura.setValBool(superficieAnCoord > 0);
			logger.debug("Particella: {} e parcella {} isAnomaliaCoordinamento: {} per misura {}", richiesta.getInfoCatastali().getIdParticella(),
					richiesta.getRiferimentiCartografici().getIdParcella(),
					particellaColtura.getValBool(), suffissoMisura);
		}

		particellaColtura = 
				caricaParticellaColtura(variabiliOutput, TipoVariabile.valueOf("ACSPFSUPANCOORD_".concat(suffissoMisura)), richiesta);
		if (particellaColtura.getValNum() == null) {
			float superficieAnCoord = getSuperficieAnomaliaCoordinamento(richiesta, info);
			particellaColtura.setValNum(superficieAnCoord);
			logger.debug("Particella: {} con coltura {} superficieAnCoord: {} per misura {}", richiesta.getInfoCatastali().getIdParticella(),
					richiesta.getCodiceColtura3(),
					superficieAnCoord, suffissoMisura);
		}

		// devo sommare la superficie richiesta
		particellaColtura = 
				caricaParticellaColtura(variabiliOutput, TipoVariabile.valueOf("ACSPFSUPRIC_".concat(suffissoMisura)), richiesta);
		float superficieRichiesta = getSuperficieRichiesta(richiesta);
		if (particellaColtura.getValNum() == null) {
			particellaColtura.setValNum(superficieRichiesta);
		} else {
			particellaColtura.setValNum(ConversioniCalcoli.sommaSuperfici(superficieRichiesta, particellaColtura.getValNum()));
		}
		logger.debug("Particella: {} e coltura: {} superficieRichiesta: {} per misura {}", richiesta.getInfoCatastali().getIdParticella(),
				richiesta.getCodiceColtura3(),
				particellaColtura.getValNum(), suffissoMisura);
		
		// devo superficie eleggibile già aggregata
		particellaColtura = 
				caricaParticellaColtura(variabiliOutput, TipoVariabile.valueOf("ACSPFSUPELEGIS_".concat(suffissoMisura)), richiesta);
		if (particellaColtura.getValNum() == null) {
			float superficieEleggibileGis = getSuperficieEleggibileGis(richiesta, info);
			particellaColtura.setValNum(superficieEleggibileGis);
			logger.debug("Particella: {} con coltura {} superficieEleggibileGis: {} per misura {}", richiesta.getInfoCatastali().getIdParticella(),
					richiesta.getCodiceColtura3(),
					superficieEleggibileGis, suffissoMisura);
		}

		particellaColtura = 
				caricaParticellaColtura(variabiliOutput, TipoVariabile.valueOf("ACSPFSUPCTRLLOCO_".concat(suffissoMisura)), richiesta);
		if (particellaColtura.getValNum() == null) {
			float superficieControlliInLoco = getSuperficieControlliInLoco(richiesta, info);
			particellaColtura.setValNum(superficieControlliInLoco);
			logger.debug("Particella: {} con coltura {} superficieControlliInLoco: {} per misura {}", richiesta.getInfoCatastali().getIdParticella(),
					richiesta.getCodiceColtura3(),
					superficieControlliInLoco, suffissoMisura);
		}

		particellaColtura = 
				caricaParticellaColtura(variabiliOutput, TipoVariabile.valueOf("ACSPFSUPDET_".concat(suffissoMisura)), richiesta);
		if (particellaColtura.getValNum() == null) {
			Float superficieDeterminata = calcolaSuperficieDeterminata(richiesta, info, suffissoMisura);
			particellaColtura.setValNum(superficieDeterminata);
			logger.debug("Particella: {} con coltura {} superficieDeterminata: {} per misura {}", richiesta.getInfoCatastali().getIdParticella(),
					richiesta.getCodiceColtura3(),
					superficieDeterminata, suffissoMisura);
		}
	}
	protected ParticellaColtura caricaParticellaColtura(MapVariabili variabiliOutput, TipoVariabile tipo, RichiestaSuperficiePerCalcoloDto richiesta) {
		ParticellaColtura particellaColtura = null;
		VariabileCalcolo variabile = variabiliOutput.get(tipo);
		if (variabile == null) {
			variabile = new VariabileCalcolo(tipo, new ArrayList<ParticellaColtura>());
			variabiliOutput.add(variabile);
		}
		ArrayList<ParticellaColtura> listaParticelleColtura = variabile.getValList();
		if (listaParticelleColtura == null) {
			listaParticelleColtura = new ArrayList<>();
			variabile.setValList(listaParticelleColtura);
		}
		// cerco la particella coltura della stessa particella e stessa coltura
		Predicate<ParticellaColtura> testStessaParticellaStessaColtura = pc -> pc.getParticella().getIdParticella()
				.equals(richiesta.getInfoCatastali().getIdParticella())
				&& pc.getColtura().equals(richiesta.getCodiceColtura3());
		
		Optional<ParticellaColtura> optionalParticellaColtura = listaParticelleColtura.stream().filter(testStessaParticellaStessaColtura).findFirst();
		if (optionalParticellaColtura.isPresent()) {
			particellaColtura = optionalParticellaColtura.get();
		} else {
			particellaColtura = new ParticellaColtura();
			particellaColtura.setColtura(richiesta.getCodiceColtura3());
			particellaColtura.setParticella(richiesta.getInfoCatastali());
			listaParticelleColtura.add(particellaColtura);
		}
		return particellaColtura;
	}

	/**
	 * Recupera la superficie richiesta (netta) per la particella a partire dalle
	 * info precaricate
	 */
	protected float getSuperficieRichiesta(RichiestaSuperficiePerCalcoloDto richiesta) {
		return convertiMetriQuadriInEttaro(richiesta.getSupRichiestaNetta().floatValue());
	}

	/**
	 * Recupera la superficie eleggibile Gis per la particella a partire dalle info
	 * precaricate
	 */
	protected float getSuperficieEleggibileGis(RichiestaSuperficiePerCalcoloDto richiesta,
                                               CalcoloAccoppiatoHandler info) {
		// betty: mail di michele lugo 13.03.2019 -> leggo sostegno BPS
		Long sommaSuperficieEleggibile = info.getInfoIstruttoriaDomanda().getDatiEleggibilita().stream()
				.filter(filtraBPSParticellaColtura(richiesta))
				.collect(Collectors.summingLong(InfoEleggibilitaParticella::getSuperficieGis));
		return convertiMetriQuadriInEttaro(sommaSuperficieEleggibile.floatValue());
	}

	/**
	 * Recupera la superficie Anomalia Coordinamento per la particella a partire dalle info
	 * precaricate
	 */
	protected float getSuperficieAnomaliaCoordinamento(RichiestaSuperficiePerCalcoloDto richiesta,
			CalcoloAccoppiatoHandler info) {
		Long sommaSuperficieAnCoord = info.getInfoIstruttoriaDomanda().getDatiEleggibilita().stream()
				.filter(filtraBPSParticellaColtura(richiesta))
				.collect(Collectors.summingLong(InfoEleggibilitaParticella::getSuperficieAnomaliaCoor));
		return convertiMetriQuadriInEttaro(sommaSuperficieAnCoord.floatValue());
	}

	protected Predicate<InfoEleggibilitaParticella> filtraBPSParticellaColtura(RichiestaSuperficiePerCalcoloDto richiesta) {
		return p -> "BPS".equals(p.getSostegno())
				&& p.getParticella().getIdParticella().equals(richiesta.getInfoCatastali().getIdParticella())
				&& p.getCodColtura3().equals(richiesta.getCodiceColtura3());
	}

	/**
	 * Recupera la superficie da controlli in loco per la particella a partire dalle
	 * info precaricate
	 */
	protected Float getSuperficieControlliInLoco(RichiestaSuperficiePerCalcoloDto richiesta,
                                                 CalcoloAccoppiatoHandler info) {

		Float result = Float.valueOf(0);
		Boolean isControlloSigeco = getBooleanWhenNotNull(info, TipoVariabile.DOMSIGECOCHIUSA);
		Boolean isAziendaCampione = getBooleanWhenNotNull(info, TipoVariabile.ISCAMP);
		if (Boolean.TRUE.equals(isControlloSigeco) && Boolean.TRUE.equals(isAziendaCampione)) {
			Long sommaSuperficieSigeco = info.getInfoIstruttoriaDomanda().getDatiEleggibilita().stream()
					.filter(filtraBPSParticellaColtura(richiesta))
					.collect(Collectors.summingLong(InfoEleggibilitaParticella::getSuperficieSigeco));
			result = convertiMetriQuadriInEttaro(sommaSuperficieSigeco.floatValue());
		}
		return result;
	}

	protected Boolean getBooleanWhenNotNull(CalcoloAccoppiatoHandler info, TipoVariabile tipoVariabili) {
		VariabileCalcolo vc = info.getVariabiliInput().get(tipoVariabili);
		if (vc != null) return vc.getValBoolean();
		return null;
	}

	/**
	 * Recupera la superficie determinata per la particella a partire dalle info
	 * precaricate
	 */
	abstract protected Float calcolaSuperficieDeterminata(RichiestaSuperficiePerCalcoloDto richiesta,
            CalcoloAccoppiatoHandler info, String suffissoMisura);
	
	protected boolean isSuperficieValida(RichiestaSuperficiePerCalcoloDto richiesta,
                                         CalcoloAccoppiatoHandler info, String suffissoMisura) {
		Boolean isControlloRegioni = getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSCTRLREG_".concat(suffissoMisura)), Boolean.class);
		Boolean isCompatibilitaColturaIntervento = getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSCTRLCOLT_".concat(suffissoMisura)), Boolean.class);
		Boolean isAnomaliaCoordinamento = getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSCTRLCOORD_".concat(suffissoMisura)), Boolean.class);
		
		return Boolean.TRUE.equals(isControlloRegioni) && Boolean.TRUE.equals(isCompatibilitaColturaIntervento)
				&& Boolean.TRUE.equals(isAnomaliaCoordinamento);
		
	}
	
	protected boolean isControlloRegioni(RichiestaSuperficiePerCalcoloDto richiesta,
			CalcoloAccoppiatoHandler info, String suffissoMisura) {
		return getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSCTRLREG_".concat(suffissoMisura)), Boolean.class);
	}

	protected boolean isCompatibilitaColturaIntervento(RichiestaSuperficiePerCalcoloDto richiesta,
			CalcoloAccoppiatoHandler info, String suffissoMisura) {
		return getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSCTRLCOLT_".concat(suffissoMisura)), Boolean.class);
	}

	protected boolean isAnomaliaCoordinamento(RichiestaSuperficiePerCalcoloDto richiesta,
			CalcoloAccoppiatoHandler info, String suffissoMisura) {
		return getParticellaValue(richiesta, info,
				TipoVariabile.valueOf("ACSCTRLCOORD_".concat(suffissoMisura)), Boolean.class);
	}
	
	/**
	 * Recupera un parametro per la particella già inserito nella lista di output
	 */
	protected <T> T getParticellaValue(RichiestaSuperficiePerCalcoloDto richiesta, CalcoloAccoppiatoHandler info,
                                       TipoVariabile tipo, Class<T> type) {
		ParticellaColtura particellaColtura = 
				caricaParticellaColtura(info.getVariabiliOutput(), tipo, richiesta);
		if (Boolean.class.isAssignableFrom(type)) {
			return type.cast(particellaColtura.getValBool());
		}
		if (Float.class.isAssignableFrom(type)) {
			return type.cast(particellaColtura.getValNum());
		}
		if (String.class.isAssignableFrom(type)) {
			return type.cast(particellaColtura.getValString());
		}
		throw new IllegalArgumentException("Il tipo del valore che si sta recuperando non rientra tra i tipi gestiti.");
	}
}

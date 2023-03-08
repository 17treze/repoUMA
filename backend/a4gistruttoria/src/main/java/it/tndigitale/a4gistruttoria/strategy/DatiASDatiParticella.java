package it.tndigitale.a4gistruttoria.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiParticellaACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioDatiParticellaACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ParticellaColtura;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

@Component
public class DatiASDatiParticella extends DatiDettaglioAbstract {

	private static final Logger logger = LoggerFactory.getLogger(DatiASDatiParticella.class);
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	public DatiParticellaACS recupera(Long idIstruttoria) {

		Set<PassoTransizioneModel> pls = recuperaPassiLavorazione(istruttoriaDao.getOne(idIstruttoria));

		try {
			List<DatiInput> datiInput = pls.stream()
					.map(item -> CustomConverters.jsonConvert(item.getDatiInput(), DatiInput.class))
					.collect(Collectors.toList());
			List<DatiOutput> datiOutput = pls.stream()
					.map(item -> CustomConverters.jsonConvert(item.getDatiOutput(), DatiOutput.class))
					.collect(Collectors.toList());

			DatiParticellaACS datiParticella = new DatiParticellaACS();

			datiParticella.setM8(creaListaDettaglio(datiInput, datiOutput, "M8"));
			datiParticella.setM9(creaListaDettaglio(datiInput, datiOutput, "M9"));
			datiParticella.setM10(creaListaDettaglio(datiInput, datiOutput, "M10"));
			datiParticella.setM11(creaListaDettaglio(datiInput, datiOutput, "M11"));
			datiParticella.setM14(creaListaDettaglio(datiInput, datiOutput, "M14"));
			datiParticella.setM15(creaListaDettaglio(datiInput, datiOutput, "M15"));
			datiParticella.setM16(creaListaDettaglio(datiInput, datiOutput, "M16"));
			datiParticella.setM17(creaListaDettaglio(datiInput, datiOutput, "M17"));

			return datiParticella;

		} catch (Exception e) {
			logger.error("Impossibile recuperare i dati di particella per l'istruttoria {}",
					idIstruttoria, e);
			throw e;
		}
	}

	private List<DettaglioDatiParticellaACS> creaListaDettaglio(List<DatiInput> datiInput, List<DatiOutput> datiOutput,
			String misura) {
		// Questo dettaglio lavora solo sul primo elemento dei passi di lavorazione
		DatiInput input = datiInput.get(0);
		DatiOutput output = datiOutput.get(0);
		if (input.getVariabiliCalcolo().stream()
				.anyMatch(p -> p.getTipoVariabile().equals(TipoVariabile.valueOf("PFSUPIMP_".concat(misura))) && p.getValList() != null)) {
			
			List<DettaglioDatiParticellaACS> result = new ArrayList<>();
			VariabileCalcolo variabileInput = input.getVariabiliCalcolo().stream()
					.filter(p -> p.getTipoVariabile().equals(TipoVariabile.valueOf("PFSUPIMP_".concat(misura))))
					.collect(CustomCollectors.toSingleton());

			variabileInput.getValList().forEach(p -> {
				DettaglioDatiParticellaACS item = new DettaglioDatiParticellaACS();
				item.setCodComune(p.getParticella().getCodNazionale());
				item.setFoglio(p.getParticella().getFoglio());
				item.setParticella(p.getParticella().getParticella());
				item.setSub(p.getParticella().getSub());
				item.setCodColtura(p.getColtura());
				item.setDescColtura(p.getDescColtura());
				item.setSupImpegnata(p.getValNum());

				item.setControlloRegioni(
						getValoreOutputPerParticella("ACSCTRLREG_".concat(misura), p, output, Boolean.class));
				item.setControlloColture(
						getValoreOutputPerParticella("ACSCTRLCOLT_".concat(misura), p, output, Boolean.class));
				item.setControlloAnomCoord(
						getValoreOutputPerParticella("ACSCTRLCOORD_".concat(misura), p, output, Boolean.class));
				item.setSupRichiesta(ConversioniCalcoli.convertiEttariInMetriQuadri(
						getValoreOutputPerParticella("ACSPFSUPRIC_".concat(misura), p, output, Float.class)));
				item.setSupControlliLoco(ConversioniCalcoli.convertiEttariInMetriQuadri(
						getValoreOutputPerParticella("ACSPFSUPCTRLLOCO_".concat(misura), p, output, Float.class)));
				item.setSupEleggibileGis(ConversioniCalcoli.convertiEttariInMetriQuadri(
						getValoreOutputPerParticella("ACSPFSUPELEGIS_".concat(misura), p, output, Float.class)));
				item.setSupDeterminata(ConversioniCalcoli.convertiEttariInMetriQuadri(
						getValoreOutputPerParticella("ACSPFSUPDET_".concat(misura), p, output, Float.class)));
				item.setSuperficieAnomaliaCoor(ConversioniCalcoli.convertiEttariInMetriQuadri(
						getValoreOutputPerParticella("ACSPFSUPANCOORD_".concat(misura), p, output, Float.class)));
				result.add(item);
			});
			return result;
		} else {
			return Collections.emptyList();
		}
	}

	private <T> T getValoreOutputPerParticella(String variabile, ParticellaColtura particellaColtura, DatiOutput datiOutput, Class<T> type) {

		TipoVariabile tipoVariabile = TipoVariabile.valueOf(variabile);
		if (datiOutput.getVariabiliCalcolo().stream().anyMatch(p -> p.getTipoVariabile().equals(tipoVariabile))) {
			// Recupero tra le variabili di calcolo l'unica e sola che corrisponde alla variabile cercata
			VariabileCalcolo v = datiOutput.getVariabiliCalcolo().stream()
					.filter(p -> p.getTipoVariabile().equals(tipoVariabile)).collect(CustomCollectors.toSingleton());
			// Recupero l'unica e sola particella/coltura tra quelle nella lista presente nella variabile di calcolo
			ParticellaColtura item = v.getValList().stream()
					.filter(p -> p.equals(particellaColtura))
					.collect(CustomCollectors.toSingleton());
			if (Boolean.class.isAssignableFrom(type)) {
				return type.cast(item.getValBool());
			} else if (Float.class.isAssignableFrom(type)) {
				return type.cast(item.getValNum());
			} else {
				throw new IllegalArgumentException("Il tipo del valore che si sta recuperando non rientra tra i tipi gestiti.");
			}
		} else return null;
	}
	
	@Override
	protected StatoIstruttoria adjustStatoLavorazioneSostegno(StatoIstruttoria stato) {
		// I dati di particella vengono recuperati dai risultati dell'algoritmo di calcolo, quindi dai passi
		// CONTROLLI_CALCOLO_OK se l'algoritmo è andato a buon fine, altrimenti da CONTROLLI_CALCOLO_KO
		if (StatoIstruttoria.NON_AMMISSIBILE.equals(stato)
				|| StatoIstruttoria.CONTROLLI_CALCOLO_KO.equals(stato))
			return StatoIstruttoria.CONTROLLI_CALCOLO_KO;
		else 
			return StatoIstruttoria.CONTROLLI_CALCOLO_OK;
	}
}

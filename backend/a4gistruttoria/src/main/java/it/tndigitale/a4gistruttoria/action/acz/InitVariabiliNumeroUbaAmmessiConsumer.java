package it.tndigitale.a4gistruttoria.action.acz;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.StatoDomandaIntegrativa;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Component
public class InitVariabiliNumeroUbaAmmessiConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliNumeroUbaAmmessiConsumer.class);
	
	private static final Float COEFFICIENTE_LAT = 1.0f;
	private static final Float COEFFICIENTE_MAC = 0.6f;
	private static final Float COEFFICIENTE_OVI = 0.15f;
	
	private static Map<CodiceInterventoAgs, TipoVariabile> variabiliIntervento;
	static {
		variabiliIntervento = new EnumMap<>(CodiceInterventoAgs.class);
		variabiliIntervento.put(CodiceInterventoAgs.LATTE, TipoVariabile.ACZUBA_LAT);
		variabiliIntervento.put(CodiceInterventoAgs.LATTE_BMONT, TipoVariabile.ACZUBA_LAT);
		variabiliIntervento.put(CodiceInterventoAgs.BOVINI_VAC, TipoVariabile.ACZUBA_LAT);
		variabiliIntervento.put(CodiceInterventoAgs.BOVINI_VAC_NO_ISCR, TipoVariabile.ACZUBA_LAT);
		variabiliIntervento.put(CodiceInterventoAgs.BOVINI_MAC, TipoVariabile.ACZUBA_MAC);
		variabiliIntervento.put(CodiceInterventoAgs.BOVINI_MAC_12M, TipoVariabile.ACZUBA_MAC);
		variabiliIntervento.put(CodiceInterventoAgs.BOVINI_MAC_ETIC, TipoVariabile.ACZUBA_MAC);
		variabiliIntervento.put(CodiceInterventoAgs.OVICAP_AGN, TipoVariabile.ACZUBA_OVI);
		variabiliIntervento.put(CodiceInterventoAgs.OVICAP_MAC, TipoVariabile.ACZUBA_OVI);
	}
	
	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();

		BigDecimal capiLat = BigDecimal.valueOf(sommaCapiUnivoci(TipoVariabile.ACZUBA_LAT, domanda));
		BigDecimal coeffLat = new BigDecimal(COEFFICIENTE_LAT.toString());
		BigDecimal ubaLat = capiLat.multiply(coeffLat).setScale(2, BigDecimal.ROUND_HALF_UP);
		handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.ACZUBA_LAT, ubaLat));
		logger.debug("Numero UBA per la variabile ACZUBA_LAT: {}", ubaLat);
		
		BigDecimal capiMac = BigDecimal.valueOf(sommaCapiUnivoci(TipoVariabile.ACZUBA_MAC, domanda));
		BigDecimal coeffMac = new BigDecimal(COEFFICIENTE_MAC.toString());
		BigDecimal ubaMac = capiMac.multiply(coeffMac).setScale(2, BigDecimal.ROUND_HALF_UP);
		handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.ACZUBA_MAC, ubaMac));
		logger.debug("Numero UBA per la variabile ACZUBA_MAC: {}", ubaMac);
		
		BigDecimal capiOvi = BigDecimal.valueOf(sommaCapiUnivoci(TipoVariabile.ACZUBA_OVI, domanda));
		BigDecimal coeffOvi = new BigDecimal(COEFFICIENTE_OVI.toString());
		BigDecimal ubaOvi = capiOvi.multiply(coeffOvi).setScale(2, BigDecimal.ROUND_HALF_UP);
		handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.ACZUBA_OVI, ubaOvi));
		logger.debug("Numero UBA per la variabile ACZUBA_OVI: {}", ubaOvi);
		
		BigDecimal ubaTot = ubaLat.add(ubaMac).add(ubaOvi);
		handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.ACZUBATOT, ubaTot));
		logger.debug("Numero UBA totale: {}", ubaTot);
	}
	
	protected Integer sommaCapiUnivoci(TipoVariabile tipoVariabile, DomandaUnicaModel domanda) {
		Set<String> result = new HashSet<>();
		variabiliIntervento.forEach((key, value) -> {
			if (value.equals(tipoVariabile))
				recuperaCapiPerIntervento(result, domanda, key);
		});
		return result.size();
	}
	
	protected Set<String> recuperaCapiPerIntervento(Set<String> capi, DomandaUnicaModel domanda, CodiceInterventoAgs intervento) {
		Set<AllevamentoImpegnatoModel> allevamentiPerIntervento =
				domanda.getAllevamentiImpegnati().stream().filter(allevamento ->
						allevamento.getIntervento().getIdentificativoIntervento().equals(intervento)
				).collect(Collectors.toSet());

		allevamentiPerIntervento.forEach(allevamento -> {
			List<EsitoCalcoloCapoModel> esitiAllevamento = allevamento.getEsitiCalcoloCapi()
					.stream()
					.filter(p -> //!p.getA4gtDomandaIntegrativas().isEmpty()
							//&& StatoDomandaIntegrativa.PRESENTATA.name().equals(p.getA4gtDomandaIntegrativas().get(0).getStato())
							Boolean.TRUE.equals(p.getRichiesto()) &&
							!Boolean.TRUE.equals(p.getDuplicato())
							)
					.collect(Collectors.toList());
			esitiAllevamento.forEach(esito -> capi.add(esito.getCodiceCapo()));
		});
		return capi;
	}
}

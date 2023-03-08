package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.FamigliaInterventi;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.repository.dao.CapoTrackingDao;
import it.tndigitale.a4gistruttoria.repository.dao.EsitoCalcoloCapoDao;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;

@Component
public class CalcoloCapiHandler {
	
	@Autowired
	private CapoTrackingDao capoTrackingDao;
	@Autowired
	private EsitoCalcoloCapoDao esitoCalcoloCapoDao;
	@Value("${zootecnia.interventi.vacchenutricielatte}")
	private String[] interventiCodiciVaccheLatte;
	@Value("${zootecnia.interventi.vacchemacello}")
	private String[] interventiVacchemacello;
	@Value("${zootecnia.interventi.ovicaprini}")
	private String[] interventiOvicaprini;
	@Value("${zootecnia.interventi.vacchemontagna}")
	private String[] interventiCodiciVaccheLatteMontagna;
	@Value("${zootecnia.interventi.controlli.latte}")
	private String[] interventiControlliLatte;
	
	public Consumer<AllevamentoImpegnatoModel> deleteElement() {
		return (a4gtRichiestaAllevamDu -> {
					// pulizia esiti e domanda integrativa nel caso di ricalcoli
					a4gtRichiestaAllevamDu.getEsitiCalcoloCapi().forEach(a4gtRichAllevamDuEsito -> {
						capoTrackingDao.deleteAll(a4gtRichAllevamDuEsito.getA4gtCapoTrackings());
						a4gtRichAllevamDuEsito.getA4gtCapoTrackings().clear();
					});
					esitoCalcoloCapoDao.deleteAll(a4gtRichiestaAllevamDu.getEsitiCalcoloCapi());
					a4gtRichiestaAllevamDu.getEsitiCalcoloCapi().clear();
				});
	}
	
	
	public Function<AllevamentoImpegnatoModel, RichiestaAllevamDu> creaDto(Integer campagna, String cuaaSubentrante) {
		return (a4gtRichiestaAllevamDu -> {
			// creazione DTO
			RichiestaAllevamDu richiestaAllevamDu = new RichiestaAllevamDu();
			BeanUtils.copyProperties(a4gtRichiestaAllevamDu, richiestaAllevamDu);
			richiestaAllevamDu.setCuaaIntestatario(a4gtRichiestaAllevamDu.getDomandaUnica().getCuaaIntestatario());
			richiestaAllevamDu.setIdDomanda(a4gtRichiestaAllevamDu.getDomandaUnica().getId());
			richiestaAllevamDu.setCodiceIntervento(a4gtRichiestaAllevamDu.getIntervento().getCodiceAgea());
			richiestaAllevamDu.setIntervento(a4gtRichiestaAllevamDu.getIntervento().getIdentificativo());
			richiestaAllevamDu.setCampagna(campagna);
			richiestaAllevamDu.setCuaaSubentrante(cuaaSubentrante);
			return richiestaAllevamDu;
		});
	}
	
	public double calcoloUBA(Set<AllevamentoImpegnatoModel> sostegniZootecnici) {
		// calcolo UBA e cambio stato
		// - Interventi 310-314 e 322: UBA = totale * 1
		// - Interventi 315-319: UBA = totale * 0,6
		// - Interventi 320-321: UBA = totale * 0,15
		Function<EsitoCalcoloCapoModel, FamigliaInterventi> interventi2Group = a4gtRichAllevamDuEsito -> {
			String codiceIntervento = a4gtRichAllevamDuEsito.getAllevamentoImpegnato().getIntervento().getCodiceAgea();
			if (Arrays.asList(interventiCodiciVaccheLatte).contains(codiceIntervento)) {
				return FamigliaInterventi.VACCHE_LATTE;
			}
			if (Arrays.asList(interventiVacchemacello).contains(codiceIntervento)) {
				return FamigliaInterventi.VACCHE_MACELLO;
			}
			if (Arrays.asList(interventiOvicaprini).contains(codiceIntervento)) {
				return FamigliaInterventi.OVICAPRINI;
			}
			if (Arrays.asList(interventiCodiciVaccheLatteMontagna).contains(codiceIntervento)) {
				return FamigliaInterventi.VACCHE_LATTE;
			}
			throw new RuntimeException("Riconoscimento codice Intervento fallito: ".concat(codiceIntervento));
		};
		// raggruppo per FAMIGLIA interventi
		Map<FamigliaInterventi, List<EsitoCalcoloCapoModel>> esitiPerFamiglia = sostegniZootecnici.stream()
				.flatMap(a4gtRichiestaAllevamDu -> a4gtRichiestaAllevamDu.getEsitiCalcoloCapi().stream()) // Flattening la lista degli esiti
				.collect(Collectors.toList())
				.stream()
				.collect(Collectors.groupingBy(interventi2Group));
		// distinct vacche per famiglia elementi e calcolo UBA
		return esitiPerFamiglia.entrySet().stream().mapToDouble(e -> {
				long count = e.getValue()
						.stream()
						.filter(a4gtRichAllevamDuEsito -> !EsitoCalcoloCapo.NON_AMMISSIBILE.equals(a4gtRichAllevamDuEsito.getEsito()))
						.filter(distinctByKey(EsitoCalcoloCapoModel::getCodiceCapo))//così da evitare doppioni
						.count();
				double calcoloUba = 0d;
				switch (e.getKey()) {
					case VACCHE_LATTE:
						calcoloUba = count * 1.00d;
						break;
					case VACCHE_MACELLO:
						calcoloUba = count * 0.60d;
						break;
					case OVICAPRINI:
						calcoloUba = count * 0.15d;
						break;
				}
				return calcoloUba;
			})
			.sum();	
	}
	
	static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	


}

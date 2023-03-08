package it.tndigitale.a4gistruttoria.action;

import it.tndigitale.a4gistruttoria.dto.DomandaIntegrativaDto;
import it.tndigitale.a4gistruttoria.dto.FamigliaInterventi;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaIntegrativaDao;
import it.tndigitale.a4gistruttoria.repository.dao.EsitoCalcoloCapoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandaIntegrativa;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.util.StatoDomandaIntegrativa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DomandaIntegrativaValidatorConsumer implements Consumer<DomandaIntegrativaDto.CapoDto> {

	@Autowired
	private DomandaIntegrativaDao domandaIntegrativaDao;
	@Autowired
	private EsitoCalcoloCapoDao esitoCalcoloCapoDao;
	@Value("${zootecnia.domandaintegrativa.interventi.bovini}")
	private String[] interventiCodiciVaccheLatte;
	@Value("${zootecnia.domandaintegrativa.interventi.vacchemacello}")
	private String[] interventiCodiciVaccheMacello;
	@Value("${zootecnia.domandaintegrativainterventi.ovicaprini}")
	private String[] interventiCodiciOvini;

	@Override
	@Transactional
	public void accept(DomandaIntegrativaDto.CapoDto capoDto) {
		// Funzione che raggruppa gli interventi per famiglia
		Function<DomandaIntegrativaDto.InterventoDto, FamigliaInterventi> interventi2Group = interventoDto -> {
			String codiceIntervento = interventoDto.getCodice();
			if (Arrays.asList(interventiCodiciVaccheLatte).contains(codiceIntervento)) {
				return FamigliaInterventi.VACCHE_LATTE;
			}
			if (Arrays.asList(interventiCodiciVaccheMacello).contains(codiceIntervento)) {
				return FamigliaInterventi.VACCHE_MACELLO;
			}
			if (Arrays.asList(interventiCodiciOvini).contains(codiceIntervento)) {
				return FamigliaInterventi.OVICAPRINI;
			}
			throw new RuntimeException("Riconoscimento codice Intervento fallito: ".concat(codiceIntervento));
		};

		// Consumer utilizzato per il salvataggio degli esiti in base alle business roules CTRLIDUACZ005
		Consumer<DomandaIntegrativaDto.InterventoDto> inserimentoEsitiDomandaIntegrativaConsumer = interventoDto -> {
			A4gtDomandaIntegrativa a4gtDomandaIntegrativa = new A4gtDomandaIntegrativa();
			EsitoCalcoloCapoModel a4gtRichAllevamDuEsito = esitoCalcoloCapoDao.getOne(interventoDto.getIdEsito());
			a4gtDomandaIntegrativa.setEsitoCalcoloCapo(a4gtRichAllevamDuEsito);
			a4gtDomandaIntegrativa.setDtUltimoAggiornamento(LocalDateTime.now());
			a4gtDomandaIntegrativa.setStato(StatoDomandaIntegrativa.CALCOLATO.toString());
			domandaIntegrativaDao.save(a4gtDomandaIntegrativa);
		};

		// Raggruppo gli interventi per famiglia: VACCHE DA LATTE,VACCHE DA MACELLO,OVICAPRINI
		Map<FamigliaInterventi, List<DomandaIntegrativaDto.InterventoDto>> interventiPerFamiglia = capoDto.getInterventi().stream().collect(Collectors.groupingBy(interventi2Group));

		interventiPerFamiglia.entrySet().stream().forEach(interventoPerFamiglia -> {

			if (interventoPerFamiglia.getKey().compareTo(FamigliaInterventi.VACCHE_LATTE) == 0) {

				// Per gli interventi 310-311 se entrambi ammissibili la proposta di intervento non è singola ma è di entrambi
				Map<Boolean, List<DomandaIntegrativaDto.InterventoDto>> richiesteAllevamDuPartitioningByIntervento = capoDto.getInterventi().stream()
						.collect(Collectors.partitioningBy(interventoDto -> Arrays.asList("310", "311").contains(interventoDto.getCodice())));

				if (!CollectionUtils.isEmpty(richiesteAllevamDuPartitioningByIntervento.get(true)) && richiesteAllevamDuPartitioningByIntervento.get(true).stream().allMatch(
						p -> p.getEsito().equals(EsitoCalcoloCapo.AMMISSIBILE.toString()) || p.getEsito().equals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE.toString()))) {
					richiesteAllevamDuPartitioningByIntervento.get(true).forEach(inserimentoEsitiDomandaIntegrativaConsumer);// da salvare gli esiti con flag a true
				} else {

					capoDto.getInterventi().stream().sorted(Comparator.comparing(DomandaIntegrativaDto.InterventoDto::getCodice)).filter(
							p -> p.getEsito().equals(EsitoCalcoloCapo.AMMISSIBILE.toString()) || p.getEsito().equals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE.toString()))
							.findFirst().ifPresent(inserimentoEsitiDomandaIntegrativaConsumer);
				}
			} else if (interventoPerFamiglia.getKey().compareTo(FamigliaInterventi.VACCHE_MACELLO) == 0) {
				capoDto.getInterventi().stream().sorted(Comparator.comparing(DomandaIntegrativaDto.InterventoDto::getCodice).reversed()).filter(
						p -> p.getEsito().equals(EsitoCalcoloCapo.AMMISSIBILE.toString()) || p.getEsito().equals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE.toString()))
						.findFirst().ifPresent(inserimentoEsitiDomandaIntegrativaConsumer);
			} else {

				capoDto.getInterventi().stream().sorted(Comparator.comparing(DomandaIntegrativaDto.InterventoDto::getCodice)).filter(p -> {
					return p.getEsito().equals(EsitoCalcoloCapo.AMMISSIBILE.toString()) || p.getEsito().equals(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE.toString());
				}).findFirst().ifPresent(inserimentoEsitiDomandaIntegrativaConsumer);

			}
		});
	}

}

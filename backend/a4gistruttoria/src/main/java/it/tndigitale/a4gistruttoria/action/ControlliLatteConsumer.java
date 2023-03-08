package it.tndigitale.a4gistruttoria.action;

import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.EsitoControlliLatte;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.repository.dao.AnalisiLatteDao;
import it.tndigitale.a4gistruttoria.repository.dao.ProduzioneLatteDao;
import it.tndigitale.a4gistruttoria.repository.dao.RegAlpeggioDao;
import it.tndigitale.a4gistruttoria.repository.dao.RegistroDopDao;
import it.tndigitale.a4gistruttoria.repository.model.Intervento;
import it.tndigitale.a4gistruttoria.repository.model.RegistroAlpeggioModel;
import it.tndigitale.a4gistruttoria.repository.model.RegistroAnalisiLatteModel;
import it.tndigitale.a4gistruttoria.repository.model.RegistroProduzioneLatteModel;
import it.tndigitale.a4gistruttoria.util.EsitoControlliLatteEnum;

@Component
public class ControlliLatteConsumer implements Function<RichiestaAllevamDu,EsitoControlliLatte> {

	@Value("${zootecnia.interventi.controlli.latte}")
	private String[] interventiControlliLatte;

	@Autowired
	private AnalisiLatteDao analisiLatteDao;
	@Autowired
	private ProduzioneLatteDao produzioneLatteDao;
	@Autowired 
	private RegAlpeggioDao regAlpeggioDao;
	@Autowired
	private RegistroDopDao registroDopDao;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


	BiPredicate<List<RegistroAnalisiLatteModel>, Double> isCelluleSomaticheNelLimite = (registro, limite) -> calcolaContenutoCelluleSomatiche(registro) < limite;
	BiPredicate<List<RegistroAnalisiLatteModel>, Double> isCaricaBattericaNelLimite = (registro, limite) -> calcolaContenutoCaricaBatterica(registro) < limite;
	BiPredicate<List<RegistroAnalisiLatteModel>, Double> isContenutoDiProteinaSufficiente = (registro, valMinimo) -> calcolaContenutoProteico(registro) > valMinimo;

	@Override
	public EsitoControlliLatte apply(RichiestaAllevamDu richiestaAllevamDu) {
		EsitoControlliLatte esito= Stream.of(checkProduzioneLatte322,checkProduzioneLatte,checkAnalisiLatte,checkEsami)
				.map(f -> f.apply( richiestaAllevamDu ))
				.filter( Optional::isPresent )
				.map( Optional::get )
				.findFirst()
				.orElseThrow( () -> new RuntimeException( "Errore generico Controlli Latte! " ) );
		esito.setRichiestaAllevamDu(richiestaAllevamDu);
		return esito;
	}

	//intervento 322 - BR1 Controllo della presenza di consegne o vendite dirette annue
	Function<RichiestaAllevamDu,Optional<EsitoControlliLatte>>  checkProduzioneLatte322 = richiestaAllevamDu -> {
		if (!"322".equals(richiestaAllevamDu.getCodiceIntervento())) return Optional.empty();
		String annoCampagna = richiestaAllevamDu.getCampagna().toString();
		RegistroProduzioneLatteModel registroProduzioneLatteModelFilter = new RegistroProduzioneLatteModel();
		registroProduzioneLatteModelFilter.setCampagna(richiestaAllevamDu.getCampagna());
		registroProduzioneLatteModelFilter.setCuaa(richiestaAllevamDu.getCuaaIntestatario());
		List<RegistroProduzioneLatteModel> registroProduzioneLatteModel = produzioneLatteDao.findAll(Example.of(registroProduzioneLatteModelFilter));
		// In caso di assenza di registrazioni tutti i capi non ammessi return Optional.of(false);
		if (registroProduzioneLatteModel.isEmpty()) {
			return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.OK,""));
		}
		return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.KO_PRESENZA_CONSEGNE,
				MessageFormat.format("Il capo non è ammesso in quanto l’intervento 322 è riservato ad allevamenti non iscritti alla BDN come allevamenti da latte e "
						+ "l’azienda ha prodotto latte nell’anno in cui ha richiesto l’intervento ({0})",
						annoCampagna)
				));
	};

	//BR1 Controllo della presenza di consegne o vendite dirette annue
	Function<RichiestaAllevamDu,Optional<EsitoControlliLatte>>  checkProduzioneLatte = richiestaAllevamDu -> {
		String annoCampagna = richiestaAllevamDu.getCampagna().toString();
		RegistroProduzioneLatteModel registroProduzioneLatteModelFilter = new RegistroProduzioneLatteModel();
		registroProduzioneLatteModelFilter.setCampagna(richiestaAllevamDu.getCampagna());
		registroProduzioneLatteModelFilter.setCuaa(richiestaAllevamDu.getCuaaIntestatario());
		List<RegistroProduzioneLatteModel> registroProduzioneLatteModel = produzioneLatteDao.findAll(Example.of(registroProduzioneLatteModelFilter));
		// In caso di assenza di registrazioni tutti i capi non ammessi ritorna Optional of false;
		if (registroProduzioneLatteModel.isEmpty()) {
			return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.KO_PRESENZA_CONSEGNE,
					MessageFormat.format("Il capo non è ammesso in quanto l’azienda non ha alcuna consegna o vendita di "
							+ "latte durante l’anno di campagna ({0})",
							annoCampagna)
					));
		}
		return Optional.empty();
	};

	//BR2 Controllo della presenza di un’analisi del latte nei mesi in cui ci sono state le consegne o le vendite dirette
	//BR3 Controllo della presenza dell’attestazione di alpeggio delle vacche tra il primo di giugno ed il trenta settembre
	Function<RichiestaAllevamDu,Optional<EsitoControlliLatte>>  checkAnalisiLatte = richiestaAllevamDu -> {
		//find produzione latte
		RegistroProduzioneLatteModel registroProduzioneLatteModelFilter = new RegistroProduzioneLatteModel();
		String cuaa=richiestaAllevamDu.getCuaaIntestatario();
		registroProduzioneLatteModelFilter.setCampagna(richiestaAllevamDu.getCampagna());
		registroProduzioneLatteModelFilter.setCuaa(cuaa);
		List<RegistroProduzioneLatteModel> listaRegistroProduzioneLatteModel = produzioneLatteDao.findAll(Example.of(registroProduzioneLatteModelFilter));
		//find analisi latte
		RegistroAnalisiLatteModel registroAnalisiLattefilterModel = new RegistroAnalisiLatteModel();
		registroAnalisiLattefilterModel.setCampagna(richiestaAllevamDu.getCampagna());
		registroAnalisiLattefilterModel.setCuaa(cuaa);
		List<RegistroAnalisiLatteModel> listaRegistroAnalisiLatteModel = analisiLatteDao.findAll(Example.of(registroAnalisiLattefilterModel));
		//per ogni mese di consegna o vendita
		//check se presente almeno un’analisi nell’archivio delle analisi 
		Function<Date, Integer> monthMap= date ->{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar.get(Calendar.MONTH);
		};
		List<Integer>  checkPresenza= listaRegistroProduzioneLatteModel.stream()
				.map(RegistroProduzioneLatteModel::getDtConsVend)
				.map(monthMap)
				.distinct()
				.filter(meseConsegna -> 
				listaRegistroAnalisiLatteModel.stream()
				.map(RegistroAnalisiLatteModel::getDtAnalisi)
				.map(monthMap)
				.distinct()
				.noneMatch(meseAnalisi -> meseAnalisi.equals(meseConsegna))
						)
				.collect(Collectors.toList());
		if (!checkPresenza.isEmpty()){
			//check BR3: alpeggio
			//se l’assenza è tra gennaio e maggio o tra ottobre e settembre, tutti i capi non sono ammessi all’intervento richiesto.
			if (checkPresenza.stream().anyMatch(meseConsegna -> meseConsegna<Calendar.JUNE || meseConsegna>Calendar.SEPTEMBER)) {
				return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.KO_MESI_SENZA_ANALISI,
						MessageFormat.format("Il capo non è ammesso perché non sono state effettuate le analisi nel mese di {0} ",
								checkPresenza.stream().map(mese -> new DateFormatSymbols(Locale.ITALY).getMonths()[mese]).collect(Collectors.joining(", ")))
						));
			}
			RegistroAlpeggioModel registroAlpeggiofilterModel =new RegistroAlpeggioModel();
			registroAlpeggiofilterModel.setCampagna(richiestaAllevamDu.getCampagna());
			registroAlpeggiofilterModel.setCuaa(cuaa);
			//find aleggio
			Optional<RegistroAlpeggioModel> a4gtRegAlpeggio= regAlpeggioDao.findOne(Example.of(registroAlpeggiofilterModel));
			//consegne di latte durante l’anno di campagna ma che non ha le analisi del latte
			boolean isAlpeggioOk= listaRegistroProduzioneLatteModel.stream()
					.map(RegistroProduzioneLatteModel::getDtConsVend)
					.map(monthMap)
					.distinct()
					.filter(meseConsegna -> 
					listaRegistroAnalisiLatteModel.stream()
					.map(RegistroAnalisiLatteModel::getDtAnalisi)
					.map(monthMap)
					.distinct()
					.noneMatch(meseAnalisi -> meseAnalisi.equals(meseConsegna)))
					//check assenza delle analisi è limitato al periodo in cui i capi erano all’alpeggio
					.allMatch(meseConsegna -> {
						if (!a4gtRegAlpeggio.isPresent()) return false;
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(a4gtRegAlpeggio.get().getDtInizio());
						int meseInizio=calendar.get(Calendar.MONTH);
						calendar.setTime(a4gtRegAlpeggio.get().getDtFine());
						int meseFine=calendar.get(Calendar.MONTH);
						return (meseConsegna>=meseInizio && meseConsegna<=meseFine);
					});
			if (!isAlpeggioOk) {
				String resultMessaggio=						
						MessageFormat.format("Il capo non è ammesso perché non sono state effettuate le analisi nel mese di {0} ",
								checkPresenza.stream().map(mese -> new DateFormatSymbols(Locale.ITALY).getMonths()[mese]).collect(Collectors.joining(", ")));
				if (a4gtRegAlpeggio.isPresent()) {
					resultMessaggio=resultMessaggio.concat(						
							MessageFormat.format( " e l’azienda ha presentato attestazione di alpeggio dal {0} al {1}",
									a4gtRegAlpeggio.isPresent()?dateFormat.format(a4gtRegAlpeggio.get().getDtInizio()):"",
											a4gtRegAlpeggio.isPresent()?dateFormat.format(a4gtRegAlpeggio.get().getDtFine()):"")
							);
				}
				return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.KO_MESI_SENZA_ANALISI,resultMessaggio));
			}

		}
		return Optional.empty();
	};
	//BR4 Controllo dei requisiti qualitativi ed igienico sanitari delle analisi
	Function<RichiestaAllevamDu,Optional<EsitoControlliLatte>>  checkEsami = richiestaAllevamDu -> {

		String annoCampagna = richiestaAllevamDu.getCampagna().toString();
		RegistroAnalisiLatteModel registroAnalisiLatteModelFilter = new RegistroAnalisiLatteModel();
		registroAnalisiLatteModelFilter.setCampagna(richiestaAllevamDu.getCampagna());
		registroAnalisiLatteModelFilter.setCuaa(richiestaAllevamDu.getCuaaIntestatario());
		List<RegistroAnalisiLatteModel> registroAnalisiLatteModel = analisiLatteDao.findAll(Example.of(registroAnalisiLatteModelFilter));

		// email ricevuta da Lorenzo e Polla il 22/03/2019
		if (registroAnalisiLatteModel.isEmpty()) { return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.OK,"")); }

		long regoleVerificate = checkLimiti(registroAnalisiLatteModel, 40.0D, 300D, 3.35D);

		// se azienda NON DOP e intervento 310
		var isAziendaDop = registroDopDao.existsByCuaaAndAnno(richiestaAllevamDu.getCuaaIntestatario(), richiestaAllevamDu.getCampagna());
		if (!isAziendaDop && Intervento.VACCA_LATTE.equals(richiestaAllevamDu.getIntervento())) {

			if (regoleVerificate <= 1) {
				return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.KO_REQUISITI_QUALITATIVI,getErrMessage(annoCampagna, registroAnalisiLatteModel, Boolean.FALSE)));
			}
			if (regoleVerificate >= 3) {
				return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.OK,""));
			}
			if (regoleVerificate == 2) {
				regoleVerificate = checkLimiti(registroAnalisiLatteModel, 100D, 400D, 3.20D);
				if (regoleVerificate >= 3) {
					return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.OK,""));
				}
				return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.KO_REQUISITI_QUALITATIVI,getErrMessage(annoCampagna, registroAnalisiLatteModel,Boolean.FALSE)));
			}
		} else { 
			if (regoleVerificate >= 1) {
				return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.OK,""));
			}
			return Optional.of(new EsitoControlliLatte(EsitoControlliLatteEnum.KO_REQUISITI_QUALITATIVI, getErrMessage(annoCampagna, registroAnalisiLatteModel,Boolean.TRUE)));
		}

		return null;
	};

	private String getErrMessage(String campagna, List<RegistroAnalisiLatteModel> registroAnalisiLatteModel, Boolean isDop) {
		var str = isDop.booleanValue() ? "[CONTROLLO DOP] " : "[CONTROLLO NO DOP] ";
		return MessageFormat.format(str.concat("Il capo non è ammesso in quanto l’azienda non ha rispettato i requisiti qualitativi ed igienico "
				+ "sanitari sulle analisi del latte durante l’anno di campagna ({0}): il tenore di cellule "
				+ "somatiche (per ml) è pari a {1} ml ed è superiore a 300.000 ml; il tenore di carica batterica a 30° (per ml) "
				+ "è pari a {2} ml ed è superiore a 40.000 ml; il contenuto di proteina è pari a {3} gr ed è inferiore a 3,35 gr "
				+ "per 100 ml."),
				campagna,
				calcolaContenutoCelluleSomatiche(registroAnalisiLatteModel)*1000,
				calcolaContenutoCaricaBatterica(registroAnalisiLatteModel)*1000,
				calcolaContenutoProteico(registroAnalisiLatteModel));
	}


	private long checkLimiti(List<RegistroAnalisiLatteModel> registroAnalisiLatteModel, Double limiteCaricaBatterica, Double limiteCelluleSomatiche, Double valMinimoProteine) {
		List<Boolean> verifiche = new ArrayList<>();
		verifiche.add(isCelluleSomaticheNelLimite.test(registroAnalisiLatteModel, limiteCelluleSomatiche));
		verifiche.add(isCaricaBattericaNelLimite.test(registroAnalisiLatteModel, limiteCaricaBatterica));
		verifiche.add(isContenutoDiProteinaSufficiente.test(registroAnalisiLatteModel, valMinimoProteine));

		return verifiche.stream().filter(x -> x).count();
	}

	private Double calcolaContenutoCelluleSomatiche(List<RegistroAnalisiLatteModel> registro) {
		return Math.exp(registro
				.stream()
				.map(RegistroAnalisiLatteModel::getCelluleSomatiche)
				.mapToDouble(x -> Math.log(x.doubleValue()))
				.average()
				.orElseThrow(() -> new RuntimeException("Errore nel calcolo della media geometrica delle cellule somatiche")));
	}

	private Double calcolaContenutoCaricaBatterica(List<RegistroAnalisiLatteModel> registro) {
		return Math.exp(registro
				.stream()
				.map(RegistroAnalisiLatteModel::getCaricaBatterica)
				.mapToDouble(x -> Math.log(x.doubleValue()))
				.average()
				.orElseThrow(() -> new RuntimeException("Errore nel calcolo della media geometrica della carica batterica")));
	}

	private Double calcolaContenutoProteico(List<RegistroAnalisiLatteModel> registro) {
		return registro
				.stream()
				.mapToDouble(x -> x.getContenutoProteina().doubleValue())
				.average()
				.orElseThrow(() -> new RuntimeException("Errore nel calcolo della media aritmetica del contenuto proteine"));

	}
}

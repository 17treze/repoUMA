package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import static it.tndigitale.a4gistruttoria.util.ControlloCapiUtil.getPrimoParto;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoLatteDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloDto;
import it.tndigitale.a4gistruttoria.repository.dao.AllevMontagnaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtAllevMontagna;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.ControlliCapiLatteMontagnaFactory.CapoLatteMontagnaCheckInterface;
import it.tndigitale.a4gistruttoria.util.ControlloCapiUtil;

@Component
public class ControlloCapoVaccaLatteMontagna extends ControlloCapoVaccaLatte implements BiFunction<RichiestaAllevamDu,CapoDto,EsitoCalcoloCapoModel> {

	private static final Logger logger = LoggerFactory.getLogger(ControlloCapoVaccaLatteMontagna.class);

	@Autowired
	private ObjectMapper objectMapper;
	@Value("${a4g.mail.zootecnia.montagna.to}")
	private String mailTo;
	@Value("${a4g.mail.zootecnia.montagna.oggetto}")
	private String oggetto;
	@Value("${a4g.mail.zootecnia.montagna.messaggio}")
	private String messaggio;
	@Value("${zootecnia.interventi.vacchemontagna.province}")
	private String[] provinceConsentite;
	@Autowired
	private AllevMontagnaDao allevMontagnaDao;
	@Autowired
	private ControlliCapiLatteMontagnaFactory controlliCapiLatteMontagnaFactory;

	@Override
	public EsitoCalcoloCapoModel apply(RichiestaAllevamDu richiestaAllevamDu, CapoDto capoDto) {
		EsitoCalcoloCapoModel esitoCalcoloCapo=
				Stream.of(
						checkPresenzaInformazioni(),
						checkPrimoParto(),
						checkAllevamentoMontagna(),
						checkLimiteEta(),
						checkIdentificazioneRegistrazione(), 
						checkMovimentazione()
						)
				.map(f -> f.apply((CapoLatteDto)capoDto, richiestaAllevamDu ))
				.filter( Optional::isPresent )
				.map( Optional::get )
				.findFirst()
				.orElseThrow( () -> new RuntimeException( "Errore generico calcolo capo Vacca Latte Montagna! " ) );
		esitoCalcoloCapo.setCapoId(Long.parseLong(capoDto.getIdCapo()));
		esitoCalcoloCapo.setCodiceCapo(capoDto.getMarcaAuricolare());		
		return esitoCalcoloCapo;		
	}


	//BR8 Check Presenza informazioni
	@Override
	protected BiFunction<CapoLatteDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>> checkPresenzaInformazioni() {
		return (capoLatte, richiestaAllevamDu) -> {
			Optional<VitelloDto> primoPartoOpt = getPrimoParto(capoLatte);

			if (!primoPartoOpt.isPresent()) {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE, "Il capo non è " +
						"ammissibile all’aiuto perchè le informazioni presenti nella Banca Dati Nazionale (BDN) "
						+ "informatizzata dell'Anagrafe Zootecnica sono insufficienti per valutare il primo parto (scenario di vacca con piu' parti)"));
			}
			VitelloDto primoParto = primoPartoOpt.get();
			if (primoParto.getDtInserimentoBdnNascita() == null || primoParto.getDataNascita()  == null || 
					primoParto.getFlagDelegatoNascitaVitello() == null || primoParto.getFlagProrogaMarcatura() == null) {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,
						"Il capo non è ammissibile all’aiuto perchè le informazioni presenti nella Banca Dati Nazionale (BDN) "
								+ "informatizzata dell'Anagrafe Zootecnica sulla nascita sono insufficienti: " +
								"flagDelegatoNascitaVitello[" +
								primoParto.getDtInserimentoBdnNascita() +
								"] dtNascita[" +
								primoParto.getDataNascita() +
								"] flagDelegatoNascitaVitello[" +
								primoParto.getFlagDelegatoNascitaVitello() +
								"] flagProrogaMarcatura[" +
								primoParto.getFlagProrogaMarcatura() +
						"]"));
			}
			List<DetenzioneDto> detenzioneList = capoLatte.getDetenzioni();
			for(DetenzioneDto detenzioneDto : detenzioneList) {
				if (detenzioneDto.getVaccaDtInserimentoBdnIngresso() == null || detenzioneDto.getVaccaDtIngresso() == null || 
						detenzioneDto.getDtFineDetenzione() == null) {
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,
							"Il capo non è ammissibile all’aiuto perchè le informazioni presenti nella Banca Dati Nazionale (BDN) "
									+ "informatizzata dell'Anagrafe Zootecnica sugli ingressi sono insufficienti: " +
									"vaccaDtInserimentoBdnIngresso[" + detenzioneDto.getVaccaDtInserimentoBdnIngresso() +
									"] vaccaDtIngresso[" + detenzioneDto.getVaccaDtIngresso() +
									"] dtFineDetenzione[" + detenzioneDto.getDtFineDetenzione() +
							"]"));
				}
			}
			return Optional.empty();
		};
	}

	//BR2 Individuazione del primo parto per il quale si richiede l’intervento 311
	protected BiFunction<CapoLatteDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>> checkPrimoParto() {
		return (capoLatte, richiestaAllevamDu) -> {
			String codiceAllevamento = "";
			try {
				InformazioniAllevamento informazioniAllevamento = objectMapper.readValue(richiestaAllevamDu.getDatiAllevamento(), InformazioniAllevamento.class);
				codiceAllevamento = informazioniAllevamento.getCodiceAllevamento();
			} catch (IOException ex) {
				logger.error("Errore conversione json InformazioniAllevamento", ex);
			}
			// non è necessario controllare il parto gemellare in quanto l'esito di questo controllo sarebbe identico
			Optional<VitelloDto> primoParto = getPrimoParto(capoLatte); 
			if (primoParto.isPresent()) {

				// trovare la detenzione del primo parto - se data di nascita coincide con inizio e fine detenzione, prendiamo la prima detenzione in ordine cronologico 
				Optional<DetenzioneDto> detenzionePrimoParto = ControlloCapiUtil.trovaDetenzioneDelPrimoParto(primoParto.get(), capoLatte.getDetenzioni());

				if (!detenzionePrimoParto.isPresent()) {
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.DA_SCARTARE, "I vitelli del primo parto non sono nati nell’allevamento per cui è richiesto l’intervento"));
				}

				// i vitelli del primo parto non sono nati nell'allevamento per cui è stato richiesto l'intervento
				if (!codiceAllevamento.equalsIgnoreCase(detenzionePrimoParto.get().getAziendaCodice())) {
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.DA_SCARTARE, "I vitelli del primo parto non sono nati nell’allevamento per cui è richiesto l’intervento"));
				} 
			}
			return Optional.empty(); //vai al prossimo controllo
		};
	}


	//BR6 - Verifica che l’allevamento è di montagna.
	protected BiFunction<CapoLatteDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>> checkAllevamentoMontagna() {
		return (capoLatte, richiestaAllevamDu) -> {
			try {
				InformazioniAllevamento informazioniAllevamento = objectMapper.readValue(richiestaAllevamDu.getDatiAllevamento(), InformazioniAllevamento.class);
				String codiceAllevamento=informazioniAllevamento.getCodiceAllevamento();
				if (!isAllevamentoDiMontagnaCertificato(codiceAllevamento).test(null)) {
					String esitoMessaggio = MessageFormat.format("Il capo non è ammissibile perché l’allevamento {0} per cui è stato "
							+ "richiesto il sostegno non è di montagna.",
							codiceAllevamento
							);
					logger.info(esitoMessaggio);
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE, esitoMessaggio));
				}
			} catch (IOException e) {
				logger.error("Errore conversione json InformazioniAllevamento",e);
			}
			return Optional.empty();
		};
	}

	//BR5 Verifica del rispetto dei sei mesi di detenzione in allevamenti in zona montana
	@Override
	protected BiFunction<CapoLatteDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>> checkMovimentazione() {
		return (capoLatte, richiestaAllevamDu) -> {
			List<DetenzioneDto> detenzioni=capoLatte.getDetenzioni()
					.stream()
					.sorted((DetenzioneDto det1,DetenzioneDto det2) -> det1.getDtInizioDetenzione().compareTo(det2.getDtInizioDetenzione()))
					.collect(Collectors.toList());
			List<DetenzioneDto> detenzioniSenzaBuchi = new ArrayList<>();
			VitelloDto primoparto = getPrimoParto(capoLatte).get();
			// se primo parto = data inizio = data fine, scelgo la detenzione che viene prima in ordine cronologico
			Optional<DetenzioneDto> detenzionePrimoParto = ControlloCapiUtil.trovaDetenzioneDelPrimoParto(primoparto, detenzioni);

			int indexOfPrimoParto = capoLatte.getDetenzioni().indexOf(detenzionePrimoParto.get());

			detenzioniSenzaBuchi.add(detenzioni.get(indexOfPrimoParto));
			//a partire da quella del primo parto andando a ritroso
			if (indexOfPrimoParto>0) {//se è il primo elemento è inutile ricercare a ritroso
				for (int i = indexOfPrimoParto-1; i >=0; i--) {
					DetenzioneDto currentClsCapoVacca=detenzioni.get(i);
					DetenzioneDto nextClsCapoVacca=detenzioni.get(i+1);
					if (isAllevamentoDiMontagnaCertificato(null).test(currentClsCapoVacca)
							&& (currentClsCapoVacca.getDtFineDetenzione().compareTo(nextClsCapoVacca.getDtInizioDetenzione()) == 0 )) { //rispettando la contiguità dei periodi
						detenzioniSenzaBuchi.add(detenzioni.get(i));
					} else {
						break;
					}
				}
			}
			//Nel caso non fossero sufficienti le detenzioni precedenti, vanno sommati i giorni di quelle 
			//successive in allevamenti di montagna sempre rispettando la contiguità dei periodi
			if (indexOfPrimoParto<detenzioni.size()-1) { //se è l'ultimo elemento è inutile ricercare a posteriori
				for (int i = indexOfPrimoParto+1; i < detenzioni.size(); i++) {
					DetenzioneDto currentClsCapoVacca=detenzioni.get(i);
					DetenzioneDto prevClsCapoVacca=detenzioni.get(i-1);
					if (isAllevamentoDiMontagnaCertificato(null).test(currentClsCapoVacca)
							&& (currentClsCapoVacca.getDtInizioDetenzione().compareTo(prevClsCapoVacca.getDtFineDetenzione()) == 0)) { //rispettando la contiguità dei periodi
						detenzioniSenzaBuchi.add(detenzioni.get(i));
					} else {
						break;
					}			
				}
			}

			int sommaGiorniMotagnaCertificati = detenzioniSenzaBuchi.stream()
					.sorted((DetenzioneDto det1,DetenzioneDto det2) -> det1.getDtInizioDetenzione().compareTo(det2.getDtInizioDetenzione()))
					.mapToInt(calcoloGiorni(richiestaAllevamDu.getCampagna()))
					.sum();

			if (sommaGiorniMotagnaCertificati < 180) {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,
						MessageFormat.format("Il capo non è ammissibile perché il periodo di detenzione in allevamenti di montagna calcolato "
								+ "({0} giorni)  sommando i giorni di detenzione nel periodo contenente il primo parto è inferiore ai giorni "
								+ "richiesti (180 giorni).",
								sommaGiorniMotagnaCertificati
								))	
						);
			} else {
				CapoLatteMontagnaCheckInterface checkInterface = controlliCapiLatteMontagnaFactory.fromData(detenzionePrimoParto.get().getVaccaDtIngresso());
				return checkInterface.checkSanzione(detenzioniSenzaBuchi, primoparto, richiestaAllevamDu.getCampagna());
			}			
		};
	}

	private Predicate<DetenzioneDto> isAllevamentoDiMontagnaCertificato(String codiceAllevamento) {
		return clsCapoVacca -> {
			String codiceAllev = codiceAllevamento==null ? clsCapoVacca.getAziendaCodice() : codiceAllevamento;
			// nelle provincie consentite ci sono esclusivamente allevamenti di montagna
			if (!Arrays.asList(provinceConsentite).contains(codiceAllev.substring(3, 5))) {
				A4gtAllevMontagna a4gtAlevMotagna=new A4gtAllevMontagna();
				a4gtAlevMotagna.setCodiceAllevamento(codiceAllev);
				Optional<A4gtAllevMontagna> allevamentoMotagna = allevMontagnaDao.findOne(Example.of(a4gtAlevMotagna));
				if (!(allevamentoMotagna.isPresent() && allevamentoMotagna.get().getFlagMotagna())) {
					return false;
				}
			}
			return true;
		};
	}

	private ToIntFunction<DetenzioneDto> calcoloGiorni(Integer campagna) {
		return detenzione -> {
			LocalDateTime primoGennaioAnnoCampagna = Clock.ofStartOfDay(LocalDate.of(campagna, 1, 1));
			long epochMillis =  primoGennaioAnnoCampagna.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;

			Optional<EsitoCalcoloCapoModel> checkMovimentazione = controlliCapiLatteMontagnaFactory.from(detenzione.getVaccaDtIngresso()).checkMovimentazione(detenzione, campagna);
			if (checkMovimentazione.isPresent()) {  // non rispetta i tempi di movimentazione
				long dtInserimentoBdnIngresso = LocalDateConverter.to(detenzione.getVaccaDtInserimentoBdnIngresso()).getTime();
				long dtFineDetenzione = LocalDateConverter.to(detenzione.getDtFineDetenzione()).getTime();
				long differenzaDate = TimeUnit.DAYS.convert(dtFineDetenzione-Math.max(dtInserimentoBdnIngresso, epochMillis), TimeUnit.MILLISECONDS);	
				return (int) differenzaDate;
			} else {	// rispetta i tempi di movimentazione
				long dtInizioDetenzione = LocalDateConverter.to(detenzione.getDtInizioDetenzione()).getTime();
				long dtFineDetenzione = LocalDateConverter.to(detenzione.getDtFineDetenzione()).getTime();
				long differenzaDate = TimeUnit.DAYS.convert(dtFineDetenzione-dtInizioDetenzione, TimeUnit.MILLISECONDS);
				return (int) differenzaDate;
			}
		};
	}
}

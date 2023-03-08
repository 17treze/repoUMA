package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import static it.tndigitale.a4gistruttoria.util.ControlloCapiUtil.getPrimoParto;
import static it.tndigitale.a4gistruttoria.util.ControlloCapiUtil.getPrimoPartoGemellare;
import static it.tndigitale.a4gistruttoria.util.ControlloCapiUtil.isPrimoPartoGemellare;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoLatteDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.ControlliCapiLatteFactory.CapoLatteCheckInterface;

@Component
public class ControlloCapoVaccaLatte implements BiFunction<RichiestaAllevamDu,CapoDto,EsitoCalcoloCapoModel> {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	private ControlliCapiLatteFactory controlliCapiLatteFactory;

	@Override
	public EsitoCalcoloCapoModel apply(RichiestaAllevamDu richiestaAllevamDu, CapoDto capoDto) {
		EsitoCalcoloCapoModel esitoCalcoloCapo=Stream.of(
				checkInterventoDaScartare(),
				checkPresenzaInformazioni(),
				checkLimiteEta(),
				checkIdentificazioneRegistrazione(),
				checkMovimentazione())
				.map(f -> f.apply((CapoLatteDto)capoDto, richiestaAllevamDu ))
				.filter( Optional::isPresent )
				.map( Optional::get )
				.findFirst()
				.orElseThrow( () -> new RuntimeException( "Errore generico calcolo capo Vacca Latte! " ) );
		esitoCalcoloCapo.setCapoId(Long.parseLong(capoDto.getIdCapo()));
		esitoCalcoloCapo.setCodiceCapo(capoDto.getMarcaAuricolare());		
		return esitoCalcoloCapo;
	}

	//IDU-ACZ-00 Ammissibilità interventi 314, 317 e 319
	protected BiFunction<CapoLatteDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>> checkInterventoDaScartare() {
		return (capoLatte, richiestaAllevamDu) -> {
			if ("314".equals(richiestaAllevamDu.getCodiceIntervento())) {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,"Capo appartenente a intervento non ammissibile (314)"));
			}
			return Optional.empty();
		};
	}

	//BR8 Check presenza informazioni
	protected BiFunction<CapoLatteDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>> checkPresenzaInformazioni() {
		return (capoLatte, richiestaAllevamDu) -> {
			Optional<EsitoCalcoloCapoModel> resonseKO = Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE, 
					"Il capo non è ammissibile all’aiuto perchè le informazioni presenti nella Banca Dati Nazionale (BDN) "
							+ "informatizzata dell'Anagrafe Zootecnica sono insufficienti o incongruenti o contraddittorie "
							+ "e non consentano di eseguire i controlli di ammissibilità"));
			if (capoLatte == null) {
				return resonseKO;
			}
			Optional<VitelloDto> primoPartoOpt = getPrimoParto(capoLatte);
			if (!primoPartoOpt.isPresent()) {
				return resonseKO;
			}
			VitelloDto primoParto = primoPartoOpt.get();
			if (primoParto.getDtInserimentoBdnNascita() == null || primoParto.getDataNascita()  == null || 
					primoParto.getFlagDelegatoNascitaVitello() == null || primoParto.getFlagProrogaMarcatura() == null) {
				return resonseKO;
			}
			List<DetenzioneDto> detenzioni = capoLatte.getDetenzioni();
			for(DetenzioneDto detenzioneDto : detenzioni) {
				if (detenzioneDto.getVaccaDtInserimentoBdnIngresso() == null || detenzioneDto.getVaccaDtIngresso() == null || 
						detenzioneDto.getDtFineDetenzione() == null) {
					return resonseKO;
				}
			}
			return Optional.empty();
		};
	}

	//BR3 Controllo sui limiti di età della vacca per essere fertile - i controlli vanno eseguiti sul primo parto
	protected BiFunction<CapoLatteDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>> checkLimiteEta() {
		return (capoLatte, richiestaAllevamDu) -> {
			// non è necessario controllare sui gemelli in quanto avrebbe lo stesso esito
			Optional<VitelloDto> primoPartoOpt = getPrimoParto(capoLatte);

			long dtNascita = LocalDateConverter.to(capoLatte.getDataNascita()).getTime();
			long dtNascitaVitello = LocalDateConverter.to(primoPartoOpt.get().getDataNascita()).getTime();
			long differenzaDate =  TimeUnit.DAYS.convert(dtNascitaVitello-dtNascita, TimeUnit.MILLISECONDS);
			// la vacca abbia tra i 20 mesi ed i 18 anni di età
			if (differenzaDate>20*30 && differenzaDate<18*365) {
				return Optional.empty();
			} else {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,
						MessageFormat.format("Il capo non è ammesso in quanto al momento del parto ({0}) la vacca nata il {1}, aveva {2} anni di età.",
								dateFormat.format(new Date(dtNascitaVitello)),
								dateFormat.format(new Date(dtNascita)),
								differenzaDate/365
								)));				
			}
		};
	}

	//BR4 Verifica del rispetto degli obblighi di identificazione e registrazione dei vitelli nati nel primo parto
	protected BiFunction<CapoLatteDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>> checkIdentificazioneRegistrazione() {
		return (capoLatte, richiestaAllevamDu) -> {
			List<VitelloDto> vitelli = new ArrayList<>();
			if (!isPrimoPartoGemellare(capoLatte).booleanValue()) {
				Optional<VitelloDto> primoParto = getPrimoParto(capoLatte);
				if (primoParto.isPresent()) {
					vitelli.add(primoParto.get());
				}
			} else {
				vitelli = getPrimoPartoGemellare(capoLatte);
			}

			for (VitelloDto vitelloToCheck : vitelli) {
				CapoLatteCheckInterface controlli = controlliCapiLatteFactory.from(vitelloToCheck.getDataNascita());
				Optional<EsitoCalcoloCapoModel> esito = controlli.checkIdentificazioneRegistrazione(vitelloToCheck);
				if (esito.isPresent()) { return esito; }
			}
			return Optional.empty();
		};
	}

	//BR5 Check Movimentazione
	protected BiFunction<CapoLatteDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>> checkMovimentazione() {
		return (capoLatte, richiestaAllevamDu) -> {
			for (DetenzioneDto detenzioneDto : capoLatte.getDetenzioni()) {
				CapoLatteCheckInterface controlli = controlliCapiLatteFactory.from(detenzioneDto.getVaccaDtIngresso());
				Optional<EsitoCalcoloCapoModel> esito = controlli.checkMovimentazione(detenzioneDto, richiestaAllevamDu.getCampagna());
				if (esito.isPresent()) { return esito; }
			}
			return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE,null));
		};
	}
}

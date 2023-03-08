package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.util.ControlloCapiUtil;

@Component
public class ControlliCapiLatteFactory {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	// la data è 20/04/2021 incluso - le funzioni isBefore e isAfter non sono riflessive. 
	private static final LocalDate DATA_RIFERIMENTO = LocalDate.of(2021, 4, 20);

	public CapoLatteCheckInterface from(LocalDate data) {
		return data.isAfter(DATA_RIFERIMENTO) ? new AfterDataRiferimento() : new BeforeDataRiferimento();
	}


	interface CapoLatteCheckInterface {
		//BR4
		public Optional<EsitoCalcoloCapoModel> checkIdentificazioneRegistrazione(VitelloDto vitelloDto);
		//BR5
		public Optional<EsitoCalcoloCapoModel> checkMovimentazione(DetenzioneDto detenzioneDto, Integer campagna);
	}

	class BeforeDataRiferimento implements CapoLatteCheckInterface {

		@Override
		public Optional<EsitoCalcoloCapoModel> checkIdentificazioneRegistrazione(VitelloDto vitelloDto) {
			long dtNascitaVitello = LocalDateConverter.to(vitelloDto.getDataNascita()).getTime();
			//Verifica del rispetto degli obblighi di identificazione e registrazione dei vitelli nati nel primo parto
			// BR4 Verifica del rispetto degli obblighi di identificazione e registrazione
			long vitelloDtInserimentoBdnNascita = LocalDateConverter.to(vitelloDto.getDtInserimentoBdnNascita()).getTime();
			//Flag_Delegato_Nascita_Vitello che può valere N se il capo è stato registrato dal detentore o S o R se è stato registrato da un delegato
			String flagDelegatoNascitaVitello=vitelloDto.getFlagDelegatoNascitaVitello();
			//Flag_Proroga_Marcatura che può valere S o N, ovvero se l’allevatore ha la proroga alla marcatura (S) oppure no (N).
			String flagProrogaMarcatura=vitelloDto.getFlagProrogaMarcatura();
			Integer[][] matriceFlag = new Integer[2][2];
			matriceFlag[1][1] = 27;
			matriceFlag[0][1] = 34;
			matriceFlag[1][0] = 180;
			matriceFlag[0][0] = 187;
			Integer ggLimite = matriceFlag["N".equals(flagDelegatoNascitaVitello)? 1 : 0]["N".equals(flagProrogaMarcatura)? 1 : 0];
			long differenzaDate = TimeUnit.DAYS.convert(vitelloDtInserimentoBdnNascita-dtNascitaVitello, TimeUnit.MILLISECONDS);		
			long offset = ControlloCapiUtil.getNumeroGiorniOffline(vitelloDto.getDataNascita(), vitelloDto.getDtInserimentoBdnNascita());
			if (differenzaDate > (ggLimite.intValue()+Math.toIntExact(offset))) {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,
						MessageFormat.format("Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e "
								+ "registrazione del vitello in quanto è stato superato il limite "
								+ "{0} tra la data di registrazione della nascita {1} e "
								+ "la data di nascita {2} che è pari a {3} ({4} "
								+ "delegata la registrazione nascita; allevamento {5} autorizzato a proroga marcatura)",
								ggLimite,
								dateFormat.format(new Date(vitelloDtInserimentoBdnNascita)),
								dateFormat.format(new Date(dtNascitaVitello)),
								differenzaDate,
								"N".equals(flagDelegatoNascitaVitello)? "non":"",
										"N".equals(flagProrogaMarcatura)? "non":""
								)));
			}
			return Optional.empty();
		}

		@Override
		public Optional<EsitoCalcoloCapoModel> checkMovimentazione(DetenzioneDto detenzioneDto, Integer campagna) {
			Integer limiteTemporale = 7;
			LocalDateTime primoGennaioAnnoCampagna = Clock.ofStartOfDay(LocalDate.of(campagna, 1, 1));
			long epochMillis =  primoGennaioAnnoCampagna.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;

			long vaccaDtInserimentoBdnIngresso = LocalDateConverter.to(detenzioneDto.getVaccaDtInserimentoBdnIngresso()).getTime();
			long vaccaDtIngresso = LocalDateConverter.to(detenzioneDto.getVaccaDtIngresso()).getTime();
			long vaccaDtComAutoritaIngresso = LocalDateConverter.to(detenzioneDto.getVaccaDtComAutoritaIngresso()).getTime();
			long differenzaDtIngresso =  TimeUnit.DAYS.convert(vaccaDtComAutoritaIngresso-vaccaDtIngresso, TimeUnit.MILLISECONDS);
			long differenzaDtIngressoBdn =  TimeUnit.DAYS.convert(vaccaDtInserimentoBdnIngresso-vaccaDtComAutoritaIngresso, TimeUnit.MILLISECONDS);
			long offset = ControlloCapiUtil.getNumeroGiorniOffline(detenzioneDto.getVaccaDtComAutoritaIngresso(), detenzioneDto.getVaccaDtInserimentoBdnIngresso());

			if (vaccaDtInserimentoBdnIngresso < epochMillis || (differenzaDtIngresso <= limiteTemporale && differenzaDtIngressoBdn <= (limiteTemporale + offset))) {
				//ok
			}else{
				// nel caso degli allevamenti di montagna (311), è sufficiente che il ritorno sia non vuoto per continuare il calcolo.
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE,
						MessageFormat.format("il capo è ammesso con sanzioni perché non sono stati rispettati i tempi di registrazione della movimentazione "
								+ "in quanto non sono stati rispettati tutti i tempi di registrazione (entrambi devono essere inferiori agli 8 giorni): "
								+ "Vacca_Dt_Com_Autorita_Ingresso={0} – Vacca_Dt_Ingresso={1} è pari a {2}; Vacca_Dt_Inserimento_Bdn_Ingresso={3} – Vacca_Dt_Com_Autorita_Ingresso={4} è pari a {5}",
								dateFormat.format(new Date(vaccaDtComAutoritaIngresso)),
								dateFormat.format(new Date(vaccaDtIngresso)),
								differenzaDtIngresso,
								dateFormat.format(new Date(vaccaDtInserimentoBdnIngresso)),
								dateFormat.format(new Date(vaccaDtComAutoritaIngresso)),
								differenzaDtIngressoBdn
								)));
			}
			return Optional.empty();
		}
	}


	class AfterDataRiferimento implements CapoLatteCheckInterface {

		@Override
		public Optional<EsitoCalcoloCapoModel> checkIdentificazioneRegistrazione(VitelloDto vitelloDto) {

			long limiteTemporale = "S".equals(vitelloDto.getFlagProrogaMarcatura()) ? 180 : 20;

			long dtNascita = LocalDateConverter.to(vitelloDto.getDataNascita()).getTime();
			long dtApplicazioneMarchio = LocalDateConverter.to(vitelloDto.getDtApplicazioneMarchio()).getTime();
			long dtInserimentoBdn = LocalDateConverter.to(vitelloDto.getDtInserimentoBdnNascita()).getTime();

			long tempisticheMarchio = TimeUnit.DAYS.convert(dtApplicazioneMarchio - dtNascita, TimeUnit.MILLISECONDS);
			long tempisticheInserimentoBdn = TimeUnit.DAYS.convert(dtInserimentoBdn - dtApplicazioneMarchio, TimeUnit.MILLISECONDS);
			long offset = ControlloCapiUtil.getNumeroGiorniOffline(vitelloDto.getDtApplicazioneMarchio(), vitelloDto.getDtInserimentoBdnNascita());

			boolean rispettaObblighi = tempisticheMarchio <= limiteTemporale && tempisticheInserimentoBdn <= (7 + offset);

			if (!rispettaObblighi) {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,
						MessageFormat.format("Il capo non è ammissibile perché non sono stati rispettati i tempi di identificazione e "
								+ "registrazione del vitello: "
								+ "(Vitello_Dt_Appl_Marchio - Dt_Nascita_Vitello) <= {0} ({1})"
								+ "{2}",
								tempisticheMarchio,
								limiteTemporale,
								"S".equals(vitelloDto.getFlagProrogaMarcatura()) ? "." : " E (Vitello_Dt_Inserimento_Bdn_Nascita - Vitello_Dt_Appl_Marchio) <= ".concat(String.valueOf(tempisticheInserimentoBdn)).concat(" (7).")
								)));
			}
			return Optional.empty();
		}

		@Override
		public Optional<EsitoCalcoloCapoModel> checkMovimentazione(DetenzioneDto detenzioneDto, Integer campagna) {
			Integer limiteTemporale = 7;
			if (detenzioneDto.getVaccaDtInserimentoBdnIngresso().getYear() != detenzioneDto.getVaccaDtIngresso().getYear()) {
				long vaccaDtInserimentoBdnIngresso = LocalDateConverter.to(detenzioneDto.getVaccaDtInserimentoBdnIngresso()).getTime(); 
				long vaccaDtIngresso = LocalDateConverter.to(detenzioneDto.getVaccaDtIngresso()).getTime();
				long differenzaDtIngresso =  TimeUnit.DAYS.convert(vaccaDtInserimentoBdnIngresso-vaccaDtIngresso, TimeUnit.MILLISECONDS);
				long offset = ControlloCapiUtil.getNumeroGiorniOffline(detenzioneDto.getVaccaDtIngresso(), detenzioneDto.getVaccaDtInserimentoBdnIngresso());

				if (differenzaDtIngresso > (limiteTemporale+offset)) {
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE,
							MessageFormat.format("il capo è ammesso con sanzioni perché non sono stati rispettati i tempi di registrazione della movimentazione: "
									+ "(Vacca_Dt_Inserimento_Bdn_Ingresso < {0} (01/01/{1})) O "
									+ "(Vacca_Dt_Inserimento_Bdn_Ingresso – Vacca_Dt_Ingresso) <= {2} (7).",
									dateFormat.format(new Date(vaccaDtInserimentoBdnIngresso)),
									campagna.toString(),
									differenzaDtIngresso
									)));
				}
			}
			return Optional.empty();
		}
	}
}

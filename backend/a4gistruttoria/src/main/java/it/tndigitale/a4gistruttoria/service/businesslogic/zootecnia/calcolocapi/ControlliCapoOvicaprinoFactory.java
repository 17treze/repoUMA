package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoOvicaprinoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.util.ControlloCapiUtil;

@Component
public class ControlliCapoOvicaprinoFactory {

	private static final LocalDate DATA_RIFERIMENTO = LocalDate.of(2021, 4, 20);

	public CapoOvicaprinoCheckInterface from(LocalDate data) {
		return data.isAfter(DATA_RIFERIMENTO) ? new AfterDataRiferimento() : new BeforeDataRiferimento();
	}

	interface CapoOvicaprinoCheckInterface {

		// BR2: Ammissibilità dei capi 320
		public Optional<EsitoCalcoloCapoModel> checkIntervento320(CapoOvicaprinoDto capo, RichiestaAllevamDu allevamento);
	}

	class BeforeDataRiferimento implements CapoOvicaprinoCheckInterface {

		@Override
		public Optional<EsitoCalcoloCapoModel> checkIntervento320(CapoOvicaprinoDto capo, RichiestaAllevamDu allevamento) {
			var limiteTemporale = 180;
			long dtNascita = LocalDateConverter.to(capo.getDataNascita()).getTime();
			long dtApplicazioneMarchio = LocalDateConverter.to(capo.getDataApplicazioneMarchio()).getTime();
			long tempisticaApplicazioneMarchio =  TimeUnit.DAYS.convert(dtApplicazioneMarchio-dtNascita, TimeUnit.MILLISECONDS);

			return tempisticaApplicazioneMarchio <= limiteTemporale ? 
					Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE,null)) :
						Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, MessageFormat.format("Il capo è ammesso con sanzione perché: (Dt_Appl_Marchio - Dt_Nascita) é superiore a {0} giorni", limiteTemporale)));
		}
	}

	class AfterDataRiferimento implements CapoOvicaprinoCheckInterface {

		@Override
		public Optional<EsitoCalcoloCapoModel> checkIntervento320(CapoOvicaprinoDto capo, RichiestaAllevamDu allevamento) {

			long dataApplicazioneMarchio = LocalDateConverter.to(capo.getDataApplicazioneMarchio()).getTime();
			long dataNascita = LocalDateConverter.to(capo.getDataNascita()).getTime();
			long tempisticaApplicazioneMarchio = TimeUnit.DAYS.convert(dataApplicazioneMarchio - dataNascita, TimeUnit.MILLISECONDS); 

			long dataInserimentoBdnNascita = LocalDateConverter.to(capo.getDataInserimentoBdnNascita()).getTime();
			long offsetNascMarc = ControlloCapiUtil.getNumeroGiorniOffline(capo.getDataApplicazioneMarchio(), capo.getDataInserimentoBdnNascita());
			long tempisticaRegistrazioneNascita = TimeUnit.DAYS.convert(dataInserimentoBdnNascita - dataApplicazioneMarchio, TimeUnit.MILLISECONDS);

			// estrae (una) la prima detenzione che non rispetta la tempistica. E' sufficiente che ce ne sia una affinchè sia Ammissibile con sanzione. 
			Optional<DetenzioneDto> detenzioneFuoriTempisticaOpt = capo.getDetenzioni().stream().filter(detenzione -> {
				// se nato in stalla
				boolean isOk;
				if (capo.getDataNascita().compareTo(detenzione.getVaccaDtIngresso()) == 0) {
					var rispettaTempistiche = tempisticaApplicazioneMarchio <= 180 && tempisticaRegistrazioneNascita <= (7 + offsetNascMarc);
					isOk = rispettaTempistiche || capo.getDataApplicazioneMarchio().getYear() == capo.getDataInserimentoBdnNascita().getYear();
				} else {
					long dataInserimentoBdnIngresso = LocalDateConverter.to(detenzione.getVaccaDtInserimentoBdnIngresso()).getTime();
					long dataIngresso = LocalDateConverter.to(detenzione.getVaccaDtIngresso()).getTime();
					long tempisticaIngresso = TimeUnit.DAYS.convert(dataInserimentoBdnIngresso - dataIngresso, TimeUnit.MILLISECONDS); 
					long offset = ControlloCapiUtil.getNumeroGiorniOffline(detenzione.getVaccaDtIngresso(), detenzione.getVaccaDtInserimentoBdnIngresso());
					isOk = tempisticaIngresso <= (7 + offset) || detenzione.getVaccaDtInserimentoBdnIngresso().getYear() == detenzione.getVaccaDtIngresso().getYear();
				}
				return !isOk;
			}).findFirst();

			if (detenzioneFuoriTempisticaOpt.isPresent()) {
				long dataInserimentoBdnIngresso = LocalDateConverter.to(detenzioneFuoriTempisticaOpt.get().getVaccaDtInserimentoBdnIngresso()).getTime();
				long dataIngresso = LocalDateConverter.to(detenzioneFuoriTempisticaOpt.get().getVaccaDtIngresso()).getTime();
				long tempisticaIngresso = TimeUnit.DAYS.convert(dataInserimentoBdnIngresso - dataIngresso, TimeUnit.MILLISECONDS); 

				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, 
						MessageFormat.format("Il capo è ammesso con sanzione perché: "
								+ "(Dt_Appl_Marchio - Dt_Nascita) <= {0} (180) E "
								+ "(Dt_inserimento_BDN_nascita - Dt_Appl_Marchio) <= {1} (7) E "
								+ "((Dt_Nascita != Dt_Ingresso) O (Dt_inserimento_bdn_ingresso – Dt_Ingresso) <= {2} (7)."  ,
								tempisticaApplicazioneMarchio, 
								tempisticaRegistrazioneNascita, 
								tempisticaIngresso)));
			}
			return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE,null));
		}
	}
}

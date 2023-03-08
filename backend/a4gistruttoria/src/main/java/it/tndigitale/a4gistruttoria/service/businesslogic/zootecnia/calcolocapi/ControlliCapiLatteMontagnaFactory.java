package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.util.ControlloCapiUtil;

@Component
public class ControlliCapiLatteMontagnaFactory extends ControlliCapiLatteFactory {

	private static final Logger logger = LoggerFactory.getLogger(ControlliCapiLatteMontagnaFactory.class);

	// la data è 20/04/2021 incluso - le funzioni isBefore e isAfter non sono riflessive. 
	private static final LocalDate DATA_RIFERIMENTO = LocalDate.of(2021, 4, 20);

	public CapoLatteMontagnaCheckInterface fromData(LocalDate data) {
		return data.isAfter(DATA_RIFERIMENTO) ? new AfterDataRiferimento() : new BeforeDataRiferimento();
	}

	interface CapoLatteMontagnaCheckInterface {
		// BR5: 2021 - IDU-ACZ-03.02 Controlli veterinari su vacche da latte allevate in montagna (311) - Revisione regola rispetto tempistiche post 20/04/2021
		public Optional<EsitoCalcoloCapoModel> checkMovimentazione(DetenzioneDto detenzioneDto, Integer campagna);

		// nel caso in cui la detenzione del primo parto non rispetti la tempistica di registrazione della movimentazione
		// si da per assunto che venga passata solo la detenzione del primo parto nell'array.
		public Optional<EsitoCalcoloCapoModel> checkSanzione(List<DetenzioneDto> detenzioni, VitelloDto primoParto, Integer campagna);

	}

	class BeforeDataRiferimento implements CapoLatteMontagnaCheckInterface {

		// prima della data di riferimento il controllo per il 311 è identico a quello per le vacche latte
		@Override
		public Optional<EsitoCalcoloCapoModel> checkMovimentazione(DetenzioneDto detenzioneDto, Integer campagna) {
			return new ControlliCapiLatteFactory.BeforeDataRiferimento().checkMovimentazione(detenzioneDto, campagna);
		}

		@Override
		public Optional<EsitoCalcoloCapoModel> checkSanzione(List<DetenzioneDto> detenzioni, VitelloDto primoParto, Integer campagna) {
			boolean nonRispettaTempistiche = detenzioni.stream().anyMatch(detenzioneDto -> {
				Optional<EsitoCalcoloCapoModel> checkMovimentazione = fromData(detenzioneDto.getVaccaDtIngresso()).checkMovimentazione(detenzioneDto, campagna);
				return checkMovimentazione.isPresent();
			});
			if (nonRispettaTempistiche) {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE,
						"Il capo è ammesso con sanzioni perché non sono stati rispettati i tempi di registrazione della movimentazione in "
								+ "almeno una delle detenzioni che hanno concorso a raggiungere i 6 mesi."));
			}
			return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE, ""));
		}

	}

	class AfterDataRiferimento implements CapoLatteMontagnaCheckInterface {
		// override movimentazione per intervento 311
		@Override
		public Optional<EsitoCalcoloCapoModel> checkMovimentazione(DetenzioneDto detenzioneDto, Integer campagna) {
			Integer limiteTemporale = 7;

			long vaccaDtInserimentoBdnIngresso = LocalDateConverter.to(detenzioneDto.getVaccaDtInserimentoBdnIngresso()).getTime();
			long vaccaDtIngresso = LocalDateConverter.to(detenzioneDto.getVaccaDtIngresso()).getTime();
			long differenzaDtIngresso =  TimeUnit.DAYS.convert(vaccaDtInserimentoBdnIngresso-vaccaDtIngresso, TimeUnit.MILLISECONDS);
			long offset = ControlloCapiUtil.getNumeroGiorniOffline(detenzioneDto.getVaccaDtIngresso(), detenzioneDto.getVaccaDtInserimentoBdnIngresso());
			// ok
			if (differenzaDtIngresso <= (limiteTemporale + offset)) {
				return Optional.empty();
			} else {
				// il messaggio di errore non è significativo nel caso dell'intervento 311 (valuta solo è presente una sanzione o no)
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, ""));
			} 
		}

		@Override
		public Optional<EsitoCalcoloCapoModel> checkSanzione(List<DetenzioneDto> detenzioni, VitelloDto primoParto, Integer campagna) {
			Optional<DetenzioneDto> detenzioneOpt = ControlloCapiUtil.trovaDetenzioneDelPrimoParto(primoParto, detenzioni);
			if (!detenzioneOpt.isPresent()) {
				String errMsg = "[checkSanzione] - AfterDataRiferimento - Nessuna detenzione primo parto trovata per il vitello " + primoParto.getMarcaAuricolare();
				logger.info(errMsg);
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE, "Il capo non è ammissibile perché la detenzione del primo parto non rientra tra le detenzioni contigue."));
			}

			return new BeforeDataRiferimento().checkSanzione(Arrays.asList(detenzioneOpt.get()), primoParto, campagna);
		}
	}
}

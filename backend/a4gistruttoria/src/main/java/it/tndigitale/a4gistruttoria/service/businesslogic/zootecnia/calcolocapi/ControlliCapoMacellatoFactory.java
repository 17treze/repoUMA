package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoMacellatoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.util.ControlloCapiUtil;

@Component
public class ControlliCapoMacellatoFactory {

	private static final LocalDate DATA_RIFERIMENTO = LocalDate.of(2021, 4, 20);

	public CapoMacellatoCheckInterface from(LocalDate data) {
		return data.isAfter(DATA_RIFERIMENTO) ? new AfterDataRiferimento() : new BeforeDataRiferimento();
	}

	interface CapoMacellatoCheckInterface {

		public Optional<EsitoCalcoloCapoModel> checkDetenzioni(CapoMacellatoDto capoMacellato, String codiceIntervento);
	}

	class BeforeDataRiferimento implements CapoMacellatoCheckInterface {

		public boolean checkSanzioni(DetenzioneDto detenzione, CapoMacellatoDto capoMacellato) {
			long dtIngresso = LocalDateConverter.to(detenzione.getVaccaDtIngresso()).getTime();
			long dtComAutoritaIngresso = LocalDateConverter.to(detenzione.getVaccaDtComAutoritaIngresso()).getTime();
			long dtInserimentoBdnIngresso = LocalDateConverter.to(detenzione.getVaccaDtInserimentoBdnIngresso()).getTime();
			// Calcolo LIMITE: se Dt_Nascita = Dt_ingresso allora LIMITE = 27; altrimenti LIMITE = 7
			Integer ggLimite = 7;
			long dtNascita = LocalDateConverter.to(capoMacellato.getDataNascita()).getTime();
			if (dtNascita == dtIngresso) {
				ggLimite = 27;
			}	
			//Se (Dt_Com_Autorita_Ingresso – Dt_Ingresso) =< LIMITE giorni E (Dt_Inserimento_Bdn_Ingresso – Dt_Com_Autorita_Ingresso) =< 7 giorni allora la tempistica è rispettata; altrimenti no.
			long differenzaDate = TimeUnit.DAYS.convert(dtComAutoritaIngresso - dtIngresso, TimeUnit.MILLISECONDS);
			long differenzaDateIngresso = TimeUnit.DAYS.convert(dtInserimentoBdnIngresso - dtComAutoritaIngresso, TimeUnit.MILLISECONDS);
			long offset = ControlloCapiUtil.getNumeroGiorniOffline(detenzione.getVaccaDtComAutoritaIngresso(), detenzione.getVaccaDtInserimentoBdnIngresso());

			if (differenzaDate <= ggLimite && differenzaDateIngresso <= (7 + offset)) {
				return false; //non sanzionabile
			}
			return true; //ha sanzione
		}

		@Override
		public Optional<EsitoCalcoloCapoModel> checkDetenzioni(CapoMacellatoDto capoMacellato, String codiceIntervento) {
			
			// considera le detenzioni senza buchi e ne calcola i giorni di detenzione amministrativi e reali, e per ciascuno, anche se ha sanzioni
			List<DetenzioneDto> detenzioniSenzaBuchi = ControlloCapiUtil.ordinaEfiltraCapi(capoMacellato.getDetenzioni());
			List<DetenzioniHandler> detenzioniHandler = detenzioniSenzaBuchi.stream()
					.map(detenzione -> calcolaGiorniDetenzione(detenzione, capoMacellato))
					.collect(Collectors.toList());
			
			DetenzioniHandler handler = detenzioniHandler.stream().collect(Collectors.reducing(new DetenzioniHandler(), DetenzioniHandler::somma));
			int ggDetenzioniMinimi = "316".equals(codiceIntervento) ? 360 : 180;

			// rispetta i giorniAmministrativi >= limite
			if (handler.getGgAmministrativi() >= ggDetenzioniMinimi) {
				if (detenzioniHandler.stream().anyMatch(DetenzioniHandler::getHaSanzioni)) { // almeno una detenzione ha sanzioni
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, "La vacca è ammissibile al sostegno con sanzione perché c’è almeno una detenzione tra quelle verificate che non rispetta la tempistica di registrazione della movimentazione."));
				} else { // nessuna detenzione ha sanzioni
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE, null));
				}
			} else { // giorniAmministrativi sotto la soglia limite
				if (handler.getGgReali() >= ggDetenzioniMinimi) { // giorniReali sopra o uguale la soglia limite
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, MessageFormat.format("Il capo è ammesso con sanzione nonostante i giorni reali di detenzione siano maggiori o uguali a {0} giorni, ma non sono stati raggiunti i {0} giorni di detenzione amministrativi nell’allevamento.", ggDetenzioniMinimi)));
				} else { // giorniReali sotto la soglia limite
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE, MessageFormat.format("Il capo non è ammissibile perché non soddisfa né i giorni di detenzione amministrativi né quelli reali: entrambi sono inferiori a {0} giorni.", ggDetenzioniMinimi)));
				}
			}
		}
		
		private DetenzioniHandler calcolaGiorniDetenzione(DetenzioneDto detenzione, CapoMacellatoDto capo) {
			long dtUscita = LocalDateConverter.to(detenzione.getDtUscita()).getTime();
			long dtInserimentoBdnIngresso = LocalDateConverter.to(detenzione.getVaccaDtInserimentoBdnIngresso()).getTime();
			long dtIngresso = LocalDateConverter.to(detenzione.getVaccaDtIngresso()).getTime();
			boolean haSanzioni = checkSanzioni(detenzione, capo);

			return new DetenzioniHandler()
					.setGgAmministrativi(TimeUnit.DAYS.convert(dtUscita-dtInserimentoBdnIngresso, TimeUnit.MILLISECONDS))
					.setGgReali(TimeUnit.DAYS.convert(dtUscita-dtIngresso, TimeUnit.MILLISECONDS))
					.setHaSanzioni(haSanzioni);
			}
		
		private class DetenzioniHandler {

			private long ggAmministrativi;
			private long ggReali;
			private Boolean haSanzioni;

			public long getGgAmministrativi() {
				return ggAmministrativi;
			}
			public DetenzioniHandler setGgAmministrativi(long ggAmministrativi) {
				this.ggAmministrativi = ggAmministrativi;
				return this;
			}
			public long getGgReali() {
				return ggReali;
			}
			public DetenzioniHandler setGgReali(long ggReali) {
				this.ggReali = ggReali;
				return this;
			}
			public Boolean getHaSanzioni() {
				return haSanzioni;
			}
			public DetenzioniHandler setHaSanzioni(Boolean haSanzioni) {
				this.haSanzioni = haSanzioni;
				return this;
			}
			public DetenzioniHandler somma(DetenzioniHandler handler) {
				return new DetenzioniHandler()
						.setGgAmministrativi(ggAmministrativi + handler.getGgAmministrativi())
						.setGgReali(ggReali + handler.getGgReali());
			}
		}
	}

	class AfterDataRiferimento implements CapoMacellatoCheckInterface {

		@Override
		public Optional<EsitoCalcoloCapoModel> checkDetenzioni(CapoMacellatoDto capoMacellato,	String codiceIntervento) {
			// considera le detenzioni ordinate in maniera crescente
			List<DetenzioneDto> detenzioniOrdinate = capoMacellato.getDetenzioni().stream()
					.sorted(Comparator.comparing(DetenzioneDto::getDtInizioDetenzione))
					.collect(Collectors.toList());
			
			long dtNascita = LocalDateConverter.to(capoMacellato.getDataNascita()).getTime();
			long dtApplMarchio = LocalDateConverter.to(capoMacellato.getDataApplicazioneMarchio()).getTime();

			DetenzioneDto primaDetenzione = detenzioniOrdinate.stream().findFirst().orElse(null); 					
			long dtIngressoPrimaDetenzione = LocalDateConverter.to(primaDetenzione.getVaccaDtIngresso()).getTime();
			long dtInserimentoBdnIngressoPrimaDetenzione = LocalDateConverter.to(primaDetenzione.getVaccaDtInserimentoBdnIngresso()).getTime();

			boolean isTempisticaRispettata = false;
			boolean isDetenzioneMinimaRispettata = false;
			
			// Requisito 1 - nato in stalla: Se (Dt_Ingresso = Dt_Nascita) allora Dt_Appl_Marchio - Dt_Nascita <= 20 giorni E Dt_Inserimento_Bdn_Ingresso - Dt_Appl_Marchio) <= 7
			if (dtNascita == dtIngressoPrimaDetenzione) {  
				long offset = ControlloCapiUtil.getNumeroGiorniOffline(capoMacellato.getDataApplicazioneMarchio(), primaDetenzione.getVaccaDtInserimentoBdnIngresso());
				if (TimeUnit.DAYS.convert(dtApplMarchio-dtNascita, TimeUnit.MILLISECONDS) <=20 && TimeUnit.DAYS.convert(dtInserimentoBdnIngressoPrimaDetenzione-dtApplMarchio, TimeUnit.MILLISECONDS) <= (7+offset) ) {
					isTempisticaRispettata = true;
				}
			}
			// Requisito 1 - non nato in stalla: Se (Dt_Ingresso != Dt_Nascita) allora Dt_Inserimento_Bdn_Ingresso - Dt_Ingresso) <= 7
			if (dtNascita != dtIngressoPrimaDetenzione) {
				long offset = ControlloCapiUtil.getNumeroGiorniOffline(primaDetenzione.getVaccaDtIngresso(), primaDetenzione.getVaccaDtInserimentoBdnIngresso());
				if (TimeUnit.DAYS.convert(dtInserimentoBdnIngressoPrimaDetenzione-dtIngressoPrimaDetenzione, TimeUnit.MILLISECONDS) <= (7+offset) ) {
					isTempisticaRispettata = true;
				}
			}
			
			// Requisito 2: Dt_uscita dell’ultima (temporalmente) detenzione - Dt_Inserimento_Bdn_Ingresso della prima (temporalmente) detenzione >= 180 (360 per l'interevento 316)
			long count = detenzioniOrdinate.stream().count();
			DetenzioneDto ultimaDetenzione = detenzioniOrdinate.stream().skip(count - 1).findFirst().orElse(null);
			long dtUscitaUltimaDetenzione = LocalDateConverter.to(ultimaDetenzione.getDtUscita()).getTime();
			long minGiorniDetenzione = Long.parseLong(codiceIntervento) == 316 ? 360 : 180;
			long offset = ControlloCapiUtil.getNumeroGiorniOffline(primaDetenzione.getVaccaDtInserimentoBdnIngresso(), ultimaDetenzione.getDtUscita());
			
			if (TimeUnit.DAYS.convert(dtUscitaUltimaDetenzione-dtInserimentoBdnIngressoPrimaDetenzione, TimeUnit.MILLISECONDS) >= (minGiorniDetenzione-offset) ) {
				isDetenzioneMinimaRispettata = true;
			}
			
			if (isTempisticaRispettata && isDetenzioneMinimaRispettata) {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE, null));
			} else if (isTempisticaRispettata && !isDetenzioneMinimaRispettata) {
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE, MessageFormat.format("Il capo non è ammissibile perché non soddisfa i giorni minimi di detenzione che sono inferiori a {0}", minGiorniDetenzione))); // non è ammesso
			} else { // if (!isTempisticaRispettata)
				return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.AMMISSIBILE_CON_SANZIONE, "Il capo è ammesso con sanzione perché non soddisfa i giorni di tempistica minima rispettata")); 
			}
		}

	}

}

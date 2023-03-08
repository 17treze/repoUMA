package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import it.tndigitale.a4gistruttoria.action.InitVariabiliAgricoltoreAttivoConsumer;
import it.tndigitale.a4gistruttoria.action.acz.InitVariabiliCampioneZootecniaConsumer;
import it.tndigitale.a4gistruttoria.action.acz.InitVariabiliControlliLocoConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public class EsitoAmmissibilitaAccoppiatoZootecniaBuilder {
	
	private EsitoAmmissibilitaAccoppiatoZootecniaBuilder() {
	}

	public static EsitoAmmissibilitaAccoppiatoZootecnia buildEsitoAmmissibilitaAccoppiatoZootecnia(CalcoloAccoppiatoHandler handler) throws Exception {
		return EsitoAmmissibilitaAccoppiatoZootecnia.empty()
				.withInfoAgricoltoreAttivo(bridu_InfoAgratt(handler.getVariabiliInput()))
				.withAgricoltoreAttivo(bridu_Agratt(handler.getVariabiliInput()))
				.withCampione(briduacz121_campioneZootecnia(handler.getVariabiliInput()))
				.withEsitoControlliInLoco(briduacz123_esitoControlliInLoco(handler.getVariabiliInput()))
				.withUbaMinime(briduacz107_ubaMinime(handler.getVariabiliInput()))
				.withInterventiSanzionati(briduacz126_interventiSanzionati(handler.getVariabiliInput()))
				.withRiduzioni(briduacz127_riduzioni(handler.getVariabiliOutput()));
	}
	
	protected static Optional<Boolean> bridu_InfoAgratt(MapVariabili inputListaVariabiliCalcolo) {
		return getCheck(inputListaVariabiliCalcolo, InitVariabiliAgricoltoreAttivoConsumer.INFOAGRATT);
	}
	
	protected static Optional<Boolean> bridu_Agratt(MapVariabili inputListaVariabiliCalcolo) {
		if (!bridu_InfoAgratt(inputListaVariabiliCalcolo).orElse(false))
			return Optional.empty();
		return getCheck(inputListaVariabiliCalcolo, InitVariabiliAgricoltoreAttivoConsumer.AGRATT);
	}

	protected static Optional<Boolean> briduacz121_campioneZootecnia(MapVariabili inputListaVariabiliCalcolo) {
		Optional<Boolean> campioneBovini = getCheck(inputListaVariabiliCalcolo, InitVariabiliCampioneZootecniaConsumer.AZCMPBOV);
		Optional<Boolean> campioneOvicaprini = getCheck(inputListaVariabiliCalcolo, InitVariabiliCampioneZootecniaConsumer.AZCMPOVI);
		if (campioneBovini.isPresent() || campioneOvicaprini.isPresent()) {
			return Optional.ofNullable((campioneBovini.isPresent() && campioneBovini.get()) 
					|| (campioneOvicaprini.isPresent() && campioneOvicaprini.get()));
		} else {
			return Optional.empty();
		}
	}
	
	protected static Optional<Boolean> briduacz123_esitoControlliInLoco(MapVariabili inputListaVariabiliCalcolo) {
		// Se l'azienda non è campione non viene popolato l'esito dei controlli in loco
		if (!briduacz121_campioneZootecnia(inputListaVariabiliCalcolo).orElse(false))
			return Optional.empty();
		
		VariabileCalcolo vc = inputListaVariabiliCalcolo.get(InitVariabiliControlliLocoConsumer.ACZCONTROLLILOCO);
		return Optional.ofNullable(vc != null && vc.getValBoolean());
	}
	
	protected static Optional<Boolean> getCheck(MapVariabili inputListaVariabiliCalcolo, TipoVariabile tipo) {
		VariabileCalcolo vc = inputListaVariabiliCalcolo.get(tipo);
		return Optional.ofNullable(vc != null && vc.getValBoolean());
	}
	
	protected static Optional<Boolean> briduacz107_ubaMinime(MapVariabili inputListaVariabiliCalcolo) {
		// Se l'azienda è campione e l'esito controlli in loco non è positivo non viene popolato l'esito delle uba minime
		if (briduacz121_campioneZootecnia(inputListaVariabiliCalcolo).orElse(true)
				&& !briduacz123_esitoControlliInLoco(inputListaVariabiliCalcolo).orElse(false))
			return Optional.empty();
		
		VariabileCalcolo vc = inputListaVariabiliCalcolo.get(TipoVariabile.ACZUBATOT);
		if (vc == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(vc.getValNumber().compareTo(BigDecimal.valueOf(3)) >= 0);
	}

	protected static Optional<InterventiSanzionati> briduacz126_interventiSanzionati(MapVariabili inputListaVariabiliCalcolo) {
		// Se ubaMinime non presenti o false non viene popolato l'esito dei controlli sanzionati
		if (!briduacz107_ubaMinime(inputListaVariabiliCalcolo).orElse(false))
			return Optional.empty();
		
		List<TipoVariabile> percentualiSanzioniApplicate = Arrays.asList(
						TipoVariabile.PERCSANZ_310, TipoVariabile.PERCSANZ_311, TipoVariabile.PERCSANZ_313, 
						TipoVariabile.PERCSANZ_315, TipoVariabile.PERCSANZ_316, TipoVariabile.PERCSANZ_318, 
						TipoVariabile.PERCSANZ_320, TipoVariabile.PERCSANZ_321, TipoVariabile.PERCSANZ_322);
		
		List<VariabileCalcolo> variabiliSanzioniApplicate = new ArrayList<>();
		percentualiSanzioniApplicate.forEach(p -> {
			VariabileCalcolo vc = inputListaVariabiliCalcolo.get(p);
			if (vc != null) variabiliSanzioniApplicate.add(vc);
		});
		if (variabiliSanzioniApplicate.stream()
				.allMatch(p -> p.getValNumber() != null && (p.getValNumber().compareTo(BigDecimal.ZERO) == 0))) {
			return Optional.of(InterventiSanzionati.NESSUNA_SANZIONE);
		} else if (variabiliSanzioniApplicate.stream()
				.allMatch(p -> p.getValNumber() != null && (p.getValNumber().compareTo(BigDecimal.valueOf(1)) >= 0))) {
			return Optional.of(InterventiSanzionati.TUTTI_SANZIONATI);
		} else {
			return Optional.of(InterventiSanzionati.CON_SANZIONI);
		}
	}
	
	protected static Optional<Boolean> briduacz127_riduzioni(MapVariabili outputListaVariabiliCalcolo) {
		Boolean result = false;
		VariabileCalcolo vcRiduzioniSanzioni = outputListaVariabiliCalcolo.get(TipoVariabile.ACZIMPRIDSANZTOT);
		VariabileCalcolo vcRiduzioniRitardo = outputListaVariabiliCalcolo.get(TipoVariabile.ACZIMPRIDRITTOT);
		if (vcRiduzioniSanzioni == null && vcRiduzioniRitardo == null) {
			return Optional.empty();
		}
		result = (vcRiduzioniSanzioni != null && vcRiduzioniSanzioni.getValNumber().compareTo(BigDecimal.ZERO) > 0)
				|| (vcRiduzioniRitardo != null && vcRiduzioniRitardo.getValNumber().compareTo(BigDecimal.ZERO) > 0);
		return Optional.ofNullable(result);
	}
}

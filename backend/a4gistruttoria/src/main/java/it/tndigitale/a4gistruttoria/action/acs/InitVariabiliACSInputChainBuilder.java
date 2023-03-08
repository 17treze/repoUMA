package it.tndigitale.a4gistruttoria.action.acs;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.action.InitVariabilePercentualeRitardataConsumer;
import it.tndigitale.a4gistruttoria.action.InitVariabiliAgricoltoreAttivoConsumer;

@Component
public class InitVariabiliACSInputChainBuilder {

	@Autowired
	private InitVariabiliAgricoltoreAttivoConsumer ivAgricoltoreAttivoCons;

	@Autowired
	private InitVariabileCampioneSuperficiConsumer ivCampioneSupCons;

	@Autowired
	private InitVariabileSigecoConsumer ivSigecoCons;
	
	@Autowired
	private InitVariabileOlioNazionaleConsumer ivOlioNazCons;
	
	@Autowired
	private InitVariabileOlioDOPConsumer ivOlioQualCons;
	
	@Autowired
	private InitVariabilePercentualeRitardataConsumer ivPercRitCons;
	
	@Autowired
	private InitVariabiliSuperficiImpegnateConsumer ivSupImpCons;
	
	@Autowired
	private InitVariabiliSuperficiDeterminateIstruttoreConsumer ivSupDetIstrutCons;
	
	@Autowired
	private InitVariabiliSuperficiValoriUnitariConsumer ivValUnitCons;
	
	@Autowired
	private InitVariabiliParticellaConsumer ivParticellaCons;

	@Autowired
	private InitVariabiliImportiErogatiACSConsumer ivImpEroCons;
	
	public void initVariabili(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		List<BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel>> initCons = Arrays.asList(ivCampioneSupCons, ivSigecoCons,
				ivOlioNazCons, ivOlioQualCons, ivPercRitCons, ivSupImpCons, ivSupDetIstrutCons, ivValUnitCons, ivParticellaCons,
				ivImpEroCons);
		
		BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> currCons = ivAgricoltoreAttivoCons;
		for (BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> after : initCons) {
			currCons = currCons.andThen(after);
		}
		currCons.accept(handler, istruttoria);
	}
}

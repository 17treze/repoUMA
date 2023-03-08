package it.tndigitale.a4gistruttoria.action.acz;

import it.tndigitale.a4gistruttoria.action.InitVariabilePercentualeRitardataConsumer;
import it.tndigitale.a4gistruttoria.action.InitVariabiliAgricoltoreAttivoConsumer;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

@Component
public class InitVariabiliACZInputChainBuilder {
	
	@Autowired
	private InitVariabiliCapiRichiestiConsumer ivCapiRichiesti;
	
	@Autowired
	private InitVariabiliCapiDuplicatiConsumer ivCapiDuplicati;
	
	@Autowired
	private InitVariabiliCapiRichiestiNettiConsumer ivCapiRichiestiNetti;
	
	@Autowired
	private InitVariabiliCapiSanzionatiConsumer ivCapiSanzionati;
	
	@Autowired
	private InitVariabiliCapiAccertatiConsumer ivCapiAccertati;
	
	@Autowired
	private InitVariabiliNumeroUbaAmmessiConsumer ivNumeroUbaAmmessi;
	
	@Autowired
	private InitVariabiliZootecniaValoriUnitariConsumer ivValoriUnitari;

	@Autowired
	private InitVariabiliAgricoltoreAttivoConsumer ivAgricoltoreAttivo;
	
	@Autowired
	private InitVariabiliControlliLocoConsumer ivControlliLoco;
	
	@Autowired
	private InitVariabiliCampioneZootecniaConsumer ivCampioneZootecnia;
	
	@Autowired
	private InitVariabileOviniAdultiConsumer ivOviniAdulti;

	@Autowired
	private InitVariabilePercentualeRitardataConsumer ivPercentualeRitardata;
	
	@Autowired
	private InitVariabiliPercentualeSanzioneDeterminataConsumer ivPercentualeSanzioneDeterminata;
	
	@Autowired
	private InitVariabiliPercentualeSanzioneApplicataConsumer ivPercentualeSanzioneApplicata;
	
	@Autowired
	private InitVariabiliNumeroCapiRegistroDebitoriConsumer	 ivNumeroCapiRegistroDebitori;
	
	@Autowired
	private InitVariabiliImportiErogatiACZConsumer ivImpEro;
	
	public void initVariabili(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		List<BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel>> initCons = Arrays.asList(ivCampioneZootecnia, ivControlliLoco, ivOviniAdulti,
				ivCapiRichiesti, ivCapiDuplicati, ivCapiRichiestiNetti, ivCapiSanzionati, ivCapiAccertati, 
				ivNumeroUbaAmmessi, ivValoriUnitari, ivPercentualeRitardata, ivPercentualeSanzioneDeterminata,
				ivPercentualeSanzioneApplicata, ivNumeroCapiRegistroDebitori, ivImpEro);
		
		BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> currCons = ivAgricoltoreAttivo;
		for (BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> after : initCons) {
			currCons = currCons.andThen(after);
		}
		currCons.accept(handler, istruttoria);
	}
}

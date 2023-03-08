package it.tndigitale.a4gistruttoria.action.acs;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

@Component
public class CalcoloOutputAccoppiatoSuperficieChainBuilder {

	@Autowired
	private CalcoloOutputCalcoloParticelleMisureConsumer calcoloSostegnoParticelleMisure;
	
	@Autowired
	private CalcoloOutputSuperficiRichiesteConsumer coSupRichieste;
	
	@Autowired
	private CalcoloOutputSuperficiDeterminateConsumer coSupDeterminate;

	@Autowired
	private CalcoloOutputSuperficiAmmesseConsumer coSupAmmesse;

	@Autowired
	private CalcoloOutputImportiRichiestiMisureConsumer coImpRichiesti;

	@Autowired
	private CalcoloOutputImportiAmmessiMisureConsumer coImpAmmessi;

	@Autowired
	private CalcoloOutputImportiRiduzioniMisureConsumer coImpRiduzioni;

	@Autowired
	private CalcoloOutputImportiRiduzioniRitardoMisureConsumer coImpRiduzioniRitardo;

	@Autowired
	private CalcoloOutputImportiCalcolatiMisureConsumer coImpCalcolati;

	@Autowired
	private CalcoloOutputImportiCalcolatiLordiMisureConsumer coImpCalcolatiLordi;
	
	public void initVariabili(CalcoloAccoppiatoHandler hanlder, IstruttoriaModel istruttoria) {
		List<BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel>> initCons = Arrays.asList(coSupRichieste, coSupDeterminate,
				coSupAmmesse, coImpRichiesti, coImpAmmessi, coImpRiduzioni, coImpRiduzioniRitardo, coImpCalcolatiLordi,
				coImpCalcolati);
		
		BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> currCons = calcoloSostegnoParticelleMisure;
		for (BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> after : initCons) {
			currCons = currCons.andThen(after);
		}
		currCons.accept(hanlder, istruttoria);
	}
}

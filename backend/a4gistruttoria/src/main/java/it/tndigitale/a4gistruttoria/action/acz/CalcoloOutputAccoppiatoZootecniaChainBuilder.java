package it.tndigitale.a4gistruttoria.action.acz;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CalcoloOutputAccoppiatoZootecniaChainBuilder {

	@Autowired
	private CalcoloOutputImportoRichiestoNettoConsumer coImpRichiestoNetto;
	
	@Autowired
	private CalcoloOutputImportoRiduzioneConsumer coImpRiduzione;
	
	@Autowired
	private CalcoloOutputImportoAccertatoConsumer coImpAccertato;
	
	@Autowired
	private CalcoloOutputImportoRiduzioneSanzioneConsumer coImpRiduzioneSanzione;
	
	@Autowired
	private CalcoloOutputImportoRiduzioneRitardoConsumer coImpRiduzioneRitardo;
	
	@Autowired
	private CalcoloOutputImportoCalcolatoLordoConsumer coImpCalcolatoLordo;
	
	@Autowired
	private CalcoloOutputImportoRegistroDebitoriConsumer coImportoRegistroDebitori;
	
	@Autowired
	private CalcoloOutputImportoCalcolatoConsumer coImpCalcolato;
	
	public void initVariabili(CalcoloAccoppiatoHandler handler, IstruttoriaModel domanda) {
		List<BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel>> initCons = Arrays.asList(
				coImpRichiestoNetto, coImpRiduzione, coImpAccertato, coImpRiduzioneSanzione,
				coImpRiduzioneRitardo, coImpCalcolatoLordo, coImportoRegistroDebitori, coImpCalcolato
				);
		
		BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> currCons = coImpRichiestoNetto;
		for (BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> after : initCons) {
			currCons = currCons.andThen(after);
		}
		currCons.accept(handler, domanda);
	}
}

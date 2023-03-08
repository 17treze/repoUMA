package it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione;

import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.GeneraDatiAnnualiStrategy;
import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.ElaborazioneDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.statistica.GeneraStatisticaBase;
import it.tndigitale.a4gistruttoria.strategy.SincronizzazioneInputData;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class GeneraDatiSincronizzazioneBase<T extends Enum<T>> implements GeneraDatiAnnualiStrategy<T>, ElaborazioneDomanda {

	@Autowired
	protected IstruttoriaComponent istruttoriaComponent;

	private static final Logger logger = LoggerFactory.getLogger(GeneraStatisticaBase.class);

	@Autowired
	private DomandaUnicaDao domandaDao;

	@Override
	@Transactional(readOnly = true)
	public List<IstruttoriaModel> caricaIstruttorie(Integer annoCampagna) {
		List<IstruttoriaModel> istruttorie = null;
		List<DomandaUnicaModel> domande = domandaDao.findByCampagna(annoCampagna);

		if (domande == null || domande.isEmpty()) {
			return istruttorie;
		}

		istruttorie = new ArrayList<IstruttoriaModel>();

		logger.info("per ogni domanda [# {}] dell'anno di campagna {} carico le relative istruttorie", domande.size(), annoCampagna);

		for (DomandaUnicaModel domanda : domande) {
			addIstruttorie(domanda, istruttorie);
		}
		logger.info("completato caricamento delle relative istruttorie [# {}]", istruttorie.size());

		return istruttorie;
	}

	protected void addIstruttorie(DomandaUnicaModel domanda, List<IstruttoriaModel> istruttorie) {
		istruttorie.add(istruttoriaComponent.getUltimaIstruttoria(domanda, Sostegno.DISACCOPPIATO));
	}
	
	protected SincronizzazioneInputData caricaDatiInput(IstruttoriaModel istruttoria) {
		if (istruttoria == null)
			return null;
		return SincronizzazioneInputData.from(istruttoria.getDomandaUnicaModel(), SincronizzazioneInputData.class);
	}
	
	public <T> T nvl(T a, T b) {
		return (a == null) ? b : a;
	}
	
	/**
	 * 
	 * Accetta in ingresso un idIstruttoria a differenza della altre implementazioni
	 *
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElaborazioneDomandaException.class)
	public void elabora(Long idIstruttoria) throws ElaborazioneDomandaException {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);
		generaDatiPerIstruttoria(
				istruttoria, 
				istruttoria.getDomandaUnicaModel().getCampagna()
				);
	}

	/**
	 * 
	 * Per non intaccare il codice pre-esistente vengono recuperate tutte le domande e poi 
	 * collezzionati tutti gli ID ISTRUTTORIA da passare al metodo elabora. Questo differisce dalle altre implementazioni 
	 *
	 */
	@Override
	public List<Long> caricaIdDaElaborare(Integer annoCampagna) throws ElaborazioneDomandaException {
		cancellaDatiEsistenti(getTipoDatoAnnuale(), annoCampagna);
		List<IstruttoriaModel> istruttorie = caricaIstruttorie(annoCampagna);
		logger.info("Avvio generazione dati sincronizzazione per {} istruttorie per tipo {} anno {}",
				istruttorie.size(), getTipoDatoAnnuale(), annoCampagna);
		return istruttorie.stream()
				.filter(Objects::nonNull)
				.map(IstruttoriaModel::getId)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

}

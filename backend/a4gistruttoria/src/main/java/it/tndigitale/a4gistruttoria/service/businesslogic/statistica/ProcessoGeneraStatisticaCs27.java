package it.tndigitale.a4gistruttoria.service.businesslogic.statistica;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.ElaborazioneDomanda;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;
import it.tndigitale.a4gistruttoria.util.CustomCollectors;

/**
 * 
 * Servizio che fa la UNION di GeneraStatisticaCs27Superficie e GeneraStatisticaCs27Zootecnia
 * in modo da gestire correttamente l'avanzamento del processo
 * 
 * @author s.caccia
 *
 */
@Component
public class ProcessoGeneraStatisticaCs27 implements ElaborazioneDomanda{
	
	@Autowired
	private GeneraStatisticaCs27Superficie generaStatisticaCs27Superficie;
	@Autowired
	private GeneraStatisticaCs27Zootecnia generaStatisticaCs27Zootecnia;
	@Autowired
	private IstruttoriaComponent istruttoriaComponent;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElaborazioneDomandaException.class)
	public void elabora(Long idIstruttoria) throws ElaborazioneDomandaException {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);
		//recupero la statistica corretta da eseguire in base al sostegno
		GeneraStatisticaCs27 generaStatisticaCs27 = 
				Stream.of(generaStatisticaCs27Superficie, generaStatisticaCs27Zootecnia)
					.filter(p -> p.getSostegno().equals(istruttoria.getSostegno()))
					.collect(CustomCollectors.toSingleton());
		generaStatisticaCs27.elabora(idIstruttoria);
	}

	@Override
	public List<Long> caricaIdDaElaborare(Integer annoCampagna) throws ElaborazioneDomandaException {
		List<Long> idDaElaborare=generaStatisticaCs27Superficie.caricaIdDaElaborare(annoCampagna);
		idDaElaborare.addAll(generaStatisticaCs27Zootecnia.caricaIdDaElaborare(annoCampagna));
		return idDaElaborare;
	}

}

package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.dis.CaricaPremioCalcolatoDisaccoppiato;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

@Component(VerificatoreIstruttoriaIntegrazioneAvviabile.NOME_QUALIFICATORE)
public class VerificatoreIstruttoriaIntegrazioneAvviabile extends VerificatoreIstruttoriaAvviabile {
	
	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "INTEGRAZIONE";

	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@Autowired
	private DomandaUnicaDao domandaDao;
	
	@Override
	public boolean isIstruttoriaAvviabile(Long idDomanda, TipoIstruttoria tipologia, Sostegno sostegno) {
		return super.isIstruttoriaAvviabile(idDomanda, tipologia, sostegno) //Non hanno un’istruttoria di integrazione in corso o conclusa  
				&& esisteIstruttoriaSaldoPagata(idDomanda, sostegno);//Hanno il saldo nello stato “pagamento autorizzato”
	}
	
	protected boolean esisteIstruttoriaSaldoPagata(Long idDomanda, Sostegno sostegno) {
		DomandaUnicaModel domanda = domandaDao.findById(idDomanda).get();
		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, sostegno, TipoIstruttoria.SALDO);
		return istruttoria != null && StatoIstruttoria.PAGAMENTO_AUTORIZZATO.equals(istruttoria.getStato());
	}

}

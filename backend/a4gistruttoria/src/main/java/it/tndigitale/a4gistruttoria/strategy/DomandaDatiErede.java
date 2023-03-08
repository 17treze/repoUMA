package it.tndigitale.a4gistruttoria.strategy;

import java.util.Optional;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.DatiErede;
import it.tndigitale.a4gistruttoria.dto.Domanda;
import it.tndigitale.a4gistruttoria.repository.dao.DatiEredeDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiErede;

@Component
public class DomandaDatiErede implements DatiDomanda {

	@Autowired
	private DatiEredeDao datiEredeDao;
	
	@Override
	public void recupera(Domanda domanda) {
		DatiErede erede = new DatiErede();
        A4gtDatiErede a4gtDatiErede = new A4gtDatiErede();
        DomandaUnicaModel domandaUnicaModel = new DomandaUnicaModel();
        domandaUnicaModel.setId(domanda.getId());
        a4gtDatiErede.setDomandaUnicaModel(domandaUnicaModel);
        
        Optional<A4gtDatiErede> datiEredeOpt = datiEredeDao.findOne(Example.of(a4gtDatiErede));
        if (datiEredeOpt.isPresent()) {
            a4gtDatiErede = datiEredeOpt.get();
            BeanUtils.copyProperties(datiEredeOpt.get(), erede);
            erede.setIndirizzoResidenza(a4gtDatiErede.getIndirizzo());
            erede.setProvResidenza(a4gtDatiErede.getProvincia());
            erede.setCapResidenza(a4gtDatiErede.getCap());
            erede.setProvNascita(a4gtDatiErede.getProvinciaNascita());
            domanda.setErede(erede);
        }
	}
}

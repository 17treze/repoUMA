package it.tndigitale.a4gistruttoria.strategy;

import it.tndigitale.a4gistruttoria.dto.Domanda;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaDto;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DomandaLavorazioneSostegno  implements DatiDomanda {
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	@Override
	public void recupera(Domanda domanda) {
		IstruttoriaModel exampleA4gtLavSos=new IstruttoriaModel();
		DomandaUnicaModel domandaUnicaModel =new DomandaUnicaModel();
		domandaUnicaModel.setId(domanda.getId());
		exampleA4gtLavSos.setDomandaUnicaModel(domandaUnicaModel);
		List<IstruttoriaModel> istruttorie = istruttoriaDao.findAll(Example.of(exampleA4gtLavSos));
		domanda.setIstruttoriaDto(new ArrayList<>());
		istruttorie.forEach(istruttoria -> {
			IstruttoriaDto istruttoriaDto = new IstruttoriaDto();
			istruttoriaDto.setId(istruttoria.getId());
			istruttoriaDto.setBloccataBool(istruttoria.getBloccataBool());
			istruttoriaDto.setStatoIstruttoria(istruttoria.getA4gdStatoLavSostegno().getIdentificativo());
			istruttoriaDto.setSostegno(istruttoria.getSostegno());
			domanda.getIstruttoriaDto().add(istruttoriaDto);
		});
		
	}
	
}

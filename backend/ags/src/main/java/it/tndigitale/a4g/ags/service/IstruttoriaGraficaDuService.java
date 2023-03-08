package it.tndigitale.a4g.ags.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.ags.dto.IstruttoriaGraficaDuDto;
import it.tndigitale.a4g.ags.entity.ViewIstruttoriaGraficaDuEntity;
import it.tndigitale.a4g.ags.repository.dao.ViewIstruttoriaGraficaDuDao;

@Service
public class IstruttoriaGraficaDuService {
	
	@Autowired
	private ViewIstruttoriaGraficaDuDao viewIstruttoriaGraficaDuDao;
	
	@Autowired
	private IstruttoriaGraficaDuConverter istruttoriaGraficaDuConverter;
	
	public IstruttoriaGraficaDuDto getByCuaaAndIdDomanda(final String cuaa, final Long id) {
		Optional<ViewIstruttoriaGraficaDuEntity> istruttoriaGraficaOpt = viewIstruttoriaGraficaDuDao.findByCuaaAndIdDomanda(cuaa, id);
		if (istruttoriaGraficaOpt.isPresent()) {
			return istruttoriaGraficaDuConverter.convert(istruttoriaGraficaOpt.get());			
		} else return null; 
	}
}

package it.tndigitale.a4g.territorio.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dao.SezioneCatastaleAgsDao;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto.ComuneAmministrativoDto;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto.ProvinciaDto;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dto.SezioneCatastaleDto;
import it.tndigitale.a4g.territorio.dto.ComuneAmministrativo;
import it.tndigitale.a4g.territorio.dto.Provincia;
import it.tndigitale.a4g.territorio.dto.SezioneCatastale;
import it.tndigitale.a4g.territorio.dto.SezioneCatastaleFilter;

@Service
public class SezioneCatastaleService {

	@Autowired 
	private SezioneCatastaleAgsDao dao;
	
	public RisultatiPaginati<SezioneCatastale> ricerca(SezioneCatastaleFilter criteri) {
		RisultatiPaginati<SezioneCatastaleDto> sezioniTrovate = dao.ricerca(criteri);
		RisultatiPaginati<SezioneCatastale> result = new RisultatiPaginati<SezioneCatastale>();
		result.setCount(sezioniTrovate.getCount());
		result.setRisultati(SezioneCatastaleSupport.from(sezioniTrovate.getRisultati()));
		return result;
	}
	
	public static class SezioneCatastaleSupport {
	    public static List<SezioneCatastale> from(List<SezioneCatastaleDto> resultObjects) {
	        return Optional.ofNullable(resultObjects)
	        			.orElse(new ArrayList<SezioneCatastaleDto>())
	                       .stream()
	                       .map(objects -> from(objects))
	                       .collect(Collectors.toList());
	    }
	    
	    public static SezioneCatastale from(SezioneCatastaleDto sezioneRes) {
	    	SezioneCatastale result = new SezioneCatastale();
	    	result.setCodice(sezioneRes.getCodice());
	    	result.setDenominazione(sezioneRes.getDenominazione());
	    	result.setComune(from(sezioneRes.getComune()));
	    	return result;
	    }
		
	    public static ComuneAmministrativo from(ComuneAmministrativoDto caRes) {
	    	ComuneAmministrativo result = new ComuneAmministrativo();
	    	result.setCodiceFiscale(caRes.getCodiceFiscale());
	    	result.setCodiceIstat(caRes.getCodiceIstat());
	    	result.setDenominazione(caRes.getDenominazione());
	    	result.setProvincia(from(caRes.getProvincia()));
	    	return result;
	    }

	    public static Provincia from(ProvinciaDto provRes) {
	    	Provincia result = new Provincia();
	    	result.setSigla(provRes.getSigla());
	    	result.setDenominazione(provRes.getDenominazione());
	    	result.setCodiceIstat(provRes.getCodiceIstat());
	    	return result;
	    }
	}
}

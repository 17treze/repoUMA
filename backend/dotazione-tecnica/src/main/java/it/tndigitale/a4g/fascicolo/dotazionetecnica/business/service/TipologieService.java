package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.ClasseFunzionaleModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.TipologiaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.AmbitoTipologia;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.ClasseFunzionaleDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipologiaConverter;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipologiaDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.ClasseFunzionaleDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.SottotipoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaDto;

@Service
public class TipologieService {

	private static final Logger logger = LoggerFactory.getLogger(TipologieService.class);

	@Autowired
	private TipologiaDao tipologiaDao;
	@Autowired
	private ClasseFunzionaleDao classeFunzionaleDao;
	@Autowired
	private TipologiaConverter tipologiaConverter;

	public List<TipologiaDto> getTipologie(AmbitoTipologia ambito) {
		List<TipologiaModel> tipologieModel = tipologiaDao.findByAmbito(ambito);
		logger.info("Recupero tipologie fascicolo");
		return tipologieModel.stream().map(tipologiaConverter::convert).collect(Collectors.toList());
	}

	public ClasseFunzionaleDto getClasseFunzionale(Long idTipologia) {
		TipologiaModel tipologia = tipologiaDao.findById(idTipologia).orElseThrow(() -> new IllegalArgumentException("Nessuna tipologia trovata per id " + idTipologia));
		var classeFunzionale = new ClasseFunzionaleDto();
		classeFunzionale.setId(tipologia.getId());
		classeFunzionale.setDescrizione(tipologia.getDescrizione());
		classeFunzionale.setTipologie(tipologia.getClassiFunzionaliMacchinario().stream().map(tipologiaConverter::convert).collect(Collectors.toList()));
		return classeFunzionale;
	}

	public SottotipoDto getSottotipi(Long id, AmbitoTipologia ambito) {
		if (ambito != null && ambito.equals(AmbitoTipologia.MACCHINE)) {
			ClasseFunzionaleModel classeFunzionale = classeFunzionaleDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Nessuna classe funzionale trovata per id " + id));
			var sottotipo = new SottotipoDto();
			sottotipo.setId(classeFunzionale.getId());
			sottotipo.setDescrizione(classeFunzionale.getDescrizione());
			sottotipo.setTipologie(classeFunzionale.getSottotipologieMacchinario().stream().map(tipologiaConverter::convert).collect(Collectors.toList()));
			return sottotipo;
		} else {
			TipologiaModel tipologia = tipologiaDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Nessuna tipologia trovata per id " + id));
			var sottotipo = new SottotipoDto();
			sottotipo.setId(tipologia.getId());
			sottotipo.setDescrizione(tipologia.getDescrizione());
			sottotipo.setTipologie(tipologia.getSottotipologieMacchinario().stream().map(tipologiaConverter::convert).collect(Collectors.toList()));
			return sottotipo;
		}
	}

}

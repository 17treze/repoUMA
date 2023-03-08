package it.tndigitale.a4gistruttoria.action.antimafia;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.DomandaUnicheCsvImport;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateImport;
import it.tndigitale.a4gistruttoria.repository.dao.DomandeCollegateDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;
import it.tndigitale.a4gistruttoria.util.AntimafiaDomandeCollegateHelper;
import it.tndigitale.a4gistruttoria.util.StatoDomandaCollegata;
import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

@Component
public class DatiDuHandler {

	@Autowired
	private DomandeCollegateDao domandeCollegateDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void elaboraDomanda(DomandaUnicheCsvImport domandaUnicaCsvImport,List<DomandaCollegata> domandeCollegate, DomandeCollegateImport domandeCollegateImport) {
		BigDecimal importoDomanda = BigDecimal.valueOf(domandaUnicaCsvImport.getImportoRichiesto().doubleValue());
		if (importoDomanda.compareTo(domandeCollegateImport.getImporto()) >= 0 && domandeCollegateImport.getDataPresentazione().before(domandaUnicaCsvImport.getDataPresentazione())) {
			A4gtDomandeCollegate filter = new A4gtDomandeCollegate();
			filter.setTipoDomanda(TipoDomandaCollegata.DOMANDA_UNICA);
			filter.setCuaa(domandaUnicaCsvImport.getCuaa());
			filter.setIdDomanda(domandaUnicaCsvImport.getIdDomanda());
			Optional<A4gtDomandeCollegate> domanda = domandeCollegateDao.findOne(Example.of(filter));
			A4gtDomandeCollegate a4gtDomandeCollegate;
			if (!domanda.isPresent()) {
				a4gtDomandeCollegate = new A4gtDomandeCollegate();
				a4gtDomandeCollegate.setIdDomanda(domandaUnicaCsvImport.getIdDomanda());
				a4gtDomandeCollegate.setCuaa(domandaUnicaCsvImport.getCuaa());
				a4gtDomandeCollegate.setImportoRichiesto(BigDecimal.valueOf(domandaUnicaCsvImport.getImportoRichiesto().doubleValue()));
				a4gtDomandeCollegate.setDtDomanda(domandaUnicaCsvImport.getDataPresentazione());
				a4gtDomandeCollegate.setTipoDomanda(TipoDomandaCollegata.DOMANDA_UNICA);
				a4gtDomandeCollegate.setStatoBdna(StatoDomandaCollegata.NON_CARICATO);
				a4gtDomandeCollegate.setCampagna(domandaUnicaCsvImport.getCampagna());
				domandeCollegateDao.save(a4gtDomandeCollegate);
				DomandaCollegata domandaCollegata = new DomandaCollegata();
				BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
				domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
				domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
				domandeCollegate.add(domandaCollegata);
			} else {
				a4gtDomandeCollegate = domanda.get();
				if (a4gtDomandeCollegate.getImportoRichiesto().compareTo(importoDomanda) != 0) {
					a4gtDomandeCollegate.setImportoRichiesto(importoDomanda);
					a4gtDomandeCollegate.setStatoBdna(StatoDomandaCollegata.NON_CARICATO);
					a4gtDomandeCollegate.setDtDomanda(domandaUnicaCsvImport.getDataPresentazione());
					AntimafiaDomandeCollegateHelper.resetDomandaCollegata(a4gtDomandeCollegate);
					a4gtDomandeCollegate.setCampagna(domandaUnicaCsvImport.getCampagna());
					domandeCollegateDao.save(a4gtDomandeCollegate);
					DomandaCollegata domandaCollegata = new DomandaCollegata();
					BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
					domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
					domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
					domandeCollegate.add(domandaCollegata);
				}
			}
		}
	}

}

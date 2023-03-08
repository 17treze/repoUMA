package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.framework.time.Clock;

@Component
public class FascicoloModelValidazioneConverter implements Converter<FascicoloModel, ValidazioneFascicoloDto> {

	@Autowired private MandatoDao mandatoDao;
	@Autowired private Clock clock;
	
	@Override
	public ValidazioneFascicoloDto convert(final FascicoloModel source) {
		var output = new ValidazioneFascicoloDto();
		output.setId(source.getId());
		output.setIdValidazione(source.getIdValidazione());
		output.setCuaa(source.getCuaa());
		output.setDataModifica(source.getDataModifica());
		output.setDataValidazione(source.getDataValidazione());
//		output.setDenominazioneImpresa(source.getDenominazione());
		output.setUtenteValidazione(
				source.getUtenteValidazione());
		List<MandatoModel> mandatiModel = mandatoDao.findByFascicolo(source);
		LocalDate nowDate = clock.today();
		for (MandatoModel mandatoModel : mandatiModel) {
			LocalDate dataInizio = mandatoModel.getDataInizio();
			LocalDate dataFine = mandatoModel.getDataFine();
			if (dataInizio.isAfter(nowDate) || !(dataFine == null || nowDate.isBefore(dataFine))) {
				continue;
			}
			SportelloModel sportello = mandatoModel.getSportello();
			if (sportello != null) {
				output.setDenominazioneSportello(
						sportello.getDenominazione());
			}
			
		}
		return output;
	}
}

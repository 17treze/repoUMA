package it.tndigitale.a4g.fascicolo.anagrafica.dto.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CentroAssistenzaAgricolaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.IndirizzoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.CentroAssistenzaAgricolaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloCAADto;

public class CentroAssistenzaAgricolaDtoBuilder {

	private CentroAssistenzaAgricolaDto centroAssistenzaAgricolaDto;

	public CentroAssistenzaAgricolaDtoBuilder() {
		centroAssistenzaAgricolaDto = new CentroAssistenzaAgricolaDto();
	}

	public CentroAssistenzaAgricolaDtoBuilder withSoggetto(CentroAssistenzaAgricolaModel caaModel) {
		BeanUtils.copyProperties(caaModel, centroAssistenzaAgricolaDto);
		centroAssistenzaAgricolaDto.setDenominazione(caaModel.getDenominazione());
		centroAssistenzaAgricolaDto.setEstremiAtto(caaModel.getAttoRiconoscimento());
		centroAssistenzaAgricolaDto.setSocietaServizi(caaModel.getSocietaServizi());
		
		IndirizzoDto indirizzoDto = new IndirizzoDto();
		indirizzoDto.setVia(caaModel.getIndirizzo().getToponimo());
		indirizzoDto.setCap(caaModel.getIndirizzo().getCap());
		indirizzoDto.setDenominazioneComune(caaModel.getIndirizzo().getComune());
		indirizzoDto.setSiglaProvincia(caaModel.getIndirizzo().getProvincia());
		centroAssistenzaAgricolaDto.setIndirizzo(indirizzoDto);
		
		return this;
	}

	public CentroAssistenzaAgricolaDtoBuilder withSportelli(List<SportelloModel> sportelloModelList) {
		List<SportelloCAADto> sportelliDto = new ArrayList<>();
		centroAssistenzaAgricolaDto.setSportelli(sportelliDto);
		sportelloModelList.forEach(sportello ->	{
			SportelloCAADto sportelloDto = new SportelloCAADto();
			BeanUtils.copyProperties(sportello, sportelloDto);
			sportelloDto.setIdentificativo(sportello.getIdentificativo());
			sportelloDto.setDenominazione(sportello.getDenominazione());
			sportelloDto.setIndirizzo(sportello.getIndirizzo());
			sportelloDto.setComune(sportello.getComune());
			sportelloDto.setCap(sportello.getCap());
			sportelloDto.setProvincia(sportello.getProvincia());
			sportelloDto.setTelefono(sportello.getTelefono());
			sportelloDto.setEmail(sportello.getEmail());
			sportelliDto.add(sportelloDto);
		});
		centroAssistenzaAgricolaDto.setSportelli(sportelliDto);
		return this;
	}

	public CentroAssistenzaAgricolaDto build() {
		return centroAssistenzaAgricolaDto;
	}
}

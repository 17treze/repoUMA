package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CentroAssistenzaAgricolaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.RevocaImmediataModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DescrizioneRichiestaRevocaImmediataMandatoDto;

@Component
public class RichiestaRevocaImmediataConverter implements Converter<RevocaImmediataModel, DescrizioneRichiestaRevocaImmediataMandatoDto> {

	@Override
	public DescrizioneRichiestaRevocaImmediataMandatoDto convert(final RevocaImmediataModel source) {
		DescrizioneRichiestaRevocaImmediataMandatoDto dto = new DescrizioneRichiestaRevocaImmediataMandatoDto();
		
		MandatoModel mandato = source.getMandato();
		FascicoloModel fascicolo = source.getMandato().getFascicolo();
		SportelloModel sportelloModel = mandato.getSportello();
		if (sportelloModel != null) {
			CentroAssistenzaAgricolaModel centroAssistenzaAgricola = sportelloModel.getCentroAssistenzaAgricola();
			dto.setSedeAmministrativaCaa(centroAssistenzaAgricola.getIndirizzo().getComune());
			dto.setDenominazioneSportelloCaa(sportelloModel.getDenominazione());
			dto.setSedeSportelloCaa(sportelloModel.getComune());
			dto.setDenominazioneSedeAmministrativaCaa(centroAssistenzaAgricola.getDenominazione());
		}
		dto.setCuaa(fascicolo.getCuaa());
		dto.setDenominazioneAzienda(fascicolo.getDenominazione());
		dto.setIdProtocollo(source.getIdProtocollo());
		dto.setCausaRichiesta(source.getCausaRichiesta());
		dto.setDataSottoscrizione(source.getDataSottoscrizione());
		dto.setCodiceFiscaleRappresentante(source.getCodiceFiscale());
		dto.setMotivazioneRifiuto(source.getMotivazioneRifiuto());
		dto.setDataValutazione(source.getDataValutazione());
		dto.setEsito(source.getStato());
		return dto;
	}
	
	public List<DescrizioneRichiestaRevocaImmediataMandatoDto> convertList(final List<RevocaImmediataModel> sources) {
		List<DescrizioneRichiestaRevocaImmediataMandatoDto> dtos = new ArrayList<>();
		for (RevocaImmediataModel source : sources) {
			dtos.add(convert(source));
		}
		return dtos;
	}
}
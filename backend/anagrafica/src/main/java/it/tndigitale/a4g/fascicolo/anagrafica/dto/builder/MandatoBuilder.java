package it.tndigitale.a4g.fascicolo.anagrafica.dto.builder;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CentroAssistenzaAgricolaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IndirizzoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.IndirizzoSedeAmministrativaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.MandatoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.TipoDetenzioneEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloCAADto;

public class MandatoBuilder {
	
	private MandatoBuilder() {}
	
	public static MandatoDto from(final MandatoModel mandatoModel)  {
		if (mandatoModel == null) return null;
		SportelloModel sportelloModel = mandatoModel.getSportello();
		CentroAssistenzaAgricolaModel centroAssistenzaAgricola = sportelloModel.getCentroAssistenzaAgricola();
		MandatoDto mandatoDto = new MandatoDto();
		mandatoDto.setTipoDetenzione(TipoDetenzioneEnum.MANDATO);
		DetenzioneBuilder.fromAux(mandatoDto, mandatoModel);
		mandatoDto.setDenominazione(sportelloModel.getCentroAssistenzaAgricola().getDenominazione());//denominazione CAA
		mandatoDto.setAttoRiconoscimento(centroAssistenzaAgricola.getAttoRiconoscimento());
		mandatoDto.setCodiceFiscale(centroAssistenzaAgricola.getCodiceFiscale());
		mandatoDto.setPartitaIva(centroAssistenzaAgricola.getPartitaIVA());
		mandatoDto.setSocietaServizi(centroAssistenzaAgricola.getSocietaServizi());
		mandatoDto.setDataSottoscrizione(mandatoModel.getDataSottoscrizione());
		
		IndirizzoSedeAmministrativaDto indirizzoSedeAmministrativaDto = new IndirizzoSedeAmministrativaDto();
		IndirizzoModel indirizzoSedeAmministrativa = centroAssistenzaAgricola.getIndirizzo();
		indirizzoSedeAmministrativaDto.setCap(indirizzoSedeAmministrativa.getCap());
		indirizzoSedeAmministrativaDto.setDenominazioneComune(indirizzoSedeAmministrativa.getComune());
		indirizzoSedeAmministrativaDto.setDenominazioneProvincia(indirizzoSedeAmministrativa.getProvincia());
		indirizzoSedeAmministrativaDto.setSiglaProvincia(indirizzoSedeAmministrativa.getProvincia());
		indirizzoSedeAmministrativaDto.setToponimo(indirizzoSedeAmministrativa.getToponimo());
		mandatoDto.setIndirizzoSedeAmministrativa(indirizzoSedeAmministrativaDto);
		
		SportelloCAADto sportelloDto = new SportelloCAADto();
		sportelloDto.setId(sportelloModel.getId());
		sportelloDto.setDenominazione(sportelloModel.getDenominazione());
		sportelloDto.setIdentificativo(sportelloModel.getIdentificativo());
		sportelloDto.setComune(sportelloModel.getComune());
		sportelloDto.setIndirizzo(sportelloModel.getIndirizzo());
		sportelloDto.setCap(sportelloModel.getCap());
		sportelloDto.setProvincia(sportelloModel.getProvincia());
		sportelloDto.setTelefono(sportelloModel.getTelefono());
		sportelloDto.setEmail(sportelloModel.getEmail());
		mandatoDto.setSportello(sportelloDto);
		return mandatoDto;
	}
}

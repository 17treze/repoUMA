package it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.persona;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ImpresaIndividualeModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IndirizzoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SedeModel;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAnagraficiDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IndirizzoDto;

public final class PersonaFisicaBuilder {

	private DatiAperturaFascicoloDto datiAperturaFascicoloDto;

	public PersonaFisicaBuilder() {
		datiAperturaFascicoloDto = new DatiAperturaFascicoloDto();
	}

	public PersonaFisicaBuilder withDatiAnagraficiRappresentante(PersonaFisicaModel personaFisicaModel) {
		if (personaFisicaModel != null) {
			DatiAnagraficiDto datiAnagraficiDto = new DatiAnagraficiDto();
			datiAnagraficiDto.setCodiceFiscale(personaFisicaModel.getCodiceFiscale());
			datiAnagraficiDto.setComuneNascita(personaFisicaModel.getComuneNascita());
			datiAnagraficiDto.setDataNascita(personaFisicaModel.getDataNascita());
			datiAnagraficiDto.setNominativo(personaFisicaModel.getCognome() + " " + personaFisicaModel.getNome());
			datiAnagraficiDto.setProvinciaNascita(personaFisicaModel.getProvinciaNascita());
			datiAnagraficiDto.setPec(personaFisicaModel.getPec());
			datiAperturaFascicoloDto.setDatiAnagraficiRappresentante(datiAnagraficiDto);
		}
		return this;
	}

	public PersonaFisicaBuilder withDomicilioFiscaleRappresentante(IndirizzoModel domicilioFiscaleModel) {
		if (domicilioFiscaleModel != null) {
			IndirizzoDto domicilioFiscaleRappresentante = new IndirizzoDto();
			domicilioFiscaleRappresentante.setCap(domicilioFiscaleModel.getCap());
			domicilioFiscaleRappresentante.setCivico(domicilioFiscaleModel.getNumeroCivico());
			domicilioFiscaleRappresentante.setCodiceIstat(domicilioFiscaleModel.getCodiceIstat());
			domicilioFiscaleRappresentante.setComune(domicilioFiscaleModel.getComune());
			domicilioFiscaleRappresentante.setFrazione(domicilioFiscaleModel.getFrazione());
			domicilioFiscaleRappresentante.setLocalita(domicilioFiscaleModel.getFrazione());
			domicilioFiscaleRappresentante.setProvincia(domicilioFiscaleModel.getProvincia());
			domicilioFiscaleRappresentante.setToponimo(domicilioFiscaleModel.getDescrizioneEstesa());
			domicilioFiscaleRappresentante.setVia(domicilioFiscaleModel.getVia());
			datiAperturaFascicoloDto.setDomicilioFiscaleRappresentante(domicilioFiscaleRappresentante);
		}
		return this;
	}

	public PersonaFisicaBuilder withDittaIndividuale(ImpresaIndividualeModel impresaIndividualeModel) {
		if (impresaIndividualeModel != null) {
			datiAperturaFascicoloDto.setNaturaGiuridica(impresaIndividualeModel.getFormaGiuridica() != null ? impresaIndividualeModel.getFormaGiuridica() : "Impresa individuale");
			datiAperturaFascicoloDto.setPartitaIva(impresaIndividualeModel.getPartitaIVA());
			datiAperturaFascicoloDto.setCodiceFiscale(impresaIndividualeModel.getPartitaIVA());
			datiAperturaFascicoloDto.setDenominazione(impresaIndividualeModel.getDenominazione());
			// si è scelto di reperire l'indirizzo della ditta da anagrafe tributaria invece che da camera di commercio
			SedeModel sedeLegale = impresaIndividualeModel.getSedeLegale();
			IndirizzoDto ubicazioneDittaAnagrafeTributaria = new IndirizzoDtoBuilder()
					.withIndirizzo(sedeLegale.getIndirizzo())
					.build();

			datiAperturaFascicoloDto.setUbicazioneDitta(ubicazioneDittaAnagrafeTributaria);
		}
		return this;
	}

//	public PersonaFisicaBuilder withDenominazione(PersonaFisicaModel personaFisicaModel) {
//
//		return this;
//	}
	public PersonaFisicaBuilder withDenominazioneFascicolo(FascicoloModel fascicoloModel) {
		if (fascicoloModel != null && fascicoloModel.getDenominazione() != null) {
			datiAperturaFascicoloDto.setDenominazioneFascicolo(fascicoloModel.getDenominazione());			
		}
		return this;
	}

	public DatiAperturaFascicoloDto build() {
		return datiAperturaFascicoloDto;
	}
}

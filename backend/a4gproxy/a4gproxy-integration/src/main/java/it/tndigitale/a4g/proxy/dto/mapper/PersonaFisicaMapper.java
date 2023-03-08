package it.tndigitale.a4g.proxy.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.proxy.dto.persona.AnagraficaDto;
import it.tndigitale.a4g.proxy.dto.persona.ImpresaIndividualeDto;
import it.tndigitale.a4g.proxy.dto.persona.IscrizioneSezioneDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.dto.persona.SedeDto;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import it.tndigitale.ws.isvalidazioneanagrafe.ISDatiPersonaFisicaPro2P;
import it.tndigitale.ws.isvalidazioneanagrafe.ISPartitaIvaPro2P;
import it.tndigitale.ws.isvalidazioneanagrafe.RispostaRichiestaRispostaSincronaRicercaAnagraficaAll;
import it.tndigitale.ws.wssanagraficaimprese.DATIIMPRESA;
import it.tndigitale.ws.wssanagraficaimprese.ESTREMIIMPRESA;
import it.tndigitale.ws.wssanagraficaimprese.PERSONA;
import it.tndigitale.ws.wssanagraficaimprese.PERSONAFISICA;
import it.tndigitale.ws.wssanagraficaimprese.RISPOSTA;

public class PersonaFisicaMapper {

	private PersonaFisicaMapper() { }

	public static PersonaFisicaDto fromAnagrafeTributaria(RispostaRichiestaRispostaSincronaRicercaAnagraficaAll response) throws Exception {
		PersonaFisicaDto personaFisica = new PersonaFisicaDto();

		ISDatiPersonaFisicaPro2P persona = response.getRisposta().getPersona().getValue();
		personaFisica.setCodiceFiscale(persona.getPersonaFisica().getValue().getCodiceFiscale().getValue());
		personaFisica.setAnagrafica(AnagraficaMapper.from(persona.getPersonaFisica().getValue()));
		personaFisica.setDomicilioFiscale(IndirizzoMapper.from(persona.getPersonaFisica().getValue().getDomicilioFiscale().getValue().getUbicazione().getValue()));

		if (persona.getPersonaFisica().getValue().getDecesso() == null || persona.getPersonaFisica().getValue().getDecesso().isNil()) {
			personaFisica.setDeceduta(Boolean.FALSE);
		} else {
			personaFisica.setDeceduta(Boolean.TRUE);
			personaFisica.setDataMorte(DateFormatUtils.convertiLocalDate(persona.getPersonaFisica().getValue().getDecesso().getValue().getData()));
		}

		ISPartitaIvaPro2P partitaIva = persona.getDittaIndividuale().getValue().getPartitaIva().getValue();
		ImpresaIndividualeDto impresaIndividuale = null;
		if (partitaIva.getPiva() != null && partitaIva.getPiva().getValue() != null
				&& (partitaIva.getDate() == null || partitaIva.getDate().getValue() == null || partitaIva.getDate().getValue().getDataFine() == null)) {
			SedeDto sede = new SedeDto();
			/* ISUbicazionePro2P ubicazione = Optional.ofNullable(indirizzoAgenziaEntrate.getSede().getValue())
				.map(ISLuogoPro2P::getUbicazione).map(JAXBElement::getValue).orElse(null); */
			sede.setIndirizzo(IndirizzoMapper.from(persona.getDittaIndividuale().getValue().getSede().getValue().getUbicazione().getValue()));
			sede.setAttivitaAteco(AttivitaAtecoMapper.fromAgenziaEntrateV2(persona.getDittaIndividuale().getValue().getAttivita()));

			impresaIndividuale = new ImpresaIndividualeDto();
			impresaIndividuale.setPartitaIva(partitaIva.getPiva().getValue());
			impresaIndividuale.setDenominazione(persona.getDittaIndividuale().getValue().getDenominazione() != null ? persona.getDittaIndividuale().getValue().getDenominazione().getValue() : null);
			impresaIndividuale.setSedeLegale(sede);
		}

		personaFisica.setImpresaIndividuale(impresaIndividuale);

		return personaFisica;
	}

	// PARIX (Camera di Commercio)
	public static PersonaFisicaDto from(JAXBElement<RISPOSTA> dettaglioCompleto) throws Exception {
		if (dettaglioCompleto == null)
			return null;
		PersonaFisicaDto personaFisica = new PersonaFisicaDto();

		//Dati Identificativi
		DATIIMPRESA datiImpresa = dettaglioCompleto.getValue().getDATI().getDATIIMPRESA();
		ESTREMIIMPRESA estremiImpresa = datiImpresa.getESTREMIIMPRESA();
		String codiceFiscale = estremiImpresa.getCODICEFISCALE();
		personaFisica.setCodiceFiscale(codiceFiscale);

		//		Sezione Iscrizione
		List<IscrizioneSezioneDto> iscrizioniSezioneDto = IscrizioneSezioneMapper.from(datiImpresa);
		personaFisica.setIscrizioniSezione(iscrizioniSezioneDto);

		//		Sezione Impresa individuale
		ImpresaIndividualeDto impresaIndividualeDto = ImpresaIndividualeMapper.from(datiImpresa);
		personaFisica.setImpresaIndividuale(impresaIndividualeDto);

		//		verifica presenza persone in sede
		List<PERSONA> personaSedeList = datiImpresa.getPERSONESEDE().getPERSONA();
		if (CollectionUtils.isEmpty(personaSedeList)) {
			throw new Exception("La ditta individuale " + datiImpresa.getESTREMIIMPRESA().getCODICEFISCALE() +
					"per definizione deve avere persone associate alla sede");
		}

		PERSONAFISICA personafisica = datiImpresa.getPERSONESEDE().getPERSONA().get(0).getPERSONAFISICA();

		if (personaSedeList.size() > 1) {
			//			una persona fisica (titolare dell'impresa individuale) puo' avere persone con cariche (ad es. curatore fallimentare).
			//			Per cui il numero di persone in sede puo' anche essere maggiore di uno. Quindi cerco il titolare in base al codice fiscale passato.
			List<PERSONA> intestatario = personaSedeList.stream().filter(
					pf -> pf.getPERSONAFISICA().getCODICEFISCALE().equals(codiceFiscale)).collect(Collectors.toList());

			if (CollectionUtils.isEmpty(intestatario) || intestatario.size() > 1) {
				throw new Exception("Il codice fiscale della ditta individuale " + datiImpresa.getESTREMIIMPRESA().getCODICEFISCALE() +
						" non corrisponde al titolario:" + codiceFiscale);
			}
			personafisica = intestatario.get(0).getPERSONAFISICA();
		}

		if (!codiceFiscale.equals(personafisica.getCODICEFISCALE())) {
			throw new Exception("Dati inconsistenti, codice fiscale di PERSONE_SEDE non combacia con quello di ESTREMI_IMPRESA");
		}
		AnagraficaDto anagrafica = AnagraficaMapper.from(personafisica);
		personaFisica.setAnagrafica(anagrafica);
		personaFisica.setDomicilioFiscale(IndirizzoMapper.from(personafisica.getINDIRIZZO()));

		List<PERSONA> personeList = datiImpresa.getPERSONESEDE().getPERSONA();
		personaFisica.setPersoneFisicheConCarica(PersonaFisicaConCaricaMapper.from(personeList));
		personaFisica.setPersoneGiuridicheConCarica(PersonaGiuridicaConCaricaMapper.from(personeList));

		return personaFisica;
	}
}

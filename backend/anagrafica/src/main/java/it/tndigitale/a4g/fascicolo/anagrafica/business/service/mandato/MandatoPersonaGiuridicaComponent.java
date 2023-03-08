package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.*;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAnagraficiDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IndirizzoDto;
import it.tndigitale.a4g.proxy.client.model.PersonaGiuridicaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MandatoPersonaGiuridicaComponent extends MandatoAbstractComponent<PersonaGiuridicaModel> {

    private static final Logger logger = LoggerFactory.getLogger(MandatoPersonaGiuridicaComponent.class);

    @Autowired
    private DetenzioneService detenzioneService;

    private PersonaGiuridicaDto personaGiuridicaDto;

    @Override
    public List<FascicoloCreationAnomalyEnum> verificaAperturaMandato() throws MandatoVerificaException {
        // verifico in a4g presenza fascicolo
        // verifico in sian
        // chiamo anagrafe tributaria e creo il model
        var anomalies = new ArrayList<FascicoloCreationAnomalyEnum>();
        var personaGiuridicaDtoFromAT = getPersonaGiuridicaFromAnagrafeTributaria();
        esistePersona(personaGiuridicaDtoFromAT);
        MandatoValidatorComponent validator = getValidator();
        String codiceFiscaleDaCaa = getCodiceFiscaleDaCaa();
        String codiceFiscaleDaAnagrafeTributaria = personaGiuridicaDtoFromAT.getCodiceFiscale();
//      FAS-ANA-04-10
//		BR2
        validator.controlliMismatchAnagrafeTributariaECaa(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
        validator.controlloMismatchAnagrafeTributariaESian(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
//		BR3 e BR4
        FascicoloModel fascicoloModel = validator.verificaFascicoloLocaleEsistenzaEInStatoNonChiuso(codiceFiscaleDaCaa);
//		BR5
        verificaDetenzioneNonPresenteSuFascicolo(fascicoloModel);
//		BR6 noop dato che vale solo per persona fisica
//		BR7
        verificaUbicazioneProvinciaTrento(personaGiuridicaDtoFromAT);

//        TODO va aggiunta anche questa verifica?
//        anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaGiuridicaDtoFromAT));
        return anomalies;
    }

    @Override
    public DatiAperturaFascicoloDto getDatiPerAcquisizioneMandato() throws MandatoVerificaException {
        PersonaGiuridicaDto personaGiuridicaFromAnagrafeTributaria = getPersonaGiuridicaFromAnagrafeTributaria();
        if (personaGiuridicaFromAnagrafeTributaria == null) {
            return null;
        }
        MandatoValidatorComponent validator = getValidator();
        FascicoloModel fascicoloModel = validator.verificaFascicoloLocaleEsistenzaEInStatoNonChiuso(getCodiceFiscaleDaCaa());
        var datiApertura = new DatiAperturaFascicoloDto();
        datiApertura.setDatiAnagraficiRappresentante(new DatiAnagraficiDto());

        datiApertura.getDatiAnagraficiRappresentante()
                .setNominativo(personaGiuridicaFromAnagrafeTributaria.getRappresentanteLegale().getNominativo())
                .setCodiceFiscale(personaGiuridicaFromAnagrafeTributaria.getRappresentanteLegale().getCodiceFiscale());

        datiApertura.setCodiceFiscale(personaGiuridicaFromAnagrafeTributaria.getCodiceFiscale())
                .setPartitaIva(personaGiuridicaFromAnagrafeTributaria.getPartitaIva())
                .setDenominazione(personaGiuridicaFromAnagrafeTributaria.getDenominazione())
                .setNaturaGiuridica(personaGiuridicaFromAnagrafeTributaria.getFormaGiuridica())
                .setDenominazioneFascicolo(fascicoloModel.getDenominazione());

        datiApertura.setUbicazioneDitta(new IndirizzoDto());
        it.tndigitale.a4g.proxy.client.model.IndirizzoDto indirizzo =
                personaGiuridicaFromAnagrafeTributaria.getSedeLegale().getIndirizzo();
        datiApertura.getUbicazioneDitta()
                .setCap(indirizzo.getCap())
                .setComune(indirizzo.getComune())
                .setToponimo(indirizzo.getDenominazioneEstesa())
                .setLocalita(indirizzo.getFrazione())
                .setProvincia(indirizzo.getProvincia());
        return datiApertura;
    }

    private PersonaGiuridicaDto getPersonaGiuridicaFromAnagrafeTributaria() throws MandatoVerificaException {
        if (personaGiuridicaDto == null) {
            try {
                personaGiuridicaDto = getAnagraficaProxyClient().getPersonaGiuridicaAnagrafeTributaria(getCodiceFiscaleDaCaa());
            } catch (Exception e) {
                logger.error("Errore nell'interazione con proxy", e);
                throw new MandatoVerificaException(FascicoloValidazioneEnum.ANAGRAFE_TRIBUTARIA_NON_DISPONIBILE);
            }
        }
        return personaGiuridicaDto;
    }

//    private List<FascicoloCreationAnomalyEnum> verificaAnomaliaCodiceFiscaleATCameraDiCommercio(PersonaGiuridicaDto personaGiuridicaDtoFromAT){
//        var anomalies = new ArrayList<FascicoloCreationAnomalyEnum>();
//        var codiceFiscaleDaAnagrafeTributaria = personaGiuridicaDtoFromAT.getCodiceFiscale();
//        var personaGiuridicaDtoParix = getAnagraficaProxyClient().getPersonaGiuridicaAnagraficaImpresa(
//                codiceFiscaleDaAnagrafeTributaria,
//                personaGiuridicaDtoFromAT.getSedeLegale().getIndirizzo().getProvincia());
//        if (!codiceFiscaleDaAnagrafeTributaria.equals(getCodiceFiscaleDaCaa()) && personaGiuridicaDtoParix == null) {
//            personaGiuridicaDtoParix = getAnagraficaProxyClient().getPersonaGiuridicaAnagraficaImpresa(
//                    getCodiceFiscaleDaCaa(),
//                    personaGiuridicaDtoFromAT.getSedeLegale().getIndirizzo().getProvincia());
//            if (personaGiuridicaDtoParix != null &&
//                    !personaGiuridicaDtoParix.getSedeLegale().getIscrizioneRegistroImprese().isCessata()) {
//                anomalies.add(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAMERA_COMMERCIO);
//            }
//        }
//        return anomalies;
//    }

    // verifica l'esistenza della persona
    private void esistePersona(final PersonaGiuridicaDto personaGiuridicaDto) throws MandatoVerificaException {
        logger.debug("Start - esistePersonaGiuridica");
        if (personaGiuridicaDto == null) {
            throw new MandatoVerificaException(FascicoloValidazioneEnum.CUAA_NON_PRESENTE);
        }
    }

    // Verifica che la residenza del titolare dell’azienda individuale (persona fisica) oppure
    // la sede legale della società (persona giuridica) sia in provincia di Trento.
    private void verificaUbicazioneProvinciaTrento(final PersonaGiuridicaDto personaGiuridicaDto) throws MandatoVerificaException {
        logger.debug("Start - verificaUbicazioneProvinciaTrento {}", personaGiuridicaDto.getCodiceFiscale());
        if (personaGiuridicaDto.getSedeLegale().getIndirizzo().getProvincia() == null ||
                !personaGiuridicaDto.getSedeLegale().getIndirizzo().getProvincia().equals(MandatoValidatorComponent.PROV_TRENTO))
            throw new MandatoVerificaException(FascicoloValidazioneEnum.PROVINCIA_DIVERSA_DA_TRENTO);
    }
}


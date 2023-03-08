package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloCreationAnomalyEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloOperationEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloValidazioneEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloValidazioneException;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAnagraficiDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IndirizzoDto;
import it.tndigitale.a4g.proxy.client.model.AnagraficaDto;
import it.tndigitale.a4g.proxy.client.model.ImpresaIndividualeDto;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.client.model.SedeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MandatoPersonaFisicaComponent extends MandatoAbstractComponent<PersonaFisicaModel> {

    private static final Logger logger = LoggerFactory.getLogger(MandatoPersonaFisicaComponent.class);

    private PersonaFisicaDto personaFisicaDto;

    @Override
    public List<FascicoloCreationAnomalyEnum> verificaAperturaMandato() throws MandatoVerificaException {
        // verifico in a4g presenza fascicolo
        // verifico in sian
        // chiamo anagrafe tributaria e creo il model
        var anomalies = new ArrayList<FascicoloCreationAnomalyEnum>();
        var personaFisicaDtoFromAT = getPersonaFisicaFromAnagrafeTributaria();
        esistePersona(personaFisicaDtoFromAT);
        MandatoValidatorComponent validator = getValidator();
        String codiceFiscaleDaCaa = getCodiceFiscaleDaCaa();
        String codiceFiscaleDaAnagrafeTributaria = personaFisicaDtoFromAT.getCodiceFiscale();
//      FAS-ANA-04-10
//		BR2
        validator.controlliMismatchAnagrafeTributariaECaa(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
        validator.controlloMismatchAnagrafeTributariaESian(anomalies, codiceFiscaleDaAnagrafeTributaria, codiceFiscaleDaCaa);
//		BR3 e BR4
        FascicoloModel fascicoloModel = validator.verificaFascicoloLocaleEsistenzaEInStatoNonChiuso(codiceFiscaleDaCaa);
//		BR5
        verificaDetenzioneNonPresenteSuFascicolo(fascicoloModel);
//		BR6
        verificaPersonaNonDeceduta(personaFisicaDto);
//		BR7
        verificaUbicazioneProvinciaTrento(personaFisicaDtoFromAT);
//        TODO va aggiunta anche questa verifica?
//        anomalies.addAll(verificaAnomaliaCodiceFiscaleATCameraDiCommercio(personaFisicaDtoFromAT));
        return anomalies;
    }

    private PersonaFisicaDto getPersonaFisicaFromAnagrafeTributaria() throws MandatoVerificaException {
        if (personaFisicaDto == null) {
            try {
                personaFisicaDto = getAnagraficaProxyClient().getPersonaFisicaAnagrafeTributaria(this.getCodiceFiscaleDaCaa());
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new MandatoVerificaException(FascicoloValidazioneEnum.ANAGRAFE_TRIBUTARIA_NON_DISPONIBILE);
            }
        }
        return personaFisicaDto;
    }

//    /**
//     * Le verifiche fatte ad oggi sono le seguenti:
//     * - verifico che in camera di commercio sia stato aggiornato il codice fiscale in caso di cambio segnalato da Anagrafe tributaria
//     *
//     * @param personaFisicaDtoFromAT i dati di anagrafe tributaria
//     * @return elenco di anomalie
//     */
//    private List<FascicoloCreationAnomalyEnum> verificaAnomaliaCodiceFiscaleATCameraDiCommercio(
//            final PersonaFisicaDto personaFisicaDtoFromAT){
//        var anomalies = new ArrayList<FascicoloCreationAnomalyEnum>();
//        var codiceFiscaleDaAnagrafeTributaria = personaFisicaDtoFromAT.getCodiceFiscale();
//
//        String provinciaDaAnagrafeTributaria = null;
//        ImpresaIndividualeDto impresaIndividuale = personaFisicaDtoFromAT.getImpresaIndividuale();
//        if (impresaIndividuale != null) {
//            provinciaDaAnagrafeTributaria = impresaIndividuale.getSedeLegale().getIndirizzo().getProvincia();
//        } else {
//            provinciaDaAnagrafeTributaria = personaFisicaDtoFromAT.getDomicilioFiscale().getProvincia();
//        }
//
//        // Chiama Camera di commercio (PARIX)
//        var personaFisicaDtoParix = getAnagraficaProxyClient().getPersonaFisicaAnagraficaImpresa(
//                codiceFiscaleDaAnagrafeTributaria,
//                provinciaDaAnagrafeTributaria);
//
//        /**
//         * Nel caso di cambio di codice fiscale possono esserci due scenari se PARIX non restituisce dati:
//         * - nessuna iscrizione in camera di commercio
//         * - in camera di commercio c'e' il vecchio codice fiscale
//         * Per evitare dubbi faccio la chiamata anche col codice fiscale vecchio.
//         */
//        if (!codiceFiscaleDaAnagrafeTributaria.equals(getCodiceFiscaleDaCaa()) && personaFisicaDtoParix == null) {
//            personaFisicaDtoParix = getAnagraficaProxyClient().getPersonaFisicaAnagraficaImpresa(
//                    getCodiceFiscaleDaCaa(), provinciaDaAnagrafeTributaria);
//            if (personaFisicaDtoParix != null &&
//                    !personaFisicaDtoParix.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().isCessata()) {
//                anomalies.add(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAMERA_COMMERCIO);
//            }
//        }
//        return anomalies;
//    }

    // verifica l'esistenza della persona
    private void esistePersona(PersonaFisicaDto personaFisicaDto) throws MandatoVerificaException {
        logger.debug("Start - esistePersonaFisica");
        if (personaFisicaDto == null) {
            throw new MandatoVerificaException(FascicoloValidazioneEnum.CUAA_NON_PRESENTE);
        }
    }

    // Verifica che la persona sia presente in anagrafe tributaria e non sia deceduta
    private void verificaPersonaNonDeceduta(final PersonaFisicaDto personaFisicaDto) throws MandatoVerificaException {
        logger.debug("Start - personaFisicaDeceduta {}", personaFisicaDto.getCodiceFiscale());
        if (personaFisicaDto.isDeceduta().booleanValue()) {
            throw new MandatoVerificaException(FascicoloValidazioneEnum.DECEDUTO);
        }
    }

    // Verifica che la residenza del titolare dell’azienda individuale (persona fisica) oppure
    // la residenza della persona fisica sia in provincia di Trento.
    private void verificaUbicazioneProvinciaTrento(final PersonaFisicaDto personaFisicaDto) throws MandatoVerificaException {
        logger.debug("Start - verificaUbicazioneProvinciaTrento {}", personaFisicaDto.getCodiceFiscale());
        if (personaFisicaDto.getImpresaIndividuale() != null && (personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIndirizzo().getProvincia() == null
                || !personaFisicaDto.getImpresaIndividuale().getSedeLegale().getIndirizzo().getProvincia().equals(MandatoValidatorComponent.PROV_TRENTO)))
            throw new MandatoVerificaException(FascicoloValidazioneEnum.PROVINCIA_DIVERSA_DA_TRENTO);
        if (personaFisicaDto.getImpresaIndividuale() == null
                && (personaFisicaDto.getDomicilioFiscale().getProvincia() == null
                || !personaFisicaDto.getDomicilioFiscale().getProvincia().equals(MandatoValidatorComponent.PROV_TRENTO)))
            throw new MandatoVerificaException(FascicoloValidazioneEnum.PROVINCIA_DIVERSA_DA_TRENTO);
    }

    @Override
    public DatiAperturaFascicoloDto getDatiPerAcquisizioneMandato() throws MandatoVerificaException {
        PersonaFisicaDto personaFisicaFromAnagrafeTributaria = getPersonaFisicaFromAnagrafeTributaria();
        if (personaFisicaFromAnagrafeTributaria == null) {
            return null;
        }
        MandatoValidatorComponent validator = getValidator();
        FascicoloModel fascicoloModel = validator.verificaFascicoloLocaleEsistenzaEInStatoNonChiuso(getCodiceFiscaleDaCaa());

        var datiApertura = new DatiAperturaFascicoloDto();
        datiApertura.setDatiAnagraficiRappresentante(new DatiAnagraficiDto());

        AnagraficaDto anagrafica = personaFisicaFromAnagrafeTributaria.getAnagrafica();
        datiApertura.getDatiAnagraficiRappresentante()
                .setNominativo(anagrafica.getCognome() + " " + anagrafica.getNome())
                .setCodiceFiscale(personaFisicaFromAnagrafeTributaria.getCodiceFiscale())
                .setComuneNascita(anagrafica.getComuneNascita())
                .setProvinciaNascita(anagrafica.getProvinciaNascita())
                .setDataNascita(anagrafica.getDataNascita());
        datiApertura.setDenominazioneFascicolo(fascicoloModel.getDenominazione());
        datiApertura.setDomicilioFiscaleRappresentante(new IndirizzoDto());
        datiApertura.getDomicilioFiscaleRappresentante()
                .setCap(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getCap())
                .setComune(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getComune())
                .setToponimo(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getDenominazioneEstesa())
                .setLocalita(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getFrazione())
                .setProvincia(personaFisicaFromAnagrafeTributaria.getDomicilioFiscale().getProvincia());
        if (personaFisicaFromAnagrafeTributaria.getImpresaIndividuale() != null) {
            ImpresaIndividualeDto impresa = personaFisicaFromAnagrafeTributaria.getImpresaIndividuale();
            datiApertura.setCodiceFiscale(personaFisicaFromAnagrafeTributaria.getCodiceFiscale())
                    .setPartitaIva(impresa.getPartitaIva()).setDenominazione(impresa.getDenominazione())
                    .setNaturaGiuridica(impresa.getFormaGiuridica() != null ? impresa.getFormaGiuridica() : "Impresa individuale");
            datiApertura.setUbicazioneDitta(new IndirizzoDto());
            SedeDto sede = impresa.getSedeLegale();
            datiApertura.getUbicazioneDitta()
                    .setCap(sede.getIndirizzo().getCap())
                    .setComune(sede.getIndirizzo().getComune())
                    .setToponimo(sede.getIndirizzo().getDenominazioneEstesa())
                    .setLocalita(sede.getIndirizzo().getFrazione()).setProvincia(sede.getIndirizzo().getProvincia());
        }
        return datiApertura;
    }

}


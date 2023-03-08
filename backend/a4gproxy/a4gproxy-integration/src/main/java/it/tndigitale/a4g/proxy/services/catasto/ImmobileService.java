package it.tndigitale.a4g.proxy.services.catasto;

import it.tndigitale.a4g.proxy.config.WSKSTSSupport;
import it.tndigitale.a4g.proxy.dto.catasto.*;
import it.tndigitale.a4g.proxy.ws.catasto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ImmobileService extends WSKSTSSupport {

    private static final Logger log = LoggerFactory.getLogger(ImmobileService.class);

    @Value("${it.tndigit.catasto.immobile.uri}")
    private String wsUri;

    @Value("${it.tndigit.catasto.password}")
    private String wsAuthPsw;

    @Value("${it.tndigit.catasto.user}")
    private String wsAuthUsn;

    it.tndigitale.a4g.proxy.ws.catasto.ObjectFactory factory = new it.tndigitale.a4g.proxy.ws.catasto.ObjectFactory();

    @PostConstruct
    private void buildWebTemplate() throws Exception {
        super.buildWebTemplate("it.tndigitale.a4g.proxy.ws.catasto", wsUri, wsAuthUsn, wsAuthPsw);
    }

    private UnitaImmobiliareType getInfoUnitaFromWs(String numeroParticella, Integer codiceComuneCatastale, BigInteger subalterno) {

        ChiaveComuneCatastaleType chiaveComuneCatastaleType = new ChiaveComuneCatastaleType();
        chiaveComuneCatastaleType.setCodice(BigInteger.valueOf(codiceComuneCatastale));

        ChiaveParticellaType chiaveParticellaType = new ChiaveParticellaType();
        chiaveParticellaType.setNumero(numeroParticella);
        chiaveParticellaType.setComuneCatastale(chiaveComuneCatastaleType);
//        chiaveParticellaType.setTipologia(TipoParticellaType.fromValue(tipologia.name()));

        ChiaveUnitaImmobiliareType chiaveUnitaImmobiliareType = new ChiaveUnitaImmobiliareType();
        chiaveUnitaImmobiliareType.setParticellaEdificiale(chiaveParticellaType);
        chiaveUnitaImmobiliareType.setSubalterno(subalterno);

        GetImmobile createGetImmobile = factory.createGetImmobile();
        createGetImmobile.setChiaveUI(chiaveUnitaImmobiliareType);
        GetImmobileResponse marshalSendAndReceive = (GetImmobileResponse) getWebServiceTemplate().marshalSendAndReceive(wsUri, createGetImmobile);
//        ritorna NULL se non esiste
        if (marshalSendAndReceive.getImmobile() != null) {
            log.debug("chiamata a get info particella {}" , marshalSendAndReceive.getImmobile().toString());
        }

		return marshalSendAndReceive.getImmobile();
    }

    public InformazioniImmobileDto getInfoImmobile(String numeroParticella, Integer codiceComuneCatastale, BigInteger subalterno) {
        try {
                UnitaImmobiliareType unitaImmobiliareType = getInfoUnitaFromWs(numeroParticella, codiceComuneCatastale, subalterno);
                if (unitaImmobiliareType != null) {
                    InformazioniImmobileDto informazioniImmobileDto = new InformazioniImmobileDto();
    //                TODO valorizzare
//                    List<InformazioniImmobileDettaglioDto> immobileDettaglioDtoList = new ArrayList<>();
//                    unitaImmobiliareType.getDettaglioChiave().forEach(dettaglioChiaveUnitaImmobiliareType -> {
//                        InformazioniImmobileDettaglioDto informazioniImmobileDettaglioDto = new InformazioniImmobileDettaglioDto();
//                        informazioniImmobileDettaglioDto.setComuneCatastale(dettaglioChiaveUnitaImmobiliareType.getChiaveUI().getParticellaEdificiale().getComuneCatastale().getCodice());
//                        informazioniImmobileDettaglioDto.setSubalterno(String.valueOf(dettaglioChiaveUnitaImmobiliareType.getChiaveUI().getSubalterno()));
//                        informazioniImmobileDettaglioDto.setParticella(dettaglioChiaveUnitaImmobiliareType.getChiaveUI().getParticellaEdificiale().getNumero());
//                        informazioniImmobileDettaglioDto.setTipologia(TipologiaParticellaCatastale.valueOf(dettaglioChiaveUnitaImmobiliareType.getChiaveUI().getParticellaEdificiale().getTipologia().value()));
//                        List<PorzioneMaterialeDto> porzioniMateriali = new ArrayList<>();
//                        dettaglioChiaveUnitaImmobiliareType.getPorzioneMateriale().forEach(chiavePorzioneMaterialeType -> {
//                            PorzioneMaterialeDto porzioneMaterialeDto = new PorzioneMaterialeDto();
//                            porzioneMaterialeDto.setComuneCatastale(chiavePorzioneMaterialeType.getParticellaEdificiale().getComuneCatastale().getCodice());
//                            porzioneMaterialeDto.setParticella(chiavePorzioneMaterialeType.getParticellaEdificiale().getNumero());
//                            porzioneMaterialeDto.setStato(EntitaCatastaleStato.valueOf(chiavePorzioneMaterialeType.getStato().value()));
//                            porzioneMaterialeDto.setNumeroPorzione(chiavePorzioneMaterialeType.getNumero());
//                            porzioneMaterialeDto.setTipologia(TipologiaParticellaCatastale.valueOf(chiavePorzioneMaterialeType.getParticellaEdificiale().getTipologia().value()));
//                            porzioniMateriali.add(porzioneMaterialeDto);
//
//                        });
//                        informazioniImmobileDettaglioDto.setPorzioniMateriali(porzioniMateriali);
//                        immobileDettaglioDtoList.add(informazioniImmobileDettaglioDto);
//                    });
//                    informazioniImmobileDto.setDettaglio(immobileDettaglioDtoList);

                    if (unitaImmobiliareType.getDatiClassamento() != null) {
                        DatiClassamentoDto datiClassamentoDto = new DatiClassamentoDto();
                        datiClassamentoDto.setClasse(unitaImmobiliareType.getDatiClassamento().getClasse());
                        datiClassamentoDto.setConsistenza(unitaImmobiliareType.getDatiClassamento().getConsistenza());
                        datiClassamentoDto.setCategoria(TipologiaCategoria.fromValue(unitaImmobiliareType.getDatiClassamento().getCategoria().value()));
                        informazioniImmobileDto.setDatiClassamento(datiClassamentoDto);
                    }
//                    informazioniImmobileDto.setRendita(unitaImmobiliareType.getRendita());
                    informazioniImmobileDto.setIndirizzo(unitaImmobiliareType.getIndirizzo());
//                    informazioniImmobileDto.setPiani(unitaImmobiliareType.getPiani());

                    return  informazioniImmobileDto;
                } else {
                    return null;
                }
        } catch (Exception e) {
            log.error("Errore nel reperimento dei dati da catasto", e);
            return null;
        }
    }

    @Override
    protected String getAlias() {
        return null;
    }
}

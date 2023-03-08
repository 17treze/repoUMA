package it.tndigitale.a4g.ags.service;

import static it.tndigitale.a4g.ags.dto.AnagraficaAzienda.TIPO_AZIENDA_PERSONA_FISICA;
import static it.tndigitale.a4g.ags.dto.AnagraficaAzienda.TIPO_AZIENDA_PERSONA_GIURIDICA;
import static it.tndigitale.a4g.ags.dto.AnagraficaAzienda.TIPO_AZIENDA_SOCIETA_SEMPLICE;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.ags.dto.AnagraficaAzienda;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaBaseData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaDocumentData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaEnteData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaFascicoloData;
import it.tndigitale.a4g.ags.repository.dao.FascicoloAziendaleDao;
import it.tndigitale.a4g.ags.repository.dao.exceptions.CUAANotFoundException;
import it.tndigitale.a4g.ags.repository.dao.exceptions.MultipleMatchesForCUAAException;

@Service
public class FascicoloAziendaleServiceImpl implements FascicoloAziendaleService {

    private final FascicoloAziendaleDao fascicoloAziendaleDao;

    @Autowired
    public FascicoloAziendaleServiceImpl(FascicoloAziendaleDao fascicoloAziendaleDao) {
        this.fascicoloAziendaleDao = fascicoloAziendaleDao;
    }

    @Override
    public AnagraficaAzienda findAnagraficaAziendaByCuaa(String cuaa) {

        try {
            long pkCuaa = fascicoloAziendaleDao.findPkCuaaByCuaa(cuaa);

            Map<Integer, Integer> docTypeMapping = fascicoloAziendaleDao.loadDocumentTypeMapping();

            AnagraficaAziendaBaseData baseData = fascicoloAziendaleDao.loadAnagraficaBaseData(pkCuaa);
            AnagraficaAziendaDocumentData documentData = fascicoloAziendaleDao.loadDocumentData(pkCuaa);
            AnagraficaAziendaEnteData enteData = fascicoloAziendaleDao.loadEnteData(pkCuaa);
            AnagraficaAziendaFascicoloData fascicoloData = fascicoloAziendaleDao.loadFascicoloData(pkCuaa);

            return buildDto(cuaa, baseData, documentData, enteData, fascicoloData, docTypeMapping);
        } catch (EmptyResultDataAccessException erdaex) {
            throw new CUAANotFoundException("Could not find AnagraficaAzienda with CUAA " + cuaa, erdaex);
        } catch (IncorrectResultSizeDataAccessException irsdaex) {
            throw new MultipleMatchesForCUAAException("Found more than one result of AnagraficaAzienda with CUAA " + cuaa, irsdaex);
        }
    }

    private AnagraficaAzienda buildDto(String cuaa,
                                       AnagraficaAziendaBaseData baseData,
                                       AnagraficaAziendaDocumentData documentData,
                                       AnagraficaAziendaEnteData enteData,
                                       AnagraficaAziendaFascicoloData fascicoloData,
                                       Map<Integer, Integer> docTypeMapping) {
        AnagraficaAzienda dto = new AnagraficaAzienda(cuaa);

        final String tipoAzienda = mapTipoAzienda(baseData.getNaturaGiuridica());

        dto.setTipoAzienda(tipoAzienda);

        // TODO: Not sure about TIPO_AZIENDA_DITTA_INDIVIDUALE
        if (TIPO_AZIENDA_PERSONA_FISICA.equals(tipoAzienda)) {
            dto.setNomePf(baseData.getNome());
            dto.setDenominazione(baseData.getDenominazione());
            dto.setSessoPf(baseData.getSessoPf());
            dto.setDataNascitaPf(toLocalDateTime(baseData.getDataNascita()));
            dto.setComuneNascitaPf(baseData.getComuneNascita());
        } else if (TIPO_AZIENDA_SOCIETA_SEMPLICE.equals(tipoAzienda)) {
            dto.setDenominazione(baseData.getDenominazione());
        } else if (TIPO_AZIENDA_PERSONA_GIURIDICA.equals(tipoAzienda)) {
            dto.setDenominazione(baseData.getDenominazione());
        } else {
            throw new IllegalArgumentException("Unhandled value for tipoAzienda: " + tipoAzienda);
        }

        Integer tipoDoc = docTypeMapping.getOrDefault(documentData.getTipoDocumento(), -1);
        dto.setTipoDocumento(String.valueOf(tipoDoc));
        dto.setNumeroDocumento(documentData.getNumeroDocumento());
        dto.setDataDocumento(toLocalDateTime(documentData.getDataDocumento()));
        dto.setDataScadDocumento(toLocalDateTime(documentData.getDataScadDocumento()));

        dto.setProvinciaResidenza(baseData.getProvinciaResidenza());
        dto.setComuneResidenza(baseData.getComuneResidenza());
        dto.setIndirizzoResidenza(baseData.getIndirizzoResidenza());
        dto.setCapResidenza(baseData.getCapResidenza());
        //dto.setCodiceStatoEsteroResidenza();  TODO
        dto.setProvinciaRecapito(baseData.getProvinciaRecapito());
        dto.setComuneRecapito(baseData.getComuneRecapito());
        dto.setIndirizzoRecapito(baseData.getIndirizzoRecapito());
        dto.setCapRecapito(baseData.getCapRecapito());
        // dto.setCodiceStatoEsteroRecapito();  TODO
        dto.setPartitaIva(baseData.getPartitaIva());
        dto.setIscrizioneRea(baseData.getIscrizioneRea());
        // dto.setIscrizioneRegistroImprese(); TODO
        // dto.setCodiceInps(); TODO
        dto.setOrganismoPagatore(baseData.getOrganismoPagatore());
        dto.setDataAperturaFascicolo(toLocalDateTime(baseData.getDataAperturaFascicolo()));
        dto.setDataChiusuraFascicolo(toLocalDateTime(baseData.getDataChiusuraFascicolo()));
        dto.setTipoDetentore(mapTipoDetentore(enteData.getTipoDetentore()));
        dto.setDetentore(enteData.getDetentore());
        dto.setPec(baseData.getPec());
        dto.setAttivita(baseData.getAttivita());
        dto.setDataValidazFascicolo(toLocalDateTime(fascicoloData.getDataValidazFascicolo()));
        // dto.setSchedaValidazione(); TODO
        // dto.setDataSchedaValidazione(); TODO
        dto.setDataSottMandato(toLocalDateTime(enteData.getDataSottMandato()));
        // dto.setDataElaborazione(); TODO

        return dto;
    }

    private String mapTipoDetentore(String desEnte) {
        if (!StringUtils.isEmpty(desEnte) && "CAA".equals(desEnte.substring(0, 3))) {
            return "001";
        }

        return "002";
    }

    private String mapTipoAzienda(String naturaGiuridica) {
        switch (naturaGiuridica) {
            case "900": return TIPO_AZIENDA_PERSONA_FISICA;
            case "901": return TIPO_AZIENDA_PERSONA_GIURIDICA;

            // TODO: Not sure about these 2
            case "2": return TIPO_AZIENDA_PERSONA_FISICA;
            case "13": return TIPO_AZIENDA_PERSONA_FISICA;

            // TODO: Infered from an example JSON
            case "24": return TIPO_AZIENDA_PERSONA_GIURIDICA;
            case "35": return TIPO_AZIENDA_SOCIETA_SEMPLICE;
        }
        throw new IllegalArgumentException("Unhandled value for naturaGiuridica: " + naturaGiuridica);
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date == null ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private String blankString(String s) {
        return StringUtils.isEmpty(s) ? "" : s;
    }

}

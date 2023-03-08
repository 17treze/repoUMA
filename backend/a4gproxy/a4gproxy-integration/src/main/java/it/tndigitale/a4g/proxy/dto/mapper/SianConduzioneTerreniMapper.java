package it.tndigitale.a4g.proxy.dto.mapper;

import it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno.*;
import it.tndigitale.a4g.proxy.ws.siap.ISWSProprietario;
import it.tndigitale.a4g.proxy.ws.siap.ISWSTerritorioFS6;
import it.tndigitale.a4g.proxy.ws.siap.ISWSToOprResponse;
import it.tndigitale.a4g.proxy.ws.siap.WsDocumenti;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SianConduzioneTerreniMapper {

	public static List<ConduzioneDto> from(String cuaa, ISWSToOprResponse response) {
		if (response == null || response.getRisposta29() == null) return new ArrayList<>();
		if (!response.getISWSResponse().getCodRet().equals(CodiciDiRitornoEnum.OPERAZIONE_OK.getCodice())) return new ArrayList<>();

		var resultFromWS = response.getRisposta29();
		var conduzioneDtoList = new ArrayList<ConduzioneDto>();
		for (ISWSTerritorioFS6 territorioFS6: resultFromWS) {
			var conduzioneDto = new ConduzioneDto();

			conduzioneDto.setDataInizioConduzione(territorioFS6.getDataInizioConduzione());
			conduzioneDto.setDataFineConduzione(territorioFS6.getDataFineConduzione());
			conduzioneDto.setSuperficieCondotta(territorioFS6.getSuperficieCondotta());
			if (!StringUtils.isBlank(territorioFS6.getCodiceTipoConduzione())) {
				conduzioneDto.setCodiceTipoConduzione(territorioFS6.getCodiceTipoConduzione());
				conduzioneDto.setDescrizioneTipoConduzione(CodiceTipoConduzioneEnum.fromCodTipoConduzione(territorioFS6.getCodiceTipoConduzione()).getCodDescrizione());
			}
			var codiceFiscaleProprietarioList = new ArrayList<String>();
			if (territorioFS6.getProprietari() != null && !territorioFS6.getProprietari().isEmpty()) {
				for (ISWSProprietario codiceFiscaleProprietarioFromWS:  territorioFS6.getProprietari()) {
					codiceFiscaleProprietarioList.add(codiceFiscaleProprietarioFromWS.getProprietario());
				}
			}
			conduzioneDto.setCodiceFiscaleProprietarioList(codiceFiscaleProprietarioList);

			var particellaDto = new ParticellaDto();
			particellaDto.setProvincia(territorioFS6.getProvincia());
			particellaDto.setComune(territorioFS6.getComune());
			particellaDto.setSezione(territorioFS6.getSezione());
			particellaDto.setFoglio(territorioFS6.getFoglio());
			particellaDto.setParticella(territorioFS6.getParticella());
			particellaDto.setSubalterno(territorioFS6.getSubalterno());
			conduzioneDto.setDatiParticella(particellaDto);
			conduzioneDtoList.add(conduzioneDto);

			var caratteristicheZonaDto = new CaratteristicheZonaDto();

			if (!StringUtils.isBlank(territorioFS6.getCasiParticolari())) {
				caratteristicheZonaDto.setCasiParticolari(territorioFS6.getCasiParticolari());
				caratteristicheZonaDto.setCasiParticolariDescrizione(CasiParticolariEnum.fromCodice(territorioFS6.getCasiParticolari()).getDescrizione());
			}

			if (!StringUtils.isBlank(territorioFS6.getFlagGiust())) {
				caratteristicheZonaDto.setFlagGiust(territorioFS6.getFlagGiust());
				caratteristicheZonaDto.setFlagGiustDescrizione(FlagGiustEnum.fromCodice(territorioFS6.getFlagGiust()).getDescrizione());
			}
//			codice zvn
			if (territorioFS6.getCodiZVN() != null) {
				caratteristicheZonaDto.setCodiZVN(territorioFS6.getCodiZVN().getValue());
				caratteristicheZonaDto.setCodiZVNDescrizione(CodiZVNEnum.fromCodice(territorioFS6.getCodiZVN().getValue()).getDescrizione());
			}
			conduzioneDto.setCaratteristicheZona(caratteristicheZonaDto);

			if (territorioFS6.getDocumento()!= null && !territorioFS6.getDocumento().isEmpty()) {
				var documentiAggiuntivi = new ArrayList<DocumentoConduzioneDto>();
				for (WsDocumenti documentoFromWS: territorioFS6.getDocumento()) {
					var documentoDto = new DocumentoConduzioneDto();
//					TODO per il momento viene mostrato un codice numerico per tipodocumento
					if (documentoFromWS.getTipoDocumento() != null) {
						documentoDto.setTipoDocumento(documentoFromWS.getTipoDocumento().getValue());
					}

					documentoDto.setNumeroDocumento(documentoFromWS.getNumeroDocumento());
					documentoDto.setDataRilascio(documentoFromWS.getDataRilascio());
					documentoDto.setDataScadenza(documentoFromWS.getDataScadenza());
					documentiAggiuntivi.add(documentoDto);
				}
				conduzioneDto.setDocumentiConduzione(documentiAggiuntivi);
			}
		}
		return conduzioneDtoList;
	}

}

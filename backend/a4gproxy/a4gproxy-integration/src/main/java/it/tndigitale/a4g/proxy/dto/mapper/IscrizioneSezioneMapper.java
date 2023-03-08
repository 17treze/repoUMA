package it.tndigitale.a4g.proxy.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import it.tndigitale.a4g.proxy.dto.persona.IscrizioneSezioneDto;
import it.tndigitale.a4g.proxy.dto.persona.SezioneEnum;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import it.tndigitale.ws.wssanagraficaimprese.DATIIMPRESA;
import it.tndigitale.ws.wssanagraficaimprese.SEZIONE;

public class IscrizioneSezioneMapper {
	
	private IscrizioneSezioneMapper() {}
	
	static List<IscrizioneSezioneDto> from(DATIIMPRESA datiImpresa) throws MapperException {
		List<IscrizioneSezioneDto> iscrizioniSezioneDto = new ArrayList<>();
		if (datiImpresa != null && datiImpresa.getESTREMIIMPRESA() != null
				&& datiImpresa.getESTREMIIMPRESA().getDATIISCRIZIONERI() != null
				&& datiImpresa.getESTREMIIMPRESA().getDATIISCRIZIONERI().getSEZIONI() != null
				&& !datiImpresa.getESTREMIIMPRESA().getDATIISCRIZIONERI().getSEZIONI().getSEZIONE().isEmpty()) {
			List<SEZIONE> sezioni = datiImpresa.getESTREMIIMPRESA().getDATIISCRIZIONERI().getSEZIONI().getSEZIONE();
			for (SEZIONE sezione : sezioni) {
				IscrizioneSezioneDto iscrizioneSezioneDto = new IscrizioneSezioneDto();
				iscrizioneSezioneDto.setDataIscrizione(DateFormatUtils.convertiDataParixLocalDate(sezione.getDTISCRIZIONE()));
				if (sezione.getCSEZIONE().equals("O")) {
					iscrizioneSezioneDto.setSezione(SezioneEnum.ORDINARIA);
				} else if (sezione.getCSEZIONE().matches("A|G|P|S")) {
					iscrizioneSezioneDto.setSezione(SezioneEnum.SPECIALE);
					iscrizioneSezioneDto.setQualifica(sezione.getDESCRIZIONE());
					iscrizioneSezioneDto.setColtivatoreDiretto(sezione.getCOLTDIRETTO());
				} else {
					throw new MapperException("Caso CSEZIONE non gestito");
				}
				iscrizioniSezioneDto.add(iscrizioneSezioneDto);
			}
		}
		return iscrizioniSezioneDto;
	}
}

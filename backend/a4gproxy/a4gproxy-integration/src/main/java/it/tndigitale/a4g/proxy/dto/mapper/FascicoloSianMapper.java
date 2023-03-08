package it.tndigitale.a4g.proxy.dto.mapper;

import it.tndigitale.a4g.proxy.dto.fascicolo.sian.FascicoloSian;
import it.tndigitale.a4g.proxy.dto.fascicolo.sian.OrganismoPagatore;
import it.tndigitale.a4g.proxy.dto.fascicolo.sian.TipoDetentore;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import it.tndigitale.a4g.proxy.ws.siap.ISWSToOprResponse;

public class FascicoloSianMapper {
	
	public static final String OP_APPAG = "IT25";

	public static FascicoloSian from(ISWSToOprResponse response) {
		if (response==null || response.getRisposta20()==null) return null;
		FascicoloSian fascicolo = new FascicoloSian();
		fascicolo.setCuaa(response.getRisposta20().getValue().getCUAA());
		fascicolo.setDataAperturaFascicolo(
				DateFormatUtils.convertiDataParixLocalDateTime(response.getRisposta20().getValue().getDataAperturaFascicolo()));
		fascicolo.setDataChiusuraFascicolo(
				DateFormatUtils.convertiDataParixLocalDateTime(response.getRisposta20().getValue().getDataChiusuraFascicolo()));
		fascicolo.setDataValidazFascicolo(
				DateFormatUtils.convertiDataParixLocalDateTime(response.getRisposta20().getValue().getDataValidazFascicolo()));
		fascicolo.setTipoDetentore(TipoDetentore.fromCodStato(response.getRisposta20().getValue().getTipoDetentore()));
		fascicolo.setDetentore(response.getRisposta20().getValue().getDetentore());
		fascicolo.setOrganismoPagatore(
				OP_APPAG.equals(response.getRisposta20().getValue().getOrganismoPagatore()) ? 
						OrganismoPagatore.APPAG : OrganismoPagatore.ALTRO
				);
		return fascicolo;
	}

}

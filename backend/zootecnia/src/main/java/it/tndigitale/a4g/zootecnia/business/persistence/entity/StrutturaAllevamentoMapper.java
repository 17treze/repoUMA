package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import it.tndigitale.a4g.proxy.client.model.StrutturaAllevamentoDto;

public class StrutturaAllevamentoMapper {

	
	public static StrutturaAllevamentoModel from(StrutturaAllevamentoDto dto, String cuaa){
		if(dto == null) {
			return null;
		}
		StrutturaAllevamentoModel model = new StrutturaAllevamentoModel();
		
		model.setCap(dto.getCap());
		model.setComune(dto.getComune());
		model.setFoglio(dto.getFoglioCatastale());
		model.setIdentificativo(dto.getIdentificativo());
		model.setIndirizzo(dto.getIndirizzo());
		model.setLatitudine(dto.getLatitudine().toPlainString());
		model.setLocalita(dto.getLocalita());
		model.setLongitudine(dto.getLongitudine().toPlainString());
		model.setParticella(dto.getParticella());
		model.setSezione(dto.getSezione());
		model.setSubalterno(dto.getSubalterno());
		model.setIdValidazione(0);
		model.setCuaa(cuaa);
		return model;
	}
	
	
	public static it.tndigitale.a4g.zootecnia.dto.StrutturaAllevamentoDto from(AllevamentoModel input){
		if(input == null || input.getStrutturaAllevamento() ==null) {
			return null;
		}
		it.tndigitale.a4g.zootecnia.dto.StrutturaAllevamentoDto output = new it.tndigitale.a4g.zootecnia.dto.StrutturaAllevamentoDto();
		output.setIdentificativo(input.getStrutturaAllevamento().getIdentificativo());
		output.setCap(input.getStrutturaAllevamento().getCap());
		output.setComune(input.getStrutturaAllevamento().getComune());
		output.setFoglioCatastale(input.getStrutturaAllevamento().getFoglio());
		output.setIndirizzo(input.getStrutturaAllevamento().getIndirizzo());
		output.setLatitudine(input.getStrutturaAllevamento().getLatitudine());
		output.setLocalita(input.getStrutturaAllevamento().getLocalita());
		output.setLongitudine(input.getStrutturaAllevamento().getLongitudine());
		output.setParticella(input.getStrutturaAllevamento().getParticella());
		output.setSezione(input.getStrutturaAllevamento().getSezione());
		output.setSubalterno(input.getStrutturaAllevamento().getSubalterno());
		return output;
	}
}

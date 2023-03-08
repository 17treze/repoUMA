package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.DatiCatastaliModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.SerreModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.StoccaggioModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.StrumentaliModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DatiCatastaliDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioFabbricatoAbstract;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioFabbricatoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioSerreDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioStoccaggioDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioStrumentaliDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.NameTypeFabbricato;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.SottotipoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaDto;

@Component
public class DettaglioFabbricatoDtoBuilder {
	private DettaglioFabbricatoAbstract dettaglioFabbricatoDto;
	
	public DettaglioFabbricatoAbstract from (FabbricatoModel model) {
		if (model instanceof StrumentaliModel) {
			var strumentaliModel = (StrumentaliModel) model;
			var strumentaliDto = new DettaglioStrumentaliDto();
			strumentaliDto.setType(NameTypeFabbricato.STRUMENTALE);
			strumentaliDto.setSuperficieScoperta(strumentaliModel.getSuperficieScoperta())
			.setSuperficieCoperta(strumentaliModel.getSuperficiecoperta());
			dettaglioFabbricatoDto = strumentaliDto;
		} else if (model instanceof SerreModel) {
			var serraModel = (SerreModel) model;
			var serraDto = new DettaglioSerreDto();
			serraDto.setType(NameTypeFabbricato.SERRA);
			serraDto.setAnnoAcquisto(serraModel.getAnnoAcquisto())
			.setAnnoCostruzione(serraModel.getAnnoCostruzione())
			.setImpiantoRiscaldamento(serraModel.isImpiantoRiscaldamento())
			.setTipologiaMateriale(serraModel.getTipologiaMateriale())
			.setTitoloConformitaUrbanistica(serraModel.getTitoloConformitaUrbanistica());
			dettaglioFabbricatoDto = serraDto;
		} else if (model instanceof StoccaggioModel) {
			var stoccaggioModel = (StoccaggioModel) model;
			var stoccaggioDto = new DettaglioStoccaggioDto();
			stoccaggioDto.setType(NameTypeFabbricato.STOCCAGGIO);
			stoccaggioDto.setAltezza(stoccaggioModel.getAltezza())
			.setCopertura(stoccaggioModel.getCopertura());
			dettaglioFabbricatoDto = stoccaggioDto;
		} else if (model instanceof FabbricatoModel) {
			dettaglioFabbricatoDto = new DettaglioFabbricatoDto().setType(NameTypeFabbricato.FABBRICATO);
		}
		SottotipoDto sottotipo = new SottotipoDto();
		sottotipo.setId(model.getSottotipo().getTipologia().getId())
		.setDescrizione(model.getSottotipo().getTipologia().getDescrizione());
		TipologiaDto tipologia = new TipologiaDto();
		tipologia.setId(model.getSottotipo().getId())
		.setDescrizione(model.getSottotipo().getDescrizione());
		var tipologiaList = new ArrayList<TipologiaDto>();
		tipologiaList.add(tipologia);
		sottotipo.setTipologie(tipologiaList);
		dettaglioFabbricatoDto.setDescrizione(model.getDescrizione())
		.setComune(model.getComune())
		.setDenominazione(model.getDenominazione())
		.setIndirizzo(model.getIndirizzo())
		.setVolume(model.getVolume())
		.setSuperficie(model.getSuperficie())
		.setTipoConduzione(model.getTipoConduzione())
		.setId(model.getId())
		.setSottotipo(sottotipo);
		
		if (model.getDatiCatastali() != null) {
			List<DatiCatastaliDto> datiCatastali = new ArrayList<DatiCatastaliDto>();
			model.getDatiCatastali().forEach(el -> {
				DatiCatastaliDto dato = new DatiCatastaliDto();
				dato.setCategoria(el.getCategoria());
				dato.setConsistenza(el.getConsistenza());
				dato.setDenominatore(el.getDenominatore());
				dato.setIndirizzo(el.getIndirizzo());
				dato.setInTrentino(el.getInTrentino());
				dato.setNote(el.getNote());
				dato.setTipologia(el.getTipologia());
				dato.setSuperficie(el.getSuperficie());
				dato.setComune(el.getComune());
				dato.setSezione(el.getSezione());
				dato.setFoglio(el.getFoglio());
				dato.setParticella(el.getParticella());
				dato.setSub(el.getSub());
				dato.setEsito(el.getEsito());
				datiCatastali.add(dato);
			});
			dettaglioFabbricatoDto.setDatiCatastali(datiCatastali);
		}
		return dettaglioFabbricatoDto;
	}
}

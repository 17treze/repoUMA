package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.builder;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaMotorizzataModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.ClasseFunzionaleDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioMacchinaDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.MotoreDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.SottotipoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaDto;

@Component
public class DettaglioMacchinaDtoBuilder {

	private DettaglioMacchinaDto dettaglioMacchinaDto;

	public DettaglioMacchinaDtoBuilder() {
		dettaglioMacchinaDto = new DettaglioMacchinaDto();
	}

	public DettaglioMacchinaDtoBuilder newDto() {
		dettaglioMacchinaDto = new DettaglioMacchinaDto();
		return this;
	}

	public DettaglioMacchinaDtoBuilder from(MacchinaModel model) {

		// valorizzazione classe funzionale
		ClasseFunzionaleDto classeFunzionale = new ClasseFunzionaleDto();
		classeFunzionale.setId(model.getSottotipoMacchinario().getClassefunzionale().getId());
		classeFunzionale.setDescrizione(model.getSottotipoMacchinario().getClassefunzionale().getDescrizione());

		// valorizzazione tipologia
		SottotipoDto tipologia = new SottotipoDto();
		tipologia.setId(model.getSottotipoMacchinario().getClassefunzionale().getTipologia().getId())
				.setDescrizione(model.getSottotipoMacchinario().getClassefunzionale().getTipologia().getDescrizione());

		// valorizzazione sottotipo
		TipologiaDto sottoTipo = new TipologiaDto();
		sottoTipo.setId(model.getSottotipoMacchinario().getId()).setDescrizione(model.getSottotipoMacchinario().getDescrizione());

		var sottoTipologiaList = new ArrayList<TipologiaDto>();
		sottoTipologiaList.add(sottoTipo);

		classeFunzionale.setTipologie(sottoTipologiaList);

		var classiFunzionaliList = new ArrayList<ClasseFunzionaleDto>();
		classiFunzionaliList.add(classeFunzionale);

		tipologia.setClassiFunzionali(classiFunzionaliList);

		dettaglioMacchinaDto.setId(model.getId()).setAnnoCostruzione(model.getAnnoDiCostruzione()).setDataImmatricolazione(model.getDataImmatricolazione()).setMarca(model.getMarca())
				.setModello(model.getModello()).setNumeroMatricola(model.getNumeroMatricola()).setNumeroTelaio(model.getNumeroTelaio()).setSottotipo(tipologia).setTarga(model.getTarga())
				.setTipoPossesso(model.getTipoPossesso());
		dettaglioMacchinaDto.setCodiceFiscale(model.getCodiceFiscale());
		dettaglioMacchinaDto.setRagioneSociale(model.getRagioneSociale());
		dettaglioMacchinaDto.setFlagMigrato(model.getFlagMigrato() != null ? model.getFlagMigrato() : 0);

		return this;
	}

	public DettaglioMacchinaDtoBuilder withMotore(MacchinaModel macchina) {
		MotoreDto motore = new MotoreDto();
		if (macchina instanceof MacchinaMotorizzataModel) {
			var macchinaMotorizzata = (MacchinaMotorizzataModel) macchina;
			motore.setAlimentazione(macchinaMotorizzata.getAlimentazione()).setMarca(macchinaMotorizzata.getMarcaMotore()).setMatricola(macchinaMotorizzata.getMatricola())
					.setPotenza(macchinaMotorizzata.getPotenza()).setTipo(macchinaMotorizzata.getTipoMotore());
			dettaglioMacchinaDto.setMotore(motore);
		} else {
			dettaglioMacchinaDto.setMotore(null);
		}

		return this;
	}

	public DettaglioMacchinaDto build() {
		return dettaglioMacchinaDto;
	}
}

package it.tndigitale.a4g.uma.dto.richiesta.builder;

import java.util.List;
import java.util.stream.Collectors;

import it.tndigitale.a4g.uma.business.persistence.entity.UtilizzoMacchinariModel;
import it.tndigitale.a4g.uma.dto.richiesta.MacchinaDto;

public class MacchinaUmaBuilder  {

	public List<MacchinaDto> from(List<UtilizzoMacchinariModel> utilizzoMacchinariModelList) {
		return utilizzoMacchinariModelList.stream().map(this::fromModel).collect(Collectors.toList());
	}

	public MacchinaDto fromModel(UtilizzoMacchinariModel utilizzoMacchinariModel) {
		if (utilizzoMacchinariModel != null) {
			return new MacchinaDto()
					.setIsUtilizzata(utilizzoMacchinariModel.getFlagUtilizzo())
					.setAlimentazione(utilizzoMacchinariModel.getAlimentazione())
					.setClasse(utilizzoMacchinariModel.getClasse())
					.setDescrizione(utilizzoMacchinariModel.getDescrizione())
					.setId(utilizzoMacchinariModel.getId())
					.setMarca(utilizzoMacchinariModel.getMarca())
					.setPossesso(utilizzoMacchinariModel.getPossesso())
					.setTarga(utilizzoMacchinariModel.getTarga())
					.setIdentificativoAgs(utilizzoMacchinariModel.getIdentificativoAgs());
		}
		return null;
	}
}

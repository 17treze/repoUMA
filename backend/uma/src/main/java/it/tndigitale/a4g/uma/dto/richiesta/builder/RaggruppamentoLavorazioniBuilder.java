package it.tndigitale.a4g.uma.dto.richiesta.builder;

import java.util.ArrayList;
import java.util.List;

import it.tndigitale.a4g.uma.business.persistence.entity.GruppoLavorazioneModel;
import it.tndigitale.a4g.uma.business.persistence.entity.LavorazioneModel;
import it.tndigitale.a4g.uma.dto.richiesta.FabbricatoDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;

public class RaggruppamentoLavorazioniBuilder {

	private RaggruppamentoLavorazioniDto raggruppamentoLavorazioniDto;

	public RaggruppamentoLavorazioniBuilder() {
		raggruppamentoLavorazioniDto = new RaggruppamentoLavorazioniDto();
	}

	public RaggruppamentoLavorazioniBuilder withGruppo(GruppoLavorazioneModel gruppo) {
		raggruppamentoLavorazioniDto
		.setId(gruppo.getId())
		.setNome(gruppo.getNome())
		.setIndice(gruppo.getIndice());
		return this;
	}

	public RaggruppamentoLavorazioniBuilder withLavorazioni(List<LavorazioneModel> lavorazioni) {
		ArrayList<LavorazioneDto> list = new ArrayList<>();
		lavorazioni.forEach(lav -> 
		list.add(new LavorazioneDto()
				.setId(lav.getId())
				.setIndice(lav.getIndice())
				.setNome(lav.getNome())
				.setTipologia(lav.getTipologia())
				.setUnitaDiMisura(lav.getUnitaDiMisura())));
		this.raggruppamentoLavorazioniDto.setLavorazioni(list);
		return this;
	}

	public RaggruppamentoLavorazioniBuilder withSuperficieMassima(Long supMassima) {
		raggruppamentoLavorazioniDto.setSuperficieMassima(supMassima);
		return this;
	}

	public RaggruppamentoLavorazioniBuilder withFabbricati(List<FabbricatoDto> fabbricati) {
		raggruppamentoLavorazioniDto.setFabbricati(fabbricati);
		return this;
	}

	public RaggruppamentoLavorazioniDto build() {
		return raggruppamentoLavorazioniDto;
	}
}

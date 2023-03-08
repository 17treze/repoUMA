package it.tndigitale.a4g.uma.business.service.lavorazioni;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.GruppoLavorazioneModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbricatiDao;
import it.tndigitale.a4g.uma.dto.richiesta.FabbricatoDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneFilter;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;
import it.tndigitale.a4g.uma.dto.richiesta.builder.RaggruppamentoLavorazioniBuilder;

public abstract class RecuperaLavorazioniStrategy {

	@Autowired
	private FabbricatiDao fabbricatiDao;

	abstract List<RaggruppamentoLavorazioniDto> getRaggruppamenti(RichiestaCarburanteModel domandaUma);

	// utilizzato da lav altro e zootecnia
	List<RaggruppamentoLavorazioniDto> buildDto(List<GruppoLavorazioneModel> gruppi) {
		List<RaggruppamentoLavorazioniDto> raggruppamento = new ArrayList<>();
		gruppi.stream().forEach(gruppo -> raggruppamento.add(new RaggruppamentoLavorazioniBuilder()
				.withGruppo(gruppo)
				.withLavorazioni(gruppo.getLavorazioneModels())
				.build()));
		return raggruppamento;
	} 

	// utilizzato solo da lav fabbricati e serre
	List<RaggruppamentoLavorazioniDto> getRaggruppamentiFabbricati(RichiestaCarburanteModel richiestaCarburante, LavorazioneFilter.LavorazioniFabbricati ambito) {
		List<FabbricatoModel> fabbricati = fabbricatiDao.findByRichiestaCarburante_id(richiestaCarburante.getId());
		var ambitoLavorazione = AmbitoLavorazione.valueOf(ambito.name());
		if (CollectionUtils.isEmpty(fabbricati)) { return new ArrayList<>();}

		List<RaggruppamentoLavorazioniDto> listRaggruppamentoLavorazioniDto = new ArrayList<>();
		fabbricati
		.stream()
		.filter(f -> ambitoLavorazione.equals(f.getTipoFabbricato().getGruppoLavorazione().getAmbitoLavorazione()))
		.collect(Collectors.groupingBy(p -> p.getTipoFabbricato().getGruppoLavorazione()))
		.forEach((gruppo, fab) -> {
			List<FabbricatoDto> fabbricatiDto = fab.stream().map(f -> new FabbricatoDto()
					.setComune(f.getComune())
					.setId(f.getId())
					.setParticella(f.getParticella())
					.setProvincia(f.getProvincia())
					.setSiglaProvincia(f.getSiglaProvincia())
					.setSubalterno(f.getSubalterno())
					.setVolume(f.getVolume()))
					.collect(Collectors.toList());

			listRaggruppamentoLavorazioniDto.add(new RaggruppamentoLavorazioniBuilder()
					.withGruppo(gruppo)
					.withLavorazioni(gruppo.getLavorazioneModels())
					.withFabbricati(fabbricatiDto)
					.build());
		});
		return listRaggruppamentoLavorazioniDto;
	}
}

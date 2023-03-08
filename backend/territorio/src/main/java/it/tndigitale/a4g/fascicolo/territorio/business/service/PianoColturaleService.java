package it.tndigitale.a4g.fascicolo.territorio.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.fascicolo.territorio.business.persistence.repository.legacy.PianoColtureAgsDao;
import it.tndigitale.a4g.fascicolo.territorio.dto.CodificaColtura;
import it.tndigitale.a4g.fascicolo.territorio.dto.ColturaDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.ConduzioneDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.ParticellaDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.PianoColturaleFilter;
import it.tndigitale.a4g.fascicolo.territorio.dto.legacy.PianoColturaleAgsDto;
import it.tndigitale.a4g.framework.time.Clock;

@Service
public class PianoColturaleService {

	@Autowired 
	private PianoColtureAgsDao pianoColtureAgsDao;
	@Autowired 
	private Clock clock;

	public List<ParticellaDto> getPianoColturale(PianoColturaleFilter filter) {
		if (filter.getData() == null) {
			filter.setData(clock.now());
		}
		List<PianoColturaleAgsDto> listaColture = pianoColtureAgsDao.getColture(filter);

		List<ParticellaDto> response = new ArrayList<>();

		listaColture.stream()
		.collect(Collectors.groupingBy(PianoColturaleAgsDto::getIdParticella))
		.forEach((a,b) ->  {
			Optional<PianoColturaleAgsDto> pianoColturaleByIdParticellaOpt = b.stream().findAny();
			if (!pianoColturaleByIdParticellaOpt.isPresent()) {return;}
			PianoColturaleAgsDto pianoColturaleByIdParticella = pianoColturaleByIdParticellaOpt.get();
			var particellaDto = new ParticellaDto()
					.setNumero(pianoColturaleByIdParticella.getParticella())
					.setCodiceNazionale(pianoColturaleByIdParticella.getCodiceNazionale())
					.setSubalterno(Optional.ofNullable(pianoColturaleByIdParticella.getSubalterno()))
					.setFoglio(pianoColturaleByIdParticella.getFoglio())
					.setConduzioneDto(new ConduzioneDto()
							.setCodiceAtto(pianoColturaleByIdParticella.getTipoAtto())
							.setDescrizioneAtto(pianoColturaleByIdParticella.getDescrizioneAtto())
							.setTitolo(pianoColturaleByIdParticella.getTitoloConduzione()))
					.setColture(new ArrayList<>());

			b.stream().collect(Collectors.groupingBy(PianoColturaleAgsDto::getIdColtura))
			.entrySet()
			.stream()
			.forEach(entry -> entry.getValue().stream().forEach(pianoColturaleByIdColtura -> particellaDto.addColtura(new ColturaDto()
					.setSuperficieAccertata(pianoColturaleByIdColtura.getSuperficieAccertata())
					.setSuperficieDichiarata(pianoColturaleByIdColtura.getSuperficieDichiarata())
					.setCriterioMantenimento(pianoColturaleByIdColtura.getCriterioMantenimento())
					.setCodifica(new CodificaColtura()
							.setCodiceDestinazioneUso(pianoColturaleByIdColtura.getCodiceDestinazioneUso())
							.setCodiceSuolo(pianoColturaleByIdColtura.getCodiceProdotto())
							.setCodiceQualita(pianoColturaleByIdColtura.getCodiceQualita())
							.setCodiceUso(pianoColturaleByIdColtura.getCodiceUso())
							.setCodiceVarieta(pianoColturaleByIdColtura.getCodiceVarieta())))));
			response.add(particellaDto);
		});
		return response;
	}

}

package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoOvicaprinoDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;

@Component
public class ControlloCapoOvicaprino implements BiFunction<RichiestaAllevamDu,CapoDto,EsitoCalcoloCapoModel> {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ControlliCapoOvicaprinoFactory controlliCapoOvicaprinoFactory;

	@Override
	public EsitoCalcoloCapoModel apply(RichiestaAllevamDu richiestaAllevamDu, CapoDto capoOvicaprino) {
		EsitoCalcoloCapoModel a4gtRichAllevamDuEsito=Stream.of(
				checkIntervento320,
				checkIntervento321
				)
				.map(f -> f.apply((CapoOvicaprinoDto) capoOvicaprino,richiestaAllevamDu ))
				.filter( Optional::isPresent )
				.map( Optional::get )
				.findFirst()
				.orElseThrow( () -> new RuntimeException( "Errore generico calcolo capo Ovicaprino! " ) );
		a4gtRichAllevamDuEsito.setCapoId(Long.parseLong(capoOvicaprino.getIdCapo()));
		a4gtRichAllevamDuEsito.setCodiceCapo(capoOvicaprino.getMarcaAuricolare());		
		return a4gtRichAllevamDuEsito;
	}	

	protected BiFunction<CapoOvicaprinoDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>>  checkIntervento320 = (capo, richiestaAllevamDu) -> {
		if ("320".equals(richiestaAllevamDu.getCodiceIntervento())) {
			return controlliCapoOvicaprinoFactory.from(capo.getDataNascita()).checkIntervento320(capo, richiestaAllevamDu);
		}
		return Optional.empty();
	};

	protected BiFunction<CapoOvicaprinoDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>>  checkIntervento321 = (capo, richiestaAllevamDu) -> {
		try {
			InformazioniAllevamento infoAllevamento = objectMapper.readValue(richiestaAllevamDu.getDatiAllevamento(), InformazioniAllevamento.class);
			return  Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,
					MessageFormat.format("Il capo non è ammissibile perché l’allevamento {0} non è presente nell’archivio IGP"  ,
							infoAllevamento.getCodiceAllevamento())
					));
		} catch (IOException e) {
			throw new IllegalArgumentException(String.format("Errore Reperimento dati allevamento capo %s" , capo.getMarcaAuricolare()));
		}
	};
}

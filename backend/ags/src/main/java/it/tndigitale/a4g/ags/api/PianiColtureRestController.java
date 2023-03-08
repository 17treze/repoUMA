package it.tndigitale.a4g.ags.api;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.ags.dto.CatastoPianoColture;
import it.tndigitale.a4g.ags.dto.DatiAggiuntiviParticella;
import it.tndigitale.a4g.ags.dto.InfoParticella;
import it.tndigitale.a4g.ags.service.PianiColtureService;

@RestController
@RequestMapping(ApiUrls.PIANI_COLTURALI_V1)
public class PianiColtureRestController {

	private static final Logger logger = LoggerFactory.getLogger(PianiColtureRestController.class);

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PianiColtureService pianiColtureService;

	@ApiOperation("Recupera tutte le particelle che hanno un piano colturale")
	@GetMapping("/particelle")
	@PreAuthorize("@abilitazioniComponent.checkLetturaPianiColture()")
	public List<InfoParticella> getParticelleFromPianiColture(@RequestParam(value = "anno", required = true) Integer anno) {

		return pianiColtureService.getParticelleFromPianiColture(anno);
	}

	@ApiOperation("Ritorna il numero di particelle che hanno un piano colturale")
	@GetMapping("/particelle/count")
	@PreAuthorize("@abilitazioniComponent.checkLetturaPianiColture()")
	public ResponseEntity<Integer> getParticelleDistinctCount(@RequestParam(value = "anno", required = true) Integer anno) {

		return new ResponseEntity<>(pianiColtureService.getParticelleDistinctCount(anno), HttpStatus.OK);
	}

	@ApiOperation("Recupera le informazioni sul piano colture della particella")
	@GetMapping("")
	@PreAuthorize("@abilitazioniComponent.checkLetturaPianiColture()")
	public ResponseEntity<List<CatastoPianoColture>> getPianiColtureByInfoParticella(
			@RequestParam(value = "anno", required = true) Integer anno,
			@RequestParam(value = "params") @ApiParam(value = "Informazioni sulla particella (Codice comune catastale, codice particella, sub)", required = true) String params)
			throws Exception {
		InfoParticella info;
		try {
			info = objectMapper.readValue(params, InfoParticella.class);
			if (info.getCodiceComuneCatastale() == null)
				throw new Exception("Codice Comune Catastale non valorizzato.");
			if (Strings.isNullOrEmpty(info.getParticella()))
				throw new Exception("Particella non valorizzata.");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(pianiColtureService.getPianiColtureByInfoParticella(info, anno), HttpStatus.OK);
	}

	@ApiOperation("Recupera i dati aggiuntivi della particella (orientamento, pendenza, altitudine, irrigabilità)")
	@GetMapping("/particelle/dati-aggiuntivi")
	@PreAuthorize("@abilitazioniComponent.checkLetturaPianiColture()")
	public ResponseEntity<DatiAggiuntiviParticella> getDatiAggiuntiviInfoParticella(
			@RequestParam(value = "anno", required = true) Integer anno,
			@RequestParam(value = "params") @ApiParam(value = "Informazioni sulla particella (Codice comune catastale, codice particella, sub)", required = true) String params)
			throws Exception {
		InfoParticella info = objectMapper.readValue(params, InfoParticella.class);
		if (info.getCodiceComuneCatastale() == null)
			throw new Exception("Codice Comune Catastale non valorizzato.");
		if (Strings.isNullOrEmpty(info.getParticella()))
			throw new Exception("Particella non valorizzata.");

		DatiAggiuntiviParticella result = pianiColtureService.getDatiAggiuntiviPerParticella(info, anno);
		return new ResponseEntity<>(result, result != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
}

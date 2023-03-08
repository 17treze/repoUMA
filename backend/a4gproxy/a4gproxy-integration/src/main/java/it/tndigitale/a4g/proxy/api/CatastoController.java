package it.tndigitale.a4g.proxy.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import it.tndigitale.a4g.proxy.dto.catasto.InformazioniImmobileDto;
import it.tndigitale.a4g.proxy.dto.catasto.InformazioniParticellaDto;
import it.tndigitale.a4g.proxy.dto.catasto.InformazioniVisuraImmobileDto;
import it.tndigitale.a4g.proxy.dto.catasto.TipologiaParticellaCatastale;
import it.tndigitale.a4g.proxy.services.catasto.ImmobileService;
import it.tndigitale.a4g.proxy.services.catasto.ParticellaService;
import it.tndigitale.a4g.proxy.services.catasto.VisuraImmobileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping(ApiUrls.CATASTO)
@Api(value = "Interfaccia per l'interfacciamento con il catasto")
public class CatastoController {

	@Autowired
	private ParticellaService particellaService;
    @Autowired
    private ImmobileService immobileService;
    @Autowired
    private VisuraImmobileService visuraImmobileService;

	@ApiOperation("Reperisce informazioni di una particella dal catasto")
    @GetMapping("/")
    public InformazioniParticellaDto getInfoParticella(
			@RequestParam(required = true) String numeroParticella,
			@RequestParam(required = true) TipologiaParticellaCatastale tipologia,
			@RequestParam(required = true) Integer codiceComuneCatastale
	) {
		InformazioniParticellaDto result = particellaService.getInfoParticella(numeroParticella, tipologia, codiceComuneCatastale);
		return result;
	}

	@ApiOperation("Reperisce informazioni su un immobile di una particella edificiale dal catasto")
	@GetMapping("/immobile")
	public InformazioniImmobileDto getInfoImmobile(
			@RequestParam(required = true) String numeroParticella,
			@RequestParam(required = true) Integer codiceComuneCatastale,
			@RequestParam BigInteger subalterno
	) {
		InformazioniImmobileDto result = immobileService.getInfoImmobile(numeroParticella, codiceComuneCatastale, subalterno);
		return result;
	}

	@ApiOperation("Data una certa particella edificiale, permette di ottenere la lista dei subalterni inerenti la visura dell'unita' immobiliare alla data odierna")
	@GetMapping("/visura-immobile")
	public List<String> getElencoSubalterniParticella(
			@RequestParam(required = true) String numeroParticella,
			@RequestParam(required = true) Integer codiceComuneCatastale
	) {
		List<String> result = visuraImmobileService.getElencoSubalterniParticella(numeroParticella, codiceComuneCatastale);
		return result;
	}

//    @ApiOperation("Reperisce informazioni di una particella dal catasto")
//    @GetMapping("/immobile")
//    public UnitaImmobiliareType getInfoImmobile(
//            @RequestParam(required = true) String numeroParticella,
//            @RequestParam(required = true) Integer codiceComuneCatastale,
//			@RequestParam(required = true) BigInteger subalterno)  {
//        return immobileService.getInfoUnitaFromWs(numeroParticella, codiceComuneCatastale, subalterno);
//    }
//
//	@ApiOperation("Reperisce informazioni di una particella dal catasto")
//	@GetMapping("/")
//	public ParticellaTavolareType getInfoParticella(
//			@RequestParam(required = true) String numeroParticella,
//			@RequestParam(required = true) TipologiaParticellaCatastale tipologia,
//			@RequestParam(required = true) Integer codiceComuneCatastale) {
//		return particellaService.getInfoParticella(numeroParticella, tipologia, codiceComuneCatastale);
//	}
}

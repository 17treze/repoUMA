package it.tndigitale.a4gistruttoria.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.dto.PremioPagamentiStatoIstruttoriaNettoLordoDto;
import it.tndigitale.a4gistruttoria.dto.istruttoria.IstruttoriaDomandaUnica;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.IstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.IstruttoriaDisaccoppiatoService;
import it.tndigitale.a4gistruttoria.util.CodicePac;

@RestController
@RequestMapping(ApiUrls.ISTRUTTORIE_DU_V1)
@Api(value = "Istruttoria di pagamento per la domanda unica")
public class IstruttoriaDomandaUnicaController {
	
	private static Logger logger = LoggerFactory.getLogger(IstruttoriaDomandaUnicaController.class);
	
	@Autowired
	private IstruttoriaService istruttoriaService;
	@Autowired
	private IstruttoriaDisaccoppiatoService istruttoriaDettaglioService;

	@GetMapping
	@ApiOperation("Ricerca paginata delle istruttorie delle domande uniche secondo i criteri di ricerca impostati")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public RisultatiPaginati<IstruttoriaDomandaUnica> ricercaIstruttorie(
			IstruttoriaDomandaUnicaFilter filter, Paginazione paginazione,
			Ordinamento ordinamento) throws Exception {
		return istruttoriaService.ricerca(
				filter, paginazione,
				Optional.ofNullable(ordinamento).filter(o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY));
	}
	
	@GetMapping("/count")
	@ApiOperation("Conteggio delle istruttorie secondo i criteri di ricerca impostati")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public Long countIstruttorieDU(IstruttoriaDomandaUnicaFilter filter) throws Exception {
		return istruttoriaService.countIstruttorie(filter);
	}
	
	@GetMapping("/{idIstruttoria}")
	@ApiOperation("Recupero istruttoria per id dell'istruttoria")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public IstruttoriaDomandaUnica getIstruttoriaById(@PathVariable(value = "idIstruttoria") Long idIstruttoria) {
		return istruttoriaService.getIstruttoriaById(idIstruttoria);
	}
	
	@GetMapping("/anniCampagna")
	@ApiOperation("Recupero degli anni di campagna gestiti dal sistema")
	public List<Integer> getAnniCampagna(CodicePac codicePac) {
		return istruttoriaService.getAnniCampagna(codicePac);
	}

	@ApiOperation("Recupera dati aggregati su premi pagati delle istruttorie disaccoppiato, suddivisi per stato istruttoria")
	@GetMapping("/{tipoSostegno}/{tipoIstruttoria}/{annoCampagna}/dati-aggregati-pagamenti")
	public List<PremioPagamentiStatoIstruttoriaNettoLordoDto> getValoriPremioLordoNettoPerStato(
			@PathVariable final Integer annoCampagna,
			@PathVariable final Sostegno tipoSostegno,
			@PathVariable final TipoIstruttoria tipoIstruttoria) {
		return istruttoriaDettaglioService.getValoriPremioLordoNettoPerStato(annoCampagna, tipoSostegno, tipoIstruttoria);
	}
	
	@ApiOperation("Permette il download di un file .csv per le istruttorie selezionate")
	@PostMapping("/csv")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public ResponseEntity<Resource> downloadCsv(HttpServletRequest request, @RequestBody List<Long> idIstruttorie) throws IOException {
		
		// create file as Resource
		Resource resource = istruttoriaService.downloadCsv(idIstruttorie);
		
		 // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        // Il nome del file secondo quanto richiesto in IDU-ANT-12 viene generato Front-End
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SCARICA_DATI_ISTRUTTORIE.csv")
                .body(resource);
	}
}

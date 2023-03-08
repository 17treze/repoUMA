package it.tndigitale.a4gistruttoria.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.dto.Processo;
import it.tndigitale.a4gistruttoria.service.ProcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller per gestire operazioni sui processi generali
 * 
 * @author Alessandro.Cuel
 *
 */
@RestController
@RequestMapping(ApiUrls.PROCESSI)
@Transactional
@Deprecated
public class ProcessiRestController {

	@Autowired
	protected ProcessoService processoService;

	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("")
	@ApiOperation("Servizio REST che ritorna la lista dei processi attivi")
	public List<Processo> listaProcessiAttivi(@RequestParam(value = "params") @ApiParam("params che contiene l'id dell'istruttoria") String params) {
		List<Processo> processi = new ArrayList<Processo>();
		try {
			return processoService.getListaProcessiAttivi();

		} catch (Exception e) {
			return processi;
		}
	}

	@GetMapping("/{id}")
	@ApiOperation("Servizio REST che ritorna le informazioni del processo")
	public Processo getInfoProcesso(@PathVariable(value = "id") Long id) {
		try {
			return processoService.getProcessoById(id);
		} catch (Exception e) {
			return new Processo();
		}
	}

}

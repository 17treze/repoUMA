package it.tndigitale.a4gistruttoria.api.domandaunica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.domandaunica.InformazioniDisaccoppiatoDomanda;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SuperficiImpegnate;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.DisaccoppiatoService;
import it.tndigitale.a4gistruttoria.service.businesslogic.domanda.SuperficiImpegnateService;

@RestController
@RequestMapping(ApiUrls.DISACCOPPIATO_DOMANDA_UNICA_V1)
@Api(value = "Gestione Servizi relativi alla gestione dati del disaccoppiato (domanda unica)")
public class DisaccoppiatoController {

	@Autowired
	private SuperficiImpegnateService superficiService;
	@Autowired
	private DisaccoppiatoService disaccoppiatoService;
	
	@ApiOperation("Recupera i dati delle superfici impegnate sull'intervento BPS")
	@GetMapping("{idDomanda}/superfici")
	public SuperficiImpegnate getSuperficiImpegnateBPS(@PathVariable(value = "idDomanda") Long idDomanda,
			Paginazione paginazione, Ordinamento ordinamento) throws Exception {
		return superficiService.superficiImpegnateBPSDomanda(idDomanda, paginazione, ordinamento);
	}

	@ApiOperation("Recupera i dati caratteristici del disaccoppiato")
	@GetMapping("{idDomanda}/informazioni")
	public InformazioniDisaccoppiatoDomanda getInformazioni(@PathVariable(value = "idDomanda") Long idDomanda) throws Exception {
		return disaccoppiatoService.getInformazioniDisaccoppiatoDomanda(idDomanda);
	}
}

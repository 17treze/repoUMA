package it.tndigitale.a4gistruttoria.api.domandaunica;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.AccoppiatoSuperficie;
import it.tndigitale.a4gistruttoria.dto.domandaunica.DichiarazioneDu;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioSuperficiImpegnateACS;
import it.tndigitale.a4gistruttoria.strategy.DatiASDatiSuperficiImpegnate;
import it.tndigitale.a4gistruttoria.strategy.DatiASDichiarazioni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiUrls.ACC_SUP_DOMANDA_UNICA_V1)
@Api(value = "Gestione Servizi relativi alla gestione dati dell'accoppiato superficie (domanda unica)")
public class AccoppiatoSuperficieController {

	
	@Autowired
	private DatiASDatiSuperficiImpegnate datiASDatiSuperficiImpegnate;
	@Autowired
	private DatiASDichiarazioni datiASDichiarazioni;

	@ApiOperation("Recupera i dati delle superfici impegnate sull'intervento ACS")
	@GetMapping("{idDomanda}/superfici")
	public DettaglioSuperficiImpegnateACS getSuperficiImpegnate(@PathVariable Long idDomanda) throws Exception {
		AccoppiatoSuperficie accoppiatoSuperficie= new AccoppiatoSuperficie(idDomanda, null);
		datiASDatiSuperficiImpegnate.recupera(accoppiatoSuperficie);
		return accoppiatoSuperficie.getDatiSuperficiImpegnate();
	}
	
	@ApiOperation("Recupera i dati delle superfici impegnate sull'intervento ACS")
	@GetMapping("{idDomanda}/dichiarazioni")
	public List<DichiarazioneDu> getDichiarazioni(@PathVariable Long idDomanda) throws Exception {
		AccoppiatoSuperficie accoppiatoSuperficie= new AccoppiatoSuperficie(idDomanda, null);
		datiASDichiarazioni.recupera(accoppiatoSuperficie);
		return accoppiatoSuperficie.getDichiarazioni();
	}
}

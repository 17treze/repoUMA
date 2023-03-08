package it.tndigitale.a4gistruttoria.api.domandaunica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.AccoppiatoZootecnia;
import it.tndigitale.a4gistruttoria.dto.domandaunica.DichiarazioneDu;
import it.tndigitale.a4gistruttoria.strategy.DatiAZDichiarazioni;

@RestController
@RequestMapping(ApiUrls.ACC_ZOO_DOMANDA_UNICA_V1)
@Api(value = "Gestione Servizi relativi alla gestione dati dell'accoppiato zootenica (domanda unica)")
public class AccoppiatoZootecniaController {
	
	@Autowired
	private DatiAZDichiarazioni datiAZDichiarazioni;

	@ApiOperation("Recupera i dati delle superfici impegnate sull'intervento ACZ")
	@GetMapping("{idDomanda}/dichiarazioni")
	public List<DichiarazioneDu> getDichiarazioni(@PathVariable Long idDomanda) throws Exception {
		AccoppiatoZootecnia accoppiatoZootecnia= new AccoppiatoZootecnia(idDomanda, null);
		datiAZDichiarazioni.recupera(accoppiatoZootecnia);
		return accoppiatoZootecnia.getDichiarazioni();
	}

}

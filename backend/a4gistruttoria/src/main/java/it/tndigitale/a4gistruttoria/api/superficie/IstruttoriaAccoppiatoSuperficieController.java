package it.tndigitale.a4gistruttoria.api.superficie;

import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.ControlliSostegno;
import it.tndigitale.a4gistruttoria.dto.InterventoSuperficieDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.*;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;
import it.tndigitale.a4gistruttoria.service.businesslogic.superficie.AccoppiatoSuperficieService;
import it.tndigitale.a4gistruttoria.strategy.DatiASDatiDomanda;
import it.tndigitale.a4gistruttoria.strategy.DatiASDatiParticella;
import it.tndigitale.a4gistruttoria.strategy.DatiASDisciplinaFinanziaria;
import it.tndigitale.a4gistruttoria.util.CustomConverters;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoControllo.LivelloControllo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiUrls.ISTRUTTORIE_ACCOPPIATO_SUPERFICIE_V1)
public class IstruttoriaAccoppiatoSuperficieController {

	public static final String DATI_ISTRUTTORE = "/datiIstruttore";
	
	@Autowired
	private DatiIstruttoreService datiIstruttoreService;

	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@Autowired
	private DatiASDatiDomanda datiASDatiDomanda;
	
	@Autowired
	private DatiASDisciplinaFinanziaria datiASDisciplinaFinanziaria;
	
	@Autowired
	private DatiASDatiParticella datiASDatiParticella;

	@Autowired
	private AccoppiatoSuperficieService accoppiatoSuperficieService;


	@ApiOperation("Recupera i dati degli esiti sui controlli sostegno")
	@GetMapping("{idIstruttoria}/esiti-controlli-sostegno")
	public ControlliSostegno getEsitiControlliSostegno(@PathVariable(value = "idIstruttoria") Long idIstruttoria) {
		return loadEsitiControlliSostegno(idIstruttoria);
	}
	
	@ApiOperation("Recupera i dati di dettaglio degli esiti dei controlli dell'istruttoria accoppiato superficie")
	@GetMapping("{idIstruttoria}/esiticalcoli")
	public DatiDomandaAccoppiato getEsitiCalcoli(@PathVariable Long idIstruttoria) throws Exception {
		return datiASDatiDomanda.recupera(idIstruttoria);
	}
	
	@ApiOperation("Recupera  i dati della disciplina finanziaria dell'istruttoria accoppiato superficie")
	@GetMapping("{idIstruttoria}/disciplina")
	public Map<String, String> getDisciplina(@PathVariable Long idIstruttoria) throws Exception {
		return datiASDisciplinaFinanziaria.recupera(idIstruttoria);
	}
	
	@ApiOperation("Recupera  i dati della disciplina finanziaria dell'istruttoria accoppiato superficie")
	@GetMapping("{idIstruttoria}/esitiparticelle")
	public DatiParticellaACS getEsitiParticelle(@PathVariable Long idIstruttoria) throws Exception {
		return datiASDatiParticella.recupera(idIstruttoria);
	}

	
	@GetMapping("/{idIstruttoria}" + DATI_ISTRUTTORE)
	@ApiOperation("Recupero dati istruttore relativi a istruttoria superficie")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria()")
	public DatiIstruttoriaACS  getDatiIstruttore(@PathVariable(value = "idIstruttoria") Long idIstruttoria) throws Exception {
		return datiIstruttoreService.getDatiIstruttoreSuperficie(idIstruttoria);
	}
	
	@PostMapping("/{idIstruttoria}" + DATI_ISTRUTTORE)
	@ApiOperation("Salvataggio dati dell'istruttore relativi all'istruttoria superficie")
	@PreAuthorize("@abilitazioniPACComponent.checkVisualizzaIstruttoria() && @abilitazioniPACComponent.checkEditaIstruttoria() && @permessiModificaDU.checkPermessiIstruttoria(#idIstruttoria)")
	public DatiIstruttoriaACS saveOrUpdateDatiIstruttore(@PathVariable(value = "idIstruttoria") Long idIstruttoria,
			@RequestBody DatiIstruttoriaACS datiIstruttoriaAcs) throws Exception {
		return datiIstruttoreService.saveOrUpdateDatiIstruttore(idIstruttoria, datiIstruttoriaAcs);
	}

	@ApiOperation("Recupera i dati delle superfici impegnate per ogni intervento ACS")
	@GetMapping("{annoCampagna}/superficie-intervento")
	public List<InterventoSuperficieDto> getTotaleSuperficiePerIntervento(@PathVariable final Integer annoCampagna) {
		return accoppiatoSuperficieService.getTotaleSuperficiePerIntervento(annoCampagna);
	}

	protected String buildStringaEsito(EsitoControllo esito) {
		if (esito.getTipoControllo().equals(TipoControllo.importoMinimoAntimafia) ||
				esito.getTipoControllo().equals(TipoControllo.importoMinimoPagamento)) // Valori non-boolean
			return esito.getTipoControllo().toString().concat("_").concat(esito.getValString());
		String esitoCodificato = esito.getTipoControllo().toString();
		if (esito.getTipoControllo().equals(TipoControllo.BRIDUSDC022_idDomandaCampione))
			esitoCodificato = esitoCodificato.concat("Acs");
		if (esito.getEsito() == null) {
			return esitoCodificato.concat("_NA");
		} else if (esito.getEsito()) {
			return esitoCodificato.concat("_SI");
		} else {
			return esitoCodificato.concat("_NO");
		}
	}

	private ControlliSostegno loadEsitiControlliSostegno(final Long idIstruttoria) {
		// 1) get istruttoria IstruttoriaModel
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).orElseThrow(
				() -> new EntityNotFoundException(String.valueOf(idIstruttoria)));
		if (istruttoria.getTransizioni().isEmpty()) return null;
		TransizioneIstruttoriaModel lastTransizioneIstruttoriaModel = istruttoria.getTransizioni().stream().filter(Objects::nonNull).max(
				(trans1, trans2) -> Long.compare(trans1.getId(), trans2.getId())).get();

		List<DatiSintesi> datiSintesi = lastTransizioneIstruttoriaModel.getPassiTransizione().stream()
				.map(item -> CustomConverters.jsonConvert(item.getDatiSintesiLavorazione(), DatiSintesi.class))
				.collect(Collectors.toList());
		List<EsitoControllo> esitiControlli = datiSintesi.stream()
				.flatMap(item -> item.getEsitiControlli().stream())
				.collect(Collectors.toList());

		ControlliSostegno controlliSostegno = new ControlliSostegno();
		// filtro messaggi info
		controlliSostegno.setInfos(
				esitiControlli.stream()
						.filter(esito -> LivelloControllo.INFO.equals(esito.getLivelloControllo()))
						.map(this::buildStringaEsito)
						.collect(Collectors.toList()));
		// filtro messaggi error
		controlliSostegno.setErrors(
				esitiControlli.stream()
						.filter(esito -> LivelloControllo.ERROR.equals(esito.getLivelloControllo()))
						.map(this::buildStringaEsito)
						.collect(Collectors.toList()));
		// filtro messaggi success
		controlliSostegno.setSuccesses(
				esitiControlli.stream()
						.filter(esito -> LivelloControllo.SUCCESS.equals(esito.getLivelloControllo()))
						.map(this::buildStringaEsito)
						.collect(Collectors.toList()));
		// filtro messaggi warning
		controlliSostegno.setWarnings(
				esitiControlli.stream()
						.filter(esito -> LivelloControllo.WARNING.equals(esito.getLivelloControllo()))
						.map(this::buildStringaEsito)
						.collect(Collectors.toList()));
		return controlliSostegno;
	}
}

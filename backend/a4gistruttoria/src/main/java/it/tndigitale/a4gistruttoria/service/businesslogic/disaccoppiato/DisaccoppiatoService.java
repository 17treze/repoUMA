package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import it.tndigitale.a4gistruttoria.dto.domandaunica.InformazioniDisaccoppiatoDomanda;
import it.tndigitale.a4gistruttoria.repository.dao.CampioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.CampioneModel;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.Quadro;
import it.tndigitale.a4gistruttoria.util.DichiarazioniDu;
import it.tndigitale.a4gistruttoria.repository.model.AmbitoCampione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisaccoppiatoService {

	@Autowired
	private DomandaUnicaDao daoDomanda;
	@Autowired
	private CampioneDao daoCampione;
	
	public static final String CODICE_GIOVANE = "DUGA02";
	
	private final List<DichiarazioniDu> listaDichiarazioniRiserva = 
			Arrays.asList(DichiarazioniDu.values()).stream()
				.filter(tipoDichiarazione -> tipoDichiarazione.name().equals(tipoDichiarazione.getGruppo()))
				.collect(Collectors.toList());
 
	
	public InformazioniDisaccoppiatoDomanda getInformazioniDisaccoppiatoDomanda(Long idDomanda) {
		DomandaUnicaModel domanda = daoDomanda.getOne(idDomanda);
		InformazioniDisaccoppiatoDomanda result = new InformazioniDisaccoppiatoDomanda();
		result.setCampione(isCampione(domanda));
		result.setPascolo(isPascoliImpegnati(domanda));
		result.setGiovane(isGiovane(domanda));
		result.setRiserva(getRiserva(domanda));
		return result;		
	}
	
	public String getRiserva(DomandaUnicaModel domanda) {
		return domanda.getDichiarazioni().stream()
			// .filter(dich -> "DICH_RISERVA_NAZIONALE".equalsIgnoreCase(dich.getA4gdTipoDichiarazioneDu().getIdentificativo()))
			.filter(dich -> Quadro.RISERVA_NAZIONALE.equals(dich.getQuadro()))
			.filter(d -> BigDecimal.ONE.equals(d.getValoreBool()))
			.filter(di -> Arrays.asList(DichiarazioniDu.values())
					.stream().map(ddu -> ddu.name())
					.anyMatch(ddu -> ddu.equals(di.getCodice())))
			.map(di -> DichiarazioniDu.valueOf(di.getCodice()))
			.filter(dichiarazione -> listaDichiarazioniRiserva.contains(dichiarazione))
			.map(dich -> dich.getDescrizione())
			.findAny().orElse(null);
	}
	
	public boolean isPascoliImpegnati(DomandaUnicaModel domanda) {
		return Optional.ofNullable(domanda.getA4gtPascoloParticellas())
				.map(pascoli -> !pascoli.isEmpty())
				.orElse(false);
	}
	
	public boolean isCampione(DomandaUnicaModel domanda) {
		CampioneModel campione = daoCampione.findByCuaaAndAmbitoCampioneAndAnnoCampagna(
				domanda.getCuaaIntestatario(),
				AmbitoCampione.SUPERFICIE,
				domanda.getCampagna());		
		return Optional.ofNullable(campione).map(c -> true).orElse(false);
	}

	public boolean isGiovane(DomandaUnicaModel domanda) {
		return domanda.getDichiarazioni().stream()
				.anyMatch(dich -> CODICE_GIOVANE.equalsIgnoreCase(dich.getCodice()));
	}
}

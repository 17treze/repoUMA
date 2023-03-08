package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatiSintesi extends DatiCalcoli {
	private List<EsitoControllo> esitiControlli;
	private List<VariabileCalcolo> variabiliParticellaColtura;

	public DatiSintesi() {
		super();
		esitiControlli = new ArrayList<>();
	}

	public List<EsitoControllo> getEsitiControlli() {
		return esitiControlli;
	}

	public void setEsitiControlli(List<EsitoControllo> esitiControlli) {
		this.esitiControlli = esitiControlli;
	}

	public List<VariabileCalcolo> getVariabiliParticellaColtura() {
		return variabiliParticellaColtura;
	}

	public List<VariabileCalcolo> recuperaVariabiliParticellaColturaDaStampare() {
		if (variabiliParticellaColtura != null)
			return variabiliParticellaColtura.stream().filter(x -> x.getTipoVariabile().getStampa().equals(true)).collect(Collectors.toList());
		else
			return null;
	}

	public void setVariabiliParticellaColtura(List<VariabileCalcolo> variabiliParticellaColtura) {
		this.variabiliParticellaColtura = variabiliParticellaColtura;
	}

}

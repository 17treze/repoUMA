package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.util.ArrayList;
import java.util.List;

public class DebitoBuilder {
	
	private DebitoBuilder() {}
	
	public static List<Debito> from(List<it.tndigitale.a4g.soc.client.model.Debito> debiti) {
		// CI-02-01-02-03-01 - recupero debiti recuperati dal soc
		List<Debito> debitiOutput = new ArrayList<>();
		if (debiti == null) return debitiOutput;
		debiti.stream().forEach(debito -> {
			Debito debitoOutput = new Debito();
			debitoOutput.setImporto(debito.getImporto());
			debitoOutput.setDescrizione(debito.getDescrizioneCapitolo());
			debitiOutput.add(debitoOutput);
		});
		return debitiOutput;
	}

}

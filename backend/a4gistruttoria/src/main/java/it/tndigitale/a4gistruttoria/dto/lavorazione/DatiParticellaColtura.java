package it.tndigitale.a4gistruttoria.dto.lavorazione;

import it.tndigitale.a4gistruttoria.dto.Particella;

public class DatiParticellaColtura {

	private Particella infoParticella;
	private String codColtura;
	private VariabiliParticellaColtura variabiliCalcoloParticella;

	public DatiParticellaColtura() {
		this.infoParticella = new Particella();
		this.variabiliCalcoloParticella = new VariabiliParticellaColtura();
	}

	public Particella getInfoParticella() {
		return infoParticella;
	}

	public void setInfoParticella(Particella infoParticella) {
		this.infoParticella = infoParticella;
	}

	public String getCodColtura() {
		return codColtura;
	}

	public void setCodColtura(String codColtura) {
		this.codColtura = codColtura;
	}

	public VariabiliParticellaColtura getVariabiliCalcoloParticella() {
		return variabiliCalcoloParticella;
	}

	public void setVariabiliCalcoloParticella(VariabiliParticellaColtura variabiliCalcoloParticella) {
		this.variabiliCalcoloParticella = variabiliCalcoloParticella;
	}

}

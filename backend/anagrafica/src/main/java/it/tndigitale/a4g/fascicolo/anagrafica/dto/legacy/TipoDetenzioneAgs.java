package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy;

public enum TipoDetenzioneAgs {
	DELEGA("DEL"), 
	MANDATO("MAN");

	private TipoDetenzioneAgs(String nome) {
		this.nome = nome;
	}

	private final String nome;

	public String getNome() {
		return nome;
	}
}

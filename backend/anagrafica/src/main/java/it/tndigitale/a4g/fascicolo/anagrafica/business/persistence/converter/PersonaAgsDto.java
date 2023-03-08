package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.converter;

@Deprecated
public class PersonaAgsDto {
	
	private String cuaa;
	private String ragioneSociale;
	private String nome;
	private String cognome;
	
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

}

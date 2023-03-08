package it.tndigitale.a4g.fascicolo.territorio.dto;

public class ConduzioneDto {

	private TitoloConduzione titolo;
	private Integer codiceAtto;
	private String descrizioneAtto;

	public TitoloConduzione getTitolo() {
		return titolo;
	}
	public ConduzioneDto setTitolo(TitoloConduzione titolo) {
		this.titolo = titolo;
		return this;
	}
	public Integer getCodiceAtto() {
		return codiceAtto;
	}
	public ConduzioneDto setCodiceAtto(Integer numeroAtto) {
		this.codiceAtto = numeroAtto;
		return this;
	}
	public String getDescrizioneAtto() {
		return descrizioneAtto;
	}
	public ConduzioneDto setDescrizioneAtto(String descrizioneAtto) {
		this.descrizioneAtto = descrizioneAtto;
		return this;
	}
}

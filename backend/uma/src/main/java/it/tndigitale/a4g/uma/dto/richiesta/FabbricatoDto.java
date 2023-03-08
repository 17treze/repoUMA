package it.tndigitale.a4g.uma.dto.richiesta;

public class FabbricatoDto {

	private Long id;
	private String comune;
	private String provincia;
	private String siglaProvincia;
	private String particella;
	private String subalterno;
	private Integer volume;

	public Long getId() {
		return id;
	}
	public FabbricatoDto setId(Long id) {
		this.id = id;
		return this;
	}
	public String getComune() {
		return comune;
	}
	public FabbricatoDto setComune(String comune) {
		this.comune = comune;
		return this;
	}
	public String getProvincia() {
		return provincia;
	}
	public FabbricatoDto setProvincia(String provincia) {
		this.provincia = provincia;
		return this;
	}
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public FabbricatoDto setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
		return this;
	}
	public String getParticella() {
		return particella;
	}
	public FabbricatoDto setParticella(String particella) {
		this.particella = particella;
		return this;
	}
	public String getSubalterno() {
		return subalterno;
	}
	public FabbricatoDto setSubalterno(String subalterno) {
		this.subalterno = subalterno;
		return this;
	}
	public Integer getVolume() {
		return volume;
	}
	public FabbricatoDto setVolume(Integer volume) {
		this.volume = volume;
		return this;
	}
}

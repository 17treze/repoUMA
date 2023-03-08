package it.tndigitale.a4g.fascicolo.territorio.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Filtro per ricercare un piano colturale")
public class PianoColturaleFilter {

	private String cuaa;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(required = true, example = "2020-12-31T23:59" , description = "utilizzare il formato yyyy-MM-dd'T'HH:mm")
	private LocalDateTime data;

	@Schema(required = false, description = "Opzionale - Default TN e BZ")
	List<String> province;

	private Integer codiceAtto;

	@Schema(enumAsRef = true)
	private TitoloConduzione titolo;

	public String getCuaa() {
		return cuaa;
	}
	public PianoColturaleFilter setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public LocalDateTime getData() {
		return data;
	}
	public PianoColturaleFilter setData(LocalDateTime data) {
		this.data = data;
		return this;
	}
	public List<String> getProvince() {
		return province;
	}
	public PianoColturaleFilter setProvince(List<String> province) {
		this.province = province;
		return this;
	}
	public Integer getCodiceAtto() {
		return codiceAtto;
	}
	public PianoColturaleFilter setCodiceAtto(Integer codiceAtto) {
		this.codiceAtto = codiceAtto;
		return this;
	}
	public TitoloConduzione getTitolo() {
		return titolo;
	}
	public PianoColturaleFilter setTitolo(TitoloConduzione titolo) {
		this.titolo = titolo;
		return this;
	}

}

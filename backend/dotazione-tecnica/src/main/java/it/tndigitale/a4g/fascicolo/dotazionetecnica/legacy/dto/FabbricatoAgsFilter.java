package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtro per ricercare un fabbricato")
public class FabbricatoAgsFilter {
	
	String cuaa;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(required = true, example = "2020-12-31T23:59" , description = "utilizzare il formato yyyy-MM-dd'T'HH:mm")
	private LocalDateTime data;
	
	@Schema(required = false , description = "Opzionale - Valori di default TN e BZ")
	List<String> province;
	
	@Parameter(name = "titoliConduzione", required = false , description = "Filtro fabbricati in base al titolo di conduzione")
	@Schema(enumAsRef = true)
	List<TitoloConduzioneAgs> titoliConduzione;
	
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	public List<TitoloConduzioneAgs> getTitoliConduzione() {
		return titoliConduzione;
	}
	public void setTitoliConduzione(List<TitoloConduzioneAgs> titoliConduzione) {
		this.titoliConduzione = titoliConduzione;
	}
	public List<String> getProvince() {
		return province;
	}
	public void setProvince(List<String> province) {
		this.province = province;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
}

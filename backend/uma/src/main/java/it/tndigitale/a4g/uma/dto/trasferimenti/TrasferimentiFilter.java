package it.tndigitale.a4g.uma.dto.trasferimenti;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public class TrasferimentiFilter {

	// almeno uno tra cuaa mittente e destinatario risulta obbligatorio
	private String cuaaMittente;
	private String cuaaDestinatario;

	@Schema(required = true)
	private Long campagna;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Schema(required = false, example = "2021-11-01" , description = "utilizzare il formato yyyy-MM-dd")
	private LocalDate data;

	public String getCuaaMittente() {
		return cuaaMittente;
	}
	public TrasferimentiFilter setCuaaMittente(String cuaaMittente) {
		this.cuaaMittente = cuaaMittente;
		return this;
	}
	public String getCuaaDestinatario() {
		return cuaaDestinatario;
	}
	public TrasferimentiFilter setCuaaDestinatario(String cuaaDestinatario) {
		this.cuaaDestinatario = cuaaDestinatario;
		return this;
	}
	public Long getCampagna() {
		return campagna;
	}
	public TrasferimentiFilter setCampagna(Long campagna) {
		this.campagna = campagna;
		return this;
	}
	public LocalDate getData() {
		return data;
	}
	public TrasferimentiFilter setData(LocalDate data) {
		this.data = data;
		return this;
	}
}

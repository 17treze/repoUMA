package it.tndigitale.a4g.uma.dto.trasferimenti;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.AziendaDto;

@JsonInclude(Include.NON_NULL)
public class TrasferimentoDto {

	private Long id;
	private LocalDateTime data;
	private CarburanteDto carburante;

	private AziendaDto mittente;
	private AziendaDto destinatario;

	public Long getId() {
		return id;
	}
	public TrasferimentoDto setId(Long id) {
		this.id = id;
		return this;
	}
	public LocalDateTime getData() {
		return data;
	}
	public TrasferimentoDto setData(LocalDateTime data) {
		this.data = data;
		return this;
	}
	public CarburanteDto getCarburante() {
		return carburante;
	}
	public TrasferimentoDto setCarburante(CarburanteDto carburante) {
		this.carburante = carburante;
		return this;
	}
	public AziendaDto getMittente() {
		return mittente;
	}
	public TrasferimentoDto setMittente(AziendaDto mittente) {
		this.mittente = mittente;
		return this;
	}
	public AziendaDto getDestinatario() {
		return destinatario;
	}
	public TrasferimentoDto setDestinatario(AziendaDto destinatario) {
		this.destinatario = destinatario;
		return this;
	}
}

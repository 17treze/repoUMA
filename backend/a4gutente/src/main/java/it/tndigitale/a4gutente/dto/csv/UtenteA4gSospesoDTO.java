package it.tndigitale.a4gutente.dto.csv;

import java.time.LocalDateTime;

public interface UtenteA4gSospesoDTO {
	
	String getUserName();
	LocalDateTime getDataSospensione();
	Boolean getUtenteSospeso();
	String getMotivoSospensione();

}

package it.tndigitale.a4gutente.dto.csv;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

public interface UtenteA4gDTO {
	
	String getNome();
	String getCognome();
	String getCodiceFiscale();
	String getUserName();
	LocalDateTime getDataAttivazione();
	String getProfilo();
	String getDescProfilo();

	@Value("#{@utenteA4gMapperUtility.buildEnteApparteneza(target.responsabilita, target.caa, target.denomDistributore, target.comuneDistributore)}")
	String getEnteAppartenenza();
	

}

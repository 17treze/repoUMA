package it.tndigitale.a4gistruttoria.repository.dao;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiLavorazione;

@Repository
public interface DatiLavorazioneDao extends JpaRepository<A4gtDatiLavorazione, Long> {

	A4gtDatiLavorazione findByDomandaUnicaModel_numeroDomanda(BigDecimal numeroDomanda);
	
}

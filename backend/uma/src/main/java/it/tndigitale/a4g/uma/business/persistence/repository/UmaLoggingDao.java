package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.LoggingModel;

@Repository
public interface UmaLoggingDao extends JpaRepository<LoggingModel, Long> {
	List<LoggingModel> findByTabellaAndTipoEventoAndIdEntita(String tabella, String tipoEvento, Long idEntita);
}

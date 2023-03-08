/**
 * 
 */
package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.tndigitale.a4gistruttoria.repository.model.A4gtSogliaAcquisizione;

/**
 * @author a.pasca
 *
 */
public interface SogliaAcquisizioneDao extends JpaRepository<A4gtSogliaAcquisizione, Long> {
	@Query("SELECT a FROM A4gtSogliaAcquisizione a WHERE a.settore = :settore AND :sysdate BETWEEN a.dataInizioApplicazione AND a.dataFineApplicazione")
	A4gtSogliaAcquisizione findBySettoreAndDate(@Param("settore") String settore, @Param("sysdate") Date data);
}

/**
 * 
 */
package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.RegistroAlpeggioModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author a.pasca
 *
 */
public interface RegAlpeggioDao extends JpaRepository<RegistroAlpeggioModel, Long> {

}

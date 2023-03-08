/**
 * 
 */
package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.RegistroAnalisiLatteModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author a.pasca
 *
 */
public interface AnalisiLatteDao extends JpaRepository<RegistroAnalisiLatteModel, Long> {

}

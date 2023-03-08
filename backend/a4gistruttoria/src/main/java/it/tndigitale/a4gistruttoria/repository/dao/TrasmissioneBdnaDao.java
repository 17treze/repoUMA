/**
 * 
 */
package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.tndigitale.a4gistruttoria.repository.model.A4gtTrasmissioneBdna;

/**
 * @author B.Conetta
 *
 */
public interface TrasmissioneBdnaDao extends JpaRepository<A4gtTrasmissioneBdna, Long> , JpaSpecificationExecutor<A4gtTrasmissioneBdna> {

}

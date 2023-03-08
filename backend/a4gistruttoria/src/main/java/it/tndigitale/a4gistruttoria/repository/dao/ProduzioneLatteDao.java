/**
 * 
 */
package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tndigitale.a4gistruttoria.repository.model.RegistroProduzioneLatteModel;

/**
 * @author a.pasca
 *
 */
public interface ProduzioneLatteDao extends JpaRepository<RegistroProduzioneLatteModel, Long> {

}

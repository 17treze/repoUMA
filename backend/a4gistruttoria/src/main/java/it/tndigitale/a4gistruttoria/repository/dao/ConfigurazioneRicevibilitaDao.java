package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneRicevibilitaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurazioneRicevibilitaDao extends JpaRepository<ConfigurazioneRicevibilitaModel, Long> {

	Optional<ConfigurazioneRicevibilitaModel> findByCampagna(Integer campagna);

}

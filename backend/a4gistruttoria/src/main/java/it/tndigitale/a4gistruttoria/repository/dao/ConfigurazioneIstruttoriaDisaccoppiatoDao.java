package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneIstruttoriaDisaccoppiatoModel;

@Repository
public interface ConfigurazioneIstruttoriaDisaccoppiatoDao extends JpaRepository<ConfigurazioneIstruttoriaDisaccoppiatoModel, Long> {

	Optional<ConfigurazioneIstruttoriaDisaccoppiatoModel> findByCampagna(Integer annoCampagna);

}

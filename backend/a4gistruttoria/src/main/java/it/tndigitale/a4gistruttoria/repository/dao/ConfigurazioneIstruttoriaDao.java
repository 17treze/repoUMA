package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneIstruttoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurazioneIstruttoriaDao extends JpaRepository<ConfigurazioneIstruttoriaModel, Long> {

    Optional<ConfigurazioneIstruttoriaModel> findByCampagna(Integer annoCampagna);

}

package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.ConfigurazioneModel;

@Repository
public interface ConfigurazioneDao extends JpaRepository<ConfigurazioneModel, Long> {
	public Optional<ConfigurazioneModel> findByCampagna(int campagna);
}

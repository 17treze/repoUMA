package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;

@Repository
public interface SportelloDao extends JpaRepository<SportelloModel, Long> {
	
	public List<SportelloModel> findByIdentificativoIn(@Param("identificativo")List<Long> identificativi);

	public Optional<SportelloModel> findByIdentificativo(@Param("identificativo")Long identificativo);
	
	public List<SportelloModel> findByCentroAssistenzaAgricola_id(@Param("id")Long id);
}


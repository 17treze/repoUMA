package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.EsitoMantenimentoPascolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsitoMantenimentoPascoloDao extends JpaRepository<EsitoMantenimentoPascolo, Long> {
	List<EsitoMantenimentoPascolo> findByIstruttoria(IstruttoriaModel istruttoria);

}

package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.ImportoUnitarioInterventoModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportoUnitarioInterventoDao extends JpaRepository<ImportoUnitarioInterventoModel, Long> {

	public ImportoUnitarioInterventoModel findByCampagnaAndIntervento_identificativoIntervento(
			Integer campagna, CodiceInterventoAgs identificativoIntervento);
	
	@Query(
			"FROM ImportoUnitarioInterventoModel AS ium "
		+ 	"WHERE ium.campagna = :campagna "
		+ 	"and ium.intervento.sostegno = :sostegno"
	)
	public List<ImportoUnitarioInterventoModel> findByCampagnaAndSostegno(
			@Param("campagna") Integer campagna, @Param("sostegno") Sostegno sostegno);
}

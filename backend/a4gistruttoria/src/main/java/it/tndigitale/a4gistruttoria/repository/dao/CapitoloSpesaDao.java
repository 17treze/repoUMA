/**
 * 
 */
package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.tndigitale.a4gistruttoria.repository.model.CapitoloSpesaModel;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.util.TipoCapitoloSpesa;

/**
 * @author a.pasca
 *
 */
public interface CapitoloSpesaDao extends JpaRepository<CapitoloSpesaModel, Long> {

	@Query("SELECT cap FROM CapitoloSpesaModel cap " +
			"WHERE cap.campagna = :campagna and cap.tipo = :#{#tipo.tipoCapitoloSpesa} and cap.intervento.identificativoIntervento = :intervento")
	public CapitoloSpesaModel findByAnnoTipoIntervento(@Param("campagna") Integer campagna, 
			@Param("tipo") TipoCapitoloSpesa tipo,
			@Param("intervento") CodiceInterventoAgs intervento);
}

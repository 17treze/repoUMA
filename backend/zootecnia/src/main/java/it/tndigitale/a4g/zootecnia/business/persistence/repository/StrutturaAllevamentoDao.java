package it.tndigitale.a4g.zootecnia.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.StrutturaAllevamentoModel;

@Repository
public interface StrutturaAllevamentoDao extends JpaRepository<StrutturaAllevamentoModel, EntitaDominioFascicoloId> {
	
	Optional<StrutturaAllevamentoModel> findByIdentificativoAndIdValidazioneAndCuaa(String identificativo, Integer idValidazione, String cuaa);
	List<StrutturaAllevamentoModel> findByIdValidazioneAndCuaa(Integer idValidazione, String cuaa);
}

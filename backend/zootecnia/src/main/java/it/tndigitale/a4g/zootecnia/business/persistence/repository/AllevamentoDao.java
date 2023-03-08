package it.tndigitale.a4g.zootecnia.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.AllevamentoModel;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.FascicoloModel;

@Repository
public interface AllevamentoDao extends JpaRepository<AllevamentoModel, EntitaDominioFascicoloId> {
	long deleteByFascicolo(FascicoloModel fascicolo);
	List<AllevamentoModel> findByFascicolo_CuaaAndFascicolo_IdValidazione(String cuaa, Integer idValidazione);
}

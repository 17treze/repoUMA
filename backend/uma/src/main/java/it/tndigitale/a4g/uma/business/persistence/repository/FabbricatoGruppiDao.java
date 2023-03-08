package it.tndigitale.a4g.uma.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoGruppiModel;

@Repository
public interface FabbricatoGruppiDao extends JpaRepository<FabbricatoGruppiModel, Long> {
	public FabbricatoGruppiModel findByCodiceFabbricato(String codiceFabbricato);
}

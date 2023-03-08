package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ErroreRicevibilitaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErroreRicevibilitaDao extends JpaRepository<ErroreRicevibilitaModel, Long> {

	List<ErroreRicevibilitaModel> findByDomandaUnicaModel(DomandaUnicaModel idDomanda);
}

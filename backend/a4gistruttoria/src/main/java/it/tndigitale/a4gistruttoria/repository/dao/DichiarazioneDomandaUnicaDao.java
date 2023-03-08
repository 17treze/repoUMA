package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.DichiarazioneDomandaUnicaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DichiarazioneDomandaUnicaDao extends JpaRepository<DichiarazioneDomandaUnicaModel, Long> {

}

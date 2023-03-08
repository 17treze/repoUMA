package it.tndigitale.a4gutente.repository.dao;

import it.tndigitale.a4gutente.repository.model.A4gtEnte;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IEnteDao extends IEntitaDominioRepository<A4gtEnte> {

    Optional<A4gtEnte> findByIdentificativo(Long identificativo);

}

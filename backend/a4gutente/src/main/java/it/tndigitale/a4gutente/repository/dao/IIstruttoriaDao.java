package it.tndigitale.a4gutente.repository.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;

@Repository
public interface IIstruttoriaDao  extends IEntitaDominioRepository<IstruttoriaEntita> {

    @Query("select i from IstruttoriaEntita i where i.domanda.id = (:idDomanda)")
    Optional<IstruttoriaEntita> findByIdDomanda(@Param("idDomanda") Long idDomanda);

    @Query("select i from IstruttoriaEntita i where i.utente.id = (:idUtente) order by i.dataTermineIstruttoria desc")
    List<IstruttoriaEntita> findIstruttorieByIdUtente(@Param("idUtente") Long idUtente);

    @Query("select i from IstruttoriaEntita i join  i.utente as u where u.identificativo = (:identificativo) order by i.dataTermineIstruttoria desc")
    List<IstruttoriaEntita> findSenzaDomandaXIdentificativoUtente(@Param("identificativo") String identificativo);

}

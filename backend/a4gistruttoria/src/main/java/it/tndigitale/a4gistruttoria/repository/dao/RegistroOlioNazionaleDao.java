package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.RegistroOlioNazionaleModel;

import java.util.Optional;

@Repository
public interface RegistroOlioNazionaleDao extends JpaRepository<RegistroOlioNazionaleModel, Long> {

    Optional<RegistroOlioNazionaleModel>
        findByCuaaIntestatarioAndInizioCampagnaLessThanEqualAndFineCampagnaGreaterThanEqual(
                String cuaaIntestatario, Integer inizioCampagna, Integer fineCampagna);

}

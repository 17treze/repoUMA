package it.tndigitale.a4gutente.service.loader;

import it.tndigitale.a4gutente.repository.dao.IEnteDao;
import it.tndigitale.a4gutente.repository.model.A4gtEnte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EnteLoader extends AbstractLoader {

    @Autowired
    private IEnteDao enteDao;

    public A4gtEnte load(Long idEnte) {
        checkId(idEnte);
        return enteDao.findById(idEnte)
                      .orElseThrow(() -> new ValidationException("Ente con identificativo " + idEnte + " non trovato"));
    }

    public List<A4gtEnte> load(List<Long> idsEnti) {
        return Optional.ofNullable(idsEnti)
                       .orElse(new ArrayList<>())
                       .stream()
                       .map(idEnte -> load(idEnte))
                       .collect(Collectors.toList());
    }

}

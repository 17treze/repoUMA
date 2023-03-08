package it.tndigitale.a4gutente.service.loader;

import it.tndigitale.a4gutente.repository.dao.IProfiloDao;
import it.tndigitale.a4gutente.repository.model.A4gtProfilo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProfiloLoader extends AbstractLoader {

    @Autowired
    private IProfiloDao profiloDao;

    public A4gtProfilo load(Long idProfilo) {
        checkId(idProfilo);
        return profiloDao.findById(idProfilo)
                         .orElseThrow(() -> new ValidationException("Profilo con identificativo " + idProfilo + " non trovato"));
    }

    public List<A4gtProfilo> load(List<Long> idsProfili) {
        return Optional.ofNullable(idsProfili)
                       .orElse(new ArrayList<>())
                       .stream()
                       .map(id -> load(id))
                       .collect(Collectors.toList());

    }

}

package it.tndigitale.a4gutente.repository.dao.custom;

import it.tndigitale.a4g.framework.support.SQLSupport;
import it.tndigitale.a4g.framework.support.StringSupport;
import it.tndigitale.a4gutente.dto.UtentiFilter;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static it.tndigitale.a4gutente.utility.ListSupport.emptyIfNull;
import static java.util.Collections.emptyList;

@Repository
public class UtenteCompletoCustomDao extends BaseCustomDao {

    public List<A4gtUtente> ricerca(UtentiFilter filter) {
        if (filter == null) {
            return emptyList();
        }
        String jpqlQuery = "select distinct u,p from A4gtUtente u left join PersonaEntita p on u.codiceFiscale = p.codiceFiscale ";
        if (StringSupport.isNotEmpty(filter.getCodiceFiscale())) {
            jpqlQuery = SQLSupport.addWhere(jpqlQuery);
            jpqlQuery = jpqlQuery + " upper(u.codiceFiscale) like '" + SQLSupport.upperLike(filter.getCodiceFiscale()) + "' ";
        }
        if (StringSupport.isNotEmpty(filter.getCognome())) {
            jpqlQuery = SQLSupport.addWhere(jpqlQuery);
            jpqlQuery = SQLSupport.addAnd(jpqlQuery);
            jpqlQuery = jpqlQuery + " upper(p.cognome) like '" + SQLSupport.upperLike(filter.getCognome()) + "' ";
        }
        if (StringSupport.isNotEmpty(filter.getNome())) {
            jpqlQuery = SQLSupport.addWhere(jpqlQuery);
            jpqlQuery = SQLSupport.addAnd(jpqlQuery);
            jpqlQuery = jpqlQuery + " upper(p.nome) like '" + SQLSupport.upperLike(filter.getNome()) + "' ";
        }
        List<Object[]> result =  executeJpaQuery(jpqlQuery, Object[].class);
        return from(result);
    }

    private List<A4gtUtente> from(List<Object[]> entities) {
        return emptyIfNull(entities).stream()
                                    .map(entity -> from(entity))
                                    .collect(Collectors.toList());
    }

    private A4gtUtente from(Object[] entity) {
        A4gtUtente utente = (A4gtUtente) entity[0];
        PersonaEntita personaEntita = (entity[1]!=null)? (PersonaEntita) entity[1] :null;
        return utente.setPersona(personaEntita);
    }

}

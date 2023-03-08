package it.tndigitale.a4g.srt.repository.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.srt.dto.Utente;
import it.tndigitale.a4g.srt.dto.UtenteFilter;

@Repository
public class UtentiDaoImpl extends NamedParameterJdbcDaoSupport implements UtentiDao {

	private static final Logger logger = LoggerFactory.getLogger(UtentiDaoImpl.class);

	private static final String SQL_FIND_BY_FILTER = "SELECT P.NOME AS nome, P.COGNOME AS cognome, P.CODICE_FISCALE AS codiceFiscale, V.PROFILO AS ruolo, V.ENTE AS ente " + 
			"FROM VUTENTI V " + 
			"INNER JOIN PERSONA_FISICA P ON V.ID_PERSONA_FISICA = P.ID_PERSONA " + 
			"WHERE V.ATTIVO = 1 ";
	
	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public List<Utente> findByFilter(UtenteFilter filter) {
		try {
			logger.debug("findByFilter con filtro {}", filter);
	
			final MapSqlParameterSource params = new MapSqlParameterSource();
			StringBuilder sb = new StringBuilder(SQL_FIND_BY_FILTER);
			Optional<UtenteFilter> filterOpt = Optional.ofNullable(filter);
			sb.append(filterOpt
					.map(UtenteFilter::getCodiceFiscale)
					.filter(codiceFiscale -> !StringUtils.isEmpty(codiceFiscale))
					.map(codiceFiscale -> {
						params.addValue("codiceFiscale", codiceFiscale.toUpperCase() + "%");
						return " and UPPER(P.CODICE_FISCALE) like :codiceFiscale";
					})
					.orElse(""));
			sb.append(filterOpt
					.map(UtenteFilter::getNome)
					.filter(nome -> !StringUtils.isEmpty(nome))
					.map(nome -> {
						params.addValue("nome", "%".concat(nome.trim().toUpperCase()).concat("%"));
						return " and UPPER(P.NOME) like :nome";
					})
					.orElse(""));
			sb.append(filterOpt
					.map(UtenteFilter::getCognome)
					.filter(cognome -> !StringUtils.isEmpty(cognome))
					.map(cognome -> {
						params.addValue("cognome", "%".concat(cognome.trim().toUpperCase()).concat("%"));
						return " and UPPER(P.COGNOME) like :cognome";
					})
					.orElse(""));
			String sql = sb.toString();
			logger.debug("findByFilter sql: {}", sql);
			
			List<Utente> res = getNamedParameterJdbcTemplate().<Utente>query(sql, params,
						new BeanPropertyRowMapper<Utente>(Utente.class));
			return res;
		} catch (NullPointerException | NoResultException e) {
			return Collections.<Utente>emptyList();
		}
	}
}

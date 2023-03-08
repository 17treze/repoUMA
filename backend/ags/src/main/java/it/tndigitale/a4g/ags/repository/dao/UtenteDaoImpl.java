package it.tndigitale.a4g.ags.repository.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import it.tndigitale.a4g.ags.dto.Utente;
import it.tndigitale.a4g.ags.dto.UtenteFilter;


@Repository
public class UtenteDaoImpl extends NamedParameterJdbcDaoSupport implements UtenteDao {
	private static final Logger logger = LoggerFactory.getLogger(UtenteDaoImpl.class);

	private final String sqlRicercaUtente = "select u.USERID as utenza, u.DESCR as descrizione, up.content as cf from SSO_USERS u inner join SSO_USERS_PROPERTIES up on up.userid = u.userid and up.name = 'COD_FISCALE' where u.is_locked != 'Y' ";

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public List<Utente> getUtenti(String codiceFiscale) {

		logger.debug("getUtenti: {}", codiceFiscale);

		
		final Map<String, Object> params = new HashMap<String, Object>();
		Optional<String> filtroOp = 
				Optional.ofNullable(codiceFiscale)
					.filter(cf -> !StringUtils.isEmpty(cf))
					.map(cf -> {
						params.put("codiceFiscale", cf.trim().toUpperCase());
						return " and UPPER(up.content) = :codiceFiscale";						
					});
		

		try {
			String filter = filtroOp.orElseThrow(NoResultException::new);
			String sql = this.sqlRicercaUtente.concat(filter);
			logger.debug("getUtenti: eseguo la query {}", sql);
			List<Utente> utenteDTOList = getNamedParameterJdbcTemplate().<Utente>query(sql, params,
					new BeanPropertyRowMapper<Utente>(Utente.class));
			return utenteDTOList;
		} catch (NoResultException e) {
			logger.debug("getUtenti: nessun risultato trovato per codice fiscale {}", codiceFiscale);
			return Collections.<Utente>emptyList();
		}
	}
	
	public List<Utente> getUtenti(UtenteFilter filtri) {
		try {
			final MapSqlParameterSource params = new MapSqlParameterSource();
			StringBuilder sb = new StringBuilder();
			sb.append("select u.USERID as utenza, u.DESCR as descrizione, upcf.content as cf, upn.content as nome, upc.content as cognome ");
			sb.append("from SSO_USERS u ");
			sb.append("join SSO_USERS_PROPERTIES upcf on upcf.userid = u.userid and upcf.name = 'COD_FISCALE' ");
			sb.append("join SSO_USERS_PROPERTIES upn on upn.userid = u.userid and upn.name = 'NOME' ");
			sb.append("join SSO_USERS_PROPERTIES upc on upc.userid = u.userid and upc.name = 'COGNOME' ");
			sb.append("where u.is_locked != 'Y'");
			Optional<UtenteFilter> filtroOp = Optional.of(filtri);
			sb.append(filtroOp
						.map(UtenteFilter::getCodiceFiscale)
						.filter(cf -> !StringUtils.isEmpty(cf))
						.map(cf -> {
							params.addValue("cf", cf.toUpperCase() + "%");
							return " and UPPER(upcf.content) like :cf";
						})
						.orElse(""));
			sb.append(filtroOp
						.map(UtenteFilter::getNome)
						.filter(n -> !StringUtils.isEmpty(n))
						.map(n -> {
							params.addValue("nome", "%".concat(n.trim().toUpperCase()).concat("%"));
							return " and UPPER(upn.content) like :nome";
						})
						.orElse(""));
			sb.append(filtroOp
						.map(UtenteFilter::getCognome)
						.filter(c -> !StringUtils.isEmpty(c))
						.map(c -> {
							params.addValue("cognome", "%".concat(c.trim().toUpperCase()).concat("%"));
							return " and UPPER(upc.content) like :cognome";
						})
						.orElse(""));
			String sql = sb.toString();
			logger.debug("getUtenti: eseguo la query {}", sql);
			return getNamedParameterJdbcTemplate().<Utente>query(sql, params,
					new BeanPropertyRowMapper<Utente>(Utente.class));
		} catch (NullPointerException | NoResultException e) {
			return Collections.<Utente>emptyList();
		}
	}

}

package it.tndigitale.a4g.framework.ext.validazione.repository.handler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.repository.handler.LoggingUser;

@Repository
public class LoggingValidazioneDao extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    protected Long getGeneratedId() {
        return getJdbcTemplate().queryForObject("SELECT NXTNBR.NEXTVAL FROM DUAL", null, Long.class);
    }

    public void save(LoggingValidazioneUser dati) throws Exception {
        Long pkid = getGeneratedId();

        List<Object> params = new ArrayList<>();

        params.add(pkid);
        params.add(dati.getUtente());
        params.add(dati.getTipoEvento());
        params.add(dati.getTabella());
        params.add(dati.getIdEntita());
        params.add(Timestamp.valueOf(dati.getDtEvento()));
        params.add(dati.getIdValidazione());

        this.getJdbcTemplate().update(
        		"INSERT INTO A4GT_LOGGING_VALIDAZIONE (id, versione, utente, tipo_evento, tabella, id_entita, dt_evento, id_validazione) values (?, 0, ?, ?, ?, ?, ?, ?)",
        		params.toArray());
    }
}

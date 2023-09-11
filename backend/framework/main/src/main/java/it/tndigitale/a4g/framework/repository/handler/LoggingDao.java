package it.tndigitale.a4g.framework.repository.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoggingDao extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    protected Long getGeneratedId() {
        return getJdbcTemplate().queryForObject("SELECT SEQ_AGRI_UMAL_NXTNBR.NEXTVAL FROM DUAL", null, Long.class);
    }

    public void save(LoggingUser dati) throws Exception {
        Long pkid = getGeneratedId();

        List<Object> params = new ArrayList<>();

        params.add(pkid);
        params.add(dati.getUtente());
        params.add(dati.getTipoEvento());
        params.add(dati.getTabella());
        params.add(dati.getIdEntita());
        params.add(Timestamp.valueOf(dati.getDtEvento()));
       	this.getJdbcTemplate().update(
        		"INSERT INTO TAB_AGRI_UMAL_LOGGING (id, versione, utente, tipo_evento, tabella, id_entita, dt_evento) values (?, 0, ?, ?, ?, ?, ?)",
        		params.toArray());
    }
}

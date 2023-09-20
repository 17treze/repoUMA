package it.tndigitale.a4g.framework.event.store.repository;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.framework.event.store.EventStoredModel;

@Repository
public class EventStoreJdbcRepository extends JdbcDaoSupport {
    private static final String SQL_INSERT =
//    		"INSERT INTO TAB_AGRI_UMAL_EVENTSTORED (ID, VERSIONE, EVENTO, JSON_EVENT, NUMERO_RETRY, DATA_INSERIMENTO, ERRORE) VALUES(?, 0, ?, ?, ?, ?, ?)"; // [SPRING.2.2.0.RELEASE] funziona con generatedId forzato a String
            "INSERT INTO TAB_AGRI_UMAL_EVENTSTORED (ID, VERSIONE, EVENTO, JSON_EVENT, USER_NAME, NUMERO_RETRY, DATA_INSERIMENTO, ERRORE) VALUES(SEQ_AGRI_UMAL_NXTNBR.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID =
            "SELECT ID, VERSIONE, EVENTO, JSON_EVENT, USER_NAME, DATA_INSERIMENTO, NUMERO_RETRY, ERRORE FROM TAB_AGRI_UMAL_EVENTSTORED WHERE ID = ?";
    private static final String SQL_DELETE =
            "DELETE FROM TAB_AGRI_UMAL_EVENTSTORED WHERE ID = ?";
    private static final String SQL_SELECT_ALL =
            "SELECT ID, VERSIONE, EVENTO, JSON_EVENT, USER_NAME, DATA_INSERIMENTO, NUMERO_RETRY, ERRORE FROM TAB_AGRI_UMAL_EVENTSTORED";


    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

//    TODO  [SPRING.2.2.0.RELEASE]  evita errore di cast da "Oracle.ROWID to Number" 
//    private String getGeneratedId() {
//		return getJdbcTemplate().queryForObject("SELECT NXTNBR.NEXTVAL FROM DUAL", null, String.class);
//	}
    
    public void save(EventStoredModel eventStored){
//    	funziona con SPRING 2.0.3.RELEASE
        getJdbcTemplate().update(SQL_INSERT,
                0,
                eventStored.getEvent(),
                eventStored.getJsonEvent(),
                eventStored.getUserName(),
                eventStored.getNumberOfRetry(),
                Timestamp.valueOf(eventStored.getDate()),
                eventStored.getError());

//    	TODO [SPRING.2.2.0.RELEASE] [begin] cambiata la gestione dell'update. In questo modo viene ritornato correttamente
//    	l'id generato
//    	String pkid = getGeneratedId();
//    	List<Object> params = new ArrayList<Object>();
//		params.add(pkid);
//		params.add(eventStored.getEvent());
//		params.add(eventStored.getJsonEvent());
//		params.add(eventStored.getNumberOfRetry());
//		params.add(Timestamp.valueOf(eventStored.getDate()));
//		params.add(eventStored.getError());
//		this.getJdbcTemplate().update(
//				SQL_INSERT,
//				params.toArray());
//		eventStored.setId(Long.parseLong(pkid));
//		TODO [SPRING.2.2.0.RELEASE] [end]
    }

    public EventStoredModel findById(Long id) {
        return getJdbcTemplate().queryForObject(
                SQL_SELECT_BY_ID,
                new Object[]{id},
                (rs, c) -> {
                	EventStoredModel esm = new EventStoredModel()
                			.setError(rs.getString("ERRORE"))
                            .setEvent(rs.getString("EVENTO"))
                            .setJsonEvent(rs.getString("JSON_EVENT"))
                            .setUserName(rs.getString("USER_NAME"))
                            .setNumberOfRetry(rs.getInt("NUMERO_RETRY"))
                            .setDate(rs.getTimestamp("DATA_INSERIMENTO").toLocalDateTime());
                	esm.setId(rs.getLong("ID"));
                	esm.setVersion(rs.getInt("VERSIONE"));
                	return esm;
                }
        );
    }

    public void delete(Long id) {
        getJdbcTemplate().update(SQL_DELETE, id);
    }

    public List<EventStoredModel> findAll() {
        return getJdbcTemplate().query(
                SQL_SELECT_ALL,
                (rs, c) -> {
                	EventStoredModel esm = new EventStoredModel()
                			.setError(rs.getString("ERRORE"))
                            .setEvent(rs.getString("EVENTO"))
                            .setJsonEvent(rs.getString("JSON_EVENT"))
                            .setUserName(rs.getString("USER_NAME"))
                            .setNumberOfRetry(rs.getInt("NUMERO_RETRY"))
                            .setDate(rs.getTimestamp("DATA_INSERIMENTO").toLocalDateTime());
                	esm.setId(rs.getLong("ID"));
                	esm.setVersion(rs.getInt("VERSIONE"));
                	return esm;
		    }
		);
    }
}

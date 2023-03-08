package it.tndigitale.a4g.richiestamodificasuolo.config;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import org.geolatte.geom.codec.db.oracle.ConnectionFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.pool.HikariProxyConnection;

import oracle.jdbc.OracleConnection;

public class OracleConnectionFinder implements ConnectionFinder {

	private static final long serialVersionUID = 628613885663127582L;

	private static Logger log = LoggerFactory.getLogger(OracleConnectionFinder.class);
	
	public OracleConnectionFinder() {
		super();
	}

	@Override
	public Connection find(Connection connection) {
		log.debug("eseguo find su {}", connection);
		if (connection instanceof OracleConnection) {
			return connection;
		}
		Connection result = connection;
		if (connection instanceof HikariProxyConnection) {
			log.debug("Eseguo unwrap connection di hikariProxyConnection");
			HikariProxyConnection con = (HikariProxyConnection) connection;
			try {
				result = con.unwrap(OracleConnection.class);
			} catch (SQLException e) {
				log.error("Errore cercando di accedere alla connection oracle", e);
			}
			
		} else {
			try {
				log.debug("Tento di accedere al metodo getUnderlyingConnection (non posso usare class.forName perche la classe di jboss non e' visibile");
				log.debug("Eseguo unwrap connection di WrappedConnectionJDK8 di jboss");
				result = (OracleConnection)connection.getClass().getMethod("getUnderlyingConnection").invoke(connection);
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				log.warn("Errore cercando di accedere alla connection oracle tramite getUnderlyingConnection", e);
			}
		}
		log.debug("ritorno result {}", result);
		return result;
	}
}

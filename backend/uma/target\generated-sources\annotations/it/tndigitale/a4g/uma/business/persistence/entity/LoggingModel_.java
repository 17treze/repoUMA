package it.tndigitale.a4g.uma.business.persistence.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LoggingModel.class)
public abstract class LoggingModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<LoggingModel, String> utente;
	public static volatile SingularAttribute<LoggingModel, LocalDateTime> dtEvento;
	public static volatile SingularAttribute<LoggingModel, String> tipoEvento;
	public static volatile SingularAttribute<LoggingModel, Long> idEntita;
	public static volatile SingularAttribute<LoggingModel, String> tabella;

	public static final String UTENTE = "utente";
	public static final String DT_EVENTO = "dtEvento";
	public static final String TIPO_EVENTO = "tipoEvento";
	public static final String ID_ENTITA = "idEntita";
	public static final String TABELLA = "tabella";

}


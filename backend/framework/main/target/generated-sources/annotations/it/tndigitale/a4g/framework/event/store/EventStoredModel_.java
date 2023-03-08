package it.tndigitale.a4g.framework.event.store;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EventStoredModel.class)
public abstract class EventStoredModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<EventStoredModel, LocalDateTime> date;
	public static volatile SingularAttribute<EventStoredModel, Integer> numberOfRetry;
	public static volatile SingularAttribute<EventStoredModel, String> jsonEvent;
	public static volatile SingularAttribute<EventStoredModel, String> event;
	public static volatile SingularAttribute<EventStoredModel, String> userName;
	public static volatile SingularAttribute<EventStoredModel, String> error;

}


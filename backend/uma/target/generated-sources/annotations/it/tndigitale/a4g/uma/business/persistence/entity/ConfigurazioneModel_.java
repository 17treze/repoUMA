package it.tndigitale.a4g.uma.business.persistence.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ConfigurazioneModel.class)
public abstract class ConfigurazioneModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<ConfigurazioneModel, LocalDateTime> dataPrelievi;
	public static volatile SingularAttribute<ConfigurazioneModel, Integer> campagna;

	public static final String DATA_PRELIEVI = "dataPrelievi";
	public static final String CAMPAGNA = "campagna";

}


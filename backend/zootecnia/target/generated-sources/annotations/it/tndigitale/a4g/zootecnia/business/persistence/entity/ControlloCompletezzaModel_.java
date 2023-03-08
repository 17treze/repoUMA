package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ControlloCompletezzaModel.class)
public abstract class ControlloCompletezzaModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<ControlloCompletezzaModel, String> utente;
	public static volatile SingularAttribute<ControlloCompletezzaModel, Integer> esito;
	public static volatile SingularAttribute<ControlloCompletezzaModel, Integer> idControllo;
	public static volatile SingularAttribute<ControlloCompletezzaModel, String> tipoControllo;
	public static volatile SingularAttribute<ControlloCompletezzaModel, String> cuaa;
	public static volatile SingularAttribute<ControlloCompletezzaModel, LocalDateTime> dataEsecuzione;

	public static final String UTENTE = "utente";
	public static final String ESITO = "esito";
	public static final String ID_CONTROLLO = "idControllo";
	public static final String TIPO_CONTROLLO = "tipoControllo";
	public static final String CUAA = "cuaa";
	public static final String DATA_ESECUZIONE = "dataEsecuzione";

}


package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FascicoloModel.class)
public abstract class FascicoloModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile ListAttribute<FascicoloModel, AllevamentoModel> allevamenti;
	public static volatile SingularAttribute<FascicoloModel, LocalDateTime> dtAggiornamentoFontiEsterne;
	public static volatile SingularAttribute<FascicoloModel, String> cuaa;

	public static final String ALLEVAMENTI = "allevamenti";
	public static final String DT_AGGIORNAMENTO_FONTI_ESTERNE = "dtAggiornamentoFontiEsterne";
	public static final String CUAA = "cuaa";

}


package it.tndigitale.a4g.uma.business.persistence.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ConsuntivoConsumiModel.class)
public abstract class ConsuntivoConsumiModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<ConsuntivoConsumiModel, TipoConsuntivo> tipoConsuntivo;
	public static volatile SingularAttribute<ConsuntivoConsumiModel, TipoCarburanteConsuntivo> tipoCarburante;
	public static volatile SingularAttribute<ConsuntivoConsumiModel, MotivazioneConsuntivo> motivazione;
	public static volatile SingularAttribute<ConsuntivoConsumiModel, DichiarazioneConsumiModel> dichiarazioneConsumi;
	public static volatile SingularAttribute<ConsuntivoConsumiModel, BigDecimal> quantita;
	public static volatile ListAttribute<ConsuntivoConsumiModel, AllegatoConsuntivoModel> allegati;

	public static final String TIPO_CONSUNTIVO = "tipoConsuntivo";
	public static final String TIPO_CARBURANTE = "tipoCarburante";
	public static final String MOTIVAZIONE = "motivazione";
	public static final String DICHIARAZIONE_CONSUMI = "dichiarazioneConsumi";
	public static final String QUANTITA = "quantita";
	public static final String ALLEGATI = "allegati";

}


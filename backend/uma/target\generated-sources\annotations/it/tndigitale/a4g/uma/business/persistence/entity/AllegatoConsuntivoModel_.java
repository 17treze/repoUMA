package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AllegatoConsuntivoModel.class)
public abstract class AllegatoConsuntivoModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<AllegatoConsuntivoModel, String> nomeFile;
	public static volatile SingularAttribute<AllegatoConsuntivoModel, String> descrizione;
	public static volatile SingularAttribute<AllegatoConsuntivoModel, ConsuntivoConsumiModel> consuntivoModel;
	public static volatile SingularAttribute<AllegatoConsuntivoModel, TipoAllegatoConsuntivo> tipoAllegato;

	public static final String NOME_FILE = "nomeFile";
	public static final String DESCRIZIONE = "descrizione";
	public static final String CONSUNTIVO_MODEL = "consuntivoModel";
	public static final String TIPO_ALLEGATO = "tipoAllegato";

}


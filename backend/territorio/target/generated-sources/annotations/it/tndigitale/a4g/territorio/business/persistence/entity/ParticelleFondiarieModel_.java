package it.tndigitale.a4g.territorio.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ParticelleFondiarieModel.class)
public abstract class ParticelleFondiarieModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<ParticelleFondiarieModel, String> sub;
	public static volatile SingularAttribute<ParticelleFondiarieModel, String> comune;
	public static volatile SingularAttribute<ParticelleFondiarieModel, ConduzioneModel> conduzione;
	public static volatile SingularAttribute<ParticelleFondiarieModel, Long> superficieCondotta;
	public static volatile SingularAttribute<ParticelleFondiarieModel, String> sezione;
	public static volatile SingularAttribute<ParticelleFondiarieModel, String> particella;
	public static volatile SingularAttribute<ParticelleFondiarieModel, Integer> foglio;

	public static final String SUB = "sub";
	public static final String COMUNE = "comune";
	public static final String CONDUZIONE = "conduzione";
	public static final String SUPERFICIE_CONDOTTA = "superficieCondotta";
	public static final String SEZIONE = "sezione";
	public static final String PARTICELLA = "particella";
	public static final String FOGLIO = "foglio";

}


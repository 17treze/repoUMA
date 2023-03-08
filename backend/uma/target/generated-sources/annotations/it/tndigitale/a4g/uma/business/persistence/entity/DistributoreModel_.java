package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DistributoreModel.class)
public abstract class DistributoreModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<DistributoreModel, String> comune;
	public static volatile SingularAttribute<DistributoreModel, Long> identificativo;
	public static volatile SingularAttribute<DistributoreModel, String> indirizzo;
	public static volatile SingularAttribute<DistributoreModel, String> denominazione;
	public static volatile SingularAttribute<DistributoreModel, String> provincia;
	public static volatile ListAttribute<DistributoreModel, PrelievoModel> prelievi;

	public static final String COMUNE = "comune";
	public static final String IDENTIFICATIVO = "identificativo";
	public static final String INDIRIZZO = "indirizzo";
	public static final String DENOMINAZIONE = "denominazione";
	public static final String PROVINCIA = "provincia";
	public static final String PRELIEVI = "prelievi";

}


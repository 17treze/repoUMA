package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrganizzazioneModel.class)
public abstract class OrganizzazioneModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SetAttribute<OrganizzazioneModel, FascicoloOrganizzazioneModel> fascicoloOrganizzazioni;
	public static volatile SingularAttribute<OrganizzazioneModel, String> denominazione;

	public static final String FASCICOLO_ORGANIZZAZIONI = "fascicoloOrganizzazioni";
	public static final String DENOMINAZIONE = "denominazione";

}


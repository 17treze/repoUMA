package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersonaGiuridicaConCaricaModel.class)
public abstract class PersonaGiuridicaConCaricaModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<PersonaGiuridicaConCaricaModel, String> denominazione;
	public static volatile SingularAttribute<PersonaGiuridicaConCaricaModel, String> codiceFiscale;
	public static volatile ListAttribute<PersonaGiuridicaConCaricaModel, CaricaModel> cariche;

	public static final String DENOMINAZIONE = "denominazione";
	public static final String CODICE_FISCALE = "codiceFiscale";
	public static final String CARICHE = "cariche";

}


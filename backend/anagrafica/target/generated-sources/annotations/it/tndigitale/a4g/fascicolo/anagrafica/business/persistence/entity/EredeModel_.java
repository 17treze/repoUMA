package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EredeModel.class)
public abstract class EredeModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<EredeModel, Boolean> firmatario;
	public static volatile SingularAttribute<EredeModel, FascicoloModel> fascicolo;
	public static volatile SingularAttribute<EredeModel, PersonaFisicaModel> personaFisica;

	public static final String FIRMATARIO = "firmatario";
	public static final String FASCICOLO = "fascicolo";
	public static final String PERSONA_FISICA = "personaFisica";

}


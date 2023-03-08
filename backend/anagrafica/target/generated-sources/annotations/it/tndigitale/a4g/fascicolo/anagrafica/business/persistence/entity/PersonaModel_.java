package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersonaModel.class)
public abstract class PersonaModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile ListAttribute<PersonaModel, UnitaTecnicoEconomicheModel> unitaTecnicoEconomiche;
	public static volatile ListAttribute<PersonaModel, IscrizioneSezioneModel> iscrizioniSezione;
	public static volatile SingularAttribute<PersonaModel, String> codiceFiscale;

	public static final String UNITA_TECNICO_ECONOMICHE = "unitaTecnicoEconomiche";
	public static final String ISCRIZIONI_SEZIONE = "iscrizioniSezione";
	public static final String CODICE_FISCALE = "codiceFiscale";

}


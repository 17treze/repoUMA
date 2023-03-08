package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AttivitaAtecoModel.class)
public abstract class AttivitaAtecoModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<AttivitaAtecoModel, PersonaModel> personaModel;
	public static volatile SingularAttribute<AttivitaAtecoModel, String> descrizione;
	public static volatile SingularAttribute<AttivitaAtecoModel, UnitaTecnicoEconomicheModel> unitaTecnicoEconomiche;
	public static volatile SingularAttribute<AttivitaAtecoModel, FonteDatoAnagrafico> fonte;
	public static volatile SingularAttribute<AttivitaAtecoModel, String> codice;
	public static volatile SingularAttribute<AttivitaAtecoModel, ImportanzaAttivita> importanza;

	public static final String PERSONA_MODEL = "personaModel";
	public static final String DESCRIZIONE = "descrizione";
	public static final String UNITA_TECNICO_ECONOMICHE = "unitaTecnicoEconomiche";
	public static final String FONTE = "fonte";
	public static final String CODICE = "codice";
	public static final String IMPORTANZA = "importanza";

}


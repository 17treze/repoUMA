package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DestinazioneUsoModel.class)
public abstract class DestinazioneUsoModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<DestinazioneUsoModel, String> descrizione;
	public static volatile SingularAttribute<DestinazioneUsoModel, UnitaTecnicoEconomicheModel> unitaTecnicoEconomiche;

	public static final String DESCRIZIONE = "descrizione";
	public static final String UNITA_TECNICO_ECONOMICHE = "unitaTecnicoEconomiche";

}


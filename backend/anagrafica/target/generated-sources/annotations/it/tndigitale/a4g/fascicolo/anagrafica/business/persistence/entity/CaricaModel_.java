package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CaricaModel.class)
public abstract class CaricaModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<CaricaModel, Boolean> firmatario;
	public static volatile SingularAttribute<CaricaModel, String> descrizione;
	public static volatile SingularAttribute<CaricaModel, LocalDate> dataInizio;
	public static volatile SingularAttribute<CaricaModel, PersonaFisicaModel> personaFisicaModel;
	public static volatile SingularAttribute<CaricaModel, String> identificativo;
	public static volatile SingularAttribute<CaricaModel, PersonaGiuridicaConCaricaModel> personaGiuridicaConCaricaModel;
	public static volatile SingularAttribute<CaricaModel, PersonaFisicaConCaricaModel> personaFisicaConCaricaModel;
	public static volatile SingularAttribute<CaricaModel, PersonaGiuridicaModel> personaGiuridicaModel;

	public static final String FIRMATARIO = "firmatario";
	public static final String DESCRIZIONE = "descrizione";
	public static final String DATA_INIZIO = "dataInizio";
	public static final String PERSONA_FISICA_MODEL = "personaFisicaModel";
	public static final String IDENTIFICATIVO = "identificativo";
	public static final String PERSONA_GIURIDICA_CON_CARICA_MODEL = "personaGiuridicaConCaricaModel";
	public static final String PERSONA_FISICA_CON_CARICA_MODEL = "personaFisicaConCaricaModel";
	public static final String PERSONA_GIURIDICA_MODEL = "personaGiuridicaModel";

}


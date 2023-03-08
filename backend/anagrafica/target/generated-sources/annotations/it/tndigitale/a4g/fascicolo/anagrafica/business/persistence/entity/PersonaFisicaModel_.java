package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersonaFisicaModel.class)
public abstract class PersonaFisicaModel_ extends it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaModel_ {

	public static volatile SingularAttribute<PersonaFisicaModel, LocalDate> dataMorte;
	public static volatile SingularAttribute<PersonaFisicaModel, ImpresaIndividualeModel> impresaIndividuale;
	public static volatile SingularAttribute<PersonaFisicaModel, LocalDate> dataNascita;
	public static volatile SingularAttribute<PersonaFisicaModel, String> cognome;
	public static volatile SingularAttribute<PersonaFisicaModel, Boolean> deceduto;
	public static volatile SingularAttribute<PersonaFisicaModel, String> provinciaNascita;
	public static volatile SingularAttribute<PersonaFisicaModel, String> comuneNascita;
	public static volatile SingularAttribute<PersonaFisicaModel, Sesso> sesso;
	public static volatile SingularAttribute<PersonaFisicaModel, String> nome;
	public static volatile SingularAttribute<PersonaFisicaModel, IndirizzoModel> domicilioFiscale;
	public static volatile ListAttribute<PersonaFisicaModel, CaricaModel> cariche;
	public static volatile SingularAttribute<PersonaFisicaModel, String> pec;

	public static final String DATA_MORTE = "dataMorte";
	public static final String IMPRESA_INDIVIDUALE = "impresaIndividuale";
	public static final String DATA_NASCITA = "dataNascita";
	public static final String COGNOME = "cognome";
	public static final String DECEDUTO = "deceduto";
	public static final String PROVINCIA_NASCITA = "provinciaNascita";
	public static final String COMUNE_NASCITA = "comuneNascita";
	public static final String SESSO = "sesso";
	public static final String NOME = "nome";
	public static final String DOMICILIO_FISCALE = "domicilioFiscale";
	public static final String CARICHE = "cariche";
	public static final String PEC = "pec";

}


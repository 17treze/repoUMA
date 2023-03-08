package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersonaFisicaConCaricaModel.class)
public abstract class PersonaFisicaConCaricaModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, VerificaCodiceFiscale> verificaCodiceFiscale;
	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, LocalDate> dataNascita;
	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, String> cognome;
	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, Boolean> deceduto;
	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, String> provinciaNascita;
	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, String> comuneNascita;
	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, IndirizzoModel> indirizzo;
	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, Sesso> sesso;
	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, String> nome;
	public static volatile SingularAttribute<PersonaFisicaConCaricaModel, String> codiceFiscale;
	public static volatile ListAttribute<PersonaFisicaConCaricaModel, CaricaModel> cariche;

	public static final String VERIFICA_CODICE_FISCALE = "verificaCodiceFiscale";
	public static final String DATA_NASCITA = "dataNascita";
	public static final String COGNOME = "cognome";
	public static final String DECEDUTO = "deceduto";
	public static final String PROVINCIA_NASCITA = "provinciaNascita";
	public static final String COMUNE_NASCITA = "comuneNascita";
	public static final String INDIRIZZO = "indirizzo";
	public static final String SESSO = "sesso";
	public static final String NOME = "nome";
	public static final String CODICE_FISCALE = "codiceFiscale";
	public static final String CARICHE = "cariche";

}


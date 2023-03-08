package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersonaGiuridicaModel.class)
public abstract class PersonaGiuridicaModel_ extends it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaModel_ {

	public static volatile SingularAttribute<PersonaGiuridicaModel, Double> capitaleSocialeDeliberato;
	public static volatile SingularAttribute<PersonaGiuridicaModel, String> partitaIVA;
	public static volatile SingularAttribute<PersonaGiuridicaModel, LocalDate> dataTermine;
	public static volatile SingularAttribute<PersonaGiuridicaModel, SedeModel> sedeLegale;
	public static volatile SingularAttribute<PersonaGiuridicaModel, String> denominazione;
	public static volatile SingularAttribute<PersonaGiuridicaModel, String> oggettoSociale;
	public static volatile SingularAttribute<PersonaGiuridicaModel, String> codiceFiscaleRappresentanteLegale;
	public static volatile SingularAttribute<PersonaGiuridicaModel, String> nominativoRappresentanteLegale;
	public static volatile SingularAttribute<PersonaGiuridicaModel, String> formaGiuridica;
	public static volatile SingularAttribute<PersonaGiuridicaModel, LocalDate> dataCostituzione;
	public static volatile ListAttribute<PersonaGiuridicaModel, CaricaModel> cariche;
	public static volatile SingularAttribute<PersonaGiuridicaModel, String> pec;

	public static final String CAPITALE_SOCIALE_DELIBERATO = "capitaleSocialeDeliberato";
	public static final String PARTITA_IV_A = "partitaIVA";
	public static final String DATA_TERMINE = "dataTermine";
	public static final String SEDE_LEGALE = "sedeLegale";
	public static final String DENOMINAZIONE = "denominazione";
	public static final String OGGETTO_SOCIALE = "oggettoSociale";
	public static final String CODICE_FISCALE_RAPPRESENTANTE_LEGALE = "codiceFiscaleRappresentanteLegale";
	public static final String NOMINATIVO_RAPPRESENTANTE_LEGALE = "nominativoRappresentanteLegale";
	public static final String FORMA_GIURIDICA = "formaGiuridica";
	public static final String DATA_COSTITUZIONE = "dataCostituzione";
	public static final String CARICHE = "cariche";
	public static final String PEC = "pec";

}


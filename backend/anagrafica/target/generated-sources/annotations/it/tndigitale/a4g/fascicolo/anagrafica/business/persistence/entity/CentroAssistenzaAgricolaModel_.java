package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CentroAssistenzaAgricolaModel.class)
public abstract class CentroAssistenzaAgricolaModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<CentroAssistenzaAgricolaModel, String> cfSocietaServizi;
	public static volatile SingularAttribute<CentroAssistenzaAgricolaModel, String> partitaIVA;
	public static volatile ListAttribute<CentroAssistenzaAgricolaModel, SportelloModel> sportelli;
	public static volatile SingularAttribute<CentroAssistenzaAgricolaModel, IndirizzoModel> indirizzo;
	public static volatile SingularAttribute<CentroAssistenzaAgricolaModel, String> societaServizi;
	public static volatile SingularAttribute<CentroAssistenzaAgricolaModel, String> denominazione;
	public static volatile SingularAttribute<CentroAssistenzaAgricolaModel, String> formaGiuridica;
	public static volatile SingularAttribute<CentroAssistenzaAgricolaModel, String> attoRiconoscimento;
	public static volatile SingularAttribute<CentroAssistenzaAgricolaModel, String> codiceFiscale;
	public static volatile SingularAttribute<CentroAssistenzaAgricolaModel, String> email;

	public static final String CF_SOCIETA_SERVIZI = "cfSocietaServizi";
	public static final String PARTITA_IV_A = "partitaIVA";
	public static final String SPORTELLI = "sportelli";
	public static final String INDIRIZZO = "indirizzo";
	public static final String SOCIETA_SERVIZI = "societaServizi";
	public static final String DENOMINAZIONE = "denominazione";
	public static final String FORMA_GIURIDICA = "formaGiuridica";
	public static final String ATTO_RICONOSCIMENTO = "attoRiconoscimento";
	public static final String CODICE_FISCALE = "codiceFiscale";
	public static final String EMAIL = "email";

}


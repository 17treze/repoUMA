package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SportelloModel.class)
public abstract class SportelloModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile ListAttribute<SportelloModel, RevocaImmediataModel> revocheImmediate;
	public static volatile SingularAttribute<SportelloModel, String> cap;
	public static volatile SingularAttribute<SportelloModel, String> comune;
	public static volatile SingularAttribute<SportelloModel, Long> identificativo;
	public static volatile SingularAttribute<SportelloModel, String> indirizzo;
	public static volatile SingularAttribute<SportelloModel, String> denominazione;
	public static volatile SingularAttribute<SportelloModel, CentroAssistenzaAgricolaModel> centroAssistenzaAgricola;
	public static volatile SingularAttribute<SportelloModel, String> provincia;
	public static volatile SingularAttribute<SportelloModel, String> telefono;
	public static volatile SingularAttribute<SportelloModel, String> email;

	public static final String REVOCHE_IMMEDIATE = "revocheImmediate";
	public static final String CAP = "cap";
	public static final String COMUNE = "comune";
	public static final String IDENTIFICATIVO = "identificativo";
	public static final String INDIRIZZO = "indirizzo";
	public static final String DENOMINAZIONE = "denominazione";
	public static final String CENTRO_ASSISTENZA_AGRICOLA = "centroAssistenzaAgricola";
	public static final String PROVINCIA = "provincia";
	public static final String TELEFONO = "telefono";
	public static final String EMAIL = "email";

}


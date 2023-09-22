package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClienteModel.class)
public abstract class ClienteModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<ClienteModel, DichiarazioneConsumiModel> dichiarazioneConsumi;
	public static volatile SingularAttribute<ClienteModel, String> denominazione;
	public static volatile SingularAttribute<ClienteModel, Long> idFascicolo;
	public static volatile SingularAttribute<ClienteModel, String> cuaa;
	public static volatile ListAttribute<ClienteModel, FatturaClienteModel> allegati;

	public static final String DICHIARAZIONE_CONSUMI = "dichiarazioneConsumi";
	public static final String DENOMINAZIONE = "denominazione";
	public static final String ID_FASCICOLO = "idFascicolo";
	public static final String CUAA = "cuaa";
	public static final String ALLEGATI = "allegati";

}


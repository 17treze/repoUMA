package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GruppoLavorazioneModel.class)
public abstract class GruppoLavorazioneModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile ListAttribute<GruppoLavorazioneModel, LavorazioneModel> lavorazioneModels;
	public static volatile SingularAttribute<GruppoLavorazioneModel, Integer> indice;
	public static volatile SingularAttribute<GruppoLavorazioneModel, Integer> annoFine;
	public static volatile SingularAttribute<GruppoLavorazioneModel, String> nome;
	public static volatile SingularAttribute<GruppoLavorazioneModel, Integer> annoInizio;
	public static volatile SingularAttribute<GruppoLavorazioneModel, AmbitoLavorazione> ambitoLavorazione;

	public static final String LAVORAZIONE_MODELS = "lavorazioneModels";
	public static final String INDICE = "indice";
	public static final String ANNO_FINE = "annoFine";
	public static final String NOME = "nome";
	public static final String ANNO_INIZIO = "annoInizio";
	public static final String AMBITO_LAVORAZIONE = "ambitoLavorazione";

}


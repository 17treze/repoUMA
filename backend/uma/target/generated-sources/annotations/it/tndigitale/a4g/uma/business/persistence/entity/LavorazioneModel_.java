package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LavorazioneModel.class)
public abstract class LavorazioneModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile ListAttribute<LavorazioneModel, CoefficienteModel> coefficienti;
	public static volatile SingularAttribute<LavorazioneModel, Integer> indice;
	public static volatile SingularAttribute<LavorazioneModel, UnitaDiMisura> unitaDiMisura;
	public static volatile SingularAttribute<LavorazioneModel, GruppoLavorazioneModel> gruppoLavorazione;
	public static volatile SingularAttribute<LavorazioneModel, String> nome;
	public static volatile SingularAttribute<LavorazioneModel, TipologiaLavorazione> tipologia;

	public static final String COEFFICIENTI = "coefficienti";
	public static final String INDICE = "indice";
	public static final String UNITA_DI_MISURA = "unitaDiMisura";
	public static final String GRUPPO_LAVORAZIONE = "gruppoLavorazione";
	public static final String NOME = "nome";
	public static final String TIPOLOGIA = "tipologia";

}


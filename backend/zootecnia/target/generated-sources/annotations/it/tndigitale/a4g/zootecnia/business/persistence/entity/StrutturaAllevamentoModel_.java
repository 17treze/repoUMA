package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StrutturaAllevamentoModel.class)
public abstract class StrutturaAllevamentoModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> localita;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> subalterno;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> indirizzo;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> sezione;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> foglio;
	public static volatile ListAttribute<StrutturaAllevamentoModel, AllevamentoModel> allevamenti;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> cap;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> comune;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> identificativo;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> latitudine;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> cuaa;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> longitudine;
	public static volatile SingularAttribute<StrutturaAllevamentoModel, String> particella;

	public static final String LOCALITA = "localita";
	public static final String SUBALTERNO = "subalterno";
	public static final String INDIRIZZO = "indirizzo";
	public static final String SEZIONE = "sezione";
	public static final String FOGLIO = "foglio";
	public static final String ALLEVAMENTI = "allevamenti";
	public static final String CAP = "cap";
	public static final String COMUNE = "comune";
	public static final String IDENTIFICATIVO = "identificativo";
	public static final String LATITUDINE = "latitudine";
	public static final String CUAA = "cuaa";
	public static final String LONGITUDINE = "longitudine";
	public static final String PARTICELLA = "particella";

}


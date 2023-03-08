package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FabbricatoGruppiModel.class)
public abstract class FabbricatoGruppiModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<FabbricatoGruppiModel, GruppoLavorazioneModel> gruppoLavorazione;
	public static volatile SingularAttribute<FabbricatoGruppiModel, String> tipoFabbricato;
	public static volatile SingularAttribute<FabbricatoGruppiModel, String> codiceFabbricato;

	public static final String GRUPPO_LAVORAZIONE = "gruppoLavorazione";
	public static final String TIPO_FABBRICATO = "tipoFabbricato";
	public static final String CODICE_FABBRICATO = "codiceFabbricato";

}


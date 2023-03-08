package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FabbricatoModel.class)
public abstract class FabbricatoModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<FabbricatoModel, Integer> volume;
	public static volatile SingularAttribute<FabbricatoModel, String> comune;
	public static volatile ListAttribute<FabbricatoModel, FabbisognoFabbricatoModel> fabbisogni;
	public static volatile SingularAttribute<FabbricatoModel, RichiestaCarburanteModel> richiestaCarburante;
	public static volatile SingularAttribute<FabbricatoModel, String> subalterno;
	public static volatile SingularAttribute<FabbricatoModel, FabbricatoGruppiModel> tipoFabbricato;
	public static volatile SingularAttribute<FabbricatoModel, String> provincia;
	public static volatile SingularAttribute<FabbricatoModel, String> siglaProvincia;
	public static volatile SingularAttribute<FabbricatoModel, Long> identificativoAgs;
	public static volatile SingularAttribute<FabbricatoModel, String> particella;

	public static final String VOLUME = "volume";
	public static final String COMUNE = "comune";
	public static final String FABBISOGNI = "fabbisogni";
	public static final String RICHIESTA_CARBURANTE = "richiestaCarburante";
	public static final String SUBALTERNO = "subalterno";
	public static final String TIPO_FABBRICATO = "tipoFabbricato";
	public static final String PROVINCIA = "provincia";
	public static final String SIGLA_PROVINCIA = "siglaProvincia";
	public static final String IDENTIFICATIVO_AGS = "identificativoAgs";
	public static final String PARTICELLA = "particella";

}


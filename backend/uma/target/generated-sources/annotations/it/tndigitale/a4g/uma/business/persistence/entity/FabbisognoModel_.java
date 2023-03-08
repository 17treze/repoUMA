package it.tndigitale.a4g.uma.business.persistence.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FabbisognoModel.class)
public abstract class FabbisognoModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<FabbisognoModel, TipoCarburante> carburante;
	public static volatile SingularAttribute<FabbisognoModel, RichiestaCarburanteModel> richiestaCarburante;
	public static volatile SingularAttribute<FabbisognoModel, LavorazioneModel> lavorazioneModel;
	public static volatile SingularAttribute<FabbisognoModel, BigDecimal> quantita;

	public static final String CARBURANTE = "carburante";
	public static final String RICHIESTA_CARBURANTE = "richiestaCarburante";
	public static final String LAVORAZIONE_MODEL = "lavorazioneModel";
	public static final String QUANTITA = "quantita";

}


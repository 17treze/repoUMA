package it.tndigitale.a4g.uma.business.persistence.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CoefficienteModel.class)
public abstract class CoefficienteModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<CoefficienteModel, BigDecimal> coefficiente;
	public static volatile SingularAttribute<CoefficienteModel, LavorazioneModel> lavorazioneModel;
	public static volatile SingularAttribute<CoefficienteModel, Integer> annoFine;
	public static volatile SingularAttribute<CoefficienteModel, Integer> annoInizio;

	public static final String COEFFICIENTE = "coefficiente";
	public static final String LAVORAZIONE_MODEL = "lavorazioneModel";
	public static final String ANNO_FINE = "annoFine";
	public static final String ANNO_INIZIO = "annoInizio";

}


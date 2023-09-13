package it.tndigitale.a4g.uma.business.persistence.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ConsumiClienteModel.class)
public abstract class ConsumiClienteModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<ConsumiClienteModel, TipoCarburante> carburante;
	public static volatile SingularAttribute<ConsumiClienteModel, ClienteModel> cliente;
	public static volatile SingularAttribute<ConsumiClienteModel, LavorazioneModel> lavorazioneModel;
	public static volatile SingularAttribute<ConsumiClienteModel, BigDecimal> quantita;

	public static final String CARBURANTE = "carburante";
	public static final String CLIENTE = "cliente";
	public static final String LAVORAZIONE_MODEL = "lavorazioneModel";
	public static final String QUANTITA = "quantita";

}


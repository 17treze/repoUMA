package it.tndigitale.a4g.uma.business.persistence.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TrasferimentoCarburanteModel.class)
public abstract class TrasferimentoCarburanteModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<TrasferimentoCarburanteModel, Integer> gasolioSerre;
	public static volatile SingularAttribute<TrasferimentoCarburanteModel, LocalDateTime> data;
	public static volatile SingularAttribute<TrasferimentoCarburanteModel, RichiestaCarburanteModel> richiestaCarburante;
	public static volatile SingularAttribute<TrasferimentoCarburanteModel, String> cuaaDestinatario;
	public static volatile SingularAttribute<TrasferimentoCarburanteModel, Integer> gasolio;
	public static volatile SingularAttribute<TrasferimentoCarburanteModel, Integer> benzina;

	public static final String GASOLIO_SERRE = "gasolioSerre";
	public static final String DATA = "data";
	public static final String RICHIESTA_CARBURANTE = "richiestaCarburante";
	public static final String CUAA_DESTINATARIO = "cuaaDestinatario";
	public static final String GASOLIO = "gasolio";
	public static final String BENZINA = "benzina";

}


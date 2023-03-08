package it.tndigitale.a4g.uma.business.persistence.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PrelievoModel.class)
public abstract class PrelievoModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<PrelievoModel, Integer> gasolioSerre;
	public static volatile SingularAttribute<PrelievoModel, LocalDateTime> data;
	public static volatile SingularAttribute<PrelievoModel, RichiestaCarburanteModel> richiestaCarburante;
	public static volatile SingularAttribute<PrelievoModel, DistributoreModel> distributore;
	public static volatile SingularAttribute<PrelievoModel, Boolean> consegnato;
	public static volatile SingularAttribute<PrelievoModel, Integer> gasolio;
	public static volatile SingularAttribute<PrelievoModel, String> estremiDocumentoFiscale;
	public static volatile SingularAttribute<PrelievoModel, Integer> benzina;

	public static final String GASOLIO_SERRE = "gasolioSerre";
	public static final String DATA = "data";
	public static final String RICHIESTA_CARBURANTE = "richiestaCarburante";
	public static final String DISTRIBUTORE = "distributore";
	public static final String CONSEGNATO = "consegnato";
	public static final String GASOLIO = "gasolio";
	public static final String ESTREMI_DOCUMENTO_FISCALE = "estremiDocumentoFiscale";
	public static final String BENZINA = "benzina";

}


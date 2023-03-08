package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ImpresaIndividualeModel.class)
public abstract class ImpresaIndividualeModel_ {

	public static volatile SingularAttribute<ImpresaIndividualeModel, String> partitaIVA;
	public static volatile SingularAttribute<ImpresaIndividualeModel, SedeModel> sedeLegale;
	public static volatile SingularAttribute<ImpresaIndividualeModel, String> denominazione;
	public static volatile SingularAttribute<ImpresaIndividualeModel, String> formaGiuridica;

	public static final String PARTITA_IV_A = "partitaIVA";
	public static final String SEDE_LEGALE = "sedeLegale";
	public static final String DENOMINAZIONE = "denominazione";
	public static final String FORMA_GIURIDICA = "formaGiuridica";

}


package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FatturaClienteModel.class)
public abstract class FatturaClienteModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<FatturaClienteModel, String> nomeFile;
	public static volatile SingularAttribute<FatturaClienteModel, ClienteModel> cliente;

	public static final String NOME_FILE = "nomeFile";
	public static final String CLIENTE = "cliente";

}


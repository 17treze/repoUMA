package it.tndigitale.a4g.territorio.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SottotipoModel.class)
public abstract class SottotipoModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<SottotipoModel, String> descrizione;
	public static volatile SingularAttribute<SottotipoModel, String> ambito;
	public static volatile ListAttribute<SottotipoModel, ConduzioneModel> conduzioni;
	public static volatile ListAttribute<SottotipoModel, TipoDocumentoConduzioneModel> tipiDocumentoConduzione;

	public static final String DESCRIZIONE = "descrizione";
	public static final String AMBITO = "ambito";
	public static final String CONDUZIONI = "conduzioni";
	public static final String TIPI_DOCUMENTO_CONDUZIONE = "tipiDocumentoConduzione";

}


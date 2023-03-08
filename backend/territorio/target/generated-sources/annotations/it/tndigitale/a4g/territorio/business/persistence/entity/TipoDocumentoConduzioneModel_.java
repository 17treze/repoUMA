package it.tndigitale.a4g.territorio.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TipoDocumentoConduzioneModel.class)
public abstract class TipoDocumentoConduzioneModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<TipoDocumentoConduzioneModel, SottotipoModel> sottotipo;
	public static volatile SingularAttribute<TipoDocumentoConduzioneModel, String> descrizione;
	public static volatile ListAttribute<TipoDocumentoConduzioneModel, DocumentoConduzioneModel> documenti;

	public static final String SOTTOTIPO = "sottotipo";
	public static final String DESCRIZIONE = "descrizione";
	public static final String DOCUMENTI = "documenti";

}


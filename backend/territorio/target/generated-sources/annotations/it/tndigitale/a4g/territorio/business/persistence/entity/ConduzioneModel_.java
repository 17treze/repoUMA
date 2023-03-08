package it.tndigitale.a4g.territorio.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ConduzioneModel.class)
public abstract class ConduzioneModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<ConduzioneModel, SottotipoModel> sottotipo;
	public static volatile ListAttribute<ConduzioneModel, DocumentoConduzioneModel> documenti;
	public static volatile SingularAttribute<ConduzioneModel, FascicoloModel> fascicolo;
	public static volatile ListAttribute<ConduzioneModel, ParticelleFondiarieModel> particelle;

	public static final String SOTTOTIPO = "sottotipo";
	public static final String DOCUMENTI = "documenti";
	public static final String FASCICOLO = "fascicolo";
	public static final String PARTICELLE = "particelle";

}


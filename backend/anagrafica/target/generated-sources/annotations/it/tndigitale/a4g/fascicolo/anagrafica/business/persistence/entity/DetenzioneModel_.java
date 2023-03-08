package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DetenzioneModel.class)
public abstract class DetenzioneModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<DetenzioneModel, LocalDate> dataInizio;
	public static volatile SingularAttribute<DetenzioneModel, FascicoloModel> fascicolo;
	public static volatile SingularAttribute<DetenzioneModel, LocalDate> dataFine;

	public static final String DATA_INIZIO = "dataInizio";
	public static final String FASCICOLO = "fascicolo";
	public static final String DATA_FINE = "dataFine";

}


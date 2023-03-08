package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MandatoModel.class)
public abstract class MandatoModel_ extends it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel_ {

	public static volatile ListAttribute<MandatoModel, RevocaImmediataModel> revocheImmediate;
	public static volatile SingularAttribute<MandatoModel, SportelloModel> sportello;
	public static volatile SingularAttribute<MandatoModel, byte[]> contratto;
	public static volatile SingularAttribute<MandatoModel, LocalDate> dataSottoscrizione;

	public static final String REVOCHE_IMMEDIATE = "revocheImmediate";
	public static final String SPORTELLO = "sportello";
	public static final String CONTRATTO = "contratto";
	public static final String DATA_SOTTOSCRIZIONE = "dataSottoscrizione";

}


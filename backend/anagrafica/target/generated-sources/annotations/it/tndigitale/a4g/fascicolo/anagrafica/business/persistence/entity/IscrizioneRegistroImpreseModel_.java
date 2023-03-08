package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IscrizioneRegistroImpreseModel.class)
public abstract class IscrizioneRegistroImpreseModel_ {

	public static volatile SingularAttribute<IscrizioneRegistroImpreseModel, Boolean> cessata;
	public static volatile SingularAttribute<IscrizioneRegistroImpreseModel, String> provinciaCameraCommercio;
	public static volatile SingularAttribute<IscrizioneRegistroImpreseModel, Long> numeroRepertorioEconomicoAmministrativo;
	public static volatile SingularAttribute<IscrizioneRegistroImpreseModel, LocalDate> dataIscrizione;

	public static final String CESSATA = "cessata";
	public static final String PROVINCIA_CAMERA_COMMERCIO = "provinciaCameraCommercio";
	public static final String NUMERO_REPERTORIO_ECONOMICO_AMMINISTRATIVO = "numeroRepertorioEconomicoAmministrativo";
	public static final String DATA_ISCRIZIONE = "dataIscrizione";

}


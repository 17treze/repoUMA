package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.SezioneEnum;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IscrizioneSezioneModel.class)
public abstract class IscrizioneSezioneModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<IscrizioneSezioneModel, PersonaModel> persona;
	public static volatile SingularAttribute<IscrizioneSezioneModel, String> qualifica;
	public static volatile SingularAttribute<IscrizioneSezioneModel, SezioneEnum> sezione;
	public static volatile SingularAttribute<IscrizioneSezioneModel, String> coltivatoreDiretto;
	public static volatile SingularAttribute<IscrizioneSezioneModel, LocalDate> dataIscrizione;

	public static final String PERSONA = "persona";
	public static final String QUALIFICA = "qualifica";
	public static final String SEZIONE = "sezione";
	public static final String COLTIVATORE_DIRETTO = "coltivatoreDiretto";
	public static final String DATA_ISCRIZIONE = "dataIscrizione";

}


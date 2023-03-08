package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SedeModel.class)
public abstract class SedeModel_ {

	public static volatile SingularAttribute<SedeModel, IndirizzoModel> indirizzo;
	public static volatile SingularAttribute<SedeModel, IscrizioneRegistroImpreseModel> iscrizioneRegistroImprese;
	public static volatile SingularAttribute<SedeModel, String> telefono;
	public static volatile ListAttribute<SedeModel, AttivitaAtecoModel> attivita;
	public static volatile SingularAttribute<SedeModel, IndirizzoModel> indirizzoCameraCommercio;
	public static volatile SingularAttribute<SedeModel, String> pec;

	public static final String INDIRIZZO = "indirizzo";
	public static final String ISCRIZIONE_REGISTRO_IMPRESE = "iscrizioneRegistroImprese";
	public static final String TELEFONO = "telefono";
	public static final String ATTIVITA = "attivita";
	public static final String INDIRIZZO_CAMERA_COMMERCIO = "indirizzoCameraCommercio";
	public static final String PEC = "pec";

}


package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MovimentazioneModel.class)
public abstract class MovimentazioneModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<MovimentazioneModel, String> utente;
	public static volatile SingularAttribute<MovimentazioneModel, LocalDateTime> dataInizio;
	public static volatile SingularAttribute<MovimentazioneModel, TipoMovimentazioneFascicolo> tipo;
	public static volatile SingularAttribute<MovimentazioneModel, FascicoloModel> fascicolo;
	public static volatile SingularAttribute<MovimentazioneModel, LocalDateTime> dataFine;
	public static volatile SingularAttribute<MovimentazioneModel, String> motivazioneInizio;
	public static volatile SingularAttribute<MovimentazioneModel, String> motivazioneFine;

	public static final String UTENTE = "utente";
	public static final String DATA_INIZIO = "dataInizio";
	public static final String TIPO = "tipo";
	public static final String FASCICOLO = "fascicolo";
	public static final String DATA_FINE = "dataFine";
	public static final String MOTIVAZIONE_INIZIO = "motivazioneInizio";
	public static final String MOTIVAZIONE_FINE = "motivazioneFine";

}


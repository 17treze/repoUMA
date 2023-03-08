package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FascicoloOrganizzazioneModel.class)
public abstract class FascicoloOrganizzazioneModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<FascicoloOrganizzazioneModel, String> utenteInserimento;
	public static volatile SingularAttribute<FascicoloOrganizzazioneModel, String> utenteCancellazione;
	public static volatile SingularAttribute<FascicoloOrganizzazioneModel, LocalDateTime> dataInserimentoAssociazione;
	public static volatile SingularAttribute<FascicoloOrganizzazioneModel, OrganizzazioneModel> organizzazione;
	public static volatile SingularAttribute<FascicoloOrganizzazioneModel, LocalDateTime> dataCancellazioneAssociazione;
	public static volatile SingularAttribute<FascicoloOrganizzazioneModel, LocalDate> dataInizioAssociazione;
	public static volatile SingularAttribute<FascicoloOrganizzazioneModel, String> cuaa;
	public static volatile SingularAttribute<FascicoloOrganizzazioneModel, LocalDate> dataFineAssociazione;

	public static final String UTENTE_INSERIMENTO = "utenteInserimento";
	public static final String UTENTE_CANCELLAZIONE = "utenteCancellazione";
	public static final String DATA_INSERIMENTO_ASSOCIAZIONE = "dataInserimentoAssociazione";
	public static final String ORGANIZZAZIONE = "organizzazione";
	public static final String DATA_CANCELLAZIONE_ASSOCIAZIONE = "dataCancellazioneAssociazione";
	public static final String DATA_INIZIO_ASSOCIAZIONE = "dataInizioAssociazione";
	public static final String CUAA = "cuaa";
	public static final String DATA_FINE_ASSOCIAZIONE = "dataFineAssociazione";

}


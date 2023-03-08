package it.tndigitale.a4g.uma.business.persistence.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DichiarazioneConsumiModel.class)
public abstract class DichiarazioneConsumiModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<DichiarazioneConsumiModel, LocalDateTime> dataProtocollazione;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, RichiestaCarburanteModel> richiestaCarburante;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, byte[]> documento;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, Boolean> firma;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, StatoDichiarazioneConsumi> stato;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, String> entePresentatore;
	public static volatile ListAttribute<DichiarazioneConsumiModel, ClienteModel> clienti;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, LocalDateTime> dataPresentazione;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, String> protocollo;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, String> motivazioneAccisa;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, LocalDateTime> dataConduzione;
	public static volatile SingularAttribute<DichiarazioneConsumiModel, String> cfRichiedente;
	public static volatile ListAttribute<DichiarazioneConsumiModel, ConsuntivoConsumiModel> consuntivi;

	public static final String DATA_PROTOCOLLAZIONE = "dataProtocollazione";
	public static final String RICHIESTA_CARBURANTE = "richiestaCarburante";
	public static final String DOCUMENTO = "documento";
	public static final String FIRMA = "firma";
	public static final String STATO = "stato";
	public static final String ENTE_PRESENTATORE = "entePresentatore";
	public static final String CLIENTI = "clienti";
	public static final String DATA_PRESENTAZIONE = "dataPresentazione";
	public static final String PROTOCOLLO = "protocollo";
	public static final String MOTIVAZIONE_ACCISA = "motivazioneAccisa";
	public static final String DATA_CONDUZIONE = "dataConduzione";
	public static final String CF_RICHIEDENTE = "cfRichiedente";
	public static final String CONSUNTIVI = "consuntivi";

}


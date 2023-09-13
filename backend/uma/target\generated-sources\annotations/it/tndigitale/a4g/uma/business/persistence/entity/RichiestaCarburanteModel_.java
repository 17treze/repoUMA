package it.tndigitale.a4g.uma.business.persistence.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RichiestaCarburanteModel.class)
public abstract class RichiestaCarburanteModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile ListAttribute<RichiestaCarburanteModel, FabbricatoModel> fabbricati;
	public static volatile SingularAttribute<RichiestaCarburanteModel, LocalDateTime> dataProtocollazione;
	public static volatile SingularAttribute<RichiestaCarburanteModel, String> note;
	public static volatile ListAttribute<RichiestaCarburanteModel, FabbisognoModel> fabbisogni;
	public static volatile SingularAttribute<RichiestaCarburanteModel, Integer> gasolioTerzi;
	public static volatile SingularAttribute<RichiestaCarburanteModel, byte[]> documento;
	public static volatile SingularAttribute<RichiestaCarburanteModel, Integer> gasolio;
	public static volatile SingularAttribute<RichiestaCarburanteModel, Boolean> firma;
	public static volatile ListAttribute<RichiestaCarburanteModel, SuperficieMassimaModel> superficiMassime;
	public static volatile SingularAttribute<RichiestaCarburanteModel, Integer> benzina;
	public static volatile SingularAttribute<RichiestaCarburanteModel, StatoRichiestaCarburante> stato;
	public static volatile SingularAttribute<RichiestaCarburanteModel, Long> campagna;
	public static volatile SingularAttribute<RichiestaCarburanteModel, Integer> gasolioSerre;
	public static volatile SingularAttribute<RichiestaCarburanteModel, String> entePresentatore;
	public static volatile SingularAttribute<RichiestaCarburanteModel, DichiarazioneConsumiModel> dichiarazioneConsumi;
	public static volatile SingularAttribute<RichiestaCarburanteModel, LocalDateTime> dataPresentazione;
	public static volatile SingularAttribute<RichiestaCarburanteModel, String> protocollo;
	public static volatile SingularAttribute<RichiestaCarburanteModel, String> denominazione;
	public static volatile SingularAttribute<RichiestaCarburanteModel, String> cuaa;
	public static volatile ListAttribute<RichiestaCarburanteModel, UtilizzoMacchinariModel> macchine;
	public static volatile SingularAttribute<RichiestaCarburanteModel, String> cfRichiedente;
	public static volatile ListAttribute<RichiestaCarburanteModel, PrelievoModel> prelievi;

	public static final String FABBRICATI = "fabbricati";
	public static final String DATA_PROTOCOLLAZIONE = "dataProtocollazione";
	public static final String NOTE = "note";
	public static final String FABBISOGNI = "fabbisogni";
	public static final String GASOLIO_TERZI = "gasolioTerzi";
	public static final String DOCUMENTO = "documento";
	public static final String GASOLIO = "gasolio";
	public static final String FIRMA = "firma";
	public static final String SUPERFICI_MASSIME = "superficiMassime";
	public static final String BENZINA = "benzina";
	public static final String STATO = "stato";
	public static final String CAMPAGNA = "campagna";
	public static final String GASOLIO_SERRE = "gasolioSerre";
	public static final String ENTE_PRESENTATORE = "entePresentatore";
	public static final String DICHIARAZIONE_CONSUMI = "dichiarazioneConsumi";
	public static final String DATA_PRESENTAZIONE = "dataPresentazione";
	public static final String PROTOCOLLO = "protocollo";
	public static final String DENOMINAZIONE = "denominazione";
	public static final String CUAA = "cuaa";
	public static final String MACCHINE = "macchine";
	public static final String CF_RICHIEDENTE = "cfRichiedente";
	public static final String PRELIEVI = "prelievi";

}


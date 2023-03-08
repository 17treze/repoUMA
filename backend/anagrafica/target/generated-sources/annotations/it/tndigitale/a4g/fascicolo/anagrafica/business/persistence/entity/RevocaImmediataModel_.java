package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RevocaImmediataModel.class)
public abstract class RevocaImmediataModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<RevocaImmediataModel, StatoRevocaImmediata> stato;
	public static volatile SingularAttribute<RevocaImmediataModel, SportelloModel> sportello;
	public static volatile SingularAttribute<RevocaImmediataModel, String> motivazioneRifiuto;
	public static volatile SingularAttribute<RevocaImmediataModel, String> idProtocollo;
	public static volatile SingularAttribute<RevocaImmediataModel, LocalDate> dtProtocollo;
	public static volatile SingularAttribute<RevocaImmediataModel, LocalDate> dataValutazione;
	public static volatile SingularAttribute<RevocaImmediataModel, LocalDate> dataSottoscrizione;
	public static volatile SingularAttribute<RevocaImmediataModel, MandatoModel> mandato;
	public static volatile SingularAttribute<RevocaImmediataModel, String> causaRichiesta;
	public static volatile SingularAttribute<RevocaImmediataModel, String> codiceFiscale;
	public static volatile SingularAttribute<RevocaImmediataModel, byte[]> richiestaFirmata;

	public static final String STATO = "stato";
	public static final String SPORTELLO = "sportello";
	public static final String MOTIVAZIONE_RIFIUTO = "motivazioneRifiuto";
	public static final String ID_PROTOCOLLO = "idProtocollo";
	public static final String DT_PROTOCOLLO = "dtProtocollo";
	public static final String DATA_VALUTAZIONE = "dataValutazione";
	public static final String DATA_SOTTOSCRIZIONE = "dataSottoscrizione";
	public static final String MANDATO = "mandato";
	public static final String CAUSA_RICHIESTA = "causaRichiesta";
	public static final String CODICE_FISCALE = "codiceFiscale";
	public static final String RICHIESTA_FIRMATA = "richiestaFirmata";

}


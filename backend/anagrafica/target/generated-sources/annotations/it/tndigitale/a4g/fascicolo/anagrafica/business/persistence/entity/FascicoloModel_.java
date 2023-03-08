package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FascicoloModel.class)
public abstract class FascicoloModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<FascicoloModel, PersonaModel> persona;
	public static volatile SingularAttribute<FascicoloModel, byte[]> schedaValidazioneFirmata;
	public static volatile SingularAttribute<FascicoloModel, LocalDate> dataValidazione;
	public static volatile SingularAttribute<FascicoloModel, LocalDateTime> dtAggiornamentoFontiEsterne;
	public static volatile SingularAttribute<FascicoloModel, LocalDate> dtChiusuraTrasferimentoOp;
	public static volatile SingularAttribute<FascicoloModel, LocalDate> dataApertura;
	public static volatile SingularAttribute<FascicoloModel, String> utenteModifica;
	public static volatile SingularAttribute<FascicoloModel, StatoFascicoloEnum> stato;
	public static volatile ListAttribute<FascicoloModel, ModoPagamentoModel> modoPagamentoList;
	public static volatile SingularAttribute<FascicoloModel, String> idProtocollo;
	public static volatile ListAttribute<FascicoloModel, DetenzioneModel> detenzioni;
	public static volatile SingularAttribute<FascicoloModel, String> utenteValidazione;
	public static volatile SingularAttribute<FascicoloModel, DocumentoIdentitaModel> documentoIdentita;
	public static volatile SingularAttribute<FascicoloModel, LocalDate> dtProtocollazione;
	public static volatile SingularAttribute<FascicoloModel, OrganismoPagatoreEnum> organismoPagatore;
	public static volatile SingularAttribute<FascicoloModel, String> denominazione;
	public static volatile SingularAttribute<FascicoloModel, String> cuaa;
	public static volatile SingularAttribute<FascicoloModel, LocalDate> dataModifica;

	public static final String PERSONA = "persona";
	public static final String SCHEDA_VALIDAZIONE_FIRMATA = "schedaValidazioneFirmata";
	public static final String DATA_VALIDAZIONE = "dataValidazione";
	public static final String DT_AGGIORNAMENTO_FONTI_ESTERNE = "dtAggiornamentoFontiEsterne";
	public static final String DT_CHIUSURA_TRASFERIMENTO_OP = "dtChiusuraTrasferimentoOp";
	public static final String DATA_APERTURA = "dataApertura";
	public static final String UTENTE_MODIFICA = "utenteModifica";
	public static final String STATO = "stato";
	public static final String MODO_PAGAMENTO_LIST = "modoPagamentoList";
	public static final String ID_PROTOCOLLO = "idProtocollo";
	public static final String DETENZIONI = "detenzioni";
	public static final String UTENTE_VALIDAZIONE = "utenteValidazione";
	public static final String DOCUMENTO_IDENTITA = "documentoIdentita";
	public static final String DT_PROTOCOLLAZIONE = "dtProtocollazione";
	public static final String ORGANISMO_PAGATORE = "organismoPagatore";
	public static final String DENOMINAZIONE = "denominazione";
	public static final String CUAA = "cuaa";
	public static final String DATA_MODIFICA = "dataModifica";

}


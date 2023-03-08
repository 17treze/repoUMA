package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AllevamentoModel.class)
public abstract class AllevamentoModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<AllevamentoModel, String> denominazioneDetentore;
	public static volatile SingularAttribute<AllevamentoModel, String> specie;
	public static volatile SingularAttribute<AllevamentoModel, StrutturaAllevamentoModel> strutturaAllevamento;
	public static volatile SingularAttribute<AllevamentoModel, LocalDate> dtChiusuraAllevamento;
	public static volatile SingularAttribute<AllevamentoModel, String> identificativoFiscale;
	public static volatile SingularAttribute<AllevamentoModel, String> tipologiaAllevamento;
	public static volatile SingularAttribute<AllevamentoModel, FascicoloModel> fascicolo;
	public static volatile SingularAttribute<AllevamentoModel, LocalDate> dtInizioDetenzione;
	public static volatile SingularAttribute<AllevamentoModel, String> orientamentoProduttivo;
	public static volatile SingularAttribute<AllevamentoModel, String> autorizzazioneSanitariaLatte;
	public static volatile SingularAttribute<AllevamentoModel, LocalDate> dtAperturaAllevamento;
	public static volatile SingularAttribute<AllevamentoModel, String> cfDetentore;
	public static volatile SingularAttribute<AllevamentoModel, String> soccida;
	public static volatile SingularAttribute<AllevamentoModel, LocalDate> dtFineDetenzione;
	public static volatile SingularAttribute<AllevamentoModel, String> identificativo;
	public static volatile SingularAttribute<AllevamentoModel, String> denominazioneProprietario;
	public static volatile SingularAttribute<AllevamentoModel, String> cfProprietario;
	public static volatile SingularAttribute<AllevamentoModel, String> tipologiaProduzione;

	public static final String DENOMINAZIONE_DETENTORE = "denominazioneDetentore";
	public static final String SPECIE = "specie";
	public static final String STRUTTURA_ALLEVAMENTO = "strutturaAllevamento";
	public static final String DT_CHIUSURA_ALLEVAMENTO = "dtChiusuraAllevamento";
	public static final String IDENTIFICATIVO_FISCALE = "identificativoFiscale";
	public static final String TIPOLOGIA_ALLEVAMENTO = "tipologiaAllevamento";
	public static final String FASCICOLO = "fascicolo";
	public static final String DT_INIZIO_DETENZIONE = "dtInizioDetenzione";
	public static final String ORIENTAMENTO_PRODUTTIVO = "orientamentoProduttivo";
	public static final String AUTORIZZAZIONE_SANITARIA_LATTE = "autorizzazioneSanitariaLatte";
	public static final String DT_APERTURA_ALLEVAMENTO = "dtAperturaAllevamento";
	public static final String CF_DETENTORE = "cfDetentore";
	public static final String SOCCIDA = "soccida";
	public static final String DT_FINE_DETENZIONE = "dtFineDetenzione";
	public static final String IDENTIFICATIVO = "identificativo";
	public static final String DENOMINAZIONE_PROPRIETARIO = "denominazioneProprietario";
	public static final String CF_PROPRIETARIO = "cfProprietario";
	public static final String TIPOLOGIA_PRODUZIONE = "tipologiaProduzione";

}


package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UnitaTecnicoEconomicheModel.class)
public abstract class UnitaTecnicoEconomicheModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, PersonaModel> persona;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> identificativoUte;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, LocalDate> dataApertura;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> codiceIstatComune;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, LocalDate> dataCessazione;
	public static volatile ListAttribute<UnitaTecnicoEconomicheModel, DestinazioneUsoModel> destinazioneUso;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> settoreMerceologico;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> provincia;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> numeroCivico;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> frazione;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> via;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, LocalDate> dataDenunciaCessazione;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> indirizzoPec;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, LocalDate> dataDenunciaInizioAttivita;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> cap;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> comune;
	public static volatile ListAttribute<UnitaTecnicoEconomicheModel, AttivitaAtecoModel> attivitaAteco;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> telefono;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> causaleCessazione;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> attivita;
	public static volatile SingularAttribute<UnitaTecnicoEconomicheModel, String> toponimo;

	public static final String PERSONA = "persona";
	public static final String IDENTIFICATIVO_UTE = "identificativoUte";
	public static final String DATA_APERTURA = "dataApertura";
	public static final String CODICE_ISTAT_COMUNE = "codiceIstatComune";
	public static final String DATA_CESSAZIONE = "dataCessazione";
	public static final String DESTINAZIONE_USO = "destinazioneUso";
	public static final String SETTORE_MERCEOLOGICO = "settoreMerceologico";
	public static final String PROVINCIA = "provincia";
	public static final String NUMERO_CIVICO = "numeroCivico";
	public static final String FRAZIONE = "frazione";
	public static final String VIA = "via";
	public static final String DATA_DENUNCIA_CESSAZIONE = "dataDenunciaCessazione";
	public static final String INDIRIZZO_PEC = "indirizzoPec";
	public static final String DATA_DENUNCIA_INIZIO_ATTIVITA = "dataDenunciaInizioAttivita";
	public static final String CAP = "cap";
	public static final String COMUNE = "comune";
	public static final String ATTIVITA_ATECO = "attivitaAteco";
	public static final String TELEFONO = "telefono";
	public static final String CAUSALE_CESSAZIONE = "causaleCessazione";
	public static final String ATTIVITA = "attivita";
	public static final String TOPONIMO = "toponimo";

}


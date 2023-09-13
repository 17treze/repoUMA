package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ColturaGruppiModel.class)
public abstract class ColturaGruppiModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<ColturaGruppiModel, String> codiceSuolo;
	public static volatile SingularAttribute<ColturaGruppiModel, String> codiceVarieta;
	public static volatile SingularAttribute<ColturaGruppiModel, GruppoLavorazioneModel> gruppoLavorazione;
	public static volatile SingularAttribute<ColturaGruppiModel, String> codiceUso;
	public static volatile SingularAttribute<ColturaGruppiModel, Integer> annoFine;
	public static volatile SingularAttribute<ColturaGruppiModel, String> codiceQualita;
	public static volatile SingularAttribute<ColturaGruppiModel, Integer> annoInizio;
	public static volatile SingularAttribute<ColturaGruppiModel, String> codiceDestUso;

	public static final String CODICE_SUOLO = "codiceSuolo";
	public static final String CODICE_VARIETA = "codiceVarieta";
	public static final String GRUPPO_LAVORAZIONE = "gruppoLavorazione";
	public static final String CODICE_USO = "codiceUso";
	public static final String ANNO_FINE = "annoFine";
	public static final String CODICE_QUALITA = "codiceQualita";
	public static final String ANNO_INIZIO = "annoInizio";
	public static final String CODICE_DEST_USO = "codiceDestUso";

}


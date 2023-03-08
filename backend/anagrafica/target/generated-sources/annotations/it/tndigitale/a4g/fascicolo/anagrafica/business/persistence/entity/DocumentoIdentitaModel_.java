package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DocumentoIdentitaModel.class)
public abstract class DocumentoIdentitaModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<DocumentoIdentitaModel, String> numero;
	public static volatile SingularAttribute<DocumentoIdentitaModel, LocalDate> dataRilascio;
	public static volatile SingularAttribute<DocumentoIdentitaModel, FascicoloModel> fascicolo;
	public static volatile SingularAttribute<DocumentoIdentitaModel, byte[]> documento;
	public static volatile SingularAttribute<DocumentoIdentitaModel, String> codiceFiscale;
	public static volatile SingularAttribute<DocumentoIdentitaModel, String> tipologia;
	public static volatile SingularAttribute<DocumentoIdentitaModel, LocalDate> dataScadenza;

	public static final String NUMERO = "numero";
	public static final String DATA_RILASCIO = "dataRilascio";
	public static final String FASCICOLO = "fascicolo";
	public static final String DOCUMENTO = "documento";
	public static final String CODICE_FISCALE = "codiceFiscale";
	public static final String TIPOLOGIA = "tipologia";
	public static final String DATA_SCADENZA = "dataScadenza";

}


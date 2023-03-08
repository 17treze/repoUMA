package it.tndigitale.a4g.territorio.business.persistence.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DocumentoConduzioneModel.class)
public abstract class DocumentoConduzioneModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<DocumentoConduzioneModel, LocalDate> dataInserimento;
	public static volatile SingularAttribute<DocumentoConduzioneModel, byte[]> contratto;
	public static volatile SingularAttribute<DocumentoConduzioneModel, ConduzioneModel> conduzione;
	public static volatile SingularAttribute<DocumentoConduzioneModel, LocalDate> dataInizioValidita;
	public static volatile SingularAttribute<DocumentoConduzioneModel, TipoDocumentoConduzioneModel> tipoDocumentoConduzione;
	public static volatile SingularAttribute<DocumentoConduzioneModel, LocalDate> dataFineValidita;

	public static final String DATA_INSERIMENTO = "dataInserimento";
	public static final String CONTRATTO = "contratto";
	public static final String CONDUZIONE = "conduzione";
	public static final String DATA_INIZIO_VALIDITA = "dataInizioValidita";
	public static final String TIPO_DOCUMENTO_CONDUZIONE = "tipoDocumentoConduzione";
	public static final String DATA_FINE_VALIDITA = "dataFineValidita";

}


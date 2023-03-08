package it.tndigitale.a4g.uma.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UtilizzoMacchinariModel.class)
public abstract class UtilizzoMacchinariModel_ extends it.tndigitale.a4g.framework.repository.model.EntitaDominio_ {

	public static volatile SingularAttribute<UtilizzoMacchinariModel, String> marca;
	public static volatile SingularAttribute<UtilizzoMacchinariModel, String> possesso;
	public static volatile SingularAttribute<UtilizzoMacchinariModel, String> descrizione;
	public static volatile SingularAttribute<UtilizzoMacchinariModel, String> classe;
	public static volatile SingularAttribute<UtilizzoMacchinariModel, Boolean> flagUtilizzo;
	public static volatile SingularAttribute<UtilizzoMacchinariModel, RichiestaCarburanteModel> richiestaCarburante;
	public static volatile SingularAttribute<UtilizzoMacchinariModel, Long> identificativoAgs;
	public static volatile SingularAttribute<UtilizzoMacchinariModel, TipoCarburante> alimentazione;
	public static volatile SingularAttribute<UtilizzoMacchinariModel, String> targa;

	public static final String MARCA = "marca";
	public static final String POSSESSO = "possesso";
	public static final String DESCRIZIONE = "descrizione";
	public static final String CLASSE = "classe";
	public static final String FLAG_UTILIZZO = "flagUtilizzo";
	public static final String RICHIESTA_CARBURANTE = "richiestaCarburante";
	public static final String IDENTIFICATIVO_AGS = "identificativoAgs";
	public static final String ALIMENTAZIONE = "alimentazione";
	public static final String TARGA = "targa";

}


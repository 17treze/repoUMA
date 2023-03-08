package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ModoPagamentoModel.class)
public abstract class ModoPagamentoModel_ extends it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_ {

	public static volatile SingularAttribute<ModoPagamentoModel, String> denominazioneIstituto;
	public static volatile SingularAttribute<ModoPagamentoModel, String> cittaFiliale;
	public static volatile SingularAttribute<ModoPagamentoModel, FascicoloModel> fascicolo;
	public static volatile SingularAttribute<ModoPagamentoModel, String> iban;
	public static volatile SingularAttribute<ModoPagamentoModel, String> denominazioneFiliale;
	public static volatile SingularAttribute<ModoPagamentoModel, String> bic;

	public static final String DENOMINAZIONE_ISTITUTO = "denominazioneIstituto";
	public static final String CITTA_FILIALE = "cittaFiliale";
	public static final String FASCICOLO = "fascicolo";
	public static final String IBAN = "iban";
	public static final String DENOMINAZIONE_FILIALE = "denominazioneFiliale";
	public static final String BIC = "bic";

}


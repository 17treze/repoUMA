package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;

public interface IRichiestaSuperficie {

	Long getId();

	String getCodiceColtura3();

	String getCodiceColtura5();

	BigDecimal getSupRichiesta();

	BigDecimal getSupRichiestaNetta();

	Long getIdInterventoDu();

	Long getIdDomanda();

	// private Particella infoCatastali;

	Long getIdParticella();

	String getComune();

	String getCodNazionale();

	Long getFoglio();

	String getParticella();

	String getSub();

	// private InformazioniColtivazione infoColtivazione;
	Long getIdPianoColture();

	Long getIdColtura();

	String getCodColtura3();

	String getCodColtura5();

	String getCodLivello();

	String getDescrizioneColtura();

	Long getCoefficienteTara();

	Long getSuperficieDichiarata();

	String getDescMantenimento();

	// private RiferimentiCartografici riferimentiCartografici;

	Long getIdParcella();

	Long getIdIsola();

	String getCodIsola();

}

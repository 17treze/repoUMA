package it.tndigitale.a4gistruttoria.dto;

public interface IDettaglioParticella {
	String getCodiceColtura3();

	Long getIdParticella();

	String getComune();

	String getCodNazionale();

	Long getFoglio();

	String getParticella();

	String getSub();

	String getTipoColtura();

	String getTipoSeminativo();

	Boolean getColturaPrincipale();

	Boolean getSecondaColtura();

	Boolean getAzotoFissatrice();

	Float getSuperficieImpegnata();

	Float getSuperficieEleggibile();

	Float getSuperficieSigeco();

	Boolean getAnomalieMantenimento();

	Boolean getAnomalieCoordinamento();

	Float getSuperficieDeterminata();

	String getPascolo();
	
	Float getSuperficieAnomalieCoordinamento();
	
	Float getSuperficieScostamento();
}

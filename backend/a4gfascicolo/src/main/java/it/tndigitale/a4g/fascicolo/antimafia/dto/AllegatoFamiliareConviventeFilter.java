package it.tndigitale.a4g.fascicolo.antimafia.dto;

public class AllegatoFamiliareConviventeFilter {

	private Long idDichiarazioneAntimafia;
	private Long idAllegatoFamiliariConviventi;
	
	public AllegatoFamiliareConviventeFilter() {
		super();
	}
	
	public AllegatoFamiliareConviventeFilter(Long idDichiarazioneAntimafia, Long idAllegatoFamiliariConviventi) {
		super();
		this.idDichiarazioneAntimafia = idDichiarazioneAntimafia;
		this.idAllegatoFamiliariConviventi = idAllegatoFamiliariConviventi;
	}

	public Long getIdDichiarazioneAntimafia() {
		return idDichiarazioneAntimafia;
	}
	public void setIdDichiarazioneAntimafia(Long idDichiarazioneAntimafia) {
		this.idDichiarazioneAntimafia = idDichiarazioneAntimafia;
	}
	public Long getIdAllegatoFamiliariConviventi() {
		return idAllegatoFamiliariConviventi;
	}
	public void setIdAllegatoFamiliariConviventi(Long idAllegatoFamiliariConviventi) {
		this.idAllegatoFamiliariConviventi = idAllegatoFamiliariConviventi;
	}
}

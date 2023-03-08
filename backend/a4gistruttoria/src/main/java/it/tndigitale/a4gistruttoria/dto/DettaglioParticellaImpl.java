package it.tndigitale.a4gistruttoria.dto;

public class DettaglioParticellaImpl implements IDettaglioParticella {

    private String codiceColtura3;

    private Long idParticella;

    private String comune;

    private String codNazionale;

    private Long foglio;

    private String particella;

    private String sub;

    private String tipoColtura;

    private String tipoSeminativo;

    private Boolean colturaPrincipale;

    private Boolean secondaColtura;

    private Boolean azotoFissatrice;

    private Float superficieImpegnata;

    private Float superficieEleggibile;

    private Float superficieSigeco;

    private Boolean anomalieMantenimento;

    private Boolean anomalieCoordinamento;

    private Float superficieDeterminata;

    private String pascolo;
    
    private Float superficieAnomalieCoordinamento;
    
    private Float superficieScostamento;
    

    @Override
    public String getCodiceColtura3() {
        return codiceColtura3;
    }

    public DettaglioParticellaImpl setCodiceColtura3(String codiceColtura3) {
        this.codiceColtura3 = codiceColtura3;
        return this;
    }

    @Override
    public Long getIdParticella() {
        return idParticella;
    }

    public DettaglioParticellaImpl setIdParticella(Long idParticella) {
        this.idParticella = idParticella;
        return this;
    }

    @Override
    public String getComune() {
        return comune;
    }

    public DettaglioParticellaImpl setComune(String comune) {
        this.comune = comune;
        return this;
    }

    @Override
    public String getCodNazionale() {
        return codNazionale;
    }

    public DettaglioParticellaImpl setCodNazionale(String codNazionale) {
        this.codNazionale = codNazionale;
        return this;
    }

    @Override
    public Long getFoglio() {
        return foglio;
    }

    public DettaglioParticellaImpl setFoglio(Long foglio) {
        this.foglio = foglio;
        return this;
    }

    @Override
    public String getParticella() {
        return particella;
    }

    public DettaglioParticellaImpl setParticella(String particella) {
        this.particella = particella;
        return this;
    }

    @Override
    public String getSub() {
        return sub;
    }

    public DettaglioParticellaImpl setSub(String sub) {
        this.sub = sub;
        return this;
    }

    @Override
    public String getTipoColtura() {
        return tipoColtura;
    }

    public DettaglioParticellaImpl setTipoColtura(String tipoColtura) {
        this.tipoColtura = tipoColtura;
        return this;
    }

    @Override
    public String getTipoSeminativo() {
        return tipoSeminativo;
    }

    public DettaglioParticellaImpl setTipoSeminativo(String tipoSeminativo) {
        this.tipoSeminativo = tipoSeminativo;
        return this;
    }

    @Override
    public Boolean getColturaPrincipale() {
        return colturaPrincipale;
    }

    public DettaglioParticellaImpl setColturaPrincipale(Boolean colturaPrincipale) {
        this.colturaPrincipale = colturaPrincipale;
        return this;
    }

    @Override
    public Boolean getSecondaColtura() {
        return secondaColtura;
    }

    public DettaglioParticellaImpl setSecondaColtura(Boolean secondaColtura) {
        this.secondaColtura = secondaColtura;
        return this;
    }

    @Override
    public Boolean getAzotoFissatrice() {
        return azotoFissatrice;
    }

    public DettaglioParticellaImpl setAzotoFissatrice(Boolean azotoFissatrice) {
        this.azotoFissatrice = azotoFissatrice;
        return this;
    }

    @Override
    public Float getSuperficieImpegnata() {
        return superficieImpegnata;
    }

    public DettaglioParticellaImpl setSuperficieImpegnata(Float superficieImpegnata) {
        this.superficieImpegnata = superficieImpegnata;
        return this;
    }

    @Override
    public Float getSuperficieEleggibile() {
        return superficieEleggibile;
    }

    public DettaglioParticellaImpl setSuperficieEleggibile(Float superficieEleggibile) {
        this.superficieEleggibile = superficieEleggibile;
        return this;
    }

    @Override
    public Float getSuperficieSigeco() {
        return superficieSigeco;
    }

    public DettaglioParticellaImpl setSuperficieSigeco(Float superficieSigeco) {
        this.superficieSigeco = superficieSigeco;
        return this;
    }

    @Override
    public Boolean getAnomalieMantenimento() {
        return anomalieMantenimento;
    }

    public DettaglioParticellaImpl setAnomalieMantenimento(Boolean anomalieMantenimento) {
        this.anomalieMantenimento = anomalieMantenimento;
        return this;
    }

    @Override
    public Boolean getAnomalieCoordinamento() {
        return anomalieCoordinamento;
    }

    public DettaglioParticellaImpl setAnomalieCoordinamento(Boolean anomalieCoordinamento) {
        this.anomalieCoordinamento = anomalieCoordinamento;
        return this;
    }

    @Override
    public Float getSuperficieDeterminata() {
        return superficieDeterminata;
    }

    public DettaglioParticellaImpl setSuperficieDeterminata(Float superficieDeterminata) {
        this.superficieDeterminata = superficieDeterminata;
        return this;
    }

    @Override
    public String getPascolo() {
        return pascolo;
    }

    public DettaglioParticellaImpl setPascolo(String pascolo) {
        this.pascolo = pascolo;
        return this;
    }

    @Override
	public Float getSuperficieAnomalieCoordinamento() {
		return superficieAnomalieCoordinamento;
	}

	public DettaglioParticellaImpl setSuperficieAnomalieCoordinamento(Float superficieAnomalieCoordinamento) {
		this.superficieAnomalieCoordinamento = superficieAnomalieCoordinamento;
		return this;
	}

	@Override
	public Float getSuperficieScostamento() {
		return superficieScostamento;
	}

	public DettaglioParticellaImpl setSuperficieScostamento(Float superficieScostamento) {
		this.superficieScostamento = superficieScostamento;
		return this;
	}
}

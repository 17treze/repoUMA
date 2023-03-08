package it.tndigitale.a4g.ags.dto.domandaunica;

public class SuperficieImpegnata {

	private Particella particella;
	private Coltura coltura;
	private Long idParcella;
	private Long idIsola;
	private String codIsola;
	private Long supDichiarata;
	private Long supImpegnata;
	public Particella getParticella() {
		return particella;
	}
	public Coltura getColtura() {
		return coltura;
	}
	public Long getIdParcella() {
		return idParcella;
	}
	public Long getIdIsola() {
		return idIsola;
	}
	public String getCodIsola() {
		return codIsola;
	}
	public Long getSupDichiarata() {
		return supDichiarata;
	}
	public Long getSupImpegnata() {
		return supImpegnata;
	}
	public void setParticella(Particella particella) {
		this.particella = particella;
	}
	public void setColtura(Coltura coltura) {
		this.coltura = coltura;
	}
	public void setIdParcella(Long idParcella) {
		this.idParcella = idParcella;
	}
	public void setIdIsola(Long idIsola) {
		this.idIsola = idIsola;
	}
	public void setCodIsola(String codIsola) {
		this.codIsola = codIsola;
	}
	public void setSupDichiarata(Long supDichiarata) {
		this.supDichiarata = supDichiarata;
	}
	public void setSupImpegnata(Long supImpegnata) {
		this.supImpegnata = supImpegnata;
	}
	
}

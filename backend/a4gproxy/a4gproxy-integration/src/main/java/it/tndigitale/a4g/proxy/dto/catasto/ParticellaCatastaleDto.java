package it.tndigitale.a4g.proxy.dto.catasto;

import java.math.BigInteger;

public class ParticellaCatastaleDto {

	private BigInteger comuneCatastale;
	private String particella;
	private TipologiaParticellaCatastale tipologia;
	private EntitaCatastaleStato stato;

	private PartitaTavolareDto partitaTavolare;

	public BigInteger getComuneCatastale() {
		return comuneCatastale;
	}

	public ParticellaCatastaleDto setComuneCatastale(BigInteger comuneCatastale) {
		this.comuneCatastale = comuneCatastale;
		return this;
	}

	public String getParticella() {
		return particella;
	}

	public ParticellaCatastaleDto setParticella(String particella) {
		this.particella = particella;
		return this;
	}

	public TipologiaParticellaCatastale getTipologia() {
		return tipologia;
	}

	public ParticellaCatastaleDto setTipologia(TipologiaParticellaCatastale tipologia) {
		this.tipologia = tipologia;
		return this;
	}

	public EntitaCatastaleStato getStato() {
		return stato;
	}

	public ParticellaCatastaleDto setStato(EntitaCatastaleStato stato) {
		this.stato = stato;
		return this;
	}

	public PartitaTavolareDto getPartitaTavolare() {
		return partitaTavolare;
	}

	public ParticellaCatastaleDto setPartitaTavolare(PartitaTavolareDto partitaTavolare) {
		this.partitaTavolare = partitaTavolare;
		return this;
	}
}

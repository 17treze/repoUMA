package it.tndigitale.a4g.proxy.dto.catasto;

import java.math.BigInteger;
import java.util.List;

public class InformazioniParticellaDto extends ParticellaCatastaleDto {

	protected BigInteger superficie;

	public BigInteger getSuperficie() {
		return superficie;
	}

	public InformazioniParticellaDto setSuperficie(BigInteger superficie) {
		this.superficie = superficie;
		return this;
	}
//	List<QualitaColturaDto> qualita;
//
//	public List<QualitaColturaDto> getQualita() {
//		return qualita;
//	}
//
//	public void setQualita(List<QualitaColturaDto> qualita) {
//		this.qualita = qualita;
//	}
}

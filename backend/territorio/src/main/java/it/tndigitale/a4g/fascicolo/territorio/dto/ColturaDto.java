package it.tndigitale.a4g.fascicolo.territorio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import it.tndigitale.a4g.fascicolo.territorio.dto.legacy.CriterioMantenimento;

@Schema
public class ColturaDto {

	private Integer superficieAccertata;
	private Integer superficieDichiarata;

	@Schema(enumAsRef = true)
	private CriterioMantenimento criterioMantenimento;
	private CodificaColtura codifica;

	public Integer getSuperficieAccertata() {
		return superficieAccertata;
	}
	public ColturaDto setSuperficieAccertata(Integer superficieAccertata) {
		this.superficieAccertata = superficieAccertata;
		return this;
	}
	public CodificaColtura getCodifica() {
		return codifica;
	}
	public ColturaDto setCodifica(CodificaColtura codifica) {
		this.codifica = codifica;
		return this;
	}
	public Integer getSuperficieDichiarata() {
		return superficieDichiarata;
	}
	public ColturaDto setSuperficieDichiarata(Integer superficieDichiarata) {
		this.superficieDichiarata = superficieDichiarata;
		return this;
	}
	public CriterioMantenimento getCriterioMantenimento() {
		return criterioMantenimento;
	}
	public ColturaDto setCriterioMantenimento(CriterioMantenimento criterioMantenimento) {
		this.criterioMantenimento = criterioMantenimento;
		return this;
	}
}

package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;

public class InterventoDto {
	private Long id;
	private String codiceAgea;
	private String descrizioneBreve;
	private String descrizioneLunga;
	private String descrizioneSettore;
	private String identificativoIntervento;
	private String identificativoSettore;
	private String misura;
	private Sostegno identificativoSostegno;

	public String getCodiceAgea() {
		return codiceAgea;
	}

	public String getDescrizioneBreve() {
		return descrizioneBreve;
	}

	public String getDescrizioneLunga() {
		return descrizioneLunga;
	}

	public String getDescrizioneSettore() {
		return descrizioneSettore;
	}

	public String getIdentificativoIntervento() {
		return identificativoIntervento;
	}

	public String getIdentificativoSettore() {
		return identificativoSettore;
	}

	public String getMisura() {
		return misura;
	}

	public void setCodiceAgea(String codiceAgea) {
		this.codiceAgea = codiceAgea;
	}

	public void setDescrizioneBreve(String descrizioneBreve) {
		this.descrizioneBreve = descrizioneBreve;
	}

	public void setDescrizioneLunga(String descrizioneLunga) {
		this.descrizioneLunga = descrizioneLunga;
	}

	public void setDescrizioneSettore(String descrizioneSettore) {
		this.descrizioneSettore = descrizioneSettore;
	}

	public void setIdentificativoIntervento(String identificativoIntervento) {
		this.identificativoIntervento = identificativoIntervento;
	}

	public void setIdentificativoSettore(String identificativoSettore) {
		this.identificativoSettore = identificativoSettore;
	}

	public void setMisura(String misura) {
		this.misura = misura;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sostegno getIdentificativoSostegno() {
		return identificativoSostegno;
	}

	public void setIdentificativoSostegno(Sostegno identificativoSostegno) {
		this.identificativoSostegno = identificativoSostegno;
	}

}

package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.ArrayList;
import java.util.List;

public class DettaglioSuperficieCalcoloDto {
	private Long idDomanda;
	private String codiceCultura;
	private Long idParticella;
	private List<Long> idParcelle;
	
	public Long getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}
	public String getCodiceCultura() {
		return codiceCultura;
	}
	public void setCodiceCultura(String codiceCultura) {
		this.codiceCultura = codiceCultura;
	}
	public Long getIdParticella() {
		return idParticella;
	}
	public void setIdParticella(final Long idParticella) {
		this.idParticella = idParticella;
	}
	public List<Long> getIdParcelle() {
		if (idParcelle == null) {
			idParcelle = new ArrayList<Long>();
		}
		return idParcelle;
	}
	public void setIdParcelle(final List<Long> idParcelle) {
		this.idParcelle = idParcelle;
	}
}

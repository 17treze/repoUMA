package it.tndigitale.a4gistruttoria.dto.lavorazione;

import it.tndigitale.a4gistruttoria.dto.InformazioniColtivazione;
import it.tndigitale.a4gistruttoria.dto.Particella;

public class DettaglioDatiSuperficeImpegnataACS {

	private Particella datiCatastali;
	private InformazioniColtivazione datiColtivazione;
	private RiferimentiCartografici riferimentiCartografici;

	private Double supRichiesta;
	private Double supRichiestaNetta;
	
	public Particella getDatiCatastali() {
		return datiCatastali;
	}
	public void setDatiCatastali(Particella datiCatastali) {
		this.datiCatastali = datiCatastali;
	}
	public InformazioniColtivazione getDatiColtivazione() {
		return datiColtivazione;
	}
	public void setDatiColtivazione(InformazioniColtivazione datiColtivazione) {
		this.datiColtivazione = datiColtivazione;
	}
	public RiferimentiCartografici getRiferimentiCartografici() {
		return riferimentiCartografici;
	}
	public void setRiferimentiCartografici(RiferimentiCartografici riferimentiCartografici) {
		this.riferimentiCartografici = riferimentiCartografici;
	}
	public Double getSupRichiesta() {
		return supRichiesta;
	}
	public void setSupRichiesta(Double supRichiesta) {
		this.supRichiesta = supRichiesta;
	}
	public Double getSupRichiestaNetta() {
		return supRichiestaNetta;
	}
	public void setSupRichiestaNetta(Double supRichiestaNetta) {
		this.supRichiestaNetta = supRichiestaNetta;
	}
}

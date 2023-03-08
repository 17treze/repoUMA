package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.io.Serializable;
import java.math.BigDecimal;

import it.tndigitale.a4gistruttoria.dto.InformazioniColtivazione;
import it.tndigitale.a4gistruttoria.dto.Particella;
import it.tndigitale.a4gistruttoria.dto.lavorazione.RiferimentiCartografici;

public class RichiestaSuperficie implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String codiceColtura3;

	private String codiceColtura5;

	private Particella infoCatastali;

	private InformazioniColtivazione infoColtivazione;

	private RiferimentiCartografici riferimentiCartografici;

	private BigDecimal supRichiesta;

	private BigDecimal supRichiestaNetta;

	private Long idInterventoDu;

	private Long idDomanda;

	public String getCodiceColtura3() {
		return this.codiceColtura3;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdInterventoDu() {
		return idInterventoDu;
	}

	public void setIdInterventoDu(Long idInterventoDu) {
		this.idInterventoDu = idInterventoDu;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public void setCodiceColtura3(String codiceColtura3) {
		this.codiceColtura3 = codiceColtura3;
	}

	public String getCodiceColtura5() {
		return this.codiceColtura5;
	}

	public void setCodiceColtura5(String codiceColtura5) {
		this.codiceColtura5 = codiceColtura5;
	}

	public Particella getInfoCatastali() {
		return this.infoCatastali;
	}

	public void setInfoCatastali(Particella infoCatastali) {
		this.infoCatastali = infoCatastali;
	}

	public InformazioniColtivazione getInfoColtivazione() {
		return this.infoColtivazione;
	}

	public void setInfoColtivazione(InformazioniColtivazione infoColtivazione) {
		this.infoColtivazione = infoColtivazione;
	}

	public RiferimentiCartografici getRiferimentiCartografici() {
		return this.riferimentiCartografici;
	}

	public void setRiferimentiCartografici(RiferimentiCartografici riferimentiCartografici) {
		this.riferimentiCartografici = riferimentiCartografici;
	}

	public BigDecimal getSupRichiesta() {
		return this.supRichiesta;
	}

	public void setSupRichiesta(BigDecimal supRichiesta) {
		this.supRichiesta = supRichiesta;
	}

	public BigDecimal getSupRichiestaNetta() {
		return this.supRichiestaNetta;
	}

	public void setSupRichiestaNetta(BigDecimal supRichiestaNetta) {
		this.supRichiestaNetta = supRichiestaNetta;
	}
}
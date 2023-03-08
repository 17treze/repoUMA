package it.tndigitale.a4gistruttoria.dto.domandaunica;

import java.time.LocalDateTime;
import java.util.List;

import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

/**
 * Usata dai servizi mobile per recuperare tutte le informazioni da visualizzare, tipo debiti, anomalie e importi
 * @author s.caccia
 *
 */
@Deprecated
public class Istruttoria {
	private Long id;
	private TipoIstruttoria tipoIstruttoria;
	private Sostegno sostegno;
	private StatoIstruttoria statoLavorazioneSostegno;
	private LocalDateTime dtUltimoCalcolo;
	private List<EsitoControllo> esitiControlli;
	private ImportiIstruttoria importiIstruttoria;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ImportiIstruttoria getImportiIstruttoria() {
		return importiIstruttoria;
	}
	public Istruttoria setImportiIstruttoria(ImportiIstruttoria importiIstruttoria) {
		this.importiIstruttoria = importiIstruttoria;
		return this;
	}
	public TipoIstruttoria getTipoIstruttoria() {
		return tipoIstruttoria;
	}
	public Istruttoria setTipoIstruttoria(TipoIstruttoria tipoIstruttoria) {
		this.tipoIstruttoria = tipoIstruttoria;
		return this;
	}
	public LocalDateTime getDtUltimoCalcolo() {
		return dtUltimoCalcolo;
	}
	public void setDtUltimoCalcolo(LocalDateTime dtUltimoCalcolo) {
		this.dtUltimoCalcolo = dtUltimoCalcolo;
	}
	public Sostegno getSostegno() {
		return sostegno;
	}
	public Istruttoria setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
		return this;
	}
	public StatoIstruttoria getStatoLavorazioneSostegno() {
		return statoLavorazioneSostegno;
	}
	public Istruttoria setStatoLavorazioneSostegno(StatoIstruttoria statoLavorazioneSostegno) {
		this.statoLavorazioneSostegno = statoLavorazioneSostegno;
		return this;
	}
	public List<EsitoControllo> getEsitiControlli() {
		return esitiControlli;
	}
	public Istruttoria setEsitiControlli(List<EsitoControllo> esitiControlli) {
		this.esitiControlli = esitiControlli;
		return this;
	}
}
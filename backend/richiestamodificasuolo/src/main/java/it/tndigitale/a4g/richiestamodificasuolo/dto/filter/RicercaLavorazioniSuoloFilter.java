package it.tndigitale.a4g.richiestamodificasuolo.dto.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;

@ApiModel(description = "Rappresenta il modello alternativo dei filtri di ricerca delle lavorazioni suolo in Ricerca Richieste")
public class RicercaLavorazioniSuoloFilter implements Serializable {

	private static final long serialVersionUID = 8271459863321786394L;

	@ApiParam(value = "Utente lavorazioni da ricercare")
	private String utente;

	@ApiParam(value = "id lavorazioni suolo da ricercare")
	private Long idLavorazione;

	@ApiParam(value = "Titolo lavorazione")
	private String titolo;

	@ApiParam(value = "Cuaa")
	private String cuaa;

	@ApiParam(value = "StatoLavorazioneSuolo da ricercare")
	private List<StatoLavorazioneSuolo> statoLavorazione;

	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public Long getIdLavorazione() {
		return idLavorazione;
	}
	public void setIdLavorazione(Long idLavorazione) {
		this.idLavorazione = idLavorazione;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public List<StatoLavorazioneSuolo> getStatoLavorazione() {
		if (statoLavorazione == null || statoLavorazione.isEmpty()){
			//Se non è presente uno stato da ricercare allora si da per scontato che debbano essere ricercati tutti
			return Arrays.asList(StatoLavorazioneSuolo.values());
		}else {
			return statoLavorazione;
		}
	}
	public void setStatoLavorazione(List<StatoLavorazioneSuolo> statoLavorazione) {
		this.statoLavorazione = statoLavorazione;
	}

	public List<StatoLavorazioneSuolo> getStatiToSearchNotIn(List<StatoLavorazioneSuolo> statiNotToSearch){
		//In input una lista di stati da rimuovere da tutti gli stati possibili e restituisce una lista di stati ricercabili
		List<StatoLavorazioneSuolo> statiPossibili = Arrays.asList(StatoLavorazioneSuolo.values());
		List<StatoLavorazioneSuolo> statiToSearch = new ArrayList<>();

		boolean flgSearch;
		for (int i = 0; i < statiPossibili.size(); i++){
			flgSearch = false;
			for (int n = 0; n < statiNotToSearch.size(); n++){
				if (statiPossibili.get(i).equals(statiNotToSearch.get(n))){
					flgSearch = true;
				}
			}
			if (!flgSearch) {
				statiToSearch.add(statiPossibili.get(i));
			}
		}
		return statoLavorazione = statiToSearch;
	}

	@Override
	public String toString() {
		return "RicercaLavorazioniSuoloFilter{" +
				"utente=" + utente +
				", idLavorazione=" + idLavorazione +
				", titolo='" + titolo + '\'' +
				", cuaa='" + cuaa + '\'' +
				", statoLavorazione=" + statoLavorazione +
				'}';
	}
}

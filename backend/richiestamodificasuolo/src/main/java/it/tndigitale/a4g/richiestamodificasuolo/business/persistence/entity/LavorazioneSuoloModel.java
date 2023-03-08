package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_LAVORAZIONE_SUOLO")
@DynamicUpdate
public class LavorazioneSuoloModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7083768917336263479L;

	@Column(name = "UTENTE")
	private String utente;

	@Column(name = "UTENTE_AGS")
	private String utenteAgs;

	@Enumerated(EnumType.STRING)
	private StatoLavorazioneSuolo stato;

	@Column(name = "CAMPAGNA")
	private Integer campagna;

	@Column(name = "DATA_INIZIO_LAVORAZIONE")
	private LocalDateTime dataInizioLavorazione;

	@Column(name = "DATA_FINE_LAVORAZIONE")
	private LocalDateTime dataFineLavorazione;

	@Column(name = "DATA_ULTIMA_MODIFICA")
	private LocalDateTime dataUltimaModifica;

	@Enumerated(EnumType.STRING)
	@Column(name = "MODALITA_ADL")
	private ModalitaADL modalitaADL;

	@Column(name = "X_ULTIMO_ZOOM")
	private Double xUltimoZoom;

	@Column(name = "Y_ULTIMO_ZOOM")
	private Double yUltimoZoom;

	@Column(name = "SCALA_ULTIMO_ZOOM")
	private Double scalaUltimoZoom;

	private String note;

	private String titolo;

	private String sopralluogo;

	@Column(name = "ID_LAVORAZIONE_PADRE")
	private Long idLavorazionePadre;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = false, mappedBy = "lavorazioneSuolo")
	private List<SuoloDichiaratoModel> suoloDichiaratoModel;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = false, mappedBy = "idLavorazioneInCorso")
	private List<SuoloModel> listaSuoloInCorsoModel;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idLavorazioneInizio")
	private List<SuoloModel> listaSuoloInizioModel;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idLavorazioneFine")
	private List<SuoloModel> listaSuoloFineModel;

	public Long getIdLavorazionePadre() {
		return idLavorazionePadre;
	}

	public void setIdLavorazionePadre(Long idLavorazionePadre) {
		this.idLavorazionePadre = idLavorazionePadre;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "idLavorazioneWorkspaceLavSuolo")
	private List<WorkspaceLavSuoloModel> listaLavorazioneWorkspaceModel;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "idLavorazioneWorkspaceTmp")
	private List<WorkspaceTmpModel> listaLavorazioneWorkspaceTmpModel;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "lavorazioneSuoloInAnomaliaValidazione")
	private List<AnomaliaValidazioneModel> listaAnomalieValidazione;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "lavorazioneSuolo")
	private List<AreaDiLavoroModel> listaAreadiLavoro;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "lavorazioneSuolo")
	private List<TempClipSuADLModel> listaTempClipSuADL;

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public StatoLavorazioneSuolo getStato() {
		return stato;
	}

	public String getUtenteAgs() {
		return utenteAgs;
	}

	public void setUtenteAgs(String utenteAgs) {
		this.utenteAgs = utenteAgs;
	}

	public void setStato(StatoLavorazioneSuolo stato) {
		this.stato = stato;
	}

	public LocalDateTime getDataInizioLavorazione() {
		return dataInizioLavorazione;
	}

	public void setDataInizioLavorazione(LocalDateTime dataInizioLavorazione) {
		this.dataInizioLavorazione = dataInizioLavorazione;
	}

	public LocalDateTime getDataFineLavorazione() {
		return dataFineLavorazione;
	}

	public void setDataFineLavorazione(LocalDateTime dataFineLavorazione) {
		this.dataFineLavorazione = dataFineLavorazione;
	}

	public LocalDateTime getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(LocalDateTime dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getSopralluogo() {
		return sopralluogo;
	}

	public void setSopralluogo(String sopralluogo) {
		this.sopralluogo = sopralluogo;
	}

	public Double getxUltimoZoom() {
		return xUltimoZoom;
	}

	public void setxUltimoZoom(Double xUltimoZoom) {
		this.xUltimoZoom = xUltimoZoom;
	}

	public Double getyUltimoZoom() {
		return yUltimoZoom;
	}

	public void setyUltimoZoom(Double yUltimoZoom) {
		this.yUltimoZoom = yUltimoZoom;
	}

	public Double getScalaUltimoZoom() {
		return scalaUltimoZoom;
	}

	public void setScalaUltimoZoom(Double scalaUltimoZoom) {
		this.scalaUltimoZoom = scalaUltimoZoom;
	}

	public List<SuoloDichiaratoModel> getSuoloDichiaratoModel() {
		return suoloDichiaratoModel;
	}

	public void setSuoloDichiaratoModel(List<SuoloDichiaratoModel> suoloDichiaratoModel) {
		this.suoloDichiaratoModel = suoloDichiaratoModel;
	}

	public void addSuoloDichiaratoModel(SuoloDichiaratoModel suoloDichiarato) {
		if (suoloDichiaratoModel == null) {
			suoloDichiaratoModel = new ArrayList<>();
		}
		suoloDichiarato.setLavorazioneSuolo(this);
		suoloDichiaratoModel.add(suoloDichiarato);
	}

	public void removeSuoloDichiaratoModel(SuoloDichiaratoModel suoloDichiarato) {
		suoloDichiarato.setLavorazioneSuolo(null);
		suoloDichiaratoModel.remove(suoloDichiarato);
	}

	public List<SuoloModel> getListaSuoloInCorsoModel() {
		return listaSuoloInCorsoModel;
	}

	public void setListaSuoloInCorsoModel(List<SuoloModel> listaSuoloInCorsoModel) {
		this.listaSuoloInCorsoModel = listaSuoloInCorsoModel;
	}

	public void addSuoloInCorsoModel(SuoloModel suoloInCorsoModel) {
		if (listaSuoloInCorsoModel == null) {
			listaSuoloInCorsoModel = new ArrayList<>();
		}
		suoloInCorsoModel.setIdLavorazioneInCorso(this);
		listaSuoloInCorsoModel.add(suoloInCorsoModel);
	}

	public void removeSuoloInCorsoModel(SuoloModel suoloInCorsoModel) {
		suoloInCorsoModel.setIdLavorazioneInCorso(null);
		listaSuoloInCorsoModel.remove(suoloInCorsoModel);
	}

	public List<SuoloModel> getListaSuoloInizioModel() {
		return listaSuoloInizioModel;
	}

	public void setListaSuoloInizioModel(List<SuoloModel> listaSuoloInizioModel) {
		this.listaSuoloInizioModel = listaSuoloInizioModel;
	}

	public List<SuoloModel> getListaSuoloFineModel() {
		return listaSuoloFineModel;
	}

	public void setListaSuoloFineModel(List<SuoloModel> listaSuoloFineModel) {
		this.listaSuoloFineModel = listaSuoloFineModel;
	}

	public List<WorkspaceLavSuoloModel> getListaLavorazioneWorkspaceModel() {
		return listaLavorazioneWorkspaceModel;
	}

	public void setListaLavorazioneWorkspaceModel(List<WorkspaceLavSuoloModel> listaLavorazioneWorkspaceModel) {
		this.listaLavorazioneWorkspaceModel = listaLavorazioneWorkspaceModel;
	}

	public void addWorkspaceLavSuoloModel(WorkspaceLavSuoloModel workspaceLavSuoloModel) {
		if (listaLavorazioneWorkspaceModel == null) {
			listaLavorazioneWorkspaceModel = new ArrayList<>();
		}
		workspaceLavSuoloModel.setIdLavorazioneWorkspaceLavSuolo(this);
		listaLavorazioneWorkspaceModel.add(workspaceLavSuoloModel);
	}

	public void addAdlLavSuoloModel(AreaDiLavoroModel areaDiLavoroModel) {
		if (listaAreadiLavoro == null) {
			listaAreadiLavoro = new ArrayList<>();
		}
		areaDiLavoroModel.setLavorazioneSuolo(this);
		listaAreadiLavoro.add(areaDiLavoroModel);
	}

	public void removeWorkspaceLavSuoloModel(WorkspaceLavSuoloModel workspaceLavSuoloModel) {
		workspaceLavSuoloModel.setIdLavorazioneWorkspaceLavSuolo(null);
		listaLavorazioneWorkspaceModel.remove(workspaceLavSuoloModel);
	}

	public List<WorkspaceTmpModel> getListaLavorazioneWorkspaceTmpModel() {
		return listaLavorazioneWorkspaceTmpModel;
	}

	public void setListaLavorazioneWorkspaceTmpModel(List<WorkspaceTmpModel> listaLavorazioneWorkspaceTmpModel) {
		this.listaLavorazioneWorkspaceTmpModel = listaLavorazioneWorkspaceTmpModel;
	}

	public void removeWorkspaceTmpModel(WorkspaceTmpModel workspaceTmpModel) {
		workspaceTmpModel.setIdLavorazioneWorkspaceTmp(null);
		listaLavorazioneWorkspaceTmpModel.remove(workspaceTmpModel);
	}

	public List<AnomaliaValidazioneModel> getListaAnomalieValidazione() {
		return listaAnomalieValidazione;
	}

	public void setListaAnomalieValidazione(List<AnomaliaValidazioneModel> listaAnomalieValidazione) {
		this.listaAnomalieValidazione = listaAnomalieValidazione;
	}

	public void addAnomaliaLavSuoloModel(AnomaliaValidazioneModel anomalia) {
		if (listaAnomalieValidazione == null) {
			listaAnomalieValidazione = new ArrayList<>();
		}
		anomalia.setLavorazioneSuoloInAnomaliaValidazione(this);
		listaAnomalieValidazione.add(anomalia);
	}

	public void removeAnomaliaLavSuoloModel(AnomaliaValidazioneModel anomalia) {
		anomalia.setLavorazioneSuoloInAnomaliaValidazione(null);
		listaAnomalieValidazione.remove(anomalia);
	}

	public void removeAreaDiLavoroModel(AreaDiLavoroModel areaDiLavoroModel) {
		areaDiLavoroModel.setLavorazioneSuolo(null);
		listaAreadiLavoro.remove(areaDiLavoroModel);
	}

	public void removeTempClipSuADLModel(TempClipSuADLModel tempClipSuADLModel) {
		tempClipSuADLModel.setLavorazioneSuolo(null);
		listaTempClipSuADL.remove(tempClipSuADLModel);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof LavorazioneSuoloModel))
			return false;
		return id != null && id.equals(((LavorazioneSuoloModel) o).getId());
	}

	@Override
	public int hashCode() {
		return Optional.ofNullable(id).map(i -> i.hashCode()).orElseGet(() -> getClass().hashCode());
	}

	public List<AreaDiLavoroModel> getListaAreadiLavoro() {
		return listaAreadiLavoro;
	}

	public void setListaAreadiLavoro(List<AreaDiLavoroModel> listaAreadiLavoro) {
		this.listaAreadiLavoro = listaAreadiLavoro;
	}

	public List<TempClipSuADLModel> getListaTempClipSuADL() {
		return listaTempClipSuADL;
	}

	public void setListaTempClipSuADL(List<TempClipSuADLModel> listaTempClipSuADL) {
		this.listaTempClipSuADL = listaTempClipSuADL;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public ModalitaADL getModalitaADL() {
		return modalitaADL;
	}

	public void setModalitaAdl(ModalitaADL modalitaADL) {
		this.modalitaADL = modalitaADL;
	}
}

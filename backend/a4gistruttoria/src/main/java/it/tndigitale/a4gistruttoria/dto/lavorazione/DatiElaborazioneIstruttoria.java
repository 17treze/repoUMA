package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.dto.InfoIstruttoriaDomanda;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public class DatiElaborazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(DatiElaborazioneIstruttoria.class);

	private TransizioneIstruttoriaModel transizione;
	// La dovremo leggere da transizione
	@Deprecated
	private IstruttoriaModel istruttoria;
	private List<VariabileCalcolo> variabiliInputNext;
	private List<EsitoControllo> esitiInputNext;
	private HashMap<TipologiaPassoTransizione, DatiInput> datiInput;
	private HashMap<TipologiaPassoTransizione, DatiOutput> datiOutput;
	private HashMap<TipologiaPassoTransizione, DatiSintesi> datiSintesi;
	private InfoIstruttoriaDomanda infoIstruttoria;
	private AgricoltoreSIAN infoSian;
	private List<String> coltureCompatibiliSostegno;
	private HashMap<String, DatiParticellaColtura> infoParticella;
	private Set<Long> parcelleNonAmmissibili;

	public DatiElaborazioneIstruttoria() {
		this.datiInput = new HashMap<>();
		this.datiOutput = new HashMap<>();
		this.datiSintesi = new HashMap<>();
		this.esitiInputNext = new ArrayList<>();
		this.infoParticella = new HashMap<String, DatiParticellaColtura>();
		this.parcelleNonAmmissibili = new HashSet<>();
	}

	public List<VariabileCalcolo> getVariabiliInputNext() {
		return variabiliInputNext;
	}

	public void setVariabiliInputNext(List<VariabileCalcolo> variabiliInputNext) {
		this.variabiliInputNext = variabiliInputNext;
	}

	public List<EsitoControllo> getEsitiInputNext() {
		return esitiInputNext;
	}

	public void setEsitiInputNext(List<EsitoControllo> esitiInputNext) {
		this.esitiInputNext = esitiInputNext;
	}

	public TransizioneIstruttoriaModel getTransizione() {
		return transizione;
	}

	public void setTransizione(TransizioneIstruttoriaModel transizione) {
		this.transizione = transizione;
	}

	@Deprecated
	public IstruttoriaModel getIstruttoria() {
		return istruttoria;
	}

	@Deprecated
	public void setIstruttoria(IstruttoriaModel istruttoria) {
		this.istruttoria = istruttoria;
	}

	public HashMap<TipologiaPassoTransizione, DatiInput> getDatiInput() {
		return datiInput;
	}

	public HashMap<TipologiaPassoTransizione, DatiOutput> getDatiOutput() {
		return datiOutput;
	}

	public HashMap<TipologiaPassoTransizione, DatiSintesi> getDatiSintesi() {
		return datiSintesi;
	}

	public InfoIstruttoriaDomanda getInfoIstruttoria() {
		return infoIstruttoria;
	}

	public void setInfoIstruttoria(InfoIstruttoriaDomanda infoIstruttoria) {
		this.infoIstruttoria = infoIstruttoria;
	}

	public AgricoltoreSIAN getInfoSian() {
		return infoSian;
	}

	public void setInfoSian(AgricoltoreSIAN infoSian) {
		this.infoSian = infoSian;
	}

	public List<String> getColtureCompatibiliSostegno() {
		return coltureCompatibiliSostegno;
	}

	public void setColtureCompatibiliSostegno(List<String> coltureCompatibiliSostegno) {
		this.coltureCompatibiliSostegno = coltureCompatibiliSostegno;
	}

	public HashMap<String, DatiParticellaColtura> getInfoParticella() {
		return infoParticella;
	}

	public void setInfoParticella(HashMap<String, DatiParticellaColtura> infoParticella) {
		this.infoParticella = infoParticella;
	}

	public Set<Long> getParcelleNonAmmissibili() {
		return parcelleNonAmmissibili;
	}

	public void setParcelleNonAmmissibili(Set<Long> parcelleNonAmmissibili) {
		this.parcelleNonAmmissibili = parcelleNonAmmissibili;
	}

	/**
	 * Metodo per il recupero di una variabile dalla lista di variabili di input del passo di lavorazione indicato
	 * 
	 * @param tipologiaPasso
	 *            Enum TipologiaPassoTransizione
	 * @param tipoVariabile
	 *            Enum TipoVariabile
	 * @return Oggetto VariabileCalcolo se la variabile è presente all'interno dell'input del passo indicato, altrimenti null.
	 */
	public VariabileCalcolo getVariabileInput(TipologiaPassoTransizione tipologiaPasso, TipoVariabile tipoVariabile) {
		DatiInput datiInputPasso = this.datiInput.get(tipologiaPasso);
		if (datiInputPasso != null) {
			Optional<VariabileCalcolo> variabileOpt = datiInputPasso.getVariabiliCalcolo().stream()
					.filter(v -> v.getTipoVariabile().equals(tipoVariabile))
					.findFirst();
			if (variabileOpt.isPresent()) {
				return variabileOpt.get();
			} else {
				logger.debug("getVariabileInput: la variabile " + tipoVariabile.getDescrizione() +
						" non è presente nell'input del passo " + tipologiaPasso + " per l'istruttoria "
						+ istruttoria.getId());
			}
		}
		return null;
	}

	/**
	 * Metodo per il recupero di una variabile dalla lista di variabili di output del passo di lavorazione indicato
	 * 
	 * @param tipologiaPasso
	 *            Enum TipologiaPassoTransizione
	 * @param tipoVariabile
	 *            Enum TipoVariabile
	 * @return Oggetto VariabileCalcolo se la variabile è presente all'interno dell'output del passo indicato, altrimenti null.
	 */
	public VariabileCalcolo getVariabileOutput(TipologiaPassoTransizione tipologiaPasso, TipoVariabile tipoVariabile) {
		DatiOutput datiOutputPasso = this.datiOutput.get(tipologiaPasso);
		if (datiOutputPasso != null) {
			Optional<VariabileCalcolo> variabileOpt = datiOutputPasso.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(tipoVariabile)).findFirst();
			if (variabileOpt.isPresent()) {
				return variabileOpt.get();
			} else {
				logger.debug("getVariabileOutput: la variabile " + tipoVariabile.getDescrizione() +
						" non è presente nell'output del passo " + tipologiaPasso
						+ " per l'istruttoria " + istruttoria.getId());
			}
		}
		return null;
	}

	/**
	 * Metodo per il recupero di una variabile Particella/coltura dalla lista di variabili presenti nei dati di sintesi del passo di lavorazione indicato
	 * 
	 * @param tipologiaPasso
	 *            Enum TipologiaPassoTransizione
	 * @param tipoVariabile
	 *            Enum TipoVariabile
	 * @return Oggetto VariabileCalcolo se la variabile è presente all'interno dei dati di sintesi del passo indicato, altrimenti null.
	 */
	public VariabileCalcolo getVariabileParticellaColtura(TipologiaPassoTransizione tipologiaPasso, TipoVariabile tipoVariabile) {
		DatiSintesi datiSintesiPasso = this.datiSintesi.get(tipologiaPasso);
		if (datiSintesiPasso != null) {
			Optional<VariabileCalcolo> variabileOpt = datiSintesiPasso.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(tipoVariabile)).findFirst();
			if (variabileOpt.isPresent()) {
				return variabileOpt.get();
			} else {
				logger.debug("getVariabileParticellaColtura: la variabile " + tipoVariabile.getDescrizione() +
						" non è presente nei dati di sintesi del passo " + tipologiaPasso
						+ " per l'istruttoria " + istruttoria.getId());
			}
		}
		return null;
	}

	public void setDatiFromEntity(TipologiaPassoTransizione tipologiaPasso, PassoTransizioneModel entity) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		if (entity.getDatiInput() != null && !entity.getDatiInput().isEmpty()) {
			DatiInput datiInputDto = mapper.readValue(entity.getDatiInput(), DatiInput.class);
			if (datiInputDto != null)
				this.datiInput.put(tipologiaPasso, datiInputDto);
		}
		if (entity.getDatiOutput() != null && !entity.getDatiOutput().isEmpty()) {
			DatiOutput datiOutputDto = mapper.readValue(entity.getDatiOutput(), DatiOutput.class);
			if (datiOutputDto != null)
				this.datiOutput.put(tipologiaPasso, datiOutputDto);
		}
		if (entity.getDatiSintesiLavorazione() != null && !entity.getDatiSintesiLavorazione().isEmpty()) {
			DatiSintesi datiSintesiDto = mapper.readValue(entity.getDatiSintesiLavorazione(), DatiSintesi.class);
			if (datiSintesiDto != null) {
				if (datiSintesiDto.getVariabiliParticellaColtura() != null && !datiSintesiDto.getVariabiliParticellaColtura().isEmpty()) {
					List<VariabileCalcolo> listaParticellaColtura = datiSintesiDto.getVariabiliParticellaColtura();
					listaParticellaColtura.forEach(v -> elaboraVariabileParticellaColtura(v));
				}
				this.datiSintesi.put(tipologiaPasso, datiSintesiDto);
			}
		}
	}

	public void elaboraVariabileParticellaColtura(VariabileCalcolo v) {
		TipoVariabile tipo = v.getTipoVariabile();
		if (v.getValList() != null && !v.getValList().isEmpty()) {
			v.getValList().forEach(pc -> {
				Long idParticella = pc.getParticella().getIdParticella();
				String codColtura = pc.getColtura();
				String key = idParticella.toString().concat("-").concat(codColtura);
				DatiParticellaColtura datiParticellaColtura;
				if (this.infoParticella.containsKey(key)) {
					datiParticellaColtura = this.infoParticella.get(key);
				} else {
					datiParticellaColtura = new DatiParticellaColtura();
				}
				datiParticellaColtura.setInfoParticella(pc.getParticella());
				datiParticellaColtura.setCodColtura(codColtura);
				switch (tipo) {
				case PFSUPIMP:
					datiParticellaColtura.getVariabiliCalcoloParticella().setSuperficieImpegnata(pc.getValNum());
					break;
				case PFSUPELE:
					datiParticellaColtura.getVariabiliCalcoloParticella().setSuperficieEleggibile(pc.getValNum());
					break;
				case PFSUPSIGECO:
					datiParticellaColtura.getVariabiliCalcoloParticella().setSuperficieSigeco(pc.getValNum());
					break;
				case PFANOMMAN:
					datiParticellaColtura.getVariabiliCalcoloParticella().setAnomalieMantenimento(pc.getValBool());
					break;
				case PFANOMCOORD:
					datiParticellaColtura.getVariabiliCalcoloParticella().setAnomalieCoordinamento(pc.getValBool());
					break;
//				TODO[begin]
				case PFSUPANCOORD:
					datiParticellaColtura.getVariabiliCalcoloParticella().setSuperficieAnomalieCoordinamento(pc.getValNum());
					break;
//				TODO[end]					
				case PFSUPDET:
					datiParticellaColtura.getVariabiliCalcoloParticella().setSuperficieDeterminata(pc.getValNum());
					break;
				case PFSUPDETARB:
					if (pc.getValBool())
						datiParticellaColtura.getVariabiliCalcoloParticella().setTipoColtura(tipo.getDescrizione());
					break;
				case PFSUPDETPP:
					if (pc.getValBool())
						datiParticellaColtura.getVariabiliCalcoloParticella().setTipoColtura(tipo.getDescrizione());
					break;
				case PFSUPDETSEM:
					if (pc.getValBool())
						datiParticellaColtura.getVariabiliCalcoloParticella().setTipoColtura(tipo.getDescrizione());
					break;
				case PFSUPDETERBAI:
					if (pc.getValBool())
						datiParticellaColtura.getVariabiliCalcoloParticella().setTipoSeminativo(tipo.getDescrizione());
					break;
				case PFSUPDETLEGUM:
					if (pc.getValBool())
						datiParticellaColtura.getVariabiliCalcoloParticella().setTipoSeminativo(tipo.getDescrizione());
					break;
				case PFSUPDETRIPOSO:
					if (pc.getValBool())
						datiParticellaColtura.getVariabiliCalcoloParticella().setTipoSeminativo(tipo.getDescrizione());
					break;
				case PFSUPDETSOMM:
					if (pc.getValBool())
						datiParticellaColtura.getVariabiliCalcoloParticella().setTipoSeminativo(tipo.getDescrizione());
					break;
				case PFSUPDETPRIMA:
					datiParticellaColtura.getVariabiliCalcoloParticella().setColturaPrincipale(pc.getValBool());
					break;
				case PFPASCOLO:
					datiParticellaColtura.getVariabiliCalcoloParticella().setPascolo(pc.getValString());
					break;
				case PFSUPDETSECONDA:
					datiParticellaColtura.getVariabiliCalcoloParticella().setSecondaColtura(pc.getValBool());
					break;
				case PFAZOTO:
					datiParticellaColtura.getVariabiliCalcoloParticella().setAzotoFissatrice(pc.getValBool());
					break;
				default:
					logger.error("Varibile non prevista per la particella {}", idParticella);
					break;
				}

				if (this.infoParticella.containsKey(key)) {
					this.infoParticella.replace(key, datiParticellaColtura);
				} else {
					this.infoParticella.put(key, datiParticellaColtura);
				}
			});
		}
	}

}

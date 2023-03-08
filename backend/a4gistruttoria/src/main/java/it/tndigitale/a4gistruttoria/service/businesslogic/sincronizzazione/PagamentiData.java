package it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.strategy.PagamentiDataItem;
import it.tndigitale.a4gistruttoria.strategy.SincronizzazioneInputData;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

import java.util.*;

public class PagamentiData extends SincronizzazioneInputData {
	
    public PagamentiData() {
		super();
	}

	private IstruttoriaModel istruttoria;

    private Map<String, PagamentiDataItem> dataItems = new HashMap<>();
    private List<PassoTransizioneModel> passiLavorazioneTransizione = new ArrayList<>();
    private Map<TipoVariabile, VariabileCalcolo> variabiliCalcolo = new EnumMap<>(TipoVariabile.class);

    public IstruttoriaModel getIstruttoria() {
        return istruttoria;
    }

    public void setIstruttoria(IstruttoriaModel istruttoria) {
        this.istruttoria = istruttoria;
    }

    public Optional<PagamentiDataItem> getDataItem(String intervento) {
        return Optional.ofNullable(dataItems.get(intervento));
    }

    public Map<String, PagamentiDataItem> getDataItems() {
        return this.dataItems;
    }

    void addDataItem(String intervento, PagamentiDataItem dataItem) {
        dataItems.put(intervento, dataItem);
    }

    public List<PassoTransizioneModel> getPassiLavorazioneTransizione() {
        return passiLavorazioneTransizione;
    }

    public void setPassiLavorazioneTransizione(List<PassoTransizioneModel> passiLavorazioneTransizione) {
        this.passiLavorazioneTransizione = passiLavorazioneTransizione;
    }

    public Boolean isStatoPagamentoAutorizzato() {
        return StatoIstruttoria.PAGAMENTO_AUTORIZZATO.equals(
                StatoIstruttoria.valueOfByIdentificativo(getIstruttoria().getA4gdStatoLavSostegno().getIdentificativo()));
    }

    public Map<TipoVariabile, VariabileCalcolo> getVariabiliCalcolo() {
        return variabiliCalcolo;
    }

    public void setVariabiliCalcolo(Map<TipoVariabile, VariabileCalcolo> variabiliCalcolo) {
        this.variabiliCalcolo = variabiliCalcolo;
    }
}

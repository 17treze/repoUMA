package it.tndigitale.a4gistruttoria.service.businesslogic.verbale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.component.StampaComponent;
import it.tndigitale.a4gistruttoria.dto.DatiLiquidazioneStampa;
import it.tndigitale.a4gistruttoria.dto.DomandaLiquidataStampa;
import it.tndigitale.a4gistruttoria.repository.dao.ElencoLiquidazioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;

public abstract class VerbaleLiquidazioneService<T extends DomandaLiquidataStampa> {
	
	private static final String TEMPLATE_NAME = "template/verbaleLiquidazioneDomandaUnica.docx";
	
	public static final String PREFISSO_QUALIFIER = "VerbaleLiquidazioneService_";

	@Autowired
	private ElencoLiquidazioneDao daoElencoLiquidazione;
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private StampaComponent stampaComponent;
	
	public byte[] stampa(Long idElencoLiquidazione) throws Exception {
		DatiLiquidazioneStampa dati = generaDati(idElencoLiquidazione);
		String nomeTemplate = getTemplateVerbaleName();
		return stampaComponent.stampaPDF_A(mapper.writeValueAsString(dati), nomeTemplate);
	}
	
	protected DatiLiquidazioneStampa generaDati(Long idElencoLiquidazione) throws Exception {
		DatiLiquidazioneStampa datiLiquidazioneStampa = new DatiLiquidazioneStampa();
		ElencoLiquidazioneModel elencoLiquidazione = daoElencoLiquidazione.findById(idElencoLiquidazione).get();
		datiLiquidazioneStampa.setTotaleElencoLiquidazione(BigDecimal.ZERO);
		List<IstruttoriaModel> istruttorie = istruttoriaDao.findByElencoLiquidazione(elencoLiquidazione);
		List<DomandaLiquidataStampa> domandeLiquidabili = new ArrayList<>();
		int index = 0;
		for (IstruttoriaModel istruttoria : istruttorie) {
			index++;
			T datiIstruttoriaLiquidata = inizializzaDomandaLiquidataStampa();
			DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
			datiIstruttoriaLiquidata.setCuaa(domanda.getCuaaIntestatario());
			datiIstruttoriaLiquidata.setDataPresentazione(domanda.getDtPresentazione());
			datiIstruttoriaLiquidata.setNumeroDomanda(domanda.getNumeroDomanda().toString());
			datiIstruttoriaLiquidata.setNome(domanda.getRagioneSociale());
			datiLiquidazioneStampa.setCampagna(domanda.getCampagna().toString());
			datiIstruttoriaLiquidata.setCounter(index);
			domandeLiquidabili.add(datiIstruttoriaLiquidata);
			popolaDatiIstruttoria(istruttoria, datiIstruttoriaLiquidata);
			datiLiquidazioneStampa.setTotaleElencoLiquidazione(
					datiLiquidazioneStampa.getTotaleElencoLiquidazione()
					.add(datiIstruttoriaLiquidata.getTotalePremio())
					);
			datiLiquidazioneStampa.setIntervento(getDescrizioneIntervento(istruttoria.getSostegno()));
		}
		datiLiquidazioneStampa.setElencoDomande(domandeLiquidabili);
		datiLiquidazioneStampa.setCodiceElenco(elencoLiquidazione.getCodElenco());
		datiLiquidazioneStampa.setDataElenco(elencoLiquidazione.getDtCreazione().toLocalDate());
		datiLiquidazioneStampa.setTipoPagamento(getTipoPagamento(istruttorie));

		return datiLiquidazioneStampa;
	}
	
	protected String getTemplateVerbaleName() {
		return TEMPLATE_NAME;
	}
	
	protected String getDescrizioneIntervento(Sostegno sostegno) {
		switch (sostegno) {
			case DISACCOPPIATO:
				return "BASE, GREENING, GIOVANE AGRICOLTORE";
			case SUPERFICIE:
				return "ACCOPPIATO SUPERFICIE";
			case ZOOTECNIA:
				return "ACCOPPIATO ZOOTECNIA";
			default:
				return "";
		}
	}
	
	protected abstract String getTipoPagamento(List<IstruttoriaModel> istruttorie);
	protected abstract void popolaDatiIstruttoria(IstruttoriaModel istruttoria, T datiIstruttoriaLiquidata);
	protected abstract T inizializzaDomandaLiquidataStampa();
}

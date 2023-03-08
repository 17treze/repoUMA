package it.tndigitale.a4g.soc.business.service;


import static it.tndigitale.a4g.soc.business.service.LiquidazioneHandler.raggruppa;
import static it.tndigitale.a4g.soc.business.service.LiquidazioneHandler.sommaIncassi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.soc.business.dto.Debito;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidatoFilter;
import it.tndigitale.a4g.soc.business.persistence.repository.LiquidazioneCustomDao;


@Service
public class LiquidazioneService {

	@Autowired
	private LiquidazioneCustomDao liquidazioneCustomDao;

	public LiquidazioneService setLiquidazioneCustomDao(LiquidazioneCustomDao liquidazioneCustomDao) {
		this.liquidazioneCustomDao = liquidazioneCustomDao;
		return this;
	}

	public List<ImportoLiquidato> caricaImportoLiquidazione(final ImportoLiquidatoFilter importLiquidatoFilter) {
		importLiquidatoFilter.isValid();
		List<ImportoLiquidato> importiLiquidatoConCapitoli = liquidazioneCustomDao.calcolaImportoLiquidato(importLiquidatoFilter);
		List<ImportoLiquidato> importiLiquidato = new ArrayList<>();
		raggruppa(importiLiquidatoConCapitoli).forEach(sommaIncassi(importiLiquidato));
		importiLiquidato.forEach(this::recuperaDebiti);
		return importiLiquidato;
	}
	
    private Void recuperaDebiti(ImportoLiquidato il){
		List<Debito> debiti = liquidazioneCustomDao.calcolaDebitiImportoLiquidato(il);
		il.setDebiti(debiti);
		return null;
    }

}

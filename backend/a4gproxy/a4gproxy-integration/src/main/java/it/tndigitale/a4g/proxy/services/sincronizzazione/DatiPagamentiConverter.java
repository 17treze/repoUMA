package it.tndigitale.a4g.proxy.services.sincronizzazione;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import it.tndigitale.a4g.proxy.dto.DatiPagamentiDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Pagamenti;

@Component
class DatiPagamentiConverter implements Converter<DatiPagamentiDto, Pagamenti> {

	@Override
	public Pagamenti convert(DatiPagamentiDto source) {

		Pagamenti target = new Pagamenti();
		target.setNumeProgDecr(source.getNumeroProgressivoLavorazione());
		target.setNumeCampRife(source.getAnnoCampagna());
		target.setIdEnte(CostantiSincronizzazione.CODICE_APPAG); // Codice APPAG
		target.setDescEnte(null); // Non considerare
		target.setBcAttoAmmi(null); // Non considerare
		target.setIdAttoAmmi(null); // Non considerare
		target.setCodiFisc(source.getCuaa());
		target.setCodiAttoOpr(source.getNumeroDomanda());
		target.setIdInte(null); // Non considerare
		target.setCodiInte(source.getCodiceIntervento());
		target.setImpoDete(BigDecimal.valueOf(source.getImportoDeterminato()));
		target.setImpoLiqui(BigDecimal.valueOf(source.getImportoLiquidato()));
		target.setImpoRichiesto(BigDecimal.valueOf(source.getImportoRichiesto()));
		target.setDataAggi(null); // Non considerare
		target.setDecoStatPaga(Long.valueOf(source.getPagamentoAutorizzato() ? 1 : 0));
		target.setDataInizVali(new Date()); // Default SYSDATE
		target.setDataFineVali(Date.from(LocalDate.of(9999, 12, 31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())); // Default 31/12/9999
		target.setDecoStat(null); // Default null, impostato a 93 automaticamente in fase di cancellazione logica
		target.setFonte(CostantiSincronizzazione.FONTE_SINCRONIZZAZIONE);
		return target;
	}
}

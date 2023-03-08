package it.tndigitale.a4gistruttoria.service.businesslogic.verbale;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.DomandaLiquidataStampaDisaccoppiato;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.VariabileCalcoloUtils;
import javassist.NotFoundException;

@Service(VerbaleLiquidazioneService.PREFISSO_QUALIFIER + "DISACCOPPIATO")
public class VerbaleLiquidazioneDisaccoppiatoService extends VerbaleLiquidazioneService<DomandaLiquidataStampaDisaccoppiato> {
	
	@Autowired
	private VariabileCalcoloUtils utilsVariabileCalcolo;
	
	@Override
	protected String getTipoPagamento(List<IstruttoriaModel> istruttorie) {
		return istruttorie.get(0).getTipologia().name();
	}
	
	@Override
	protected void popolaDatiIstruttoria(IstruttoriaModel istruttoria,
			DomandaLiquidataStampaDisaccoppiato datiIstruttoriaLiquidata) {
		Optional<TransizioneIstruttoriaModel> transizioneControlliIntersostegnoOpt = getTransizioneControlliIntersostegno(istruttoria);
		
		TransizioneIstruttoriaModel transizione = transizioneControlliIntersostegnoOpt.orElseThrow(
				() -> new EntityNotFoundException(String.format(
						"Transizione del controllo intersostegno non trovata per l'istruttoria ID %d ",
						istruttoria.getId())));
		try {
			
			BigDecimal impBpsNoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGDISBPS);
			BigDecimal impBpsDf = valueOrZero(transizione, TipoVariabile.DFIMPDFDISBPS);
			BigDecimal totaleBps = impBpsNoDf.add(impBpsDf);
			datiIstruttoriaLiquidata.setQuotaPremioBps(totaleBps);
			
			BigDecimal impGreeningNoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGDISGRE);
			BigDecimal impGreeningDf = valueOrZero(transizione, TipoVariabile.DFIMPDFDISGRE);
			BigDecimal totaleGreening = impGreeningNoDf.add(impGreeningDf);
			datiIstruttoriaLiquidata.setQuotaPremioGreening(totaleGreening);
			
			BigDecimal impGiovaneNoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGDISGIO);
			BigDecimal impGiovaneDf = valueOrZero(transizione, TipoVariabile.DFIMPDFDISGIO);
			BigDecimal totaleGiovane = impGiovaneNoDf.add(impGiovaneDf);
			datiIstruttoriaLiquidata.setQuotaPremioGiovane(totaleGiovane);
	
			BigDecimal totaleDomanda = totaleBps.add(totaleGreening).add(totaleGiovane);
			datiIstruttoriaLiquidata.setTotalePremio(totaleDomanda);
		} catch (NotFoundException | IOException e) {
			throw new RuntimeException(e);
		}		
	}

	@Override
	protected DomandaLiquidataStampaDisaccoppiato inizializzaDomandaLiquidataStampa() {
		return new DomandaLiquidataStampaDisaccoppiato();
	}
	
	protected Optional<TransizioneIstruttoriaModel> getTransizioneControlliIntersostegno(final IstruttoriaModel istruttoria) {
		return istruttoria.getTransizioni().stream()
				.filter(t -> StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria().equals(t.getA4gdStatoLavSostegno1().getIdentificativo()))
				.max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione));
	}
	
	protected BigDecimal valueOrZero(
			final TransizioneIstruttoriaModel transizione,
			final TipoVariabile tipoVariabile) throws NotFoundException, IOException {
		BigDecimal retVariable = BigDecimal.ZERO;
		VariabileCalcolo variable = utilsVariabileCalcolo.recuperaVariabileCalcolata(
				transizione, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, tipoVariabile);
		if (variable != null) {
			retVariable = variable.getValNumber();
		}
		return retVariable;
	}
}

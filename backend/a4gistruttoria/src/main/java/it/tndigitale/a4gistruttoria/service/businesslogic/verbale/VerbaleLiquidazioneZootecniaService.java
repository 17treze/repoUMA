package it.tndigitale.a4gistruttoria.service.businesslogic.verbale;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.DomandaLiquidataStampaAcz;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.VariabileCalcoloUtils;
import javassist.NotFoundException;

@Service(VerbaleLiquidazioneService.PREFISSO_QUALIFIER + "ZOOTECNIA")
public class VerbaleLiquidazioneZootecniaService extends VerbaleLiquidazioneService<DomandaLiquidataStampaAcz> {
	
	@Autowired
	private VariabileCalcoloUtils utilsVariabileCalcolo;
	
	protected Optional<TransizioneIstruttoriaModel> getTransizioneControlliIntersostegno(final IstruttoriaModel istruttoria) {
		return istruttoria.getTransizioni().stream()
				.filter(t -> StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria().equals(t.getA4gdStatoLavSostegno1().getIdentificativo()))
				// l'ultima
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
	
	@Override
	protected void popolaDatiIstruttoria(
			final IstruttoriaModel istruttoria,
			final DomandaLiquidataStampaAcz datiIstruttoriaLiquidata) {
		Optional<TransizioneIstruttoriaModel> transizioneControlliIntersostegnoOpt = getTransizioneControlliIntersostegno(istruttoria);
		TransizioneIstruttoriaModel transizione = transizioneControlliIntersostegnoOpt.orElseThrow(() -> new EntityNotFoundException(
				String.format(
						"Transizione del controllo intersostegno non trovata per l'istruttoria ID %d ",
						istruttoria.getId())));
		try {
			BigDecimal impAcz310NoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGACZ_310);
			BigDecimal impAcz310Df = valueOrZero(transizione, TipoVariabile.DFIMPDFDISACZ_310);
			BigDecimal totaleAcz310 = impAcz310NoDf.add(impAcz310Df);
			if (totaleAcz310.compareTo(BigDecimal.ZERO) != 0) {
				datiIstruttoriaLiquidata.setQuotaPremioAcz310(totaleAcz310);
			}
			
			BigDecimal impAcz311NoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGACZ_311);
			BigDecimal impAcz311Df = valueOrZero(transizione, TipoVariabile.DFIMPDFDISACZ_311);
			BigDecimal totaleAcz311 = impAcz311NoDf.add(impAcz311Df);
			if (totaleAcz311.compareTo(BigDecimal.ZERO) != 0) {
				datiIstruttoriaLiquidata.setQuotaPremioAcz311(totaleAcz311);
			}
			
			BigDecimal impAcz313NoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGACZ_313);
			BigDecimal impAcz313Df = valueOrZero(transizione, TipoVariabile.DFIMPDFDISACZ_313);
			BigDecimal totaleAcz313 = impAcz313NoDf.add(impAcz313Df);
			if (totaleAcz313.compareTo(BigDecimal.ZERO) != 0) {
				datiIstruttoriaLiquidata.setQuotaPremioAcz313(totaleAcz313);
			}
			
			BigDecimal impAcz322NoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGACZ_322);
			BigDecimal impAcz322Df = valueOrZero(transizione, TipoVariabile.DFIMPDFDISACZ_322);
			BigDecimal totaleAcz322 = impAcz322NoDf.add(impAcz322Df);
			if (totaleAcz322.compareTo(BigDecimal.ZERO) != 0) {
				datiIstruttoriaLiquidata.setQuotaPremioAcz322(totaleAcz322);
			}
			
			BigDecimal impAcz315NoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGACZ_315);
			BigDecimal impAcz315Df = valueOrZero(transizione, TipoVariabile.DFIMPDFDISACZ_315);
			BigDecimal totaleAcz315 = impAcz315NoDf.add(impAcz315Df);
			if (totaleAcz315.compareTo(BigDecimal.ZERO) != 0) {
				datiIstruttoriaLiquidata.setQuotaPremioAcz315(totaleAcz315);
			}
			
			BigDecimal impAcz316NoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGACZ_316);
			BigDecimal impAcz316Df = valueOrZero(transizione, TipoVariabile.DFIMPDFDISACZ_316);
			BigDecimal totaleAcz316 = impAcz316NoDf.add(impAcz316Df);
			if (totaleAcz316.compareTo(BigDecimal.ZERO) != 0) {
				datiIstruttoriaLiquidata.setQuotaPremioAcz316(totaleAcz316);
			}
	
			BigDecimal impAcz318NoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGACZ_318);
			BigDecimal impAcz318Df = valueOrZero(transizione, TipoVariabile.DFIMPDFDISACZ_318);
			BigDecimal totaleAcz318 = impAcz318NoDf.add(impAcz318Df);
			if (totaleAcz318.compareTo(BigDecimal.ZERO) != 0) {
				datiIstruttoriaLiquidata.setQuotaPremioAcz318(totaleAcz318);
			}
	
			BigDecimal impAcz320NoDf = valueOrZero(transizione, TipoVariabile.DFFRPAGACZ_320);
			BigDecimal impAcz320Df = valueOrZero(transizione, TipoVariabile.DFIMPDFDISACZ_320);
			BigDecimal totaleAcz320 = impAcz320NoDf.add(impAcz320Df);
			if (totaleAcz320.compareTo(BigDecimal.ZERO) != 0) {
				datiIstruttoriaLiquidata.setQuotaPremioAcz320(totaleAcz320);
			}
	
			BigDecimal impAcz321NoDf = BigDecimal.ZERO;
			VariabileCalcolo vImpAcz321NoDf = utilsVariabileCalcolo.recuperaVariabileCalcolata(transizione, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, TipoVariabile.DFFRPAGACZ_321);
			if (vImpAcz321NoDf != null) {
				impAcz321NoDf = vImpAcz321NoDf.getValNumber();
			}
	
			BigDecimal impAcz321Df = BigDecimal.ZERO;
			VariabileCalcolo vImpAcz321Df = utilsVariabileCalcolo.recuperaVariabileCalcolata(transizione, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, TipoVariabile.DFIMPDFDISACZ_321);
			if (vImpAcz321Df != null) {
				impAcz321Df = vImpAcz321Df.getValNumber();
			}
	
			BigDecimal totaleAcz321 = impAcz321NoDf.add(impAcz321Df);
			if (totaleAcz321.compareTo(BigDecimal.ZERO) != 0) {
				datiIstruttoriaLiquidata.setQuotaPremioAcz321(totaleAcz321);
			}
	
			BigDecimal totaleDomanda = totaleAcz310
					.add(totaleAcz311).add(totaleAcz313).add(totaleAcz322)
					.add(totaleAcz315).add(totaleAcz316).add(totaleAcz318)
					.add(totaleAcz320).add(totaleAcz321);
			datiIstruttoriaLiquidata.setTotalePremio(totaleDomanda);
		} catch (NotFoundException | IOException e) {
			throw new RuntimeException(e);
		}		
	}

	@Override
	protected String getTipoPagamento(List<IstruttoriaModel> istruttorie) {
		return "INTEGRAZIONE";
	}

	@Override
	protected DomandaLiquidataStampaAcz inizializzaDomandaLiquidataStampa() {
		return new DomandaLiquidataStampaAcz();
	}
}

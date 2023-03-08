package it.tndigitale.a4gistruttoria.service.businesslogic.verbale;

import it.tndigitale.a4gistruttoria.dto.DomandaLiquidataStampaAcs;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DomandeServiceImpl;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.VariabileCalcoloUtils;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service(VerbaleLiquidazioneService.PREFISSO_QUALIFIER + "SUPERFICIE")
public class VerbaleLiquidazioneSuperficieService extends VerbaleLiquidazioneService<DomandaLiquidataStampaAcs> {

    private static final Logger logger = LoggerFactory.getLogger(DomandeServiceImpl.class);

    @Autowired
    private TransizioneIstruttoriaDao transizioneIstruttoriaDao;
    @Autowired
    private VariabileCalcoloUtils utilsVariabileCalcolo;

    @Override
    protected void popolaDatiIstruttoria(IstruttoriaModel istruttoria,
                                         DomandaLiquidataStampaAcs datiIstruttoriaLiquidata) {

        Optional<TransizioneIstruttoriaModel> a4gtTransizioneInterSostegno = transizioneIstruttoriaDao.findTransizioneControlloIntersostegno(istruttoria).stream().max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione));

        if (a4gtTransizioneInterSostegno.isPresent()) {
            TransizioneIstruttoriaModel transizione = a4gtTransizioneInterSostegno.get();
            try {
                BigDecimal totaleAcsM8 = getTotaleSuperficie(transizione, TipoVariabile.DFIMPDFDISACS_M8, TipoVariabile.DFFRPAGACS_M8);
                if (totaleAcsM8.compareTo(BigDecimal.ZERO) != 0) {
                    datiIstruttoriaLiquidata.setQuotaPremioAcsM8(totaleAcsM8);
                }

                BigDecimal totaleAcsM9 = getTotaleSuperficie(transizione, TipoVariabile.DFIMPDFDISACS_M9, TipoVariabile.DFFRPAGACS_M9);
                if (totaleAcsM9.compareTo(BigDecimal.ZERO) != 0) {
                    datiIstruttoriaLiquidata.setQuotaPremioAcsM9(totaleAcsM9);
                }

                BigDecimal totaleAcsM10 = getTotaleSuperficie(transizione, TipoVariabile.DFIMPDFDISACS_M10, TipoVariabile.DFFRPAGACS_M10);
                if (totaleAcsM10.compareTo(BigDecimal.ZERO) != 0) {
                    datiIstruttoriaLiquidata.setQuotaPremioAcsM10(totaleAcsM10);
                }

                BigDecimal totaleAcsM11 = getTotaleSuperficie(transizione, TipoVariabile.DFIMPDFDISACS_M11, TipoVariabile.DFFRPAGACS_M11);
                if (totaleAcsM11.compareTo(BigDecimal.ZERO) != 0) {
                    datiIstruttoriaLiquidata.setQuotaPremioAcsM11(totaleAcsM11);
                }

                BigDecimal totaleAcsM14 = getTotaleSuperficie(transizione, TipoVariabile.DFIMPDFDISACS_M14, TipoVariabile.DFFRPAGACS_M14);
                if (totaleAcsM14.compareTo(BigDecimal.ZERO) != 0) {
                    datiIstruttoriaLiquidata.setQuotaPremioAcsM14(totaleAcsM14);
                }

                BigDecimal totaleAcsM15 = getTotaleSuperficie(transizione, TipoVariabile.DFIMPDFDISACS_M15, TipoVariabile.DFFRPAGACS_M15);
                if (totaleAcsM15.compareTo(BigDecimal.ZERO) != 0) {
                    datiIstruttoriaLiquidata.setQuotaPremioAcsM15(totaleAcsM15);
                }

                BigDecimal totaleAcsM16 = getTotaleSuperficie(transizione, TipoVariabile.DFIMPDFDISACS_M16, TipoVariabile.DFFRPAGACS_M16);
                if (totaleAcsM16.compareTo(BigDecimal.ZERO) != 0) {
                    datiIstruttoriaLiquidata.setQuotaPremioAcsM16(totaleAcsM16);
                }

                BigDecimal totaleAcsM17 = getTotaleSuperficie(transizione, TipoVariabile.DFIMPDFDISACS_M17, TipoVariabile.DFFRPAGACS_M17);
                if (totaleAcsM17.compareTo(BigDecimal.ZERO) != 0) {
                    datiIstruttoriaLiquidata.setQuotaPremioAcsM17(totaleAcsM17);
                }

                BigDecimal totaleDomanda = totaleAcsM8.add(totaleAcsM9).add(totaleAcsM10).add(totaleAcsM11).add(totaleAcsM14).add(totaleAcsM15).add(totaleAcsM16).add(totaleAcsM17);
                datiIstruttoriaLiquidata.setTotalePremio(totaleDomanda);

            } catch (NotFoundException | IOException e) {
                logger.error("Impossibile recuperare la variabile {} dal passo di lavorazione {} per l'istruttoria {} .", TipoVariabile.DFFRPAGACS, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA,
                        istruttoria.getId());
            }
        }
    }

    @Override
    protected String getTipoPagamento(List<IstruttoriaModel> istruttorie) {
        return "INTEGRAZIONE";
    }

    @Override
    protected DomandaLiquidataStampaAcs inizializzaDomandaLiquidataStampa() {
        return new DomandaLiquidataStampaAcs();
    }

    private BigDecimal getImportoSuperficie(TransizioneIstruttoriaModel transizione, TipoVariabile variabile) throws NotFoundException, IOException {
        VariabileCalcolo varImportoAcsDiscFinanziaria = utilsVariabileCalcolo.recuperaVariabileCalcolata(transizione, TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA, variabile);
        if (varImportoAcsDiscFinanziaria != null) {
            return varImportoAcsDiscFinanziaria.getValNumber();
        }
        return null;
    }

    private BigDecimal getTotaleSuperficie(TransizioneIstruttoriaModel transizione, TipoVariabile varDiscFinanziaria, TipoVariabile varNoDiscFinanziaria) throws NotFoundException, IOException {
        BigDecimal importoSuperficieNoDiscFinanziaria = getImportoSuperficie(transizione, varNoDiscFinanziaria);
        BigDecimal importoSuperficieDiscFinanziaria = getImportoSuperficie(transizione, varDiscFinanziaria);
        return importoSuperficieNoDiscFinanziaria.add(importoSuperficieDiscFinanziaria);
    }

}

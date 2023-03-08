package it.tndigitale.a4gistruttoria.service.support;

import static it.tndigitale.a4gistruttoria.service.support.ConsultazioneIstruttoriaSupport.creaDtoIstruttoria;
import static it.tndigitale.a4gistruttoria.service.support.ConsultazioneIstruttoriaSupport.creaDtoIstruttoriaPerDisciplina;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.soc.client.model.ImportoLiquidato;
import it.tndigitale.a4g.soc.client.model.ImportoLiquidato.CausaleEnum;
import it.tndigitale.a4gistruttoria.dto.domandaunica.Istruttoria;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SintesiPagamento;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SintesiPagamentoBuilder;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Soc;
import it.tndigitale.a4gistruttoria.util.NumberUtil;

@Component
public class DomandaUnicaSupport {

    private static Logger logger = LoggerFactory.getLogger(DomandaUnicaSupport.class);

    @Autowired
    private ConsumeExternalRestApi4Soc consumeExternalRestApi;
    @Autowired
    private IstruttoriaDao istruttoriaDao;
    @Autowired
    private SintesiPagamentoBuilder sintesiPagamentoBuilder;
    

    private void aroundSintesiPagamento(SintesiPagamento sintesiPagamento) {
        sintesiPagamento.setImportoCalcolato(NumberUtil.round(sintesiPagamento.getImportoCalcolato()));
        sintesiPagamento.setImportoLiquidato(NumberUtil.round(sintesiPagamento.getImportoLiquidato()));
    }

    private List<ImportoLiquidato>  recuperaImportiLiquidazioneInImportiIstruttoria(DomandaUnicaModel domanda) {
        logger.info("Chiamata al modulo SOC di A4G - get debiti recuperati. numero domanda ".concat(domanda.getNumeroDomanda().toPlainString()));
        return consumeExternalRestApi.retrieveImpLiquidazioneByApi(domanda);
    }

    public SintesiPagamento calcolaSintesiPagamentoFrom(DomandaUnicaModel domanda) {
        SintesiPagamento sintesiPagamento = sintesiPagamentoBuilder.from(domanda);
        logger.info("Chiamata al modulo SOC di A4G - per recupero liquidato netto: {} ", domanda.getNumeroDomanda());
        List<ImportoLiquidato> importiLiquidazione =
                consumeExternalRestApi.retrieveImpLiquidazioneByApi(domanda);
        Double sommaLiquidato = 0.0;
        if((importiLiquidazione != null) && (!importiLiquidazione.isEmpty())){
            sommaLiquidato = importiLiquidazione.stream()
                    .mapToDouble(importoLiquidazione -> importoLiquidazione.getIncassatoNetto().doubleValue())
                    .sum();
        }
        sintesiPagamento.setImportoLiquidato(sommaLiquidato);
        aroundSintesiPagamento(sintesiPagamento);
        return sintesiPagamento;
    }

    public List<Istruttoria> getIstruttorieBy(DomandaUnicaModel domanda) {
   		List<IstruttoriaModel> istruttorie = istruttoriaDao.findByDomandaUnicaModelId(domanda.getId());
        if (istruttorie == null) return null;

        List<Istruttoria> results = new ArrayList<>();
        List<ImportoLiquidato> impLiquidati=recuperaImportiLiquidazioneInImportiIstruttoria(domanda);
		istruttorie.forEach(creaDtoIstruttoria(impLiquidati,results));
        //gestione disciplina finanziaria
        if((impLiquidati != null) && (!impLiquidati.isEmpty())){
            impLiquidati.stream()
                    .filter(imp -> CausaleEnum.DISCIPLINA_FINANZIARIA.equals(imp.getCausale()))
                    .findAny()
                    .ifPresent(creaDtoIstruttoriaPerDisciplina(results));
        }
        return results;
    }
}

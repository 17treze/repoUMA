package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.SostegnoModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.avvio.AvvioIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.specification.DomandaUnicaSpecificationBuilder;

@Service
public class IstruisciDomandaService implements ElaborazioneDomanda {

    private static final Logger logger = LoggerFactory.getLogger(IstruisciDomandaService.class);

    @Autowired
    private DomandaUnicaDao domandaUnicaDao;

    @Autowired
    private Clock clock;

    @Autowired
    private AvvioIstruttoriaService avvioIstruttoriaService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElaborazioneDomandaException.class)
    public void elabora(Long idDomanda) throws ElaborazioneDomandaException {
        DomandaUnicaModel domandaUnicaModel = domandaUnicaDao.getOne(idDomanda);
        TipoIstruttoria tipoIstruttoria = this.getTipoIstruttoria(domandaUnicaModel.getCampagna());
        List<SostegnoModel> listaSostegni = domandaUnicaModel.getSostegni();

        boolean cambioStato = false;
        if ((listaSostegni != null) && (!listaSostegni.isEmpty())) {
            for (SostegnoModel sostegno : listaSostegni) {
                try {
                	if (avvioIstruttoriaService.isIstruttoriaAvviabile(idDomanda, tipoIstruttoria, sostegno.getSostegno())) {
                		logger.debug("Avvio istruttoria per domanda {}, tipologia {}, sostegno {}", idDomanda, tipoIstruttoria, sostegno);
                        Long idIstruttoria = avvioIstruttoriaService.avvioIstruttoria(idDomanda, tipoIstruttoria, sostegno.getSostegno());
                        cambioStato = cambioStato || (idIstruttoria != null);
                	} else {
                		logger.debug("Nuova istruttoria per domanda {}, tipologia {}, sostegno {} non avviabile", idDomanda, tipoIstruttoria, sostegno);
                	}
                } catch (ElaborazioneIstruttoriaException e) {
                	throw new ElaborazioneDomandaException("Errore elaborando la domanda " + idDomanda, e);
                }
            }
        }
        if (cambioStato) {
        	cambiaStatoDomanda(domandaUnicaModel, StatoDomanda.IN_ISTRUTTORIA);
        }
    }

    private void cambiaStatoDomanda(DomandaUnicaModel domandaUnicaModel, StatoDomanda statoDomanda) {
        domandaUnicaModel.setStato(statoDomanda);
        domandaUnicaDao.save(domandaUnicaModel);
    }

    @Override
    public List<Long> caricaIdDaElaborare(Integer campagna) throws ElaborazioneDomandaException {
        List<DomandaUnicaModel> listaDomande = domandaUnicaDao.findAll(
                DomandaUnicaSpecificationBuilder.statoNotPiccoliAgricoltoriAndNotTipoIstruttoriaAndNotNonRicevibile(campagna, getTipoIstruttoria(campagna)));
        return listaDomande.stream().map(DomandaUnicaModel::getId).collect(Collectors.toList());
    }

    public TipoIstruttoria getTipoIstruttoria(Integer campagna) {
        LocalDate inizioIstruttoriaSaldo = LocalDate.of(campagna, 12, 1);
        if (clock.today().isBefore(inizioIstruttoriaSaldo)) {
            return TipoIstruttoria.ANTICIPO;
        } else {
            return TipoIstruttoria.SALDO;
        }
    }

}

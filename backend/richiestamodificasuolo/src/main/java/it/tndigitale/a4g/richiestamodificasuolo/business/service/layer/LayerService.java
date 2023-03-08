package it.tndigitale.a4g.richiestamodificasuolo.business.service.layer;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LayerModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LayerDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.UtenteControllerClient;
import it.tndigitale.a4g.richiestamodificasuolo.dto.layer.LayerSwitcherDto;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.LayerMapper;
import java.util.stream.Collectors;

@Service
public class LayerService {

    private Logger log = LoggerFactory.getLogger(LayerService.class);

    @Autowired
    private LayerDao layerDao;
    @Autowired
    private LayerMapper layerMapper;
    @Autowired
    private UtenteControllerClient utenteControllerClient;

    public List<LayerSwitcherDto> getLayerProfiloUtente(ProfiloUtente profiloUtente) {
        log.debug("START - getLayerProfiloUtente per profiloUtente{0} ", profiloUtente);

        List<LayerModel> layerModel = layerDao.findByListLayerProfilo_ProfiloUtenteIgnoreCase(profiloUtente.toString());
        List<LayerSwitcherDto> listLayerSwitcherDto = layerMapper.from(layerModel);

        log.debug("END - getLayerProfiloUtente");
        
        return listLayerSwitcherDto;
    }
    public List<String> getLayersUtente(String utente) {
        log.debug("START - getLayersUtente per utente {0} ", utente);

        List<String> profili = utenteControllerClient.getProfili(utente);
        List<LayerModel> layers = layerDao.findByListLayerProfilo_ProfiloUtenteInIgnoreCase(profili);
        List<String> nomi = layers
            .stream()
            .map(layer -> layer.getWorkspace().substring(1) + ":" + layer.getNomeLayer())
            .distinct()
            .collect(Collectors.toList());
        
        log.debug("END - getLayersUtente");
        
        return nomi;
    }
}
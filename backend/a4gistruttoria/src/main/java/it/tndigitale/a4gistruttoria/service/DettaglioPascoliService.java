package it.tndigitale.a4gistruttoria.service;

import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioPascoli;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtPascoloParticella;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.builder.DettaglioPascoliBuilder;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static it.tndigitale.a4gistruttoria.util.SetUtil.emptyIfNull;

@Service
public class DettaglioPascoliService {

    @Autowired
    private MapperWrapper mapper;
    @Autowired
    private IstruttoriaComponent istruttoriaComponent;


    public List<DettaglioPascoli> getInfoDettaglioPascoliWithEsitoMantenimento(Long idIstruttoria) {
        IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);

        Set<A4gtPascoloParticella> pascoloParticellas = istruttoria.getDomandaUnicaModel().getA4gtPascoloParticellas();
        return emptyIfNull(pascoloParticellas)
                .stream()
                .map(pascolo -> getBuildDettaglioPascoliConEsitoMantenimento(istruttoria, pascolo))
                .collect(Collectors.toList());
    }

    public List<DettaglioPascoli> getInfoDettaglioPascoliWithDatiIstruttoria(Long idIstruttoria) {
        IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);
        Set<A4gtPascoloParticella> pascoloParticellas = istruttoria.getDomandaUnicaModel().getA4gtPascoloParticellas();
        return emptyIfNull(pascoloParticellas)
                .stream()
                .map(pascolo -> getBuildDettaglioPascoliConDatiIstruttoria(istruttoria, pascolo))
                .collect(Collectors.toList());
    }


    private DettaglioPascoli getBuildDettaglioPascoliConEsitoMantenimento(IstruttoriaModel istruttoria, A4gtPascoloParticella pascolo) {
        return new DettaglioPascoliBuilder(pascolo, istruttoria, mapper)
                .withEsitoMantenimento()
                .build();
    }

    private DettaglioPascoli getBuildDettaglioPascoliConDatiIstruttoria(IstruttoriaModel istruttoria, A4gtPascoloParticella pascolo) {
        return new DettaglioPascoliBuilder(pascolo, istruttoria, mapper)
                .withDatiIstruttoriaPascoli()
                .build();
    }
}

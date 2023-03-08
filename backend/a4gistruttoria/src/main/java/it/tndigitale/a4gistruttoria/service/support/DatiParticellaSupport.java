package it.tndigitale.a4gistruttoria.service.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.*;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabiliParticellaColtura;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DatiParticellaSupport {

    @Autowired
    private RichiestaSuperficieDao richiestaSuperficieDao;
    @Autowired
    private ObjectMapper mapper;

    public List<DettaglioParticellaDto> from(List<IDettaglioParticella> particelle, IstruttoriaModel istruttoria) {
        List<DettaglioParticellaDto> dettaglioParticelle = new ArrayList<>();
        particelle.forEach(particella -> {
            DettaglioParticellaDto dettaglioPart = new DettaglioParticellaDto();
            try {
                BeanUtils.copyProperties(particella, dettaglioPart);

                Particella infoCat = new Particella();
                BeanUtils.copyProperties(particella, infoCat);
                dettaglioPart.setInfoCatastali(infoCat);

                VariabiliParticellaColtura variabiliParticellaColtura = new VariabiliParticellaColtura();
                convert(particella, variabiliParticellaColtura);

                //BeanUtils.copyProperties(particella, variabiliParticellaColtura);
                dettaglioPart.setVariabiliCalcoloParticella(variabiliParticellaColtura);

                eleboraDatiParticella(istruttoria.getDomandaUnicaModel().getId(), istruttoria.getSostegno(), dettaglioPart);
                dettaglioParticelle.add(dettaglioPart);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return dettaglioParticelle;
    }

    private void convert(IDettaglioParticella particella, VariabiliParticellaColtura variabiliParticellaColtura) {
        variabiliParticellaColtura.setAnomalieMantenimento(particella.getAnomalieMantenimento());
        variabiliParticellaColtura.setAzotoFissatrice(particella.getAzotoFissatrice());
        variabiliParticellaColtura.setColturaPrincipale(particella.getColturaPrincipale());
        variabiliParticellaColtura.setSecondaColtura(particella.getSecondaColtura());
        variabiliParticellaColtura.setAnomalieCoordinamento(particella.getAnomalieCoordinamento());
        variabiliParticellaColtura.setPascolo(particella.getPascolo());
        variabiliParticellaColtura.setSuperficieDeterminata(particella.getSuperficieDeterminata());
        variabiliParticellaColtura.setSuperficieEleggibile(particella.getSuperficieEleggibile());
        variabiliParticellaColtura.setSuperficieImpegnata(particella.getSuperficieImpegnata());
        variabiliParticellaColtura.setSuperficieSigeco(particella.getSuperficieSigeco());
        variabiliParticellaColtura.setTipoColtura(particella.getTipoColtura());
        variabiliParticellaColtura.setTipoSeminativo(particella.getTipoSeminativo());
        variabiliParticellaColtura.setSuperficieAnomalieCoordinamento(particella.getSuperficieAnomalieCoordinamento());
        variabiliParticellaColtura.setSuperficieScostamento(particella.getSuperficieScostamento());
    }

    private void eleboraDatiParticella(Long idDomanda, Sostegno sostegno, DettaglioParticellaDto dettaglioPart) throws Exception {
        List<String> jsonInfoColtivazione = richiestaSuperficieDao.getinfoColtivazione(
                idDomanda, dettaglioPart.getInfoCatastali().getIdParticella(), sostegno, dettaglioPart.getCodiceColtura3());

        if (!jsonInfoColtivazione.isEmpty()) {
            InformazioniColtivazione infoColtivazione = mapper.readValue(jsonInfoColtivazione.get(0), InformazioniColtivazione.class);
            dettaglioPart.setDescrizioneColtura(infoColtivazione.getDescrizioneColtura());

            dettaglioPart.getVariabiliCalcoloParticella().setSuperficieDeterminata(Float.valueOf(Math
                    .round(dettaglioPart.getVariabiliCalcoloParticella().getSuperficieDeterminata() != null ? dettaglioPart.getVariabiliCalcoloParticella().getSuperficieDeterminata() * 10000 : 0)));
            dettaglioPart.getVariabiliCalcoloParticella().setSuperficieEleggibile(Float.valueOf(
                    Math.round(dettaglioPart.getVariabiliCalcoloParticella().getSuperficieEleggibile() != null ? dettaglioPart.getVariabiliCalcoloParticella().getSuperficieEleggibile() * 10000 : 0)));
            dettaglioPart.getVariabiliCalcoloParticella().setSuperficieImpegnata(Float.valueOf(
                    Math.round(dettaglioPart.getVariabiliCalcoloParticella().getSuperficieImpegnata() != null ? dettaglioPart.getVariabiliCalcoloParticella().getSuperficieImpegnata() * 10000 : 0)));
            dettaglioPart.getVariabiliCalcoloParticella().setSuperficieSigeco(Float.valueOf(
                    Math.round(dettaglioPart.getVariabiliCalcoloParticella().getSuperficieSigeco() != null ? dettaglioPart.getVariabiliCalcoloParticella().getSuperficieSigeco() * 10000 : 0)));
            dettaglioPart.getVariabiliCalcoloParticella().setSuperficieAnomalieCoordinamento(Float.valueOf(
                    Math.round(dettaglioPart.getVariabiliCalcoloParticella().getSuperficieAnomalieCoordinamento() != null ? dettaglioPart.getVariabiliCalcoloParticella().getSuperficieAnomalieCoordinamento() * 10000 : 0)));
            dettaglioPart.getVariabiliCalcoloParticella().setSuperficieScostamento(Float.valueOf(
                    Math.round(dettaglioPart.getVariabiliCalcoloParticella().getSuperficieScostamento() != null ? dettaglioPart.getVariabiliCalcoloParticella().getSuperficieScostamento() * 10000 : 0)));
        }
    }

    public static List<IDettaglioParticella> from(List<Object[]> resultObjects) {
        return Optional.ofNullable(resultObjects)
                       .orElse(new ArrayList<>())
                       .stream()
                       .map(objects -> from(objects))
                       .collect(Collectors.toList());
    }

    private static IDettaglioParticella from(Object[] objects) {
        DettaglioParticellaImpl dettaglioParticella = new DettaglioParticellaImpl();
        dettaglioParticella.setCodiceColtura3(getStringOrNull(objects[1]));
        dettaglioParticella.setIdParticella(getLongOrNull(objects[2]));
        dettaglioParticella.setComune(getStringOrNull(objects[3]));
        dettaglioParticella.setCodNazionale(getStringOrNull(objects[4]));
        dettaglioParticella.setFoglio(getLongOrNull(objects[5]));
        dettaglioParticella.setParticella(getStringOrNull(objects[6]));
        dettaglioParticella.setSub(getStringOrNull(objects[7]));
        dettaglioParticella.setSuperficieImpegnata(getFloatOrNull(objects[8]));
        dettaglioParticella.setSuperficieEleggibile(getFloatOrNull(objects[9]));
        dettaglioParticella.setSuperficieSigeco(getFloatOrNull(objects[10]));
        dettaglioParticella.setAnomalieMantenimento(getBooleanOrNull(objects[11]));
        dettaglioParticella.setAnomalieCoordinamento(getBooleanOrNull(objects[12]));
        dettaglioParticella.setSuperficieDeterminata(getFloatOrNull(objects[13]));
        dettaglioParticella.setTipoColtura(getStringOrNull(objects[14]));
        dettaglioParticella.setTipoSeminativo(getStringOrNull(objects[15]));
        dettaglioParticella.setColturaPrincipale(getBooleanOrNull(objects[16]));
        dettaglioParticella.setPascolo(getStringOrNull(objects[17]));
        dettaglioParticella.setSecondaColtura(getBooleanOrNull(objects[18]));
        dettaglioParticella.setSuperficieAnomalieCoordinamento(getFloatOrNull(objects[19]));
        dettaglioParticella.setSuperficieScostamento(getFloatOrNull(objects[20]));
        dettaglioParticella.setAzotoFissatrice(getBooleanOrNull(objects[objects.length - 1]));
        return dettaglioParticella;
    }

    private static String getStringOrNull(Object obj) {
        return (obj!=null)? obj.toString():null;
    }

    private static Float getFloatOrNull(Object obj) {
    	return (obj != null && obj.toString().trim().length() > 0)? Float.valueOf(obj.toString()):null;
//        return (obj!=null)? Float.valueOf(obj.toString()):null;
    }

    private static Boolean getBooleanOrNull(Object obj) {
        return (obj!=null)? Boolean.valueOf(obj.toString()):Boolean.FALSE;
    }

    private static Long getLongOrNull(Object obj) {
        return (obj!=null)? Long.valueOf(obj.toString()):null;
    }
}

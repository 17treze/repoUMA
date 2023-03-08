package it.tndigitale.a4gistruttoria.service.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import it.tndigitale.a4gistruttoria.dto.lavorazione.ControlloFrontend;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaPascoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioPascoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoreDisPascoliBuilder;
import it.tndigitale.a4gistruttoria.repository.model.A4gtPascoloParticella;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreDisPascoliModel;
import it.tndigitale.a4gistruttoria.repository.model.EsitoMantenimentoPascolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;
import it.tndigitale.a4gistruttoria.util.StringSupport;

public class DettaglioPascoliBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DettaglioPascoliBuilder.class);

    private A4gtPascoloParticella pascolo;
    private IstruttoriaModel istruttoria;
    private MapperWrapper mapper;
    private DettaglioPascoli dettaglioPascoli;
    private final String PASCOLO_GENERICO = "999TN999";

    public DettaglioPascoliBuilder(A4gtPascoloParticella pascolo,
                                   IstruttoriaModel istruttoria,
                                   MapperWrapper mapper) {
        this.mapper = mapper;
        this.pascolo = pascolo;
        this.istruttoria = istruttoria;
        dettaglioPascoli = creaDettaglioPascoli(pascolo);
    }

    public DettaglioPascoliBuilder withEsitoMantenimento() {
        EsitoMantenimentoPascolo esito = getEsitoMantenimentoFrom(pascolo, istruttoria.getId());
        if (esito!=null) {
            dettaglioPascoli.setEsitoMan(esito.getEsitoMan());
            addDatiInput(dettaglioPascoli,  esito.getDatiInput());
            addDatiOutput(dettaglioPascoli,  esito.getDatiOutput());
        }
        return this;
    }

    public DettaglioPascoliBuilder withDatiIstruttoriaPascoli() {
        Set<DatiIstruttoreDisPascoliModel> datiIstruttoreDisPascoli = istruttoria.getDatiIstruttoreDisPascoli();
        if (!CollectionUtils.isEmpty(datiIstruttoreDisPascoli)) {
        	List<DatiIstruttoriaPascoli> datiIstruttoriaPascoliList = datiIstruttoreDisPascoli.stream()
        			.map(DatiIstruttoreDisPascoliBuilder::from)
        			.collect(Collectors.toList()); 
            Optional<DatiIstruttoriaPascoli> datiIstruttoriaPascoliOpt = datiIstruttoriaPascoliList
                    .stream()
                    .filter(x -> compareDescrizionePascolo(x.getDescrizionePascolo(), pascolo.getDescPascolo()))
                    .findFirst();
            if (datiIstruttoriaPascoliOpt.isPresent()) {
                dettaglioPascoli.setDatiIstruttoriaPascoli(datiIstruttoriaPascoliOpt.get());
            }
        }
        return this;
    }

    public DettaglioPascoli build() {
        return dettaglioPascoli;
    }


    private Boolean compareDescrizionePascolo(String descrizionePascolo1, String descrizionePascolo2) {
        return descrizionePascolo1!=null &&
               descrizionePascolo2!=null &&
               descrizionePascolo1.equals(descrizionePascolo2);
    }


    private DettaglioPascoli creaDettaglioPascoli(A4gtPascoloParticella pascolo) {
        DettaglioPascoli dettaglioPascoli = new DettaglioPascoli();
        dettaglioPascoli.setCodicePascolo(pascolo.getCodPascolo());
        dettaglioPascoli.setSupNettaPascolo(pascolo.getSupNettaPascolo());
        dettaglioPascoli.setDescPascolo((pascolo.getDescPascolo().startsWith("MALGA") ? pascolo.getCodPascolo() : pascolo.getDescPascolo()));

        if (pascolo.getCodPascolo().startsWith(PASCOLO_GENERICO)) {
            dettaglioPascoli.setTipoPascolo("AZIENDALE");
        } else if (pascolo.getCodPascolo().substring(3, 5).equals("TN")) {
            dettaglioPascoli.setTipoPascolo("MALGA TN");
        } else {
            dettaglioPascoli.setTipoPascolo("MALGA FUORI PROV");
        }

        return dettaglioPascoli;
    }

    private void addDatiInput(DettaglioPascoli dettaglioPascoli,
                              String datiInputContent) {
        if (StringSupport.isNotEmpty(datiInputContent)) {
            List<VariabileCalcolo> datiInput = mapper.readValue(datiInputContent, new TypeReference<List<VariabileCalcolo>>() {});
            List<VariabileCalcolo> lista_vc = datiInput;
            settingVariabiliCalcolo(lista_vc);
            dettaglioPascoli.setDatiInput(datiInput);
        }
    }

    private void addDatiOutput(DettaglioPascoli dettaglioPascoli,
                               String datiOutputContent) {
        if (StringSupport.isNotEmpty(datiOutputContent)) {
            DatiSintesi datiSintesiPascolo = mapper.readValue(datiOutputContent, DatiSintesi.class);
            List<VariabileCalcolo> datiOutput = datiSintesiPascolo.getVariabiliCalcoloDaStampare();
            List<VariabileCalcolo> lista_vc = datiOutput;
            settingVariabiliCalcolo(lista_vc);
            dettaglioPascoli.setDatiOutput(datiOutput);

            datiSintesiPascolo.getEsitiControlli().forEach(es -> {
                try {
                    ControlloFrontend controlloFrontend = ControlloFrontend.newControlloFrontend(null, es);
                    if (controlloFrontend != null)
                        dettaglioPascoli.getListaEsitiPascolo().add(controlloFrontend);
                } catch (Exception ex) {
                    logger.error("Errore nel metodo addDatiOutput", ex);
                }
            });
        }
    }

    private void settingVariabiliCalcolo(List<VariabileCalcolo> lista_vc) {
        for (int i = 0; i < lista_vc.size(); i++) {
            VariabileCalcolo vc = lista_vc.get(i);
            if (vc != null) {
                vc.setValString(vc.getTipoVariabile().getDescrizione() + "**" + vc.recuperaValoreString(false));
                lista_vc.set(i, vc);
            } else {
                lista_vc.remove(i);
            }
        }
    }

    private EsitoMantenimentoPascolo getEsitoMantenimentoFrom(A4gtPascoloParticella particella,
                                                              Long idIstruttoria) {
        return Optional.ofNullable(particella.getEsitiMantenimentoPascolo())
                .orElse(new ArrayList<>())
                .stream()
                .filter(esito -> idIstruttoria.equals(esito.getIstruttoria().getId()))
                .findFirst()
                .orElse(null);
    }
}

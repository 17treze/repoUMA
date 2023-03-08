package it.tndigitale.a4gistruttoria.service;

import static it.tndigitale.a4g.framework.pagination.model.Paginazione.getOrDefault;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.ConvertitorePaginazione;
import it.tndigitale.a4gistruttoria.dto.CsvFile;
import it.tndigitale.a4gistruttoria.dto.DettaglioParticellaDto;
import it.tndigitale.a4gistruttoria.dto.IDettaglioParticella;
import it.tndigitale.a4gistruttoria.dto.Pagina;
import it.tndigitale.a4gistruttoria.dto.csv.DettaglioParticellaCsvEleggibilita;
import it.tndigitale.a4gistruttoria.dto.csv.DettaglioParticellaCsvGreening;
import it.tndigitale.a4gistruttoria.dto.csv.DettaglioParticellaCsvMantenimento;
import it.tndigitale.a4gistruttoria.dto.filter.DettaglioParticellaRequest;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioSuperficieCalcoloDto;
import it.tndigitale.a4gistruttoria.repository.dao.DatiParticellaColturaDao;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.support.DatiParticellaSupport;

@Service
public class DettaglioParticellaService {
    private IstruttoriaComponent istruttoriaComponent;
    private DatiParticellaSupport datiParticellaSupport;
    private DatiParticellaColturaDao daoDatiParticellaColtura;
    @Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;
    @Autowired
	private ObjectMapper objectMapper;
    
    @Autowired
    public DettaglioParticellaService setComponents(IstruttoriaComponent istruttoriaComponent,
                                                    DatiParticellaSupport datiParticellaSupport,
                                                    DatiParticellaColturaDao daoDatiParticellaColtura) {
        this.istruttoriaComponent = istruttoriaComponent;
        this.datiParticellaSupport = datiParticellaSupport;
        this.daoDatiParticellaColtura = daoDatiParticellaColtura;
        return this;
    }

    public Pagina<DettaglioParticellaDto> getDettaglioParticellaPaginabile(DettaglioParticellaRequest dettaglioParticellaRequest) throws Exception {
        Pageable pageable = PageableBuilder.build().from(getOrDefault(dettaglioParticellaRequest.getPaginazione()),
                Ordinamento.getOrDefault(dettaglioParticellaRequest.getOrdinamento()));
        IstruttoriaModel istruttoria = istruttoriaComponent.load(dettaglioParticellaRequest.getIdIstruttoria());
        List<Object[]> resultObjects = daoDatiParticellaColtura.getDettaglioParticellaByIdIstruttoriaPaginata(
                dettaglioParticellaRequest.getIdIstruttoria(),
                (dettaglioParticellaRequest.getPascolo() != null)? dettaglioParticellaRequest.getPascolo().toString():null,
                pageable);
        List<IDettaglioParticella> particelle = DatiParticellaSupport.from(resultObjects);
        List<DettaglioParticellaDto> dettaglioParticelle = datiParticellaSupport.from(particelle, istruttoria);
        Long count = daoDatiParticellaColtura.countFindByIdIstruttoria(
                dettaglioParticellaRequest.getIdIstruttoria(),
                (dettaglioParticellaRequest.getPascolo() != null)? dettaglioParticellaRequest.getPascolo().toString():null);
        PageImpl<DettaglioParticellaDto> page = new PageImpl<>(dettaglioParticelle, pageable, count);
        return ConvertitorePaginazione.converti(page);
    }

	public List<DettaglioSuperficieCalcoloDto> getDettaglioSuperficiePerCalcolo(
			final Long idDomanda, final Long idParticella, final String codiceColtura) throws IOException {
		List<String> particellaIntervento = daoRichiestaSuperficie.findIdParcelleByDomandaParticellaIntervento(
				idDomanda, idParticella, CodiceInterventoAgs.BPS, codiceColtura);
		List<DettaglioSuperficieCalcoloDto> toList = new ArrayList<>();
		for (String jsonRiferimentiCartograficiString : particellaIntervento) {
			if (StringUtils.isEmpty(jsonRiferimentiCartograficiString)) {
				return toList;
			}
			JsonNode jsonRiferimentiCartografici = objectMapper.readTree(jsonRiferimentiCartograficiString);
			DettaglioSuperficieCalcoloDto dsc = new DettaglioSuperficieCalcoloDto();
			dsc.setIdDomanda(idDomanda);
			dsc.setIdParticella(idParticella);
			dsc.setCodiceCultura(codiceColtura);
			JsonNode jsonNode = jsonRiferimentiCartografici.get("idParcella");
			if (jsonNode == null) continue;
			Long idParcella = jsonNode.asLong();
			dsc.getIdParcelle().add(idParcella);
			toList.add(dsc);
		}
		return toList;
	}

    public CsvFile getDettaglioParticellaCsvEleggibilita(final Long idIstruttoria) throws Exception {
    	IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);
        List<Object[]> resultObjects = daoDatiParticellaColtura.getDettaglioParticellaByIdIstruttoriaPaginata(
        		idIstruttoria, null, Pageable.unpaged());
        List<IDettaglioParticella> particelle = DatiParticellaSupport.from(resultObjects);
        List<DettaglioParticellaDto> dettaglioParticelleDto = datiParticellaSupport.from(particelle, istruttoria);
        List<DettaglioParticellaCsvEleggibilita> dettaglioParticellaCsv = DettaglioParticellaCsvEleggibilita.fromDto(dettaglioParticelleDto);
		ObjectWriter objWriter = new CsvMapper()
				.writerFor(DettaglioParticellaCsvEleggibilita.class)
				.with(DettaglioParticellaCsvEleggibilita.getSchema());
		ByteArrayOutputStream csvByteArray = new ByteArrayOutputStream();
		objWriter.writeValues(csvByteArray).writeAll(dettaglioParticellaCsv);
		CsvFile csvFile = new CsvFile();
		csvFile.setCsvByteArray(csvByteArray.toByteArray());
		csvFile.setCsvFileName(String.format("dettaglio-particella-eleggibilita-%d.csv", idIstruttoria));
		return csvFile;
    }
    
    public CsvFile getDettaglioParticellaCsvGreening(final Long idIstruttoria) throws Exception {
    	IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);
        List<Object[]> resultObjects = daoDatiParticellaColtura.getDettaglioParticellaByIdIstruttoriaPaginata(
        		idIstruttoria, null, Pageable.unpaged());
        List<IDettaglioParticella> particelle = DatiParticellaSupport.from(resultObjects);
        List<DettaglioParticellaDto> dettaglioParticelleDto = datiParticellaSupport.from(particelle, istruttoria);
        List<DettaglioParticellaCsvGreening> dettaglioParticellaCsv = DettaglioParticellaCsvGreening.fromDto(dettaglioParticelleDto);
		ObjectWriter objWriter = new CsvMapper()
				.writerFor(DettaglioParticellaCsvGreening.class)
				.with(DettaglioParticellaCsvGreening.getSchema());
		ByteArrayOutputStream csvByteArray = new ByteArrayOutputStream();
		objWriter.writeValues(csvByteArray).writeAll(dettaglioParticellaCsv);
		CsvFile csvFile = new CsvFile();
		csvFile.setCsvByteArray(csvByteArray.toByteArray());
		csvFile.setCsvFileName(String.format("dettaglio-particella-greening-%d.csv", idIstruttoria));
		return csvFile;
    }
    
    public CsvFile getDettaglioParticellaCsvMantenimento(final Long idIstruttoria) throws Exception {
    	IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);
        List<Object[]> resultObjects = daoDatiParticellaColtura.getDettaglioParticellaByIdIstruttoriaPaginata(
        		idIstruttoria, "true", Pageable.unpaged());
        List<IDettaglioParticella> particelle = DatiParticellaSupport.from(resultObjects);
        List<DettaglioParticellaDto> dettaglioParticelleDto = datiParticellaSupport.from(particelle, istruttoria);
        List<DettaglioParticellaCsvMantenimento> dettaglioParticellaCsv = DettaglioParticellaCsvMantenimento.fromDto(dettaglioParticelleDto);
		ObjectWriter objWriter = new CsvMapper()
				.writerFor(DettaglioParticellaCsvMantenimento.class)
				.with(DettaglioParticellaCsvMantenimento.getSchema());
		ByteArrayOutputStream csvByteArray = new ByteArrayOutputStream();
		objWriter.writeValues(csvByteArray).writeAll(dettaglioParticellaCsv);
		CsvFile csvFile = new CsvFile();
		csvFile.setCsvByteArray(csvByteArray.toByteArray());
		csvFile.setCsvFileName(String.format("dettaglio-particella-mantenimento-%d.csv", idIstruttoria));
		return csvFile;
    }
}

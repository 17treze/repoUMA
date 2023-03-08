package it.tndigitale.a4g.uma.business.service.protocollo;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.DetenzioneAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.DetenzioneAgsDto.TipoDetenzioneEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto.StatoEnum;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ParticellaDto;
import it.tndigitale.a4g.framework.client.custom.VerificaFirmaClient;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.builder.ColturaTestBuilder;
import it.tndigitale.a4g.uma.builder.ParticellaTestBuilder;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.SuperficieMassimaModel;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.dto.protocollo.ProtocollaDocumentoUmaDto;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
public class ProtocolloServiceTest {

    @Autowired
    private ProtocolloService protocolloService;
    @Autowired
    private RichiestaCarburanteDao richiestaCarburanteDao;
    
    @SpyBean
    private Clock clock;
    @SpyBean
    private UmaAnagraficaClient anagraficaClient;
    @SpyBean
    private VerificaFirmaClient verificaFirmaClient;
    @SpyBean
    private UmaTerritorioClient territorioClient;
    @SpyBean
    private EventBus eventBus;
    
    @Captor
    private ArgumentCaptor<ProtocollaDocumentoUmaEvent> eventCaptor;    
    
    @Test
    @Transactional
    @Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/richiesta/richiesta_carburante_controller_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void protocollaRichiestaTest() throws Exception {
        mockClockNow();
        mockUmaAnagraficaClientGetFascicolo();
        mockUmaAnagraficaClientGetTitolariRappresentantiLegali();
        mockVerificaFirmaClientVerificaFirma();
        mockUmaTerritorioClientGetColture();
        
        long idRichiesta = 13L;
        ByteArrayResource documento = getDocumento();
        
        protocolloService.protocollaRichiesta(idRichiesta, documento, false);
        
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
        
        Mockito.verify(eventBus).publishEvent(eventCaptor.capture());
        
        ProtocollaDocumentoUmaEvent event = eventCaptor.getValue();
        ProtocollaDocumentoUmaDto protocollaDocumento = event.getData();
        
        assertEquals(documento, protocollaDocumento.getDocumento());
	assertEquals(idRichiesta, protocollaDocumento.getId());
	assertEquals("BBBDNL95R14L378T", protocollaDocumento.getCuaa());
	assertEquals(2020, protocollaDocumento.getAnno());
	assertEquals("<nome>", protocollaDocumento.getNome());
	assertEquals("<cognome>", protocollaDocumento.getCognome());
	assertEquals("<denominazione>", protocollaDocumento.getDescrizioneImpresa());
	assertEquals("<pec>", protocollaDocumento.getPec());
	assertEquals(TipoDocumentoUma.RICHIESTA, protocollaDocumento.getTipoDocumentoUma());
        
        RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(idRichiesta).get();
        
        assertEquals("BBBDNL95R14L378T", richiesta.getCuaa());
	assertEquals(2020, richiesta.getCampagna());
	assertEquals(LocalDateTime.of(2020, 9, 1, 0, 0), richiesta.getDataPresentazione());
	assertEquals("BBBDNL95R14L378T", richiesta.getCfRichiedente());
	assertEquals(null, richiesta.getProtocollo());
	assertEquals(null, richiesta.getDataProtocollazione());
	assertEquals(StatoRichiestaCarburante.AUTORIZZATA, richiesta.getStato());
	assertEquals(15, richiesta.getGasolio());
	assertEquals(0, richiesta.getBenzina());
	assertEquals(0, richiesta.getGasolioSerre());
	assertEquals(0, richiesta.getGasolioTerzi());
	assertEquals(null, richiesta.getNote());
	assertArrayEquals(documento.getByteArray(), richiesta.getDocumento());
	assertEquals("NO_DENOM", richiesta.getDenominazione());
	assertEquals(true, richiesta.getFirma());
        assertEquals("<sportello>", richiesta.getEntePresentatore());
        
        List<SuperficieMassimaModel> superfici = richiesta.getSuperficiMassime();
        
        List<long[]> actualIds = superfici
            .stream()
            .map(superficie -> new long[] {
                superficie.getRichiestaCarburante().getId(),
                superficie.getGruppoLavorazione().getId(),
                superficie.getSuperficieMassima()
            })
            .collect(Collectors.toList());
        
        List<long[]> expectedIds = Arrays.asList(
            new long[] {13, 955, 1652},
            new long[] {13, 951, 1422},
            new long[] {13, 947, 5253},
            new long[] {13, 953, 748}
        );
        
        Assertions
            .assertThat(actualIds)
            .hasSameElementsAs(expectedIds);
    }
    
    private void mockClockNow() {
        Mockito
            .doReturn(LocalDateTime.of(2020, 3, 3, 0, 0))
            .when(clock).now();
    }
    private void mockUmaAnagraficaClientGetFascicolo() {
        DetenzioneAgsDto detenzione = new DetenzioneAgsDto();
        detenzione.setTipoDetenzione(TipoDetenzioneEnum.MANDATO);
        detenzione.setSportello("<sportello>");
        
        FascicoloAgsDto fascicolo = new FascicoloAgsDto();
        fascicolo.setDataValidazione(LocalDateTime.of(2020, 1, 1, 0, 0));
        fascicolo.setStato(StatoEnum.VALIDO);
        fascicolo.setDenominazione("<denominazione>");
        fascicolo.setPec("<pec>");
        fascicolo.setDetenzioni(Arrays.asList(detenzione));

        Mockito
            .doReturn(fascicolo)
            .when(anagraficaClient).getFascicolo(Mockito.any());
    }
    private void mockUmaAnagraficaClientGetTitolariRappresentantiLegali() {
        CaricaAgsDto soggetto = new CaricaAgsDto();
        soggetto.setNome("<nome>");
        soggetto.setCognome("<cognome>");
        soggetto.setCodiceFiscale("BBBDNL95R14L378T");
        
        List<CaricaAgsDto> soggetti = Arrays.asList(soggetto);

        Mockito
            .doReturn(soggetti)
            .when(anagraficaClient).getTitolariRappresentantiLegali(Mockito.any());
    }
    private void mockVerificaFirmaClientVerificaFirma() throws Exception {
        Mockito
            .doReturn(null)
            .when(verificaFirmaClient).verificaFirma(Mockito.any(), Mockito.any());
    }
    private void mockUmaTerritorioClientGetColture() {
        List<ParticellaDto> particelle = getParticelle();

        Mockito
            .doReturn(particelle)
            .when(territorioClient).getColture(Mockito.any(), Mockito.any());
    }
    
    private ByteArrayResource getDocumento() throws IOException {
        Path path = Paths.get("src/test/resources/documentiFirmati/MANDATO_ftoDPDNDR77B03L378L.pdf");
        
        byte[] bytes = Files.readAllBytes(path);
        
        ByteArrayResource byteArrayResource = new ByteArrayResource(bytes) {

            @Override
            public String getFilename() {
                    return "1891_richiestacarburante.pdf";
            }
        };
        
        return byteArrayResource;
    }
    private List<ParticellaDto> getParticelle() {
        ParticellaDto particella1 = new ParticellaTestBuilder()
            .withInfoCatastali("012", "9999", ".1522", null)
            .addColtura(new ColturaTestBuilder()
                .descrizione("2 - colturaMaisSorgo")
                .withCodifica("008", "001", "042", "000", "000")
                .withSuperficie(5253)
                .build()
            )
            .addColtura(new ColturaTestBuilder()
                .descrizione("6 - colturaPascolo")
                .withCodifica("000", "382", "000", "009", "000")
                .withSuperficie(1422)
                .build()
            )
            .build();

        ParticellaDto particella2 = new ParticellaTestBuilder()
            .withInfoCatastali("621", "9999", "01514/A", "A")
            .addColtura(new ColturaTestBuilder()
                .descrizione("8 - colturaLattugheInsalateRadicchi")
                .withCodifica("007", "919", "000", "000", "000")
                .withSuperficie(123)
                .build()
            )
            .addColtura(new ColturaTestBuilder()
                .descrizione("8 - colturaLattugheInsalateRadicchi_1")
                .withCodifica("008", "680", "000", "000", "000")
                .withSuperficie(625)
                .build()
            )
            .addColtura(new ColturaTestBuilder()
                .descrizione("10 - colturaViteDaVinoDaTavola")
                .withCodifica("005", "410", "000", "000", "507")
                .withSuperficie(1652)
                .build()
            )
            .build();
                    
        List<ParticellaDto> particelle = Arrays.asList(particella1, particella2);
        
        return particelle;
    }
}
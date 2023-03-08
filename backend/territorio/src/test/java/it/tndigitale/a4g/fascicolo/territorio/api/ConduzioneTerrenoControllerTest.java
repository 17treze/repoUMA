package it.tndigitale.a4g.fascicolo.territorio.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.territorio.business.service.ConduzioneTerrenoService;
import it.tndigitale.a4g.fascicolo.territorio.dto.TitoloConduzione;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.SottotipoConduzioneDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.SottotipoDocumentoDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.conduzione.TipoConduzioneDto;
import it.tndigitale.a4g.territorio.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.territorio.business.persistence.entity.ParticelleFondiarieModel;
import it.tndigitale.a4g.territorio.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.territorio.business.persistence.repository.ParticelleFondiarieDao;
import it.tndigitale.a4g.territorio.dto.ConduzioneTerreniDto;
import it.tndigitale.a4g.territorio.dto.DocumentoConduzioneDto;
import it.tndigitale.a4g.territorio.dto.ParticellaFondiariaDto;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ConduzioneTerrenoControllerTest {

    @Autowired private ConduzioneTerrenoService conduzioneTerrenoService;
    @Autowired private ConduzioneTerrenoController conduzioneTerrenoController;
    @Autowired private FascicoloDao fascicoloDao;
    @Autowired private ParticelleFondiarieDao particelleFondiarieDao;

	 
    @Test
    @Sql(scripts = "/sql/conduzione-terreno/elenco_a4gd_tipo_conduzione_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/conduzione-terreno/elenco_a4gd_tipo_conduzione_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void elenco_documenti_conduzione() throws Exception {
    	List<TipoConduzioneDto> resultTipologieConduzione = conduzioneTerrenoController.getElencoTipologieConduzione();
    	
    	//recupero tipologie
    	resultTipologieConduzione.forEach(tipoConduzioneDto -> {
    		//recupero sottotipologie
    		List<SottotipoConduzioneDto> resultSottotipoConduzione = conduzioneTerrenoService.getElencoSottoipologieConduzione(tipoConduzioneDto.getId());    			
    			//cliclo per le verifiche sui documenti
    			resultSottotipoConduzione.forEach(sottotipoConduzione->{    				    		
    				if (sottotipoConduzione.getDescrizione().equals("PROPRIETÀ INDIVISA")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(4, resultSottotipoDocumento.size());
    					
    					assertEquals(4, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("NUDA PROPRIETÀ")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(5, resultSottotipoDocumento.size());
    					
    					assertEquals(4, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("IRREPERIBILITÀ")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(5, resultSottotipoDocumento.size());
    					
    					assertEquals(4, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("COMPROPRIETÀ")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(5, resultSottotipoDocumento.size());
    					
    					assertEquals(4, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("COMUNIONE DEI BENI FRA CONIUGI")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(5, resultSottotipoDocumento.size());
    					
    					assertEquals(4, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("USUCAPIONE")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(1, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("CONFERIMENTO DI SUPERFICIDA PARTE DI UN SOCIO")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(1, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("AFFITTO A \"GIOVANI AGRICOLTORI\"")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(4, resultSottotipoDocumento.size());
    					
    					assertEquals(2, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(2, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    		
    				if (sottotipoConduzione.getDescrizione().equals("AFFITTO SCRITTO")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(3, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(2, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("AFFITTO VERBALE")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(5, resultSottotipoDocumento.size());
    					 
    					assertEquals(2, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(3, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("MEZZADRIA")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(1, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("COLONIA PARZIARIA")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(1, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("ENFITEUSI")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(1, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("AFFRANCAZIONE DELL'ENFITEUSI")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(1, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("USO CIVICO")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(2, resultSottotipoDocumento.size());
    					
    					assertEquals(2, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("USUFRUTTO")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(2, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("CONCESSIONE E LOCAZIONE DI BENI IMMOBILI DEMANIALI")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(2, resultSottotipoDocumento.size());
    					
    					assertEquals(2, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("COMODATO SCRITTO")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(3, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(2, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("COMODATO VERBALE")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(3, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(2, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("USO OGGETTIVO")) {    					
    					List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(1, resultSottotipoDocumento.size());
    					
    					assertEquals(1, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    				if (sottotipoConduzione.getDescrizione().equals("ZONE MONTANE")) {    					
						List<SottotipoDocumentoDto> resultSottotipoDocumento = conduzioneTerrenoService.getElencoSottotipoDocumenti(sottotipoConduzione.getId());
    					assertNotNull(resultSottotipoDocumento);
    					assertEquals(2, resultSottotipoDocumento.size());
    					
    					assertEquals(2, resultSottotipoDocumento.stream().filter(res -> "P".equals(res.getTipo())).count());
    					assertEquals(0, resultSottotipoDocumento.stream().filter(res -> "S".equals(res.getTipo())).count());    			 
    				}    			
    			});
    		
    	});
    	
    	
    }
    
    
    
    @Test
    @Sql(scripts = "/sql/conduzione-terreno/elenco_a4gd_tipo_conduzione_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/conduzione-terreno/elenco_a4gd_tipo_conduzione_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void elenco_tipo_conduzione() throws Exception {
    	List<TipoConduzioneDto> resultTipologieConduzione = conduzioneTerrenoController.getElencoTipologieConduzione();
    	assertNotNull(resultTipologieConduzione);
        assertEquals(4, resultTipologieConduzione.size());
          
        resultTipologieConduzione.forEach(tipoConduzioneDto -> {
            if (tipoConduzioneDto.getDescrizione().equals("PROPRIETÀ")) {
            	List<SottotipoConduzioneDto> result = conduzioneTerrenoService.getElencoSottoipologieConduzione(tipoConduzioneDto.getId());
                assertNotNull( tipoConduzioneDto);                
                assertEquals(7, result.size());
               
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("PROPRIETÀ INDIVISA")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("NUDA PROPRIETÀ")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("COMPROPRIETÀ")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("COMUNIONE DEI BENI FRA CONIUGI")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("USUCAPIONE")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("CONFERIMENTO DI SUPERFICIDA PARTE DI UN SOCIO")).findFirst().isPresent());               
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("AFFITTO SCRITTO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("AFFITTO VERBALE")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("MEZZADRIA")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COLONIA PARZIARIA")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("ENFITEUSI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("AFFRANCAZIONE DELL'ENFITEUSI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USO CIVICO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USUFRUTTO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("CONCESSIONE E LOCAZIONE DI BENI IMMOBILI DEMANIALI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMODATO SCRITTO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMODATO VERBALE")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USO OGGETTIVO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("ZONE MONTANE")).findFirst().isPresent());

                		
            }
            if (tipoConduzioneDto.getDescrizione().equals("AFFITTO")) {
                List<SottotipoConduzioneDto> result = conduzioneTerrenoService.getElencoSottoipologieConduzione(tipoConduzioneDto.getId());
                assertNotNull( tipoConduzioneDto);
                result = conduzioneTerrenoService.getElencoSottoipologieConduzione(tipoConduzioneDto.getId());
                assertEquals(3, result.size());
                
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("PROPRIETÀ INDIVISA")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("NUDA PROPRIETÀ")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMPROPRIETÀ")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMUNIONE DEI BENI FRA CONIUGI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USUCAPIONE")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("CONFERIMENTO DI SUPERFICIDA PARTE DI UN SOCIO")).findFirst().isPresent());               
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("AFFITTO SCRITTO")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("AFFITTO VERBALE")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("MEZZADRIA")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COLONIA PARZIARIA")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("ENFITEUSI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("AFFRANCAZIONE DELL'ENFITEUSI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USO CIVICO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USUFRUTTO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("CONCESSIONE E LOCAZIONE DI BENI IMMOBILI DEMANIALI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMODATO SCRITTO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMODATO VERBALE")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USO OGGETTIVO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("ZONE MONTANE")).findFirst().isPresent());
            }
            if (tipoConduzioneDto.getDescrizione().equals("MEZZADRIA")) {
                List<SottotipoConduzioneDto> result = conduzioneTerrenoService.getElencoSottoipologieConduzione(tipoConduzioneDto.getId());
                result = conduzioneTerrenoService.getElencoSottoipologieConduzione(tipoConduzioneDto.getId());
                assertEquals(2, result.size());
                
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("PROPRIETÀ INDIVISA")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("NUDA PROPRIETÀ")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMPROPRIETÀ")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMUNIONE DEI BENI FRA CONIUGI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USUCAPIONE")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("CONFERIMENTO DI SUPERFICIDA PARTE DI UN SOCIO")).findFirst().isPresent());               
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("AFFITTO SCRITTO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("AFFITTO VERBALE")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("MEZZADRIA")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("COLONIA PARZIARIA")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("ENFITEUSI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("AFFRANCAZIONE DELL'ENFITEUSI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USO CIVICO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USUFRUTTO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("CONCESSIONE E LOCAZIONE DI BENI IMMOBILI DEMANIALI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMODATO SCRITTO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMODATO VERBALE")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USO OGGETTIVO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("ZONE MONTANE")).findFirst().isPresent());
            }
            if (tipoConduzioneDto.getDescrizione().equals("ALTRO")) {            	
                List<SottotipoConduzioneDto> result = conduzioneTerrenoService.getElencoSottoipologieConduzione(tipoConduzioneDto.getId());
                result = conduzioneTerrenoService.getElencoSottoipologieConduzione(tipoConduzioneDto.getId());
                assertEquals(9, result.size());
            
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("PROPRIETÀ INDIVISA")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("NUDA PROPRIETÀ")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMPROPRIETÀ")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COMUNIONE DEI BENI FRA CONIUGI")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("USUCAPIONE")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("CONFERIMENTO DI SUPERFICIDA PARTE DI UN SOCIO")).findFirst().isPresent());               
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("AFFITTO SCRITTO")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("AFFITTO VERBALE")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("MEZZADRIA")).findFirst().isPresent());
                assertEquals(false, result.stream().filter(o -> o.getDescrizione().equals("COLONIA PARZIARIA")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("ENFITEUSI")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("AFFRANCAZIONE DELL'ENFITEUSI")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("USO CIVICO")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("USUFRUTTO")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("CONCESSIONE E LOCAZIONE DI BENI IMMOBILI DEMANIALI")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("COMODATO SCRITTO")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("COMODATO VERBALE")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("USO OGGETTIVO")).findFirst().isPresent());
                assertEquals(true, result.stream().filter(o -> o.getDescrizione().equals("ZONE MONTANE")).findFirst().isPresent());
            
            }
        });
    }
 
	

    @Test
    @WithMockUser(username = "caa")
    @Sql(scripts = "/sql/conduzione-terreno/elenco_tipo_conduzione_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/conduzione-terreno/user_data_delete.sql", "/sql/conduzione-terreno/elenco_tipo_conduzione_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Transactional
    void salvataggio_conduzione() throws Exception {
        String cuaa = "XPDNDR77B03L378X";
        ConduzioneTerreniDto conduzioneTerreno = new ConduzioneTerreniDto();
        conduzioneTerreno.setAmbito(TitoloConduzione.AFFITTO);
        conduzioneTerreno.setIdSottotipo(6L);//AFFITTO
        List<DocumentoConduzioneDto> documentoConduzioneDtoList = new ArrayList<>();
        DocumentoConduzioneDto documentoConduzioneDto = new DocumentoConduzioneDto();
        documentoConduzioneDto.setIdTipoDocumento(15L); //CONTRATTO DI AFFITTO
        documentoConduzioneDto.setDataInizioValidita(LocalDate.now());
        documentoConduzioneDto.setDataFineValidita(LocalDate.now().plusDays(30L));
        documentoConduzioneDto.setContratto("contratto-test".getBytes(StandardCharsets.UTF_8));
        documentoConduzioneDtoList.add(documentoConduzioneDto);
        conduzioneTerreno.setDocumentiConduzione(documentoConduzioneDtoList);
        List<ParticellaFondiariaDto> particellaFondiariaDtoList = new ArrayList<>();
        ParticellaFondiariaDto particellaFondiariaDto = new ParticellaFondiariaDto();
        particellaFondiariaDto.setParticella("Prt01");
        particellaFondiariaDto.setComune("Comune di Rovereto");
        particellaFondiariaDto.setFoglio(222);
        particellaFondiariaDto.setSub("sb01");
        particellaFondiariaDto.setSuperficieCondotta(10L);
        particellaFondiariaDtoList.add(particellaFondiariaDto);
        conduzioneTerreno.setParticelleFondiarie(particellaFondiariaDtoList);
        conduzioneTerrenoController.salvaConduzioneTerreniA4g(cuaa, conduzioneTerreno);
        Optional<FascicoloModel> result = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);

        assertTrue(result.isPresent());
        List<ParticelleFondiarieModel> particelleFondiarieModelList = particelleFondiarieDao.findByConduzione_Fascicolo_CuaaAndConduzione_Fascicolo_IdValidazione(cuaa, 0);
        assertEquals(1, particelleFondiarieModelList.size());
        assertEquals("Prt01", particelleFondiarieModelList.get(0).getParticella());
        assertEquals("Comune di Rovereto", particelleFondiarieModelList.get(0).getComune());
        assertEquals("sb01", particelleFondiarieModelList.get(0).getSub());
        assertEquals(222, particelleFondiarieModelList.get(0).getFoglio());
    }

    @Test
    @Sql(scripts = "/sql/conduzione-terreno/tipo_conduzione_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/conduzione-terreno/tipo_conduzione_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getElencoTipologieConduzione() throws Exception {
        var result = conduzioneTerrenoController.getElencoTipologieConduzione();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TEST", result.get(0).getCodice());
    }

    @Test
    @Sql(scripts = "/sql/conduzione-terreno/tipo_conduzione_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/conduzione-terreno/tipo_conduzione_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getElencoSottoipologieConduzione() throws Exception {
        var result = conduzioneTerrenoController.getElencoSottoipologieConduzione(0L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(0, result.get(0).getIdTipologia());
        assertEquals("STTEST", result.get(0).getCodice());
    }

    @Test
    @Sql(scripts = "/sql/conduzione-terreno/tipo_conduzione_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/conduzione-terreno/tipo_conduzione_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getElencoDocumentiConduzionePerTipologia() throws Exception {
        var result = conduzioneTerrenoController.getElencoSottotipoDocumenti(0L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("TEST DOCUMENTO 1", result.get(0).getDescrizione());
        assertEquals("TEST DOCUMENTO 2", result.get(1).getDescrizione());
    }

    @Test
    @Sql(scripts = "/sql/conduzione-terreno/tipo_conduzione_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/conduzione-terreno/tipo_conduzione_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getDocumentiDipendenti() throws Exception {
        var result = conduzioneTerrenoController.getElencoSottotipoDocumenti(0L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getDocumentoDipendenza().length);
        assertEquals(1, result.get(0).getDocumentoDipendenza()[0]);
    }
}

package it.tndigitale.a4g.fascicolo.territorio.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.fascicolo.territorio.dto.TitoloConduzione;
import it.tndigitale.a4g.territorio.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.territorio.business.persistence.entity.ParticelleFondiarieModel;
import it.tndigitale.a4g.territorio.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.territorio.business.persistence.repository.ParticelleFondiarieDao;
import it.tndigitale.a4g.territorio.dto.ConduzioneTerreniDto;
import it.tndigitale.a4g.territorio.dto.DocumentoConduzioneDto;
import it.tndigitale.a4g.territorio.dto.ParticellaFondiariaDto;
import it.tndigitale.a4g.territorio.dto.TipoConduzioneDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ConduzioneTerrenoControllerMvcTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired private FascicoloDao fascicoloDao;
    @Autowired private ParticelleFondiarieDao particelleFondiarieDao;

    @Test
    @WithMockUser(username = "caa")
    @Sql(scripts = "/sql/conduzione-terreno/elenco_tipo_conduzione_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/sql/conduzione-terreno/user_data_delete.sql", "/sql/conduzione-terreno/elenco_tipo_conduzione_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void salvataggio_conduzione_ko_Particella_Lunga() throws Exception {
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
        particellaFondiariaDto.setParticella("Particella troppo lunga");
        particellaFondiariaDto.setComune("Comune di Rovereto");
        particellaFondiariaDto.setFoglio(222);
        particellaFondiariaDto.setSub("sb01");
        particellaFondiariaDto.setSuperficieCondotta(10L);
        particellaFondiariaDtoList.add(particellaFondiariaDto);
        conduzioneTerreno.setParticelleFondiarie(particellaFondiariaDtoList);

        mockMvc.perform(put(String.format("/api/v1/conduzione-terreno/salva/%s", cuaa))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(conduzioneTerreno)))
                    .andExpect(status().isInternalServerError());

        Optional<FascicoloModel> result = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
//      verifica che sia stato effettuato il rollback constatando l'assenza del fascicolo e delle particelle fondiarie
        assertTrue(result.isEmpty());
        List<ParticelleFondiarieModel> particelleFondiarieModelList = particelleFondiarieDao.findByConduzione_Fascicolo_CuaaAndConduzione_Fascicolo_IdValidazione(cuaa, 0);
        assertTrue(particelleFondiarieModelList.isEmpty());
    }
}

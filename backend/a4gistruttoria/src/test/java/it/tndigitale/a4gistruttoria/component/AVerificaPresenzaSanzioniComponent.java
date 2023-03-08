package it.tndigitale.a4gistruttoria.component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

public class AVerificaPresenzaSanzioniComponent {
	
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	protected void mockCheckDomandaPresentataAnnoPrecedente(String cuaa, Integer anno, boolean esiste) throws Exception {
		String serviceUrlCheckDomandePerSettore = "http://localhost:8080/ags/api/v1/domandeDU/checkPresenzaDomandeFiltered/" + anno + "/" + cuaa;
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlCheckDomandePerSettore)), Mockito.eq(Boolean.class))).thenReturn(esiste);
	}

	
	protected VariabileCalcolo getVC(List<VariabileCalcolo> input, TipoVariabile tipoV) {
		return input.stream().filter(v -> v != null && tipoV.equals(v.getTipoVariabile())).findFirst().orElse(null);
	}
	
	protected void mockCalcoliAnnoPrecedenteAGS(String numeroDomanda) throws Exception {
		String serviceAgs = "http://localhost:8080/ags/api/v1/domandeDU/".concat(numeroDomanda)
				.concat("?expand=premiSostegno.BPS_2017");
		DomandaUnica du = objectMapper.readValue(getDomandaUnicaAgsExpandedPremiSostegnoBPS(numeroDomanda), new TypeReference<DomandaUnica>() {
		});
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceAgs)), Mockito.eq(DomandaUnica.class))).thenReturn(du);
		
	}

	private String getDomandaUnicaAgsExpandedPremiSostegnoBPS(String numeroDomanda) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/calcoloDisaccoppiato/" + numeroDomanda + "_AGS_premiSostegnoBPS.json"));
		return objectMapper.writeValueAsString(response);
	}
}

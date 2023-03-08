package it.tndigitale.a4g.proxy.services.pagopa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import it.tndigitale.a4g.proxy.dto.PagoPaIbanDettaglioDto;
import reactor.core.publisher.Mono;

@Component
public class PagoPaService {
	@Value("${it.tndigit.pagopa.url}")
	private String pagoPaUrl;

	@Value("${it.tndigit.pagopa.user}")
	private String pagoPaUser;

	@Value("${it.tndigit.pagopa.password}")
	private String pagoPaPassword;

	private static final String validateIbanRelativeUrl = "/validateiban";
	private static final String checkIbanRelativeUrl = "/checkIban";
	
	private WebClient buildClient() {
		return WebClient.builder()
				  .baseUrl(pagoPaUrl)
				  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				  .filter(ExchangeFilterFunctions.basicAuthentication(pagoPaUser, pagoPaPassword))// basic auth webclient con spring < 5.1 (https://www.viralpatel.net/basic-authentication-spring-webclient/)
//				  .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth("admin", "password")) // basic auth webclient con spring >= 5.1 (https://www.viralpatel.net/basic-authentication-spring-webclient/)
				  .build();
	}
	
	public boolean validateIban(String iban) {
		WebClient client = buildClient();
		
		ValidateIbanResponse result = client.get()
				  .uri(uriBuilder -> uriBuilder
						    .path(validateIbanRelativeUrl)
						    .queryParam("IBAN", iban)
						    .build())
		        .retrieve().bodyToMono(ValidateIbanResponse.class).block();
		
		return result.getIBANisValid();
	}
	

	
	public PagoPaIbanDettaglioDto checkIbanPersonaFisica(String iban, String codiceFiscale) throws VerificaIbanException {
		WebClient client = buildClient();
		
		CheckIbanPersonaFisicaRequestBody checkIbanPersonaFisicaRequestBody = new CheckIbanPersonaFisicaRequestBody();
		checkIbanPersonaFisicaRequestBody.setCodFiscalePersonaFisica(codiceFiscale);
		checkIbanPersonaFisicaRequestBody.setIban(iban);

		CheckIbanResponse result = client.post()
				.uri(checkIbanRelativeUrl)
				.body(Mono.just(checkIbanPersonaFisicaRequestBody), CheckIbanPersonaFisicaRequestBody.class)  
		        .retrieve()
		        .bodyToMono(CheckIbanResponse.class)
		        .block();
		return PagoPaIbanDettaglioDto.buildFrom(iban, result);
	}
	
	public PagoPaIbanDettaglioDto checkIbanPersonaGiuridicaEPartitaIva(String iban, String codiceFiscale, String partitaIva) throws VerificaIbanException {
		WebClient client = buildClient();
		
		CheckIbanPersonaGiuridicaRequestBody checkIbanPersonaGiuridicaRequestBody = new CheckIbanPersonaGiuridicaRequestBody();
		checkIbanPersonaGiuridicaRequestBody.setCodFiscalePersonaGiuridica(codiceFiscale);
		checkIbanPersonaGiuridicaRequestBody.setIban(iban);
		checkIbanPersonaGiuridicaRequestBody.setCodIVAPersonaGiuridica(partitaIva);
		
		CheckIbanResponse result = client.post()
				.uri(checkIbanRelativeUrl)
				.body(Mono.just(checkIbanPersonaGiuridicaRequestBody), CheckIbanPersonaGiuridicaRequestBody.class)  
		        .retrieve()
		        .bodyToMono(CheckIbanResponse.class)
		        .block();
		return PagoPaIbanDettaglioDto.buildFrom(iban, result);
	}
	
	public PagoPaIbanDettaglioDto checkIbanPartitaIva(String iban, String partitaIVa) throws VerificaIbanException {
		WebClient client = buildClient();
		
		CheckIbanPersonaGiuridicaRequestBody checkIbanPersonaGiuridicaRequestBody = new CheckIbanPersonaGiuridicaRequestBody();
		checkIbanPersonaGiuridicaRequestBody.setIban(iban);
		checkIbanPersonaGiuridicaRequestBody.setCodIVAPersonaGiuridica(partitaIVa);
		
		CheckIbanResponse result = client.post()
				.uri(checkIbanRelativeUrl)
				.body(Mono.just(checkIbanPersonaGiuridicaRequestBody), CheckIbanPersonaGiuridicaRequestBody.class)  
		        .retrieve()
		        .bodyToMono(CheckIbanResponse.class)
		        .block();
		return PagoPaIbanDettaglioDto.buildFrom(iban, result);
	}

	public PagoPaIbanDettaglioDto checkIbanFake(String iban) throws VerificaIbanException {

		PagoPaIbanDettaglioDto pagoPaIbanDettaglioDto = new PagoPaIbanDettaglioDto();
		pagoPaIbanDettaglioDto.setBic("Test - Bic");
		pagoPaIbanDettaglioDto.setCittaFiliale("Test - Città");
		pagoPaIbanDettaglioDto.setDenominazioneFiliale("Test - Denominazione filiale");
		pagoPaIbanDettaglioDto.setDenominazioneIstituto("Test - Denominazione istituto");
		pagoPaIbanDettaglioDto.setIban(iban);

		return pagoPaIbanDettaglioDto;
	}
}

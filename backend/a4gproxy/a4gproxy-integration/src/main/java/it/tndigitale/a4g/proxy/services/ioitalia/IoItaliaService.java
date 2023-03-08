package it.tndigitale.a4g.proxy.services.ioitalia;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.tndigit.iotrentino.it.tndigit.iot.service.dto.MessageDTO;
import it.tndigit.iotrentino.web.rest.GestioneMessaggiApi;
import it.tndigitale.a4g.proxy.dto.ioitalia.ComunicationDto;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import reactor.core.publisher.Mono;

@Component
public class IoItaliaService {
	public static final String VALUE_GRANT_TYPE = "client_credentials";
	@Value("${it.tndigit.iotrentino.oauth.url}")
	private String urlOauth;

	@Value("${it.tndigit.iotrentino.service.url}")
	private String urlIoTrentino;

	@Value("${it.tndigit.iotrentino.oauth.clientId}")
	private String clientId;

	@Value("${it.tndigit.iotrentino.oauth.clientSecret}")
	private String clientSecret;

	@Value("${it.tndigit.iotrentino.enable}")
	private boolean isIoItaliaEnabled;

	@Value("${it.tndigit.iotrentino.whitelist}")
	private String whiteList;

	private static final String KEY_CLIENT_ID = "client_id";

	private static final String KEY_CLIENT_SECRET = "client_secret";

	private static final String KEY_GRANT_TYPE = "grant_type";

	private static final String OAUTH_SERVICE = "/oauth/token";

	private static final Logger log = LoggerFactory.getLogger(IoItaliaService.class);

	@Autowired
	private GestioneMessaggiApi messaggiApi;

	@Async
	public String sendComunication(ComunicationDto comunication) {
		if (!isIoItaliaEnabled)
			return null;

		if (whiteList != null && whiteList.length() > 0) {
			Arrays.asList(whiteList.split("\\s*,\\s*"))
			.stream().filter(cf -> comunication.getCodiceFiscale().equals(cf))
			.findAny()
			.orElseThrow(RuntimeException::new);
		}

		buildClient();

		// Invio il messaggio
		MessageDTO message = new MessageDTO();
		// Tra 10 e 120
		message.setOggetto(comunication.getOggetto());
		// Non serve settare codice fiscale in MessageDTO
		// 80 e 10.000
		message.setTesto(comunication.getMessaggio());
		// 3660 secondi e dopo scade il messaggio -->
		// Per quanto tempo deve restare attivo nel sistema IO Italia il messaggio prima di rimuovere il messaggo
		// Ovvero tentativi di IO Italia
		// 3600 (1 ora) fino 604800 (7 giorni)
		message.setTimeToLive(
				comunication.getTimeToLive() == null 		 ?
						ComunicationDto.DEFAULT_TIME_TO_LIVE :
							comunication.getTimeToLive()
				);

		// se valorizzato inserisci una scadenza "scadenza": "2020-08-15T12:47:41.736Z"
		if (comunication.getScadenza() != null) {
			message.setScadenza(DateFormatUtils.localDateTimeToStringIoItaliaPattern(comunication.getScadenza()));
		}

		// Cripta messaggio nel db di IO Trentino
		message.setTipoCryptoMessage(MessageDTO.TipoCryptoMessageEnum.NO_CRYPTO);
		message.setTipoMessage(MessageDTO.TipoMessageEnum.IO_ITALIA);

		MessageDTO response = messaggiApi.createMessageUsingPOST(message, comunication.getCodiceFiscale());

		log.info("Messaggio inviato con id {} e codice identificativo {} per il codice fiscale {}",
				response.getIdObj(), response.getCodiceIdentificativo(), response.getCodiceFiscale());

		return response.getCodiceIdentificativo();
	}

	private void buildClient() {
		messaggiApi.getApiClient().setBasePath(urlIoTrentino);
		messaggiApi.getApiClient().setApiKey(
				getAccessInformation().getToken_type() + " " + getAccessInformation().getAccess_token());
	}

	private AccessInformation getAccessInformation() {
		WebClient webClient = WebClient.builder()
				.baseUrl(urlOauth)
				.build();

		Mono<AccessInformation> result = webClient.post().uri(uriBuilder -> uriBuilder
				.path(OAUTH_SERVICE)
				.queryParam(KEY_CLIENT_ID, clientId)
				.queryParam(KEY_CLIENT_SECRET, clientSecret)
				.queryParam(KEY_GRANT_TYPE, VALUE_GRANT_TYPE)
				.build())
				.retrieve()
				.bodyToMono(AccessInformation.class);
		return result.block();
	}

	private static class AccessInformation implements Serializable {
		// Spacchettare token: https://jwt.io/
		private String access_token;
		private String token_type;
		private Integer expires_in;
		private String scope;
		private String jti;

		private static final long serialVersionUID = 6366382681843824653L;

		public String getAccess_token() {
			return access_token;
		}

		public AccessInformation setAccess_token(String access_token) {
			this.access_token = access_token;
			return this;
		}

		public String getToken_type() {
			return token_type;
		}

		public AccessInformation setToken_type(String token_type) {
			this.token_type = token_type;
			return this;
		}

		public Integer getExpires_in() {
			return expires_in;
		}

		public AccessInformation setExpires_in(Integer expires_in) {
			this.expires_in = expires_in;
			return this;
		}

		public String getScope() {
			return scope;
		}

		public AccessInformation setScope(String scope) {
			this.scope = scope;
			return this;
		}

		public String getJti() {
			return jti;
		}

		public AccessInformation setJti(String jti) {
			this.jti = jti;
			return this;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			AccessInformation that = (AccessInformation) o;
			return Objects.equals(access_token, that.access_token) &&
					Objects.equals(token_type, that.token_type) &&
					Objects.equals(expires_in, that.expires_in) &&
					Objects.equals(scope, that.scope) &&
					Objects.equals(jti, that.jti);
		}

		@Override
		public int hashCode() {
			return Objects.hash(access_token, token_type, expires_in, scope, jti);
		}
	}
}

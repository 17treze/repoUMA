package it.tndigitale.a4g.proxy.bdn.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.istruttoria.client.api.DomandeRestControllerApi;
import it.tndigitale.a4g.istruttoria.client.model.CuaaDenominazione;

@Component
public class A4gIstruttoriaClient extends AbstractClient {

	@Value("${a4gproxy-bdn.client.a4gistruttoria.uri}")
	private String urlIstruttoria;

	@Autowired
	private ObjectMapper objectMapper;

	public List<String> getElencoPascoli(Integer campagna, String cuaa) {
		return this.getDomandeRestControllerApi().getElencoPascoliUsingGET(campagna, cuaa);
	}
	public List<String> getElencoPascoli(Integer campagna) {
		return this.getDomandeRestControllerApi().getElencoPascoliUsingGET(campagna, null);
	}
	public List<CuaaDenominazione> getElencoCuaa(Integer campagna) throws JsonProcessingException {
		String params = objectMapper.writeValueAsString(new GetElencoCuaaParams().setCampagna(campagna));
		return this.getDomandeRestControllerApi().getCuaaDomandeCampagnaUsingGET(params);
	}

	private DomandeRestControllerApi getDomandeRestControllerApi() {
		return restClient(DomandeRestControllerApi.class, urlIstruttoria);
	}


	private class GetElencoCuaaParams {
		private Integer campagna;

		public Integer getCampagna() {
			return campagna;
		}

		public GetElencoCuaaParams setCampagna(Integer campagna) {
			this.campagna = campagna;
			return this;
		}

	}
}

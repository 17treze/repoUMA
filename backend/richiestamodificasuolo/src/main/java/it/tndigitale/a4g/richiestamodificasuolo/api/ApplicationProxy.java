package it.tndigitale.a4g.richiestamodificasuolo.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.Call;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ApiUrls.APPLICATION_PROXY)
@Api(value = "Controller per la redirezione di richieste verso sistemi esterni")
public class ApplicationProxy {

        private final static Duration TIMEOUT = Duration.ofSeconds(60);
    
	private static final Logger log = LoggerFactory.getLogger(ApplicationProxy.class);
	
	private OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .connectTimeout(TIMEOUT)
            .readTimeout(TIMEOUT)
            .writeTimeout(TIMEOUT)
            .build();

	@Value("${server.servlet.context-path}")
	private String appContextPath;

	@Value("${it.tndigit.geoserver.protocol}")
	private String geoserverProtocol;

	@Value("${it.tndigit.geoserver.host}")
	private String geoserverHost;

	@Value("${it.tndigit.geoserver.port}")
	private int geoserverPort;

	@Value("${it.tndigit.geoserver.stem.username}")
	private String geoserverStemUsername;

	@Value("${it.tndigit.geoserver.stem.password}")
	private String geoserverStemPassword;

	@Value("${it.tndigit.geoserver.stem.workspace}")
	private String geoserverStemWorkspace;
	
	@Value("${it.tndigit.geoserver.app_a4s.username}")
	private String geoserverAppA4sUsername;

	@Value("${it.tndigit.geoserver.app_a4s.password}")
	private String geoserverAppA4sPassword;

	@Value("${it.tndigit.geoserver.app_a4s.workspace}")
	private String geoserverAppA4sWorkspace;

	@GetMapping("/geoserver/**")
	@ApiOperation("Gestisce e redirige le richieste di tipo GET verso Geoserver")
	public ResponseEntity geoserverGetHandler(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException {

			URI uri = generateUri(request, geoserverProtocol, geoserverHost, geoserverPort);
			Request newRequest = createRequest(request, uri.toURL(), body);
			
			Call call = client.newCall(newRequest);
		    Response newResponse = call.execute();
		    ResponseBody resBody = newResponse.body();
		    ResponseEntity<byte[]> res = new ResponseEntity<byte[]>(resBody.bytes(), generateResponseHeaders(newResponse), HttpStatus.SC_OK);
		    return res; 
		
	}

	@PostMapping("/geoserver/**")
	@ApiOperation("Gestisce e redirige le richieste di tipo POST verso Geoserver")
	public ResponseEntity geoserverPostHandler(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException {

		URI uri = generateUri(request, geoserverProtocol, geoserverHost, geoserverPort);
		Request newRequest = createRequest(request, uri.toURL(), body);
		
		Call call = client.newCall(newRequest);
	    Response newResponse = call.execute();
	    ResponseBody resBody = newResponse.body();
	    ResponseEntity<byte[]> res = new ResponseEntity<byte[]>(resBody.bytes(), generateResponseHeaders(newResponse), HttpStatus.SC_OK);
	    return res;
	}

	@GetMapping("/**")
	@ApiOperation("Gestisce e redirige le richieste di tipo GET verso Geoserver")
	public ResponseEntity proxyGetHandler(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException {

			String uri = request.getQueryString();
			Request newRequest = createRequest(request, new URL(uri), body);
			
			Call call = client.newCall(newRequest);
		    Response newResponse = call.execute();
		    ResponseBody resBody = newResponse.body();
		    ResponseEntity<byte[]> res = new ResponseEntity<byte[]>(resBody.bytes(), generateResponseHeaders(newResponse), HttpStatus.SC_OK);
		    return res; 
		
	}
	
	private URI generateUri(HttpServletRequest request, String protocol, String host, int port) throws URISyntaxException {

		String requestUrl = request.getRequestURI().replace(appContextPath + ApiUrls.APPLICATION_PROXY, "");
		requestUrl = requestUrl.replace("dipendente", "").replace("cittadino", "");

		URI uri = new URI(protocol, null, host, port, null, null, null);
		uri = UriComponentsBuilder.fromUri(uri).path(requestUrl).query(request.getQueryString()).build(true).toUri();

		return uri;
	}

	private Request createRequest(HttpServletRequest request, URL url, String body) {
		
		Request.Builder reqBuilder = new Request.Builder();
		reqBuilder.url(url);
		
		if (body != null) {
			okhttp3.RequestBody reqBody = okhttp3.RequestBody.create(MediaType.parse("application/json"), body);
			reqBuilder.post(reqBody);
		}
		
		if (request.getRequestURI().contains(geoserverStemWorkspace) || (request.getQueryString() != null && request.getQueryString().contains(geoserverStemWorkspace))) {
			// Set Authentication header
			String credential = Credentials.basic(geoserverStemUsername, geoserverStemPassword);
			reqBuilder.header("Authorization", credential);
		} else if(request.getRequestURI().contains(geoserverAppA4sWorkspace) || (request.getQueryString() != null && request.getQueryString().contains(geoserverAppA4sWorkspace))) {
			if (!geoserverAppA4sUsername.equals("none")) {
				String credential = Credentials.basic(geoserverAppA4sUsername, geoserverAppA4sPassword);
				reqBuilder.header("Authorization", credential);
			}
		}
		
		return reqBuilder.build();
		
	}

	private HttpHeaders generateResponseHeaders(Response res) {
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.setConnection(res.header("Connection"));
		responseHeader.add("Content-Type", res.header("Content-Type"));
		return responseHeader;
	}
}

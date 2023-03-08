package it.tndigitale.a4g.proxy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.tndigitale.a4g.proxy.dto.ioitalia.ComunicationDto;
import it.tndigitale.a4g.proxy.services.ioitalia.IoItaliaService;

@RestController
//@RequestMapping("/api/v1/ioitalia")
@RequestMapping(ApiUrls.IOITALIA)
@Api(value = "Interfaccia per la comunicazione con IO Italia")
public class IoItaliaController {
	
	@Autowired
	private IoItaliaService ioItaliaService;
	
	@ApiOperation("Invia messaggio ai servizi di IO Italia")
	@PostMapping("/invia-messaggio")
	public String inviaMessaggio(@RequestBody ComunicationDto comunication) {
		return ioItaliaService.sendComunication(comunication);
	}

}

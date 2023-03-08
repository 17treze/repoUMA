package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.config.FtpClientConfiguration;

@Component
public class InvioTracciatoSOCComponent {

	@Autowired
	private FtpClientConfiguration ftpClient;
	
	@Autowired
	private A4gistruttoriaConfigurazione configurazione;
	
	void inviaTracciato(String tracciato, String nomeFile) throws IOException {
		try {
			ftpClient.open(configurazione.getUriFtpSoc(), configurazione.getPortFtpSoc(),
					configurazione.getUserFtpSoc(), configurazione.getPasswordFtpSoc());

			String remotePathElenco = configurazione.getDirectoryFtpSoc().concat(nomeFile.toString());
			ftpClient.uploadFile(new ByteArrayInputStream(tracciato.getBytes()), remotePathElenco);
		} finally {
			ftpClient.close();
		}
		
	}
}

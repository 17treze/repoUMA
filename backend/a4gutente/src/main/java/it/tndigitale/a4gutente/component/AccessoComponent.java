package it.tndigitale.a4gutente.component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import javax.security.cert.X509Certificate;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gutente.codici.TipoAutenticazione;
import it.tndigitale.a4gutente.config.Costanti;
import it.tndigitale.a4gutente.dto.DatiAnagrafici;
import it.tndigitale.a4gutente.dto.DatiAutenticazione;

@Component
public class AccessoComponent {

	public DatiAutenticazione caricaDatiAutenticazione(HttpHeaders headers) throws Exception {
		DatiAutenticazione autenticazione = new DatiAutenticazione();
		autenticazione.setDatiAnagrafici(caricaDatiAnagrafici(headers));
		String shibAuthIst = getHeaderProperty(headers, Costanti.HEADER_SHIB_AUTH_INST);
		if (shibAuthIst != null && !shibAuthIst.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			autenticazione.setDataAutenticazione(LocalDateTime.parse(shibAuthIst, formatter));
		}
		TipoAutenticazione tipoAutenticazione = TipoAutenticazione.ofAuthenticationMethod(getHeaderProperty(headers, Costanti.HEADER_SHIB_AUTH_METHOD));
		autenticazione.setTipoAuthenticazione(tipoAutenticazione);
		String x509base64 = getHeaderProperty(headers, Costanti.HEADER_X509);
		if (x509base64 != null && !x509base64.isEmpty()) {
			autenticazione.setDatiCertificato(X509Certificate.getInstance(Base64.getDecoder().decode(x509base64)));
		}
		autenticazione.setCodiceSPID(getHeaderProperty(headers, Costanti.HEADER_SPID_CODE));
		return autenticazione;
	}
	
	public DatiAnagrafici caricaDatiAnagrafici(HttpHeaders headers) throws Exception {
		DatiAnagrafici datiAnagrafici = new DatiAnagrafici();
		datiAnagrafici.setCodiceFiscale(getHeaderProperty(headers, Costanti.HEADER_CF));
		datiAnagrafici.setNome(getHeaderProperty(headers, Costanti.HEADER_NOME));
		datiAnagrafici.setCognome(getHeaderProperty(headers, Costanti.HEADER_COGNOME));
		datiAnagrafici.setEmail(getHeaderProperty(headers, Costanti.HEADER_MAIL));
		return datiAnagrafici;
	}

	protected static String getHeaderProperty(HttpHeaders headers, String nome) {
		List<String> header = headers.get(nome);
		return (header == null || header.isEmpty()) ? null : header.get(0);
	}
	
	public boolean isAutenticazioneForte(DatiAutenticazione datiAutenticazione) {
		TipoAutenticazione tipoAutenticazione = datiAutenticazione.getTipoAuthenticazione();
		return isAutenticazioneSmartCard(tipoAutenticazione) || isAutenticazioneConSpid(datiAutenticazione);
	}
	
	public boolean isAutenticazioneSmartCard(TipoAutenticazione tipoAutenticazione) {
		return TipoAutenticazione.SMARTCARD.equals(tipoAutenticazione);
	}
	
	public boolean isAutenticazioneConSpid(DatiAutenticazione datiAutenticazione) {
		String spidCode = datiAutenticazione.getCodiceSPID();
		return spidCode != null && !spidCode.isEmpty();
	}
}

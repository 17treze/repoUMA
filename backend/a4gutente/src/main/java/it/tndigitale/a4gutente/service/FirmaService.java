package it.tndigitale.a4gutente.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.security.cert.X509Certificate;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gutente.artefatto.Istanzeonline;
import it.tndigitale.a4gutente.component.AccessoComponent;
import it.tndigitale.a4gutente.component.EncryptionComponent;
import it.tndigitale.a4gutente.component.StampaComponent;
import it.tndigitale.a4gutente.dto.DatiAnagrafici;
import it.tndigitale.a4gutente.dto.DatiAutenticazione;
import it.tndigitale.a4gutente.dto.Firma;
import it.tndigitale.a4gutente.exception.UtenteException;

@Service
public class FirmaService implements IFirmaService {

	private static final Logger logger = LoggerFactory.getLogger(FirmaService.class);
	
	@Autowired
	private EncryptionComponent encComponent;

	@Autowired
	private AccessoComponent accessoComponent;

	@Autowired
	private StampaComponent stampaComponent;
	
	@Autowired
	private ObjectMapper objectMapper;

	/* (non-Javadoc)
	 * @see it.tndigitale.a4gutente.service.IFirmaService#firma(java.lang.String, it.tndigitale.a4gutente.dto.DatiAutenticazione, byte[])
	 */
	@Override
	public Firma firma(String modulo, DatiAutenticazione datiAutenticazione, byte[] documento) throws Exception {
		if (!accessoComponent.isAutenticazioneForte(datiAutenticazione)) {
			logger.error("firma: non e' possibile firmare senza una autenticazione forte");
			throw new UtenteException("Per poter firmare è necessario autenticarsi con Carta Provinciale dei Servizi/Carta Nazionale dei Servizi o le credenziali SPID");
		}
        Istanzeonline token = new Istanzeonline();
        Istanzeonline.Serviceprovider sp = getServiceProvider(datiAutenticazione);
        populateTokenAutenticazione(modulo, datiAutenticazione, sp);
        populateHash(sp, documento);
        sp.setValue("A4G");
        token.setServiceprovider(sp);
        Firma firmaResult = new Firma();
        firmaResult.setXml(trasformaXML(token));
        firmaResult.setPdf(stampaPDF(token, documento));
        return firmaResult;
	}
	
	protected byte[] stampaPDF(Istanzeonline token, byte[] documento) throws Exception {
        String jsonToken = objectMapper.writeValueAsString(token);
        return stampaComponent.stampaPDF_A(jsonToken, "template/templateFirmaDatiAccesso.docx");
	}

    protected void populateHash(Istanzeonline.Serviceprovider sp, byte[] documento) {    	
    	sp.setHashdocumento(DigestUtils.sha256Hex(documento));
    	sp.setHashdati(DigestUtils.sha256Hex(documento));
    }
    
    protected Istanzeonline.Serviceprovider getServiceProvider(DatiAutenticazione datiAutenticazione) throws Exception {
        Istanzeonline.Serviceprovider sp = new Istanzeonline.Serviceprovider();
        DatiAnagrafici datiAnagrafici = datiAutenticazione.getDatiAnagrafici();
        if (datiAnagrafici != null) {
            sp.setCodicefiscale(datiAnagrafici.getCodiceFiscale().toUpperCase());
            sp.setCognome(datiAnagrafici.getCognome().toUpperCase());
            sp.setNome(datiAnagrafici.getNome().toUpperCase());
        }
        return sp;
    }
    
    protected void populateTokenAutenticazione(String modulo, DatiAutenticazione datiAutenticazione, Istanzeonline.Serviceprovider sp) throws Exception {
        
        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterOra = DateTimeFormatter.ofPattern("HH:mm");
        SimpleDateFormat formatterCert = new SimpleDateFormat("ddMMyyyy");

        LocalDateTime dataAutenticazione = datiAutenticazione.getDataAutenticazione();
        sp.setModuloId(modulo);
        sp.setDataautenticazione(dataAutenticazione.format(formatterData));
        sp.setOraautenticazione(dataAutenticazione.format(formatterOra));
        X509Certificate certificato = datiAutenticazione.getDatiCertificato();
        if (certificato != null) {
        	sp.setCertificationauthority(certificato.getIssuerDN().getName());
            Date dataRilascioCertificato = certificato.getNotBefore();
            Date dataScadenzaCertificato = certificato.getNotAfter();
            sp.setDatarilasciocertificato(formatterCert.format(dataRilascioCertificato));
            sp.setDatascadenzacertificato(formatterCert.format(dataScadenzaCertificato));
            sp.setHashcertificato(Integer.toString(certificato.hashCode()));
            sp.setNumerocertificato(certificato.getSerialNumber().toString());
        }
        String spidCode = datiAutenticazione.getCodiceSPID();
        if (spidCode != null && !spidCode.isEmpty()) {
            sp.setSpidcode(spidCode);
        }
		sp.setAutenticationmethod((datiAutenticazione.getTipoAuthenticazione() == null ? "N/D"
				: datiAutenticazione.getTipoAuthenticazione().name()));
		
		String sicurezza = 
				encComponent.encrypt(sp.getDataautenticazione() + " " + sp.getOraautenticazione() + ", " + sp.getCodicefiscale());
		sp.setSicurezza(sicurezza);    	
    }
    
    protected String trasformaXML(Istanzeonline token) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Istanzeonline.class.getPackage().getName());
		Marshaller m  = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		QName qName = new QName("", token.getClass().getSimpleName().toLowerCase());
		JAXBElement<Istanzeonline> root = new JAXBElement<Istanzeonline>(qName, Istanzeonline.class, token);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			m.marshal(root, outputStream);
			outputStream.flush();
			return outputStream.toString();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				
				//logger.error("marshall: errore eseguendo la close");
			}			
		}
    }
}

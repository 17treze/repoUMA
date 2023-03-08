package it.tndigitale.a4g.proxy.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.tndigitale.a4g.proxy.dto.InfoVerificaFirma;
import it.tndigitale.ws.verificafirmedigitali.WarningResponseType;

/**
 * Service per la gestione dei WS esposti perc la verifica della firma.
 * 
 * @author S.DeLuca
 *
 */
public interface VerificaFirmaService {

	/**
	 * Verifica la firma del documento in input.
	 * 
	 * @param documentoFirmato
	 *            rappresenta un documento firmato digitalmente
	 * @return {@link WarningResponseType}
	 * @throws Exception
	 */
	public WarningResponseType verificaFirma(MultipartFile documentoFirmato) throws Exception;
	
	/**
	 * Verifica la firma del documento in input e la corrispondenza con il CF del firmatario. 
	 * 
	 * @param documentoFirmato rappresenta un documento firmato digitalmente
	 * @return Data di firma e codice fiscale firmatario
	 * @throws Exception
	 */
	public InfoVerificaFirma verificaFirmaSingola(MultipartFile documentoFirmato, String codiceFiscale) throws Exception;
	
	
	public List<InfoVerificaFirma> verificaFirmaMultipla(MultipartFile documentoFirmato, List<String> codiceFiscaleList) throws IOException, VerificaFirmaException;
}

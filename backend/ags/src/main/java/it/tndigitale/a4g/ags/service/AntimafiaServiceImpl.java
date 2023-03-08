/**
 * 
 */
package it.tndigitale.a4g.ags.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.ags.dto.EsitoAntimafia;
import it.tndigitale.a4g.ags.repository.dao.AntimafiaDao;
import it.tndigitale.a4g.ags.utils.EmailUtils;

/**
 * @author S.DeLuca
 *
 */
@Service
public class AntimafiaServiceImpl implements AntimafiaService {

	@Autowired
	private AntimafiaDao antimafiaDao;
	@Autowired
	private EmailUtils emailUtils;
	@Value("${a4g.mail.to}")
	private String mailTo;
	@Value("${a4g.mail.oggetto}")
	private String oggetto;
	@Value("${a4g.mail.messaggio}")
	private String messaggio;		

	@Override
	public void salva(List<EsitoAntimafia> esitiAntimafia) throws Exception {
		esitiAntimafia.forEach(esitoAntimafia -> {
			if ("DOMANDA_UNICA".equals(esitoAntimafia.getTipoDomanda())) {
				esitoAntimafia.setTipoDomanda("0");
			} else if ("PSR_SUPERFICIE_EU".equals(esitoAntimafia.getTipoDomanda())) {
				esitoAntimafia.setTipoDomanda("1");
			} else if ("PSR_STRUTTURALI_EU".equals(esitoAntimafia.getTipoDomanda())) {
				esitoAntimafia.setTipoDomanda("2");
			} else {
				return;
			}
		});
		// DECODE('DOMANDA_UNICA',0; 'PSR_SUPERFICIE_EU',1,'PSR_STRUTTURALI_EU',2')
		try {
			antimafiaDao.salva(esitiAntimafia);
		} catch (Exception ex) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw, true);
			ex.printStackTrace(pw);
			String object = MessageFormat.format(oggetto, String.join(" - ", esitiAntimafia.stream().map(esito -> esito.getCuaa()).collect(Collectors.toList())),
					String.join(" - ", esitiAntimafia.stream().map(esito -> esito.getIdDomanda().toString()).collect(Collectors.toList())),
					String.join(" - ", esitiAntimafia.stream().map(esito -> esito.getTipoDomanda()).collect(Collectors.toList())));
			emailUtils.sendSimpleMessage(mailTo, 
					object,		
					MessageFormat.format(messaggio, sw.getBuffer().toString()));	
			throw ex;
		}
	}

	@Override
	public void cancella(EsitoAntimafia esitoAntimafia) throws Exception {
		antimafiaDao.cancella(esitoAntimafia.getCuaa());
	}

	@Override
	public List<EsitoAntimafia> recuperaEsiti(EsitoAntimafia esitoAntimafia) throws Exception {
		return antimafiaDao.recuperaEsiti(esitoAntimafia);
	}

	@Override
	public void sincronizzaCert(List<String> cuaaList) throws Exception {
		//TASK - SINCRONIZZAZIONE CERTIFICAZIONI ANTIMAFIA CON AGS
		antimafiaDao.sincronizzaCert(cuaaList);
	}

}

package it.tndigitale.a4g.proxy.services;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.infotn.winward.client.OutputFormat;
import it.infotn.winward.client.ReportClient;
import it.infotn.winward.client.ReportClientBuilder.DataSourceType;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StampaServiceImpl implements StampaService {

	@Value("${report.windward.uri}")
	private String windwardUri;

	private static final Logger logger = LoggerFactory.getLogger(StampaServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.tndigitale.a4gproxy.services.IStampaService#stampaXML2PDF(java.lang.String, java.io.InputStream)
	 */
	@Override
	public byte[] stampaXML2PDF(String inputData, InputStream template) throws Exception {
		return stampa(inputData, template, OutputFormat.PDF, DataSourceType.XML);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.tndigitale.a4gproxy.services.IStampaService#stampaJSON2PDF(java.lang.String, java.io.InputStream)
	 */
	@Override
	public byte[] stampaJSON2PDF(String inputData, InputStream template) throws Exception {
		return stampa(inputData, template, OutputFormat.PDF, DataSourceType.JSON);
	}

	@Override
	public byte[] stampaXML2PDFA(String inputData, InputStream template) throws Exception {
		return stampa(inputData, template, OutputFormat.PDF_A, DataSourceType.XML);
	}

	@Override
	public byte[] stampaJSON2PDFA(String inputData, InputStream template) throws Exception {
		return stampa(inputData, template, OutputFormat.PDF_A, DataSourceType.JSON);
	}

	@Override
	public byte[] stampaJSON2DOCX(String inputData, InputStream template) throws Exception {
		return stampa(inputData, template, OutputFormat.DOCX, DataSourceType.JSON);
	}

	protected byte[] stampa(String inputData, InputStream template, OutputFormat outputType, DataSourceType dsType) throws Exception {
		logger.info("Chiamo il server di stampa dsType : {} outputType {}", dsType, outputType);
		if (inputData != null)
			logger.info("Dimensione dati di input {}", inputData.length());
		ReportClient rc = ReportClient.builderInstance().withBaseServerUrl(windwardUri).withTemplate(template).withOutputFormat(outputType).withDataSource(inputData.getBytes(), dsType).build();
		rc.setTimeoutInMillis(900000);
		byte[] pdf = rc.generate().orElseThrow((e) -> {
			logger.error("Errore in fase di stampa dsType : {} outputType {}", dsType, outputType, e);
			return new RuntimeException(e);
		});
		logger.info("Termine stampa dsType : {} outputType {}", dsType, outputType);
		return pdf;
	}
}

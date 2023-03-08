package it.tndigitale.a4gistruttoria;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "a4gistruttoria")
public class A4gistruttoriaConfigurazione {

	@Value("${a4gistruttoria.ags.uri}")
	private String uriAgs;

	@Value("${a4gistruttoria.proxy.uri}")
	private String uriProxy;

	@Value("${a4gistruttoria.a4gfascicolo.uri}")
	private String uriFascicolo;

	@Value("${a4gistruttoria.uri}")
	private String uriIstruttoria;

	@Value("${a4gistruttoria.cachebdn.uri}")
	private String uriCacheBdn;

	@Value("${a4gistruttoria.ftp.soc.uri}")
	private String uriFtpSoc;

	@Value("${a4gistruttoria.ftp.soc.port}")
	private int portFtpSoc;

	@Value("${a4gistruttoria.ftp.soc.user}")
	private String userFtpSoc;

	@Value("${a4gistruttoria.ftp.soc.password}")
	private String passwordFtpSoc;

	@Value("${a4gistruttoria.ftp.soc.directory}")
	private String directoryFtpSoc;
	
    @Value("${a4gistruttoria.istruttoriadu.liquidazione.mail.to}")
	private String destinatarioMailLiquidazione;
       	
    
    public String getUriFtpSoc() {
		return uriFtpSoc;
	}

	public int getPortFtpSoc() {
		return portFtpSoc;
	}

	public String getUserFtpSoc() {
		return userFtpSoc;
	}

	public String getPasswordFtpSoc() {
		return passwordFtpSoc;
	}

	public String getUriAgs() {
		return uriAgs;
	}

	public String getUriProxy() {
		return uriProxy;
	}

	public String getUriFascicolo() {
		return uriFascicolo;
	}

	public String getUriIstruttoria() {
		return uriIstruttoria;
	}

	public String getUriCacheBdn() {
		return uriCacheBdn;
	}

	public String getDirectoryFtpSoc() {
		return directoryFtpSoc;
	}

	public String getDestinatarioMailLiquidazione() {
		return destinatarioMailLiquidazione;
	}
}

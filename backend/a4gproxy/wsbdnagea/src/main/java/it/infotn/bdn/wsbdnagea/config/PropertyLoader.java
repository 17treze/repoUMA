package it.infotn.bdn.wsbdnagea.config;

import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

	private static PropertyLoader instance = null;
	private Properties properties;

	protected PropertyLoader() throws Exception {

		properties = new Properties();

		InputStream in = getClass().getResourceAsStream("/application.properties");
		if (in == null) {
			throw new Exception("file application.properties non trovato");
		}
		properties.load(in);

		String activeProfile = properties.getProperty("spring.profiles.active");
		if (activeProfile != null && !activeProfile.equalsIgnoreCase("@activatedProperties@")) {
			StringBuilder sb = new StringBuilder();
			sb.append("/application-").append(activeProfile).append(".properties");
			InputStream inputStream = getClass().getResourceAsStream(sb.toString());
			if (inputStream == null)
				throw new Exception("file " + sb.toString() + " non trovato");

			properties.load(inputStream);
		}
	}

	public static PropertyLoader getInstance() {
		if (instance == null) {
			try {
				instance = new PropertyLoader();
			} catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
		return instance;
	}

	public String getValue(String key) {
		return properties.getProperty(key);
	}

}

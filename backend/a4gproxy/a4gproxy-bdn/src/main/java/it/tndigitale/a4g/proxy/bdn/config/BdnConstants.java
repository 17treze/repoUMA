package it.tndigitale.a4g.proxy.bdn.config;

public enum BdnConstants {

	SYNC_OK("SUCCESS"), SYNC_KO("FAIL"), SYNC_RETRY("RETRY");
	private String bdnConstants;

	BdnConstants(String bdnConstants) {
		this.bdnConstants = bdnConstants;
	}

	public String getBdnConstants() {
		return bdnConstants;
	}

}

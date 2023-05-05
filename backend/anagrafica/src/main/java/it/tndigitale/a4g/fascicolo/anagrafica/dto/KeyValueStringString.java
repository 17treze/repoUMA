package it.tndigitale.a4g.fascicolo.anagrafica.dto;

public class KeyValueStringString {
	public KeyValueStringString(String key, String value) {
		super();
		this.mkey = key;
		this.mvalue = value;
	}

	String mkey;
	String mvalue;

	public String getMkey() {
		return mkey;
	}

	public void setMkey(String key) {
		this.mkey = key;
	}

	public String getMvalue() {
		return mvalue;
	}

	public void setMvalue(String value) {
		this.mvalue = value;
	}



}

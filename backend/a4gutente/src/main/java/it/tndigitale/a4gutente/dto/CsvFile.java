package it.tndigitale.a4gutente.dto;

import java.io.Serializable;

public class CsvFile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4655509903649797538L;
	private byte[] csvByteArray;
	private String csvFileName;
	
	
	public byte[] getCsvByteArray() {
		return csvByteArray;
	}
	public void setCsvByteArray(byte[] csvByteArray) {
		this.csvByteArray = csvByteArray;
	}
	public String getCsvFileName() {
		return csvFileName;
	}
	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}

}

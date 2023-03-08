package it.tndigitale.a4g.proxy.services;

import java.io.InputStream;

public interface StampaService {

	byte[] stampaXML2PDF(String inputData, InputStream template) throws Exception;

	byte[] stampaJSON2PDF(String inputData, InputStream template) throws Exception;

	byte[] stampaXML2PDFA(String inputData, InputStream template) throws Exception;

	byte[] stampaJSON2PDFA(String inputData, InputStream template) throws Exception;

	byte[] stampaJSON2DOCX(String inputData, InputStream template) throws Exception;
}
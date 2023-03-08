package it.tndigitale.a4g.fascicolo.anagrafica.business.service.client;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.client.DefaultUrlMicroService;
import it.tndigitale.a4g.proxy.client.api.AnagrafeTributariaControllerApi;
import it.tndigitale.a4g.proxy.client.api.AnagraficaImpresaControllerApi;
import it.tndigitale.a4g.proxy.client.api.PagoPaControllerApi;
import it.tndigitale.a4g.proxy.client.api.ProtocolloControllerApi;
import it.tndigitale.a4g.proxy.client.api.SianControllerApi;
import it.tndigitale.a4g.proxy.client.api.VerificaFirmaControllerApi;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian;
import it.tndigitale.a4g.proxy.client.model.InfoVerificaFirma;
import it.tndigitale.a4g.proxy.client.model.PagoPaIbanDettaglioDto;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.client.model.PersonaGiuridicaDto;
import it.tndigitale.a4g.proxy.client.model.WarningResponseType;

@Component
public class AnagraficaProxyClient extends AbstractClient {

	@Value(DefaultUrlMicroService.PROXY_URL)
	private String serverProxyPath;
	
	public PagoPaIbanDettaglioDto verificaIbanPersonaFisica(String cuaa, String iban) {
		return getPagoPaControllerApi().checkIbanPersonaFisicaUsingPOST(cuaa, iban);
	}
	
	public PagoPaIbanDettaglioDto checkIbanFake(String iban) {
		return getPagoPaControllerApi().checkIbanFakeUsingPOST(iban);
	}

	public PagoPaIbanDettaglioDto verificaIbanPersonaGiuridicaEPartitaIva(String cuaa, String iban, String partitaIva) {
		return getPagoPaControllerApi().checkIbanPersonaGiuridicaEPartitaIvaUsingPOST(cuaa, iban, partitaIva);
	}
	
	public PagoPaIbanDettaglioDto verificaIbanPartitaIva(String partitaIva, String iban) {
		return getPagoPaControllerApi().checkIbanPartitaIvaUsingPOST(partitaIva, iban);
	}
	
	public FascicoloSian verificaEsistenzaFascicolo(String cuaa) {
		return getSianControllerApi().verificaEsistenzaFascicoloUsingGET(cuaa);
	}

	public WarningResponseType verificaFirma(File contratto) {
		return this.getVerificaFirmaControllerApi().verificaFirmaUsingPOST(contratto);
	}
	
	public InfoVerificaFirma verificaFirmaSingola(File contratto, String codiceFiscaleRappresentante) {
		return this.getVerificaFirmaControllerApi().verificaFirmaSingolaUsingPOST(contratto, codiceFiscaleRappresentante);
	}
	
	public List<InfoVerificaFirma> verificaFirmaMultipla(File contratto, List<String> codiceFiscaleList) {
		return this.getVerificaFirmaControllerApi().verificaFirmaMultiplaUsingPOST(contratto, codiceFiscaleList);
	}

	public String protocollaMandato(String metadati, File mandato, List<File> allegati) {
		return getProtocolloControllerApi().createIncomingDocumentRESTUsingPOST(metadati, mandato, allegati);
	}

	// Anagrafe tributaria
	public PersonaGiuridicaDto getPersonaGiuridicaAnagrafeTributaria(final String codiceFiscale) {
		return getAnagrafeTributariaControllerApi().getPersonaGiuridicaUsingGET(codiceFiscale);
	}

	public PersonaFisicaDto getPersonaFisicaAnagrafeTributaria(String codiceFiscale) {
		return getAnagrafeTributariaControllerApi().getPersonaFisicaUsingGET(codiceFiscale);
	}

	// PARIX
	public PersonaGiuridicaDto getPersonaGiuridicaAnagraficaImpresa(String codiceFiscale, String provinciaIscrizione) {
		return getAnagraficaImpresaControllerApi().getPersonaGiuridicaUsingGET1(codiceFiscale, provinciaIscrizione);
	}

	public PersonaFisicaDto getPersonaFisicaAnagraficaImpresa(String codiceFiscale, String provinciaIscrizione) {
		return getAnagraficaImpresaControllerApi().getPersonaFisicaUsingGET1(codiceFiscale, provinciaIscrizione);
	}

	private AnagrafeTributariaControllerApi getAnagrafeTributariaControllerApi() {
		return restClientProxy(AnagrafeTributariaControllerApi.class, serverProxyPath);
	}

	private AnagraficaImpresaControllerApi getAnagraficaImpresaControllerApi() {
		return restClientProxy(AnagraficaImpresaControllerApi.class, serverProxyPath);
	}

	private SianControllerApi getSianControllerApi() {
		return restClientProxy(SianControllerApi.class, serverProxyPath);
	}

	private VerificaFirmaControllerApi getVerificaFirmaControllerApi() {
		return restClientProxy(VerificaFirmaControllerApi.class, serverProxyPath);
	}

	private ProtocolloControllerApi getProtocolloControllerApi() {
		return restClientProxy(ProtocolloControllerApi.class, serverProxyPath);
	}
	
	private PagoPaControllerApi getPagoPaControllerApi() {
		return restClientProxy(PagoPaControllerApi.class, serverProxyPath);
	}
}

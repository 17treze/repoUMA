package it.infotn.agn.bdn.wsbdndu;

import it.infotn.bdn.wsbdndu.wsclient.DomandaUnicaService;
import it.izs.wsdl.wsBDNDomandaUnica.ClsPremioValidazioneResponse;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
		DomandaUnicaService service = new DomandaUnicaService();
		
		
		ClsPremioValidazioneResponse response = service.getElencoCapiPremio("DPRGTN57P17E850D", "311", 2017);
		
		System.out.println(response);

        System.out.println( "Hello World!" );
    }
}

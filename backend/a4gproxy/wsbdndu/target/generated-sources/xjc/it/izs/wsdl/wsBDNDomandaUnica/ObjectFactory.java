//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:19 PM CEST 
//


package it.izs.wsdl.wsBDNDomandaUnica;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.izs.wsdl.wsBDNDomandaUnica package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SOAPAutenticazione_QNAME = new QName("http://bdr.izs.it/webservices", "SOAPAutenticazione");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.izs.wsdl.wsBDNDomandaUnica
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetElencoCapiPremio }
     * 
     */
    public GetElencoCapiPremio createGetElencoCapiPremio() {
        return new GetElencoCapiPremio();
    }

    /**
     * Create an instance of {@link ClsPremioValidazione }
     * 
     */
    public ClsPremioValidazione createClsPremioValidazione() {
        return new ClsPremioValidazione();
    }

    /**
     * Create an instance of {@link GetElencoCapiPremioResponse }
     * 
     */
    public GetElencoCapiPremioResponse createGetElencoCapiPremioResponse() {
        return new GetElencoCapiPremioResponse();
    }

    /**
     * Create an instance of {@link ClsPremioValidazioneResponse }
     * 
     */
    public ClsPremioValidazioneResponse createClsPremioValidazioneResponse() {
        return new ClsPremioValidazioneResponse();
    }

    /**
     * Create an instance of {@link SOAPAutenticazione }
     * 
     */
    public SOAPAutenticazione createSOAPAutenticazione() {
        return new SOAPAutenticazione();
    }

    /**
     * Create an instance of {@link GetElencoCapiPremio2 }
     * 
     */
    public GetElencoCapiPremio2 createGetElencoCapiPremio2() {
        return new GetElencoCapiPremio2();
    }

    /**
     * Create an instance of {@link ClsPremioValidazionePP }
     * 
     */
    public ClsPremioValidazionePP createClsPremioValidazionePP() {
        return new ClsPremioValidazionePP();
    }

    /**
     * Create an instance of {@link GetElencoCapiPremio2Response }
     * 
     */
    public GetElencoCapiPremio2Response createGetElencoCapiPremio2Response() {
        return new GetElencoCapiPremio2Response();
    }

    /**
     * Create an instance of {@link ArrayOfClsCapo }
     * 
     */
    public ArrayOfClsCapo createArrayOfClsCapo() {
        return new ArrayOfClsCapo();
    }

    /**
     * Create an instance of {@link ClsCapo }
     * 
     */
    public ClsCapo createClsCapo() {
        return new ClsCapo();
    }

    /**
     * Create an instance of {@link ArrayOfClsCapoMacellato }
     * 
     */
    public ArrayOfClsCapoMacellato createArrayOfClsCapoMacellato() {
        return new ArrayOfClsCapoMacellato();
    }

    /**
     * Create an instance of {@link ClsCapoMacellato }
     * 
     */
    public ClsCapoMacellato createClsCapoMacellato() {
        return new ClsCapoMacellato();
    }

    /**
     * Create an instance of {@link ArrayOfClsCapoOvicaprino }
     * 
     */
    public ArrayOfClsCapoOvicaprino createArrayOfClsCapoOvicaprino() {
        return new ArrayOfClsCapoOvicaprino();
    }

    /**
     * Create an instance of {@link ClsCapoOvicaprino }
     * 
     */
    public ClsCapoOvicaprino createClsCapoOvicaprino() {
        return new ClsCapoOvicaprino();
    }

    /**
     * Create an instance of {@link ArrayOfClsCapoVacca }
     * 
     */
    public ArrayOfClsCapoVacca createArrayOfClsCapoVacca() {
        return new ArrayOfClsCapoVacca();
    }

    /**
     * Create an instance of {@link ClsCapoVacca }
     * 
     */
    public ClsCapoVacca createClsCapoVacca() {
        return new ClsCapoVacca();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SOAPAutenticazione }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SOAPAutenticazione }{@code >}
     */
    @XmlElementDecl(namespace = "http://bdr.izs.it/webservices", name = "SOAPAutenticazione")
    public JAXBElement<SOAPAutenticazione> createSOAPAutenticazione(SOAPAutenticazione value) {
        return new JAXBElement<SOAPAutenticazione>(_SOAPAutenticazione_QNAME, SOAPAutenticazione.class, null, value);
    }

}

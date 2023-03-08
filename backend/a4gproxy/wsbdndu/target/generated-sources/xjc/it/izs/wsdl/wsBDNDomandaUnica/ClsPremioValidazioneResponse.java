//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:19 PM CEST 
//


package it.izs.wsdl.wsBDNDomandaUnica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per clsPremio_ValidazioneResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="clsPremio_ValidazioneResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://bdr.izs.it/webservices}clsResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Numero_Capi" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Lista_Capi" type="{http://bdr.izs.it/webservices}ArrayOfClsCapo" minOccurs="0"/&gt;
 *         &lt;element name="Lista_Capi_Macellati" type="{http://bdr.izs.it/webservices}ArrayOfClsCapoMacellato" minOccurs="0"/&gt;
 *         &lt;element name="Lista_CapiOvicaprini" type="{http://bdr.izs.it/webservices}ArrayOfClsCapoOvicaprino" minOccurs="0"/&gt;
 *         &lt;element name="Lista_Vacche" type="{http://bdr.izs.it/webservices}ArrayOfClsCapoVacca" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsPremio_ValidazioneResponse", propOrder = {
    "numeroCapi",
    "listaCapi",
    "listaCapiMacellati",
    "listaCapiOvicaprini",
    "listaVacche"
})
public class ClsPremioValidazioneResponse
    extends ClsResponse
{

    @XmlElement(name = "Numero_Capi")
    protected long numeroCapi;
    @XmlElement(name = "Lista_Capi")
    protected ArrayOfClsCapo listaCapi;
    @XmlElement(name = "Lista_Capi_Macellati")
    protected ArrayOfClsCapoMacellato listaCapiMacellati;
    @XmlElement(name = "Lista_CapiOvicaprini")
    protected ArrayOfClsCapoOvicaprino listaCapiOvicaprini;
    @XmlElement(name = "Lista_Vacche")
    protected ArrayOfClsCapoVacca listaVacche;

    /**
     * Recupera il valore della proprietà numeroCapi.
     * 
     */
    public long getNumeroCapi() {
        return numeroCapi;
    }

    /**
     * Imposta il valore della proprietà numeroCapi.
     * 
     */
    public void setNumeroCapi(long value) {
        this.numeroCapi = value;
    }

    /**
     * Recupera il valore della proprietà listaCapi.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfClsCapo }
     *     
     */
    public ArrayOfClsCapo getListaCapi() {
        return listaCapi;
    }

    /**
     * Imposta il valore della proprietà listaCapi.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfClsCapo }
     *     
     */
    public void setListaCapi(ArrayOfClsCapo value) {
        this.listaCapi = value;
    }

    /**
     * Recupera il valore della proprietà listaCapiMacellati.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfClsCapoMacellato }
     *     
     */
    public ArrayOfClsCapoMacellato getListaCapiMacellati() {
        return listaCapiMacellati;
    }

    /**
     * Imposta il valore della proprietà listaCapiMacellati.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfClsCapoMacellato }
     *     
     */
    public void setListaCapiMacellati(ArrayOfClsCapoMacellato value) {
        this.listaCapiMacellati = value;
    }

    /**
     * Recupera il valore della proprietà listaCapiOvicaprini.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfClsCapoOvicaprino }
     *     
     */
    public ArrayOfClsCapoOvicaprino getListaCapiOvicaprini() {
        return listaCapiOvicaprini;
    }

    /**
     * Imposta il valore della proprietà listaCapiOvicaprini.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfClsCapoOvicaprino }
     *     
     */
    public void setListaCapiOvicaprini(ArrayOfClsCapoOvicaprino value) {
        this.listaCapiOvicaprini = value;
    }

    /**
     * Recupera il valore della proprietà listaVacche.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfClsCapoVacca }
     *     
     */
    public ArrayOfClsCapoVacca getListaVacche() {
        return listaVacche;
    }

    /**
     * Imposta il valore della proprietà listaVacche.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfClsCapoVacca }
     *     
     */
    public void setListaVacche(ArrayOfClsCapoVacca value) {
        this.listaVacche = value;
    }

}

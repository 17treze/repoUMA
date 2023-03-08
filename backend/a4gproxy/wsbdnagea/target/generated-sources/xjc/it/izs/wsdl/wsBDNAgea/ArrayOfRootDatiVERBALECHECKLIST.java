//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:04 PM CEST 
//


package it.izs.wsdl.wsBDNAgea;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per ArrayOfRootDatiVERBALE_CHECK_LIST complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiVERBALE_CHECK_LIST"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="VERBALE_CHECK_LIST" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="DOC" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *                   &lt;element name="NOME_FILE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INSERIMENTO_BDN" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRootDatiVERBALE_CHECK_LIST", propOrder = {
    "verbalechecklist"
})
public class ArrayOfRootDatiVERBALECHECKLIST {

    @XmlElement(name = "VERBALE_CHECK_LIST")
    protected List<ArrayOfRootDatiVERBALECHECKLIST.VERBALECHECKLIST> verbalechecklist;

    /**
     * Gets the value of the verbalechecklist property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the verbalechecklist property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVERBALECHECKLIST().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiVERBALECHECKLIST.VERBALECHECKLIST }
     * 
     * 
     */
    public List<ArrayOfRootDatiVERBALECHECKLIST.VERBALECHECKLIST> getVERBALECHECKLIST() {
        if (verbalechecklist == null) {
            verbalechecklist = new ArrayList<ArrayOfRootDatiVERBALECHECKLIST.VERBALECHECKLIST>();
        }
        return this.verbalechecklist;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="DOC" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
     *         &lt;element name="NOME_FILE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INSERIMENTO_BDN" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "doc",
        "nomefile",
        "dtinserimentobdn"
    })
    public static class VERBALECHECKLIST {

        @XmlElement(name = "DOC")
        protected byte[] doc;
        @XmlElement(name = "NOME_FILE")
        protected String nomefile;
        @XmlElement(name = "DT_INSERIMENTO_BDN")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtinserimentobdn;

        /**
         * Recupera il valore della proprietà doc.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getDOC() {
            return doc;
        }

        /**
         * Imposta il valore della proprietà doc.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setDOC(byte[] value) {
            this.doc = value;
        }

        /**
         * Recupera il valore della proprietà nomefile.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNOMEFILE() {
            return nomefile;
        }

        /**
         * Imposta il valore della proprietà nomefile.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNOMEFILE(String value) {
            this.nomefile = value;
        }

        /**
         * Recupera il valore della proprietà dtinserimentobdn.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINSERIMENTOBDN() {
            return dtinserimentobdn;
        }

        /**
         * Imposta il valore della proprietà dtinserimentobdn.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINSERIMENTOBDN(XMLGregorianCalendar value) {
            this.dtinserimentobdn = value;
        }

    }

}

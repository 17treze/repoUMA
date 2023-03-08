//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:04 PM CEST 
//


package it.izs.wsdl.wsBDNAgea;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per ArrayOfRootDatiAGEA_RICH_CALCOLO_CONSISTENZA complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiAGEA_RICH_CALCOLO_CONSISTENZA"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AGEA_RICH_CALCOLO_CONSISTENZA" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="RICHIESTA_CONS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="DT_RICHIESTA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_ELABORAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="N_LOTTO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DT_PRESA_IN_CARICO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="ERRORI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="METODO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
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
@XmlType(name = "ArrayOfRootDatiAGEA_RICH_CALCOLO_CONSISTENZA", propOrder = {
    "agearichcalcoloconsistenza"
})
public class ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA {

    @XmlElement(name = "AGEA_RICH_CALCOLO_CONSISTENZA")
    protected List<ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA.AGEARICHCALCOLOCONSISTENZA> agearichcalcoloconsistenza;

    /**
     * Gets the value of the agearichcalcoloconsistenza property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agearichcalcoloconsistenza property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAGEARICHCALCOLOCONSISTENZA().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA.AGEARICHCALCOLOCONSISTENZA }
     * 
     * 
     */
    public List<ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA.AGEARICHCALCOLOCONSISTENZA> getAGEARICHCALCOLOCONSISTENZA() {
        if (agearichcalcoloconsistenza == null) {
            agearichcalcoloconsistenza = new ArrayList<ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA.AGEARICHCALCOLOCONSISTENZA>();
        }
        return this.agearichcalcoloconsistenza;
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
     *         &lt;element name="RICHIESTA_CONS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="DT_RICHIESTA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_ELABORAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="UTENTE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="N_LOTTO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DT_PRESA_IN_CARICO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="ERRORI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="METODO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
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
        "richiestaconsid",
        "dtrichiesta",
        "dtinizioperiodo",
        "dtfineperiodo",
        "cuaa",
        "tiporesponsabilita",
        "dtfineelaborazione",
        "flagcontrollo",
        "utenteid",
        "nlotto",
        "dtpresaincarico",
        "errori",
        "metodoid"
    })
    public static class AGEARICHCALCOLOCONSISTENZA {

        @XmlElement(name = "RICHIESTA_CONS_ID", required = true)
        protected BigDecimal richiestaconsid;
        @XmlElement(name = "DT_RICHIESTA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtrichiesta;
        @XmlElement(name = "DT_INIZIO_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtinizioperiodo;
        @XmlElement(name = "DT_FINE_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtfineperiodo;
        @XmlElement(name = "CUAA")
        protected String cuaa;
        @XmlElement(name = "TIPO_RESPONSABILITA")
        protected String tiporesponsabilita;
        @XmlElement(name = "DT_FINE_ELABORAZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtfineelaborazione;
        @XmlElement(name = "FLAG_CONTROLLO")
        protected String flagcontrollo;
        @XmlElement(name = "UTENTE_ID")
        protected BigDecimal utenteid;
        @XmlElement(name = "N_LOTTO")
        protected BigDecimal nlotto;
        @XmlElement(name = "DT_PRESA_IN_CARICO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtpresaincarico;
        @XmlElement(name = "ERRORI")
        protected String errori;
        @XmlElement(name = "METODO_ID", required = true)
        protected BigDecimal metodoid;

        /**
         * Recupera il valore della proprietà richiestaconsid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getRICHIESTACONSID() {
            return richiestaconsid;
        }

        /**
         * Imposta il valore della proprietà richiestaconsid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setRICHIESTACONSID(BigDecimal value) {
            this.richiestaconsid = value;
        }

        /**
         * Recupera il valore della proprietà dtrichiesta.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTRICHIESTA() {
            return dtrichiesta;
        }

        /**
         * Imposta il valore della proprietà dtrichiesta.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTRICHIESTA(XMLGregorianCalendar value) {
            this.dtrichiesta = value;
        }

        /**
         * Recupera il valore della proprietà dtinizioperiodo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINIZIOPERIODO() {
            return dtinizioperiodo;
        }

        /**
         * Imposta il valore della proprietà dtinizioperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINIZIOPERIODO(XMLGregorianCalendar value) {
            this.dtinizioperiodo = value;
        }

        /**
         * Recupera il valore della proprietà dtfineperiodo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTFINEPERIODO() {
            return dtfineperiodo;
        }

        /**
         * Imposta il valore della proprietà dtfineperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTFINEPERIODO(XMLGregorianCalendar value) {
            this.dtfineperiodo = value;
        }

        /**
         * Recupera il valore della proprietà cuaa.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCUAA() {
            return cuaa;
        }

        /**
         * Imposta il valore della proprietà cuaa.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCUAA(String value) {
            this.cuaa = value;
        }

        /**
         * Recupera il valore della proprietà tiporesponsabilita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPORESPONSABILITA() {
            return tiporesponsabilita;
        }

        /**
         * Imposta il valore della proprietà tiporesponsabilita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPORESPONSABILITA(String value) {
            this.tiporesponsabilita = value;
        }

        /**
         * Recupera il valore della proprietà dtfineelaborazione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTFINEELABORAZIONE() {
            return dtfineelaborazione;
        }

        /**
         * Imposta il valore della proprietà dtfineelaborazione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTFINEELABORAZIONE(XMLGregorianCalendar value) {
            this.dtfineelaborazione = value;
        }

        /**
         * Recupera il valore della proprietà flagcontrollo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGCONTROLLO() {
            return flagcontrollo;
        }

        /**
         * Imposta il valore della proprietà flagcontrollo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGCONTROLLO(String value) {
            this.flagcontrollo = value;
        }

        /**
         * Recupera il valore della proprietà utenteid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getUTENTEID() {
            return utenteid;
        }

        /**
         * Imposta il valore della proprietà utenteid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setUTENTEID(BigDecimal value) {
            this.utenteid = value;
        }

        /**
         * Recupera il valore della proprietà nlotto.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNLOTTO() {
            return nlotto;
        }

        /**
         * Imposta il valore della proprietà nlotto.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNLOTTO(BigDecimal value) {
            this.nlotto = value;
        }

        /**
         * Recupera il valore della proprietà dtpresaincarico.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTPRESAINCARICO() {
            return dtpresaincarico;
        }

        /**
         * Imposta il valore della proprietà dtpresaincarico.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTPRESAINCARICO(XMLGregorianCalendar value) {
            this.dtpresaincarico = value;
        }

        /**
         * Recupera il valore della proprietà errori.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getERRORI() {
            return errori;
        }

        /**
         * Imposta il valore della proprietà errori.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setERRORI(String value) {
            this.errori = value;
        }

        /**
         * Recupera il valore della proprietà metodoid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMETODOID() {
            return metodoid;
        }

        /**
         * Imposta il valore della proprietà metodoid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMETODOID(BigDecimal value) {
            this.metodoid = value;
        }

    }

}

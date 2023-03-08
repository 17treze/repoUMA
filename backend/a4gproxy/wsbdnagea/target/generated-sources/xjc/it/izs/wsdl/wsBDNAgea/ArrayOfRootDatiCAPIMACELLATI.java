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
 * <p>Classe Java per ArrayOfRootDatiCAPI_MACELLATI complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCAPI_MACELLATI"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAPI_MACELLATI" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DATA_REPERIMENTO_INFO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="P_SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_MADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_PRECEDENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_COMPILAZIONE_CEDOLA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="P_TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="P_MACELLO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_FISC_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="P_MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_PESO_CARCASSA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_NUM_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCAPI_MACELLATI", propOrder = {
    "capimacellati"
})
public class ArrayOfRootDatiCAPIMACELLATI {

    @XmlElement(name = "CAPI_MACELLATI")
    protected List<ArrayOfRootDatiCAPIMACELLATI.CAPIMACELLATI> capimacellati;

    /**
     * Gets the value of the capimacellati property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capimacellati property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAPIMACELLATI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCAPIMACELLATI.CAPIMACELLATI }
     * 
     * 
     */
    public List<ArrayOfRootDatiCAPIMACELLATI.CAPIMACELLATI> getCAPIMACELLATI() {
        if (capimacellati == null) {
            capimacellati = new ArrayList<ArrayOfRootDatiCAPIMACELLATI.CAPIMACELLATI>();
        }
        return this.capimacellati;
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
     *         &lt;element name="P_CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="P_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DATA_REPERIMENTO_INFO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="P_SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_MADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_PRECEDENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_COMPILAZIONE_CEDOLA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="P_TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="P_MACELLO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_FISC_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="P_MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_PESO_CARCASSA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="P_NUM_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pcapoid",
        "pcodice",
        "pdatareperimentoinfo",
        "psesso",
        "prazzacodice",
        "pdtnascita",
        "pcodmadre",
        "pcodprecedente",
        "pdtcompilazionecedola",
        "ptipoorigine",
        "plgcodice",
        "pdtiscrizione",
        "pmacelloid",
        "pcodregione",
        "pcodmacello",
        "pcodfiscmacello",
        "pdtmacellazione",
        "pdtingresso",
        "pdtuscita",
        "pmotivouscita",
        "ppesocarcassa",
        "pnummacellazione"
    })
    public static class CAPIMACELLATI {

        @XmlElement(name = "P_CAPO_ID")
        protected BigDecimal pcapoid;
        @XmlElement(name = "P_CODICE")
        protected String pcodice;
        @XmlElement(name = "P_DATA_REPERIMENTO_INFO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdatareperimentoinfo;
        @XmlElement(name = "P_SESSO")
        protected String psesso;
        @XmlElement(name = "P_RAZZA_CODICE")
        protected String prazzacodice;
        @XmlElement(name = "P_DT_NASCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdtnascita;
        @XmlElement(name = "P_COD_MADRE")
        protected String pcodmadre;
        @XmlElement(name = "P_COD_PRECEDENTE")
        protected String pcodprecedente;
        @XmlElement(name = "P_DT_COMPILAZIONE_CEDOLA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdtcompilazionecedola;
        @XmlElement(name = "P_TIPO_ORIGINE")
        protected String ptipoorigine;
        @XmlElement(name = "P_LG_CODICE")
        protected String plgcodice;
        @XmlElement(name = "P_DT_ISCRIZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdtiscrizione;
        @XmlElement(name = "P_MACELLO_ID")
        protected BigDecimal pmacelloid;
        @XmlElement(name = "P_COD_REGIONE")
        protected String pcodregione;
        @XmlElement(name = "P_COD_MACELLO")
        protected String pcodmacello;
        @XmlElement(name = "P_COD_FISC_MACELLO")
        protected String pcodfiscmacello;
        @XmlElement(name = "P_DT_MACELLAZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdtmacellazione;
        @XmlElement(name = "P_DT_INGRESSO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdtingresso;
        @XmlElement(name = "P_DT_USCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar pdtuscita;
        @XmlElement(name = "P_MOTIVO_USCITA")
        protected String pmotivouscita;
        @XmlElement(name = "P_PESO_CARCASSA")
        protected BigDecimal ppesocarcassa;
        @XmlElement(name = "P_NUM_MACELLAZIONE")
        protected String pnummacellazione;

        /**
         * Recupera il valore della proprietà pcapoid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPCAPOID() {
            return pcapoid;
        }

        /**
         * Imposta il valore della proprietà pcapoid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPCAPOID(BigDecimal value) {
            this.pcapoid = value;
        }

        /**
         * Recupera il valore della proprietà pcodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODICE() {
            return pcodice;
        }

        /**
         * Imposta il valore della proprietà pcodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODICE(String value) {
            this.pcodice = value;
        }

        /**
         * Recupera il valore della proprietà pdatareperimentoinfo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDATAREPERIMENTOINFO() {
            return pdatareperimentoinfo;
        }

        /**
         * Imposta il valore della proprietà pdatareperimentoinfo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDATAREPERIMENTOINFO(XMLGregorianCalendar value) {
            this.pdatareperimentoinfo = value;
        }

        /**
         * Recupera il valore della proprietà psesso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPSESSO() {
            return psesso;
        }

        /**
         * Imposta il valore della proprietà psesso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPSESSO(String value) {
            this.psesso = value;
        }

        /**
         * Recupera il valore della proprietà prazzacodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPRAZZACODICE() {
            return prazzacodice;
        }

        /**
         * Imposta il valore della proprietà prazzacodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPRAZZACODICE(String value) {
            this.prazzacodice = value;
        }

        /**
         * Recupera il valore della proprietà pdtnascita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDTNASCITA() {
            return pdtnascita;
        }

        /**
         * Imposta il valore della proprietà pdtnascita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDTNASCITA(XMLGregorianCalendar value) {
            this.pdtnascita = value;
        }

        /**
         * Recupera il valore della proprietà pcodmadre.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODMADRE() {
            return pcodmadre;
        }

        /**
         * Imposta il valore della proprietà pcodmadre.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODMADRE(String value) {
            this.pcodmadre = value;
        }

        /**
         * Recupera il valore della proprietà pcodprecedente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODPRECEDENTE() {
            return pcodprecedente;
        }

        /**
         * Imposta il valore della proprietà pcodprecedente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODPRECEDENTE(String value) {
            this.pcodprecedente = value;
        }

        /**
         * Recupera il valore della proprietà pdtcompilazionecedola.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDTCOMPILAZIONECEDOLA() {
            return pdtcompilazionecedola;
        }

        /**
         * Imposta il valore della proprietà pdtcompilazionecedola.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDTCOMPILAZIONECEDOLA(XMLGregorianCalendar value) {
            this.pdtcompilazionecedola = value;
        }

        /**
         * Recupera il valore della proprietà ptipoorigine.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPTIPOORIGINE() {
            return ptipoorigine;
        }

        /**
         * Imposta il valore della proprietà ptipoorigine.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPTIPOORIGINE(String value) {
            this.ptipoorigine = value;
        }

        /**
         * Recupera il valore della proprietà plgcodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPLGCODICE() {
            return plgcodice;
        }

        /**
         * Imposta il valore della proprietà plgcodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPLGCODICE(String value) {
            this.plgcodice = value;
        }

        /**
         * Recupera il valore della proprietà pdtiscrizione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDTISCRIZIONE() {
            return pdtiscrizione;
        }

        /**
         * Imposta il valore della proprietà pdtiscrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDTISCRIZIONE(XMLGregorianCalendar value) {
            this.pdtiscrizione = value;
        }

        /**
         * Recupera il valore della proprietà pmacelloid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPMACELLOID() {
            return pmacelloid;
        }

        /**
         * Imposta il valore della proprietà pmacelloid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPMACELLOID(BigDecimal value) {
            this.pmacelloid = value;
        }

        /**
         * Recupera il valore della proprietà pcodregione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODREGIONE() {
            return pcodregione;
        }

        /**
         * Imposta il valore della proprietà pcodregione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODREGIONE(String value) {
            this.pcodregione = value;
        }

        /**
         * Recupera il valore della proprietà pcodmacello.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODMACELLO() {
            return pcodmacello;
        }

        /**
         * Imposta il valore della proprietà pcodmacello.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODMACELLO(String value) {
            this.pcodmacello = value;
        }

        /**
         * Recupera il valore della proprietà pcodfiscmacello.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCODFISCMACELLO() {
            return pcodfiscmacello;
        }

        /**
         * Imposta il valore della proprietà pcodfiscmacello.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCODFISCMACELLO(String value) {
            this.pcodfiscmacello = value;
        }

        /**
         * Recupera il valore della proprietà pdtmacellazione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDTMACELLAZIONE() {
            return pdtmacellazione;
        }

        /**
         * Imposta il valore della proprietà pdtmacellazione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDTMACELLAZIONE(XMLGregorianCalendar value) {
            this.pdtmacellazione = value;
        }

        /**
         * Recupera il valore della proprietà pdtingresso.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDTINGRESSO() {
            return pdtingresso;
        }

        /**
         * Imposta il valore della proprietà pdtingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDTINGRESSO(XMLGregorianCalendar value) {
            this.pdtingresso = value;
        }

        /**
         * Recupera il valore della proprietà pdtuscita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getPDTUSCITA() {
            return pdtuscita;
        }

        /**
         * Imposta il valore della proprietà pdtuscita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setPDTUSCITA(XMLGregorianCalendar value) {
            this.pdtuscita = value;
        }

        /**
         * Recupera il valore della proprietà pmotivouscita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPMOTIVOUSCITA() {
            return pmotivouscita;
        }

        /**
         * Imposta il valore della proprietà pmotivouscita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPMOTIVOUSCITA(String value) {
            this.pmotivouscita = value;
        }

        /**
         * Recupera il valore della proprietà ppesocarcassa.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPPESOCARCASSA() {
            return ppesocarcassa;
        }

        /**
         * Imposta il valore della proprietà ppesocarcassa.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPPESOCARCASSA(BigDecimal value) {
            this.ppesocarcassa = value;
        }

        /**
         * Recupera il valore della proprietà pnummacellazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPNUMMACELLAZIONE() {
            return pnummacellazione;
        }

        /**
         * Imposta il valore della proprietà pnummacellazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPNUMMACELLAZIONE(String value) {
            this.pnummacellazione = value;
        }

    }

}

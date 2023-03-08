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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ArrayOfRootDatiCAPI_DOMANDA_MAC complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCAPI_DOMANDA_MAC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAPI_DOMANDA_MAC" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_CAPO_A_PREMIO_MAC_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_DOMANDA_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_TIPO_DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DOMANDA_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_CAPO_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="P_CAPO_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DATA_REPERIMENTO_INFO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_FLAG_PROVENIENZA_DATI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_COMUNICAZIONE_CAPO_BDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_MADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_PRECEDENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_COMPILAZIONE_CEDOLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_COD_FISC_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCAPI_DOMANDA_MAC", propOrder = {
    "capidomandamac"
})
public class ArrayOfRootDatiCAPIDOMANDAMAC {

    @XmlElement(name = "CAPI_DOMANDA_MAC")
    protected List<ArrayOfRootDatiCAPIDOMANDAMAC.CAPIDOMANDAMAC> capidomandamac;

    /**
     * Gets the value of the capidomandamac property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capidomandamac property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAPIDOMANDAMAC().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCAPIDOMANDAMAC.CAPIDOMANDAMAC }
     * 
     * 
     */
    public List<ArrayOfRootDatiCAPIDOMANDAMAC.CAPIDOMANDAMAC> getCAPIDOMANDAMAC() {
        if (capidomandamac == null) {
            capidomandamac = new ArrayList<ArrayOfRootDatiCAPIDOMANDAMAC.CAPIDOMANDAMAC>();
        }
        return this.capidomandamac;
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
     *         &lt;element name="P_CAPO_A_PREMIO_MAC_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_DOMANDA_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_TIPO_DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DOMANDA_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_CAPO_BDN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="P_CAPO_AGEA_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DATA_REPERIMENTO_INFO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_FLAG_PROVENIENZA_DATI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_COMUNICAZIONE_CAPO_BDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_REVOCA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_MADRE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_PRECEDENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_COMPILAZIONE_CEDOLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_COD_FISC_MACELLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_MACELLAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pcapoapremiomacid",
        "pdomandaageaid",
        "ptipodocumento",
        "pdomandabdnid",
        "pcapobdnid",
        "pcapoageaid",
        "pcodice",
        "pdatareperimentoinfo",
        "pflagprovenienzadati",
        "pdtcomunicazionecapobdn",
        "pdtrevoca",
        "psesso",
        "prazzacodice",
        "pdtnascita",
        "pcodmadre",
        "pcodprecedente",
        "pdtcompilazionecedola",
        "ptipoorigine",
        "plgcodice",
        "pdtiscrizione",
        "pcodregione",
        "pcodmacello",
        "pcodfiscmacello",
        "pdtmacellazione",
        "ppesocarcassa",
        "pnummacellazione"
    })
    public static class CAPIDOMANDAMAC {

        @XmlElement(name = "P_CAPO_A_PREMIO_MAC_ID", required = true)
        protected BigDecimal pcapoapremiomacid;
        @XmlElement(name = "P_DOMANDA_AGEA_ID")
        protected String pdomandaageaid;
        @XmlElement(name = "P_TIPO_DOCUMENTO")
        protected String ptipodocumento;
        @XmlElement(name = "P_DOMANDA_BDN_ID", required = true)
        protected BigDecimal pdomandabdnid;
        @XmlElement(name = "P_CAPO_BDN_ID")
        protected BigDecimal pcapobdnid;
        @XmlElement(name = "P_CAPO_AGEA_ID")
        protected String pcapoageaid;
        @XmlElement(name = "P_CODICE")
        protected String pcodice;
        @XmlElement(name = "P_DATA_REPERIMENTO_INFO")
        protected String pdatareperimentoinfo;
        @XmlElement(name = "P_FLAG_PROVENIENZA_DATI")
        protected String pflagprovenienzadati;
        @XmlElement(name = "P_DT_COMUNICAZIONE_CAPO_BDN")
        protected String pdtcomunicazionecapobdn;
        @XmlElement(name = "P_DT_REVOCA")
        protected String pdtrevoca;
        @XmlElement(name = "P_SESSO")
        protected String psesso;
        @XmlElement(name = "P_RAZZA_CODICE")
        protected String prazzacodice;
        @XmlElement(name = "P_DT_NASCITA")
        protected String pdtnascita;
        @XmlElement(name = "P_COD_MADRE")
        protected String pcodmadre;
        @XmlElement(name = "P_COD_PRECEDENTE")
        protected String pcodprecedente;
        @XmlElement(name = "P_DT_COMPILAZIONE_CEDOLA")
        protected String pdtcompilazionecedola;
        @XmlElement(name = "P_TIPO_ORIGINE")
        protected String ptipoorigine;
        @XmlElement(name = "P_LG_CODICE")
        protected String plgcodice;
        @XmlElement(name = "P_DT_ISCRIZIONE")
        protected String pdtiscrizione;
        @XmlElement(name = "P_COD_REGIONE")
        protected String pcodregione;
        @XmlElement(name = "P_COD_MACELLO")
        protected String pcodmacello;
        @XmlElement(name = "P_COD_FISC_MACELLO")
        protected String pcodfiscmacello;
        @XmlElement(name = "P_DT_MACELLAZIONE")
        protected String pdtmacellazione;
        @XmlElement(name = "P_PESO_CARCASSA")
        protected BigDecimal ppesocarcassa;
        @XmlElement(name = "P_NUM_MACELLAZIONE")
        protected String pnummacellazione;

        /**
         * Recupera il valore della proprietà pcapoapremiomacid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPCAPOAPREMIOMACID() {
            return pcapoapremiomacid;
        }

        /**
         * Imposta il valore della proprietà pcapoapremiomacid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPCAPOAPREMIOMACID(BigDecimal value) {
            this.pcapoapremiomacid = value;
        }

        /**
         * Recupera il valore della proprietà pdomandaageaid.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDOMANDAAGEAID() {
            return pdomandaageaid;
        }

        /**
         * Imposta il valore della proprietà pdomandaageaid.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDOMANDAAGEAID(String value) {
            this.pdomandaageaid = value;
        }

        /**
         * Recupera il valore della proprietà ptipodocumento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPTIPODOCUMENTO() {
            return ptipodocumento;
        }

        /**
         * Imposta il valore della proprietà ptipodocumento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPTIPODOCUMENTO(String value) {
            this.ptipodocumento = value;
        }

        /**
         * Recupera il valore della proprietà pdomandabdnid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPDOMANDABDNID() {
            return pdomandabdnid;
        }

        /**
         * Imposta il valore della proprietà pdomandabdnid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPDOMANDABDNID(BigDecimal value) {
            this.pdomandabdnid = value;
        }

        /**
         * Recupera il valore della proprietà pcapobdnid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPCAPOBDNID() {
            return pcapobdnid;
        }

        /**
         * Imposta il valore della proprietà pcapobdnid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPCAPOBDNID(BigDecimal value) {
            this.pcapobdnid = value;
        }

        /**
         * Recupera il valore della proprietà pcapoageaid.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPCAPOAGEAID() {
            return pcapoageaid;
        }

        /**
         * Imposta il valore della proprietà pcapoageaid.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPCAPOAGEAID(String value) {
            this.pcapoageaid = value;
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
         *     {@link String }
         *     
         */
        public String getPDATAREPERIMENTOINFO() {
            return pdatareperimentoinfo;
        }

        /**
         * Imposta il valore della proprietà pdatareperimentoinfo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDATAREPERIMENTOINFO(String value) {
            this.pdatareperimentoinfo = value;
        }

        /**
         * Recupera il valore della proprietà pflagprovenienzadati.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPFLAGPROVENIENZADATI() {
            return pflagprovenienzadati;
        }

        /**
         * Imposta il valore della proprietà pflagprovenienzadati.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPFLAGPROVENIENZADATI(String value) {
            this.pflagprovenienzadati = value;
        }

        /**
         * Recupera il valore della proprietà pdtcomunicazionecapobdn.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTCOMUNICAZIONECAPOBDN() {
            return pdtcomunicazionecapobdn;
        }

        /**
         * Imposta il valore della proprietà pdtcomunicazionecapobdn.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTCOMUNICAZIONECAPOBDN(String value) {
            this.pdtcomunicazionecapobdn = value;
        }

        /**
         * Recupera il valore della proprietà pdtrevoca.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPDTREVOCA() {
            return pdtrevoca;
        }

        /**
         * Imposta il valore della proprietà pdtrevoca.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTREVOCA(String value) {
            this.pdtrevoca = value;
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
         *     {@link String }
         *     
         */
        public String getPDTNASCITA() {
            return pdtnascita;
        }

        /**
         * Imposta il valore della proprietà pdtnascita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTNASCITA(String value) {
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
         *     {@link String }
         *     
         */
        public String getPDTCOMPILAZIONECEDOLA() {
            return pdtcompilazionecedola;
        }

        /**
         * Imposta il valore della proprietà pdtcompilazionecedola.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTCOMPILAZIONECEDOLA(String value) {
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
         *     {@link String }
         *     
         */
        public String getPDTISCRIZIONE() {
            return pdtiscrizione;
        }

        /**
         * Imposta il valore della proprietà pdtiscrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTISCRIZIONE(String value) {
            this.pdtiscrizione = value;
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
         *     {@link String }
         *     
         */
        public String getPDTMACELLAZIONE() {
            return pdtmacellazione;
        }

        /**
         * Imposta il valore della proprietà pdtmacellazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPDTMACELLAZIONE(String value) {
            this.pdtmacellazione = value;
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

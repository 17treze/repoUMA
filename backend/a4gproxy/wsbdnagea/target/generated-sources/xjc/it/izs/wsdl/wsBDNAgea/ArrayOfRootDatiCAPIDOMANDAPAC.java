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
 * <p>Classe Java per ArrayOfRootDatiCAPI_DOMANDA_PAC complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCAPI_DOMANDA_PAC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAPI_DOMANDA_PAC" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="P_CAPO_A_PREMIO_PAC_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
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
 *                   &lt;element name="P_FLAG_STATO_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCAPI_DOMANDA_PAC", propOrder = {
    "capidomandapac"
})
public class ArrayOfRootDatiCAPIDOMANDAPAC {

    @XmlElement(name = "CAPI_DOMANDA_PAC")
    protected List<ArrayOfRootDatiCAPIDOMANDAPAC.CAPIDOMANDAPAC> capidomandapac;

    /**
     * Gets the value of the capidomandapac property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capidomandapac property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCAPIDOMANDAPAC().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCAPIDOMANDAPAC.CAPIDOMANDAPAC }
     * 
     * 
     */
    public List<ArrayOfRootDatiCAPIDOMANDAPAC.CAPIDOMANDAPAC> getCAPIDOMANDAPAC() {
        if (capidomandapac == null) {
            capidomandapac = new ArrayList<ArrayOfRootDatiCAPIDOMANDAPAC.CAPIDOMANDAPAC>();
        }
        return this.capidomandapac;
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
     *         &lt;element name="P_CAPO_A_PREMIO_PAC_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
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
     *         &lt;element name="P_FLAG_STATO_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_TIPO_ORIGINE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_LG_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="P_DT_ISCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pcapoapremiopacid",
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
        "pflagstatocapo",
        "ptipoorigine",
        "plgcodice",
        "pdtiscrizione"
    })
    public static class CAPIDOMANDAPAC {

        @XmlElement(name = "P_CAPO_A_PREMIO_PAC_ID", required = true)
        protected BigDecimal pcapoapremiopacid;
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
        @XmlElement(name = "P_FLAG_STATO_CAPO")
        protected String pflagstatocapo;
        @XmlElement(name = "P_TIPO_ORIGINE")
        protected String ptipoorigine;
        @XmlElement(name = "P_LG_CODICE")
        protected String plgcodice;
        @XmlElement(name = "P_DT_ISCRIZIONE")
        protected String pdtiscrizione;

        /**
         * Recupera il valore della proprietà pcapoapremiopacid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPCAPOAPREMIOPACID() {
            return pcapoapremiopacid;
        }

        /**
         * Imposta il valore della proprietà pcapoapremiopacid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPCAPOAPREMIOPACID(BigDecimal value) {
            this.pcapoapremiopacid = value;
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
         * Recupera il valore della proprietà pflagstatocapo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPFLAGSTATOCAPO() {
            return pflagstatocapo;
        }

        /**
         * Imposta il valore della proprietà pflagstatocapo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPFLAGSTATOCAPO(String value) {
            this.pflagstatocapo = value;
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

    }

}

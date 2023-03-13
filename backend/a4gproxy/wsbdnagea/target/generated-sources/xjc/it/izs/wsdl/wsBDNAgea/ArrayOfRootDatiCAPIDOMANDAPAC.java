//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.03.13 at 04:32:46 PM CET 
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
 * <p>Java class for ArrayOfRootDatiCAPI_DOMANDA_PAC complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
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
         * Gets the value of the pcapoapremiopacid property.
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
         * Sets the value of the pcapoapremiopacid property.
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
         * Gets the value of the pdomandaageaid property.
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
         * Sets the value of the pdomandaageaid property.
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
         * Gets the value of the ptipodocumento property.
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
         * Sets the value of the ptipodocumento property.
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
         * Gets the value of the pdomandabdnid property.
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
         * Sets the value of the pdomandabdnid property.
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
         * Gets the value of the pcapobdnid property.
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
         * Sets the value of the pcapobdnid property.
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
         * Gets the value of the pcapoageaid property.
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
         * Sets the value of the pcapoageaid property.
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
         * Gets the value of the pcodice property.
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
         * Sets the value of the pcodice property.
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
         * Gets the value of the pdatareperimentoinfo property.
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
         * Sets the value of the pdatareperimentoinfo property.
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
         * Gets the value of the pflagprovenienzadati property.
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
         * Sets the value of the pflagprovenienzadati property.
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
         * Gets the value of the pdtcomunicazionecapobdn property.
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
         * Sets the value of the pdtcomunicazionecapobdn property.
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
         * Gets the value of the pdtrevoca property.
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
         * Sets the value of the pdtrevoca property.
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
         * Gets the value of the psesso property.
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
         * Sets the value of the psesso property.
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
         * Gets the value of the prazzacodice property.
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
         * Sets the value of the prazzacodice property.
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
         * Gets the value of the pdtnascita property.
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
         * Sets the value of the pdtnascita property.
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
         * Gets the value of the pcodmadre property.
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
         * Sets the value of the pcodmadre property.
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
         * Gets the value of the pcodprecedente property.
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
         * Sets the value of the pcodprecedente property.
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
         * Gets the value of the pdtcompilazionecedola property.
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
         * Sets the value of the pdtcompilazionecedola property.
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
         * Gets the value of the pflagstatocapo property.
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
         * Sets the value of the pflagstatocapo property.
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
         * Gets the value of the ptipoorigine property.
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
         * Sets the value of the ptipoorigine property.
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
         * Gets the value of the plgcodice property.
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
         * Sets the value of the plgcodice property.
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
         * Gets the value of the pdtiscrizione property.
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
         * Sets the value of the pdtiscrizione property.
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

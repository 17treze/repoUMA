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
 * <p>Java class for ArrayOfRootDatiPASCOLO_VISITATO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiPASCOLO_VISITATO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PASCOLO_VISITATO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="PAS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOMINAZIONE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="COM_ISTAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COM_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PRO_SIGLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ASL_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ASL_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ASL_DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RESP_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiPASCOLO_VISITATO", propOrder = {
    "pascolovisitato"
})
public class ArrayOfRootDatiPASCOLOVISITATO {

    @XmlElement(name = "PASCOLO_VISITATO")
    protected List<ArrayOfRootDatiPASCOLOVISITATO.PASCOLOVISITATO> pascolovisitato;

    /**
     * Gets the value of the pascolovisitato property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pascolovisitato property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPASCOLOVISITATO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiPASCOLOVISITATO.PASCOLOVISITATO }
     * 
     * 
     */
    public List<ArrayOfRootDatiPASCOLOVISITATO.PASCOLOVISITATO> getPASCOLOVISITATO() {
        if (pascolovisitato == null) {
            pascolovisitato = new ArrayList<ArrayOfRootDatiPASCOLOVISITATO.PASCOLOVISITATO>();
        }
        return this.pascolovisitato;
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
     *         &lt;element name="PAS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOMINAZIONE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LATITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="LONGITUDINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="FOGLIO_CATASTALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PARTICELLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="COM_ISTAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COM_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PRO_SIGLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ASL_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ASL_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ASL_DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SEZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SUBALTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RESP_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "pasid",
        "codicepascolo",
        "denominazionepascolo",
        "latitudine",
        "longitudine",
        "fogliocatastale",
        "particella",
        "comid",
        "comistat",
        "comdescrizione",
        "prosigla",
        "aslid",
        "aslcodice",
        "asldenominazione",
        "localita",
        "sezione",
        "subalterno",
        "respidfiscale"
    })
    public static class PASCOLOVISITATO {

        @XmlElement(name = "PAS_ID")
        protected BigDecimal pasid;
        @XmlElement(name = "CODICE_PASCOLO")
        protected String codicepascolo;
        @XmlElement(name = "DENOMINAZIONE_PASCOLO")
        protected String denominazionepascolo;
        @XmlElement(name = "LATITUDINE")
        protected BigDecimal latitudine;
        @XmlElement(name = "LONGITUDINE")
        protected BigDecimal longitudine;
        @XmlElement(name = "FOGLIO_CATASTALE")
        protected String fogliocatastale;
        @XmlElement(name = "PARTICELLA")
        protected String particella;
        @XmlElement(name = "COM_ID")
        protected BigDecimal comid;
        @XmlElement(name = "COM_ISTAT")
        protected String comistat;
        @XmlElement(name = "COM_DESCRIZIONE")
        protected String comdescrizione;
        @XmlElement(name = "PRO_SIGLA")
        protected String prosigla;
        @XmlElement(name = "ASL_ID")
        protected BigDecimal aslid;
        @XmlElement(name = "ASL_CODICE")
        protected String aslcodice;
        @XmlElement(name = "ASL_DENOMINAZIONE")
        protected String asldenominazione;
        @XmlElement(name = "LOCALITA")
        protected String localita;
        @XmlElement(name = "SEZIONE")
        protected String sezione;
        @XmlElement(name = "SUBALTERNO")
        protected String subalterno;
        @XmlElement(name = "RESP_ID_FISCALE")
        protected String respidfiscale;

        /**
         * Gets the value of the pasid property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPASID() {
            return pasid;
        }

        /**
         * Sets the value of the pasid property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPASID(BigDecimal value) {
            this.pasid = value;
        }

        /**
         * Gets the value of the codicepascolo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEPASCOLO() {
            return codicepascolo;
        }

        /**
         * Sets the value of the codicepascolo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEPASCOLO(String value) {
            this.codicepascolo = value;
        }

        /**
         * Gets the value of the denominazionepascolo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDENOMINAZIONEPASCOLO() {
            return denominazionepascolo;
        }

        /**
         * Sets the value of the denominazionepascolo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDENOMINAZIONEPASCOLO(String value) {
            this.denominazionepascolo = value;
        }

        /**
         * Gets the value of the latitudine property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getLATITUDINE() {
            return latitudine;
        }

        /**
         * Sets the value of the latitudine property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setLATITUDINE(BigDecimal value) {
            this.latitudine = value;
        }

        /**
         * Gets the value of the longitudine property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getLONGITUDINE() {
            return longitudine;
        }

        /**
         * Sets the value of the longitudine property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setLONGITUDINE(BigDecimal value) {
            this.longitudine = value;
        }

        /**
         * Gets the value of the fogliocatastale property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFOGLIOCATASTALE() {
            return fogliocatastale;
        }

        /**
         * Sets the value of the fogliocatastale property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFOGLIOCATASTALE(String value) {
            this.fogliocatastale = value;
        }

        /**
         * Gets the value of the particella property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPARTICELLA() {
            return particella;
        }

        /**
         * Sets the value of the particella property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPARTICELLA(String value) {
            this.particella = value;
        }

        /**
         * Gets the value of the comid property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCOMID() {
            return comid;
        }

        /**
         * Sets the value of the comid property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCOMID(BigDecimal value) {
            this.comid = value;
        }

        /**
         * Gets the value of the comistat property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCOMISTAT() {
            return comistat;
        }

        /**
         * Sets the value of the comistat property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCOMISTAT(String value) {
            this.comistat = value;
        }

        /**
         * Gets the value of the comdescrizione property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCOMDESCRIZIONE() {
            return comdescrizione;
        }

        /**
         * Sets the value of the comdescrizione property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCOMDESCRIZIONE(String value) {
            this.comdescrizione = value;
        }

        /**
         * Gets the value of the prosigla property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPROSIGLA() {
            return prosigla;
        }

        /**
         * Sets the value of the prosigla property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPROSIGLA(String value) {
            this.prosigla = value;
        }

        /**
         * Gets the value of the aslid property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getASLID() {
            return aslid;
        }

        /**
         * Sets the value of the aslid property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setASLID(BigDecimal value) {
            this.aslid = value;
        }

        /**
         * Gets the value of the aslcodice property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASLCODICE() {
            return aslcodice;
        }

        /**
         * Sets the value of the aslcodice property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASLCODICE(String value) {
            this.aslcodice = value;
        }

        /**
         * Gets the value of the asldenominazione property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASLDENOMINAZIONE() {
            return asldenominazione;
        }

        /**
         * Sets the value of the asldenominazione property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASLDENOMINAZIONE(String value) {
            this.asldenominazione = value;
        }

        /**
         * Gets the value of the localita property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLOCALITA() {
            return localita;
        }

        /**
         * Sets the value of the localita property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLOCALITA(String value) {
            this.localita = value;
        }

        /**
         * Gets the value of the sezione property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSEZIONE() {
            return sezione;
        }

        /**
         * Sets the value of the sezione property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSEZIONE(String value) {
            this.sezione = value;
        }

        /**
         * Gets the value of the subalterno property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSUBALTERNO() {
            return subalterno;
        }

        /**
         * Sets the value of the subalterno property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSUBALTERNO(String value) {
            this.subalterno = value;
        }

        /**
         * Gets the value of the respidfiscale property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRESPIDFISCALE() {
            return respidfiscale;
        }

        /**
         * Sets the value of the respidfiscale property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRESPIDFISCALE(String value) {
            this.respidfiscale = value;
        }

    }

}

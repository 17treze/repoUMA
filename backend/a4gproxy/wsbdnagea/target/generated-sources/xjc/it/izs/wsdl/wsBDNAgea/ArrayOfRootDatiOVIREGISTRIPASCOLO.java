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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ArrayOfRootDatiOVI_REGISTRI_PASCOLO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiOVI_REGISTRI_PASCOLO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OVI_REGISTRI_PASCOLO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="ASL_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CUAA_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COMUNE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ASL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INGRESSO_AL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_RIENTRO_DA_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ESTREMI_DOC_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_DOC_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_DESTINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ESTREMI_DOC_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_DOC_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CODICE_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_RAZZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiOVI_REGISTRI_PASCOLO", propOrder = {
    "oviregistripascolo"
})
public class ArrayOfRootDatiOVIREGISTRIPASCOLO {

    @XmlElement(name = "OVI_REGISTRI_PASCOLO")
    protected List<ArrayOfRootDatiOVIREGISTRIPASCOLO.OVIREGISTRIPASCOLO> oviregistripascolo;

    /**
     * Gets the value of the oviregistripascolo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the oviregistripascolo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOVIREGISTRIPASCOLO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiOVIREGISTRIPASCOLO.OVIREGISTRIPASCOLO }
     * 
     * 
     */
    public List<ArrayOfRootDatiOVIREGISTRIPASCOLO.OVIREGISTRIPASCOLO> getOVIREGISTRIPASCOLO() {
        if (oviregistripascolo == null) {
            oviregistripascolo = new ArrayList<ArrayOfRootDatiOVIREGISTRIPASCOLO.OVIREGISTRIPASCOLO>();
        }
        return this.oviregistripascolo;
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
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="ASL_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_AZIENDA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CUAA_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COMUNE_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ASL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INGRESSO_AL_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_RIENTRO_DA_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ESTREMI_DOC_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_DOC_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_DESTINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ESTREMI_DOC_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_DOC_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="CODICE_CAPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_RAZZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "allevid",
        "aslazienda",
        "codiceazienda",
        "allevidfiscale",
        "specodice",
        "cuaadetentore",
        "codicepascolo",
        "comunepascolo",
        "aslpascolo",
        "dtingressoalpascolo",
        "dtrientrodapascolo",
        "tipoprovenienza",
        "estremidocingresso",
        "dtdocingresso",
        "tipodestinazione",
        "estremidocuscita",
        "dtdocuscita",
        "capoid",
        "codicecapo",
        "codiceelettronico",
        "sesso",
        "codicerazza"
    })
    public static class OVIREGISTRIPASCOLO {

        @XmlElement(name = "ALLEV_ID", required = true)
        protected BigDecimal allevid;
        @XmlElement(name = "ASL_AZIENDA")
        protected String aslazienda;
        @XmlElement(name = "CODICE_AZIENDA")
        protected String codiceazienda;
        @XmlElement(name = "ALLEV_ID_FISCALE")
        protected String allevidfiscale;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "CUAA_DETENTORE")
        protected String cuaadetentore;
        @XmlElement(name = "CODICE_PASCOLO")
        protected String codicepascolo;
        @XmlElement(name = "COMUNE_PASCOLO")
        protected String comunepascolo;
        @XmlElement(name = "ASL_PASCOLO")
        protected String aslpascolo;
        @XmlElement(name = "DT_INGRESSO_AL_PASCOLO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtingressoalpascolo;
        @XmlElement(name = "DT_RIENTRO_DA_PASCOLO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtrientrodapascolo;
        @XmlElement(name = "TIPO_PROVENIENZA")
        protected String tipoprovenienza;
        @XmlElement(name = "ESTREMI_DOC_INGRESSO")
        protected String estremidocingresso;
        @XmlElement(name = "DT_DOC_INGRESSO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtdocingresso;
        @XmlElement(name = "TIPO_DESTINAZIONE")
        protected String tipodestinazione;
        @XmlElement(name = "ESTREMI_DOC_USCITA")
        protected String estremidocuscita;
        @XmlElement(name = "DT_DOC_USCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtdocuscita;
        @XmlElement(name = "CAPO_ID", required = true)
        protected BigDecimal capoid;
        @XmlElement(name = "CODICE_CAPO")
        protected String codicecapo;
        @XmlElement(name = "CODICE_ELETTRONICO")
        protected String codiceelettronico;
        @XmlElement(name = "SESSO")
        protected String sesso;
        @XmlElement(name = "CODICE_RAZZA")
        protected String codicerazza;

        /**
         * Gets the value of the allevid property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getALLEVID() {
            return allevid;
        }

        /**
         * Sets the value of the allevid property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setALLEVID(BigDecimal value) {
            this.allevid = value;
        }

        /**
         * Gets the value of the aslazienda property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASLAZIENDA() {
            return aslazienda;
        }

        /**
         * Sets the value of the aslazienda property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASLAZIENDA(String value) {
            this.aslazienda = value;
        }

        /**
         * Gets the value of the codiceazienda property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEAZIENDA() {
            return codiceazienda;
        }

        /**
         * Sets the value of the codiceazienda property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEAZIENDA(String value) {
            this.codiceazienda = value;
        }

        /**
         * Gets the value of the allevidfiscale property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getALLEVIDFISCALE() {
            return allevidfiscale;
        }

        /**
         * Sets the value of the allevidfiscale property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setALLEVIDFISCALE(String value) {
            this.allevidfiscale = value;
        }

        /**
         * Gets the value of the specodice property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECODICE() {
            return specodice;
        }

        /**
         * Sets the value of the specodice property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECODICE(String value) {
            this.specodice = value;
        }

        /**
         * Gets the value of the cuaadetentore property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCUAADETENTORE() {
            return cuaadetentore;
        }

        /**
         * Sets the value of the cuaadetentore property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCUAADETENTORE(String value) {
            this.cuaadetentore = value;
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
         * Gets the value of the comunepascolo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCOMUNEPASCOLO() {
            return comunepascolo;
        }

        /**
         * Sets the value of the comunepascolo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCOMUNEPASCOLO(String value) {
            this.comunepascolo = value;
        }

        /**
         * Gets the value of the aslpascolo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getASLPASCOLO() {
            return aslpascolo;
        }

        /**
         * Sets the value of the aslpascolo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setASLPASCOLO(String value) {
            this.aslpascolo = value;
        }

        /**
         * Gets the value of the dtingressoalpascolo property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINGRESSOALPASCOLO() {
            return dtingressoalpascolo;
        }

        /**
         * Sets the value of the dtingressoalpascolo property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINGRESSOALPASCOLO(XMLGregorianCalendar value) {
            this.dtingressoalpascolo = value;
        }

        /**
         * Gets the value of the dtrientrodapascolo property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTRIENTRODAPASCOLO() {
            return dtrientrodapascolo;
        }

        /**
         * Sets the value of the dtrientrodapascolo property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTRIENTRODAPASCOLO(XMLGregorianCalendar value) {
            this.dtrientrodapascolo = value;
        }

        /**
         * Gets the value of the tipoprovenienza property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOPROVENIENZA() {
            return tipoprovenienza;
        }

        /**
         * Sets the value of the tipoprovenienza property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOPROVENIENZA(String value) {
            this.tipoprovenienza = value;
        }

        /**
         * Gets the value of the estremidocingresso property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getESTREMIDOCINGRESSO() {
            return estremidocingresso;
        }

        /**
         * Sets the value of the estremidocingresso property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setESTREMIDOCINGRESSO(String value) {
            this.estremidocingresso = value;
        }

        /**
         * Gets the value of the dtdocingresso property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTDOCINGRESSO() {
            return dtdocingresso;
        }

        /**
         * Sets the value of the dtdocingresso property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTDOCINGRESSO(XMLGregorianCalendar value) {
            this.dtdocingresso = value;
        }

        /**
         * Gets the value of the tipodestinazione property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPODESTINAZIONE() {
            return tipodestinazione;
        }

        /**
         * Sets the value of the tipodestinazione property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPODESTINAZIONE(String value) {
            this.tipodestinazione = value;
        }

        /**
         * Gets the value of the estremidocuscita property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getESTREMIDOCUSCITA() {
            return estremidocuscita;
        }

        /**
         * Sets the value of the estremidocuscita property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setESTREMIDOCUSCITA(String value) {
            this.estremidocuscita = value;
        }

        /**
         * Gets the value of the dtdocuscita property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTDOCUSCITA() {
            return dtdocuscita;
        }

        /**
         * Sets the value of the dtdocuscita property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTDOCUSCITA(XMLGregorianCalendar value) {
            this.dtdocuscita = value;
        }

        /**
         * Gets the value of the capoid property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPOID() {
            return capoid;
        }

        /**
         * Sets the value of the capoid property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPOID(BigDecimal value) {
            this.capoid = value;
        }

        /**
         * Gets the value of the codicecapo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICECAPO() {
            return codicecapo;
        }

        /**
         * Sets the value of the codicecapo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICECAPO(String value) {
            this.codicecapo = value;
        }

        /**
         * Gets the value of the codiceelettronico property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEELETTRONICO() {
            return codiceelettronico;
        }

        /**
         * Sets the value of the codiceelettronico property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEELETTRONICO(String value) {
            this.codiceelettronico = value;
        }

        /**
         * Gets the value of the sesso property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSESSO() {
            return sesso;
        }

        /**
         * Sets the value of the sesso property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSESSO(String value) {
            this.sesso = value;
        }

        /**
         * Gets the value of the codicerazza property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICERAZZA() {
            return codicerazza;
        }

        /**
         * Sets the value of the codicerazza property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICERAZZA(String value) {
            this.codicerazza = value;
        }

    }

}

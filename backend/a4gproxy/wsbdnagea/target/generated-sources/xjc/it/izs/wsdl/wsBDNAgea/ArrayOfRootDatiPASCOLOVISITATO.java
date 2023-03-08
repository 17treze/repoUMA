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
 * <p>Classe Java per ArrayOfRootDatiPASCOLO_VISITATO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
         * Recupera il valore della proprietà pasid.
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
         * Imposta il valore della proprietà pasid.
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
         * Recupera il valore della proprietà codicepascolo.
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
         * Imposta il valore della proprietà codicepascolo.
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
         * Recupera il valore della proprietà denominazionepascolo.
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
         * Imposta il valore della proprietà denominazionepascolo.
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
         * Recupera il valore della proprietà latitudine.
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
         * Imposta il valore della proprietà latitudine.
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
         * Recupera il valore della proprietà longitudine.
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
         * Imposta il valore della proprietà longitudine.
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
         * Recupera il valore della proprietà fogliocatastale.
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
         * Imposta il valore della proprietà fogliocatastale.
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
         * Recupera il valore della proprietà particella.
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
         * Imposta il valore della proprietà particella.
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
         * Recupera il valore della proprietà comid.
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
         * Imposta il valore della proprietà comid.
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
         * Recupera il valore della proprietà comistat.
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
         * Imposta il valore della proprietà comistat.
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
         * Recupera il valore della proprietà comdescrizione.
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
         * Imposta il valore della proprietà comdescrizione.
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
         * Recupera il valore della proprietà prosigla.
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
         * Imposta il valore della proprietà prosigla.
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
         * Recupera il valore della proprietà aslid.
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
         * Imposta il valore della proprietà aslid.
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
         * Recupera il valore della proprietà aslcodice.
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
         * Imposta il valore della proprietà aslcodice.
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
         * Recupera il valore della proprietà asldenominazione.
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
         * Imposta il valore della proprietà asldenominazione.
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
         * Recupera il valore della proprietà localita.
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
         * Imposta il valore della proprietà localita.
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
         * Recupera il valore della proprietà sezione.
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
         * Imposta il valore della proprietà sezione.
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
         * Recupera il valore della proprietà subalterno.
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
         * Imposta il valore della proprietà subalterno.
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
         * Recupera il valore della proprietà respidfiscale.
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
         * Imposta il valore della proprietà respidfiscale.
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

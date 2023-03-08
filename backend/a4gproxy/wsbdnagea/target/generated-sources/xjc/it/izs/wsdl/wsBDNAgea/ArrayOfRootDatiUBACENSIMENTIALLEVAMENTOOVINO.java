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
 * <p>Classe Java per ArrayOfRootDatiUBA_CENSIMENTI_ALLEVAMENTO_OVINO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiUBA_CENSIMENTI_ALLEVAMENTO_OVINO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UBA_CENSIMENTI_ALLEVAMENTO_OVINO" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CENSIMENTO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_FEMMINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_ANIMALI_IDENTIFICATI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_MASCHI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="TOTALE_ANIMALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_OVINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPRINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_ANIMALI_IDENT_PARTITA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_SUP_SEIMESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CAPI_INF_SEIMESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_BECCHI_ARIETI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_FEMMINE_ADULTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_BECCHI_LIBRO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_FEMMINE_ADULTE_LIBRO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_RIMONTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_ALTRO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_CENSIMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiUBA_CENSIMENTI_ALLEVAMENTO_OVINO", propOrder = {
    "ubacensimentiallevamentoovino"
})
public class ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO {

    @XmlElement(name = "UBA_CENSIMENTI_ALLEVAMENTO_OVINO")
    protected List<ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO.UBACENSIMENTIALLEVAMENTOOVINO> ubacensimentiallevamentoovino;

    /**
     * Gets the value of the ubacensimentiallevamentoovino property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ubacensimentiallevamentoovino property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUBACENSIMENTIALLEVAMENTOOVINO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO.UBACENSIMENTIALLEVAMENTOOVINO }
     * 
     * 
     */
    public List<ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO.UBACENSIMENTIALLEVAMENTOOVINO> getUBACENSIMENTIALLEVAMENTOOVINO() {
        if (ubacensimentiallevamentoovino == null) {
            ubacensimentiallevamentoovino = new ArrayList<ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO.UBACENSIMENTIALLEVAMENTOOVINO>();
        }
        return this.ubacensimentiallevamentoovino;
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
     *         &lt;element name="CENSIMENTO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="P_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_PROP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_FISCALE_DETE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_FEMMINE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_ANIMALI_IDENTIFICATI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_MASCHI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="TOTALE_ANIMALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_OVINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPRINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_ANIMALI_IDENT_PARTITA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_SUP_SEIMESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CAPI_INF_SEIMESI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_BECCHI_ARIETI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_FEMMINE_ADULTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_BECCHI_LIBRO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_FEMMINE_ADULTE_LIBRO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_RIMONTE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_ALTRO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DATA_CENSIMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
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
        "censimentoid",
        "pallevid",
        "aziendacodice",
        "codfiscaleprop",
        "codfiscaledete",
        "speciecodice",
        "speciedescrizione",
        "numfemmine",
        "numanimaliidentificati",
        "nummaschi",
        "totaleanimali",
        "numovini",
        "numcaprini",
        "numanimaliidentpartita",
        "capisupseimesi",
        "capiinfseimesi",
        "numbecchiarieti",
        "numfemmineadulte",
        "numbecchilibro",
        "numfemmineadultelibro",
        "numrimonte",
        "numaltro",
        "datacensimento"
    })
    public static class UBACENSIMENTIALLEVAMENTOOVINO {

        @XmlElement(name = "CENSIMENTO_ID", required = true)
        protected BigDecimal censimentoid;
        @XmlElement(name = "P_ALLEV_ID", required = true)
        protected BigDecimal pallevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "COD_FISCALE_PROP")
        protected String codfiscaleprop;
        @XmlElement(name = "COD_FISCALE_DETE")
        protected String codfiscaledete;
        @XmlElement(name = "SPECIE_CODICE")
        protected String speciecodice;
        @XmlElement(name = "SPECIE_DESCRIZIONE")
        protected String speciedescrizione;
        @XmlElement(name = "NUM_FEMMINE")
        protected BigDecimal numfemmine;
        @XmlElement(name = "NUM_ANIMALI_IDENTIFICATI")
        protected BigDecimal numanimaliidentificati;
        @XmlElement(name = "NUM_MASCHI")
        protected BigDecimal nummaschi;
        @XmlElement(name = "TOTALE_ANIMALI")
        protected BigDecimal totaleanimali;
        @XmlElement(name = "NUM_OVINI")
        protected BigDecimal numovini;
        @XmlElement(name = "NUM_CAPRINI")
        protected BigDecimal numcaprini;
        @XmlElement(name = "NUM_ANIMALI_IDENT_PARTITA")
        protected BigDecimal numanimaliidentpartita;
        @XmlElement(name = "CAPI_SUP_SEIMESI")
        protected BigDecimal capisupseimesi;
        @XmlElement(name = "CAPI_INF_SEIMESI")
        protected BigDecimal capiinfseimesi;
        @XmlElement(name = "NUM_BECCHI_ARIETI")
        protected BigDecimal numbecchiarieti;
        @XmlElement(name = "NUM_FEMMINE_ADULTE")
        protected BigDecimal numfemmineadulte;
        @XmlElement(name = "NUM_BECCHI_LIBRO")
        protected BigDecimal numbecchilibro;
        @XmlElement(name = "NUM_FEMMINE_ADULTE_LIBRO")
        protected BigDecimal numfemmineadultelibro;
        @XmlElement(name = "NUM_RIMONTE")
        protected BigDecimal numrimonte;
        @XmlElement(name = "NUM_ALTRO")
        protected BigDecimal numaltro;
        @XmlElement(name = "DATA_CENSIMENTO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datacensimento;

        /**
         * Recupera il valore della proprietà censimentoid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCENSIMENTOID() {
            return censimentoid;
        }

        /**
         * Imposta il valore della proprietà censimentoid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCENSIMENTOID(BigDecimal value) {
            this.censimentoid = value;
        }

        /**
         * Recupera il valore della proprietà pallevid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPALLEVID() {
            return pallevid;
        }

        /**
         * Imposta il valore della proprietà pallevid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPALLEVID(BigDecimal value) {
            this.pallevid = value;
        }

        /**
         * Recupera il valore della proprietà aziendacodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAZIENDACODICE() {
            return aziendacodice;
        }

        /**
         * Imposta il valore della proprietà aziendacodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAZIENDACODICE(String value) {
            this.aziendacodice = value;
        }

        /**
         * Recupera il valore della proprietà codfiscaleprop.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODFISCALEPROP() {
            return codfiscaleprop;
        }

        /**
         * Imposta il valore della proprietà codfiscaleprop.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODFISCALEPROP(String value) {
            this.codfiscaleprop = value;
        }

        /**
         * Recupera il valore della proprietà codfiscaledete.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODFISCALEDETE() {
            return codfiscaledete;
        }

        /**
         * Imposta il valore della proprietà codfiscaledete.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODFISCALEDETE(String value) {
            this.codfiscaledete = value;
        }

        /**
         * Recupera il valore della proprietà speciecodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECIECODICE() {
            return speciecodice;
        }

        /**
         * Imposta il valore della proprietà speciecodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECIECODICE(String value) {
            this.speciecodice = value;
        }

        /**
         * Recupera il valore della proprietà speciedescrizione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECIEDESCRIZIONE() {
            return speciedescrizione;
        }

        /**
         * Imposta il valore della proprietà speciedescrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECIEDESCRIZIONE(String value) {
            this.speciedescrizione = value;
        }

        /**
         * Recupera il valore della proprietà numfemmine.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMFEMMINE() {
            return numfemmine;
        }

        /**
         * Imposta il valore della proprietà numfemmine.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMFEMMINE(BigDecimal value) {
            this.numfemmine = value;
        }

        /**
         * Recupera il valore della proprietà numanimaliidentificati.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMANIMALIIDENTIFICATI() {
            return numanimaliidentificati;
        }

        /**
         * Imposta il valore della proprietà numanimaliidentificati.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMANIMALIIDENTIFICATI(BigDecimal value) {
            this.numanimaliidentificati = value;
        }

        /**
         * Recupera il valore della proprietà nummaschi.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMMASCHI() {
            return nummaschi;
        }

        /**
         * Imposta il valore della proprietà nummaschi.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMMASCHI(BigDecimal value) {
            this.nummaschi = value;
        }

        /**
         * Recupera il valore della proprietà totaleanimali.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTOTALEANIMALI() {
            return totaleanimali;
        }

        /**
         * Imposta il valore della proprietà totaleanimali.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTOTALEANIMALI(BigDecimal value) {
            this.totaleanimali = value;
        }

        /**
         * Recupera il valore della proprietà numovini.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMOVINI() {
            return numovini;
        }

        /**
         * Imposta il valore della proprietà numovini.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMOVINI(BigDecimal value) {
            this.numovini = value;
        }

        /**
         * Recupera il valore della proprietà numcaprini.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPRINI() {
            return numcaprini;
        }

        /**
         * Imposta il valore della proprietà numcaprini.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPRINI(BigDecimal value) {
            this.numcaprini = value;
        }

        /**
         * Recupera il valore della proprietà numanimaliidentpartita.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMANIMALIIDENTPARTITA() {
            return numanimaliidentpartita;
        }

        /**
         * Imposta il valore della proprietà numanimaliidentpartita.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMANIMALIIDENTPARTITA(BigDecimal value) {
            this.numanimaliidentpartita = value;
        }

        /**
         * Recupera il valore della proprietà capisupseimesi.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPISUPSEIMESI() {
            return capisupseimesi;
        }

        /**
         * Imposta il valore della proprietà capisupseimesi.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPISUPSEIMESI(BigDecimal value) {
            this.capisupseimesi = value;
        }

        /**
         * Recupera il valore della proprietà capiinfseimesi.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAPIINFSEIMESI() {
            return capiinfseimesi;
        }

        /**
         * Imposta il valore della proprietà capiinfseimesi.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAPIINFSEIMESI(BigDecimal value) {
            this.capiinfseimesi = value;
        }

        /**
         * Recupera il valore della proprietà numbecchiarieti.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMBECCHIARIETI() {
            return numbecchiarieti;
        }

        /**
         * Imposta il valore della proprietà numbecchiarieti.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMBECCHIARIETI(BigDecimal value) {
            this.numbecchiarieti = value;
        }

        /**
         * Recupera il valore della proprietà numfemmineadulte.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMFEMMINEADULTE() {
            return numfemmineadulte;
        }

        /**
         * Imposta il valore della proprietà numfemmineadulte.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMFEMMINEADULTE(BigDecimal value) {
            this.numfemmineadulte = value;
        }

        /**
         * Recupera il valore della proprietà numbecchilibro.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMBECCHILIBRO() {
            return numbecchilibro;
        }

        /**
         * Imposta il valore della proprietà numbecchilibro.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMBECCHILIBRO(BigDecimal value) {
            this.numbecchilibro = value;
        }

        /**
         * Recupera il valore della proprietà numfemmineadultelibro.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMFEMMINEADULTELIBRO() {
            return numfemmineadultelibro;
        }

        /**
         * Imposta il valore della proprietà numfemmineadultelibro.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMFEMMINEADULTELIBRO(BigDecimal value) {
            this.numfemmineadultelibro = value;
        }

        /**
         * Recupera il valore della proprietà numrimonte.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMRIMONTE() {
            return numrimonte;
        }

        /**
         * Imposta il valore della proprietà numrimonte.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMRIMONTE(BigDecimal value) {
            this.numrimonte = value;
        }

        /**
         * Recupera il valore della proprietà numaltro.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMALTRO() {
            return numaltro;
        }

        /**
         * Imposta il valore della proprietà numaltro.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMALTRO(BigDecimal value) {
            this.numaltro = value;
        }

        /**
         * Recupera il valore della proprietà datacensimento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATACENSIMENTO() {
            return datacensimento;
        }

        /**
         * Imposta il valore della proprietà datacensimento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATACENSIMENTO(XMLGregorianCalendar value) {
            this.datacensimento = value;
        }

    }

}

//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:57 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.dsequiregistripascolig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element name="REGISTRI_PASCOLI"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="REGPAS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="PASSAPORTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="IDENT_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_UELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="RAZZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DENOMINAZIONE_RAZZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="INGRESSO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_ID_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_AZIENDA_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_ID_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_SPECIE_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INGRESSO_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_RIENTRO_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DETEN_PAS_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "registripascoli"
})
@XmlRootElement(name = "dsEQUI_REGISTRI_PASCOLI_G")
public class DsEQUIREGISTRIPASCOLIG {

    @XmlElement(name = "REGISTRI_PASCOLI")
    protected List<DsEQUIREGISTRIPASCOLIG.REGISTRIPASCOLI> registripascoli;

    /**
     * Gets the value of the registripascoli property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registripascoli property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getREGISTRIPASCOLI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DsEQUIREGISTRIPASCOLIG.REGISTRIPASCOLI }
     * 
     * 
     */
    public List<DsEQUIREGISTRIPASCOLIG.REGISTRIPASCOLI> getREGISTRIPASCOLI() {
        if (registripascoli == null) {
            registripascoli = new ArrayList<DsEQUIREGISTRIPASCOLIG.REGISTRIPASCOLI>();
        }
        return this.registripascoli;
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
     *         &lt;element name="REGPAS_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="PASSAPORTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="IDENT_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_UELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="RAZZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DENOMINAZIONE_RAZZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="INGRESSO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_ID_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_AZIENDA_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_ID_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_SPECIE_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INGRESSO_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_RIENTRO_PASCOLO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DETEN_PAS_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "regpasid",
        "capoid",
        "passaporto",
        "codiceelettronico",
        "identnome",
        "codiceueln",
        "dtnascita",
        "razzaid",
        "denominazionerazza",
        "ingressoid",
        "allevidprovenienza",
        "allevidfiscaleprovenienza",
        "aziendaidprovenienza",
        "codiceaziendaprovenienza",
        "specieidprovenienza",
        "codicespecieprovenienza",
        "dtingressopascolo",
        "dtrientropascolo",
        "detenpasidfiscale"
    })
    public static class REGISTRIPASCOLI {

        @XmlElement(name = "REGPAS_ID", required = true)
        protected BigDecimal regpasid;
        @XmlElement(name = "CAPO_ID")
        protected BigDecimal capoid;
        @XmlElement(name = "PASSAPORTO")
        protected String passaporto;
        @XmlElement(name = "CODICE_ELETTRONICO")
        protected String codiceelettronico;
        @XmlElement(name = "IDENT_NOME")
        protected String identnome;
        @XmlElement(name = "CODICE_UELN")
        protected String codiceueln;
        @XmlElement(name = "DT_NASCITA")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtnascita;
        @XmlElement(name = "RAZZA_ID")
        protected BigDecimal razzaid;
        @XmlElement(name = "DENOMINAZIONE_RAZZA")
        protected String denominazionerazza;
        @XmlElement(name = "INGRESSO_ID")
        protected BigDecimal ingressoid;
        @XmlElement(name = "ALLEV_ID_PROVENIENZA")
        protected BigDecimal allevidprovenienza;
        @XmlElement(name = "ALLEV_ID_FISCALE_PROVENIENZA")
        protected String allevidfiscaleprovenienza;
        @XmlElement(name = "AZIENDA_ID_PROVENIENZA")
        protected BigDecimal aziendaidprovenienza;
        @XmlElement(name = "CODICE_AZIENDA_PROVENIENZA")
        protected String codiceaziendaprovenienza;
        @XmlElement(name = "SPECIE_ID_PROVENIENZA")
        protected BigDecimal specieidprovenienza;
        @XmlElement(name = "CODICE_SPECIE_PROVENIENZA")
        protected String codicespecieprovenienza;
        @XmlElement(name = "DT_INGRESSO_PASCOLO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtingressopascolo;
        @XmlElement(name = "DT_RIENTRO_PASCOLO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtrientropascolo;
        @XmlElement(name = "DETEN_PAS_ID_FISCALE")
        protected String detenpasidfiscale;

        /**
         * Recupera il valore della proprietà regpasid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getREGPASID() {
            return regpasid;
        }

        /**
         * Imposta il valore della proprietà regpasid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setREGPASID(BigDecimal value) {
            this.regpasid = value;
        }

        /**
         * Recupera il valore della proprietà capoid.
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
         * Imposta il valore della proprietà capoid.
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
         * Recupera il valore della proprietà passaporto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPASSAPORTO() {
            return passaporto;
        }

        /**
         * Imposta il valore della proprietà passaporto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPASSAPORTO(String value) {
            this.passaporto = value;
        }

        /**
         * Recupera il valore della proprietà codiceelettronico.
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
         * Imposta il valore della proprietà codiceelettronico.
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
         * Recupera il valore della proprietà identnome.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIDENTNOME() {
            return identnome;
        }

        /**
         * Imposta il valore della proprietà identnome.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIDENTNOME(String value) {
            this.identnome = value;
        }

        /**
         * Recupera il valore della proprietà codiceueln.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEUELN() {
            return codiceueln;
        }

        /**
         * Imposta il valore della proprietà codiceueln.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEUELN(String value) {
            this.codiceueln = value;
        }

        /**
         * Recupera il valore della proprietà dtnascita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTNASCITA() {
            return dtnascita;
        }

        /**
         * Imposta il valore della proprietà dtnascita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTNASCITA(XMLGregorianCalendar value) {
            this.dtnascita = value;
        }

        /**
         * Recupera il valore della proprietà razzaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getRAZZAID() {
            return razzaid;
        }

        /**
         * Imposta il valore della proprietà razzaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setRAZZAID(BigDecimal value) {
            this.razzaid = value;
        }

        /**
         * Recupera il valore della proprietà denominazionerazza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDENOMINAZIONERAZZA() {
            return denominazionerazza;
        }

        /**
         * Imposta il valore della proprietà denominazionerazza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDENOMINAZIONERAZZA(String value) {
            this.denominazionerazza = value;
        }

        /**
         * Recupera il valore della proprietà ingressoid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getINGRESSOID() {
            return ingressoid;
        }

        /**
         * Imposta il valore della proprietà ingressoid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setINGRESSOID(BigDecimal value) {
            this.ingressoid = value;
        }

        /**
         * Recupera il valore della proprietà allevidprovenienza.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getALLEVIDPROVENIENZA() {
            return allevidprovenienza;
        }

        /**
         * Imposta il valore della proprietà allevidprovenienza.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setALLEVIDPROVENIENZA(BigDecimal value) {
            this.allevidprovenienza = value;
        }

        /**
         * Recupera il valore della proprietà allevidfiscaleprovenienza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getALLEVIDFISCALEPROVENIENZA() {
            return allevidfiscaleprovenienza;
        }

        /**
         * Imposta il valore della proprietà allevidfiscaleprovenienza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setALLEVIDFISCALEPROVENIENZA(String value) {
            this.allevidfiscaleprovenienza = value;
        }

        /**
         * Recupera il valore della proprietà aziendaidprovenienza.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getAZIENDAIDPROVENIENZA() {
            return aziendaidprovenienza;
        }

        /**
         * Imposta il valore della proprietà aziendaidprovenienza.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setAZIENDAIDPROVENIENZA(BigDecimal value) {
            this.aziendaidprovenienza = value;
        }

        /**
         * Recupera il valore della proprietà codiceaziendaprovenienza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEAZIENDAPROVENIENZA() {
            return codiceaziendaprovenienza;
        }

        /**
         * Imposta il valore della proprietà codiceaziendaprovenienza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEAZIENDAPROVENIENZA(String value) {
            this.codiceaziendaprovenienza = value;
        }

        /**
         * Recupera il valore della proprietà specieidprovenienza.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getSPECIEIDPROVENIENZA() {
            return specieidprovenienza;
        }

        /**
         * Imposta il valore della proprietà specieidprovenienza.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setSPECIEIDPROVENIENZA(BigDecimal value) {
            this.specieidprovenienza = value;
        }

        /**
         * Recupera il valore della proprietà codicespecieprovenienza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICESPECIEPROVENIENZA() {
            return codicespecieprovenienza;
        }

        /**
         * Imposta il valore della proprietà codicespecieprovenienza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICESPECIEPROVENIENZA(String value) {
            this.codicespecieprovenienza = value;
        }

        /**
         * Recupera il valore della proprietà dtingressopascolo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINGRESSOPASCOLO() {
            return dtingressopascolo;
        }

        /**
         * Imposta il valore della proprietà dtingressopascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINGRESSOPASCOLO(XMLGregorianCalendar value) {
            this.dtingressopascolo = value;
        }

        /**
         * Recupera il valore della proprietà dtrientropascolo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTRIENTROPASCOLO() {
            return dtrientropascolo;
        }

        /**
         * Imposta il valore della proprietà dtrientropascolo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTRIENTROPASCOLO(XMLGregorianCalendar value) {
            this.dtrientropascolo = value;
        }

        /**
         * Recupera il valore della proprietà detenpasidfiscale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDETENPASIDFISCALE() {
            return detenpasidfiscale;
        }

        /**
         * Imposta il valore della proprietà detenpasidfiscale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDETENPASIDFISCALE(String value) {
            this.detenpasidfiscale = value;
        }

    }

}

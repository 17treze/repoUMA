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
 * <p>Classe Java per ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_IR complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_IR"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CONTROLLO_ALLEVAMENTO_IR" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *                   &lt;element name="CODICE_INTERNO_REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_ASL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOMINAZIONE_ASL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_FISCALE_PROPRIETARIO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_FISCALE_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_AZIENDALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_FISCALE_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_ALLEVATA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOMINAZIONE_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_APERTURA_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_CHIUSURA_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="INDIRIZZO_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SIGLA_PROVINCIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="CRITERIO_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ORGANISMO_CONTROLLORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="STATO_PROCEDIMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NOTE_CONTROLLORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NOTE_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_ESITO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_CONDIZIONALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_CHIUSURA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_CONTROLLATI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_PRESENTI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_IR", propOrder = {
    "controlloallevamentoir"
})
public class ArrayOfRootDatiCONTROLLOALLEVAMENTOIR {

    @XmlElement(name = "CONTROLLO_ALLEVAMENTO_IR")
    protected List<ArrayOfRootDatiCONTROLLOALLEVAMENTOIR.CONTROLLOALLEVAMENTOIR> controlloallevamentoir;

    /**
     * Gets the value of the controlloallevamentoir property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the controlloallevamentoir property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCONTROLLOALLEVAMENTOIR().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiCONTROLLOALLEVAMENTOIR.CONTROLLOALLEVAMENTOIR }
     * 
     * 
     */
    public List<ArrayOfRootDatiCONTROLLOALLEVAMENTOIR.CONTROLLOALLEVAMENTOIR> getCONTROLLOALLEVAMENTOIR() {
        if (controlloallevamentoir == null) {
            controlloallevamentoir = new ArrayList<ArrayOfRootDatiCONTROLLOALLEVAMENTOIR.CONTROLLOALLEVAMENTOIR>();
        }
        return this.controlloallevamentoir;
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
     *         &lt;element name="CA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
     *         &lt;element name="CODICE_INTERNO_REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="REGIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_ASL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOMINAZIONE_ASL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_FISCALE_PROPRIETARIO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_FISCALE_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_AZIENDALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_FISCALE_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_ALLEVATA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOMINAZIONE_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_APERTURA_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DATA_CHIUSURA_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="INDIRIZZO_ALLEVAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COMUNE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SIGLA_PROVINCIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="CRITERIO_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ORGANISMO_CONTROLLORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="STATO_PROCEDIMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NOTE_CONTROLLORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NOTE_DETENTORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_ESITO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_CONDIZIONALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_CHIUSURA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_CONTROLLATI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_PRESENTI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "caid",
        "codiceinternoregione",
        "regione",
        "codiceasl",
        "denominazioneasl",
        "codicefiscaleproprietario",
        "codicefiscaledetentore",
        "codiceaziendale",
        "codicefiscaleallevamento",
        "specieallevata",
        "denominazioneallevamento",
        "dataaperturaallevamento",
        "datachiusuraallevamento",
        "indirizzoallevamento",
        "cap",
        "comune",
        "siglaprovincia",
        "datacontrollo",
        "criteriocontrollo",
        "organismocontrollore",
        "statoprocedimento",
        "notecontrollore",
        "notedetentore",
        "flagesito",
        "flagcondizionalita",
        "datachiusura",
        "numcapicontrollati",
        "numcapipresenti"
    })
    public static class CONTROLLOALLEVAMENTOIR {

        @XmlElement(name = "CA_ID", required = true)
        protected BigDecimal caid;
        @XmlElement(name = "CODICE_INTERNO_REGIONE")
        protected String codiceinternoregione;
        @XmlElement(name = "REGIONE")
        protected String regione;
        @XmlElement(name = "CODICE_ASL")
        protected String codiceasl;
        @XmlElement(name = "DENOMINAZIONE_ASL")
        protected String denominazioneasl;
        @XmlElement(name = "CODICE_FISCALE_PROPRIETARIO")
        protected String codicefiscaleproprietario;
        @XmlElement(name = "CODICE_FISCALE_DETENTORE")
        protected String codicefiscaledetentore;
        @XmlElement(name = "CODICE_AZIENDALE")
        protected String codiceaziendale;
        @XmlElement(name = "CODICE_FISCALE_ALLEVAMENTO")
        protected String codicefiscaleallevamento;
        @XmlElement(name = "SPECIE_ALLEVATA")
        protected String specieallevata;
        @XmlElement(name = "DENOMINAZIONE_ALLEVAMENTO")
        protected String denominazioneallevamento;
        @XmlElement(name = "DATA_APERTURA_ALLEVAMENTO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataaperturaallevamento;
        @XmlElement(name = "DATA_CHIUSURA_ALLEVAMENTO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datachiusuraallevamento;
        @XmlElement(name = "INDIRIZZO_ALLEVAMENTO")
        protected String indirizzoallevamento;
        @XmlElement(name = "CAP")
        protected String cap;
        @XmlElement(name = "COMUNE")
        protected String comune;
        @XmlElement(name = "SIGLA_PROVINCIA")
        protected String siglaprovincia;
        @XmlElement(name = "DATA_CONTROLLO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datacontrollo;
        @XmlElement(name = "CRITERIO_CONTROLLO")
        protected String criteriocontrollo;
        @XmlElement(name = "ORGANISMO_CONTROLLORE")
        protected String organismocontrollore;
        @XmlElement(name = "STATO_PROCEDIMENTO")
        protected String statoprocedimento;
        @XmlElement(name = "NOTE_CONTROLLORE")
        protected String notecontrollore;
        @XmlElement(name = "NOTE_DETENTORE")
        protected String notedetentore;
        @XmlElement(name = "FLAG_ESITO")
        protected String flagesito;
        @XmlElement(name = "FLAG_CONDIZIONALITA")
        protected String flagcondizionalita;
        @XmlElement(name = "DATA_CHIUSURA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datachiusura;
        @XmlElement(name = "NUM_CAPI_CONTROLLATI")
        protected String numcapicontrollati;
        @XmlElement(name = "NUM_CAPI_PRESENTI")
        protected String numcapipresenti;

        /**
         * Recupera il valore della proprietà caid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getCAID() {
            return caid;
        }

        /**
         * Imposta il valore della proprietà caid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setCAID(BigDecimal value) {
            this.caid = value;
        }

        /**
         * Recupera il valore della proprietà codiceinternoregione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEINTERNOREGIONE() {
            return codiceinternoregione;
        }

        /**
         * Imposta il valore della proprietà codiceinternoregione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEINTERNOREGIONE(String value) {
            this.codiceinternoregione = value;
        }

        /**
         * Recupera il valore della proprietà regione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getREGIONE() {
            return regione;
        }

        /**
         * Imposta il valore della proprietà regione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setREGIONE(String value) {
            this.regione = value;
        }

        /**
         * Recupera il valore della proprietà codiceasl.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEASL() {
            return codiceasl;
        }

        /**
         * Imposta il valore della proprietà codiceasl.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEASL(String value) {
            this.codiceasl = value;
        }

        /**
         * Recupera il valore della proprietà denominazioneasl.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDENOMINAZIONEASL() {
            return denominazioneasl;
        }

        /**
         * Imposta il valore della proprietà denominazioneasl.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDENOMINAZIONEASL(String value) {
            this.denominazioneasl = value;
        }

        /**
         * Recupera il valore della proprietà codicefiscaleproprietario.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEFISCALEPROPRIETARIO() {
            return codicefiscaleproprietario;
        }

        /**
         * Imposta il valore della proprietà codicefiscaleproprietario.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEFISCALEPROPRIETARIO(String value) {
            this.codicefiscaleproprietario = value;
        }

        /**
         * Recupera il valore della proprietà codicefiscaledetentore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEFISCALEDETENTORE() {
            return codicefiscaledetentore;
        }

        /**
         * Imposta il valore della proprietà codicefiscaledetentore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEFISCALEDETENTORE(String value) {
            this.codicefiscaledetentore = value;
        }

        /**
         * Recupera il valore della proprietà codiceaziendale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEAZIENDALE() {
            return codiceaziendale;
        }

        /**
         * Imposta il valore della proprietà codiceaziendale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEAZIENDALE(String value) {
            this.codiceaziendale = value;
        }

        /**
         * Recupera il valore della proprietà codicefiscaleallevamento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEFISCALEALLEVAMENTO() {
            return codicefiscaleallevamento;
        }

        /**
         * Imposta il valore della proprietà codicefiscaleallevamento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEFISCALEALLEVAMENTO(String value) {
            this.codicefiscaleallevamento = value;
        }

        /**
         * Recupera il valore della proprietà specieallevata.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECIEALLEVATA() {
            return specieallevata;
        }

        /**
         * Imposta il valore della proprietà specieallevata.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECIEALLEVATA(String value) {
            this.specieallevata = value;
        }

        /**
         * Recupera il valore della proprietà denominazioneallevamento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDENOMINAZIONEALLEVAMENTO() {
            return denominazioneallevamento;
        }

        /**
         * Imposta il valore della proprietà denominazioneallevamento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDENOMINAZIONEALLEVAMENTO(String value) {
            this.denominazioneallevamento = value;
        }

        /**
         * Recupera il valore della proprietà dataaperturaallevamento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAAPERTURAALLEVAMENTO() {
            return dataaperturaallevamento;
        }

        /**
         * Imposta il valore della proprietà dataaperturaallevamento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAAPERTURAALLEVAMENTO(XMLGregorianCalendar value) {
            this.dataaperturaallevamento = value;
        }

        /**
         * Recupera il valore della proprietà datachiusuraallevamento.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATACHIUSURAALLEVAMENTO() {
            return datachiusuraallevamento;
        }

        /**
         * Imposta il valore della proprietà datachiusuraallevamento.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATACHIUSURAALLEVAMENTO(XMLGregorianCalendar value) {
            this.datachiusuraallevamento = value;
        }

        /**
         * Recupera il valore della proprietà indirizzoallevamento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getINDIRIZZOALLEVAMENTO() {
            return indirizzoallevamento;
        }

        /**
         * Imposta il valore della proprietà indirizzoallevamento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setINDIRIZZOALLEVAMENTO(String value) {
            this.indirizzoallevamento = value;
        }

        /**
         * Recupera il valore della proprietà cap.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCAP() {
            return cap;
        }

        /**
         * Imposta il valore della proprietà cap.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCAP(String value) {
            this.cap = value;
        }

        /**
         * Recupera il valore della proprietà comune.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCOMUNE() {
            return comune;
        }

        /**
         * Imposta il valore della proprietà comune.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCOMUNE(String value) {
            this.comune = value;
        }

        /**
         * Recupera il valore della proprietà siglaprovincia.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSIGLAPROVINCIA() {
            return siglaprovincia;
        }

        /**
         * Imposta il valore della proprietà siglaprovincia.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSIGLAPROVINCIA(String value) {
            this.siglaprovincia = value;
        }

        /**
         * Recupera il valore della proprietà datacontrollo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATACONTROLLO() {
            return datacontrollo;
        }

        /**
         * Imposta il valore della proprietà datacontrollo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATACONTROLLO(XMLGregorianCalendar value) {
            this.datacontrollo = value;
        }

        /**
         * Recupera il valore della proprietà criteriocontrollo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCRITERIOCONTROLLO() {
            return criteriocontrollo;
        }

        /**
         * Imposta il valore della proprietà criteriocontrollo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCRITERIOCONTROLLO(String value) {
            this.criteriocontrollo = value;
        }

        /**
         * Recupera il valore della proprietà organismocontrollore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getORGANISMOCONTROLLORE() {
            return organismocontrollore;
        }

        /**
         * Imposta il valore della proprietà organismocontrollore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setORGANISMOCONTROLLORE(String value) {
            this.organismocontrollore = value;
        }

        /**
         * Recupera il valore della proprietà statoprocedimento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSTATOPROCEDIMENTO() {
            return statoprocedimento;
        }

        /**
         * Imposta il valore della proprietà statoprocedimento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSTATOPROCEDIMENTO(String value) {
            this.statoprocedimento = value;
        }

        /**
         * Recupera il valore della proprietà notecontrollore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNOTECONTROLLORE() {
            return notecontrollore;
        }

        /**
         * Imposta il valore della proprietà notecontrollore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNOTECONTROLLORE(String value) {
            this.notecontrollore = value;
        }

        /**
         * Recupera il valore della proprietà notedetentore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNOTEDETENTORE() {
            return notedetentore;
        }

        /**
         * Imposta il valore della proprietà notedetentore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNOTEDETENTORE(String value) {
            this.notedetentore = value;
        }

        /**
         * Recupera il valore della proprietà flagesito.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGESITO() {
            return flagesito;
        }

        /**
         * Imposta il valore della proprietà flagesito.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGESITO(String value) {
            this.flagesito = value;
        }

        /**
         * Recupera il valore della proprietà flagcondizionalita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGCONDIZIONALITA() {
            return flagcondizionalita;
        }

        /**
         * Imposta il valore della proprietà flagcondizionalita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGCONDIZIONALITA(String value) {
            this.flagcondizionalita = value;
        }

        /**
         * Recupera il valore della proprietà datachiusura.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATACHIUSURA() {
            return datachiusura;
        }

        /**
         * Imposta il valore della proprietà datachiusura.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATACHIUSURA(XMLGregorianCalendar value) {
            this.datachiusura = value;
        }

        /**
         * Recupera il valore della proprietà numcapicontrollati.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMCAPICONTROLLATI() {
            return numcapicontrollati;
        }

        /**
         * Imposta il valore della proprietà numcapicontrollati.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMCAPICONTROLLATI(String value) {
            this.numcapicontrollati = value;
        }

        /**
         * Recupera il valore della proprietà numcapipresenti.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMCAPIPRESENTI() {
            return numcapipresenti;
        }

        /**
         * Imposta il valore della proprietà numcapipresenti.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMCAPIPRESENTI(String value) {
            this.numcapipresenti = value;
        }

    }

}

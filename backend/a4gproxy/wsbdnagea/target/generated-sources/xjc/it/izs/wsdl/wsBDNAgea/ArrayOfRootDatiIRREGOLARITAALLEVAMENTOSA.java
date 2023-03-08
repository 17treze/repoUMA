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
 * <p>Classe Java per ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_SA complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_SA"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IRREGOLARITA_ALLEVAMENTO_SA" maxOccurs="unbounded" minOccurs="0"&gt;
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
 *                   &lt;element name="TIPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DESCRIZIONE_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_SN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_COPIA_CHECK_LIST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_VERIFICA_CGO4" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_VERIFICA_CGO9" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="SECONDO_CONTROLLORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="VERBALE_INSERITO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="STATO_PROCEDIMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_RICH_PRESCRIZIONI_CGO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PRESCRIZIONI_RICHIESTE_CGO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_SCAD_PRESCRIZIONI_CGO4" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_RICH_PRESCRIZIONI_CGO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PRESCRIZIONI_RICHIESTE_CGO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_SCAD_PRESCRIZIONI_CGO9" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_PRESCRIZIONE_ESITO_CGO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_CONTROLLATI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_PRESENTI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_SA", propOrder = {
    "irregolaritaallevamentosa"
})
public class ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA {

    @XmlElement(name = "IRREGOLARITA_ALLEVAMENTO_SA")
    protected List<ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA.IRREGOLARITAALLEVAMENTOSA> irregolaritaallevamentosa;

    /**
     * Gets the value of the irregolaritaallevamentosa property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the irregolaritaallevamentosa property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIRREGOLARITAALLEVAMENTOSA().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA.IRREGOLARITAALLEVAMENTOSA }
     * 
     * 
     */
    public List<ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA.IRREGOLARITAALLEVAMENTOSA> getIRREGOLARITAALLEVAMENTOSA() {
        if (irregolaritaallevamentosa == null) {
            irregolaritaallevamentosa = new ArrayList<ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA.IRREGOLARITAALLEVAMENTOSA>();
        }
        return this.irregolaritaallevamentosa;
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
     *         &lt;element name="TIPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DESCRIZIONE_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_SN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_COPIA_CHECK_LIST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_VERIFICA_CGO4" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DATA_VERIFICA_CGO9" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="SECONDO_CONTROLLORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="VERBALE_INSERITO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="STATO_PROCEDIMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_RICH_PRESCRIZIONI_CGO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PRESCRIZIONI_RICHIESTE_CGO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_SCAD_PRESCRIZIONI_CGO4" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_RICH_PRESCRIZIONI_CGO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PRESCRIZIONI_RICHIESTE_CGO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DATA_SCAD_PRESCRIZIONI_CGO9" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_PRESCRIZIONE_ESITO_CGO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_CONTROLLATI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_PRESENTI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
        "tipo",
        "descrizionecontrollo",
        "flagsn",
        "flagcopiachecklist",
        "dataverificacgo4",
        "dataverificacgo9",
        "secondocontrollore",
        "verbaleinserito",
        "statoprocedimento",
        "flagrichprescrizionicgo4",
        "prescrizionirichiestecgo4",
        "datascadprescrizionicgo4",
        "flagrichprescrizionicgo9",
        "prescrizionirichiestecgo9",
        "datascadprescrizionicgo9",
        "flagprescrizioneesitocgo4",
        "numcapicontrollati",
        "numcapipresenti"
    })
    public static class IRREGOLARITAALLEVAMENTOSA {

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
        @XmlElement(name = "TIPO")
        protected String tipo;
        @XmlElement(name = "DESCRIZIONE_CONTROLLO")
        protected String descrizionecontrollo;
        @XmlElement(name = "FLAG_SN")
        protected String flagsn;
        @XmlElement(name = "FLAG_COPIA_CHECK_LIST")
        protected String flagcopiachecklist;
        @XmlElement(name = "DATA_VERIFICA_CGO4")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataverificacgo4;
        @XmlElement(name = "DATA_VERIFICA_CGO9")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataverificacgo9;
        @XmlElement(name = "SECONDO_CONTROLLORE")
        protected String secondocontrollore;
        @XmlElement(name = "VERBALE_INSERITO")
        protected String verbaleinserito;
        @XmlElement(name = "STATO_PROCEDIMENTO")
        protected String statoprocedimento;
        @XmlElement(name = "FLAG_RICH_PRESCRIZIONI_CGO4")
        protected String flagrichprescrizionicgo4;
        @XmlElement(name = "PRESCRIZIONI_RICHIESTE_CGO4")
        protected String prescrizionirichiestecgo4;
        @XmlElement(name = "DATA_SCAD_PRESCRIZIONI_CGO4")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datascadprescrizionicgo4;
        @XmlElement(name = "FLAG_RICH_PRESCRIZIONI_CGO9")
        protected String flagrichprescrizionicgo9;
        @XmlElement(name = "PRESCRIZIONI_RICHIESTE_CGO9")
        protected String prescrizionirichiestecgo9;
        @XmlElement(name = "DATA_SCAD_PRESCRIZIONI_CGO9")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datascadprescrizionicgo9;
        @XmlElement(name = "FLAG_PRESCRIZIONE_ESITO_CGO4")
        protected String flagprescrizioneesitocgo4;
        @XmlElement(name = "NUM_CAPI_CONTROLLATI")
        protected BigDecimal numcapicontrollati;
        @XmlElement(name = "NUM_CAPI_PRESENTI")
        protected BigDecimal numcapipresenti;

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
         * Recupera il valore della proprietà tipo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPO() {
            return tipo;
        }

        /**
         * Imposta il valore della proprietà tipo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPO(String value) {
            this.tipo = value;
        }

        /**
         * Recupera il valore della proprietà descrizionecontrollo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDESCRIZIONECONTROLLO() {
            return descrizionecontrollo;
        }

        /**
         * Imposta il valore della proprietà descrizionecontrollo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDESCRIZIONECONTROLLO(String value) {
            this.descrizionecontrollo = value;
        }

        /**
         * Recupera il valore della proprietà flagsn.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGSN() {
            return flagsn;
        }

        /**
         * Imposta il valore della proprietà flagsn.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGSN(String value) {
            this.flagsn = value;
        }

        /**
         * Recupera il valore della proprietà flagcopiachecklist.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGCOPIACHECKLIST() {
            return flagcopiachecklist;
        }

        /**
         * Imposta il valore della proprietà flagcopiachecklist.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGCOPIACHECKLIST(String value) {
            this.flagcopiachecklist = value;
        }

        /**
         * Recupera il valore della proprietà dataverificacgo4.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAVERIFICACGO4() {
            return dataverificacgo4;
        }

        /**
         * Imposta il valore della proprietà dataverificacgo4.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAVERIFICACGO4(XMLGregorianCalendar value) {
            this.dataverificacgo4 = value;
        }

        /**
         * Recupera il valore della proprietà dataverificacgo9.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAVERIFICACGO9() {
            return dataverificacgo9;
        }

        /**
         * Imposta il valore della proprietà dataverificacgo9.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAVERIFICACGO9(XMLGregorianCalendar value) {
            this.dataverificacgo9 = value;
        }

        /**
         * Recupera il valore della proprietà secondocontrollore.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSECONDOCONTROLLORE() {
            return secondocontrollore;
        }

        /**
         * Imposta il valore della proprietà secondocontrollore.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSECONDOCONTROLLORE(String value) {
            this.secondocontrollore = value;
        }

        /**
         * Recupera il valore della proprietà verbaleinserito.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVERBALEINSERITO() {
            return verbaleinserito;
        }

        /**
         * Imposta il valore della proprietà verbaleinserito.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVERBALEINSERITO(String value) {
            this.verbaleinserito = value;
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
         * Recupera il valore della proprietà flagrichprescrizionicgo4.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGRICHPRESCRIZIONICGO4() {
            return flagrichprescrizionicgo4;
        }

        /**
         * Imposta il valore della proprietà flagrichprescrizionicgo4.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGRICHPRESCRIZIONICGO4(String value) {
            this.flagrichprescrizionicgo4 = value;
        }

        /**
         * Recupera il valore della proprietà prescrizionirichiestecgo4.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPRESCRIZIONIRICHIESTECGO4() {
            return prescrizionirichiestecgo4;
        }

        /**
         * Imposta il valore della proprietà prescrizionirichiestecgo4.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPRESCRIZIONIRICHIESTECGO4(String value) {
            this.prescrizionirichiestecgo4 = value;
        }

        /**
         * Recupera il valore della proprietà datascadprescrizionicgo4.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATASCADPRESCRIZIONICGO4() {
            return datascadprescrizionicgo4;
        }

        /**
         * Imposta il valore della proprietà datascadprescrizionicgo4.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATASCADPRESCRIZIONICGO4(XMLGregorianCalendar value) {
            this.datascadprescrizionicgo4 = value;
        }

        /**
         * Recupera il valore della proprietà flagrichprescrizionicgo9.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGRICHPRESCRIZIONICGO9() {
            return flagrichprescrizionicgo9;
        }

        /**
         * Imposta il valore della proprietà flagrichprescrizionicgo9.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGRICHPRESCRIZIONICGO9(String value) {
            this.flagrichprescrizionicgo9 = value;
        }

        /**
         * Recupera il valore della proprietà prescrizionirichiestecgo9.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPRESCRIZIONIRICHIESTECGO9() {
            return prescrizionirichiestecgo9;
        }

        /**
         * Imposta il valore della proprietà prescrizionirichiestecgo9.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPRESCRIZIONIRICHIESTECGO9(String value) {
            this.prescrizionirichiestecgo9 = value;
        }

        /**
         * Recupera il valore della proprietà datascadprescrizionicgo9.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATASCADPRESCRIZIONICGO9() {
            return datascadprescrizionicgo9;
        }

        /**
         * Imposta il valore della proprietà datascadprescrizionicgo9.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATASCADPRESCRIZIONICGO9(XMLGregorianCalendar value) {
            this.datascadprescrizionicgo9 = value;
        }

        /**
         * Recupera il valore della proprietà flagprescrizioneesitocgo4.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGPRESCRIZIONEESITOCGO4() {
            return flagprescrizioneesitocgo4;
        }

        /**
         * Imposta il valore della proprietà flagprescrizioneesitocgo4.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGPRESCRIZIONEESITOCGO4(String value) {
            this.flagprescrizioneesitocgo4 = value;
        }

        /**
         * Recupera il valore della proprietà numcapicontrollati.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPICONTROLLATI() {
            return numcapicontrollati;
        }

        /**
         * Imposta il valore della proprietà numcapicontrollati.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPICONTROLLATI(BigDecimal value) {
            this.numcapicontrollati = value;
        }

        /**
         * Recupera il valore della proprietà numcapipresenti.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPIPRESENTI() {
            return numcapipresenti;
        }

        /**
         * Imposta il valore della proprietà numcapipresenti.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPIPRESENTI(BigDecimal value) {
            this.numcapipresenti = value;
        }

    }

}

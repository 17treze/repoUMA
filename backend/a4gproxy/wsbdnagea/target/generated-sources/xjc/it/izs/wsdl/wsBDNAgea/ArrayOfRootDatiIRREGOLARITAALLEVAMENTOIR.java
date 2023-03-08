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
 * <p>Classe Java per ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_IR complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_IR"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IRREGOLARITA_ALLEVAMENTO_IR" maxOccurs="unbounded" minOccurs="0"&gt;
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
 *                   &lt;element name="NUM_CAPI_PRESENTI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_CONTROLLATI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_ANOMALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_IRREGOLARITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="IRREGOLARITA_RISCONTRATA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUMERO_IRREGOLARITA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CRITERIO_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ORGANISMO_CONTROLLORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SANZ_AMMINISTRATIVA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_SANZ_AMMINISTRATIVA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="SANZ_BLOCCO_MOV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_SANZ_BLOCCO_MOV" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="SANZ_SEQUESTRO_CAPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_SANZ_SEQUESTRO_CAPI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="SANZ_ABBATTIMENTO_CAPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_SANZ_ABBATTIMENTO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="SANZ_ALTRO_DESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CAPI_SANZ_ALTRO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="PRESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="TERMINE_PRESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DATA_VERIFICA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="ESITO_VERIFICA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="VERBALE_INSERITO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="STATO_PROCEDIMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="INTENZIONALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_IR", propOrder = {
    "irregolaritaallevamentoir"
})
public class ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR {

    @XmlElement(name = "IRREGOLARITA_ALLEVAMENTO_IR")
    protected List<ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR.IRREGOLARITAALLEVAMENTOIR> irregolaritaallevamentoir;

    /**
     * Gets the value of the irregolaritaallevamentoir property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the irregolaritaallevamentoir property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIRREGOLARITAALLEVAMENTOIR().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR.IRREGOLARITAALLEVAMENTOIR }
     * 
     * 
     */
    public List<ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR.IRREGOLARITAALLEVAMENTOIR> getIRREGOLARITAALLEVAMENTOIR() {
        if (irregolaritaallevamentoir == null) {
            irregolaritaallevamentoir = new ArrayList<ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR.IRREGOLARITAALLEVAMENTOIR>();
        }
        return this.irregolaritaallevamentoir;
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
     *         &lt;element name="NUM_CAPI_PRESENTI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_CONTROLLATI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_ANOMALI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_IRREGOLARITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="IRREGOLARITA_RISCONTRATA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUMERO_IRREGOLARITA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CRITERIO_CONTROLLO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ORGANISMO_CONTROLLORE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SANZ_AMMINISTRATIVA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_SANZ_AMMINISTRATIVA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="SANZ_BLOCCO_MOV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_SANZ_BLOCCO_MOV" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="SANZ_SEQUESTRO_CAPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_SANZ_SEQUESTRO_CAPI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="SANZ_ABBATTIMENTO_CAPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_SANZ_ABBATTIMENTO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="SANZ_ALTRO_DESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CAPI_SANZ_ALTRO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="PRESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="TERMINE_PRESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DATA_VERIFICA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="ESITO_VERIFICA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="VERBALE_INSERITO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="STATO_PROCEDIMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="INTENZIONALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "numcapipresenti",
        "numcapicontrollati",
        "numcapianomali",
        "codiceirregolarita",
        "irregolaritariscontrata",
        "numeroirregolarita",
        "criteriocontrollo",
        "organismocontrollore",
        "sanzamministrativa",
        "numcapisanzamministrativa",
        "sanzbloccomov",
        "numcapisanzbloccomov",
        "sanzsequestrocapi",
        "numcapisanzsequestrocapi",
        "sanzabbattimentocapi",
        "numcapisanzabbattimento",
        "sanzaltrodesc",
        "numcapisanzaltro",
        "prescrizione",
        "termineprescrizione",
        "dataverifica",
        "esitoverifica",
        "verbaleinserito",
        "statoprocedimento",
        "intenzionalita"
    })
    public static class IRREGOLARITAALLEVAMENTOIR {

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
        @XmlElement(name = "NUM_CAPI_PRESENTI")
        protected BigDecimal numcapipresenti;
        @XmlElement(name = "NUM_CAPI_CONTROLLATI")
        protected BigDecimal numcapicontrollati;
        @XmlElement(name = "NUM_CAPI_ANOMALI")
        protected BigDecimal numcapianomali;
        @XmlElement(name = "CODICE_IRREGOLARITA")
        protected String codiceirregolarita;
        @XmlElement(name = "IRREGOLARITA_RISCONTRATA")
        protected String irregolaritariscontrata;
        @XmlElement(name = "NUMERO_IRREGOLARITA")
        protected BigDecimal numeroirregolarita;
        @XmlElement(name = "CRITERIO_CONTROLLO")
        protected String criteriocontrollo;
        @XmlElement(name = "ORGANISMO_CONTROLLORE")
        protected String organismocontrollore;
        @XmlElement(name = "SANZ_AMMINISTRATIVA")
        protected String sanzamministrativa;
        @XmlElement(name = "NUM_CAPI_SANZ_AMMINISTRATIVA")
        protected BigDecimal numcapisanzamministrativa;
        @XmlElement(name = "SANZ_BLOCCO_MOV")
        protected String sanzbloccomov;
        @XmlElement(name = "NUM_CAPI_SANZ_BLOCCO_MOV")
        protected BigDecimal numcapisanzbloccomov;
        @XmlElement(name = "SANZ_SEQUESTRO_CAPI")
        protected String sanzsequestrocapi;
        @XmlElement(name = "NUM_CAPI_SANZ_SEQUESTRO_CAPI")
        protected BigDecimal numcapisanzsequestrocapi;
        @XmlElement(name = "SANZ_ABBATTIMENTO_CAPI")
        protected String sanzabbattimentocapi;
        @XmlElement(name = "NUM_CAPI_SANZ_ABBATTIMENTO")
        protected BigDecimal numcapisanzabbattimento;
        @XmlElement(name = "SANZ_ALTRO_DESC")
        protected String sanzaltrodesc;
        @XmlElement(name = "NUM_CAPI_SANZ_ALTRO")
        protected BigDecimal numcapisanzaltro;
        @XmlElement(name = "PRESCRIZIONE")
        protected String prescrizione;
        @XmlElement(name = "TERMINE_PRESCRIZIONE")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar termineprescrizione;
        @XmlElement(name = "DATA_VERIFICA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dataverifica;
        @XmlElement(name = "ESITO_VERIFICA")
        protected String esitoverifica;
        @XmlElement(name = "VERBALE_INSERITO")
        protected String verbaleinserito;
        @XmlElement(name = "STATO_PROCEDIMENTO")
        protected String statoprocedimento;
        @XmlElement(name = "INTENZIONALITA")
        protected String intenzionalita;

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
         * Recupera il valore della proprietà numcapianomali.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPIANOMALI() {
            return numcapianomali;
        }

        /**
         * Imposta il valore della proprietà numcapianomali.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPIANOMALI(BigDecimal value) {
            this.numcapianomali = value;
        }

        /**
         * Recupera il valore della proprietà codiceirregolarita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICEIRREGOLARITA() {
            return codiceirregolarita;
        }

        /**
         * Imposta il valore della proprietà codiceirregolarita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICEIRREGOLARITA(String value) {
            this.codiceirregolarita = value;
        }

        /**
         * Recupera il valore della proprietà irregolaritariscontrata.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIRREGOLARITARISCONTRATA() {
            return irregolaritariscontrata;
        }

        /**
         * Imposta il valore della proprietà irregolaritariscontrata.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIRREGOLARITARISCONTRATA(String value) {
            this.irregolaritariscontrata = value;
        }

        /**
         * Recupera il valore della proprietà numeroirregolarita.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMEROIRREGOLARITA() {
            return numeroirregolarita;
        }

        /**
         * Imposta il valore della proprietà numeroirregolarita.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMEROIRREGOLARITA(BigDecimal value) {
            this.numeroirregolarita = value;
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
         * Recupera il valore della proprietà sanzamministrativa.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSANZAMMINISTRATIVA() {
            return sanzamministrativa;
        }

        /**
         * Imposta il valore della proprietà sanzamministrativa.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSANZAMMINISTRATIVA(String value) {
            this.sanzamministrativa = value;
        }

        /**
         * Recupera il valore della proprietà numcapisanzamministrativa.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPISANZAMMINISTRATIVA() {
            return numcapisanzamministrativa;
        }

        /**
         * Imposta il valore della proprietà numcapisanzamministrativa.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPISANZAMMINISTRATIVA(BigDecimal value) {
            this.numcapisanzamministrativa = value;
        }

        /**
         * Recupera il valore della proprietà sanzbloccomov.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSANZBLOCCOMOV() {
            return sanzbloccomov;
        }

        /**
         * Imposta il valore della proprietà sanzbloccomov.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSANZBLOCCOMOV(String value) {
            this.sanzbloccomov = value;
        }

        /**
         * Recupera il valore della proprietà numcapisanzbloccomov.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPISANZBLOCCOMOV() {
            return numcapisanzbloccomov;
        }

        /**
         * Imposta il valore della proprietà numcapisanzbloccomov.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPISANZBLOCCOMOV(BigDecimal value) {
            this.numcapisanzbloccomov = value;
        }

        /**
         * Recupera il valore della proprietà sanzsequestrocapi.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSANZSEQUESTROCAPI() {
            return sanzsequestrocapi;
        }

        /**
         * Imposta il valore della proprietà sanzsequestrocapi.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSANZSEQUESTROCAPI(String value) {
            this.sanzsequestrocapi = value;
        }

        /**
         * Recupera il valore della proprietà numcapisanzsequestrocapi.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPISANZSEQUESTROCAPI() {
            return numcapisanzsequestrocapi;
        }

        /**
         * Imposta il valore della proprietà numcapisanzsequestrocapi.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPISANZSEQUESTROCAPI(BigDecimal value) {
            this.numcapisanzsequestrocapi = value;
        }

        /**
         * Recupera il valore della proprietà sanzabbattimentocapi.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSANZABBATTIMENTOCAPI() {
            return sanzabbattimentocapi;
        }

        /**
         * Imposta il valore della proprietà sanzabbattimentocapi.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSANZABBATTIMENTOCAPI(String value) {
            this.sanzabbattimentocapi = value;
        }

        /**
         * Recupera il valore della proprietà numcapisanzabbattimento.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPISANZABBATTIMENTO() {
            return numcapisanzabbattimento;
        }

        /**
         * Imposta il valore della proprietà numcapisanzabbattimento.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPISANZABBATTIMENTO(BigDecimal value) {
            this.numcapisanzabbattimento = value;
        }

        /**
         * Recupera il valore della proprietà sanzaltrodesc.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSANZALTRODESC() {
            return sanzaltrodesc;
        }

        /**
         * Imposta il valore della proprietà sanzaltrodesc.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSANZALTRODESC(String value) {
            this.sanzaltrodesc = value;
        }

        /**
         * Recupera il valore della proprietà numcapisanzaltro.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getNUMCAPISANZALTRO() {
            return numcapisanzaltro;
        }

        /**
         * Imposta il valore della proprietà numcapisanzaltro.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setNUMCAPISANZALTRO(BigDecimal value) {
            this.numcapisanzaltro = value;
        }

        /**
         * Recupera il valore della proprietà prescrizione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPRESCRIZIONE() {
            return prescrizione;
        }

        /**
         * Imposta il valore della proprietà prescrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPRESCRIZIONE(String value) {
            this.prescrizione = value;
        }

        /**
         * Recupera il valore della proprietà termineprescrizione.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getTERMINEPRESCRIZIONE() {
            return termineprescrizione;
        }

        /**
         * Imposta il valore della proprietà termineprescrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setTERMINEPRESCRIZIONE(XMLGregorianCalendar value) {
            this.termineprescrizione = value;
        }

        /**
         * Recupera il valore della proprietà dataverifica.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDATAVERIFICA() {
            return dataverifica;
        }

        /**
         * Imposta il valore della proprietà dataverifica.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDATAVERIFICA(XMLGregorianCalendar value) {
            this.dataverifica = value;
        }

        /**
         * Recupera il valore della proprietà esitoverifica.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getESITOVERIFICA() {
            return esitoverifica;
        }

        /**
         * Imposta il valore della proprietà esitoverifica.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setESITOVERIFICA(String value) {
            this.esitoverifica = value;
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
         * Recupera il valore della proprietà intenzionalita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getINTENZIONALITA() {
            return intenzionalita;
        }

        /**
         * Imposta il valore della proprietà intenzionalita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setINTENZIONALITA(String value) {
            this.intenzionalita = value;
        }

    }

}

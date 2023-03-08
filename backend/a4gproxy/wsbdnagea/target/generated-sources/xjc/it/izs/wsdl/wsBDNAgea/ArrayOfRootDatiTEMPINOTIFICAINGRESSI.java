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
 * <p>Classe Java per ArrayOfRootDatiTEMPI_NOTIFICA_INGRESSI complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiTEMPI_NOTIFICA_INGRESSI"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TEMPI_NOTIFICA_INGRESSI" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="REGSTA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DESCR_MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_COM_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_REGISTR_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="GG_RITARDO_REGISTRAZ_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="FLAG_DELEGATO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RUOLO_UTENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiTEMPI_NOTIFICA_INGRESSI", propOrder = {
    "tempinotificaingressi"
})
public class ArrayOfRootDatiTEMPINOTIFICAINGRESSI {

    @XmlElement(name = "TEMPI_NOTIFICA_INGRESSI")
    protected List<ArrayOfRootDatiTEMPINOTIFICAINGRESSI.TEMPINOTIFICAINGRESSI> tempinotificaingressi;

    /**
     * Gets the value of the tempinotificaingressi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tempinotificaingressi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTEMPINOTIFICAINGRESSI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiTEMPINOTIFICAINGRESSI.TEMPINOTIFICAINGRESSI }
     * 
     * 
     */
    public List<ArrayOfRootDatiTEMPINOTIFICAINGRESSI.TEMPINOTIFICAINGRESSI> getTEMPINOTIFICAINGRESSI() {
        if (tempinotificaingressi == null) {
            tempinotificaingressi = new ArrayList<ArrayOfRootDatiTEMPINOTIFICAINGRESSI.TEMPINOTIFICAINGRESSI>();
        }
        return this.tempinotificaingressi;
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
     *         &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="REGSTA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CAPO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DESCR_MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_COM_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_REGISTR_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="GG_RITARDO_REGISTRAZ_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="FLAG_DELEGATO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RUOLO_UTENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
        "cuaa",
        "dtinizioresponsabilita",
        "dtfineresponsabilita",
        "dtinizioperiodo",
        "dtfineperiodo",
        "regstaid",
        "aziendacodice",
        "idfiscale",
        "specodice",
        "allevid",
        "capoid",
        "codice",
        "dtnascita",
        "dtingresso",
        "motivoingresso",
        "descrmotivoingresso",
        "dtcomingresso",
        "dtregistringresso",
        "ggritardoregistrazingresso",
        "flagdelegato",
        "ruoloutente"
    })
    public static class TEMPINOTIFICAINGRESSI {

        @XmlElement(name = "CUAA")
        protected String cuaa;
        @XmlElement(name = "DT_INIZIO_RESPONSABILITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtinizioresponsabilita;
        @XmlElement(name = "DT_FINE_RESPONSABILITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtfineresponsabilita;
        @XmlElement(name = "DT_INIZIO_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtinizioperiodo;
        @XmlElement(name = "DT_FINE_PERIODO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtfineperiodo;
        @XmlElement(name = "REGSTA_ID")
        protected BigDecimal regstaid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "ID_FISCALE")
        protected String idfiscale;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "ALLEV_ID")
        protected BigDecimal allevid;
        @XmlElement(name = "CAPO_ID")
        protected BigDecimal capoid;
        @XmlElement(name = "CODICE")
        protected String codice;
        @XmlElement(name = "DT_NASCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtnascita;
        @XmlElement(name = "DT_INGRESSO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtingresso;
        @XmlElement(name = "MOTIVO_INGRESSO")
        protected String motivoingresso;
        @XmlElement(name = "DESCR_MOTIVO_INGRESSO")
        protected String descrmotivoingresso;
        @XmlElement(name = "DT_COM_INGRESSO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtcomingresso;
        @XmlElement(name = "DT_REGISTR_INGRESSO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtregistringresso;
        @XmlElement(name = "GG_RITARDO_REGISTRAZ_INGRESSO")
        protected BigDecimal ggritardoregistrazingresso;
        @XmlElement(name = "FLAG_DELEGATO")
        protected String flagdelegato;
        @XmlElement(name = "RUOLO_UTENTE")
        protected String ruoloutente;

        /**
         * Recupera il valore della proprietà cuaa.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCUAA() {
            return cuaa;
        }

        /**
         * Imposta il valore della proprietà cuaa.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCUAA(String value) {
            this.cuaa = value;
        }

        /**
         * Recupera il valore della proprietà dtinizioresponsabilita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINIZIORESPONSABILITA() {
            return dtinizioresponsabilita;
        }

        /**
         * Imposta il valore della proprietà dtinizioresponsabilita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINIZIORESPONSABILITA(XMLGregorianCalendar value) {
            this.dtinizioresponsabilita = value;
        }

        /**
         * Recupera il valore della proprietà dtfineresponsabilita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTFINERESPONSABILITA() {
            return dtfineresponsabilita;
        }

        /**
         * Imposta il valore della proprietà dtfineresponsabilita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTFINERESPONSABILITA(XMLGregorianCalendar value) {
            this.dtfineresponsabilita = value;
        }

        /**
         * Recupera il valore della proprietà dtinizioperiodo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINIZIOPERIODO() {
            return dtinizioperiodo;
        }

        /**
         * Imposta il valore della proprietà dtinizioperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINIZIOPERIODO(XMLGregorianCalendar value) {
            this.dtinizioperiodo = value;
        }

        /**
         * Recupera il valore della proprietà dtfineperiodo.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTFINEPERIODO() {
            return dtfineperiodo;
        }

        /**
         * Imposta il valore della proprietà dtfineperiodo.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTFINEPERIODO(XMLGregorianCalendar value) {
            this.dtfineperiodo = value;
        }

        /**
         * Recupera il valore della proprietà regstaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getREGSTAID() {
            return regstaid;
        }

        /**
         * Imposta il valore della proprietà regstaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setREGSTAID(BigDecimal value) {
            this.regstaid = value;
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
         * Recupera il valore della proprietà idfiscale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIDFISCALE() {
            return idfiscale;
        }

        /**
         * Imposta il valore della proprietà idfiscale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIDFISCALE(String value) {
            this.idfiscale = value;
        }

        /**
         * Recupera il valore della proprietà specodice.
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
         * Imposta il valore della proprietà specodice.
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
         * Recupera il valore della proprietà allevid.
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
         * Imposta il valore della proprietà allevid.
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
         * Recupera il valore della proprietà codice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICE() {
            return codice;
        }

        /**
         * Imposta il valore della proprietà codice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICE(String value) {
            this.codice = value;
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
         * Recupera il valore della proprietà dtingresso.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTINGRESSO() {
            return dtingresso;
        }

        /**
         * Imposta il valore della proprietà dtingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTINGRESSO(XMLGregorianCalendar value) {
            this.dtingresso = value;
        }

        /**
         * Recupera il valore della proprietà motivoingresso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOTIVOINGRESSO() {
            return motivoingresso;
        }

        /**
         * Imposta il valore della proprietà motivoingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOTIVOINGRESSO(String value) {
            this.motivoingresso = value;
        }

        /**
         * Recupera il valore della proprietà descrmotivoingresso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDESCRMOTIVOINGRESSO() {
            return descrmotivoingresso;
        }

        /**
         * Imposta il valore della proprietà descrmotivoingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDESCRMOTIVOINGRESSO(String value) {
            this.descrmotivoingresso = value;
        }

        /**
         * Recupera il valore della proprietà dtcomingresso.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTCOMINGRESSO() {
            return dtcomingresso;
        }

        /**
         * Imposta il valore della proprietà dtcomingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTCOMINGRESSO(XMLGregorianCalendar value) {
            this.dtcomingresso = value;
        }

        /**
         * Recupera il valore della proprietà dtregistringresso.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTREGISTRINGRESSO() {
            return dtregistringresso;
        }

        /**
         * Imposta il valore della proprietà dtregistringresso.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTREGISTRINGRESSO(XMLGregorianCalendar value) {
            this.dtregistringresso = value;
        }

        /**
         * Recupera il valore della proprietà ggritardoregistrazingresso.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getGGRITARDOREGISTRAZINGRESSO() {
            return ggritardoregistrazingresso;
        }

        /**
         * Imposta il valore della proprietà ggritardoregistrazingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setGGRITARDOREGISTRAZINGRESSO(BigDecimal value) {
            this.ggritardoregistrazingresso = value;
        }

        /**
         * Recupera il valore della proprietà flagdelegato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLAGDELEGATO() {
            return flagdelegato;
        }

        /**
         * Imposta il valore della proprietà flagdelegato.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLAGDELEGATO(String value) {
            this.flagdelegato = value;
        }

        /**
         * Recupera il valore della proprietà ruoloutente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRUOLOUTENTE() {
            return ruoloutente;
        }

        /**
         * Imposta il valore della proprietà ruoloutente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRUOLOUTENTE(String value) {
            this.ruoloutente = value;
        }

    }

}

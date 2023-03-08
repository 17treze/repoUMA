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
 * <p>Classe Java per ArrayOfRootDatiOVI_TEMPI_NOTIFICA_USCITE_PARTITE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiOVI_TEMPI_NOTIFICA_USCITE_PARTITE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OVI_TEMPI_NOTIFICA_USCITE_PARTITE" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CUAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_RESPONSABILITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INIZIO_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_FINE_PERIODO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="USCITA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_OVINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_DESTINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DESTINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DESCR_MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_COM_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="DT_REGISTR_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="GG_RITARDO_REGISTRAZ_USCITA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiOVI_TEMPI_NOTIFICA_USCITE_PARTITE", propOrder = {
    "ovitempinotificauscitepartite"
})
public class ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE {

    @XmlElement(name = "OVI_TEMPI_NOTIFICA_USCITE_PARTITE")
    protected List<ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE.OVITEMPINOTIFICAUSCITEPARTITE> ovitempinotificauscitepartite;

    /**
     * Gets the value of the ovitempinotificauscitepartite property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ovitempinotificauscitepartite property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOVITEMPINOTIFICAUSCITEPARTITE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE.OVITEMPINOTIFICAUSCITEPARTITE }
     * 
     * 
     */
    public List<ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE.OVITEMPINOTIFICAUSCITEPARTITE> getOVITEMPINOTIFICAUSCITEPARTITE() {
        if (ovitempinotificauscitepartite == null) {
            ovitempinotificauscitepartite = new ArrayList<ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE.OVITEMPINOTIFICAUSCITEPARTITE>();
        }
        return this.ovitempinotificauscitepartite;
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
     *         &lt;element name="USCITA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NUM_OVINI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_DESTINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DESTINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DESCR_MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_COM_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="DT_REGISTR_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="GG_RITARDO_REGISTRAZ_USCITA" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
        "uscitaid",
        "aziendacodice",
        "idfiscale",
        "specodice",
        "allevid",
        "numovini",
        "tipodestinazione",
        "destinazione",
        "dtuscita",
        "motivouscita",
        "descrmotivouscita",
        "dtcomuscita",
        "dtregistruscita",
        "ggritardoregistrazuscita",
        "flagdelegato",
        "ruoloutente"
    })
    public static class OVITEMPINOTIFICAUSCITEPARTITE {

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
        @XmlElement(name = "USCITA_ID")
        protected BigDecimal uscitaid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "ID_FISCALE")
        protected String idfiscale;
        @XmlElement(name = "SPE_CODICE")
        protected String specodice;
        @XmlElement(name = "ALLEV_ID")
        protected BigDecimal allevid;
        @XmlElement(name = "NUM_OVINI")
        protected BigDecimal numovini;
        @XmlElement(name = "TIPO_DESTINAZIONE")
        protected String tipodestinazione;
        @XmlElement(name = "DESTINAZIONE")
        protected String destinazione;
        @XmlElement(name = "DT_USCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtuscita;
        @XmlElement(name = "MOTIVO_USCITA")
        protected String motivouscita;
        @XmlElement(name = "DESCR_MOTIVO_USCITA")
        protected String descrmotivouscita;
        @XmlElement(name = "DT_COM_USCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtcomuscita;
        @XmlElement(name = "DT_REGISTR_USCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtregistruscita;
        @XmlElement(name = "GG_RITARDO_REGISTRAZ_USCITA")
        protected BigDecimal ggritardoregistrazuscita;
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
         * Recupera il valore della proprietà uscitaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getUSCITAID() {
            return uscitaid;
        }

        /**
         * Imposta il valore della proprietà uscitaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setUSCITAID(BigDecimal value) {
            this.uscitaid = value;
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
         * Recupera il valore della proprietà tipodestinazione.
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
         * Imposta il valore della proprietà tipodestinazione.
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
         * Recupera il valore della proprietà destinazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDESTINAZIONE() {
            return destinazione;
        }

        /**
         * Imposta il valore della proprietà destinazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDESTINAZIONE(String value) {
            this.destinazione = value;
        }

        /**
         * Recupera il valore della proprietà dtuscita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTUSCITA() {
            return dtuscita;
        }

        /**
         * Imposta il valore della proprietà dtuscita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTUSCITA(XMLGregorianCalendar value) {
            this.dtuscita = value;
        }

        /**
         * Recupera il valore della proprietà motivouscita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMOTIVOUSCITA() {
            return motivouscita;
        }

        /**
         * Imposta il valore della proprietà motivouscita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMOTIVOUSCITA(String value) {
            this.motivouscita = value;
        }

        /**
         * Recupera il valore della proprietà descrmotivouscita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDESCRMOTIVOUSCITA() {
            return descrmotivouscita;
        }

        /**
         * Imposta il valore della proprietà descrmotivouscita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDESCRMOTIVOUSCITA(String value) {
            this.descrmotivouscita = value;
        }

        /**
         * Recupera il valore della proprietà dtcomuscita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTCOMUSCITA() {
            return dtcomuscita;
        }

        /**
         * Imposta il valore della proprietà dtcomuscita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTCOMUSCITA(XMLGregorianCalendar value) {
            this.dtcomuscita = value;
        }

        /**
         * Recupera il valore della proprietà dtregistruscita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTREGISTRUSCITA() {
            return dtregistruscita;
        }

        /**
         * Imposta il valore della proprietà dtregistruscita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTREGISTRUSCITA(XMLGregorianCalendar value) {
            this.dtregistruscita = value;
        }

        /**
         * Recupera il valore della proprietà ggritardoregistrazuscita.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getGGRITARDOREGISTRAZUSCITA() {
            return ggritardoregistrazuscita;
        }

        /**
         * Imposta il valore della proprietà ggritardoregistrazuscita.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setGGRITARDOREGISTRAZUSCITA(BigDecimal value) {
            this.ggritardoregistrazuscita = value;
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

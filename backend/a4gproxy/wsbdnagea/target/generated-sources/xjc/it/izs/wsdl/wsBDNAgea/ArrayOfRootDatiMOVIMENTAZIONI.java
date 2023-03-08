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
 * <p>Classe Java per ArrayOfRootDatiMOVIMENTAZIONI complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRootDatiMOVIMENTAZIONI"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MOVIMENTAZIONI" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="REGSTA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="SPE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="INGRESSO_MM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="PROP_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="PROP_CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PROP_COGN_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DETEN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DETEN_CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DETEN_COGN_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PROV_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="PROV_AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="PROV_FM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="NOTE_REGISTRO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="USCITA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *                   &lt;element name="MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="USCITA_MM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DEST_ST_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DEST_FM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DEST_MACELLO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DEST_AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "ArrayOfRootDatiMOVIMENTAZIONI", propOrder = {
    "movimentazioni"
})
public class ArrayOfRootDatiMOVIMENTAZIONI {

    @XmlElement(name = "MOVIMENTAZIONI")
    protected List<ArrayOfRootDatiMOVIMENTAZIONI.MOVIMENTAZIONI> movimentazioni;

    /**
     * Gets the value of the movimentazioni property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the movimentazioni property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMOVIMENTAZIONI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfRootDatiMOVIMENTAZIONI.MOVIMENTAZIONI }
     * 
     * 
     */
    public List<ArrayOfRootDatiMOVIMENTAZIONI.MOVIMENTAZIONI> getMOVIMENTAZIONI() {
        if (movimentazioni == null) {
            movimentazioni = new ArrayList<ArrayOfRootDatiMOVIMENTAZIONI.MOVIMENTAZIONI>();
        }
        return this.movimentazioni;
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
     *         &lt;element name="REGSTA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="SPE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="INGRESSO_MM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="PROP_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="PROP_CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PROP_COGN_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DETEN_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DETEN_CUUA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DETEN_COGN_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PROV_ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="PROV_AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="PROV_FM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="NOTE_REGISTRO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="USCITA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     *         &lt;element name="MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="USCITA_MM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DEST_ST_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DEST_FM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DEST_MACELLO_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DEST_AZIENDA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
        "regstaid",
        "allevid",
        "allevidfiscale",
        "aziendacodice",
        "aziendaid",
        "speid",
        "dtingresso",
        "motivoingresso",
        "ingressommid",
        "propid",
        "propcuua",
        "propcognnome",
        "detenid",
        "detencuua",
        "detencognnome",
        "provallevid",
        "provaziendaid",
        "provfmid",
        "noteregistro",
        "uscitaid",
        "dtuscita",
        "motivouscita",
        "uscitammid",
        "deststid",
        "destfmid",
        "destmacelloid",
        "destaziendaid"
    })
    public static class MOVIMENTAZIONI {

        @XmlElement(name = "REGSTA_ID")
        protected BigDecimal regstaid;
        @XmlElement(name = "ALLEV_ID")
        protected BigDecimal allevid;
        @XmlElement(name = "ALLEV_ID_FISCALE")
        protected String allevidfiscale;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "AZIENDA_ID")
        protected BigDecimal aziendaid;
        @XmlElement(name = "SPE_ID")
        protected BigDecimal speid;
        @XmlElement(name = "DT_INGRESSO")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtingresso;
        @XmlElement(name = "MOTIVO_INGRESSO")
        protected String motivoingresso;
        @XmlElement(name = "INGRESSO_MM_ID")
        protected BigDecimal ingressommid;
        @XmlElement(name = "PROP_ID")
        protected BigDecimal propid;
        @XmlElement(name = "PROP_CUUA")
        protected String propcuua;
        @XmlElement(name = "PROP_COGN_NOME")
        protected String propcognnome;
        @XmlElement(name = "DETEN_ID")
        protected BigDecimal detenid;
        @XmlElement(name = "DETEN_CUUA")
        protected String detencuua;
        @XmlElement(name = "DETEN_COGN_NOME")
        protected String detencognnome;
        @XmlElement(name = "PROV_ALLEV_ID")
        protected BigDecimal provallevid;
        @XmlElement(name = "PROV_AZIENDA_ID")
        protected BigDecimal provaziendaid;
        @XmlElement(name = "PROV_FM_ID")
        protected BigDecimal provfmid;
        @XmlElement(name = "NOTE_REGISTRO")
        protected String noteregistro;
        @XmlElement(name = "USCITA_ID")
        protected BigDecimal uscitaid;
        @XmlElement(name = "DT_USCITA")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar dtuscita;
        @XmlElement(name = "MOTIVO_USCITA")
        protected String motivouscita;
        @XmlElement(name = "USCITA_MM_ID")
        protected BigDecimal uscitammid;
        @XmlElement(name = "DEST_ST_ID")
        protected BigDecimal deststid;
        @XmlElement(name = "DEST_FM_ID")
        protected BigDecimal destfmid;
        @XmlElement(name = "DEST_MACELLO_ID")
        protected BigDecimal destmacelloid;
        @XmlElement(name = "DEST_AZIENDA_ID")
        protected BigDecimal destaziendaid;

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
         * Recupera il valore della proprietà allevidfiscale.
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
         * Imposta il valore della proprietà allevidfiscale.
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
         * Recupera il valore della proprietà aziendaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getAZIENDAID() {
            return aziendaid;
        }

        /**
         * Imposta il valore della proprietà aziendaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setAZIENDAID(BigDecimal value) {
            this.aziendaid = value;
        }

        /**
         * Recupera il valore della proprietà speid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getSPEID() {
            return speid;
        }

        /**
         * Imposta il valore della proprietà speid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setSPEID(BigDecimal value) {
            this.speid = value;
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
         * Recupera il valore della proprietà ingressommid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getINGRESSOMMID() {
            return ingressommid;
        }

        /**
         * Imposta il valore della proprietà ingressommid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setINGRESSOMMID(BigDecimal value) {
            this.ingressommid = value;
        }

        /**
         * Recupera il valore della proprietà propid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPROPID() {
            return propid;
        }

        /**
         * Imposta il valore della proprietà propid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPROPID(BigDecimal value) {
            this.propid = value;
        }

        /**
         * Recupera il valore della proprietà propcuua.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPROPCUUA() {
            return propcuua;
        }

        /**
         * Imposta il valore della proprietà propcuua.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPROPCUUA(String value) {
            this.propcuua = value;
        }

        /**
         * Recupera il valore della proprietà propcognnome.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPROPCOGNNOME() {
            return propcognnome;
        }

        /**
         * Imposta il valore della proprietà propcognnome.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPROPCOGNNOME(String value) {
            this.propcognnome = value;
        }

        /**
         * Recupera il valore della proprietà detenid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getDETENID() {
            return detenid;
        }

        /**
         * Imposta il valore della proprietà detenid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setDETENID(BigDecimal value) {
            this.detenid = value;
        }

        /**
         * Recupera il valore della proprietà detencuua.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDETENCUUA() {
            return detencuua;
        }

        /**
         * Imposta il valore della proprietà detencuua.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDETENCUUA(String value) {
            this.detencuua = value;
        }

        /**
         * Recupera il valore della proprietà detencognnome.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDETENCOGNNOME() {
            return detencognnome;
        }

        /**
         * Imposta il valore della proprietà detencognnome.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDETENCOGNNOME(String value) {
            this.detencognnome = value;
        }

        /**
         * Recupera il valore della proprietà provallevid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPROVALLEVID() {
            return provallevid;
        }

        /**
         * Imposta il valore della proprietà provallevid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPROVALLEVID(BigDecimal value) {
            this.provallevid = value;
        }

        /**
         * Recupera il valore della proprietà provaziendaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPROVAZIENDAID() {
            return provaziendaid;
        }

        /**
         * Imposta il valore della proprietà provaziendaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPROVAZIENDAID(BigDecimal value) {
            this.provaziendaid = value;
        }

        /**
         * Recupera il valore della proprietà provfmid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPROVFMID() {
            return provfmid;
        }

        /**
         * Imposta il valore della proprietà provfmid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPROVFMID(BigDecimal value) {
            this.provfmid = value;
        }

        /**
         * Recupera il valore della proprietà noteregistro.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNOTEREGISTRO() {
            return noteregistro;
        }

        /**
         * Imposta il valore della proprietà noteregistro.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNOTEREGISTRO(String value) {
            this.noteregistro = value;
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
         * Recupera il valore della proprietà uscitammid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getUSCITAMMID() {
            return uscitammid;
        }

        /**
         * Imposta il valore della proprietà uscitammid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setUSCITAMMID(BigDecimal value) {
            this.uscitammid = value;
        }

        /**
         * Recupera il valore della proprietà deststid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getDESTSTID() {
            return deststid;
        }

        /**
         * Imposta il valore della proprietà deststid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setDESTSTID(BigDecimal value) {
            this.deststid = value;
        }

        /**
         * Recupera il valore della proprietà destfmid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getDESTFMID() {
            return destfmid;
        }

        /**
         * Imposta il valore della proprietà destfmid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setDESTFMID(BigDecimal value) {
            this.destfmid = value;
        }

        /**
         * Recupera il valore della proprietà destmacelloid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getDESTMACELLOID() {
            return destmacelloid;
        }

        /**
         * Imposta il valore della proprietà destmacelloid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setDESTMACELLOID(BigDecimal value) {
            this.destmacelloid = value;
        }

        /**
         * Recupera il valore della proprietà destaziendaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getDESTAZIENDAID() {
            return destaziendaid;
        }

        /**
         * Imposta il valore della proprietà destaziendaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setDESTAZIENDAID(BigDecimal value) {
            this.destaziendaid = value;
        }

    }

}

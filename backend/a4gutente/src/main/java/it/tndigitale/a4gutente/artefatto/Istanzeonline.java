package it.tndigitale.a4gutente.artefatto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceprovider">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="oraautenticazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="numerocertificato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="moduloId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="hashcertificato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="datascadenzacertificato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="datarilasciocertificato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataautenticazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codicefiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="certificationauthority" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="sicurezza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="spidcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="autenticationmethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="hashdati" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="hashdocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "serviceprovider"
})
public class Istanzeonline {

    @XmlElement(namespace = "", required = true)
    protected Istanzeonline.Serviceprovider serviceprovider;

    /**
     * Gets the value of the serviceprovider property.
     * 
     * @return
     *     possible object is
     *     {@link Istanzeonline.Serviceprovider }
     *     
     */
    public Istanzeonline.Serviceprovider getServiceprovider() {
        return serviceprovider;
    }

    /**
     * Sets the value of the serviceprovider property.
     * 
     * @param value
     *     allowed object is
     *     {@link Istanzeonline.Serviceprovider }
     *     
     */
    public void setServiceprovider(Istanzeonline.Serviceprovider value) {
        this.serviceprovider = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="oraautenticazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="numerocertificato" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="moduloId" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="hashcertificato" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="datascadenzacertificato" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="datarilasciocertificato" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataautenticazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codicefiscale" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="certificationauthority" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="sicurezza" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="spidcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="autenticationmethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="hashdati" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="hashdocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "oraautenticazione",
        "numerocertificato",
        "nome",
        "moduloId",
        "hashcertificato",
        "datascadenzacertificato",
        "datarilasciocertificato",
        "dataautenticazione",
        "cognome",
        "codicefiscale",
        "certificationauthority",
        "sicurezza",
//        "urlresult",
//        "salvabozzaurl",
//        "nocodicefiscale",
//        "impresaurl",
//        "salvaechiudiurl",
        "spidcode",
        "autenticationmethod",
        "hashdati",
        "hashdocumento"
    })
    public static class Serviceprovider {

        @XmlElement(namespace = "", required = true)
        protected String oraautenticazione;
        @XmlElement(namespace = "", required = true)
        protected String numerocertificato;
        @XmlElement(namespace = "", required = true)
        protected String nome;
        @XmlElement(namespace = "", required = true)
        protected String moduloId;
        @XmlElement(namespace = "", required = true)
        protected String hashcertificato;
        @XmlElement(namespace = "", required = true)
        protected String datascadenzacertificato;
        @XmlElement(namespace = "", required = true)
        protected String datarilasciocertificato;
        @XmlElement(namespace = "", required = true)
        protected String dataautenticazione;
        @XmlElement(namespace = "", required = true)
        protected String cognome;
        @XmlElement(namespace = "", required = true)
        protected String codicefiscale;
        @XmlElement(namespace = "", required = true)
        protected String certificationauthority;
        @XmlElement(namespace = "", required = true)
        protected String sicurezza;
//        @XmlElement(namespace = "", required = true)
//        protected String urlresult;
//        @XmlElement(namespace = "", required = true)
//        protected String salvabozzaurl;
//        @XmlElement(namespace = "", required = true)
//        protected String nocodicefiscale;
//        @XmlElement(namespace = "", required = true)
//        protected String impresaurl;
//        @XmlElement(namespace = "", required = true)
//        protected String salvaechiudiurl;
        @XmlElement(namespace = "", required = true)
        protected String spidcode;
        @XmlElement(namespace = "", required = true)
        protected String autenticationmethod;
        @XmlElement(namespace = "", required = true)
        protected String hashdati;
        @XmlElement(namespace = "", required = true)
        protected String hashdocumento;
        @XmlAttribute(required = true)
        protected String value;

        /**
         * Gets the value of the oraautenticazione property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraautenticazione() {
            return oraautenticazione;
        }

        /**
         * Sets the value of the oraautenticazione property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraautenticazione(String value) {
            this.oraautenticazione = value;
        }

        /**
         * Gets the value of the numerocertificato property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumerocertificato() {
            return numerocertificato;
        }

        /**
         * Sets the value of the numerocertificato property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumerocertificato(String value) {
            this.numerocertificato = value;
        }

        /**
         * Gets the value of the nome property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNome() {
            return nome;
        }

        /**
         * Sets the value of the nome property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNome(String value) {
            this.nome = value;
        }

        /**
         * Gets the value of the moduloId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getModuloId() {
            return moduloId;
        }

        /**
         * Sets the value of the moduloId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setModuloId(String value) {
            this.moduloId = value;
        }

        /**
         * Gets the value of the hashcertificato property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHashcertificato() {
            return hashcertificato;
        }

        /**
         * Sets the value of the hashcertificato property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHashcertificato(String value) {
            this.hashcertificato = value;
        }

        /**
         * Gets the value of the datascadenzacertificato property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatascadenzacertificato() {
            return datascadenzacertificato;
        }

        /**
         * Sets the value of the datascadenzacertificato property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDatascadenzacertificato(String value) {
            this.datascadenzacertificato = value;
        }

        /**
         * Gets the value of the datarilasciocertificato property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDatarilasciocertificato() {
            return datarilasciocertificato;
        }

        /**
         * Sets the value of the datarilasciocertificato property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDatarilasciocertificato(String value) {
            this.datarilasciocertificato = value;
        }

        /**
         * Gets the value of the dataautenticazione property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDataautenticazione() {
            return dataautenticazione;
        }

        /**
         * Sets the value of the dataautenticazione property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDataautenticazione(String value) {
            this.dataautenticazione = value;
        }

        /**
         * Gets the value of the cognome property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCognome() {
            return cognome;
        }

        /**
         * Sets the value of the cognome property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCognome(String value) {
            this.cognome = value;
        }

        /**
         * Gets the value of the codicefiscale property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodicefiscale() {
            return codicefiscale;
        }

        /**
         * Sets the value of the codicefiscale property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodicefiscale(String value) {
            this.codicefiscale = value;
        }

        /**
         * Gets the value of the certificationauthority property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCertificationauthority() {
            return certificationauthority;
        }

        /**
         * Sets the value of the certificationauthority property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCertificationauthority(String value) {
            this.certificationauthority = value;
        }

        /**
         * Gets the value of the sicurezza property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSicurezza() {
            return sicurezza;
        }

        /**
         * Sets the value of the sicurezza property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSicurezza(String value) {
            this.sicurezza = value;
        }

//        /**
//         * Gets the value of the urlresult property.
//         * 
//         * @return
//         *     possible object is
//         *     {@link String }
//         *     
//         */
//        public String getUrlresult() {
//            return urlresult;
//        }
//
//        /**
//         * Sets the value of the urlresult property.
//         * 
//         * @param value
//         *     allowed object is
//         *     {@link String }
//         *     
//         */
//        public void setUrlresult(String value) {
//            this.urlresult = value;
//        }
//
//        /**
//         * Gets the value of the salvabozzaurl property.
//         * 
//         * @return
//         *     possible object is
//         *     {@link String }
//         *     
//         */
//        public String getSalvabozzaurl() {
//            return salvabozzaurl;
//        }
//
//        /**
//         * Sets the value of the salvabozzaurl property.
//         * 
//         * @param value
//         *     allowed object is
//         *     {@link String }
//         *     
//         */
//        public void setSalvabozzaurl(String value) {
//            this.salvabozzaurl = value;
//        }
//
//        /**
//         * Gets the value of the nocodicefiscale property.
//         * 
//         * @return
//         *     possible object is
//         *     {@link String }
//         *     
//         */
//        public String getNocodicefiscale() {
//            return nocodicefiscale;
//        }
//
//        /**
//         * Sets the value of the nocodicefiscale property.
//         * 
//         * @param value
//         *     allowed object is
//         *     {@link String }
//         *     
//         */
//        public void setNocodicefiscale(String value) {
//            this.nocodicefiscale = value;
//        }
//
//        /**
//         * Gets the value of the impresaurl property.
//         * 
//         * @return
//         *     possible object is
//         *     {@link String }
//         *     
//         */
//        public String getImpresaurl() {
//            return impresaurl;
//        }
//
//        /**
//         * Sets the value of the impresaurl property.
//         * 
//         * @param value
//         *     allowed object is
//         *     {@link String }
//         *     
//         */
//        public void setImpresaurl(String value) {
//            this.impresaurl = value;
//        }
//
//        /**
//         * Gets the value of the salvaechiudiurl property.
//         * 
//         * @return
//         *     possible object is
//         *     {@link String }
//         *     
//         */
//        public String getSalvaechiudiurl() {
//            return salvaechiudiurl;
//        }
//
//        /**
//         * Sets the value of the salvaechiudiurl property.
//         * 
//         * @param value
//         *     allowed object is
//         *     {@link String }
//         *     
//         */
//        public void setSalvaechiudiurl(String value) {
//            this.salvaechiudiurl = value;
//        }

        /**
         * Gets the value of the spidcode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSpidcode() {
            return spidcode;
        }

        /**
         * Sets the value of the spidcode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSpidcode(String value) {
            this.spidcode = value;
        }

        /**
         * Gets the value of the autenticationmethod property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAutenticationmethod() {
            return autenticationmethod;
        }

        /**
         * Sets the value of the autenticationmethod property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAutenticationmethod(String value) {
            this.autenticationmethod = value;
        }

        public String getHashdati() {
			return hashdati;
		}

		public void setHashdati(String hashdati) {
			this.hashdati = hashdati;
		}

		public String getHashdocumento() {
			return hashdocumento;
		}

		public void setHashdocumento(String hashdocumento) {
			this.hashdocumento = hashdocumento;
		}

		/**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

}


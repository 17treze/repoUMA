//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:54 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.dsequiregistrig;

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
 *         &lt;element name="EQUI_REGISTRI_DI_STALLA"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="REGSTA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_COM_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_PRO_SIGLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PROP_COGN_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DETEN_COGN_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="COD_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_UELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="IDENT_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_CERTIFICATO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="NUM_RIF_LOCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PASSAPORTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RAZZA_DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MANTELLO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MANTELLO_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DESC_MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="REGSTA_MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE_PROV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE_PROV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_CODICE_PROV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FM_CODICE_PROV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="INGRESSO_MM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="RIF_MODELLO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_MODELLO_IMGRESSO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="TIPO_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="PROVENIENZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="USCITA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *                   &lt;element name="DESC_MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="AZIENDA_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="ALLEV_ID_FISCALE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SPECIE_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="FM_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MACELLO_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="MACELLO_REG_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="CODICE_STATO_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="RIF_MODELLO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="DT_MODELLO_USCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
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
    "equiregistridistalla"
})
@XmlRootElement(name = "dsEQUI_REGISTRI_G")
public class DsEQUIREGISTRIG {

    @XmlElement(name = "EQUI_REGISTRI_DI_STALLA")
    protected List<DsEQUIREGISTRIG.EQUIREGISTRIDISTALLA> equiregistridistalla;

    /**
     * Gets the value of the equiregistridistalla property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the equiregistridistalla property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEQUIREGISTRIDISTALLA().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DsEQUIREGISTRIG.EQUIREGISTRIDISTALLA }
     * 
     * 
     */
    public List<DsEQUIREGISTRIG.EQUIREGISTRIDISTALLA> getEQUIREGISTRIDISTALLA() {
        if (equiregistridistalla == null) {
            equiregistridistalla = new ArrayList<DsEQUIREGISTRIG.EQUIREGISTRIDISTALLA>();
        }
        return this.equiregistridistalla;
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
     *         &lt;element name="AZIENDA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="INDIRIZZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CAP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LOCALITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_COM_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_PRO_SIGLA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PROP_COGN_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DETEN_COGN_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="COD_ELETTRONICO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_UELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="IDENT_NOME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_CERTIFICATO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="NUM_RIF_LOCALE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PASSAPORTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RAZZA_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RAZZA_DENOMINAZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MANTELLO_CODICE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MANTELLO_DESCRIZIONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_NASCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DT_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DESC_MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="REGSTA_MOTIVO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE_PROV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE_PROV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_CODICE_PROV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FM_CODICE_PROV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="INGRESSO_MM_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="RIF_MODELLO_INGRESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_MODELLO_IMGRESSO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="TIPO_PROVENIENZA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="PROVENIENZA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="USCITA_ID" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
     *         &lt;element name="DESC_MOTIVO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_USCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="AZIENDA_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ALLEV_ID_FISCALE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SPECIE_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="FM_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MACELLO_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MACELLO_REG_CODICE_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="CODICE_STATO_DEST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RIF_MODELLO_USCITA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="DT_MODELLO_USCITA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
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
        "aziendacodice",
        "allevidfiscale",
        "speciecodice",
        "denominazione",
        "indirizzo",
        "cap",
        "localita",
        "aziendacomdescrizione",
        "aziendaprosigla",
        "propcognnome",
        "detencognnome",
        "codelettronico",
        "codiceueln",
        "identnome",
        "numcertificato",
        "numriflocale",
        "passaporto",
        "razzacodice",
        "razzadenominazione",
        "sesso",
        "mantellocodice",
        "mantellodescrizione",
        "dtnascita",
        "dtingresso",
        "descmotivoingresso",
        "regstamotivoingresso",
        "aziendacodiceprov",
        "allevidfiscaleprov",
        "speciecodiceprov",
        "fmcodiceprov",
        "ingressommid",
        "rifmodelloingresso",
        "dtmodelloimgresso",
        "tipoprovenienza",
        "provenienzaid",
        "uscitaid",
        "descmotivouscita",
        "dtuscita",
        "aziendacodicedest",
        "allevidfiscaledest",
        "speciecodicedest",
        "fmcodicedest",
        "macellocodicedest",
        "macelloregcodicedest",
        "codicestatodest",
        "rifmodellouscita",
        "dtmodellouscita"
    })
    public static class EQUIREGISTRIDISTALLA {

        @XmlElement(name = "REGSTA_ID")
        protected BigDecimal regstaid;
        @XmlElement(name = "ALLEV_ID")
        protected BigDecimal allevid;
        @XmlElement(name = "AZIENDA_CODICE")
        protected String aziendacodice;
        @XmlElement(name = "ALLEV_ID_FISCALE")
        protected String allevidfiscale;
        @XmlElement(name = "SPECIE_CODICE")
        protected String speciecodice;
        @XmlElement(name = "DENOMINAZIONE")
        protected String denominazione;
        @XmlElement(name = "INDIRIZZO")
        protected String indirizzo;
        @XmlElement(name = "CAP")
        protected String cap;
        @XmlElement(name = "LOCALITA")
        protected String localita;
        @XmlElement(name = "AZIENDA_COM_DESCRIZIONE")
        protected String aziendacomdescrizione;
        @XmlElement(name = "AZIENDA_PRO_SIGLA")
        protected String aziendaprosigla;
        @XmlElement(name = "PROP_COGN_NOME")
        protected String propcognnome;
        @XmlElement(name = "DETEN_COGN_NOME")
        protected String detencognnome;
        @XmlElement(name = "COD_ELETTRONICO")
        protected String codelettronico;
        @XmlElement(name = "CODICE_UELN")
        protected String codiceueln;
        @XmlElement(name = "IDENT_NOME")
        protected String identnome;
        @XmlElement(name = "NUM_CERTIFICATO")
        protected String numcertificato;
        @XmlElement(name = "NUM_RIF_LOCALE")
        protected String numriflocale;
        @XmlElement(name = "PASSAPORTO")
        protected String passaporto;
        @XmlElement(name = "RAZZA_CODICE")
        protected String razzacodice;
        @XmlElement(name = "RAZZA_DENOMINAZIONE")
        protected String razzadenominazione;
        @XmlElement(name = "SESSO")
        protected String sesso;
        @XmlElement(name = "MANTELLO_CODICE")
        protected String mantellocodice;
        @XmlElement(name = "MANTELLO_DESCRIZIONE")
        protected String mantellodescrizione;
        @XmlElement(name = "DT_NASCITA")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtnascita;
        @XmlElement(name = "DT_INGRESSO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtingresso;
        @XmlElement(name = "DESC_MOTIVO_INGRESSO")
        protected String descmotivoingresso;
        @XmlElement(name = "REGSTA_MOTIVO_INGRESSO")
        protected String regstamotivoingresso;
        @XmlElement(name = "AZIENDA_CODICE_PROV")
        protected String aziendacodiceprov;
        @XmlElement(name = "ALLEV_ID_FISCALE_PROV")
        protected String allevidfiscaleprov;
        @XmlElement(name = "SPECIE_CODICE_PROV")
        protected String speciecodiceprov;
        @XmlElement(name = "FM_CODICE_PROV")
        protected String fmcodiceprov;
        @XmlElement(name = "INGRESSO_MM_ID")
        protected BigDecimal ingressommid;
        @XmlElement(name = "RIF_MODELLO_INGRESSO")
        protected String rifmodelloingresso;
        @XmlElement(name = "DT_MODELLO_IMGRESSO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtmodelloimgresso;
        @XmlElement(name = "TIPO_PROVENIENZA")
        protected String tipoprovenienza;
        @XmlElement(name = "PROVENIENZA_ID")
        protected BigDecimal provenienzaid;
        @XmlElement(name = "USCITA_ID")
        protected BigDecimal uscitaid;
        @XmlElement(name = "DESC_MOTIVO_USCITA")
        protected String descmotivouscita;
        @XmlElement(name = "DT_USCITA")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtuscita;
        @XmlElement(name = "AZIENDA_CODICE_DEST")
        protected String aziendacodicedest;
        @XmlElement(name = "ALLEV_ID_FISCALE_DEST")
        protected String allevidfiscaledest;
        @XmlElement(name = "SPECIE_CODICE_DEST")
        protected String speciecodicedest;
        @XmlElement(name = "FM_CODICE_DEST")
        protected String fmcodicedest;
        @XmlElement(name = "MACELLO_CODICE_DEST")
        protected String macellocodicedest;
        @XmlElement(name = "MACELLO_REG_CODICE_DEST")
        protected String macelloregcodicedest;
        @XmlElement(name = "CODICE_STATO_DEST")
        protected String codicestatodest;
        @XmlElement(name = "RIF_MODELLO_USCITA")
        protected String rifmodellouscita;
        @XmlElement(name = "DT_MODELLO_USCITA")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dtmodellouscita;

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
         * Recupera il valore della proprietà denominazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDENOMINAZIONE() {
            return denominazione;
        }

        /**
         * Imposta il valore della proprietà denominazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDENOMINAZIONE(String value) {
            this.denominazione = value;
        }

        /**
         * Recupera il valore della proprietà indirizzo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getINDIRIZZO() {
            return indirizzo;
        }

        /**
         * Imposta il valore della proprietà indirizzo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setINDIRIZZO(String value) {
            this.indirizzo = value;
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
         * Recupera il valore della proprietà aziendacomdescrizione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAZIENDACOMDESCRIZIONE() {
            return aziendacomdescrizione;
        }

        /**
         * Imposta il valore della proprietà aziendacomdescrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAZIENDACOMDESCRIZIONE(String value) {
            this.aziendacomdescrizione = value;
        }

        /**
         * Recupera il valore della proprietà aziendaprosigla.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAZIENDAPROSIGLA() {
            return aziendaprosigla;
        }

        /**
         * Imposta il valore della proprietà aziendaprosigla.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAZIENDAPROSIGLA(String value) {
            this.aziendaprosigla = value;
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
         * Recupera il valore della proprietà codelettronico.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODELETTRONICO() {
            return codelettronico;
        }

        /**
         * Imposta il valore della proprietà codelettronico.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODELETTRONICO(String value) {
            this.codelettronico = value;
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
         * Recupera il valore della proprietà numcertificato.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMCERTIFICATO() {
            return numcertificato;
        }

        /**
         * Imposta il valore della proprietà numcertificato.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMCERTIFICATO(String value) {
            this.numcertificato = value;
        }

        /**
         * Recupera il valore della proprietà numriflocale.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNUMRIFLOCALE() {
            return numriflocale;
        }

        /**
         * Imposta il valore della proprietà numriflocale.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNUMRIFLOCALE(String value) {
            this.numriflocale = value;
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
         * Recupera il valore della proprietà razzacodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRAZZACODICE() {
            return razzacodice;
        }

        /**
         * Imposta il valore della proprietà razzacodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRAZZACODICE(String value) {
            this.razzacodice = value;
        }

        /**
         * Recupera il valore della proprietà razzadenominazione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRAZZADENOMINAZIONE() {
            return razzadenominazione;
        }

        /**
         * Imposta il valore della proprietà razzadenominazione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRAZZADENOMINAZIONE(String value) {
            this.razzadenominazione = value;
        }

        /**
         * Recupera il valore della proprietà sesso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSESSO() {
            return sesso;
        }

        /**
         * Imposta il valore della proprietà sesso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSESSO(String value) {
            this.sesso = value;
        }

        /**
         * Recupera il valore della proprietà mantellocodice.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMANTELLOCODICE() {
            return mantellocodice;
        }

        /**
         * Imposta il valore della proprietà mantellocodice.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMANTELLOCODICE(String value) {
            this.mantellocodice = value;
        }

        /**
         * Recupera il valore della proprietà mantellodescrizione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMANTELLODESCRIZIONE() {
            return mantellodescrizione;
        }

        /**
         * Imposta il valore della proprietà mantellodescrizione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMANTELLODESCRIZIONE(String value) {
            this.mantellodescrizione = value;
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
         * Recupera il valore della proprietà descmotivoingresso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDESCMOTIVOINGRESSO() {
            return descmotivoingresso;
        }

        /**
         * Imposta il valore della proprietà descmotivoingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDESCMOTIVOINGRESSO(String value) {
            this.descmotivoingresso = value;
        }

        /**
         * Recupera il valore della proprietà regstamotivoingresso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getREGSTAMOTIVOINGRESSO() {
            return regstamotivoingresso;
        }

        /**
         * Imposta il valore della proprietà regstamotivoingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setREGSTAMOTIVOINGRESSO(String value) {
            this.regstamotivoingresso = value;
        }

        /**
         * Recupera il valore della proprietà aziendacodiceprov.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAZIENDACODICEPROV() {
            return aziendacodiceprov;
        }

        /**
         * Imposta il valore della proprietà aziendacodiceprov.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAZIENDACODICEPROV(String value) {
            this.aziendacodiceprov = value;
        }

        /**
         * Recupera il valore della proprietà allevidfiscaleprov.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getALLEVIDFISCALEPROV() {
            return allevidfiscaleprov;
        }

        /**
         * Imposta il valore della proprietà allevidfiscaleprov.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setALLEVIDFISCALEPROV(String value) {
            this.allevidfiscaleprov = value;
        }

        /**
         * Recupera il valore della proprietà speciecodiceprov.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECIECODICEPROV() {
            return speciecodiceprov;
        }

        /**
         * Imposta il valore della proprietà speciecodiceprov.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECIECODICEPROV(String value) {
            this.speciecodiceprov = value;
        }

        /**
         * Recupera il valore della proprietà fmcodiceprov.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFMCODICEPROV() {
            return fmcodiceprov;
        }

        /**
         * Imposta il valore della proprietà fmcodiceprov.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFMCODICEPROV(String value) {
            this.fmcodiceprov = value;
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
         * Recupera il valore della proprietà rifmodelloingresso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRIFMODELLOINGRESSO() {
            return rifmodelloingresso;
        }

        /**
         * Imposta il valore della proprietà rifmodelloingresso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRIFMODELLOINGRESSO(String value) {
            this.rifmodelloingresso = value;
        }

        /**
         * Recupera il valore della proprietà dtmodelloimgresso.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTMODELLOIMGRESSO() {
            return dtmodelloimgresso;
        }

        /**
         * Imposta il valore della proprietà dtmodelloimgresso.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTMODELLOIMGRESSO(XMLGregorianCalendar value) {
            this.dtmodelloimgresso = value;
        }

        /**
         * Recupera il valore della proprietà tipoprovenienza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTIPOPROVENIENZA() {
            return tipoprovenienza;
        }

        /**
         * Imposta il valore della proprietà tipoprovenienza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTIPOPROVENIENZA(String value) {
            this.tipoprovenienza = value;
        }

        /**
         * Recupera il valore della proprietà provenienzaid.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getPROVENIENZAID() {
            return provenienzaid;
        }

        /**
         * Imposta il valore della proprietà provenienzaid.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setPROVENIENZAID(BigDecimal value) {
            this.provenienzaid = value;
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
         * Recupera il valore della proprietà descmotivouscita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDESCMOTIVOUSCITA() {
            return descmotivouscita;
        }

        /**
         * Imposta il valore della proprietà descmotivouscita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDESCMOTIVOUSCITA(String value) {
            this.descmotivouscita = value;
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
         * Recupera il valore della proprietà aziendacodicedest.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAZIENDACODICEDEST() {
            return aziendacodicedest;
        }

        /**
         * Imposta il valore della proprietà aziendacodicedest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAZIENDACODICEDEST(String value) {
            this.aziendacodicedest = value;
        }

        /**
         * Recupera il valore della proprietà allevidfiscaledest.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getALLEVIDFISCALEDEST() {
            return allevidfiscaledest;
        }

        /**
         * Imposta il valore della proprietà allevidfiscaledest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setALLEVIDFISCALEDEST(String value) {
            this.allevidfiscaledest = value;
        }

        /**
         * Recupera il valore della proprietà speciecodicedest.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPECIECODICEDEST() {
            return speciecodicedest;
        }

        /**
         * Imposta il valore della proprietà speciecodicedest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPECIECODICEDEST(String value) {
            this.speciecodicedest = value;
        }

        /**
         * Recupera il valore della proprietà fmcodicedest.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFMCODICEDEST() {
            return fmcodicedest;
        }

        /**
         * Imposta il valore della proprietà fmcodicedest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFMCODICEDEST(String value) {
            this.fmcodicedest = value;
        }

        /**
         * Recupera il valore della proprietà macellocodicedest.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMACELLOCODICEDEST() {
            return macellocodicedest;
        }

        /**
         * Imposta il valore della proprietà macellocodicedest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMACELLOCODICEDEST(String value) {
            this.macellocodicedest = value;
        }

        /**
         * Recupera il valore della proprietà macelloregcodicedest.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMACELLOREGCODICEDEST() {
            return macelloregcodicedest;
        }

        /**
         * Imposta il valore della proprietà macelloregcodicedest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMACELLOREGCODICEDEST(String value) {
            this.macelloregcodicedest = value;
        }

        /**
         * Recupera il valore della proprietà codicestatodest.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCODICESTATODEST() {
            return codicestatodest;
        }

        /**
         * Imposta il valore della proprietà codicestatodest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCODICESTATODEST(String value) {
            this.codicestatodest = value;
        }

        /**
         * Recupera il valore della proprietà rifmodellouscita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRIFMODELLOUSCITA() {
            return rifmodellouscita;
        }

        /**
         * Imposta il valore della proprietà rifmodellouscita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRIFMODELLOUSCITA(String value) {
            this.rifmodellouscita = value;
        }

        /**
         * Recupera il valore della proprietà dtmodellouscita.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDTMODELLOUSCITA() {
            return dtmodellouscita;
        }

        /**
         * Imposta il valore della proprietà dtmodellouscita.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDTMODELLOUSCITA(XMLGregorianCalendar value) {
            this.dtmodellouscita = value;
        }

    }

}

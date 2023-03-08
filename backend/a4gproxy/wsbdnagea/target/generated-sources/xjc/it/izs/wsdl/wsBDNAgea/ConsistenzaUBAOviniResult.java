//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:04 PM CEST 
//


package it.izs.wsdl.wsBDNAgea;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="error_info" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="info" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="warning" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="error" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                             &lt;element name="des" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dati" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="dsALLEVAMENTI_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiALLEVAMENTI" minOccurs="0"/&gt;
 *                   &lt;element name="dsSCARICO_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiALLEVAMENTO" minOccurs="0"/&gt;
 *                   &lt;element name="dsCAPI_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI_BOVINI" minOccurs="0"/&gt;
 *                   &lt;element name="dsCAPIDOMMAC_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI_DOMANDA_MAC" minOccurs="0"/&gt;
 *                   &lt;element name="dsCAPIDOMPAC_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI_DOMANDA_PAC" minOccurs="0"/&gt;
 *                   &lt;element name="dsCAPIMAC_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI_MACELLATI" minOccurs="0"/&gt;
 *                   &lt;element name="dsDOMANDA_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiDOMANDA_PREMIO" minOccurs="0"/&gt;
 *                   &lt;element name="dsDOMANDACHIUSA_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiDOMANDA_CHIUSA" minOccurs="0"/&gt;
 *                   &lt;element name="dsFASCICOLO_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiFASCICOLO" minOccurs="0"/&gt;
 *                   &lt;element name="dsUNITATECNICHE_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUNITA_TECNICHE" minOccurs="0"/&gt;
 *                   &lt;element name="dsINFOCAPI_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI" minOccurs="0"/&gt;
 *                   &lt;element name="dsTEMPI_NOTIFICA_NASCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiTEMPI_NOTIFICA_NASCITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsTEMPI_NOTIFICA_INGRESSI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiTEMPI_NOTIFICA_INGRESSI" minOccurs="0"/&gt;
 *                   &lt;element name="dsTEMPI_NOTIFICA_USCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiTEMPI_NOTIFICA_USCITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsMOVIMENTAZIONI_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiMOVIMENTAZIONI" minOccurs="0"/&gt;
 *                   &lt;element name="dsANAGRAFICA_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiANAGRAFICA_ALLEVAMENTO" minOccurs="0"/&gt;
 *                   &lt;element name="dsRICHIESTA_CALCOLO_CONSISTENZA_A" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiAGEA_RICH_CALCOLO_CONSISTENZA" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONSISTENZA_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_ALLEVAMENTO" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONSISTENZA_ALLEVAMENTI_DETT" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_ALLEVAMENTO_DETT" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONSISTENZA_STAT_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_STAT_ALLEVAMENTO" minOccurs="0"/&gt;
 *                   &lt;element name="dsUBA_CONSISTENZA_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUBA_CONSISTENZA_ALLEVAMENTO" minOccurs="0"/&gt;
 *                   &lt;element name="dsUBA_CONSISTENZA_OVINI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUBA_CONSISTENZA_OVINI" minOccurs="0"/&gt;
 *                   &lt;element name="dsUBA_CENSIMENTI_ALLEVAMENTI_OVINI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUBA_CENSIMENTI_ALLEVAMENTO_OVINO" minOccurs="0"/&gt;
 *                   &lt;element name="dsUBA_CENSIMENTI_OVINI_2012" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUBA_CENSIMENTO_OVINO_2012" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONSISTENZA_PASCOLI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_PASCOLO" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONSISTENZA_PASCOLO2015" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_PASCOLO2015" minOccurs="0"/&gt;
 *                   &lt;element name="dsPASCOLI_VISITATI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiPASCOLO_VISITATO" minOccurs="0"/&gt;
 *                   &lt;element name="dsOVI_INGRESSI_PASCOLO" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_INGRESSI_PASCOLO" minOccurs="0"/&gt;
 *                   &lt;element name="dsOVI_USCITE_PASCOLO" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_USCITE_PASCOLO" minOccurs="0"/&gt;
 *                   &lt;element name="dsOVI_REGISTRI_PASCOLO" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_REGISTRI_PASCOLO" minOccurs="0"/&gt;
 *                   &lt;element name="dsCAMPIONE_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAMPIONE" minOccurs="0"/&gt;
 *                   &lt;element name="dsIRREGOLARITA_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA" minOccurs="0"/&gt;
 *                   &lt;element name="dsMACELLAZIONE_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiMACELLAZIONE" minOccurs="0"/&gt;
 *                   &lt;element name="dsNUM_CAPIMAC" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPIMAC" minOccurs="0"/&gt;
 *                   &lt;element name="dsALLEVAMENTI_LIBRO_IUS" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfALLEVAMENTO_LIBRO" minOccurs="0"/&gt;
 *                   &lt;element name="dsCAPI_LIBRO_IUS" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfCAPO_LIBRO" minOccurs="0"/&gt;
 *                   &lt;element name="dsCAPI_LIBRO_A" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPO" minOccurs="0"/&gt;
 *                   &lt;element name="dsALLEVAMENTI_LIBRO_A" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiALLEVAMENTI_LIBRO" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONSISTENZA_MANDRIA_LATTIERA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_MANDRIA_LATTIERA" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONTROLLI_ALLEVAMENTI_IR" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_IR" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONTROLLI_ALLEVAMENTI_BA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_BA" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONTROLLI_ALLEVAMENTI_SA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_SA" minOccurs="0"/&gt;
 *                   &lt;element name="dsCONTROLLI_ALLEVAMENTI_SV" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_SV" minOccurs="0"/&gt;
 *                   &lt;element name="dsIRREGOLARITA_ALLEVAMENTI_IR" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_IR" minOccurs="0"/&gt;
 *                   &lt;element name="dsIRREGOLARITA_ALLEVAMENTI_BA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_BA" minOccurs="0"/&gt;
 *                   &lt;element name="dsIRREGOLARITA_ALLEVAMENTI_SV" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_SV" minOccurs="0"/&gt;
 *                   &lt;element name="dsIRREGOLARITA_ALLEVAMENTI_SA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_SA" minOccurs="0"/&gt;
 *                   &lt;element name="dsOVI_TEMPI_NOTIFICA_NASCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_NASCITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsOVI_TEMPI_NOTIFICA_INGRESSI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_INGRESSI" minOccurs="0"/&gt;
 *                   &lt;element name="dsOVI_TEMPI_NOTIFICA_INGRESSI_PARTITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_INGRESSI_PARTITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsOVI_TEMPI_NOTIFICA_USCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_USCITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsOVI_TEMPI_NOTIFICA_USCITE_PARTITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_USCITE_PARTITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsSUI_TEMPI_NOTIFICA_NASCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_NASCITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsSUI_TEMPI_NOTIFICA_INGRESSI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_INGRESSI" minOccurs="0"/&gt;
 *                   &lt;element name="dsSUI_TEMPI_NOTIFICA_INGRESSI_PARTITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_INGRESSI_PARTITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsSUI_TEMPI_NOTIFICA_USCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_USCITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsSUI_TEMPI_NOTIFICA_USCITE_PARTITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_USCITE_PARTITE" minOccurs="0"/&gt;
 *                   &lt;element name="dsVARIAZIONI_CONTROLLI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiVARIAZIONE_CONTROLLO" minOccurs="0"/&gt;
 *                   &lt;element name="dsVERBALE_CHECK_LIST" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiVERBALE_CHECK_LIST" minOccurs="0"/&gt;
 *                   &lt;element name="dsUNITATECNICHE_AGEA_MUL" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUNITA_TECNICHE_MUL" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="tipoOutput" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "errorInfo",
    "dati"
})
@XmlRootElement(name = "Consistenza_UBA_OviniResult")
public class ConsistenzaUBAOviniResult {

    @XmlElement(name = "error_info")
    protected ConsistenzaUBAOviniResult.ErrorInfo errorInfo;
    protected ConsistenzaUBAOviniResult.Dati dati;
    @XmlAttribute(name = "tipoOutput")
    protected String tipoOutput;

    /**
     * Recupera il valore della proprietà errorInfo.
     * 
     * @return
     *     possible object is
     *     {@link ConsistenzaUBAOviniResult.ErrorInfo }
     *     
     */
    public ConsistenzaUBAOviniResult.ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    /**
     * Imposta il valore della proprietà errorInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsistenzaUBAOviniResult.ErrorInfo }
     *     
     */
    public void setErrorInfo(ConsistenzaUBAOviniResult.ErrorInfo value) {
        this.errorInfo = value;
    }

    /**
     * Recupera il valore della proprietà dati.
     * 
     * @return
     *     possible object is
     *     {@link ConsistenzaUBAOviniResult.Dati }
     *     
     */
    public ConsistenzaUBAOviniResult.Dati getDati() {
        return dati;
    }

    /**
     * Imposta il valore della proprietà dati.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsistenzaUBAOviniResult.Dati }
     *     
     */
    public void setDati(ConsistenzaUBAOviniResult.Dati value) {
        this.dati = value;
    }

    /**
     * Recupera il valore della proprietà tipoOutput.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoOutput() {
        return tipoOutput;
    }

    /**
     * Imposta il valore della proprietà tipoOutput.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoOutput(String value) {
        this.tipoOutput = value;
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
     *         &lt;element name="dsALLEVAMENTI_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiALLEVAMENTI" minOccurs="0"/&gt;
     *         &lt;element name="dsSCARICO_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiALLEVAMENTO" minOccurs="0"/&gt;
     *         &lt;element name="dsCAPI_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI_BOVINI" minOccurs="0"/&gt;
     *         &lt;element name="dsCAPIDOMMAC_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI_DOMANDA_MAC" minOccurs="0"/&gt;
     *         &lt;element name="dsCAPIDOMPAC_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI_DOMANDA_PAC" minOccurs="0"/&gt;
     *         &lt;element name="dsCAPIMAC_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI_MACELLATI" minOccurs="0"/&gt;
     *         &lt;element name="dsDOMANDA_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiDOMANDA_PREMIO" minOccurs="0"/&gt;
     *         &lt;element name="dsDOMANDACHIUSA_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiDOMANDA_CHIUSA" minOccurs="0"/&gt;
     *         &lt;element name="dsFASCICOLO_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiFASCICOLO" minOccurs="0"/&gt;
     *         &lt;element name="dsUNITATECNICHE_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUNITA_TECNICHE" minOccurs="0"/&gt;
     *         &lt;element name="dsINFOCAPI_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPI" minOccurs="0"/&gt;
     *         &lt;element name="dsTEMPI_NOTIFICA_NASCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiTEMPI_NOTIFICA_NASCITE" minOccurs="0"/&gt;
     *         &lt;element name="dsTEMPI_NOTIFICA_INGRESSI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiTEMPI_NOTIFICA_INGRESSI" minOccurs="0"/&gt;
     *         &lt;element name="dsTEMPI_NOTIFICA_USCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiTEMPI_NOTIFICA_USCITE" minOccurs="0"/&gt;
     *         &lt;element name="dsMOVIMENTAZIONI_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiMOVIMENTAZIONI" minOccurs="0"/&gt;
     *         &lt;element name="dsANAGRAFICA_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiANAGRAFICA_ALLEVAMENTO" minOccurs="0"/&gt;
     *         &lt;element name="dsRICHIESTA_CALCOLO_CONSISTENZA_A" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiAGEA_RICH_CALCOLO_CONSISTENZA" minOccurs="0"/&gt;
     *         &lt;element name="dsCONSISTENZA_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_ALLEVAMENTO" minOccurs="0"/&gt;
     *         &lt;element name="dsCONSISTENZA_ALLEVAMENTI_DETT" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_ALLEVAMENTO_DETT" minOccurs="0"/&gt;
     *         &lt;element name="dsCONSISTENZA_STAT_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_STAT_ALLEVAMENTO" minOccurs="0"/&gt;
     *         &lt;element name="dsUBA_CONSISTENZA_ALLEVAMENTI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUBA_CONSISTENZA_ALLEVAMENTO" minOccurs="0"/&gt;
     *         &lt;element name="dsUBA_CONSISTENZA_OVINI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUBA_CONSISTENZA_OVINI" minOccurs="0"/&gt;
     *         &lt;element name="dsUBA_CENSIMENTI_ALLEVAMENTI_OVINI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUBA_CENSIMENTI_ALLEVAMENTO_OVINO" minOccurs="0"/&gt;
     *         &lt;element name="dsUBA_CENSIMENTI_OVINI_2012" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUBA_CENSIMENTO_OVINO_2012" minOccurs="0"/&gt;
     *         &lt;element name="dsCONSISTENZA_PASCOLI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_PASCOLO" minOccurs="0"/&gt;
     *         &lt;element name="dsCONSISTENZA_PASCOLO2015" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_PASCOLO2015" minOccurs="0"/&gt;
     *         &lt;element name="dsPASCOLI_VISITATI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiPASCOLO_VISITATO" minOccurs="0"/&gt;
     *         &lt;element name="dsOVI_INGRESSI_PASCOLO" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_INGRESSI_PASCOLO" minOccurs="0"/&gt;
     *         &lt;element name="dsOVI_USCITE_PASCOLO" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_USCITE_PASCOLO" minOccurs="0"/&gt;
     *         &lt;element name="dsOVI_REGISTRI_PASCOLO" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_REGISTRI_PASCOLO" minOccurs="0"/&gt;
     *         &lt;element name="dsCAMPIONE_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAMPIONE" minOccurs="0"/&gt;
     *         &lt;element name="dsIRREGOLARITA_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA" minOccurs="0"/&gt;
     *         &lt;element name="dsMACELLAZIONE_AGEA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiMACELLAZIONE" minOccurs="0"/&gt;
     *         &lt;element name="dsNUM_CAPIMAC" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPIMAC" minOccurs="0"/&gt;
     *         &lt;element name="dsALLEVAMENTI_LIBRO_IUS" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfALLEVAMENTO_LIBRO" minOccurs="0"/&gt;
     *         &lt;element name="dsCAPI_LIBRO_IUS" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfCAPO_LIBRO" minOccurs="0"/&gt;
     *         &lt;element name="dsCAPI_LIBRO_A" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCAPO" minOccurs="0"/&gt;
     *         &lt;element name="dsALLEVAMENTI_LIBRO_A" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiALLEVAMENTI_LIBRO" minOccurs="0"/&gt;
     *         &lt;element name="dsCONSISTENZA_MANDRIA_LATTIERA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONSISTENZA_MANDRIA_LATTIERA" minOccurs="0"/&gt;
     *         &lt;element name="dsCONTROLLI_ALLEVAMENTI_IR" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_IR" minOccurs="0"/&gt;
     *         &lt;element name="dsCONTROLLI_ALLEVAMENTI_BA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_BA" minOccurs="0"/&gt;
     *         &lt;element name="dsCONTROLLI_ALLEVAMENTI_SA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_SA" minOccurs="0"/&gt;
     *         &lt;element name="dsCONTROLLI_ALLEVAMENTI_SV" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiCONTROLLO_ALLEVAMENTO_SV" minOccurs="0"/&gt;
     *         &lt;element name="dsIRREGOLARITA_ALLEVAMENTI_IR" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_IR" minOccurs="0"/&gt;
     *         &lt;element name="dsIRREGOLARITA_ALLEVAMENTI_BA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_BA" minOccurs="0"/&gt;
     *         &lt;element name="dsIRREGOLARITA_ALLEVAMENTI_SV" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_SV" minOccurs="0"/&gt;
     *         &lt;element name="dsIRREGOLARITA_ALLEVAMENTI_SA" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiIRREGOLARITA_ALLEVAMENTO_SA" minOccurs="0"/&gt;
     *         &lt;element name="dsOVI_TEMPI_NOTIFICA_NASCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_NASCITE" minOccurs="0"/&gt;
     *         &lt;element name="dsOVI_TEMPI_NOTIFICA_INGRESSI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_INGRESSI" minOccurs="0"/&gt;
     *         &lt;element name="dsOVI_TEMPI_NOTIFICA_INGRESSI_PARTITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_INGRESSI_PARTITE" minOccurs="0"/&gt;
     *         &lt;element name="dsOVI_TEMPI_NOTIFICA_USCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_USCITE" minOccurs="0"/&gt;
     *         &lt;element name="dsOVI_TEMPI_NOTIFICA_USCITE_PARTITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiOVI_TEMPI_NOTIFICA_USCITE_PARTITE" minOccurs="0"/&gt;
     *         &lt;element name="dsSUI_TEMPI_NOTIFICA_NASCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_NASCITE" minOccurs="0"/&gt;
     *         &lt;element name="dsSUI_TEMPI_NOTIFICA_INGRESSI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_INGRESSI" minOccurs="0"/&gt;
     *         &lt;element name="dsSUI_TEMPI_NOTIFICA_INGRESSI_PARTITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_INGRESSI_PARTITE" minOccurs="0"/&gt;
     *         &lt;element name="dsSUI_TEMPI_NOTIFICA_USCITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_USCITE" minOccurs="0"/&gt;
     *         &lt;element name="dsSUI_TEMPI_NOTIFICA_USCITE_PARTITE" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiSUI_TEMPI_NOTIFICA_USCITE_PARTITE" minOccurs="0"/&gt;
     *         &lt;element name="dsVARIAZIONI_CONTROLLI" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiVARIAZIONE_CONTROLLO" minOccurs="0"/&gt;
     *         &lt;element name="dsVERBALE_CHECK_LIST" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiVERBALE_CHECK_LIST" minOccurs="0"/&gt;
     *         &lt;element name="dsUNITATECNICHE_AGEA_MUL" type="{http://bdr.izs.it/webservices/ResponseQuery.xsd}ArrayOfRootDatiUNITA_TECNICHE_MUL" minOccurs="0"/&gt;
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
        "dsALLEVAMENTIAGEA",
        "dsSCARICOALLEVAMENTI",
        "dsCAPIAGEA",
        "dsCAPIDOMMACAGEA",
        "dsCAPIDOMPACAGEA",
        "dsCAPIMACAGEA",
        "dsDOMANDAAGEA",
        "dsDOMANDACHIUSAAGEA",
        "dsFASCICOLOAGEA",
        "dsUNITATECNICHEAGEA",
        "dsINFOCAPIAGEA",
        "dsTEMPINOTIFICANASCITE",
        "dsTEMPINOTIFICAINGRESSI",
        "dsTEMPINOTIFICAUSCITE",
        "dsMOVIMENTAZIONIAGEA",
        "dsANAGRAFICAALLEVAMENTI",
        "dsRICHIESTACALCOLOCONSISTENZAA",
        "dsCONSISTENZAALLEVAMENTI",
        "dsCONSISTENZAALLEVAMENTIDETT",
        "dsCONSISTENZASTATALLEVAMENTI",
        "dsUBACONSISTENZAALLEVAMENTI",
        "dsUBACONSISTENZAOVINI",
        "dsUBACENSIMENTIALLEVAMENTIOVINI",
        "dsUBACENSIMENTIOVINI2012",
        "dsCONSISTENZAPASCOLI",
        "dsCONSISTENZAPASCOLO2015",
        "dsPASCOLIVISITATI",
        "dsOVIINGRESSIPASCOLO",
        "dsOVIUSCITEPASCOLO",
        "dsOVIREGISTRIPASCOLO",
        "dsCAMPIONEAGEA",
        "dsIRREGOLARITAAGEA",
        "dsMACELLAZIONEAGEA",
        "dsNUMCAPIMAC",
        "dsALLEVAMENTILIBROIUS",
        "dsCAPILIBROIUS",
        "dsCAPILIBROA",
        "dsALLEVAMENTILIBROA",
        "dsCONSISTENZAMANDRIALATTIERA",
        "dsCONTROLLIALLEVAMENTIIR",
        "dsCONTROLLIALLEVAMENTIBA",
        "dsCONTROLLIALLEVAMENTISA",
        "dsCONTROLLIALLEVAMENTISV",
        "dsIRREGOLARITAALLEVAMENTIIR",
        "dsIRREGOLARITAALLEVAMENTIBA",
        "dsIRREGOLARITAALLEVAMENTISV",
        "dsIRREGOLARITAALLEVAMENTISA",
        "dsOVITEMPINOTIFICANASCITE",
        "dsOVITEMPINOTIFICAINGRESSI",
        "dsOVITEMPINOTIFICAINGRESSIPARTITE",
        "dsOVITEMPINOTIFICAUSCITE",
        "dsOVITEMPINOTIFICAUSCITEPARTITE",
        "dsSUITEMPINOTIFICANASCITE",
        "dsSUITEMPINOTIFICAINGRESSI",
        "dsSUITEMPINOTIFICAINGRESSIPARTITE",
        "dsSUITEMPINOTIFICAUSCITE",
        "dsSUITEMPINOTIFICAUSCITEPARTITE",
        "dsVARIAZIONICONTROLLI",
        "dsVERBALECHECKLIST",
        "dsUNITATECNICHEAGEAMUL"
    })
    public static class Dati {

        @XmlElement(name = "dsALLEVAMENTI_AGEA")
        protected ArrayOfRootDatiALLEVAMENTI dsALLEVAMENTIAGEA;
        @XmlElement(name = "dsSCARICO_ALLEVAMENTI")
        protected ArrayOfRootDatiALLEVAMENTO dsSCARICOALLEVAMENTI;
        @XmlElement(name = "dsCAPI_AGEA")
        protected ArrayOfRootDatiCAPIBOVINI dsCAPIAGEA;
        @XmlElement(name = "dsCAPIDOMMAC_AGEA")
        protected ArrayOfRootDatiCAPIDOMANDAMAC dsCAPIDOMMACAGEA;
        @XmlElement(name = "dsCAPIDOMPAC_AGEA")
        protected ArrayOfRootDatiCAPIDOMANDAPAC dsCAPIDOMPACAGEA;
        @XmlElement(name = "dsCAPIMAC_AGEA")
        protected ArrayOfRootDatiCAPIMACELLATI dsCAPIMACAGEA;
        @XmlElement(name = "dsDOMANDA_AGEA")
        protected ArrayOfRootDatiDOMANDAPREMIO dsDOMANDAAGEA;
        @XmlElement(name = "dsDOMANDACHIUSA_AGEA")
        protected ArrayOfRootDatiDOMANDACHIUSA dsDOMANDACHIUSAAGEA;
        @XmlElement(name = "dsFASCICOLO_AGEA")
        protected ArrayOfRootDatiFASCICOLO dsFASCICOLOAGEA;
        @XmlElement(name = "dsUNITATECNICHE_AGEA")
        protected ArrayOfRootDatiUNITATECNICHE dsUNITATECNICHEAGEA;
        @XmlElement(name = "dsINFOCAPI_AGEA")
        protected ArrayOfRootDatiCAPI dsINFOCAPIAGEA;
        @XmlElement(name = "dsTEMPI_NOTIFICA_NASCITE")
        protected ArrayOfRootDatiTEMPINOTIFICANASCITE dsTEMPINOTIFICANASCITE;
        @XmlElement(name = "dsTEMPI_NOTIFICA_INGRESSI")
        protected ArrayOfRootDatiTEMPINOTIFICAINGRESSI dsTEMPINOTIFICAINGRESSI;
        @XmlElement(name = "dsTEMPI_NOTIFICA_USCITE")
        protected ArrayOfRootDatiTEMPINOTIFICAUSCITE dsTEMPINOTIFICAUSCITE;
        @XmlElement(name = "dsMOVIMENTAZIONI_AGEA")
        protected ArrayOfRootDatiMOVIMENTAZIONI dsMOVIMENTAZIONIAGEA;
        @XmlElement(name = "dsANAGRAFICA_ALLEVAMENTI")
        protected ArrayOfRootDatiANAGRAFICAALLEVAMENTO dsANAGRAFICAALLEVAMENTI;
        @XmlElement(name = "dsRICHIESTA_CALCOLO_CONSISTENZA_A")
        protected ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA dsRICHIESTACALCOLOCONSISTENZAA;
        @XmlElement(name = "dsCONSISTENZA_ALLEVAMENTI")
        protected ArrayOfRootDatiCONSISTENZAALLEVAMENTO dsCONSISTENZAALLEVAMENTI;
        @XmlElement(name = "dsCONSISTENZA_ALLEVAMENTI_DETT")
        protected ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT dsCONSISTENZAALLEVAMENTIDETT;
        @XmlElement(name = "dsCONSISTENZA_STAT_ALLEVAMENTI")
        protected ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO dsCONSISTENZASTATALLEVAMENTI;
        @XmlElement(name = "dsUBA_CONSISTENZA_ALLEVAMENTI")
        protected ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO dsUBACONSISTENZAALLEVAMENTI;
        @XmlElement(name = "dsUBA_CONSISTENZA_OVINI")
        protected ArrayOfRootDatiUBACONSISTENZAOVINI dsUBACONSISTENZAOVINI;
        @XmlElement(name = "dsUBA_CENSIMENTI_ALLEVAMENTI_OVINI")
        protected ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO dsUBACENSIMENTIALLEVAMENTIOVINI;
        @XmlElement(name = "dsUBA_CENSIMENTI_OVINI_2012")
        protected ArrayOfRootDatiUBACENSIMENTOOVINO2012 dsUBACENSIMENTIOVINI2012;
        @XmlElement(name = "dsCONSISTENZA_PASCOLI")
        protected ArrayOfRootDatiCONSISTENZAPASCOLO dsCONSISTENZAPASCOLI;
        @XmlElement(name = "dsCONSISTENZA_PASCOLO2015")
        protected ArrayOfRootDatiCONSISTENZAPASCOLO2015 dsCONSISTENZAPASCOLO2015;
        @XmlElement(name = "dsPASCOLI_VISITATI")
        protected ArrayOfRootDatiPASCOLOVISITATO dsPASCOLIVISITATI;
        @XmlElement(name = "dsOVI_INGRESSI_PASCOLO")
        protected ArrayOfRootDatiOVIINGRESSIPASCOLO dsOVIINGRESSIPASCOLO;
        @XmlElement(name = "dsOVI_USCITE_PASCOLO")
        protected ArrayOfRootDatiOVIUSCITEPASCOLO dsOVIUSCITEPASCOLO;
        @XmlElement(name = "dsOVI_REGISTRI_PASCOLO")
        protected ArrayOfRootDatiOVIREGISTRIPASCOLO dsOVIREGISTRIPASCOLO;
        @XmlElement(name = "dsCAMPIONE_AGEA")
        protected ArrayOfRootDatiCAMPIONE dsCAMPIONEAGEA;
        @XmlElement(name = "dsIRREGOLARITA_AGEA")
        protected ArrayOfRootDatiIRREGOLARITA dsIRREGOLARITAAGEA;
        @XmlElement(name = "dsMACELLAZIONE_AGEA")
        protected ArrayOfRootDatiMACELLAZIONE dsMACELLAZIONEAGEA;
        @XmlElement(name = "dsNUM_CAPIMAC")
        protected ArrayOfRootDatiCAPIMAC dsNUMCAPIMAC;
        @XmlElement(name = "dsALLEVAMENTI_LIBRO_IUS")
        protected ArrayOfALLEVAMENTOLIBRO dsALLEVAMENTILIBROIUS;
        @XmlElement(name = "dsCAPI_LIBRO_IUS")
        protected ArrayOfCAPOLIBRO dsCAPILIBROIUS;
        @XmlElement(name = "dsCAPI_LIBRO_A")
        protected ArrayOfRootDatiCAPO dsCAPILIBROA;
        @XmlElement(name = "dsALLEVAMENTI_LIBRO_A")
        protected ArrayOfRootDatiALLEVAMENTILIBRO dsALLEVAMENTILIBROA;
        @XmlElement(name = "dsCONSISTENZA_MANDRIA_LATTIERA")
        protected ArrayOfRootDatiCONSISTENZAMANDRIALATTIERA dsCONSISTENZAMANDRIALATTIERA;
        @XmlElement(name = "dsCONTROLLI_ALLEVAMENTI_IR")
        protected ArrayOfRootDatiCONTROLLOALLEVAMENTOIR dsCONTROLLIALLEVAMENTIIR;
        @XmlElement(name = "dsCONTROLLI_ALLEVAMENTI_BA")
        protected ArrayOfRootDatiCONTROLLOALLEVAMENTOBA dsCONTROLLIALLEVAMENTIBA;
        @XmlElement(name = "dsCONTROLLI_ALLEVAMENTI_SA")
        protected ArrayOfRootDatiCONTROLLOALLEVAMENTOSA dsCONTROLLIALLEVAMENTISA;
        @XmlElement(name = "dsCONTROLLI_ALLEVAMENTI_SV")
        protected ArrayOfRootDatiCONTROLLOALLEVAMENTOSV dsCONTROLLIALLEVAMENTISV;
        @XmlElement(name = "dsIRREGOLARITA_ALLEVAMENTI_IR")
        protected ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR dsIRREGOLARITAALLEVAMENTIIR;
        @XmlElement(name = "dsIRREGOLARITA_ALLEVAMENTI_BA")
        protected ArrayOfRootDatiIRREGOLARITAALLEVAMENTOBA dsIRREGOLARITAALLEVAMENTIBA;
        @XmlElement(name = "dsIRREGOLARITA_ALLEVAMENTI_SV")
        protected ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSV dsIRREGOLARITAALLEVAMENTISV;
        @XmlElement(name = "dsIRREGOLARITA_ALLEVAMENTI_SA")
        protected ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA dsIRREGOLARITAALLEVAMENTISA;
        @XmlElement(name = "dsOVI_TEMPI_NOTIFICA_NASCITE")
        protected ArrayOfRootDatiOVITEMPINOTIFICANASCITE dsOVITEMPINOTIFICANASCITE;
        @XmlElement(name = "dsOVI_TEMPI_NOTIFICA_INGRESSI")
        protected ArrayOfRootDatiOVITEMPINOTIFICAINGRESSI dsOVITEMPINOTIFICAINGRESSI;
        @XmlElement(name = "dsOVI_TEMPI_NOTIFICA_INGRESSI_PARTITE")
        protected ArrayOfRootDatiOVITEMPINOTIFICAINGRESSIPARTITE dsOVITEMPINOTIFICAINGRESSIPARTITE;
        @XmlElement(name = "dsOVI_TEMPI_NOTIFICA_USCITE")
        protected ArrayOfRootDatiOVITEMPINOTIFICAUSCITE dsOVITEMPINOTIFICAUSCITE;
        @XmlElement(name = "dsOVI_TEMPI_NOTIFICA_USCITE_PARTITE")
        protected ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE dsOVITEMPINOTIFICAUSCITEPARTITE;
        @XmlElement(name = "dsSUI_TEMPI_NOTIFICA_NASCITE")
        protected ArrayOfRootDatiSUITEMPINOTIFICANASCITE dsSUITEMPINOTIFICANASCITE;
        @XmlElement(name = "dsSUI_TEMPI_NOTIFICA_INGRESSI")
        protected ArrayOfRootDatiSUITEMPINOTIFICAINGRESSI dsSUITEMPINOTIFICAINGRESSI;
        @XmlElement(name = "dsSUI_TEMPI_NOTIFICA_INGRESSI_PARTITE")
        protected ArrayOfRootDatiSUITEMPINOTIFICAINGRESSIPARTITE dsSUITEMPINOTIFICAINGRESSIPARTITE;
        @XmlElement(name = "dsSUI_TEMPI_NOTIFICA_USCITE")
        protected ArrayOfRootDatiSUITEMPINOTIFICAUSCITE dsSUITEMPINOTIFICAUSCITE;
        @XmlElement(name = "dsSUI_TEMPI_NOTIFICA_USCITE_PARTITE")
        protected ArrayOfRootDatiSUITEMPINOTIFICAUSCITEPARTITE dsSUITEMPINOTIFICAUSCITEPARTITE;
        @XmlElement(name = "dsVARIAZIONI_CONTROLLI")
        protected ArrayOfRootDatiVARIAZIONECONTROLLO dsVARIAZIONICONTROLLI;
        @XmlElement(name = "dsVERBALE_CHECK_LIST")
        protected ArrayOfRootDatiVERBALECHECKLIST dsVERBALECHECKLIST;
        @XmlElement(name = "dsUNITATECNICHE_AGEA_MUL")
        protected ArrayOfRootDatiUNITATECNICHEMUL dsUNITATECNICHEAGEAMUL;

        /**
         * Recupera il valore della proprietà dsALLEVAMENTIAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiALLEVAMENTI }
         *     
         */
        public ArrayOfRootDatiALLEVAMENTI getDsALLEVAMENTIAGEA() {
            return dsALLEVAMENTIAGEA;
        }

        /**
         * Imposta il valore della proprietà dsALLEVAMENTIAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiALLEVAMENTI }
         *     
         */
        public void setDsALLEVAMENTIAGEA(ArrayOfRootDatiALLEVAMENTI value) {
            this.dsALLEVAMENTIAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsSCARICOALLEVAMENTI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiALLEVAMENTO }
         *     
         */
        public ArrayOfRootDatiALLEVAMENTO getDsSCARICOALLEVAMENTI() {
            return dsSCARICOALLEVAMENTI;
        }

        /**
         * Imposta il valore della proprietà dsSCARICOALLEVAMENTI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiALLEVAMENTO }
         *     
         */
        public void setDsSCARICOALLEVAMENTI(ArrayOfRootDatiALLEVAMENTO value) {
            this.dsSCARICOALLEVAMENTI = value;
        }

        /**
         * Recupera il valore della proprietà dsCAPIAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCAPIBOVINI }
         *     
         */
        public ArrayOfRootDatiCAPIBOVINI getDsCAPIAGEA() {
            return dsCAPIAGEA;
        }

        /**
         * Imposta il valore della proprietà dsCAPIAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCAPIBOVINI }
         *     
         */
        public void setDsCAPIAGEA(ArrayOfRootDatiCAPIBOVINI value) {
            this.dsCAPIAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsCAPIDOMMACAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCAPIDOMANDAMAC }
         *     
         */
        public ArrayOfRootDatiCAPIDOMANDAMAC getDsCAPIDOMMACAGEA() {
            return dsCAPIDOMMACAGEA;
        }

        /**
         * Imposta il valore della proprietà dsCAPIDOMMACAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCAPIDOMANDAMAC }
         *     
         */
        public void setDsCAPIDOMMACAGEA(ArrayOfRootDatiCAPIDOMANDAMAC value) {
            this.dsCAPIDOMMACAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsCAPIDOMPACAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCAPIDOMANDAPAC }
         *     
         */
        public ArrayOfRootDatiCAPIDOMANDAPAC getDsCAPIDOMPACAGEA() {
            return dsCAPIDOMPACAGEA;
        }

        /**
         * Imposta il valore della proprietà dsCAPIDOMPACAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCAPIDOMANDAPAC }
         *     
         */
        public void setDsCAPIDOMPACAGEA(ArrayOfRootDatiCAPIDOMANDAPAC value) {
            this.dsCAPIDOMPACAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsCAPIMACAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCAPIMACELLATI }
         *     
         */
        public ArrayOfRootDatiCAPIMACELLATI getDsCAPIMACAGEA() {
            return dsCAPIMACAGEA;
        }

        /**
         * Imposta il valore della proprietà dsCAPIMACAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCAPIMACELLATI }
         *     
         */
        public void setDsCAPIMACAGEA(ArrayOfRootDatiCAPIMACELLATI value) {
            this.dsCAPIMACAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsDOMANDAAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiDOMANDAPREMIO }
         *     
         */
        public ArrayOfRootDatiDOMANDAPREMIO getDsDOMANDAAGEA() {
            return dsDOMANDAAGEA;
        }

        /**
         * Imposta il valore della proprietà dsDOMANDAAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiDOMANDAPREMIO }
         *     
         */
        public void setDsDOMANDAAGEA(ArrayOfRootDatiDOMANDAPREMIO value) {
            this.dsDOMANDAAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsDOMANDACHIUSAAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiDOMANDACHIUSA }
         *     
         */
        public ArrayOfRootDatiDOMANDACHIUSA getDsDOMANDACHIUSAAGEA() {
            return dsDOMANDACHIUSAAGEA;
        }

        /**
         * Imposta il valore della proprietà dsDOMANDACHIUSAAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiDOMANDACHIUSA }
         *     
         */
        public void setDsDOMANDACHIUSAAGEA(ArrayOfRootDatiDOMANDACHIUSA value) {
            this.dsDOMANDACHIUSAAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsFASCICOLOAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiFASCICOLO }
         *     
         */
        public ArrayOfRootDatiFASCICOLO getDsFASCICOLOAGEA() {
            return dsFASCICOLOAGEA;
        }

        /**
         * Imposta il valore della proprietà dsFASCICOLOAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiFASCICOLO }
         *     
         */
        public void setDsFASCICOLOAGEA(ArrayOfRootDatiFASCICOLO value) {
            this.dsFASCICOLOAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsUNITATECNICHEAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiUNITATECNICHE }
         *     
         */
        public ArrayOfRootDatiUNITATECNICHE getDsUNITATECNICHEAGEA() {
            return dsUNITATECNICHEAGEA;
        }

        /**
         * Imposta il valore della proprietà dsUNITATECNICHEAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiUNITATECNICHE }
         *     
         */
        public void setDsUNITATECNICHEAGEA(ArrayOfRootDatiUNITATECNICHE value) {
            this.dsUNITATECNICHEAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsINFOCAPIAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCAPI }
         *     
         */
        public ArrayOfRootDatiCAPI getDsINFOCAPIAGEA() {
            return dsINFOCAPIAGEA;
        }

        /**
         * Imposta il valore della proprietà dsINFOCAPIAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCAPI }
         *     
         */
        public void setDsINFOCAPIAGEA(ArrayOfRootDatiCAPI value) {
            this.dsINFOCAPIAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsTEMPINOTIFICANASCITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiTEMPINOTIFICANASCITE }
         *     
         */
        public ArrayOfRootDatiTEMPINOTIFICANASCITE getDsTEMPINOTIFICANASCITE() {
            return dsTEMPINOTIFICANASCITE;
        }

        /**
         * Imposta il valore della proprietà dsTEMPINOTIFICANASCITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiTEMPINOTIFICANASCITE }
         *     
         */
        public void setDsTEMPINOTIFICANASCITE(ArrayOfRootDatiTEMPINOTIFICANASCITE value) {
            this.dsTEMPINOTIFICANASCITE = value;
        }

        /**
         * Recupera il valore della proprietà dsTEMPINOTIFICAINGRESSI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiTEMPINOTIFICAINGRESSI }
         *     
         */
        public ArrayOfRootDatiTEMPINOTIFICAINGRESSI getDsTEMPINOTIFICAINGRESSI() {
            return dsTEMPINOTIFICAINGRESSI;
        }

        /**
         * Imposta il valore della proprietà dsTEMPINOTIFICAINGRESSI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiTEMPINOTIFICAINGRESSI }
         *     
         */
        public void setDsTEMPINOTIFICAINGRESSI(ArrayOfRootDatiTEMPINOTIFICAINGRESSI value) {
            this.dsTEMPINOTIFICAINGRESSI = value;
        }

        /**
         * Recupera il valore della proprietà dsTEMPINOTIFICAUSCITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiTEMPINOTIFICAUSCITE }
         *     
         */
        public ArrayOfRootDatiTEMPINOTIFICAUSCITE getDsTEMPINOTIFICAUSCITE() {
            return dsTEMPINOTIFICAUSCITE;
        }

        /**
         * Imposta il valore della proprietà dsTEMPINOTIFICAUSCITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiTEMPINOTIFICAUSCITE }
         *     
         */
        public void setDsTEMPINOTIFICAUSCITE(ArrayOfRootDatiTEMPINOTIFICAUSCITE value) {
            this.dsTEMPINOTIFICAUSCITE = value;
        }

        /**
         * Recupera il valore della proprietà dsMOVIMENTAZIONIAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiMOVIMENTAZIONI }
         *     
         */
        public ArrayOfRootDatiMOVIMENTAZIONI getDsMOVIMENTAZIONIAGEA() {
            return dsMOVIMENTAZIONIAGEA;
        }

        /**
         * Imposta il valore della proprietà dsMOVIMENTAZIONIAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiMOVIMENTAZIONI }
         *     
         */
        public void setDsMOVIMENTAZIONIAGEA(ArrayOfRootDatiMOVIMENTAZIONI value) {
            this.dsMOVIMENTAZIONIAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsANAGRAFICAALLEVAMENTI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiANAGRAFICAALLEVAMENTO }
         *     
         */
        public ArrayOfRootDatiANAGRAFICAALLEVAMENTO getDsANAGRAFICAALLEVAMENTI() {
            return dsANAGRAFICAALLEVAMENTI;
        }

        /**
         * Imposta il valore della proprietà dsANAGRAFICAALLEVAMENTI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiANAGRAFICAALLEVAMENTO }
         *     
         */
        public void setDsANAGRAFICAALLEVAMENTI(ArrayOfRootDatiANAGRAFICAALLEVAMENTO value) {
            this.dsANAGRAFICAALLEVAMENTI = value;
        }

        /**
         * Recupera il valore della proprietà dsRICHIESTACALCOLOCONSISTENZAA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA }
         *     
         */
        public ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA getDsRICHIESTACALCOLOCONSISTENZAA() {
            return dsRICHIESTACALCOLOCONSISTENZAA;
        }

        /**
         * Imposta il valore della proprietà dsRICHIESTACALCOLOCONSISTENZAA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA }
         *     
         */
        public void setDsRICHIESTACALCOLOCONSISTENZAA(ArrayOfRootDatiAGEARICHCALCOLOCONSISTENZA value) {
            this.dsRICHIESTACALCOLOCONSISTENZAA = value;
        }

        /**
         * Recupera il valore della proprietà dsCONSISTENZAALLEVAMENTI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONSISTENZAALLEVAMENTO }
         *     
         */
        public ArrayOfRootDatiCONSISTENZAALLEVAMENTO getDsCONSISTENZAALLEVAMENTI() {
            return dsCONSISTENZAALLEVAMENTI;
        }

        /**
         * Imposta il valore della proprietà dsCONSISTENZAALLEVAMENTI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONSISTENZAALLEVAMENTO }
         *     
         */
        public void setDsCONSISTENZAALLEVAMENTI(ArrayOfRootDatiCONSISTENZAALLEVAMENTO value) {
            this.dsCONSISTENZAALLEVAMENTI = value;
        }

        /**
         * Recupera il valore della proprietà dsCONSISTENZAALLEVAMENTIDETT.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT }
         *     
         */
        public ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT getDsCONSISTENZAALLEVAMENTIDETT() {
            return dsCONSISTENZAALLEVAMENTIDETT;
        }

        /**
         * Imposta il valore della proprietà dsCONSISTENZAALLEVAMENTIDETT.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT }
         *     
         */
        public void setDsCONSISTENZAALLEVAMENTIDETT(ArrayOfRootDatiCONSISTENZAALLEVAMENTODETT value) {
            this.dsCONSISTENZAALLEVAMENTIDETT = value;
        }

        /**
         * Recupera il valore della proprietà dsCONSISTENZASTATALLEVAMENTI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO }
         *     
         */
        public ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO getDsCONSISTENZASTATALLEVAMENTI() {
            return dsCONSISTENZASTATALLEVAMENTI;
        }

        /**
         * Imposta il valore della proprietà dsCONSISTENZASTATALLEVAMENTI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO }
         *     
         */
        public void setDsCONSISTENZASTATALLEVAMENTI(ArrayOfRootDatiCONSISTENZASTATALLEVAMENTO value) {
            this.dsCONSISTENZASTATALLEVAMENTI = value;
        }

        /**
         * Recupera il valore della proprietà dsUBACONSISTENZAALLEVAMENTI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO }
         *     
         */
        public ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO getDsUBACONSISTENZAALLEVAMENTI() {
            return dsUBACONSISTENZAALLEVAMENTI;
        }

        /**
         * Imposta il valore della proprietà dsUBACONSISTENZAALLEVAMENTI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO }
         *     
         */
        public void setDsUBACONSISTENZAALLEVAMENTI(ArrayOfRootDatiUBACONSISTENZAALLEVAMENTO value) {
            this.dsUBACONSISTENZAALLEVAMENTI = value;
        }

        /**
         * Recupera il valore della proprietà dsUBACONSISTENZAOVINI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiUBACONSISTENZAOVINI }
         *     
         */
        public ArrayOfRootDatiUBACONSISTENZAOVINI getDsUBACONSISTENZAOVINI() {
            return dsUBACONSISTENZAOVINI;
        }

        /**
         * Imposta il valore della proprietà dsUBACONSISTENZAOVINI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiUBACONSISTENZAOVINI }
         *     
         */
        public void setDsUBACONSISTENZAOVINI(ArrayOfRootDatiUBACONSISTENZAOVINI value) {
            this.dsUBACONSISTENZAOVINI = value;
        }

        /**
         * Recupera il valore della proprietà dsUBACENSIMENTIALLEVAMENTIOVINI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO }
         *     
         */
        public ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO getDsUBACENSIMENTIALLEVAMENTIOVINI() {
            return dsUBACENSIMENTIALLEVAMENTIOVINI;
        }

        /**
         * Imposta il valore della proprietà dsUBACENSIMENTIALLEVAMENTIOVINI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO }
         *     
         */
        public void setDsUBACENSIMENTIALLEVAMENTIOVINI(ArrayOfRootDatiUBACENSIMENTIALLEVAMENTOOVINO value) {
            this.dsUBACENSIMENTIALLEVAMENTIOVINI = value;
        }

        /**
         * Recupera il valore della proprietà dsUBACENSIMENTIOVINI2012.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiUBACENSIMENTOOVINO2012 }
         *     
         */
        public ArrayOfRootDatiUBACENSIMENTOOVINO2012 getDsUBACENSIMENTIOVINI2012() {
            return dsUBACENSIMENTIOVINI2012;
        }

        /**
         * Imposta il valore della proprietà dsUBACENSIMENTIOVINI2012.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiUBACENSIMENTOOVINO2012 }
         *     
         */
        public void setDsUBACENSIMENTIOVINI2012(ArrayOfRootDatiUBACENSIMENTOOVINO2012 value) {
            this.dsUBACENSIMENTIOVINI2012 = value;
        }

        /**
         * Recupera il valore della proprietà dsCONSISTENZAPASCOLI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONSISTENZAPASCOLO }
         *     
         */
        public ArrayOfRootDatiCONSISTENZAPASCOLO getDsCONSISTENZAPASCOLI() {
            return dsCONSISTENZAPASCOLI;
        }

        /**
         * Imposta il valore della proprietà dsCONSISTENZAPASCOLI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONSISTENZAPASCOLO }
         *     
         */
        public void setDsCONSISTENZAPASCOLI(ArrayOfRootDatiCONSISTENZAPASCOLO value) {
            this.dsCONSISTENZAPASCOLI = value;
        }

        /**
         * Recupera il valore della proprietà dsCONSISTENZAPASCOLO2015.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONSISTENZAPASCOLO2015 }
         *     
         */
        public ArrayOfRootDatiCONSISTENZAPASCOLO2015 getDsCONSISTENZAPASCOLO2015() {
            return dsCONSISTENZAPASCOLO2015;
        }

        /**
         * Imposta il valore della proprietà dsCONSISTENZAPASCOLO2015.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONSISTENZAPASCOLO2015 }
         *     
         */
        public void setDsCONSISTENZAPASCOLO2015(ArrayOfRootDatiCONSISTENZAPASCOLO2015 value) {
            this.dsCONSISTENZAPASCOLO2015 = value;
        }

        /**
         * Recupera il valore della proprietà dsPASCOLIVISITATI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiPASCOLOVISITATO }
         *     
         */
        public ArrayOfRootDatiPASCOLOVISITATO getDsPASCOLIVISITATI() {
            return dsPASCOLIVISITATI;
        }

        /**
         * Imposta il valore della proprietà dsPASCOLIVISITATI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiPASCOLOVISITATO }
         *     
         */
        public void setDsPASCOLIVISITATI(ArrayOfRootDatiPASCOLOVISITATO value) {
            this.dsPASCOLIVISITATI = value;
        }

        /**
         * Recupera il valore della proprietà dsOVIINGRESSIPASCOLO.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiOVIINGRESSIPASCOLO }
         *     
         */
        public ArrayOfRootDatiOVIINGRESSIPASCOLO getDsOVIINGRESSIPASCOLO() {
            return dsOVIINGRESSIPASCOLO;
        }

        /**
         * Imposta il valore della proprietà dsOVIINGRESSIPASCOLO.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiOVIINGRESSIPASCOLO }
         *     
         */
        public void setDsOVIINGRESSIPASCOLO(ArrayOfRootDatiOVIINGRESSIPASCOLO value) {
            this.dsOVIINGRESSIPASCOLO = value;
        }

        /**
         * Recupera il valore della proprietà dsOVIUSCITEPASCOLO.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiOVIUSCITEPASCOLO }
         *     
         */
        public ArrayOfRootDatiOVIUSCITEPASCOLO getDsOVIUSCITEPASCOLO() {
            return dsOVIUSCITEPASCOLO;
        }

        /**
         * Imposta il valore della proprietà dsOVIUSCITEPASCOLO.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiOVIUSCITEPASCOLO }
         *     
         */
        public void setDsOVIUSCITEPASCOLO(ArrayOfRootDatiOVIUSCITEPASCOLO value) {
            this.dsOVIUSCITEPASCOLO = value;
        }

        /**
         * Recupera il valore della proprietà dsOVIREGISTRIPASCOLO.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiOVIREGISTRIPASCOLO }
         *     
         */
        public ArrayOfRootDatiOVIREGISTRIPASCOLO getDsOVIREGISTRIPASCOLO() {
            return dsOVIREGISTRIPASCOLO;
        }

        /**
         * Imposta il valore della proprietà dsOVIREGISTRIPASCOLO.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiOVIREGISTRIPASCOLO }
         *     
         */
        public void setDsOVIREGISTRIPASCOLO(ArrayOfRootDatiOVIREGISTRIPASCOLO value) {
            this.dsOVIREGISTRIPASCOLO = value;
        }

        /**
         * Recupera il valore della proprietà dsCAMPIONEAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCAMPIONE }
         *     
         */
        public ArrayOfRootDatiCAMPIONE getDsCAMPIONEAGEA() {
            return dsCAMPIONEAGEA;
        }

        /**
         * Imposta il valore della proprietà dsCAMPIONEAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCAMPIONE }
         *     
         */
        public void setDsCAMPIONEAGEA(ArrayOfRootDatiCAMPIONE value) {
            this.dsCAMPIONEAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsIRREGOLARITAAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiIRREGOLARITA }
         *     
         */
        public ArrayOfRootDatiIRREGOLARITA getDsIRREGOLARITAAGEA() {
            return dsIRREGOLARITAAGEA;
        }

        /**
         * Imposta il valore della proprietà dsIRREGOLARITAAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiIRREGOLARITA }
         *     
         */
        public void setDsIRREGOLARITAAGEA(ArrayOfRootDatiIRREGOLARITA value) {
            this.dsIRREGOLARITAAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsMACELLAZIONEAGEA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiMACELLAZIONE }
         *     
         */
        public ArrayOfRootDatiMACELLAZIONE getDsMACELLAZIONEAGEA() {
            return dsMACELLAZIONEAGEA;
        }

        /**
         * Imposta il valore della proprietà dsMACELLAZIONEAGEA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiMACELLAZIONE }
         *     
         */
        public void setDsMACELLAZIONEAGEA(ArrayOfRootDatiMACELLAZIONE value) {
            this.dsMACELLAZIONEAGEA = value;
        }

        /**
         * Recupera il valore della proprietà dsNUMCAPIMAC.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCAPIMAC }
         *     
         */
        public ArrayOfRootDatiCAPIMAC getDsNUMCAPIMAC() {
            return dsNUMCAPIMAC;
        }

        /**
         * Imposta il valore della proprietà dsNUMCAPIMAC.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCAPIMAC }
         *     
         */
        public void setDsNUMCAPIMAC(ArrayOfRootDatiCAPIMAC value) {
            this.dsNUMCAPIMAC = value;
        }

        /**
         * Recupera il valore della proprietà dsALLEVAMENTILIBROIUS.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfALLEVAMENTOLIBRO }
         *     
         */
        public ArrayOfALLEVAMENTOLIBRO getDsALLEVAMENTILIBROIUS() {
            return dsALLEVAMENTILIBROIUS;
        }

        /**
         * Imposta il valore della proprietà dsALLEVAMENTILIBROIUS.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfALLEVAMENTOLIBRO }
         *     
         */
        public void setDsALLEVAMENTILIBROIUS(ArrayOfALLEVAMENTOLIBRO value) {
            this.dsALLEVAMENTILIBROIUS = value;
        }

        /**
         * Recupera il valore della proprietà dsCAPILIBROIUS.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfCAPOLIBRO }
         *     
         */
        public ArrayOfCAPOLIBRO getDsCAPILIBROIUS() {
            return dsCAPILIBROIUS;
        }

        /**
         * Imposta il valore della proprietà dsCAPILIBROIUS.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfCAPOLIBRO }
         *     
         */
        public void setDsCAPILIBROIUS(ArrayOfCAPOLIBRO value) {
            this.dsCAPILIBROIUS = value;
        }

        /**
         * Recupera il valore della proprietà dsCAPILIBROA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCAPO }
         *     
         */
        public ArrayOfRootDatiCAPO getDsCAPILIBROA() {
            return dsCAPILIBROA;
        }

        /**
         * Imposta il valore della proprietà dsCAPILIBROA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCAPO }
         *     
         */
        public void setDsCAPILIBROA(ArrayOfRootDatiCAPO value) {
            this.dsCAPILIBROA = value;
        }

        /**
         * Recupera il valore della proprietà dsALLEVAMENTILIBROA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiALLEVAMENTILIBRO }
         *     
         */
        public ArrayOfRootDatiALLEVAMENTILIBRO getDsALLEVAMENTILIBROA() {
            return dsALLEVAMENTILIBROA;
        }

        /**
         * Imposta il valore della proprietà dsALLEVAMENTILIBROA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiALLEVAMENTILIBRO }
         *     
         */
        public void setDsALLEVAMENTILIBROA(ArrayOfRootDatiALLEVAMENTILIBRO value) {
            this.dsALLEVAMENTILIBROA = value;
        }

        /**
         * Recupera il valore della proprietà dsCONSISTENZAMANDRIALATTIERA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONSISTENZAMANDRIALATTIERA }
         *     
         */
        public ArrayOfRootDatiCONSISTENZAMANDRIALATTIERA getDsCONSISTENZAMANDRIALATTIERA() {
            return dsCONSISTENZAMANDRIALATTIERA;
        }

        /**
         * Imposta il valore della proprietà dsCONSISTENZAMANDRIALATTIERA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONSISTENZAMANDRIALATTIERA }
         *     
         */
        public void setDsCONSISTENZAMANDRIALATTIERA(ArrayOfRootDatiCONSISTENZAMANDRIALATTIERA value) {
            this.dsCONSISTENZAMANDRIALATTIERA = value;
        }

        /**
         * Recupera il valore della proprietà dsCONTROLLIALLEVAMENTIIR.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONTROLLOALLEVAMENTOIR }
         *     
         */
        public ArrayOfRootDatiCONTROLLOALLEVAMENTOIR getDsCONTROLLIALLEVAMENTIIR() {
            return dsCONTROLLIALLEVAMENTIIR;
        }

        /**
         * Imposta il valore della proprietà dsCONTROLLIALLEVAMENTIIR.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONTROLLOALLEVAMENTOIR }
         *     
         */
        public void setDsCONTROLLIALLEVAMENTIIR(ArrayOfRootDatiCONTROLLOALLEVAMENTOIR value) {
            this.dsCONTROLLIALLEVAMENTIIR = value;
        }

        /**
         * Recupera il valore della proprietà dsCONTROLLIALLEVAMENTIBA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONTROLLOALLEVAMENTOBA }
         *     
         */
        public ArrayOfRootDatiCONTROLLOALLEVAMENTOBA getDsCONTROLLIALLEVAMENTIBA() {
            return dsCONTROLLIALLEVAMENTIBA;
        }

        /**
         * Imposta il valore della proprietà dsCONTROLLIALLEVAMENTIBA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONTROLLOALLEVAMENTOBA }
         *     
         */
        public void setDsCONTROLLIALLEVAMENTIBA(ArrayOfRootDatiCONTROLLOALLEVAMENTOBA value) {
            this.dsCONTROLLIALLEVAMENTIBA = value;
        }

        /**
         * Recupera il valore della proprietà dsCONTROLLIALLEVAMENTISA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONTROLLOALLEVAMENTOSA }
         *     
         */
        public ArrayOfRootDatiCONTROLLOALLEVAMENTOSA getDsCONTROLLIALLEVAMENTISA() {
            return dsCONTROLLIALLEVAMENTISA;
        }

        /**
         * Imposta il valore della proprietà dsCONTROLLIALLEVAMENTISA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONTROLLOALLEVAMENTOSA }
         *     
         */
        public void setDsCONTROLLIALLEVAMENTISA(ArrayOfRootDatiCONTROLLOALLEVAMENTOSA value) {
            this.dsCONTROLLIALLEVAMENTISA = value;
        }

        /**
         * Recupera il valore della proprietà dsCONTROLLIALLEVAMENTISV.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiCONTROLLOALLEVAMENTOSV }
         *     
         */
        public ArrayOfRootDatiCONTROLLOALLEVAMENTOSV getDsCONTROLLIALLEVAMENTISV() {
            return dsCONTROLLIALLEVAMENTISV;
        }

        /**
         * Imposta il valore della proprietà dsCONTROLLIALLEVAMENTISV.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiCONTROLLOALLEVAMENTOSV }
         *     
         */
        public void setDsCONTROLLIALLEVAMENTISV(ArrayOfRootDatiCONTROLLOALLEVAMENTOSV value) {
            this.dsCONTROLLIALLEVAMENTISV = value;
        }

        /**
         * Recupera il valore della proprietà dsIRREGOLARITAALLEVAMENTIIR.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR }
         *     
         */
        public ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR getDsIRREGOLARITAALLEVAMENTIIR() {
            return dsIRREGOLARITAALLEVAMENTIIR;
        }

        /**
         * Imposta il valore della proprietà dsIRREGOLARITAALLEVAMENTIIR.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR }
         *     
         */
        public void setDsIRREGOLARITAALLEVAMENTIIR(ArrayOfRootDatiIRREGOLARITAALLEVAMENTOIR value) {
            this.dsIRREGOLARITAALLEVAMENTIIR = value;
        }

        /**
         * Recupera il valore della proprietà dsIRREGOLARITAALLEVAMENTIBA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOBA }
         *     
         */
        public ArrayOfRootDatiIRREGOLARITAALLEVAMENTOBA getDsIRREGOLARITAALLEVAMENTIBA() {
            return dsIRREGOLARITAALLEVAMENTIBA;
        }

        /**
         * Imposta il valore della proprietà dsIRREGOLARITAALLEVAMENTIBA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOBA }
         *     
         */
        public void setDsIRREGOLARITAALLEVAMENTIBA(ArrayOfRootDatiIRREGOLARITAALLEVAMENTOBA value) {
            this.dsIRREGOLARITAALLEVAMENTIBA = value;
        }

        /**
         * Recupera il valore della proprietà dsIRREGOLARITAALLEVAMENTISV.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSV }
         *     
         */
        public ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSV getDsIRREGOLARITAALLEVAMENTISV() {
            return dsIRREGOLARITAALLEVAMENTISV;
        }

        /**
         * Imposta il valore della proprietà dsIRREGOLARITAALLEVAMENTISV.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSV }
         *     
         */
        public void setDsIRREGOLARITAALLEVAMENTISV(ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSV value) {
            this.dsIRREGOLARITAALLEVAMENTISV = value;
        }

        /**
         * Recupera il valore della proprietà dsIRREGOLARITAALLEVAMENTISA.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA }
         *     
         */
        public ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA getDsIRREGOLARITAALLEVAMENTISA() {
            return dsIRREGOLARITAALLEVAMENTISA;
        }

        /**
         * Imposta il valore della proprietà dsIRREGOLARITAALLEVAMENTISA.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA }
         *     
         */
        public void setDsIRREGOLARITAALLEVAMENTISA(ArrayOfRootDatiIRREGOLARITAALLEVAMENTOSA value) {
            this.dsIRREGOLARITAALLEVAMENTISA = value;
        }

        /**
         * Recupera il valore della proprietà dsOVITEMPINOTIFICANASCITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICANASCITE }
         *     
         */
        public ArrayOfRootDatiOVITEMPINOTIFICANASCITE getDsOVITEMPINOTIFICANASCITE() {
            return dsOVITEMPINOTIFICANASCITE;
        }

        /**
         * Imposta il valore della proprietà dsOVITEMPINOTIFICANASCITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICANASCITE }
         *     
         */
        public void setDsOVITEMPINOTIFICANASCITE(ArrayOfRootDatiOVITEMPINOTIFICANASCITE value) {
            this.dsOVITEMPINOTIFICANASCITE = value;
        }

        /**
         * Recupera il valore della proprietà dsOVITEMPINOTIFICAINGRESSI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICAINGRESSI }
         *     
         */
        public ArrayOfRootDatiOVITEMPINOTIFICAINGRESSI getDsOVITEMPINOTIFICAINGRESSI() {
            return dsOVITEMPINOTIFICAINGRESSI;
        }

        /**
         * Imposta il valore della proprietà dsOVITEMPINOTIFICAINGRESSI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICAINGRESSI }
         *     
         */
        public void setDsOVITEMPINOTIFICAINGRESSI(ArrayOfRootDatiOVITEMPINOTIFICAINGRESSI value) {
            this.dsOVITEMPINOTIFICAINGRESSI = value;
        }

        /**
         * Recupera il valore della proprietà dsOVITEMPINOTIFICAINGRESSIPARTITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICAINGRESSIPARTITE }
         *     
         */
        public ArrayOfRootDatiOVITEMPINOTIFICAINGRESSIPARTITE getDsOVITEMPINOTIFICAINGRESSIPARTITE() {
            return dsOVITEMPINOTIFICAINGRESSIPARTITE;
        }

        /**
         * Imposta il valore della proprietà dsOVITEMPINOTIFICAINGRESSIPARTITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICAINGRESSIPARTITE }
         *     
         */
        public void setDsOVITEMPINOTIFICAINGRESSIPARTITE(ArrayOfRootDatiOVITEMPINOTIFICAINGRESSIPARTITE value) {
            this.dsOVITEMPINOTIFICAINGRESSIPARTITE = value;
        }

        /**
         * Recupera il valore della proprietà dsOVITEMPINOTIFICAUSCITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICAUSCITE }
         *     
         */
        public ArrayOfRootDatiOVITEMPINOTIFICAUSCITE getDsOVITEMPINOTIFICAUSCITE() {
            return dsOVITEMPINOTIFICAUSCITE;
        }

        /**
         * Imposta il valore della proprietà dsOVITEMPINOTIFICAUSCITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICAUSCITE }
         *     
         */
        public void setDsOVITEMPINOTIFICAUSCITE(ArrayOfRootDatiOVITEMPINOTIFICAUSCITE value) {
            this.dsOVITEMPINOTIFICAUSCITE = value;
        }

        /**
         * Recupera il valore della proprietà dsOVITEMPINOTIFICAUSCITEPARTITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE }
         *     
         */
        public ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE getDsOVITEMPINOTIFICAUSCITEPARTITE() {
            return dsOVITEMPINOTIFICAUSCITEPARTITE;
        }

        /**
         * Imposta il valore della proprietà dsOVITEMPINOTIFICAUSCITEPARTITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE }
         *     
         */
        public void setDsOVITEMPINOTIFICAUSCITEPARTITE(ArrayOfRootDatiOVITEMPINOTIFICAUSCITEPARTITE value) {
            this.dsOVITEMPINOTIFICAUSCITEPARTITE = value;
        }

        /**
         * Recupera il valore della proprietà dsSUITEMPINOTIFICANASCITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICANASCITE }
         *     
         */
        public ArrayOfRootDatiSUITEMPINOTIFICANASCITE getDsSUITEMPINOTIFICANASCITE() {
            return dsSUITEMPINOTIFICANASCITE;
        }

        /**
         * Imposta il valore della proprietà dsSUITEMPINOTIFICANASCITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICANASCITE }
         *     
         */
        public void setDsSUITEMPINOTIFICANASCITE(ArrayOfRootDatiSUITEMPINOTIFICANASCITE value) {
            this.dsSUITEMPINOTIFICANASCITE = value;
        }

        /**
         * Recupera il valore della proprietà dsSUITEMPINOTIFICAINGRESSI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICAINGRESSI }
         *     
         */
        public ArrayOfRootDatiSUITEMPINOTIFICAINGRESSI getDsSUITEMPINOTIFICAINGRESSI() {
            return dsSUITEMPINOTIFICAINGRESSI;
        }

        /**
         * Imposta il valore della proprietà dsSUITEMPINOTIFICAINGRESSI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICAINGRESSI }
         *     
         */
        public void setDsSUITEMPINOTIFICAINGRESSI(ArrayOfRootDatiSUITEMPINOTIFICAINGRESSI value) {
            this.dsSUITEMPINOTIFICAINGRESSI = value;
        }

        /**
         * Recupera il valore della proprietà dsSUITEMPINOTIFICAINGRESSIPARTITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICAINGRESSIPARTITE }
         *     
         */
        public ArrayOfRootDatiSUITEMPINOTIFICAINGRESSIPARTITE getDsSUITEMPINOTIFICAINGRESSIPARTITE() {
            return dsSUITEMPINOTIFICAINGRESSIPARTITE;
        }

        /**
         * Imposta il valore della proprietà dsSUITEMPINOTIFICAINGRESSIPARTITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICAINGRESSIPARTITE }
         *     
         */
        public void setDsSUITEMPINOTIFICAINGRESSIPARTITE(ArrayOfRootDatiSUITEMPINOTIFICAINGRESSIPARTITE value) {
            this.dsSUITEMPINOTIFICAINGRESSIPARTITE = value;
        }

        /**
         * Recupera il valore della proprietà dsSUITEMPINOTIFICAUSCITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICAUSCITE }
         *     
         */
        public ArrayOfRootDatiSUITEMPINOTIFICAUSCITE getDsSUITEMPINOTIFICAUSCITE() {
            return dsSUITEMPINOTIFICAUSCITE;
        }

        /**
         * Imposta il valore della proprietà dsSUITEMPINOTIFICAUSCITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICAUSCITE }
         *     
         */
        public void setDsSUITEMPINOTIFICAUSCITE(ArrayOfRootDatiSUITEMPINOTIFICAUSCITE value) {
            this.dsSUITEMPINOTIFICAUSCITE = value;
        }

        /**
         * Recupera il valore della proprietà dsSUITEMPINOTIFICAUSCITEPARTITE.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICAUSCITEPARTITE }
         *     
         */
        public ArrayOfRootDatiSUITEMPINOTIFICAUSCITEPARTITE getDsSUITEMPINOTIFICAUSCITEPARTITE() {
            return dsSUITEMPINOTIFICAUSCITEPARTITE;
        }

        /**
         * Imposta il valore della proprietà dsSUITEMPINOTIFICAUSCITEPARTITE.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiSUITEMPINOTIFICAUSCITEPARTITE }
         *     
         */
        public void setDsSUITEMPINOTIFICAUSCITEPARTITE(ArrayOfRootDatiSUITEMPINOTIFICAUSCITEPARTITE value) {
            this.dsSUITEMPINOTIFICAUSCITEPARTITE = value;
        }

        /**
         * Recupera il valore della proprietà dsVARIAZIONICONTROLLI.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiVARIAZIONECONTROLLO }
         *     
         */
        public ArrayOfRootDatiVARIAZIONECONTROLLO getDsVARIAZIONICONTROLLI() {
            return dsVARIAZIONICONTROLLI;
        }

        /**
         * Imposta il valore della proprietà dsVARIAZIONICONTROLLI.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiVARIAZIONECONTROLLO }
         *     
         */
        public void setDsVARIAZIONICONTROLLI(ArrayOfRootDatiVARIAZIONECONTROLLO value) {
            this.dsVARIAZIONICONTROLLI = value;
        }

        /**
         * Recupera il valore della proprietà dsVERBALECHECKLIST.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiVERBALECHECKLIST }
         *     
         */
        public ArrayOfRootDatiVERBALECHECKLIST getDsVERBALECHECKLIST() {
            return dsVERBALECHECKLIST;
        }

        /**
         * Imposta il valore della proprietà dsVERBALECHECKLIST.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiVERBALECHECKLIST }
         *     
         */
        public void setDsVERBALECHECKLIST(ArrayOfRootDatiVERBALECHECKLIST value) {
            this.dsVERBALECHECKLIST = value;
        }

        /**
         * Recupera il valore della proprietà dsUNITATECNICHEAGEAMUL.
         * 
         * @return
         *     possible object is
         *     {@link ArrayOfRootDatiUNITATECNICHEMUL }
         *     
         */
        public ArrayOfRootDatiUNITATECNICHEMUL getDsUNITATECNICHEAGEAMUL() {
            return dsUNITATECNICHEAGEAMUL;
        }

        /**
         * Imposta il valore della proprietà dsUNITATECNICHEAGEAMUL.
         * 
         * @param value
         *     allowed object is
         *     {@link ArrayOfRootDatiUNITATECNICHEMUL }
         *     
         */
        public void setDsUNITATECNICHEAGEAMUL(ArrayOfRootDatiUNITATECNICHEMUL value) {
            this.dsUNITATECNICHEAGEAMUL = value;
        }

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
     *         &lt;element name="info" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="warning" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="error" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *                   &lt;element name="des" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    @XmlType(name = "", propOrder = {
        "info",
        "warning",
        "error"
    })
    public static class ErrorInfo {

        protected String info;
        protected String warning;
        protected ConsistenzaUBAOviniResult.ErrorInfo.Error error;

        /**
         * Recupera il valore della proprietà info.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInfo() {
            return info;
        }

        /**
         * Imposta il valore della proprietà info.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInfo(String value) {
            this.info = value;
        }

        /**
         * Recupera il valore della proprietà warning.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWarning() {
            return warning;
        }

        /**
         * Imposta il valore della proprietà warning.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWarning(String value) {
            this.warning = value;
        }

        /**
         * Recupera il valore della proprietà error.
         * 
         * @return
         *     possible object is
         *     {@link ConsistenzaUBAOviniResult.ErrorInfo.Error }
         *     
         */
        public ConsistenzaUBAOviniResult.ErrorInfo.Error getError() {
            return error;
        }

        /**
         * Imposta il valore della proprietà error.
         * 
         * @param value
         *     allowed object is
         *     {@link ConsistenzaUBAOviniResult.ErrorInfo.Error }
         *     
         */
        public void setError(ConsistenzaUBAOviniResult.ErrorInfo.Error value) {
            this.error = value;
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
         *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
         *         &lt;element name="des" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
            "id",
            "des"
        })
        public static class Error {

            protected String id;
            protected String des;

            /**
             * Recupera il valore della proprietà id.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getId() {
                return id;
            }

            /**
             * Imposta il valore della proprietà id.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setId(String value) {
                this.id = value;
            }

            /**
             * Recupera il valore della proprietà des.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDes() {
                return des;
            }

            /**
             * Imposta il valore della proprietà des.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDes(String value) {
                this.des = value;
            }

        }

    }

}

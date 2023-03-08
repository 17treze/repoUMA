//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:57 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.wsStrutture;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMixed;
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
 *         &lt;element name="Elenco_OVI_RegistriPascolo_Cod_Periodo_DetenResult" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;any/&gt;
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
    "elencoOVIRegistriPascoloCodPeriodoDetenResult"
})
@XmlRootElement(name = "Elenco_OVI_RegistriPascolo_Cod_Periodo_DetenResponse")
public class ElencoOVIRegistriPascoloCodPeriodoDetenResponse {

    @XmlElement(name = "Elenco_OVI_RegistriPascolo_Cod_Periodo_DetenResult")
    protected ElencoOVIRegistriPascoloCodPeriodoDetenResponse.ElencoOVIRegistriPascoloCodPeriodoDetenResult elencoOVIRegistriPascoloCodPeriodoDetenResult;

    /**
     * Recupera il valore della proprietà elencoOVIRegistriPascoloCodPeriodoDetenResult.
     * 
     * @return
     *     possible object is
     *     {@link ElencoOVIRegistriPascoloCodPeriodoDetenResponse.ElencoOVIRegistriPascoloCodPeriodoDetenResult }
     *     
     */
    public ElencoOVIRegistriPascoloCodPeriodoDetenResponse.ElencoOVIRegistriPascoloCodPeriodoDetenResult getElencoOVIRegistriPascoloCodPeriodoDetenResult() {
        return elencoOVIRegistriPascoloCodPeriodoDetenResult;
    }

    /**
     * Imposta il valore della proprietà elencoOVIRegistriPascoloCodPeriodoDetenResult.
     * 
     * @param value
     *     allowed object is
     *     {@link ElencoOVIRegistriPascoloCodPeriodoDetenResponse.ElencoOVIRegistriPascoloCodPeriodoDetenResult }
     *     
     */
    public void setElencoOVIRegistriPascoloCodPeriodoDetenResult(ElencoOVIRegistriPascoloCodPeriodoDetenResponse.ElencoOVIRegistriPascoloCodPeriodoDetenResult value) {
        this.elencoOVIRegistriPascoloCodPeriodoDetenResult = value;
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
     *         &lt;any/&gt;
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
        "content"
    })
    public static class ElencoOVIRegistriPascoloCodPeriodoDetenResult {

        @XmlMixed
        @XmlAnyElement(lax = true)
        protected List<Object> content;

        /**
         * Gets the value of the content property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the content property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContent().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Object }
         * {@link String }
         * 
         * 
         */
        public List<Object> getContent() {
            if (content == null) {
                content = new ArrayList<Object>();
            }
            return this.content;
        }

    }

}

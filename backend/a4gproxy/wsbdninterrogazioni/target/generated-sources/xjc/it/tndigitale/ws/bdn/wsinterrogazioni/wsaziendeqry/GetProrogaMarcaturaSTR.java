//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.03.13 at 04:32:47 PM CET 
//


package it.tndigitale.ws.bdn.wsinterrogazioni.wsaziendeqry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="p_all_msm_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "pAllMsmId"
})
@XmlRootElement(name = "getProrogaMarcatura_STR")
public class GetProrogaMarcaturaSTR {

    @XmlElement(name = "p_all_msm_id")
    protected String pAllMsmId;

    /**
     * Gets the value of the pAllMsmId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAllMsmId() {
        return pAllMsmId;
    }

    /**
     * Sets the value of the pAllMsmId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAllMsmId(String value) {
        this.pAllMsmId = value;
    }

}

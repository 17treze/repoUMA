//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:19 PM CEST 
//


package it.izs.wsdl.wsBDNDomandaUnica;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ArrayOfClsCapoVacca complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfClsCapoVacca"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="clsCapoVacca" type="{http://bdr.izs.it/webservices}clsCapoVacca" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfClsCapoVacca", propOrder = {
    "clsCapoVacca"
})
public class ArrayOfClsCapoVacca {

    @XmlElement(nillable = true)
    protected List<ClsCapoVacca> clsCapoVacca;

    /**
     * Gets the value of the clsCapoVacca property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clsCapoVacca property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClsCapoVacca().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClsCapoVacca }
     * 
     * 
     */
    public List<ClsCapoVacca> getClsCapoVacca() {
        if (clsCapoVacca == null) {
            clsCapoVacca = new ArrayList<ClsCapoVacca>();
        }
        return this.clsCapoVacca;
    }

}

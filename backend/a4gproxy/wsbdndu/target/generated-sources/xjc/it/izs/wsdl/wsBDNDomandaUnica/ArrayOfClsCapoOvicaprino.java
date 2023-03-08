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
 * <p>Classe Java per ArrayOfClsCapoOvicaprino complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfClsCapoOvicaprino"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="clsCapoOvicaprino" type="{http://bdr.izs.it/webservices}clsCapoOvicaprino" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfClsCapoOvicaprino", propOrder = {
    "clsCapoOvicaprino"
})
public class ArrayOfClsCapoOvicaprino {

    @XmlElement(nillable = true)
    protected List<ClsCapoOvicaprino> clsCapoOvicaprino;

    /**
     * Gets the value of the clsCapoOvicaprino property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clsCapoOvicaprino property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClsCapoOvicaprino().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClsCapoOvicaprino }
     * 
     * 
     */
    public List<ClsCapoOvicaprino> getClsCapoOvicaprino() {
        if (clsCapoOvicaprino == null) {
            clsCapoOvicaprino = new ArrayList<ClsCapoOvicaprino>();
        }
        return this.clsCapoOvicaprino;
    }

}

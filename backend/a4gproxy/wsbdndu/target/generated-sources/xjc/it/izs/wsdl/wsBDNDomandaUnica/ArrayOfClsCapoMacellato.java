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
 * <p>Classe Java per ArrayOfClsCapoMacellato complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfClsCapoMacellato"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="clsCapoMacellato" type="{http://bdr.izs.it/webservices}clsCapoMacellato" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfClsCapoMacellato", propOrder = {
    "clsCapoMacellato"
})
public class ArrayOfClsCapoMacellato {

    @XmlElement(nillable = true)
    protected List<ClsCapoMacellato> clsCapoMacellato;

    /**
     * Gets the value of the clsCapoMacellato property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clsCapoMacellato property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClsCapoMacellato().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClsCapoMacellato }
     * 
     * 
     */
    public List<ClsCapoMacellato> getClsCapoMacellato() {
        if (clsCapoMacellato == null) {
            clsCapoMacellato = new ArrayList<ClsCapoMacellato>();
        }
        return this.clsCapoMacellato;
    }

}

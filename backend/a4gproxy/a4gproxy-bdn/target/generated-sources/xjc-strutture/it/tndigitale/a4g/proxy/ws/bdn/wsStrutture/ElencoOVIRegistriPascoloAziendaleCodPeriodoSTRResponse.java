//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2022.08.29 alle 04:34:57 PM CEST 
//


package it.tndigitale.a4g.proxy.ws.bdn.wsStrutture;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="Elenco_OVI_RegistriPascolo_Aziendale_Cod_Periodo_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "elencoOVIRegistriPascoloAziendaleCodPeriodoSTRResult"
})
@XmlRootElement(name = "Elenco_OVI_RegistriPascolo_Aziendale_Cod_Periodo_STRResponse")
public class ElencoOVIRegistriPascoloAziendaleCodPeriodoSTRResponse {

    @XmlElement(name = "Elenco_OVI_RegistriPascolo_Aziendale_Cod_Periodo_STRResult")
    protected String elencoOVIRegistriPascoloAziendaleCodPeriodoSTRResult;

    /**
     * Recupera il valore della proprietà elencoOVIRegistriPascoloAziendaleCodPeriodoSTRResult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElencoOVIRegistriPascoloAziendaleCodPeriodoSTRResult() {
        return elencoOVIRegistriPascoloAziendaleCodPeriodoSTRResult;
    }

    /**
     * Imposta il valore della proprietà elencoOVIRegistriPascoloAziendaleCodPeriodoSTRResult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElencoOVIRegistriPascoloAziendaleCodPeriodoSTRResult(String value) {
        this.elencoOVIRegistriPascoloAziendaleCodPeriodoSTRResult = value;
    }

}

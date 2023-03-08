package it.tndigitale.a4gutente.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import lombok.Data;

@Data
public class DatiDomandaRegistrazioneUtente implements Serializable {

    private static final long serialVersionUID = -4022824637548247017L;

    private Long id;
    private String idProtocollo;
    private StatoDomandaRegistrazioneUtente stato;
    private DatiAnagrafici datiAnagrafici = new DatiAnagrafici();
    private ResponsabilitaRichieste responsabilitaRichieste;
    private Set<ServiziType> servizi;
    private String luogo;
    private String data;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime dataProtocollazione;
    private TipoDomandaRegistrazione tipoDomandaRegistrazione;

    public String getCodiceFiscale() {
        return datiAnagrafici.getCodiceFiscale();
    }
    public String getNome() {
        return datiAnagrafici.getNome();
    }
    public String getCognome() {
        return datiAnagrafici.getCognome();
    }
    public String getEmail() {
        return datiAnagrafici.getEmail();
    }
    public String getTelefono() {
        return datiAnagrafici.getTelefono();
    }
    
    public void setCodiceFiscale(String codiceFiscale) {
        datiAnagrafici.setCodiceFiscale(codiceFiscale);
    }
    public void setNome(String nome) {
        datiAnagrafici.setNome(nome);
    }
    public void setCognome(String cognome) {
        datiAnagrafici.setCognome(cognome);
    }
    public void setEmail(String email) {
        datiAnagrafici.setEmail(email);
    }
    public void setTelefono(String telefono) {
        datiAnagrafici.setTelefono(telefono);
    }
    
    public static enum ServiziType {
        AGS, A4G, SRT;
    }
}

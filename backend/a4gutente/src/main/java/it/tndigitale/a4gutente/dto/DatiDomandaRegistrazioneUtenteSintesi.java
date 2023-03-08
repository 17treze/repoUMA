package it.tndigitale.a4gutente.dto;

import org.springframework.format.annotation.DateTimeFormat;

import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class DatiDomandaRegistrazioneUtenteSintesi implements Serializable {

	private static final long serialVersionUID = -619671633233494108L;

	private Long id;
    private String idProtocollo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataProtocollazione;
    private DatiAnagrafici datiAnagrafici = new DatiAnagrafici();
    private Boolean configurato;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataApprovazione;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataRifiuto;
	private StatoDomandaRegistrazioneUtente stato;


    public Long getId() {
        return id;
    }

    public DatiDomandaRegistrazioneUtenteSintesi setId(Long id) {
        this.id = id;
        return this;
    }

    public String getIdProtocollo() {
        return idProtocollo;
    }

    public DatiDomandaRegistrazioneUtenteSintesi setIdProtocollo(String idProtocollo) {
        this.idProtocollo = idProtocollo;
        return this;
    }

    public LocalDateTime getDataProtocollazione() {
        return dataProtocollazione;
    }

    public DatiDomandaRegistrazioneUtenteSintesi setDataProtocollazione(LocalDateTime dataProtocollazione) {
        this.dataProtocollazione = dataProtocollazione;
        return this;
    }

    public DatiAnagrafici getDatiAnagrafici() {
        return datiAnagrafici;
    }

    public DatiDomandaRegistrazioneUtenteSintesi setDatiAnagrafici(DatiAnagrafici datiAnagrafici) {
        this.datiAnagrafici = datiAnagrafici;
        return this;
    }

    public Boolean getConfigurato() {
        return configurato;
    }

    public DatiDomandaRegistrazioneUtenteSintesi setConfigurato(Boolean configurato) {
        this.configurato = configurato;
        return this;
    }

    public LocalDateTime getDataApprovazione() {
        return dataApprovazione;
    }

    public DatiDomandaRegistrazioneUtenteSintesi setDataApprovazione(LocalDateTime dataApprovazione) {
        this.dataApprovazione = dataApprovazione;
        return this;
    }

    public LocalDateTime getDataRifiuto() {
        return dataRifiuto;
    }

    public DatiDomandaRegistrazioneUtenteSintesi setDataRifiuto(LocalDateTime dataRifiuto) {
        this.dataRifiuto = dataRifiuto;
        return this;
    }

    public StatoDomandaRegistrazioneUtente getStato() {
		return stato;
	}

	public DatiDomandaRegistrazioneUtenteSintesi setStato(StatoDomandaRegistrazioneUtente stato) {
		this.stato = stato;
        return this;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatiDomandaRegistrazioneUtenteSintesi that = (DatiDomandaRegistrazioneUtenteSintesi) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(idProtocollo, that.idProtocollo) &&
                Objects.equals(dataProtocollazione, that.dataProtocollazione) &&
                Objects.equals(datiAnagrafici, that.datiAnagrafici) &&
                Objects.equals(configurato, that.configurato) &&
                Objects.equals(dataApprovazione, that.dataApprovazione) &&
                Objects.equals(dataRifiuto, that.dataRifiuto) &&
                Objects.equals(stato, that.stato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idProtocollo, dataProtocollazione, datiAnagrafici, configurato, dataApprovazione, dataRifiuto, stato);
    }
}

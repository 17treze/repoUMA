package it.tndigitale.a4g.proxy.dto.ioitalia;

import java.time.LocalDateTime;
import java.util.Objects;

public class ComunicationDto {
    public static final int DEFAULT_TIME_TO_LIVE = 86400;

	private String codiceFiscale;

    private String oggetto;

    private String messaggio;
    
    private LocalDateTime scadenza;

    // di default 1 giorno
    private Integer timeToLive = DEFAULT_TIME_TO_LIVE;

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public ComunicationDto setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
        return this;
    }

    public String getOggetto() {
        return oggetto;
    }

    public ComunicationDto setOggetto(String oggetto) {
        this.oggetto = oggetto;
        return this;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public ComunicationDto setMessaggio(String messaggio) {
        this.messaggio = messaggio;
        return this;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public ComunicationDto setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }
    public LocalDateTime getScadenza() {
		return scadenza;
	}

	public ComunicationDto setScadenza(LocalDateTime scadenza) {
		this.scadenza = scadenza;
		return this;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComunicationDto that = (ComunicationDto) o;
        return Objects.equals(codiceFiscale, that.codiceFiscale) &&
                Objects.equals(oggetto, that.oggetto) &&
                Objects.equals(messaggio, that.messaggio) &&
                Objects.equals(timeToLive, that.timeToLive) &&
                Objects.equals(scadenza, that.scadenza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceFiscale, oggetto, messaggio, timeToLive, scadenza);
    }
}

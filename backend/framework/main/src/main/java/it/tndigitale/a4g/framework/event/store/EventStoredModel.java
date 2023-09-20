package it.tndigitale.a4g.framework.event.store;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "TAB_AGRI_UMAL_EVENTSTORED")
public class EventStoredModel extends EntitaDominio {
    @Basic
    @NotNull
    @Column(name = "EVENTO")
    private String event;

    @Basic
    @NotNull
    @Column(name = "JSON_EVENT")
    private String jsonEvent;

    @Basic
    @Column(name = "USER_NAME")
    private String userName;

    @Basic
    @NotNull
    @Column(name = "DATA_INSERIMENTO")
    private LocalDateTime date;

    @Basic
    @NotNull
    @Column(name = "NUMERO_RETRY")
    private Integer numberOfRetry;

    @Basic
    @Column(name = "ERRORE")
    private String error;

    public String getError() {
        return error;
    }

    public EventStoredModel setError(String error) {
        this.error = error;
        return this;
    }

    public String getEvent() {
        return event;
    }

    public EventStoredModel setEvent(String event) {
        this.event = event;
        return this;
    }

    public String getJsonEvent() {
        return jsonEvent;
    }

    public EventStoredModel setJsonEvent(String jsonEvent) {
        this.jsonEvent = jsonEvent;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public EventStoredModel setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Integer getNumberOfRetry() {
        return numberOfRetry;
    }

    public EventStoredModel setNumberOfRetry(Integer numberOfRetry) {
        this.numberOfRetry = numberOfRetry;
        return this;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public EventStoredModel setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventStoredModel that = (EventStoredModel) o;
        return Objects.equals(event, that.event) &&
                Objects.equals(jsonEvent, that.jsonEvent) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(date, that.date) &&
                Objects.equals(numberOfRetry, that.numberOfRetry) &&
                Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, jsonEvent, userName, date, numberOfRetry, error);
    }
}

package it.tndigitale.a4g.framework.event;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;

public abstract class AbstractWrapperEvent<T> implements Event {
    private static final Logger logger = LoggerFactory.getLogger(AbstractWrapperEvent.class);

    private T data;

    private Integer numberOfRetry = 0;

    private String username = null;
    
    @Autowired
    UtenteComponent utenteComponent;
    
    public AbstractWrapperEvent() {
    	try {
    		// username = SecurityContextHolder.getContext().getAuthentication().getName();
    		this.username = utenteComponent.username();
    	} catch(Exception e) {
    		logger.debug("(SICURO da ignorare) Non riesco a creare il contesto di sessione per {}.", username);
    	}
	}
    
    public String getUsername() {
    	return this.username;
    }

    @Override
    public Integer getNumberOfRetry() {
        return numberOfRetry;
    }

    @Override
    public AbstractWrapperEvent<T> setNumberOfRetry(Integer numberOfRetry) {
        this.numberOfRetry = numberOfRetry;
        return this;
    }

    public T getData() {
        return data;
    }

    public AbstractWrapperEvent<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractWrapperEvent<?> that = (AbstractWrapperEvent<?>) o;
        return Objects.equals(data, that.data) &&
                Objects.equals(numberOfRetry, that.numberOfRetry) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, numberOfRetry, username);
    }
}

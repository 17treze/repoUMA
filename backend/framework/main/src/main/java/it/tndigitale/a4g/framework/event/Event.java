package it.tndigitale.a4g.framework.event;

import java.time.LocalDateTime;

public interface Event {
    default LocalDateTime timeStamp(){
        return null;
    }

    Integer getNumberOfRetry();
    
    Event setNumberOfRetry(Integer numberOfRetry);
    
    public String getUsername();
}

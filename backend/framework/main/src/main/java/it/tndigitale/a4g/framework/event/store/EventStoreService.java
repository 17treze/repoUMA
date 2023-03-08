package it.tndigitale.a4g.framework.event.store;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.event.Event;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.event.store.repository.EventStoreJdbcRepository;
import it.tndigitale.a4g.framework.jackson.ByteArrayResourceMixin;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class EventStoreService {
    private static final Logger logger = LoggerFactory.getLogger(EventStoreService.class);

    private EventStoreJdbcRepository reprocessEventRepository;

    private EventBus eventBus;

    private ObjectMapper objectMapper;

    @Autowired
    public EventStoreService setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

    @Autowired
    public EventStoreService setReprocessEventRepository(EventStoreJdbcRepository reprocessEventRepository) {
        this.reprocessEventRepository = reprocessEventRepository;
        return this;
    }

    @Autowired
    public EventStoreService setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public Long add(EventStoredModel eventStore) {
        reprocessEventRepository.save(eventStore);
        return eventStore.getId();
    }

    public List<EventStoredModel> findAll() {
        return reprocessEventRepository.findAll();
    }

    public void reprocessEvent(Long idEvent){
        EventStoredModel eventStored = reprocessEventRepository.findById(idEvent);

        publishEvent(eventStored);
    }

    private void publishEvent(EventStoredModel eventStored) {
        if(eventStored == null){
            return;
        }

        Event event = null;
        try {
//          mix-in per ByteArrayResource
            objectMapper.addMixIn(ByteArrayResource.class, ByteArrayResourceMixin.class);
            
            event = (Event) objectMapper.readValue(eventStored.getJsonEvent(), toClassEvent(eventStored));
        } catch (IOException e) {
            logger.error("Non riesco a leggere l'evento {}", eventStored.getId(), e);
            throw new RuntimeException(e);
        }
        reprocessEventRepository.delete(eventStored.getId());

        eventBus.publishEvent(event);
    }
    
    
    public String detailError(Throwable ex) throws IOException {
        StringWriter detailError = new StringWriter();
        PrintWriter printWriter = new PrintWriter(detailError);
        ex.printStackTrace(printWriter);
        detailError.flush();
        detailError.close();

        return detailError.toString();
    }
    
    public void toRepreocessEvent(final Event event, final String error) {
        try {
//          mix-in per ByteArrayResource
            objectMapper.addMixIn(ByteArrayResource.class, ByteArrayResourceMixin.class);
            
            EventStoredModel eventStore = new EventStoredModel()
                    .setJsonEvent(objectMapper.writeValueAsString(event.setNumberOfRetry(event.getNumberOfRetry() +1)))
                    .setDate(LocalDateTime.now())
                    .setEvent(event.getClass().getName())
                    .setNumberOfRetry(event.getNumberOfRetry())
                    .setError(error)
                    .setUserName(event.getUsername());
            add(eventStore);
        } catch (JsonProcessingException e) {
            logger.error("Non riesco a persistere l'evento {} generato a causa dell'errore {}", event.getClass().getName(), error);
            logger.error("Evento : ", event);
            logger.error("Eccezione: ", e);
            throw new RuntimeException(e);
        }
    }

    public void triggerRetry(final Throwable throwable, final Event event) {
        try {
            String detailError = detailError(throwable);
            toRepreocessEvent(event, detailError);
            
            logger.warn("Lanciato 'triggerRetry' per l'evento {}", event, throwable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private Class<?> toClassEvent(EventStoredModel event) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(event.getEvent());
        } catch (ClassNotFoundException e) {
            logger.error("Non ho trovato la classe {} nel class loading {}", event.getEvent(), event.getId());
            throw new RuntimeException(e);
        }
    }
}

package it.tndigitale.a4g.framework.event.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.event.store.repository.EventStoreJdbcRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EventStoreServiceTest {
    private static final long ID_EVENT = 56L;

    private EventStoreService eventStoreService;

    private EventBus eventBus;

    private EventStoreJdbcRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        eventBus = mock(EventBus.class);
        eventRepository = mock(EventStoreJdbcRepository.class);
        // objectMapper = mock(ObjectMapper.class);

        eventStoreService = new EventStoreService();
        eventStoreService.setEventBus(eventBus);
        eventStoreService.setReprocessEventRepository(eventRepository);
        eventStoreService.setObjectMapper(objectMapper);
    }

    @Test
    public void GIVEN_EVENT_SAVE_INTO_REPOSITORY() {
        EventStoredModel eventStore = new EventStoredModel().setEvent(TestEvent.class.getName()).setJsonEvent("{\"id\" : 12}");

        eventStoreService.add(eventStore);

        verify(eventRepository).save(eventStore);
    }

    @Test
    public void GIVEN_ID_EVENT_PUBLISH_EVENT_INTO_EVENT_BUS() {
    	objectMapper.configure(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL, true);
        when(eventRepository.findById(ID_EVENT)).thenReturn(new EventStoredModel().setEvent(TestEvent.class.getName()).setJsonEvent("{\"id\" : 12}"));
        /* try {
            when(objectMapper.readValue("{\"id\" : 12}", TestEvent.class)).thenReturn(new TestEvent().setId(12L));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } */

        eventStoreService.reprocessEvent(ID_EVENT);

        verify(eventBus).publishEvent(new TestEvent().setId(12L));
    }

}

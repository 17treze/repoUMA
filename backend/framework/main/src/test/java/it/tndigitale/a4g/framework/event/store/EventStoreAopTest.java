package it.tndigitale.a4g.framework.event.store;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import it.tndigitale.a4g.framework.event.store.handler.EventBus;


@SpringBootTest
@RunWith(SpringRunner.class)
public class EventStoreAopTest {
    @Autowired
    private EventBus eventBus;

    @MockBean
    private EventStoreService eventStoreService;

    @Test
    public void IF_METHOD_THROW_EXECEPTION_THEN_STORE_EVENT_AND_SEND_MAIL() throws IOException {
        eventBus.publishEvent(new TestEvent());
        
        verify(eventStoreService).triggerRetry(any(), any());
    }
}

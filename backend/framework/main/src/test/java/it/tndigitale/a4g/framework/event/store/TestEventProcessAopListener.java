package it.tndigitale.a4g.framework.event.store;

import it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class TestEventProcessAopListener {
    @EventListener
    @ReprocessEvent(reThrowException = false)
    public void process(TestEvent event){
        throw new RuntimeException("prova");
    }
}

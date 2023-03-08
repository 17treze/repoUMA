package it.tndigitale.a4g.framework.event.store;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import it.tndigitale.a4g.framework.event.Event;
import it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent;

@Aspect
@Configuration
public class EventStoreAop {
    private static final Logger logger = LoggerFactory.getLogger(EventStoreAop.class);

    private EventStoreService eventStoreService;

    @Autowired
    public EventStoreAop setEventStoreService(EventStoreService eventStoreService) {
        this.eventStoreService = eventStoreService;
        return this;
    }

    @Around("@annotation(it.tndigitale.a4g.framework.event.store.annotation.ReprocessEvent)")
    public Object processAround(ProceedingJoinPoint pjp) throws Throwable {
        Object objectReturned = null;
        try {
            objectReturned = pjp.proceed();
        } catch (Throwable ex){
            Event event = (Event) pjp.getArgs()[0];
            eventStoreService.triggerRetry(ex, event);
            ReprocessEvent annotation = getAnnotation(pjp);
            reThrowException(ex, annotation);
        }
        logger.debug("end");
        return objectReturned;
    }

    private void reThrowException(Throwable ex, ReprocessEvent annotaion) throws Throwable {
        if (annotaion.reThrowException()){
            throw ex;
        }
    }

    private ReprocessEvent getAnnotation(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(ReprocessEvent.class);
    }
}

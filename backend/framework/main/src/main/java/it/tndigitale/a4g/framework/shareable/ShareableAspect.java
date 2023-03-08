package it.tndigitale.a4g.framework.shareable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
@Slf4j
public class ShareableAspect {
    
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final ConcurrentMap<Executable, Execution> executions = new ConcurrentHashMap<>();
    
    @Around("@annotation(Shareable)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Executable executable = new Executable(joinPoint);
        Execution execution = executable.execute();
        Object result = execution.getResult();
        
        return result;
    }
     
    @EqualsAndHashCode
    @ToString
    private class Executable {
        
        @EqualsAndHashCode.Exclude
        @ToString.Exclude
        private final ProceedingJoinPoint joinPoint;
        private final Method method;
        private final Object[] arguments;

        public Executable(ProceedingJoinPoint joinPoint) {
            this.joinPoint = joinPoint;
            
            Signature signature = joinPoint.getSignature();
            
            if(!(signature instanceof MethodSignature)) {
                throw new IllegalArgumentException("Shareable non applicato ad un metodo");
            }
            
            MethodSignature methodSignature = (MethodSignature) signature;
            
            this.method = methodSignature.getMethod();
            
            Object[] args = joinPoint.getArgs();
            Annotation[][] annotations = method.getParameterAnnotations();
            
            this.arguments = IntStream
                .range(0, args.length)
                .filter(i -> Arrays
                    .stream(annotations[i])
                    .noneMatch(Shareable.Ignore.class::isInstance)
                )
                .mapToObj(i -> args[i])
                .toArray();
        }
        
        public Execution execute() {
            return executions.computeIfAbsent(this, Execution::new);
        }
        
        private Object proceed() throws ShareableExecutionException {
            try {
                log.debug("Esecuzione {} avviata", this);
                
                return joinPoint.proceed();
            } catch(Throwable throwable) {
                throw new ShareableExecutionException(throwable);
            } finally {
                log.debug("Esecuzione {} terminata", this);
            }
        }
    }
    private class Execution {

        private final Executable executable;
        private final Future<?> future;

        public Execution(Executable executable) {
            this.executable = executable;
            this.future = executor.submit(executable::proceed);
        }
        
        public Object getResult() throws Throwable {
            try {
                return future.get();
            } catch(ExecutionException executionException) {
                ShareableExecutionException shareableExecutionException = (ShareableExecutionException) executionException.getCause();

                throw shareableExecutionException.getCause();
            } finally {
                executions.remove(executable, this);
            }
        }
    }
}
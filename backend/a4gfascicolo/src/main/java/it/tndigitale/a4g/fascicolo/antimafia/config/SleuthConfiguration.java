package it.tndigitale.a4g.fascicolo.antimafia.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
// import org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

// import brave.Span;
// import brave.Tracer;

/**
 * "Inietta" il profilo spring attivo a spring in modo da vedere nella traccia di Zipkin l'ambiente (ovvero il profilo) che ha generato i log
 * @author Lorenzo Martinelli
 */
// @Component
// @Order(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1)
// extends GenericFilterBean 
public class SleuthConfiguration {

    // private final Tracer tracer;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    /*SleuthConfiguration(Tracer tracer) {
        this.tracer = tracer;
    }*/

    /*@Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan == null) {
            chain.doFilter(request, response);
            return;
        }
        // for readability we're returning trace id in a hex form
        ((HttpServletResponse) response).addHeader("ZIPKIN-TRACE-ID",
                currentSpan.context().traceIdString());
        // we can also add some custom tags
        currentSpan.tag("profile", activeProfile);

        chain.doFilter(request, response);
    }*/
}

package it.tndigitale.a4g.srt;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Classe utility che serve per fare l'override dello stato HTTP da 200 - OK a 204 - No content quando l'output di un servizio è una lista vuota.
 * L'utilizzo di @RestController e @GetMapping con le liste in output restituisce status 200 - OK.
 * e si deve controllare il body per vedere se c'è il content oppure no.
 * Con questo @ControllerAdvice, 
 * 
 * @author S.DeLuca
 *
 */

@ControllerAdvice
public class NoContentControllerAdvice implements ResponseBodyAdvice<List<?>> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return List.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public List<?> beforeBodyWrite(List<?> body, MethodParameter returnType, MediaType selectedContentType,
               Class<? extends HttpMessageConverter<?>> selectedConverterType,
               ServerHttpRequest request, ServerHttpResponse response) {

        if (body.isEmpty()) {
            response.setStatusCode(HttpStatus.NO_CONTENT);
        }
        return body;
    }
}
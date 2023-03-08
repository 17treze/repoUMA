package it.tndigitale.a4g.soc.config;

import org.apache.http.client.methods.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.Mockito.*;

public class CorsConfigTest {

    private CorsConfig corsConfig;
    private CorsRegistry registry;
    private CorsRegistration corsRegistration;

    public CorsConfigTest() {
        corsConfig = new CorsConfig();
        registry = mock(CorsRegistry.class);
        corsRegistration = mock(CorsRegistration.class);
    }

    @Test
    public void itConfigureCors() {
        doReturn(corsRegistration).when(registry).addMapping("/**");
        doReturn(corsRegistration).when(corsRegistration).allowedOrigins("*");
        doReturn(corsRegistration).when(corsRegistration)
                .allowedMethods(HttpGet.METHOD_NAME,
                                HttpPost.METHOD_NAME,
                                HttpPut.METHOD_NAME,
                                HttpDelete.METHOD_NAME,
                                HttpHead.METHOD_NAME,
                                HttpOptions.METHOD_NAME);
        doReturn(corsRegistration).when(corsRegistration).allowCredentials(true);

        corsConfig.addCorsMappings(registry);

        verify(registry).addMapping("/**");
        verify(corsRegistration).allowedOrigins("*");
        verify(corsRegistration).allowedMethods(HttpGet.METHOD_NAME,
                                                HttpPost.METHOD_NAME,
                                                HttpPut.METHOD_NAME,
                                                HttpDelete.METHOD_NAME,
                                                HttpHead.METHOD_NAME,
                                                HttpOptions.METHOD_NAME);
        verify(corsRegistration).allowCredentials(true);
    }

}

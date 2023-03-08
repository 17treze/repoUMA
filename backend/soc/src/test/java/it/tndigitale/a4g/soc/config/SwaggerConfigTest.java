package it.tndigitale.a4g.soc.config;

import org.junit.jupiter.api.Test;
import springfox.documentation.spring.web.plugins.Docket;

import static org.assertj.core.api.Assertions.assertThat;

public class SwaggerConfigTest {

    @Test
    public void itCreateApiSwagger() {
        Docket docket =  new SwaggerConfig().api();

        assertThat(docket).isNotNull();
    }

}

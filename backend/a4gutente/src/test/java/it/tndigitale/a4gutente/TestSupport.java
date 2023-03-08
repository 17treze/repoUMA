package it.tndigitale.a4gutente;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestSupport {

    public static ResultMatcher nonAutorizzato() {
        return status().isUnauthorized();
    }

}

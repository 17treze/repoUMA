package it.tndigitale.a4g.framework.support;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringSupportTest {

    @Test
    public void forGetOrDefaultIfStrNotEmptyAndWithSpaceReturnStr() {
        String result = StringSupport.getOrDefault(" ciao  ");

        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo("ciao");
    }

    @Test
    public void forGetOrDefaultIfStrNotEmptyAndWithOnlySpaceReturnNull() {
        String result = StringSupport.getOrDefault("   ");

        assertThat(result).isNull();
    }

    @Test
    public void forGetOrDefaultIfStrEmptyReturnNull() {
        String result = StringSupport.getOrDefault("");

        assertThat(result).isNull();
    }

    @Test
    public void forGetOrDefaultIfStrNullReturnNull() {
        String result = StringSupport.getOrDefault(null);

        assertThat(result).isNull();
    }
}

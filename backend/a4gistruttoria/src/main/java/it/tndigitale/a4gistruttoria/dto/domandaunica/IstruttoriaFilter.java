package it.tndigitale.a4gistruttoria.dto.domandaunica;

import io.swagger.annotations.ApiParam;

import java.util.Objects;

@Deprecated
public class IstruttoriaFilter {

    @ApiParam("Codice fiscale dell'azienda agricola")
    private String cuaa;

    @ApiParam("Anno di campagna")
    private Integer campagna;

    public String getCuaa() {
        return cuaa;
    }

    public IstruttoriaFilter setCuaa(String cuaa) {
        this.cuaa = cuaa;
        return this;
    }

    public Integer getCampagna() {
        return campagna;
    }

    public IstruttoriaFilter setCampagna(Integer campagna) {
        this.campagna = campagna;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IstruttoriaFilter that = (IstruttoriaFilter) o;
        return Objects.equals(cuaa, that.cuaa) &&
                Objects.equals(campagna, that.campagna);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cuaa, campagna);
    }
}
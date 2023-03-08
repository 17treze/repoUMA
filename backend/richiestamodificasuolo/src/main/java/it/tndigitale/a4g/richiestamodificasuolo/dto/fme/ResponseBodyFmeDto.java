package it.tndigitale.a4g.richiestamodificasuolo.dto.fme;

import java.io.Serializable;

public class ResponseBodyFmeDto implements Serializable {

    private static final long serialVersionUID = 8062275139516147954L;

    private Long id;
    private String status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

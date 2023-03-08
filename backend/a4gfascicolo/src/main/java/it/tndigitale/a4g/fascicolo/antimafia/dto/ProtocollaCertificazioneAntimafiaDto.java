package it.tndigitale.a4g.fascicolo.antimafia.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.Authentication;

import java.util.List;

public class ProtocollaCertificazioneAntimafiaDto {

    private Long idDomandaAntimafia;
    private Dichiarazione dichiarazioneAntimafia;
    private ObjectNode jsonProtocollazione;
    private ByteArrayResource documento;
    private List<ByteArrayResource> allegati;


    public Long getIdDomandaAntimafia() {
        return idDomandaAntimafia;
    }

    public ProtocollaCertificazioneAntimafiaDto setIdDomandaAntimafia(Long idDomandaAntimafia) {
        this.idDomandaAntimafia = idDomandaAntimafia;
        return this;
    }

    public Dichiarazione getDichiarazioneAntimafia() {
        return dichiarazioneAntimafia;
    }

    public ProtocollaCertificazioneAntimafiaDto setDichiarazioneAntimafia(Dichiarazione dichiarazioneAntimafia) {
        this.dichiarazioneAntimafia = dichiarazioneAntimafia;
        return this;
    }

    public ObjectNode getJsonProtocollazione() {
        return jsonProtocollazione;
    }

    public ProtocollaCertificazioneAntimafiaDto setJsonProtocollazione(ObjectNode jsonProtocollazione) {
        this.jsonProtocollazione = jsonProtocollazione;
        return this;
    }

    public ByteArrayResource getDocumento() {
        return documento;
    }

    public ProtocollaCertificazioneAntimafiaDto setDocumento(ByteArrayResource documento) {
        this.documento = documento;
        return this;
    }

    public List<ByteArrayResource> getAllegati() {
        return allegati;
    }

    public ProtocollaCertificazioneAntimafiaDto setAllegati(List<ByteArrayResource> allegati) {
        this.allegati = allegati;
        return this;
    }
}

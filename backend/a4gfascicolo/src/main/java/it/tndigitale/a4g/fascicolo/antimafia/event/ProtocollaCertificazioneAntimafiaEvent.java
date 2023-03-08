package it.tndigitale.a4g.fascicolo.antimafia.event;

import it.tndigitale.a4g.fascicolo.antimafia.dto.ProtocollaCertificazioneAntimafiaDto;
import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class ProtocollaCertificazioneAntimafiaEvent extends AbstractWrapperEvent<ProtocollaCertificazioneAntimafiaDto> {
    private ProtocollaCertificazioneAntimafiaDto protocollaCertificazioneAntimafiaDto;

    public ProtocollaCertificazioneAntimafiaEvent() {
        super();
    }

    public ProtocollaCertificazioneAntimafiaEvent(ProtocollaCertificazioneAntimafiaDto protocollaCertificazioneAntimafiaDto) {
        this.protocollaCertificazioneAntimafiaDto = protocollaCertificazioneAntimafiaDto;
    }

    @Override
    public ProtocollaCertificazioneAntimafiaDto getData() {
        return this.protocollaCertificazioneAntimafiaDto;
    }

    @Override
    public AbstractWrapperEvent<ProtocollaCertificazioneAntimafiaDto> setData(ProtocollaCertificazioneAntimafiaDto protocollaCertificazioneAntimafiaDto) {
        this.protocollaCertificazioneAntimafiaDto = protocollaCertificazioneAntimafiaDto;
        return this;
    }

}

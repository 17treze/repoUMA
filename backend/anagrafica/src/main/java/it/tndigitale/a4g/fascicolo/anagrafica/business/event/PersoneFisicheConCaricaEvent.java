package it.tndigitale.a4g.fascicolo.anagrafica.business.event;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.PersoneFisicheConCaricaExtDto;
import it.tndigitale.a4g.framework.event.AbstractWrapperEvent;

public class PersoneFisicheConCaricaEvent extends AbstractWrapperEvent<PersoneFisicheConCaricaExtDto> {

	private PersoneFisicheConCaricaExtDto personeFisicheConCaricaDto;

    public PersoneFisicheConCaricaEvent() {
        super();
    }
    
    public PersoneFisicheConCaricaEvent(PersoneFisicheConCaricaExtDto personeFisicheConCaricaDto) {
        this.personeFisicheConCaricaDto = personeFisicheConCaricaDto;
    }

    @Override
    public PersoneFisicheConCaricaExtDto getData() {
        return this.personeFisicheConCaricaDto;
    }

    @Override
    public AbstractWrapperEvent<PersoneFisicheConCaricaExtDto> setData(PersoneFisicheConCaricaExtDto personeFisicheConCaricaExtDto) {
        this.personeFisicheConCaricaDto = personeFisicheConCaricaExtDto;
        return this;
    }
}

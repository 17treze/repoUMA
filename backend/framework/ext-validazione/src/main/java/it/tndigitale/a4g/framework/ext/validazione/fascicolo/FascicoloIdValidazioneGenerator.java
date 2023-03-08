package it.tndigitale.a4g.framework.ext.validazione.fascicolo;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class FascicoloIdValidazioneGenerator extends SequenceStyleGenerator {
	
	@Override
    public Serializable generate(
    		final SharedSessionContractImplementor session, final Object object)
    		throws HibernateException {
		EntitaDominioFascicoloId idComposto = (EntitaDominioFascicoloId)session.getEntityPersister(null, object)
                      .getClassMetadata().getIdentifier(object, session);
        return idComposto.idValidazione != null ? idComposto.idValidazione : 0;
    }
}

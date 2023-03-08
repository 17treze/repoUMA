package it.tndigitale.a4gistruttoria.strategy;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.ControlliSostegno;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.util.TipoControllo;

@Component
public class DatiAZControlliSostegno extends DatiControlliSostegnoAbstract {

	public ControlliSostegno recupera(Long idIstruttoria) {
		return recuperaControlliSostegno(idIstruttoria);
	}

	@Override
	public Sostegno getIdentificativoSostegno() {
		return Sostegno.ZOOTECNIA;
	}

	@Override
	protected String buildStringaEsito(EsitoControllo esito) {
		if (esito.getTipoControllo().equals(TipoControllo.BRIDUACZ126_VerificaSanzioni) ||
			esito.getTipoControllo().equals(TipoControllo.importoMinimoAntimafia) ||
			esito.getTipoControllo().equals(TipoControllo.importoMinimoPagamento)) // Valori non-boolean
			return esito.getTipoControllo().toString().concat("_").concat(esito.getValString());
		String esitoCodificato = esito.getTipoControllo().toString();
		if (esito.getTipoControllo().equals(TipoControllo.BRIDUSDC022_idDomandaCampione))
			esitoCodificato = esitoCodificato.concat("Acz");
		if (esito.getEsito() == null) {
			return esitoCodificato.concat("_NA");
		} else if (esito.getEsito()) {
			return esitoCodificato.concat("_SI");
		} else {
			return esitoCodificato.concat("_NO");
		}
	}
}

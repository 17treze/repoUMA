package it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;

public class RichiestaModificaSuoloUtils {

	private RichiestaModificaSuoloUtils() {
	}

	public static final List<StatoRichiestaModificaSuolo> listaStatiSuoloAssociabile = Collections
			.unmodifiableList(Arrays.asList(StatoRichiestaModificaSuolo.LAVORABILE, StatoRichiestaModificaSuolo.IN_LAVORAZIONE));

	public static final List<StatoRichiestaModificaSuolo> listaStatiRichiestaModificaSuoloNonModificabile = Collections.unmodifiableList(Arrays.asList(StatoRichiestaModificaSuolo.CONCLUSA));
}

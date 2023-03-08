package it.tndigitale.a4g.framework.support;

import org.springframework.util.StringUtils;

public interface PersonaSelector {
	static final int LUNGHEZZA_CF_PERSONA_GIURIDICA = 11;
	static final int LUNGHEZZA_CF_PERSONA_FISICA = 16;

	static boolean isPersonaFisica(String codiceFiscale) {
		if (StringUtils.isEmpty(codiceFiscale) ||
				(codiceFiscale.length() != LUNGHEZZA_CF_PERSONA_GIURIDICA && codiceFiscale.length() != LUNGHEZZA_CF_PERSONA_FISICA))
			throw new IllegalArgumentException("Per capire se è una persona fisica o giuridica il codice fiscale [" +
					codiceFiscale + "] deve essere di lunghezza pari a " + LUNGHEZZA_CF_PERSONA_GIURIDICA + " o a " +
					LUNGHEZZA_CF_PERSONA_FISICA);

		return (codiceFiscale.length() == LUNGHEZZA_CF_PERSONA_FISICA);
	}

	static boolean isPersonaGiuridica(String codiceFiscale) {
		return !isPersonaFisica(codiceFiscale);
	}
}
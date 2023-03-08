package it.tndigitale.a4gistruttoria.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;

public class SincronizzazioneInputData {
	
	private static final Logger logger = LoggerFactory.getLogger(SincronizzazioneInputData.class);

	private Long numeroDomanda;
	private String cuaaIntestatario;
	private Long annoCampagna;
	
	public Long getNumeroDomanda() {
		return numeroDomanda;
	}
	
	public SincronizzazioneInputData withNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
		return this;
	}

	public String getCuaaIntestatario() {
		return cuaaIntestatario;
	}

	public SincronizzazioneInputData withCuaaIntestatario(String cuaaIntestatario) {
		this.cuaaIntestatario = cuaaIntestatario;
		return this;
	}

	public Long getAnnoCampagna() {
		return annoCampagna;
	}

	public SincronizzazioneInputData withAnnoCampagna(Long annoCampagna) {
		this.annoCampagna = annoCampagna;
		return this;
	}
	
	public static <T extends SincronizzazioneInputData> T from(DomandaUnicaModel domanda, Class<T> clazz) {
		try {
			return clazz.cast(
					clazz.newInstance()
						.withAnnoCampagna(domanda.getCampagna().longValue())
						.withCuaaIntestatario(domanda.getCuaaIntestatario())
						.withNumeroDomanda(domanda.getNumeroDomanda().longValue()));
		} catch (Exception e) {
			logger.error("Impossibile creare un'istanza SincronizzazioneInputData a partire dai dati della domanda con id {}! {}", domanda.getId(), e);
		}
		return clazz.cast(new SincronizzazioneInputData()); 
	}
}

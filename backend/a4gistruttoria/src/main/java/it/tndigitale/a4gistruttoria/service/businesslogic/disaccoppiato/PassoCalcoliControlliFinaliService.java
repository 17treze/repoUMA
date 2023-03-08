package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.lavorazione.*;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.PassoDatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import it.tndigitale.a4gistruttoria.repository.dao.TitoloDuDao;

@Service
public class PassoCalcoliControlliFinaliService extends PassoDatiElaborazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(PassoCalcoloRiduzioniService.class);

	@Autowired
	DomandeService domandaService;
	@Autowired
    DomandaUnicaDao daoDomanda;
	@Autowired
	RichiestaSuperficieDao daoRichiestaSuperficie;
	@Autowired
	TransizioneIstruttoriaService transizioneService;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	protected DatiPassoLavorazione elaboraPasso(DatiElaborazioneIstruttoria dati, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		IstruttoriaModel istruttoria = dati.getIstruttoria();
		logger.debug("elaboraPasso: inizio per istruttoria {}", istruttoria.getId());
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());
		
		calcoloVariabili(variabiliCalcolo);

		eseguiControlliFinali(variabiliCalcolo, mappaEsiti);

		valutaEsito(passo, variabiliCalcolo, mappaEsiti);

		// Memorizzo lista esiti controlli effettuati
		mappaEsiti.values().forEach(e -> {
			passo.getDatiSintesi().getEsitiControlli().add(e);
		});
		
		logger.debug("elaboraPasso: passo {} per istruttoria {}", passo.getEsito(), istruttoria.getId());


		return passo;
	}


	private void calcoloVariabili(MapVariabili variabiliCalcolo) {
		// calcolo variabili di output
		
		if (variabiliCalcolo.add(TipoVariabile.BPSIMPAMM, TipoVariabile.GREIMPAMM, TipoVariabile.GIOIMPAMM).compareTo(BigDecimal.ZERO) > 0) {

			if (variabiliCalcolo.get(TipoVariabile.BPSIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPAMM).getValNumber() != null
					&& variabiliCalcolo.get(TipoVariabile.BPSIMPAMM).getValNumber().compareTo(BigDecimal.ZERO) > 0) {
				// BPSIMPRIDRIT
				if (variabiliCalcolo.get(TipoVariabile.PERCRITISTR) != null && variabiliCalcolo.get(TipoVariabile.PERCRITISTR).getValBoolean() != null
						&& variabiliCalcolo.get(TipoVariabile.PERCRITISTR).getValBoolean()) {
					variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDRIT, BigDecimal.ZERO);
				} else {
					if (variabiliCalcolo.get(TipoVariabile.BPSIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALC).getValNumber() != null) {
						variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDRIT, variabiliCalcolo.multiply(TipoVariabile.BPSIMPCALC, TipoVariabile.PERCRIT));
					} else {
						variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDRIT, variabiliCalcolo.multiply(TipoVariabile.BPSIMPAMM, TipoVariabile.PERCRIT));
					}
				}

				// BPSIMPRIDLIN1

				if (variabiliCalcolo.get(TipoVariabile.BPSIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALC).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDLIN1, (variabiliCalcolo.get(TipoVariabile.PERCRIDLIN1).getValNumber()
							.multiply(variabiliCalcolo.get(TipoVariabile.BPSIMPCALC).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDRIT).getValNumber()))));

				} else {
					variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDLIN1, (variabiliCalcolo.get(TipoVariabile.PERCRIDLIN1).getValNumber()
							.multiply(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDRIT).getValNumber()))));
				}

				// BPSIMPBCCAP
				if (variabiliCalcolo.get(TipoVariabile.BPSIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALC).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.BPSIMPBCCAP,
							(variabiliCalcolo.get(TipoVariabile.BPSIMPCALC).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDRIT).getValNumber())
									.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDLIN1).getValNumber()).subtract(variabiliCalcolo.get(TipoVariabile.IMPSALARI).getValNumber())));

				} else {
					variabiliCalcolo.setVal(TipoVariabile.BPSIMPBCCAP,
							(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDRIT).getValNumber())
									.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDLIN1).getValNumber()).subtract(variabiliCalcolo.get(TipoVariabile.IMPSALARI).getValNumber())));
				}

				// BPSIMPRIDCAP50

				if (variabiliCalcolo.get(TipoVariabile.BPSIMPBCCAP) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPBCCAP).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDCAP50,
							(BigDecimal.ZERO.max(variabiliCalcolo.get(TipoVariabile.BPSIMPBCCAP).getValNumber().subtract(BigDecimal.valueOf(150000))).multiply(BigDecimal.valueOf(0.50))));

				}

				// BPSIMPRIDCAP100

				if (variabiliCalcolo.get(TipoVariabile.BPSIMPBCCAP) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPBCCAP).getValNumber() != null
						&& variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDCAP100,
							BigDecimal.ZERO.max(variabiliCalcolo.subtract(TipoVariabile.BPSIMPBCCAP, TipoVariabile.BPSIMPRIDCAP50).subtract(BigDecimal.valueOf(500000))));

					variabiliCalcolo.setVal(TipoVariabile.IMPRIDCAP,
							variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50).getValNumber().add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP100).getValNumber()));

				}

				// BPSIMPRIDLIN3
				if (variabiliCalcolo.get(TipoVariabile.BPSIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALC).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDLIN3,
							(variabiliCalcolo.subtract(TipoVariabile.BPSIMPCALC, TipoVariabile.BPSIMPRIDRIT, TipoVariabile.BPSIMPRIDLIN1)
									.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP100).getValNumber()))
									.multiply(variabiliCalcolo.get(TipoVariabile.PERCRIDLIN3).getValNumber())));

				} else {
					variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDLIN3,
							(variabiliCalcolo.subtract(TipoVariabile.BPSIMPAMM, TipoVariabile.BPSIMPRIDRIT, TipoVariabile.BPSIMPRIDLIN1)
									.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP100).getValNumber()))
									.multiply(variabiliCalcolo.get(TipoVariabile.PERCRIDLIN3).getValNumber())));
				}

				// BPSIMPCALCFINLORDO
				if (variabiliCalcolo.get(TipoVariabile.BPSIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALC).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.BPSIMPCALCFINLORDO,
							(variabiliCalcolo.subtract(TipoVariabile.BPSIMPCALC, TipoVariabile.BPSIMPRIDRIT, TipoVariabile.BPSIMPRIDLIN1)
									.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP100).getValNumber()))
									.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDLIN3).getValNumber()))
							.multiply(variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO) != null ? variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO).getValNumber() : BigDecimal.ONE));

				} else {
					variabiliCalcolo.setVal(TipoVariabile.BPSIMPCALCFINLORDO,
							(variabiliCalcolo.subtract(TipoVariabile.BPSIMPAMM, TipoVariabile.BPSIMPRIDRIT, TipoVariabile.BPSIMPRIDLIN1)
									.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP100).getValNumber()))
									.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDLIN3).getValNumber()))
							.multiply(variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO) != null ? variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO).getValNumber() : BigDecimal.ONE));
				}
				
				// BPSIMPCALCFIN
				variabiliCalcolo.setVal(TipoVariabile.BPSIMPCALCFIN, 
						variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFINLORDO).getValNumber()
							.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPEROGATO) != null ? variabiliCalcolo.get(TipoVariabile.BPSIMPEROGATO).getValNumber() : BigDecimal.ZERO));
			}

			if (variabiliCalcolo.get(TipoVariabile.GREIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GREIMPAMM).getValNumber() != null
					&& variabiliCalcolo.get(TipoVariabile.GREIMPAMM).getValNumber().compareTo(BigDecimal.ZERO) > 0) {
				// GREIMPRIDRIT
				if (variabiliCalcolo.get(TipoVariabile.PERCRITISTR) != null && variabiliCalcolo.get(TipoVariabile.PERCRITISTR).getValBoolean() != null
						&& variabiliCalcolo.get(TipoVariabile.PERCRITISTR).getValBoolean()) {
					variabiliCalcolo.setVal(TipoVariabile.GREIMPRIDRIT, BigDecimal.ZERO);
				} else {
					if (variabiliCalcolo.get(TipoVariabile.GREIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.GREIMPCALC).getValNumber() != null) {
						variabiliCalcolo.setVal(TipoVariabile.GREIMPRIDRIT, variabiliCalcolo.multiply(TipoVariabile.GREIMPCALC, TipoVariabile.PERCRIT));

					} else {
						variabiliCalcolo.setVal(TipoVariabile.GREIMPRIDRIT, variabiliCalcolo.multiply(TipoVariabile.GREIMPAMM, TipoVariabile.PERCRIT));
					}
				}

				// GREIMPRIDLIN3

				if (variabiliCalcolo.get(TipoVariabile.GREIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.GREIMPCALC).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.GREIMPRIDLIN3,
							(variabiliCalcolo.subtract(TipoVariabile.GREIMPCALC, TipoVariabile.GREIMPRIDRIT).multiply(variabiliCalcolo.get(TipoVariabile.PERCRIDLIN3).getValNumber())));

				} else {
					variabiliCalcolo.setVal(TipoVariabile.GREIMPRIDLIN3,
							(variabiliCalcolo.subtract(TipoVariabile.GREIMPAMM, TipoVariabile.GREIMPRIDRIT).multiply(variabiliCalcolo.get(TipoVariabile.PERCRIDLIN3).getValNumber())));
				}

				// GREIMPCALCFINLORDO
				if (variabiliCalcolo.get(TipoVariabile.GREIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.GREIMPCALC).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.GREIMPCALCFINLORDO, (variabiliCalcolo.subtract(TipoVariabile.GREIMPCALC, TipoVariabile.GREIMPRIDRIT, TipoVariabile.GREIMPRIDLIN3))
							.multiply(variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO) != null ? variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO).getValNumber() : BigDecimal.ONE));

				} else {
					variabiliCalcolo.setVal(TipoVariabile.GREIMPCALCFINLORDO, (variabiliCalcolo.subtract(TipoVariabile.GREIMPAMM, TipoVariabile.GREIMPRIDRIT, TipoVariabile.GREIMPRIDLIN3))
							.multiply(variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO) != null ? variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO).getValNumber() : BigDecimal.ONE));
				}
				
				// GREIMPCALCFIN
				variabiliCalcolo.setVal(TipoVariabile.GREIMPCALCFIN, 
						variabiliCalcolo.get(TipoVariabile.GREIMPCALCFINLORDO).getValNumber()
							.subtract(variabiliCalcolo.get(TipoVariabile.GREIMPEROGATO) != null ? variabiliCalcolo.get(TipoVariabile.GREIMPEROGATO).getValNumber() : BigDecimal.ZERO));
			}

			if (variabiliCalcolo.get(TipoVariabile.GIOIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber() != null
					&& variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber().compareTo(BigDecimal.ZERO) > 0) {

				// GIOIMPRIDRIT
				if (variabiliCalcolo.get(TipoVariabile.PERCRITISTR) != null && variabiliCalcolo.get(TipoVariabile.PERCRITISTR).getValBoolean() != null
						&& variabiliCalcolo.get(TipoVariabile.PERCRITISTR).getValBoolean()) {
					variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDRIT, BigDecimal.ZERO);
				} else {
					if (variabiliCalcolo.get(TipoVariabile.GIOIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPCALC).getValNumber() != null) {
						variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDRIT, variabiliCalcolo.multiply(TipoVariabile.GIOIMPCALC, TipoVariabile.PERCRIT));

					} else {
						if (variabiliCalcolo.get(TipoVariabile.GIOIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber() != null) {
							variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDRIT, variabiliCalcolo.multiply(TipoVariabile.GIOIMPAMM, TipoVariabile.PERCRIT));
						} else {
							variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDRIT, BigDecimal.ZERO);
						}
					}
				}

				// GIOIMPRIDLIN2
				if (variabiliCalcolo.get(TipoVariabile.GIOIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPCALC).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDLIN2,
							(variabiliCalcolo.subtract(TipoVariabile.GIOIMPCALC, TipoVariabile.GIOIMPRIDRIT).multiply(variabiliCalcolo.get(TipoVariabile.PERCRIDLIN2).getValNumber())));

				} else {
					if (variabiliCalcolo.get(TipoVariabile.GIOIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber() != null) {
						variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDLIN2,
								(variabiliCalcolo.subtract(TipoVariabile.GIOIMPAMM, TipoVariabile.GIOIMPRIDRIT).multiply(variabiliCalcolo.get(TipoVariabile.PERCRIDLIN2).getValNumber())));
					} else {
						variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDLIN2, BigDecimal.ZERO);
					}
				}

				// GIOIMPRIDLIN3
				if (variabiliCalcolo.get(TipoVariabile.GIOIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPCALC).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDLIN3, (variabiliCalcolo.subtract(TipoVariabile.GIOIMPCALC, TipoVariabile.GIOIMPRIDRIT, TipoVariabile.GIOIMPRIDLIN2)
							.multiply(variabiliCalcolo.get(TipoVariabile.PERCRIDLIN3).getValNumber())));

				} else {
					if (variabiliCalcolo.get(TipoVariabile.GIOIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber() != null) {
						variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDLIN3, (variabiliCalcolo.subtract(TipoVariabile.GIOIMPAMM, TipoVariabile.GIOIMPRIDRIT, TipoVariabile.GIOIMPRIDLIN2)
								.multiply(variabiliCalcolo.get(TipoVariabile.PERCRIDLIN3).getValNumber())));
					} else {
						variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDLIN3, BigDecimal.ZERO);
					}

				}

				// GIOIMPCALCFINLORDO
				if (variabiliCalcolo.get(TipoVariabile.GIOIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPCALC).getValNumber() != null) {

					variabiliCalcolo.setVal(TipoVariabile.GIOIMPCALCFINLORDO, (variabiliCalcolo.subtract(TipoVariabile.GIOIMPCALC, TipoVariabile.GIOIMPRIDRIT, TipoVariabile.GIOIMPRIDLIN2)
							.subtract(variabiliCalcolo.get(TipoVariabile.GIOIMPRIDLIN3).getValNumber())));

				} else {
					if (variabiliCalcolo.get(TipoVariabile.GIOIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber() != null) {
						variabiliCalcolo.setVal(TipoVariabile.GIOIMPCALCFINLORDO, (variabiliCalcolo.subtract(TipoVariabile.GIOIMPAMM, TipoVariabile.GIOIMPRIDRIT, TipoVariabile.GIOIMPRIDLIN2)
								.subtract(variabiliCalcolo.get(TipoVariabile.GIOIMPRIDLIN3).getValNumber()))
								.multiply(variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO) != null ? variabiliCalcolo.get(TipoVariabile.PERCPAGAMENTO).getValNumber() : BigDecimal.ONE));
					} else {
						variabiliCalcolo.setVal(TipoVariabile.GIOIMPCALCFIN, BigDecimal.ZERO);
					}
				}
				
				// GIOIMPCALCFIN
				variabiliCalcolo.setVal(TipoVariabile.GIOIMPCALCFIN, 
						variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFINLORDO).getValNumber()
							.subtract(variabiliCalcolo.get(TipoVariabile.GIOIMPEROGATO) != null ? variabiliCalcolo.get(TipoVariabile.GIOIMPEROGATO).getValNumber() : BigDecimal.ZERO));
			}

			if (variabiliCalcolo.get(TipoVariabile.BPSIMPRIDRIT) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPRIDRIT).getValNumber() != null) {
				variabiliCalcolo.setVal(TipoVariabile.IMPRIDRIT, variabiliCalcolo.get(TipoVariabile.BPSIMPRIDRIT).getValNumber());
			}

			if (variabiliCalcolo.get(TipoVariabile.GREIMPRIDRIT) != null && variabiliCalcolo.get(TipoVariabile.GREIMPRIDRIT).getValNumber() != null) {
				variabiliCalcolo.setVal(TipoVariabile.IMPRIDRIT, variabiliCalcolo.get(TipoVariabile.IMPRIDRIT).getValNumber().add(variabiliCalcolo.get(TipoVariabile.GREIMPRIDRIT).getValNumber()));
			}

			if (variabiliCalcolo.get(TipoVariabile.GIOIMPRIDRIT) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPRIDRIT).getValNumber() != null) {
				variabiliCalcolo.setVal(TipoVariabile.IMPRIDRIT, variabiliCalcolo.get(TipoVariabile.IMPRIDRIT).getValNumber().add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIDRIT).getValNumber()));
			}

		} else {
			variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIDRIT, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GREIMPRIDRIT, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIDRIT, BigDecimal.ZERO);
		}

		// IMPCALC - NVL(BPSIMPCALC,BPSIMPAMM)+NVL(GREIMPCALC,GREIMPAMM)+NVL(GIOIMPCALC ,GIOIMPAMM)
		BigDecimal impcalc = variabiliCalcolo.add(
				variabiliCalcolo.get(TipoVariabile.BPSIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALC).getValNumber() != null ? TipoVariabile.BPSIMPCALC : TipoVariabile.BPSIMPAMM,
				variabiliCalcolo.get(TipoVariabile.GREIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.GREIMPCALC).getValNumber() != null ? TipoVariabile.GREIMPCALC : TipoVariabile.GREIMPAMM,
				variabiliCalcolo.get(TipoVariabile.GIOIMPCALC) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPCALC).getValNumber() != null ? TipoVariabile.GIOIMPCALC : TipoVariabile.GIOIMPAMM);
		variabiliCalcolo.setVal(TipoVariabile.IMPCALC, impcalc);

		// IMPCALCFIN
		BigDecimal impCalcFin = variabiliCalcolo
				.add(variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFIN) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFIN).getValNumber() != null
						? variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFIN).getValNumber() : BigDecimal.ZERO,
						variabiliCalcolo.get(TipoVariabile.GREIMPCALCFIN) != null && variabiliCalcolo.get(TipoVariabile.GREIMPCALCFIN).getValNumber() != null
								? variabiliCalcolo.get(TipoVariabile.GREIMPCALCFIN).getValNumber() : BigDecimal.ZERO)
				.add(variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFIN) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFIN).getValNumber() != null
						? variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFIN).getValNumber() : BigDecimal.ZERO);
		variabiliCalcolo.setVal(TipoVariabile.IMPCALCFIN, impCalcFin);
		
		// IMPCALCFINLORDO
		BigDecimal impCalcFinLordo = variabiliCalcolo
				.add(variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFINLORDO) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFINLORDO).getValNumber() != null
						? variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFINLORDO).getValNumber() : BigDecimal.ZERO,
						variabiliCalcolo.get(TipoVariabile.GREIMPCALCFINLORDO) != null && variabiliCalcolo.get(TipoVariabile.GREIMPCALCFINLORDO).getValNumber() != null
								? variabiliCalcolo.get(TipoVariabile.GREIMPCALCFINLORDO).getValNumber() : BigDecimal.ZERO)
				.add(variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFINLORDO) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFINLORDO).getValNumber() != null
						? variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFINLORDO).getValNumber() : BigDecimal.ZERO);
		variabiliCalcolo.setVal(TipoVariabile.IMPCALCFINLORDO, impCalcFinLordo);

	}

	public void eseguiControlliFinali(MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		mappaEsiti.put(
				TipoControllo.BRIDUSDC043_riduzioneCapping,
				new EsitoControllo(
						TipoControllo.BRIDUSDC043_riduzioneCapping,
						hasRiduzioneCapping(variabiliCalcolo)));
		mappaEsiti.put(
				TipoControllo.BRIDUSDC036_verificaRitardo,
				new EsitoControllo(
						TipoControllo.BRIDUSDC036_verificaRitardo,
						variabiliCalcolo.get(TipoVariabile.PERCRIT) != null
						&& variabiliCalcolo.get(TipoVariabile.PERCRIT).getValNumber() != null
						&& variabiliCalcolo.get(TipoVariabile.PERCRIT).getValNumber().compareTo(BigDecimal.ZERO) > 0));
		mappaEsiti.put(
				TipoControllo.BRIDUSDC134_importoErogabilePositivo,
				new EsitoControllo(
						TipoControllo.BRIDUSDC134_importoErogabilePositivo,
						variabiliCalcolo.get(TipoVariabile.IMPCALCFIN) != null
						&& variabiliCalcolo.get(TipoVariabile.IMPCALCFIN).getValNumber() != null
						&& variabiliCalcolo.get(TipoVariabile.IMPCALCFIN).getValNumber().compareTo(BigDecimal.ZERO) > 0));
	}

	public void valutaEsito(DatiPassoLavorazione passo, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		List<VariabileCalcolo> listaOutput = new ArrayList<VariabileCalcolo>();
		passo.getDatiOutput().setVariabiliCalcolo(listaOutput);

		if (!mapEsiti.get(TipoControllo.BRIDUSDC036_verificaRitardo).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC043_riduzioneCapping).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC134_importoErogabilePositivo).getEsito()) {
			passo.setCodiceEsito(CodiceEsito.DUF_014.getCodiceEsito());
		}
		if (mapEsiti.get(TipoControllo.BRIDUSDC036_verificaRitardo).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC043_riduzioneCapping).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC134_importoErogabilePositivo).getEsito()) {
			passo.setCodiceEsito(CodiceEsito.DUF_015.getCodiceEsito());
		}
		if (!mapEsiti.get(TipoControllo.BRIDUSDC036_verificaRitardo).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC043_riduzioneCapping).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC134_importoErogabilePositivo).getEsito()) {
			passo.setCodiceEsito(CodiceEsito.DUF_016.getCodiceEsito());
		}
		if (mapEsiti.get(TipoControllo.BRIDUSDC036_verificaRitardo).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC043_riduzioneCapping).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC134_importoErogabilePositivo).getEsito()) {
			passo.setCodiceEsito(CodiceEsito.DUF_017.getCodiceEsito());
		}
		if (!mapEsiti.get(TipoControllo.BRIDUSDC036_verificaRitardo).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC043_riduzioneCapping).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC134_importoErogabilePositivo).getEsito()) {
			passo.setCodiceEsito(CodiceEsito.DUF_039.getCodiceEsito());
		}
		if (mapEsiti.get(TipoControllo.BRIDUSDC036_verificaRitardo).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC043_riduzioneCapping).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC134_importoErogabilePositivo).getEsito()) {
			passo.setCodiceEsito(CodiceEsito.DUF_040.getCodiceEsito());
		}
		if (!mapEsiti.get(TipoControllo.BRIDUSDC036_verificaRitardo).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC043_riduzioneCapping).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC134_importoErogabilePositivo).getEsito()) {
			passo.setCodiceEsito(CodiceEsito.DUF_041.getCodiceEsito());
		}
		if (mapEsiti.get(TipoControllo.BRIDUSDC036_verificaRitardo).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC043_riduzioneCapping).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC134_importoErogabilePositivo).getEsito()) {
			passo.setCodiceEsito(CodiceEsito.DUF_042.getCodiceEsito());
		}
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPRIDRIT) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDRIT));
		}
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPRIDLIN1) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDLIN1));
		}
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPBCCAP) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPBCCAP));
		}
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50));
		}
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP100) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP100));
		}
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPRIDLIN3) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIDLIN3));
		}
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFIN) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFIN));
		}
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFINLORDO) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPCALCFINLORDO));
		}
		if (variabiliCalcolo.get(TipoVariabile.GREIMPRIDRIT) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIDRIT));
		}
		if (variabiliCalcolo.get(TipoVariabile.GREIMPRIDLIN3) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIDLIN3));
		}
		if (variabiliCalcolo.get(TipoVariabile.GREIMPCALCFIN) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPCALCFIN));
		}
		if (variabiliCalcolo.get(TipoVariabile.GREIMPCALCFINLORDO) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPCALCFINLORDO));
		}
		if (variabiliCalcolo.get(TipoVariabile.GIOIMPRIDRIT) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIDRIT));
		}
		if (variabiliCalcolo.get(TipoVariabile.GIOIMPRIDLIN2) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIDLIN2));
		}
		if (variabiliCalcolo.get(TipoVariabile.GIOIMPRIDLIN3) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIDLIN3));
		}
		if (variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFIN) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFIN));
		}
		if (variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFINLORDO) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPCALCFINLORDO));
		}
		if (variabiliCalcolo.get(TipoVariabile.IMPCALC) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.IMPCALC));
		}
		if (variabiliCalcolo.get(TipoVariabile.IMPCALCFIN) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.IMPCALCFIN));
		}
		if (variabiliCalcolo.get(TipoVariabile.IMPRIDRIT) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.IMPRIDRIT));
		}
		if (variabiliCalcolo.get(TipoVariabile.IMPCALCFINLORDO) != null) {
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.IMPCALCFINLORDO));
		}

		passo.setEsito(true);
	}

	public boolean hasRiduzioneCapping(MapVariabili variabiliCalcolo) {
		return variabiliCalcolo.get(TipoVariabile.BPSIMPRIDCAP50).getValNumber().compareTo(BigDecimal.ZERO) > 0;
	}

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.CONTROLLI_FINALI;
	}
	
}

package it.tndigitale.a4gistruttoria.service.businesslogic.liquidabilita;

import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegno;
import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegnoFactory;
import it.tndigitale.a4gistruttoria.dto.InfoLiquidabilita;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.PassoDatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidabilita.ControlloLiquidabilitaService.DatiLiquidabilitaIstruttoria;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Optional;

@Service
public class PassoControlloLiquidabilitaService extends PassoDatiElaborazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(PassoControlloLiquidabilitaService.class);
	
	@Autowired
	private DomandeService serviceDomande;
	@Autowired
	private CaricaPremioCalcolatoSostegnoFactory premioCalcolatoSostegnoFactory;
	
	@Override
	protected DatiPassoLavorazione elaboraPasso(DatiElaborazioneIstruttoria dati, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());
		
		calcolaEsiti(dati, mappaEsiti);
		mappaEsitiNelPasso(mappaEsiti, passo);
		
		return passo;
	}
	
	protected void calcolaEsiti(DatiElaborazioneIstruttoria dati, HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		IstruttoriaModel istruttoria = dati.getIstruttoria();
		InfoLiquidabilita infoLiquidabilita = caricaInfoLiquidabilita(istruttoria);
		if (dati instanceof DatiLiquidabilitaIstruttoria) {
			((DatiLiquidabilitaIstruttoria)dati).setIban(infoLiquidabilita.getIban());
		}
		
		boolean eredi = Optional.ofNullable(istruttoria.getDomandaUnicaModel().getA4gtDatiEredes())
				.filter(l -> !l.isEmpty())
				.map(el -> el.get(0))
				.map(erede -> erede.getCertificato())
				.orElse(false);

		CheckImportoCalcoloFinale checkImportoCalcoloFinale = verificaImportoCalcoloFinale(dati);
		Boolean esitoImportoErogabilePositivo = CheckImportoCalcoloFinale.POSITIVO.equals(checkImportoCalcoloFinale);
		Boolean esitoImportoErogabileNegativo = CheckImportoCalcoloFinale.NEGATIVO.equals(checkImportoCalcoloFinale);
		Boolean esitoImportoErogabileZero = CheckImportoCalcoloFinale.ZERO.equals(checkImportoCalcoloFinale);
		
		mapEsiti.put(TipoControllo.BRIDUSDL133_importoErogabilePositivo, new EsitoControllo(TipoControllo.BRIDUSDL133_importoErogabilePositivo, esitoImportoErogabilePositivo));//sempre a false capire se inserirlo
		
		if (esitoImportoErogabilePositivo) {
			mapEsiti.put(TipoControllo.BRIDUSDL038_titolare, new EsitoControllo(TipoControllo.BRIDUSDL038_titolare, infoLiquidabilita.getTitolareDeceduto()));
			if (infoLiquidabilita.getTitolareDeceduto()) {
				mapEsiti.put(TipoControllo.BRIDUNVL129_erede, new EsitoControllo(TipoControllo.BRIDUNVL129_erede, eredi));
			} else {
				mapEsiti.put(TipoControllo.BRIDUSDL037_iban, new EsitoControllo(TipoControllo.BRIDUSDL037_iban, infoLiquidabilita.getIbanValido()));
			}
			mapEsiti.put(TipoControllo.BRIDUSDL039_agea, new EsitoControllo(TipoControllo.BRIDUSDL039_agea, infoLiquidabilita.getDomandaSospesaAgea()));
		}
		else {
			mapEsiti.put(TipoControllo.BRIDUSDL134_importoErogabileNegativo, new EsitoControllo(TipoControllo.BRIDUSDL134_importoErogabileNegativo, esitoImportoErogabileNegativo));
			mapEsiti.put(TipoControllo.BRIDUSDL135_importoErogabileZero, new EsitoControllo(TipoControllo.BRIDUSDL135_importoErogabileZero, esitoImportoErogabileZero));//se true 0 se false irrilevante
			mapEsiti.put(TipoControllo.BRIDUSDL135_importoErogabileIrrilevante, new EsitoControllo(TipoControllo.BRIDUSDL135_importoErogabileIrrilevante, !esitoImportoErogabileZero && !esitoImportoErogabileNegativo));//se true > 0 e <= 12
		}
	}

	protected void mappaEsitiNelPasso(HashMap<TipoControllo, EsitoControllo> mapEsiti, DatiPassoLavorazione passo) {
		if (importoErogabilePositivo(mapEsiti)) {
			if (titolareDeceduto(mapEsiti)) {
				if (datiEredeValidi(mapEsiti)) {
					if (listaNeraAgea(mapEsiti)) {
						passo.setCodiceEsito(CodiceEsito.DUF_022.getCodiceEsito());
						passo.setEsito(false);
					} else {
						passo.setCodiceEsito(CodiceEsito.DUF_021.getCodiceEsito());
						passo.setEsito(true);
					}
				} else {
					passo.setCodiceEsito(CodiceEsito.DUF_036.getCodiceEsito());
					passo.setEsito(false);
				}
			} else {
				if (ibanValido(mapEsiti)) {
					if (listaNeraAgea(mapEsiti)) {
						passo.setCodiceEsito(CodiceEsito.DUF_020.getCodiceEsito());
						passo.setEsito(false);
					} else {
						passo.setCodiceEsito(CodiceEsito.DUF_019.getCodiceEsito());
						passo.setEsito(true);
					}
				} else {
					if (listaNeraAgea(mapEsiti)) {
						passo.setCodiceEsito(CodiceEsito.DUF_024.getCodiceEsito());
					} else {
						passo.setCodiceEsito(CodiceEsito.DUF_023.getCodiceEsito());
					}
					passo.setEsito(false);
				}
			}
		}
		else {
			//esito importo negativo 0 o irrilevante
			passo.setEsito(false);//non liquidabile o debiti
			if (importoErogabileNegativo(mapEsiti)) {
				passo.setCodiceEsito(CodiceEsito.DUF_044.getCodiceEsito());
			}
			else if (importoErogabileZero(mapEsiti)) {
				passo.setCodiceEsito(CodiceEsito.DUF_043.getCodiceEsito());
			}
			else if(importoErogabileIrrilevante(mapEsiti)){//irrilevante > 0 e <= 12 
				passo.setCodiceEsito(CodiceEsito.DUF_045.getCodiceEsito());
			}
		} 
			
		// Memorizzo lista esiti controlli effettuati
		mapEsiti.values().forEach(e -> {
			passo.getDatiSintesi().getEsitiControlli().add(e);
		});
	}

	private Boolean importoErogabilePositivo(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		return mapEsiti.get(TipoControllo.BRIDUSDL133_importoErogabilePositivo).getEsito();
	}
	
	private Boolean importoErogabileNegativo(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		return mapEsiti.get(TipoControllo.BRIDUSDL134_importoErogabileNegativo).getEsito();
	}
	
	private Boolean importoErogabileZero(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		return mapEsiti.get(TipoControllo.BRIDUSDL135_importoErogabileZero).getEsito();
	}
	
	private Boolean importoErogabileIrrilevante(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		return mapEsiti.get(TipoControllo.BRIDUSDL135_importoErogabileIrrilevante).getEsito();
	}
	
	private Boolean titolareDeceduto(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		return mapEsiti.get(TipoControllo.BRIDUSDL038_titolare).getEsito();
	}

	private Boolean datiEredeValidi(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		return mapEsiti.get(TipoControllo.BRIDUNVL129_erede).getEsito();
	}

	private Boolean ibanValido(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		return mapEsiti.get(TipoControllo.BRIDUSDL037_iban).getEsito();
	}

	private Boolean listaNeraAgea(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		return mapEsiti.get(TipoControllo.BRIDUSDL039_agea).getEsito();
	}

	protected InfoLiquidabilita caricaInfoLiquidabilita(IstruttoriaModel istruttoria)  {
		Long numeroDomanda = istruttoria.getDomandaUnicaModel().getNumeroDomanda().longValue();
		try {
			return serviceDomande.recuperaInfoLiquidabilita(numeroDomanda);
		} catch (ConnectException e) {
			String messaggio = "Errore caricando i dati di liquidabilita per la domanda " + numeroDomanda;
			logger.error(messaggio, e);
			throw new RuntimeException(messaggio, e);
		}
	}

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.LIQUIDABILITA;
	}
	
	private CheckImportoCalcoloFinale verificaImportoCalcoloFinale(DatiElaborazioneIstruttoria dati) {
		IstruttoriaModel istruttoria = dati.getTransizione().getIstruttoria();
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		CaricaPremioCalcolatoSostegno premioLoader =
				premioCalcolatoSostegnoFactory.getCaricatorePremioBySostegno(CaricaPremioCalcolatoSostegno.getNomeQualificatore(istruttoria.getSostegno()));
		try {
			Double vImpCalcFin = premioLoader.getPremio(domanda);
			if (vImpCalcFin != null) {
				return vImpCalcFin >= 12 //12 valor eimporto minimo
						? CheckImportoCalcoloFinale.POSITIVO
						: (vImpCalcFin == 0) ? CheckImportoCalcoloFinale.ZERO: ((vImpCalcFin < 0) ? CheckImportoCalcoloFinale.NEGATIVO : CheckImportoCalcoloFinale.IRRILEVANTE);
			}
		} catch (Exception e) {
			return CheckImportoCalcoloFinale.IRRILEVANTE;
		}
		return CheckImportoCalcoloFinale.IRRILEVANTE;
	}
	
	private enum CheckImportoCalcoloFinale {
		POSITIVO,
		NEGATIVO,
		ZERO,
		IRRILEVANTE
	}
		

}

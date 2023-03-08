package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.service.businesslogic.PassoDatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Service
public class PassoCalcoloAmmissibilitaService extends PassoDatiElaborazioneIstruttoria {

	@Override
	protected DatiPassoLavorazione elaboraPasso(DatiElaborazioneIstruttoria dati, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());
		
		eseguiControlliAmmissibilita(mappaEsiti, variabiliCalcolo, passo);
		eseguiCalcoliAmmissibilita(mappaEsiti, variabiliCalcolo, passo);
		
		return passo;
	}
	
	private void eseguiControlliAmmissibilita(HashMap<TipoControllo, EsitoControllo> mappaEsiti, MapVariabili variabiliCalcolo, DatiPassoLavorazione passo) {

		// esiti
		mappaEsiti.put(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo, new EsitoControllo(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo, getInfoAgricoltoreAttivo(variabiliCalcolo)));
		mappaEsiti.put(TipoControllo.BRIDUSDC010_agricoltoreAttivo, new EsitoControllo(TipoControllo.BRIDUSDC010_agricoltoreAttivo, getAgricoltoreAttivo(variabiliCalcolo)));

		mappaEsiti.put(TipoControllo.BRIDUSDC011_impegnoTitoli, new EsitoControllo(TipoControllo.BRIDUSDC011_impegnoTitoli, getImpegnoTitoli(variabiliCalcolo)));
		mappaEsiti.put(TipoControllo.BRIDUSDC012_superficieMinima, new EsitoControllo(TipoControllo.BRIDUSDC012_superficieMinima, getSuperficieMinima(variabiliCalcolo)));

		// da calcolare in base ai singoli valori
		if (mappaEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && mappaEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito()
				&& mappaEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() && mappaEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {
			passo.setEsito(true);
		} else {
			passo.setEsito(false);
		}
	}

	private void eseguiCalcoliAmmissibilita(HashMap<TipoControllo, EsitoControllo> mapEsiti, MapVariabili variabiliCalcolo, DatiPassoLavorazione passo) {
		
		calcoloVariabili(mapEsiti, variabiliCalcolo);
		
		List<VariabileCalcolo> listaOutput = new ArrayList<VariabileCalcolo>();

		if (mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_001.getCodiceEsito());

		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));
			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_001.getCodiceEsito());

		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));
			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_002.getCodiceEsito());

		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));
			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_003.getCodiceEsito());

		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));
			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_004.getCodiceEsito());

		} else if (mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));

			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_005.getCodiceEsito());

		} else if (mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));
			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_006.getCodiceEsito());

		} else if (mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));
			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_007.getCodiceEsito());

		} else if (mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));
			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_008.getCodiceEsito());

		} else if (mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));

			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {

				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_009.getCodiceEsito());

		} else if (mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito()
				&& mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));

			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_010.getCodiceEsito());

		} else if (mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito() && mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito()
				&& !mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() && !mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito()) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPRIC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GRESUPAMM));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPAMM));

			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRIC));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALGIORID));
			}
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.TITVALRID));

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_011.getCodiceEsito());
			
			

		}
		// Memorizzo lista esiti controlli effettuati
		boolean infoAgricoltoreAttivo = mapEsiti.get(TipoControllo.BRIDUSDC009_infoAgricoltoreAttivo).getEsito();
		List<EsitoControllo> esiti = mapEsiti.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
		if (!infoAgricoltoreAttivo) { // se non ho info agricoltore attivo => elimino la bridu su agri attivo
			esiti = 
					esiti.stream().filter(e -> !TipoControllo.BRIDUSDC010_agricoltoreAttivo.equals(e.getTipoControllo())).collect(Collectors.toList());
		}
		passo.getDatiSintesi().getEsitiControlli().addAll(esiti);
	}
	
	private void calcoloVariabili(HashMap<TipoControllo, EsitoControllo> mappaEsiti, MapVariabili variabiliCalcolo) {

		if (!mappaEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito()) {
			variabiliCalcolo.setVal(TipoVariabile.BPSSUPRIC, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIC, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.BPSSUPAMM, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.BPSIMPAMM, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GRESUPRIC, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GREIMPRIC, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GRESUPAMM, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GREIMPAMM, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GIOSUPRIC, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIC, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GIOSUPAMM, BigDecimal.ZERO);
			variabiliCalcolo.setVal(TipoVariabile.GIOIMPAMM, BigDecimal.ZERO);

		} else {

			variabiliCalcolo.setVal(TipoVariabile.TITVALRID, getTitValRid(variabiliCalcolo));

			// BPS
			variabiliCalcolo.setVal(TipoVariabile.BPSSUPRIC, variabiliCalcolo.min(TipoVariabile.BPSSUPIMP, TipoVariabile.TITSUP));
			variabiliCalcolo.setVal(TipoVariabile.BPSIMPRIC, variabiliCalcolo.multiply(TipoVariabile.BPSSUPRIC).multiply(variabiliCalcolo.get(TipoVariabile.TITVALRID).getValNumber()));

			BigDecimal calcolo = null;
			calcolo = getBpsSupAmm(mappaEsiti);
			if (calcolo != null)
				variabiliCalcolo.setVal(TipoVariabile.BPSSUPAMM, calcolo);
			calcolo = getBpsImpAmm(mappaEsiti);
			if (calcolo != null)
				variabiliCalcolo.setVal(TipoVariabile.BPSIMPAMM, calcolo);

			// GREENING - variabili valorizzate se e solo se GRERIC = true
			if (variabiliCalcolo.get(TipoVariabile.GRERIC).getValBoolean()) {
				calcolo = getGreSupRic(variabiliCalcolo);
				if (calcolo != null)
					variabiliCalcolo.setVal(TipoVariabile.GRESUPRIC, calcolo);
				calcolo = getGreImpRic(variabiliCalcolo);
				if (calcolo != null)
					variabiliCalcolo.setVal(TipoVariabile.GREIMPRIC, calcolo);
				calcolo = getGreSupAmm(mappaEsiti);
				if (calcolo != null)
					variabiliCalcolo.setVal(TipoVariabile.GRESUPAMM, calcolo);
				calcolo = getGreImpAmm(mappaEsiti);
				if (calcolo != null)
					variabiliCalcolo.setVal(TipoVariabile.GREIMPAMM, calcolo);
			}

			// GIOVANE - variabili valorizzate se e solo se GIORIC = true
			if (variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean()) {
				calcolo = getTitValGioRid(variabiliCalcolo);
				if (calcolo != null)
					variabiliCalcolo.setVal(TipoVariabile.TITVALGIORID, calcolo);
				calcolo = getGioSupRic(variabiliCalcolo);
				if (calcolo != null)
					variabiliCalcolo.setVal(TipoVariabile.GIOSUPRIC, calcolo);
				calcolo = getGioImpRic(variabiliCalcolo);
				if (calcolo != null)
					variabiliCalcolo.setVal(TipoVariabile.GIOIMPRIC, calcolo);
				calcolo = getGioSupAmm(mappaEsiti);
				if (calcolo != null)
					variabiliCalcolo.setVal(TipoVariabile.GIOSUPAMM, calcolo);
				calcolo = getGioImpAmm(mappaEsiti);
				if (calcolo != null)
					variabiliCalcolo.setVal(TipoVariabile.GIOIMPAMM, calcolo);
			}
		}
	}
	

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.AMMISSIBILITA;
	}

	@Override
	protected MapVariabili initVariabiliCalcolo(DatiElaborazioneIstruttoria dati) {
		MapVariabili variabiliCalcolo = super.initVariabiliCalcolo(dati);
		// Aggiunta variabili di output nella lista variabili di calcolo
		variabiliCalcolo.addElements(TipoVariabile.BPSSUPRIC, TipoVariabile.BPSIMPRIC, TipoVariabile.BPSSUPAMM, TipoVariabile.BPSIMPAMM, TipoVariabile.GRESUPRIC, TipoVariabile.GREIMPRIC,
				TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM, TipoVariabile.GIOSUPRIC, TipoVariabile.GIOIMPRIC, TipoVariabile.GIOSUPAMM, TipoVariabile.GIOIMPAMM, TipoVariabile.TITVALRID,
				TipoVariabile.TITVALGIORID);
		return variabiliCalcolo;
	}

	private Boolean getInfoAgricoltoreAttivo(MapVariabili variabiliCalcolo) {
		if (variabiliCalcolo.get(TipoVariabile.AGRATT).getValNumber() != null && variabiliCalcolo.get(TipoVariabile.AGRATT).getValNumber().equals(new BigDecimal(-1))) {
			return false;
		} else {
			return true;
		}

	}

	private Boolean getAgricoltoreAttivo(MapVariabili variabiliCalcolo) {
		if (variabiliCalcolo.get(TipoVariabile.AGRATT).getValNumber() != null && variabiliCalcolo.get(TipoVariabile.AGRATT).getValNumber().equals(new BigDecimal(-1))) {
			variabiliCalcolo.setVal(TipoVariabile.AGRATT, false);
			return false;
		} else {
			return variabiliCalcolo.get(TipoVariabile.AGRATT).getValBoolean();
		}

	}

	private Boolean getImpegnoTitoli(MapVariabili variabiliCalcolo) {

		if (variabiliCalcolo.get(TipoVariabile.TITONUM).getValNumber().compareTo(BigDecimal.ZERO) == 0) {
			return false;
		} else {
			return true;

		}
	}

	private Boolean getSuperficieMinima(MapVariabili variabiliCalcolo) {

		if (variabiliCalcolo.get(TipoVariabile.BPSSUPIMP).getValNumber().compareTo(BigDecimal.valueOf(0.5000)) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	// output

	/**
	 * IF (TITNUM = 0 OR AGRATT = NO OR BPSSUPIMP <0,5000 ha) THEN 0 ELSE NULL
	 * 
	 * @return
	 */
	private BigDecimal getBpsSupAmm(HashMap<TipoControllo, EsitoControllo> mapEsiti) {

		if (mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() == false || mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito() == false
				|| mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito() == false) {
			return BigDecimal.ZERO;
		} else {
			return null;
		}
	}

	/**
	 * IF (TITNUM = 0 OR AGRATT = NO OR BPSSUPIMP <0,5000 ha) THEN 0 ELSE NULL
	 * 
	 * @return
	 */
	private BigDecimal getBpsImpAmm(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() == false || mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito() == false
				|| mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito() == false) {

			return BigDecimal.ZERO;
		} else {
			return null;
		}
	}

	/**
	 * NVL(BPSSUPAMM;BPSSUPRIC)
	 * 
	 * @return
	 */
	private BigDecimal getGreSupRic(MapVariabili variabiliCalcolo) {

		if (variabiliCalcolo.get(TipoVariabile.BPSSUPAMM).getValNumber() != null) {
			return variabiliCalcolo.get(TipoVariabile.BPSSUPAMM).getValNumber();
		} else {
			return variabiliCalcolo.get(TipoVariabile.BPSSUPRIC).getValNumber();
		}

	}

	/**
	 * GRESUPRIC*TITVALRID*GREPERC
	 * 
	 * @return
	 */
	private BigDecimal getGreImpRic(MapVariabili variabiliCalcolo) {

		return variabiliCalcolo.multiply(TipoVariabile.GRESUPRIC, TipoVariabile.TITVALRID, TipoVariabile.GREPERC);

	}

	/**
	 * IF (TITNUM = 0 OR AGRATT = NO OR BPSSUPIMP <0,5000 ha) THEN 0 ELSE NULL
	 * 
	 * @return
	 */
	private BigDecimal getGreSupAmm(HashMap<TipoControllo, EsitoControllo> mapEsiti) {

		if (mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() == false || mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito() == false
				|| mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito() == false) {

			return BigDecimal.ZERO;
		} else {
			return null;
		}

	}

	/**
	 * IF (TITNUM = 0 OR AGRATT = NO OR BPSSUPIMP <0,5000 ha) THEN 0 ELSE NULL
	 * 
	 * @return
	 */
	private BigDecimal getGreImpAmm(HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() == false || mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito() == false
				|| mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito() == false) {
			return BigDecimal.ZERO;
		} else {
			return null;
		}
	}

	/**
	 * min(BPSSUPRIC;GIOLIMITE)
	 * 
	 * @return
	 */
	private BigDecimal getGioSupRic(MapVariabili variabiliCalcolo) {

		return variabiliCalcolo.min(TipoVariabile.BPSSUPRIC, TipoVariabile.GIOLIMITE);

	}

	/**
	 * GIOSUPRIC*TITVALRID*GIOPERC
	 * 
	 * @return
	 */
	private BigDecimal getGioImpRic(MapVariabili variabiliCalcolo) {

		return variabiliCalcolo.multiply(TipoVariabile.GIOSUPRIC, TipoVariabile.TITVALGIORID, TipoVariabile.GIOPERC);

	}

	/**
	 * IF (TITNUM = 0 OR AGRATT = NO OR BPSSUPIMP <0,5000 ha) THEN 0 ELSE NULL
	 * 
	 * @return
	 */
	private BigDecimal getGioSupAmm(HashMap<TipoControllo, EsitoControllo> mapEsiti) {

		if (mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() == false || mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito() == false
				|| mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito() == false) {

			return BigDecimal.ZERO;
		} else {
			return null;
		}

	}

	/**
	 * IF (TITNUM = 0 OR AGRATT = NO OR BPSSUPIMP <0,5000 ha) THEN 0 ELSE NULL
	 * 
	 * @return
	 */
	private BigDecimal getGioImpAmm(HashMap<TipoControllo, EsitoControllo> mapEsiti) {

		if (mapEsiti.get(TipoControllo.BRIDUSDC011_impegnoTitoli).getEsito() == false || mapEsiti.get(TipoControllo.BRIDUSDC010_agricoltoreAttivo).getEsito() == false
				|| mapEsiti.get(TipoControllo.BRIDUSDC012_superficieMinima).getEsito() == false) {

			return BigDecimal.ZERO;
		} else {
			return null;
		}

	}

	/**
	 * TITVAL * (1-PERCRIDLINTIT)
	 * 
	 * @return
	 */
	private BigDecimal getTitValRid(MapVariabili variabiliCalcolo) {

		return new BigDecimal(1).subtract(variabiliCalcolo.get(TipoVariabile.PERCRIDLINTIT).getValNumber()).multiply(variabiliCalcolo.get(TipoVariabile.TITVAL).getValNumber());
	}

	/**
	 * TITVALGIO * (1-PERCRIDLINTIT)
	 * 
	 * @return
	 */
	private BigDecimal getTitValGioRid(MapVariabili variabiliCalcolo) {

		return new BigDecimal(1).subtract(variabiliCalcolo.get(TipoVariabile.PERCRIDLINTIT).getValNumber()).multiply(variabiliCalcolo.get(TipoVariabile.TITVALGIO).getValNumber());
	}
}

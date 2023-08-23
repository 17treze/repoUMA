package it.tndigitale.a4g.uma.dto.richiesta.builder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.territorio.client.model.CodificaColtura;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ColturaDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ParticellaDto;
import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.ColturaGruppiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.UtilizzoMacchinariModel;
import it.tndigitale.a4g.uma.business.persistence.repository.ColturaGruppiDao;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.TerritorioAualDto;

@Component
public class RichiestaCarburanteDtoBuilder {
	
	@Autowired
	private ColturaGruppiDao colturaGruppiDao;
	
	private RichiestaCarburanteDto richiestaCarburanteDto;
	
	public RichiestaCarburanteDtoBuilder() {
		richiestaCarburanteDto = new RichiestaCarburanteDto();
	}
	
	public RichiestaCarburanteDtoBuilder newDto() {
		richiestaCarburanteDto = new RichiestaCarburanteDto();
		return this;
	}
	
	public RichiestaCarburanteDtoBuilder from(RichiestaCarburanteModel richiestaCarburanteModel) {
		if (richiestaCarburanteModel != null) {
			richiestaCarburanteDto.setCampagna(richiestaCarburanteModel.getCampagna())
					.setCfRichiedente(richiestaCarburanteModel.getCfRichiedente())
					.setCuaa(richiestaCarburanteModel.getCuaa())
					.setDataPresentazione(richiestaCarburanteModel.getDataPresentazione())
					.setProtocollo(richiestaCarburanteModel.getProtocollo())
					.setDataProtocollazione(richiestaCarburanteModel.getDataProtocollazione())
					.setId(richiestaCarburanteModel.getId()).setStato(richiestaCarburanteModel.getStato())
					.setDenominazione(richiestaCarburanteModel.getDenominazione())
					.setNote(richiestaCarburanteModel.getNote());
		}
		return this;
	}
	
	public RichiestaCarburanteDtoBuilder withCarburante(CarburanteCompletoDto carburante) {
		if (carburante != null && carburante.getBenzina() == null && carburante.getGasolio() == null
				&& carburante.getGasolioSerre() == null && carburante.getGasolioTerzi() == null) {
			richiestaCarburanteDto.setCarburanteRichiesto(null);
		}
		else {
			richiestaCarburanteDto.setCarburanteRichiesto(carburante);
		}
		return this;
	}
	
	// verifica se esistono della macchine utilizzate (flag true) per ogni tipo di carburante
	public RichiestaCarburanteDtoBuilder withFlagMacchineDichiarate(List<UtilizzoMacchinariModel> macchineModel) {
		if (!CollectionUtils.isEmpty(macchineModel)) {
			List<UtilizzoMacchinariModel> utilizzate = macchineModel.stream()
					.filter(macchina -> Boolean.TRUE.equals(macchina.getFlagUtilizzo())).collect(Collectors.toList());
			richiestaCarburanteDto.setHaMacchineGasolio(utilizzate.stream()
					.anyMatch(macchina -> TipoCarburante.GASOLIO.equals(macchina.getAlimentazione())));
			richiestaCarburanteDto.setHaMacchineBenzina(utilizzate.stream()
					.anyMatch(macchina -> TipoCarburante.BENZINA.equals(macchina.getAlimentazione())));
		}
		return this;
	}
	
	// verifica se esiste almeno un gruppo di lavorazione per i fabbricati - ad esclusione di quelli di tipo Serre
	public RichiestaCarburanteDtoBuilder withFlagFabbricatiPresenti(List<FabbricatoModel> fabbricati) {
		if (CollectionUtils.isEmpty(fabbricati)) {
			richiestaCarburanteDto.setHaFabbricati(false);
			return this;
		}
		Boolean nonHaFabbricati = fabbricati.stream()
				.filter(fabbricato -> AmbitoLavorazione.FABBRICATI
						.equals(fabbricato.getTipoFabbricato().getGruppoLavorazione().getAmbitoLavorazione()))
				.collect(Collectors.toList()).isEmpty();
		richiestaCarburanteDto.setHaFabbricati(!nonHaFabbricati);
		return this;
	}
	
	// verifica se esiste almeno un gruppo di lavorazione per le serre
	public RichiestaCarburanteDtoBuilder withFlagSerrePresenti(List<FabbricatoModel> fabbricati) {
		if (CollectionUtils.isEmpty(fabbricati)) {
			richiestaCarburanteDto.setHaSerre(false);
			return this;
		}
		Boolean nonHaSerre = fabbricati.stream()
				.filter(fabbricato -> AmbitoLavorazione.SERRE
						.equals(fabbricato.getTipoFabbricato().getGruppoLavorazione().getAmbitoLavorazione()))
				.collect(Collectors.toList()).isEmpty();
		richiestaCarburanteDto.setHaSerre(!nonHaSerre);
		return this;
	}
	
	// verifica se esiste almeno un gruppo di lavorazione per le colture per l'anno campagna della domanda 
	public RichiestaCarburanteDtoBuilder withFlagSuperficiPresenti(List<TerritorioAualDto> particelle,
			RichiestaCarburanteModel domanda) {
		if (CollectionUtils.isEmpty(particelle)) {
			richiestaCarburanteDto.setHaSuperfici(false);
			return this;
		}
//		Optional<CodificaColtura> gruppoColtura = particelle.stream().map(ParticellaDto::getColture)
//				.flatMap(List::stream).map(ColturaDto::getCodifica).filter(c -> {
//					ColturaGruppiModel coltura = colturaGruppiDao.findByCodificaAndAnno(c.getCodiceSuolo(),
//							c.getCodiceDestinazioneUso(), c.getCodiceUso(), c.getCodiceQualita(), c.getCodiceVarieta(),
//							domanda.getCampagna().intValue());
//					return coltura != null ? Boolean.TRUE : Boolean.FALSE;
//				}).findFirst();
		richiestaCarburanteDto.setHaSuperfici(true); //gruppoColtura.isPresent()
		return this;
	}
	
	// verifica se esiste almeno una dichiarazione effettuata per qualsiasi ambito di lavorazione
	public RichiestaCarburanteDtoBuilder withFlagDichiarazioniPresenti(List<FabbisognoModel> fabbisogni) {
		richiestaCarburanteDto.setHaDichiarazioni(!CollectionUtils.isEmpty(fabbisogni));
		return this;
	}
	
	// verifica se esiste almeno una dichiarazione effettuata per qualsiasi ambito di lavorazione
	public RichiestaCarburanteDtoBuilder withIdRettificata(Long idRettificata) {
		richiestaCarburanteDto.setIdRettificata(idRettificata);
		return this;
	}
	
	public RichiestaCarburanteDto build() {
		return richiestaCarburanteDto;
	}
}

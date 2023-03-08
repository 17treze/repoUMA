package it.tndigitale.a4g.uma.business.service.trasferimenti;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloAgsDto;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.TrasferimentoCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.TrasferimentiSpecification;
import it.tndigitale.a4g.uma.business.persistence.repository.TrasferimentoCarburanteDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDtoBuilder;
import it.tndigitale.a4g.uma.dto.richiesta.AziendaDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteTotale;
import it.tndigitale.a4g.uma.dto.trasferimenti.PresentaTrasferimentoDto;
import it.tndigitale.a4g.uma.dto.trasferimenti.TrasferimentiFilter;
import it.tndigitale.a4g.uma.dto.trasferimenti.TrasferimentoDto;

@Service
public class TrasferimentiCarburanteService {

	private static final String RICHIESTA_NOT_FOUND = "Richiesta con id %s non trovata";
	@Autowired
	private TrasferimentoCarburanteDao trasferimentoCarburanteDao;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private UmaAnagraficaClient anagraficaClient;
	@Autowired
	private TrasferimentiCarburanteValidator trasferimentiCarburanteValidator;
	@Autowired
	private Clock clock;


	public CarburanteTotale<TrasferimentoDto> getTrasferimenti(TrasferimentiFilter trasferimentiFilter) {

		List<TrasferimentoCarburanteModel> trasferimenti = trasferimentoCarburanteDao.findAll(TrasferimentiSpecification.getFilter(trasferimentiFilter));

		var response = trasferimenti.stream().map(t -> {
			var trasferimento = new TrasferimentoDto()
					.setId(t.getId())
					.setData(t.getData())
					.setCarburante(new CarburanteDto()
							.setBenzina(t.getBenzina())
							.setGasolio(t.getGasolio())
							.setGasolioSerre(t.getGasolioSerre()));

			if (trasferimentiFilter.getCuaaMittente() != null) {
				// visto che a db viene salvato solo il cuaa destinatario e non l'id della richiesta destinataria -> andare a cercare la denominazione nella richiesta di carburante
				RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findByCuaaAndCampagnaAndStato(t.getCuaaDestinatario(), t.getRichiestaCarburante().getCampagna(), StatoRichiestaCarburante.AUTORIZZATA).orElseThrow(() -> new EntityNotFoundException("Nessuna richiesta autorizzata trovata per il cuaa " + t.getCuaaDestinatario()));
				trasferimento
				.setDestinatario(new AziendaDto()
						.setCuaa(t.getCuaaDestinatario())
						.setDenominazione(richiesta.getDenominazione()));
			}

			if (trasferimentiFilter.getCuaaDestinatario() != null) {
				trasferimento
				.setMittente(new AziendaDto()
						.setCuaa(t.getRichiestaCarburante().getCuaa())
						.setDenominazione(t.getRichiestaCarburante().getDenominazione()));
			}
			return trasferimento;
		}).collect(Collectors.toList());

		CarburanteDto totale = new CarburanteDtoBuilder()
				.from(response.stream().map(TrasferimentoDto::getCarburante).collect(Collectors.toList()))
				.build();

		return new CarburanteTotale<TrasferimentoDto>()
				.setDati(response)
				.setTotale(totale);
	}

	public void cancellaTrasferimento(Long id) {
		trasferimentoCarburanteDao.deleteById(id);
	}

	public TrasferimentoDto getTrasferimento(Long id) {
		TrasferimentoCarburanteModel trasferimento = trasferimentoCarburanteDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Nessun trasferimento di carburante trovato con id ".concat(id.toString())));
		RichiestaCarburanteModel richiesta = trasferimento.getRichiestaCarburante();

		FascicoloAgsDto fascicolo = anagraficaClient.getFascicolo(trasferimento.getCuaaDestinatario());
		String denominazione = fascicolo != null ? fascicolo.getDenominazione() : null;

		return new TrasferimentoDto()
				.setId(trasferimento.getId())
				.setCarburante(new CarburanteDto(trasferimento.getGasolio(), trasferimento.getBenzina(), trasferimento.getGasolioSerre()))
				.setData(trasferimento.getData())
				.setDestinatario(new AziendaDto().setCuaa(trasferimento.getCuaaDestinatario()).setDenominazione(denominazione))
				.setMittente(new AziendaDto().setCuaa(richiesta.getCuaa()).setDenominazione(richiesta.getDenominazione()));

	}

	public AziendaDto valida(PresentaTrasferimentoDto presentaTrasferimentoDto) {
		RichiestaCarburanteModel richiestaMittente = richiestaCarburanteDao.findById(presentaTrasferimentoDto.getIdRichiestaMittente()).orElseThrow(() -> new EntityNotFoundException(String.format(RICHIESTA_NOT_FOUND, presentaTrasferimentoDto.getIdRichiestaMittente())));
		RichiestaCarburanteModel richiestaDestinatario = richiestaCarburanteDao.findById(presentaTrasferimentoDto.getIdRichiestaDestinatario()).orElseThrow(() -> new EntityNotFoundException(String.format(RICHIESTA_NOT_FOUND, presentaTrasferimentoDto.getIdRichiestaDestinatario())));
		trasferimentiCarburanteValidator.valida(richiestaMittente, richiestaDestinatario);
		return new AziendaDto().setCuaa(richiestaDestinatario.getCuaa()).setDenominazione(richiestaDestinatario.getDenominazione());
	}


	public Long dichiara(PresentaTrasferimentoDto presentaTrasferimentoDto) {
		CarburanteDto trasferito = presentaTrasferimentoDto.getCarburanteTrasferito();
		RichiestaCarburanteModel richiestaMittente = richiestaCarburanteDao.findById(presentaTrasferimentoDto.getIdRichiestaMittente()).orElseThrow(() -> new EntityNotFoundException(String.format(RICHIESTA_NOT_FOUND, presentaTrasferimentoDto.getIdRichiestaMittente())));
		RichiestaCarburanteModel richiestaDestinatario = richiestaCarburanteDao.findById(presentaTrasferimentoDto.getIdRichiestaDestinatario()).orElseThrow(() -> new EntityNotFoundException(String.format(RICHIESTA_NOT_FOUND, presentaTrasferimentoDto.getIdRichiestaDestinatario())));

		trasferimentiCarburanteValidator.valida(richiestaMittente, richiestaDestinatario);
		trasferimentiCarburanteValidator.validaTrasferimento(richiestaMittente, trasferito);

		TrasferimentoCarburanteModel trasferimento = new TrasferimentoCarburanteModel()
				.setBenzina(trasferito.getBenzina())
				.setGasolio(trasferito.getGasolio())
				.setGasolioSerre(trasferito.getGasolioSerre())
				.setCuaaDestinatario(richiestaDestinatario.getCuaa())
				.setData(clock.now())
				.setRichiestaCarburante(richiestaMittente);

		return trasferimentoCarburanteDao.save(trasferimento).getId();
	}

	@Transactional
	public void aggiorna(Long id, CarburanteDto carburanteTrasferito) {
		TrasferimentoCarburanteModel trasferimento = trasferimentoCarburanteDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Nessun trasferimento con id ".concat(id.toString()).concat(" trovato")));
		RichiestaCarburanteModel richiestaMittente = trasferimento.getRichiestaCarburante();
		trasferimentiCarburanteValidator.validaTrasferimento(richiestaMittente, carburanteTrasferito);

		trasferimento
		.setBenzina(carburanteTrasferito.getBenzina())
		.setGasolio(carburanteTrasferito.getGasolio())
		.setGasolioSerre(carburanteTrasferito.getGasolioSerre())
		.setCuaaDestinatario(trasferimento.getCuaaDestinatario())
		.setData(clock.now())
		.setRichiestaCarburante(richiestaMittente);

		trasferimentoCarburanteDao.save(trasferimento);
	}

}

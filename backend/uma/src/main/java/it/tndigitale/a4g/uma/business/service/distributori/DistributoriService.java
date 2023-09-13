package it.tndigitale.a4g.uma.business.service.distributori;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.uma.Ruoli;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DistributoreModel;
import it.tndigitale.a4g.uma.business.persistence.entity.PrelievoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.DistributoriDao;
import it.tndigitale.a4g.uma.business.persistence.repository.PrelieviDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.client.UmaUtenteClient;
import it.tndigitale.a4g.uma.dto.distributori.DistributoreDto;
import it.tndigitale.a4g.uma.dto.richiesta.PrelieviFilter;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;
import it.tndigitale.a4g.uma.dto.richiesta.builder.PrelievoBuilder;
// import it.tndigitale.a4g.utente.client.model.Distributore;

@Service
public class DistributoriService {

	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private DistributoriDao distributoriDao;
	@Autowired
	private PrelieviDao prelieviDao;
	@Autowired
	private PrelieviValidator prelieviValidator;
	@Autowired
	private UmaUtenteClient umaUtenteClient;
	@Autowired
	private UtenteComponent utenteComponent;

	public Long postPrelievi(Long identificativoDistributore, PrelievoDto prelievo, Long idRichiesta) {
		RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(idRichiesta).orElseThrow(() -> new EntityNotFoundException("Richiesta con id: ".concat(String.valueOf(idRichiesta)).concat(" non trovata")));
		
		// Verifico che la richiesta sia autorizzata
		prelieviValidator.checkRichiestaCarburanteAutorizzata(richiesta);
		// Verifico che non esista una dichiarazione consumi nello stato protocollata
		prelieviValidator.checkDichiarazioneConsumiProtocollata(richiesta.getId());
		prelieviValidator.validaPrelievo(prelievo, richiesta);

		// Cerco in A4G il distributore indicato
		Optional<DistributoreModel> distributoreA4GOpt = distributoriDao.findByIdentificativo(identificativoDistributore);

//		DistributoreModel distributoreModel;

//		// Se non esiste il distributore indicato in A4G, viene salvato
//		if (!distributoreA4GOpt.isPresent()) {
//			var distributore = umaUtenteClient.getDistributoreById(prelievo.getDistributore().getIdentificativo());
//
//			distributoreModel = distributoriDao.save(new DistributoreModel()
//					.setIdentificativo(distributore.getId())
//					.setComune(distributore.getComune())
//					.setDenominazione(distributore.getDenominazioneAzienda())
//					.setIndirizzo(distributore.getIndirizzo())
//					.setProvincia(distributore.getProvincia())
//					);
//		} else {
//			distributoreModel = distributoreA4GOpt.get();
//		}

		return prelieviDao.save(new PrelievoModel()
				.setDistributore(distributoreA4GOpt.get())
				.setRichiestaCarburante(richiesta)
				.setBenzina(prelievo.getCarburante().getBenzina())
				.setGasolio(prelievo.getCarburante().getGasolio())
				.setGasolioSerre(prelievo.getCarburante().getGasolioSerre())
				.setData(prelievo.getData())
				.setConsegnato(prelievo.getIsConsegnato())
				.setEstremiDocumentoFiscale(prelievo.getEstremiDocumentoFiscale())
				).getId();
	}

	@Transactional
	public Long aggiornaPrelievo(Long idDistributore, PrelievoDto prelievo, Long idPrelievo) {
		var prelievoDaAggiornare = prelieviDao.findById(idPrelievo).orElseThrow(() -> new EntityNotFoundException("Prelievo non trovato!"));
		
		// Verifico che non esista una dichiarazione consumi nello stato protocollata
		prelieviValidator.checkDichiarazioneConsumiProtocollata(prelievoDaAggiornare.getRichiestaCarburante().getId());
		prelieviValidator.validaModificaPrelievo(prelievo, prelievoDaAggiornare, prelievoDaAggiornare.getRichiestaCarburante());

		// Cerco in A4G il distributore indicato
		Optional<DistributoreModel> distributoreA4GOpt = distributoriDao.findByIdentificativo(prelievo.getDistributore().getIdentificativo());

//		DistributoreModel distributoreModel;
//
//		// Se non esiste il distributore indicato in A4G, viene salvato
//		if (!distributoreA4GOpt.isPresent()) {
//			var distributore = umaUtenteClient.getDistributoreById(prelievo.getDistributore().getIdentificativo());
//
//			distributoreModel = distributoriDao.save(new DistributoreModel()
//					.setIdentificativo(distributore.getId())
//					.setComune(distributore.getComune())
//					.setDenominazione(distributore.getDenominazioneAzienda())
//					.setIndirizzo(distributore.getIndirizzo())
//					.setProvincia(distributore.getProvincia())
//					);
//		} else {
//			distributoreModel = distributoreA4GOpt.get();
//		}

		return prelieviDao.save(prelievoDaAggiornare
				.setDistributore(distributoreA4GOpt.get())
				.setBenzina(prelievo.getCarburante().getBenzina())
				.setGasolio(prelievo.getCarburante().getGasolio())
				.setGasolioSerre(prelievo.getCarburante().getGasolioSerre())
				.setData(prelievo.getData())
				.setConsegnato(prelievo.getIsConsegnato())
				.setEstremiDocumentoFiscale(prelievo.getEstremiDocumentoFiscale())
				).getId();
	}

	@Transactional
	public void aggiornaPrelievi(Long idDistributore, List<PrelievoDto> prelievi) {
		prelievi.forEach(prelievo -> {

			PrelievoModel prelievoDaAggiornare = prelieviDao.findById(prelievo.getId()).orElseThrow(() -> new EntityNotFoundException("Prelievo non trovato!"));
			
			// Verifico che non esista una dichiarazione consumi nello stato protocollata
			prelieviValidator.checkDichiarazioneConsumiProtocollata(prelievoDaAggiornare.getRichiestaCarburante().getId());
			prelieviValidator.validaModificaPrelievo(prelievo, prelievoDaAggiornare, prelievoDaAggiornare.getRichiestaCarburante());

			prelieviDao.save(prelievoDaAggiornare
					.setBenzina(prelievo.getCarburante().getBenzina())
					.setGasolio(prelievo.getCarburante().getGasolio())
					.setGasolioSerre(prelievo.getCarburante().getGasolioSerre())
					.setConsegnato(prelievo.getIsConsegnato())
					);
		});
	}

	public List<PrelievoDto> getPrelievi(PrelieviFilter filtro) {
		List<PrelievoModel> prelieviModel = prelieviDao.findByDistributore_id(filtro.getId());

		if (filtro.getDataPrelievo() != null) {
			prelieviModel = prelieviModel.stream()
					.filter(p -> p.getData().toLocalDate().compareTo(filtro.getDataPrelievo().toLocalDate()) == 0)
					.collect(Collectors.toList());
		}
		if (filtro.getIsConsegnato() != null) {
			prelieviModel = prelieviModel.stream()
					.filter(p -> filtro.getIsConsegnato().compareTo(p.getConsegnato()) == 0)
					.collect(Collectors.toList());
		}
		if (filtro.getCampagna() != null) {
			prelieviModel = prelieviModel.stream()
					.filter(p -> filtro.getCampagna().equals(p.getRichiestaCarburante().getCampagna()))
					.collect(Collectors.toList());
		}
		// recupera solamente i prelievi tali che le richieste associate non hanno una dichiarazione consumi in stato protocollata
		prelieviModel = prelieviModel.stream().filter(p -> {
			RichiestaCarburanteModel richiestaCarburanteModel = p.getRichiestaCarburante();
			Optional<DichiarazioneConsumiModel> dichiarazioneOpt = dichiarazioneConsumiDao.findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(richiestaCarburanteModel.getCuaa(), richiestaCarburanteModel.getCampagna());
			return !(dichiarazioneOpt.isPresent() && StatoDichiarazioneConsumi.PROTOCOLLATA.equals(dichiarazioneOpt.get().getStato()));
		}).collect(Collectors.toList());

		return prelieviModel.stream().map(p -> new PrelievoBuilder()
				.newDto()
				.withAzienda(p.getRichiestaCarburante())
				.withPrelievo(p)
				.build())
				.collect(Collectors.toList());
	}


	public void deletePrelievo(Long idPrelievo) {
		Optional<PrelievoModel> prelievoOpt = prelieviDao.findById(idPrelievo);
		if (prelievoOpt.isPresent()) {
			prelieviValidator.checkDichiarazioneConsumiProtocollata(prelievoOpt.get().getRichiestaCarburante().getId());
			prelieviDao.deleteById(idPrelievo);
		}
	}

	public List<DistributoreDto> getDistributori(Long campagna) {

		// se è un istruttore uma, recupera tutti i distributori che hanno versato prelievi durante l'anno di campagna indicato e che non hanno una dichiarazione consumi in stato protocollata
		if (utenteComponent.haRuolo(Ruoli.ISTRUTTORE_UMA)) {
			List<PrelievoModel> prelievi = distributoriDao.findAll()
					.stream()
					.flatMap(x -> x.getPrelievi().stream())
					.filter(p -> campagna.equals(p.getRichiestaCarburante().getCampagna()) && dichiarazioneConsumiNonProtocollata.test(p))
					.collect(Collectors.toList());
			return buildDistributoriDtoFromPrelievi(prelievi);
		}

		// var distributoriUtente = umaUtenteClient.getDistributori();
		// if (CollectionUtils.isEmpty(distributoriUtente)) {return new ArrayList<>();}
		// filtra i distributori che hanno prelievi non consegnati e tali che le aziende non hanno una dichiarazione consumi protocollata
//		List<DistributoreModel> distributoriSalvati = distributoriDao.findByIdentificativoIn(distributoriUtente.stream().map(Distributore::getId).collect(Collectors.toList()));
//
//		List<PrelievoModel> prelievi = distributoriSalvati
//				.stream()
//				.flatMap(x -> x.getPrelievi().stream())
//				.filter(p -> !p.getConsegnato() && campagna.equals(p.getRichiestaCarburante().getCampagna()) && dichiarazioneConsumiNonProtocollata.test(p))
//				.collect(Collectors.toList());
//		return buildDistributoriDtoFromPrelievi(prelievi);
		return null;
	}

	// almeno un prelievo di quel distributore non ha dichiarazione consumi protocollata (equivalente a dire: ha una dichiarazione consumi e questa è in compilazione || non ce l'ha proprio)
	private Predicate<PrelievoModel> dichiarazioneConsumiNonProtocollata = prelievo -> { 		
		RichiestaCarburanteModel richiestaCarburanteModel = prelievo.getRichiestaCarburante();
		Optional<DichiarazioneConsumiModel> dichiarazioneOpt = dichiarazioneConsumiDao.findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(richiestaCarburanteModel.getCuaa(), richiestaCarburanteModel.getCampagna());
		return dichiarazioneOpt.isPresent() ? StatoDichiarazioneConsumi.IN_COMPILAZIONE.equals(dichiarazioneOpt.get().getStato()) : Boolean.TRUE;
	};

	private List<DistributoreDto> buildDistributoriDtoFromPrelievi(List<PrelievoModel> prelievi) {
		return prelievi.stream()
				.collect(Collectors.groupingBy(PrelievoModel::getDistributore))
				.entrySet()
				.stream()
				.map(Map.Entry::getKey)
				.map(d -> new DistributoreDto()
						.setComune(d.getComune())
						.setDenominazione(d.getDenominazione())
						.setId(d.getId())
						.setIdentificativo(d.getIdentificativo())
						.setIndirizzo(d.getIndirizzo())
						.setProvincia(d.getProvincia()))
				.collect(Collectors.toList());
	}
}
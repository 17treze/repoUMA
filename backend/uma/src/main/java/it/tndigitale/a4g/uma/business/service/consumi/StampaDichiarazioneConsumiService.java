package it.tndigitale.a4g.uma.business.service.consumi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsuntivoConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburanteConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteHelper;
import it.tndigitale.a4g.uma.business.service.richiesta.RichiestaCarburanteService;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.StampaDichiarazioneConsumiDto;
import it.tndigitale.a4g.uma.dto.consumi.StampaPrelievoDto;
import it.tndigitale.a4g.uma.dto.consumi.builder.CarburanteCompletoDtoBuilder;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteTotale;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;

@Service
public class StampaDichiarazioneConsumiService {
	
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private RichiestaCarburanteService prelieviService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private Clock clock;
	@Autowired
	private CarburanteHelper carburanteHelper;
	
	@Value("${pathDownload}")
	private String pathDownload;
	
	private final String dummyPdf = "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uDO+xD52P7VICHYgIvawAw3VLER8ujn4ddb4pHUzO89WvTKUz3lZFs0/gpjYSvdAAaFk6p2NjsYDVETqH83MCMKC0MV505rvlBblU+GxHtpLm9x5fysa3xIEg0h0SVOPadh4zDSIOGTH9pF9Lk51KVMaPmYWDRwzK5viqRjbNI/8x6TCbny9m8ADnW+rJtvl6K6txdlru8SPbAQPZKEqtHc3eDQNz3Z7ZB9MGzvN0e0eDyTLkkuWereQI/8BL3eiPOa8Va2dhMj5SbCdd4u7G1b/ObD2tfsy8SXZDaw/Uf5qiOZVeTyV/MDJe1HBgyLyLnbqI8/AwPE5YyL5H4SRtNv1c38YOyMzlEY0nAYfLv8pgdmpaZep9/fu3doe59A6+nYzcnZD9QTzvH2Yz9Bmdvv4MJ/7Ds51Q+RPVxBTOYtxHpvE/oycA/gv+GEZqQplbmRzdHJlYW0KZW5kb2JqCjUgMCBvYmoKPDwvVHlwZS9QYWdlL01lZGlhQm94WzAgMCA1OTUgODQyXS9SZXNvdXJjZXM8PC9Gb250PDwvRjEgMSAwIFIvRjIgMiAwIFI+Pj4+L0NvbnRlbnRzIDMgMCBSL1BhcmVudCA0IDAgUj4+CmVuZG9iagoxIDAgb2JqCjw8L1R5cGUvRm9udC9TdWJ0eXBlL1R5cGUxL0Jhc2VGb250L0hlbHZldGljYS1Cb2xkL0VuY29kaW5nL1dpbkFuc2lFbmNvZGluZz4+CmVuZG9iagoyIDAgb2JqCjw8L1R5cGUvRm9udC9TdWJ0eXBlL1R5cGUxL0Jhc2VGb250L0hlbHZldGljYS9FbmNvZGluZy9XaW5BbnNpRW5jb2Rpbmc+PgplbmRvYmoKNCAwIG9iago8PC9UeXBlL1BhZ2VzL0NvdW50IDEvS2lkc1s1IDAgUl0+PgplbmRvYmoKNiAwIG9iago8PC9UeXBlL0NhdGFsb2cvUGFnZXMgNCAwIFI+PgplbmRvYmoKNyAwIG9iago8PC9Qcm9kdWNlcihpVGV4dFNoYXJwkiA1LjUuMTMuMiCpMjAwMC0yMDIwIGlUZXh0IEdyb3VwIE5WIFwoQUdQTC12ZXJzaW9uXCkpL0NyZWF0aW9uRGF0ZShEOjIwMjIwOTIzMDg0NzQzKzAyJzAwJykvTW9kRGF0ZShEOjIwMjIwOTIzMDg0NzQzKzAyJzAwJyk+PgplbmRvYmoKeHJlZgowIDgKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAyMjczIDAwMDAwIG4gCjAwMDAwMDIzNjYgMDAwMDAgbiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAyNDU0IDAwMDAwIG4gCjAwMDAwMDIxNTIgMDAwMDAgbiAKMDAwMDAwMjUwNSAwMDAwMCBuIAowMDAwMDAyNTUwIDAwMDAwIG4gCnRyYWlsZXIKPDwvU2l6ZSA4L1Jvb3QgNiAwIFIvSW5mbyA3IDAgUi9JRCBbPGVjMDhjZjdhNzJjYmMyOWY0MDZhNTllZTc0NDAwY2U1PjxlYzA4Y2Y3YTcyY2JjMjlmNDA2YTU5ZWU3NDQwMGNlNT5dPj4KJWlUZXh0LTUuNS4xMy4yCnN0YXJ0eHJlZgoyNzE1CiUlRU9GCg==";
	
	private static final String TEMPLATE_PATH = "resources/templateDichiarazioneConsumi.docx";
	
	private static final String SUB_DIRECTORY_RICHIESTE = "/richieste-carburante";
	
	public Resource stampaDichiarazioneConsumi(Long idDichiarazione) throws IOException {
		Optional<DichiarazioneConsumiModel> dichiarazioneConsumiOpt = dichiarazioneConsumiDao.findById(idDichiarazione);
		
		if (dichiarazioneConsumiOpt.isPresent()) {
			DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiOpt.get();
			// Se la dichiarazione consumi è protocollata , viene scaricato il documento caricato in fase di protocollazione
			if (dichiarazioneConsumi.getStato().equals(StatoDichiarazioneConsumi.PROTOCOLLATA)) {
				// return new ByteArrayResource(dichiarazioneConsumi.getDocumento());
				Path fileRichiesta = Paths.get(this.pathDownload + SUB_DIRECTORY_RICHIESTE + "/"
						+ dichiarazioneConsumi.getDataProtocollazione().getYear() + "/"
						+ dichiarazioneConsumi.getNomeFile());
				return new ByteArrayResource(Files.readAllBytes(fileRichiesta));
			}
			//
			//		var json = objectMapper.writeValueAsString(buildStampaDto(dichiarazioneConsumi));
			//		return new ByteArrayResource(proxyClient.stampa(json, TEMPLATE_PATH));
			return new ByteArrayResource(Base64.getDecoder().decode(dummyPdf.getBytes()));
		}
		else {
			throw new EntityNotFoundException("Dichiarazione consumi non trovata con id = " + idDichiarazione);
		}
	}
	
	private StampaDichiarazioneConsumiDto buildStampaDto(DichiarazioneConsumiModel dichiarazioneConsumi) {
		var stampaDto = new StampaDichiarazioneConsumiDto();
		
		List<ConsuntivoConsumiModel> consuntivi = dichiarazioneConsumi.getConsuntivi();
		RichiestaCarburanteModel richiesta = dichiarazioneConsumi.getRichiestaCarburante();
		
		//Residuo
		var residuo = carburanteHelper.getResiduoAnnoPrecedente(richiesta.getCuaa(), richiesta.getCampagna());
		
		//Ammissibile
		CarburanteCompletoDto ammissibileCompleto = carburanteHelper.getAmmissibileCompleto(dichiarazioneConsumi);
		
		//Richiesto
		var carburanteRichiesto = new CarburanteCompletoDto().setBenzina(richiesta.getBenzina())
				.setGasolio(richiesta.getGasolio()).setGasolioSerre(richiesta.getGasolioSerre())
				.setGasolioTerzi(richiesta.getGasolioTerzi());
		
		String cuaa = richiesta.getCuaa();
		Long campagna = richiesta.getCampagna();
		
		// Prelievi
		CarburanteTotale<PrelievoDto> prelievi = prelieviService.getPrelievi(cuaa, campagna, null);
		List<StampaPrelievoDto> stampaPrelievi = new ArrayList<>();
		if (!CollectionUtils.isEmpty(prelievi.getDati())) {
			prelievi.getDati()
					.forEach(prelievo -> stampaPrelievi.add(new StampaPrelievoDto()
							.setDistributore(prelievo.getDistributore()).setCarburante(prelievo.getCarburante())
							.setData(prelievo.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
		}
		
		//Ricevuto
		CarburanteDto ricevutiTotale = carburanteHelper.getTotaleCarburanteRicevuto(cuaa, campagna);
		
		//Trasferito
		CarburanteDto trasferitiTotale = carburanteHelper.getTotaleCarburanteTrasferito(cuaa, campagna);
		
		//Disponibile
		CarburanteDto disponibile = carburanteHelper.calcolaDisponibile(richiesta);
		
		//Accisa
		CarburanteDto accisa = carburanteHelper.calcolaAccisa(dichiarazioneConsumi);
		
		//Motivazioni Consuntivi
		consuntivi.stream().filter(c -> c.getMotivazione() != null)
				.collect(Collectors.groupingBy(ConsuntivoConsumiModel::getTipoConsuntivo, Collectors
						.groupingBy(ConsuntivoConsumiModel::getTipoCarburante, CustomCollectors.toSingleton())))
				.entrySet().stream().forEach(entrySet -> {
					var value = entrySet.getValue();
					if (TipoConsuntivo.AMMISSIBILE.equals(entrySet.getKey())) {
						stampaDto.setMotivazioneAmmissibile(value.get(TipoCarburanteConsuntivo.GASOLIO_TERZI) != null
								? value.get(TipoCarburanteConsuntivo.GASOLIO_TERZI).getMotivazione().name()
								: null);
					}
					
					if (TipoConsuntivo.RECUPERO.equals(entrySet.getKey())) {
						stampaDto
								.setMotivazioneRecuperoBenzina(value.get(TipoCarburanteConsuntivo.BENZINA) != null
										? value.get(TipoCarburanteConsuntivo.BENZINA).getMotivazione().name()
										: null)
								.setMotivazioneRecuperoGasolio(value.get(TipoCarburanteConsuntivo.GASOLIO) != null
										? value.get(TipoCarburanteConsuntivo.GASOLIO).getMotivazione().name()
										: null)
								.setMotivazioneRecuperoGasolioSerre(
										value.get(TipoCarburanteConsuntivo.GASOLIO_SERRE) != null ? value
												.get(TipoCarburanteConsuntivo.GASOLIO_SERRE).getMotivazione().name()
												: null)
								.setMotivazioneRecuperoGasolioTerzi(
										value.get(TipoCarburanteConsuntivo.GASOLIO_TERZI) != null ? value
												.get(TipoCarburanteConsuntivo.GASOLIO_TERZI).getMotivazione().name()
												: null);
					}
				});
		var builder = new CarburanteCompletoDtoBuilder();
		
		return stampaDto.setIdDichiarazioneConsumi(dichiarazioneConsumi.getId())
				.setAnnoCampagna(richiesta.getCampagna()).setCuaa(richiesta.getCuaa())
				.setDenominazione(richiesta.getDenominazione().equals("NO_DENOM") ? null : richiesta.getDenominazione())
				.setPrelievi(stampaPrelievi).setAnnoCampagnaRimanenza(richiesta.getCampagna() - 1).setResiduo(residuo)
				.setRichiesto(carburanteRichiesto).setRicevuto(ricevutiTotale).setTrasferito(trasferitiTotale)
				.setPrelevato(prelievi.getTotale()).setDisponibile(disponibile).setAccisa(accisa)
				.setMotivazioneAccisa(dichiarazioneConsumi.getMotivazioneAccisa()).setAmmissibile(ammissibileCompleto)
				.setConsumato(builder.newDto().from(consuntivi, TipoConsuntivo.CONSUMATO).build())
				.setRimanenza(builder.newDto().from(consuntivi, TipoConsuntivo.RIMANENZA).build())
				.setRecupero(builder.newDto().from(consuntivi, TipoConsuntivo.RECUPERO).build())
				.setDataStampa(clock.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}
}

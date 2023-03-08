package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoMacellatoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.repository.dao.EtichetttaturaCarneDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtEtichettaturaCarne;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.util.ControlloCapiUtil;

@Component
public class ControlloCapoMacellato implements BiFunction<RichiestaAllevamDu,CapoDto,EsitoCalcoloCapoModel> {

	private static final Logger logger = LoggerFactory.getLogger(ControlloCapoMacellato.class);

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private EtichetttaturaCarneDao etichetttaturaCarneDao;
	@Autowired
	private ControlliCapoMacellatoFactory controlliCapoMacellatoFactory;

	@Override
	public EsitoCalcoloCapoModel apply(RichiestaAllevamDu richiestaAllevamDu, CapoDto capoDto) {
		EsitoCalcoloCapoModel esitoCalcoloCapo = 
				Stream.of(
						checkInterventoDaScartare,
						checkPresenzaInformazioni,
						checkSistemaEtichettatura,
						checkPeriodoDetenzione
						)
				.map(f -> f.apply((CapoMacellatoDto)capoDto, richiestaAllevamDu))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Errore generico calcolo capo Macellato!"));
		esitoCalcoloCapo.setCapoId(Long.parseLong(capoDto.getIdCapo()));
		esitoCalcoloCapo.setCodiceCapo(capoDto.getMarcaAuricolare());		
		return esitoCalcoloCapo;	
	}


	//IDU-ACZ-00 Ammissibilità interventi 314, 317 e 319
	protected BiFunction<CapoMacellatoDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>>  checkInterventoDaScartare = (capoMacellato, richiestaAllevamDu) -> {
		if (Arrays.asList("317","319").contains(richiestaAllevamDu.getCodiceIntervento()))
			return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,"Capo appartenente a intervento non ammissibile (317/319)"));
		return Optional.empty();
	};

	//BR8 Presenza delle informazioni
	protected BiFunction<CapoMacellatoDto,RichiestaAllevamDu,Optional<EsitoCalcoloCapoModel>>  checkPresenzaInformazioni = (capoMacellato, richiestaAllevamDu) -> {
		Optional<EsitoCalcoloCapoModel> responseKO = Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE, "Il capo non è ammissibile all’aiuto perchè le informazioni presenti nella Banca Dati Nazionale (BDN) "
				+ "informatizzata dell'Anagrafe Zootecnica sono insufficienti o incongruenti o contraddittorie "
				+ "e non consentano di eseguire i controlli di ammissibilità"));
		if (isNull(capoMacellato.getMarcaAuricolare()) || isNull(capoMacellato.getDataNascita())) {
			return responseKO;
		}

		List<DetenzioneDto> detenzioneList = capoMacellato.getDetenzioni();
		for(DetenzioneDto detenzioneDto : detenzioneList) {
			if (isNull(detenzioneDto.getVaccaDtInserimentoBdnIngresso()) || isNull(detenzioneDto.getVaccaDtIngresso())
					|| isNull(detenzioneDto.getDtUscita()) || isNull(detenzioneDto.getVaccaDtComAutoritaIngresso()) 
					|| isNull(detenzioneDto.getVaccaDtInserimentoBdnUscita()) || isNull(detenzioneDto.getDtflagDelegatoIngresso()) || isNull(detenzioneDto.getAziendaCodice())) {
				return responseKO;
			}
		}
		return Optional.empty();
	};


	// BR3 Adesione ai sistemi di etichettatura facoltativa degli allevamenti per cui viene richiesto l’intervento 318
	protected BiFunction<CapoMacellatoDto, RichiestaAllevamDu, Optional<EsitoCalcoloCapoModel>> checkSistemaEtichettatura = (capoMacellato, richiestaAllevamDu) -> {
		if ("318".equals(richiestaAllevamDu.getCodiceIntervento())) {
			// Tutti gli allevamenti per i quali è stato richiesto l’intervento 318 e che non sono presenti
			// nell’archivio dei sistemi di etichettatura, non sono ammessi al sostegno.
			final String codiceAllevamento;
			InformazioniAllevamento informazioniAllevamento=new InformazioniAllevamento();
			try {
				informazioniAllevamento = objectMapper.readValue(richiestaAllevamDu.getDatiAllevamento(), InformazioniAllevamento.class);
			} catch (IOException e) {
				logger.error("Errore conversione json InformazioniAllevamento", e);
			} finally {
				codiceAllevamento = informazioniAllevamento.getCodiceAllevamentoBdn();
			}

			String cuaa = richiestaAllevamDu.getCuaaIntestatario();
			A4gtEtichettaturaCarne a4gtPagaZootEtichet = new A4gtEtichettaturaCarne();
			a4gtPagaZootEtichet.setCodiceAsl(informazioniAllevamento.getCodiceAllevamento());
			a4gtPagaZootEtichet.setCuaa(cuaa);
			Optional<A4gtEtichettaturaCarne> pagaZootEtichettOptional = etichetttaturaCarneDao.findOne(Example.of(a4gtPagaZootEtichet));
			//			Tutti i capi macellati in allevamenti per i quali è stato richiesto l’intervento 318 e che non sono presenti nell’archivio dei 
			//			sistemi di etichettatura, non sono ammessi al sostegno.
			//			--> pensare eventuale refactor e mettere nel flusso principale
			if (!pagaZootEtichettOptional.isPresent()) { 
				boolean esisteCapoPerAzienda = 
						capoMacellato.getDetenzioni()
						.stream()
						.anyMatch(detenzione -> 
						codiceAllevamento.equals(detenzione.getAllevId())
								);
				if (esisteCapoPerAzienda) {
					// non aderisce a sistema qualità
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE, MessageFormat
							.format(" il capo non è ammissibile perché l’allevamento {0} non aderisce " 
									+ "ad alcun sistema di etichettatura facoltativo riconosciuto da APPAG. ", codiceAllevamento)));
				} else {
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.DA_SCARTARE, ""));
				}
			} else {
				long ggAdesione=0;
				//BR5 Detenzione di 6 mesi del capo nel periodo di adesione ai sistemi di etichettatura facoltativa degli allevamenti per cui viene richiesto l’intervento 318
				if (capoMacellato.getDetenzioni().size()==1) {
					DetenzioneDto clsCapoMacellato=capoMacellato.getDetenzioni().get(0);
					if (!codiceAllevamento.equals(clsCapoMacellato.getAllevId())) {
						return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.DA_SCARTARE, ""));
					}
					ggAdesione=calcolaGiorni(clsCapoMacellato,pagaZootEtichettOptional.get());
				} else {
					List<DetenzioneDto> clsCapiMacellatoFiltered = ControlloCapiUtil.ordinaEfiltraCapi(capoMacellato.getDetenzioni());
					if (clsCapiMacellatoFiltered.size()==1) {
						DetenzioneDto clsCapoMacellato=clsCapiMacellatoFiltered.get(0);
						if (!codiceAllevamento.equals(clsCapoMacellato.getAllevId())) {
							return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.DA_SCARTARE, ""));
						}
						ggAdesione=calcolaGiorni(clsCapoMacellato,pagaZootEtichettOptional.get());
					}else {
						//clsCapiMacellatoFiltered.size()>1
						DetenzioneDto clsCapoMacellato=clsCapiMacellatoFiltered.get(0);
						a4gtPagaZootEtichet.setCodiceAsl(clsCapoMacellato.getAziendaCodice());
						a4gtPagaZootEtichet.setCuaa(cuaa);
						pagaZootEtichettOptional = etichetttaturaCarneDao.findOne(Example.of(a4gtPagaZootEtichet));
						if (pagaZootEtichettOptional.isPresent()) {
							ggAdesione=calcolaGiorni(clsCapoMacellato,pagaZootEtichettOptional.get());
						}
						for (int i = 1; i < clsCapiMacellatoFiltered.size(); i++) {
							a4gtPagaZootEtichet.setCodiceAsl(clsCapiMacellatoFiltered.get(i).getAziendaCodice());
							a4gtPagaZootEtichet.setCuaa(cuaa);
							pagaZootEtichettOptional = etichetttaturaCarneDao.findOne(Example.of(a4gtPagaZootEtichet));
							if (pagaZootEtichettOptional.isPresent()) {
								A4gtEtichettaturaCarne a4gtEtichetttaturaCarne = pagaZootEtichettOptional.get();

								if (a4gtEtichetttaturaCarne.getDtFineValidita() == null) {
									// controllo continuita etichiettatura non necessario se la fine è null(fine=null=infinito)
									ggAdesione += calcolaGiorni(clsCapiMacellatoFiltered.get(i), pagaZootEtichettOptional.get());
								} else {
									//controllo continuita etichiettatura
									long daysBetween=TimeUnit.DAYS.convert(Math.abs(a4gtEtichetttaturaCarne.getDtFineValidita().getTime()- LocalDateConverter.to(clsCapiMacellatoFiltered.get(i-1).getDtInizioDetenzione()).getTime()), TimeUnit.MILLISECONDS);
									if (daysBetween>1) {
										//c'è un buco
										break; //non ci possono essere buchi nemmeno nel sistema di etichettatura
									}
								}
							} else {
								break; //non ci possono essere buchi nemmeno nel sistema di etichettatura che in questo caso non è presente
							}
						}
					}
				}

				if (ggAdesione>=180) {
					// ok controllo Adesione etichettatura e prosegui chain controlli
					return Optional.empty();
				} else {
					return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.NON_AMMISSIBILE,
							MessageFormat.format("Il capo non è ammissibile perché il calcolo della verifica dell’adesione al sistema di etichettatura non copre i sei mesi: {0} gg", String.valueOf(ggAdesione))));
				}
			}
		}
		return Optional.empty();
	};

	// BR4 Verifica del periodo di detenzione del bovino da macellazione in stalla
	protected BiFunction<CapoMacellatoDto, RichiestaAllevamDu, Optional<EsitoCalcoloCapoModel>> checkPeriodoDetenzione = (capoMacellato, richiestaAllevamDu) -> {

		final String codiceAllevamento;
		InformazioniAllevamento informazioniAllevamento=new InformazioniAllevamento();
		try {
			informazioniAllevamento = objectMapper.readValue(richiestaAllevamDu.getDatiAllevamento(), InformazioniAllevamento.class);
		} catch (IOException e) {
			logger.error("Errore conversione json InformazioniAllevamento", e);
		} finally {
			codiceAllevamento = informazioniAllevamento.getCodiceAllevamentoBdn();
		}

		// se l'allevId della risposta Bdn non coincide con l'id della richiesta allevamento allore è da scartare
		if (isDaScartare(capoMacellato.getDetenzioni(),codiceAllevamento, capoMacellato.getDataMacellazione())) {
			return Optional.of(new EsitoCalcoloCapoModel(EsitoCalcoloCapo.DA_SCARTARE, ""));
		}

		// considera le detenzioni ordinate in maniera crescente
		List<DetenzioneDto> detenzioniOrdinate = capoMacellato.getDetenzioni().stream()
				.sorted(Comparator.comparing(DetenzioneDto::getDtInizioDetenzione))
				.collect(Collectors.toList());
		
		DetenzioneDto primaDetenzioneAnnoCampagnaOpt = detenzioniOrdinate.stream()
				.findFirst().orElseThrow(() -> new RuntimeException("Nessuna detenzione presente per il capo macellato") );
		
		return controlliCapoMacellatoFactory.from(primaDetenzioneAnnoCampagnaOpt.getVaccaDtIngresso()).checkDetenzioni(capoMacellato, richiestaAllevamDu.getCodiceIntervento());
	};
	
	private long calcolaGiorni(DetenzioneDto clsCapoMacellato,A4gtEtichettaturaCarne etCarne) {
		long dtInserimentoBdnIngresso = LocalDateConverter.to(clsCapoMacellato.getVaccaDtInserimentoBdnIngresso()).getTime();//era dtInizioDetenzione
		long dtUscita = LocalDateConverter.to(clsCapoMacellato.getDtUscita()).getTime();//era dtFineDetenzione
		long dtFineValidita=etCarne.getDtFineValidita() == null ? 0 : etCarne.getDtFineValidita().getTime();
		long dtInizioValidita = etCarne.getDtInizioValidita().getTime();
		//spesso la getDtFineValidita è vuota. In questo caso allora si prende dtFineDetenzione altrimenti la minore tra dtFineDetenzione e DtFineValidita
		long dtFine = dtFineValidita== 0 ? dtUscita : Math.min(dtUscita,dtFineValidita);
		//per data inizio è l'inverso. Si prende la massima tra dtInizioDetenzione e dtInizioValidita
		return TimeUnit.DAYS.convert(dtFine-Math.max(dtInserimentoBdnIngresso, dtInizioValidita), TimeUnit.MILLISECONDS);
	}	

	/**
	 * Check se le detenzioni sono tali da dover scartare il capo: 
	 * 1. la data di macellazione deve essere in una detenzione del capo
	 * 2. l'allevamento deve essere quello impegnato
	 * 
	 * 
	 * @param detenzioni
	 * @param codiceAllevamento
	 * @param dtMacellazione
	 * @return Optional<DetenzioneDto>
	 */
	private boolean isDaScartare(List<DetenzioneDto> detenzioni, String codiceAllevamento, LocalDate dtMacellazione) {
		Optional<DetenzioneDto> detenzioneDellaMacellazione = trovaDetenzioneDellaMacellazione(detenzioni, dtMacellazione);
		return !(detenzioneDellaMacellazione.isPresent() && codiceAllevamento.equals(detenzioneDellaMacellazione.get().getAllevId()));
	}

	/**
	 * Check se le detenzioni sono tali da dover scartare il capo: 
	 * 1. la data di macellazione deve essere in una detenzione del capo
	 * 2. l'allevamento deve essere quello impegnato
	 * 
	 * 
	 * @param detenzioni
	 * @param codiceAllevamento
	 * @param dtMacellazione
	 * @return Optional<DetenzioneDto>
	 */
	public Optional<DetenzioneDto> trovaDetenzioneDellaMacellazione(List<DetenzioneDto> detenzioni, LocalDate dtMacellazione) {
		return detenzioni.stream()
				.filter(detenzione -> 
				dtMacellazione.compareTo(detenzione.getDtInizioDetenzione()) >= 0 &&
				dtMacellazione.compareTo(detenzione.getDtFineDetenzione()) <= 0)
				.sorted((d1,d2) -> d1.getDtInizioDetenzione().compareTo(d2.getDtInizioDetenzione()))
				.findFirst();

	}

}


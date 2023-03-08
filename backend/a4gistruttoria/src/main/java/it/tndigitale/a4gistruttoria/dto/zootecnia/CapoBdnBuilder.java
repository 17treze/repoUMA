package it.tndigitale.a4gistruttoria.dto.zootecnia;

import static it.tndigitale.a4gistruttoria.util.ControlloCapiUtil.getPrimoParto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4gistruttoria.dto.FamigliaInterventi;
import it.tndigitale.a4gistruttoria.repository.dao.AllevMontagnaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtAllevMontagna;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloSostegnoOvicaprinoConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloSostegnoVaccheLatteMontagnaConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloSostegnoVaccheMacelloConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.ControlloCapoMacellato;
import it.tndigitale.a4gistruttoria.util.ControlloCapiUtil;

@Component
public class CapoBdnBuilder {

	@Value("${zootecnia.interventi.vacchenutricielatte}")
	private String[] interventiCodiciVaccheLatte;
	@Value("${zootecnia.interventi.vacchemacello}")
	private String[] interventiVacchemacello;
	@Value("${zootecnia.interventi.ovicaprini}")
	private String[] interventiOvicaprini;
	@Value("${zootecnia.interventi.vacchemontagna}")
	private String[] interventiCodiciVaccheLatteMontagna;
	@Value("${zootecnia.interventi.vacchemontagna.province}")
	private String[] provinceConsentite;
	@Autowired
	private AllevMontagnaDao alevMotagnaDao;

	@Autowired
	private CalcoloSostegnoVaccheMacelloConsumer calcoloSostegnoVaccheMacelloConsumer;
	@Autowired
	private CalcoloSostegnoOvicaprinoConsumer calcoloSostegnoOvicaprinoConsumer;
	@Autowired
	private CalcoloSostegnoVaccheLatteMontagnaConsumer calcoloSostegnoVaccheLatteMontagnaConsumer;

	@Autowired
	private ControlloCapoMacellato controlloCapoMacellato;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public String getDataCsv(List<JsonNode> clsCapiVacca, String codiceIntervento) {
		FamigliaInterventi famigliaInterventi=getFamigliaInterventi(codiceIntervento);
		CapoBdn capoBdn = buildCapoBdn(clsCapiVacca, codiceIntervento);
		if (famigliaInterventi!=null) {
			switch (famigliaInterventi) {
			case OVICAPRINI:
				capoBdn.setDataCsv(dateFormat.format(capoBdn.getDtInizioDetenzione()));
				break;
			case VACCHE_LATTE:
				estrapolaVitelli(clsCapiVacca, capoBdn);
				Optional<VitelloBdn> primopartoOpt=getPrimoParto(capoBdn);
				if (primopartoOpt.isPresent()) {
					VitelloBdn primoParto=primopartoOpt.get();
					capoBdn.setDataCsv(dateFormat.format(primoParto.getDtNascita()));
				}
				break;
			case VACCHE_MACELLO:
				capoBdn.setDataCsv("");
				break;
			}
		}
		return capoBdn.getDataCsv();
	}

	@Deprecated
	public CapoBdn build(List<JsonNode> clsCapiVacca, String codiceIntervento) {
		FamigliaInterventi famigliaInterventi=getFamigliaInterventi(codiceIntervento);
		CapoBdn capoBdn = buildCapoBdn(clsCapiVacca, codiceIntervento);
		if (famigliaInterventi!=null) {
			switch (famigliaInterventi) {
			case OVICAPRINI:
				capoBdn.setDataCsv(dateFormat.format(capoBdn.getDtInizioDetenzione()));
				CapoOvicaprinoBdn capoOvicaprino = new CapoOvicaprinoBdn();
				
				capoOvicaprino.setDtApplMarchio(clsCapiVacca.get(0).path("dtApplMarchio").isMissingNode() ? null : new Date(clsCapiVacca.get(0).path("dtApplMarchio").longValue()));
				capoOvicaprino.setDtInserimentoBdnNascita(clsCapiVacca.get(0).path("dtInserimentoBdnNascita").isMissingNode() ? null : new Date(clsCapiVacca.get(0).path("dtInserimentoBdnNascita").longValue()));
				List<DetenzioneBdn> detenzioniBdn1=new ArrayList<>();
				detenzioniBdn1.addAll(
						clsCapiVacca.stream().map(clsCapoVacca -> {
							DetenzioneBdn detenzioneBdn=new DetenzioneBdn();
							detenzioneBdn.setDtInizioDetenzione(clsCapoVacca.path("dtInizioDetenzione").isMissingNode() ? null : new Date(clsCapoVacca.path("dtInizioDetenzione").longValue()));
							detenzioneBdn.setDtFineDetenzione(clsCapoVacca.path("dtFineDetenzione").isMissingNode() ? null : new Date(clsCapoVacca.path("dtFineDetenzione").longValue()));
							detenzioneBdn.setVaccaDtIngresso(clsCapoVacca.path("dtIngresso").isMissingNode() ? null : new Date(clsCapoVacca.path("dtIngresso").longValue()));
							return detenzioneBdn;
						}).collect(Collectors.toList())
						);			
				capoOvicaprino.setDetenzioniBdn(detenzioniBdn1);
				capoBdn.setCapoOvicaprinoBdn(capoOvicaprino);
				break;
			case VACCHE_LATTE:
				CapoLatteBdn capoLatteBdn = estrapolaVitelli(clsCapiVacca, capoBdn);
				Optional<VitelloBdn> primopartoOpt=getPrimoParto(capoBdn);
				if (primopartoOpt.isPresent()) {
					VitelloBdn primoParto=primopartoOpt.get();
					capoBdn.setDataCsv(dateFormat.format(primoParto.getDtNascita()));
					Predicate<JsonNode> isStessoVitello = clsCapoVacca ->
					clsCapoVacca.path("codiceVitello").textValue().equals(primoParto.getCodVitello());
					List<DetenzioneBdn> detenzioniBdn=new ArrayList<>();
					//filtro per vitello in modo da evitare le duplicazione delle movimentazioni
					List<JsonNode> clsCapiVaccaFiltratiPerVitello = clsCapiVacca.stream()
							.filter(isStessoVitello)
							.sorted((JsonNode c1, JsonNode c2) -> Long.compare(c1.path("dtInizioDetenzione").longValue(), c2.path("dtInizioDetenzione").longValue()))
							.collect(Collectors.toList());
					detenzioniBdn.addAll(
							clsCapiVaccaFiltratiPerVitello.stream().map(clsCapoVacca -> {
								DetenzioneBdn detenzioneBdn=new DetenzioneBdn();
								detenzioneBdn.setVaccaDtComAutoritaIngresso(new Date(clsCapoVacca.path("vaccaDtComAutoritaIngresso").longValue()));
								detenzioneBdn.setVaccaDtIngresso(new Date(clsCapoVacca.path("vaccaDtIngresso").longValue()));
								detenzioneBdn.setVaccaDtInserimentoBdnIngresso(new Date(clsCapoVacca.path("vaccaDtInserimentoBdnIngresso").longValue()));
								detenzioneBdn.setDtInizioDetenzione(new Date(clsCapoVacca.path("dtInizioDetenzione").longValue()));
								detenzioneBdn.setDtFineDetenzione(new Date(clsCapoVacca.path("dtFineDetenzione").longValue()));
								detenzioneBdn.setCuaa(clsCapoVacca.path("cuaa").textValue());
								detenzioneBdn.setCodiceAsl(clsCapoVacca.path("aziendaCodice").textValue());
								String codiceAllev=clsCapoVacca.path("aziendaCodice").textValue();
								Boolean allevamentoDiMontagna=Boolean.TRUE;
								if (!Arrays.asList(provinceConsentite).contains(codiceAllev.substring(3, 5))) {
									A4gtAllevMontagna a4gtAlevMotagna=new A4gtAllevMontagna();
									a4gtAlevMotagna.setCodiceAllevamento(codiceAllev);
									Optional<A4gtAllevMontagna> allevamentoMotagna = alevMotagnaDao.findOne(Example.of(a4gtAlevMotagna));
									if (!(allevamentoMotagna.isPresent() && allevamentoMotagna.get().getFlagMotagna())) {
										allevamentoDiMontagna=Boolean.FALSE;
									}
								}
								detenzioneBdn.setAllevamentoMontagna(allevamentoDiMontagna);
								return detenzioneBdn;
							}).collect(Collectors.toList())
							);
					capoLatteBdn.setDetenzioniBdn(detenzioniBdn);

				}
				break;
			case VACCHE_MACELLO:
				capoBdn.setDataCsv("");
				List<DetenzioneBdn> detenzioniBdn=new ArrayList<>();
				CapoMacellatoBdn capoMacellatoBdn =new CapoMacellatoBdn();
				capoMacellatoBdn.setDtMacellazione(new Date(clsCapiVacca.get(0).path("dtMacellazione").longValue()));
				capoMacellatoBdn.setDtApplMarchio(clsCapiVacca.get(0).path("dtApplicazioneMarchio").isMissingNode() ? null : new Date(clsCapiVacca.get(0).path("dtApplicazioneMarchio").longValue()));
				detenzioniBdn.addAll(
						clsCapiVacca.stream().map(clsCapoVacca -> {
							DetenzioneBdn detenzioneBdn=new DetenzioneBdn();
							detenzioneBdn.setDtInizioDetenzione(new Date(clsCapoVacca.path("dtInizioDetenzione").longValue()));
							detenzioneBdn.setDtFineDetenzione(new Date(clsCapoVacca.path("dtFineDetenzione").longValue()));
							detenzioneBdn.setCuaa(clsCapoVacca.path("cuaa").textValue());
							detenzioneBdn.setCodiceAsl(clsCapoVacca.path("aziendaCodice").textValue());
							detenzioneBdn.setAllevId(clsCapoVacca.path("allevId").asText());
							detenzioneBdn.setVaccaDtComAutoritaIngresso(new Date(clsCapoVacca.path("dtComAutoritaIngresso").longValue()));
							detenzioneBdn.setVaccaDtIngresso(new Date(clsCapoVacca.path("dtIngresso").longValue()));
							detenzioneBdn.setVaccaDtInserimentoBdnIngresso(new Date(clsCapoVacca.path("dtInserimentoBdnIngresso").longValue()));
							detenzioneBdn.setDtUscita(new Date(clsCapoVacca.path("dtUscita").longValue()));
							detenzioneBdn.setDtflagDelegatoIngresso(new Date(clsCapoVacca.path("flagDelegatoIngresso").longValue()));
							detenzioneBdn.setVaccaDtInserimentoBdnUscita(new Date(clsCapoVacca.path("dtInserimentoBdnUscita").longValue()));
							return detenzioneBdn;
						}).collect(Collectors.toList())
						);				

				capoMacellatoBdn.setDetenzioniBdn(detenzioniBdn);
				capoBdn.setCapoMacellatoBdn(capoMacellatoBdn);
				break;			
			}
		}
		return capoBdn;
	}


	private CapoLatteBdn estrapolaVitelli(List<JsonNode> clsCapiVacca, CapoBdn capoBdn) {

		CapoLatteDto capoDto = calcoloSostegnoVaccheLatteMontagnaConsumer.buildCapoBdnVaccheLatte(clsCapiVacca);
		// deve esistere perchè sono a valle dei controlli
		VitelloDto vitelloDto = ControlloCapiUtil.getPrimoParto(capoDto).get();

		Optional<DetenzioneDto> detenzionePrimoPartoOpt = ControlloCapiUtil.trovaDetenzioneDelPrimoParto(vitelloDto, capoDto.getDetenzioni());
		// deve esistere perchè sono a valle dei controlli
		DetenzioneDto detenzionePrimoParto = detenzionePrimoPartoOpt.get();

		// adatto la costruizione del vecchio oggetto tramite la struttura nuova

		VitelloBdn vitelloBdn= new VitelloBdn();
		vitelloBdn.setCodVitello(vitelloDto.getMarcaAuricolare());
		vitelloBdn.setDtInserimentoBdnNascita(LocalDateConverter.to(vitelloDto.getDtInserimentoBdnNascita()));
		vitelloBdn.setDtNascita(LocalDateConverter.to(vitelloDto.getDataNascita()));
		vitelloBdn.setFlagDelegatoNascitaVitello(vitelloDto.getFlagDelegatoNascitaVitello());
		vitelloBdn.setFlagProrogaMarcatura(vitelloDto.getFlagProrogaMarcatura());
		vitelloBdn.setTipoOrigine(vitelloDto.getTipoOrigine());
		vitelloBdn.setAllevId(detenzionePrimoParto.getAllevId());
		vitelloBdn.setAziendaCodice(detenzionePrimoParto.getAziendaCodice());

		CapoLatteBdn capoLatteBdn=new CapoLatteBdn();
		capoLatteBdn.addVitello(vitelloBdn);

		capoBdn.setCapoLatteBdn(capoLatteBdn);
		/* old
		CapoLatteBdn capoLatteBdn=new CapoLatteBdn();
		List<JsonNode> vitelli=trovaVitelli(clsCapiVacca);
		vitelli.forEach(vitello -> {
			VitelloBdn vitelloBdn= new VitelloBdn();
			vitelloBdn.setCodVitello(vitello.path("codiceVitello").textValue());
			vitelloBdn.setDtInserimentoBdnNascita(new Date(vitello.path("vitelloDtInserimentoBdnNascita").longValue()));
			vitelloBdn.setDtNascita(new Date(vitello.path("dtNascitaVitello").longValue()));
			vitelloBdn.setFlagDelegatoNascitaVitello(vitello.path("flagDelegatoNascitaVitello").textValue());
			vitelloBdn.setFlagProrogaMarcatura(vitello.path("flagProrogaMarcatura").textValue());
			vitelloBdn.setTipoOrigine(vitello.path("vitelloTipoOrigine").textValue());
			vitelloBdn.setAllevId(vitello.path("allevId").asText());
			vitelloBdn.setAziendaCodice(vitello.path("aziendaCodice").textValue());
			capoLatteBdn.addVitello(vitelloBdn);
		});
		capoBdn.setCapoLatteBdn(capoLatteBdn);
		 */
		return capoLatteBdn;
	}

	private CapoBdn buildCapoBdn(List<JsonNode> clsCapiVacca, String codiceIntervento) {
		JsonNode capo=null;

		final FamigliaInterventi famigliaInterventi = getFamigliaInterventi(codiceIntervento);
		CapoDto capoDto = new CapoDto();
		Optional<DetenzioneDto> detenzione = Optional.empty();
		switch (famigliaInterventi) {
		case VACCHE_LATTE:
			capoDto = calcoloSostegnoVaccheLatteMontagnaConsumer.buildCapo(clsCapiVacca);
			Optional<VitelloDto> primoParto = ControlloCapiUtil.getPrimoParto((CapoLatteDto)capoDto);
			detenzione = ControlloCapiUtil.trovaDetenzioneDelPrimoParto(primoParto.get(), capoDto.getDetenzioni());
			break;
		case VACCHE_MACELLO:
			capoDto = calcoloSostegnoVaccheMacelloConsumer.buildCapo(clsCapiVacca);
			var capoMacellatoDto = (CapoMacellatoDto) capoDto;
			detenzione = controlloCapoMacellato.trovaDetenzioneDellaMacellazione(capoDto.getDetenzioni(),  capoMacellatoDto.getDataMacellazione());
			break;
		case OVICAPRINI:
			capoDto = calcoloSostegnoOvicaprinoConsumer.buildCapo(clsCapiVacca);
			detenzione = Optional.ofNullable(capoDto.getDetenzioni().get(0));
			break;
		default:
			break;
		}


		// adatta struttura dto nuova a builder vecchio mappando detenzionePrimoParto in capoBdn
		CapoBdn capoBdn=new CapoBdn();			
		capoBdn.setCapoId(capoDto.getIdCapo());
		capoBdn.setCodice(capoDto.getMarcaAuricolare());
		capoBdn.setSesso(capoDto.getSesso());
		capoBdn.setRazzaCodice(capoDto.getCodiceRazza());
		capoBdn.setDtNascita(LocalDateConverter.to(capoDto.getDataNascita()));
		capoBdn.setIntervento(codiceIntervento);
		if (detenzione.isPresent()) {
			capoBdn.setDtInizioDetenzione(LocalDateConverter.to(detenzione.get().getDtInizioDetenzione()));
			capoBdn.setDtFineDetenzione(LocalDateConverter.to(detenzione.get().getDtFineDetenzione()));
			capoBdn.setAziendaCodice(detenzione.get().getAziendaCodice());
			capoBdn.setAllevId(detenzione.get().getAllevId());
			capoBdn.setCuaa(detenzione.get().getCuaa());			
		}
		/* old
		CapoBdn capoBdn=new CapoBdn();			
		capoBdn.setCapoId(capo.path("capoId").asText());
		capoBdn.setCodice(capo.path("codice").textValue());
		capoBdn.setSesso(capo.path("sesso").textValue());
		capoBdn.setRazzaCodice(capo.path("razzaCodice").textValue());
		capoBdn.setDtNascita(new Date(capo.path("dtNascita").longValue()));
		capoBdn.setDtInizioDetenzione(new Date(capo.path("dtInizioDetenzione").longValue()));
		capoBdn.setDtFineDetenzione(new Date(capo.path("dtFineDetenzione").longValue()));
		capoBdn.setAziendaCodice(capo.path("aziendaCodice").textValue());
		capoBdn.setAllevId(capo.path("allevId").asText());
		capoBdn.setCuaa(capo.path("cuaa").textValue());
		capoBdn.setIntervento(codiceIntervento);
		 */
		return capoBdn;
	}

	private FamigliaInterventi getFamigliaInterventi(String codiceIntervento) {
		FamigliaInterventi famigliaInterventi=null;
		if (Arrays.asList(interventiCodiciVaccheLatte).contains(codiceIntervento)) {
			famigliaInterventi = FamigliaInterventi.VACCHE_LATTE;
		} 
		if (Arrays.asList(interventiVacchemacello).contains(codiceIntervento)) {
			famigliaInterventi = FamigliaInterventi.VACCHE_MACELLO;
		}
		if (Arrays.asList(interventiOvicaprini).contains(codiceIntervento)) {
			famigliaInterventi = FamigliaInterventi.OVICAPRINI;
		}
		if (Arrays.asList(interventiCodiciVaccheLatteMontagna).contains(codiceIntervento)) {
			famigliaInterventi = FamigliaInterventi.VACCHE_LATTE;
		}
		return famigliaInterventi;
	}



}

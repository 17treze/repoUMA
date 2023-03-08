package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.fasterxml.jackson.databind.JsonNode;

public class DetenzioneBuilder {

	public DetenzioneDto fromLatte(JsonNode detenzione, Boolean isAllevamentoMontagna) {
		ZoneId systemDefault = ZoneId.systemDefault();
		return new DetenzioneDto()
				.setAllevId(detenzione.get("allevId").asText())
				.setAziendaCodice(detenzione.get("aziendaCodice").asText())
				.setCuaa(detenzione.get("cuaa").asText())
				.setDtFineDetenzione(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtFineDetenzione").longValue()), systemDefault))
				.setDtInizioDetenzione(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtInizioDetenzione").longValue()), systemDefault))
				.setAllevamentoMontagna(isAllevamentoMontagna)
				.setVaccaDtComAutoritaIngresso(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("vaccaDtComAutoritaIngresso").longValue()), systemDefault))
				.setVaccaDtIngresso(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("vaccaDtIngresso").longValue()), systemDefault))
				.setVaccaDtInserimentoBdnIngresso(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("vaccaDtInserimentoBdnIngresso").longValue()), systemDefault));
	}

	public DetenzioneDto fromMacello(JsonNode detenzione) {
		ZoneId systemDefault = ZoneId.systemDefault();
		return new DetenzioneDto()
				.setAllevId(detenzione.get("allevId").asText())
				.setAziendaCodice(detenzione.get("aziendaCodice").asText())
				.setCuaa(detenzione.get("cuaa").asText())
				.setDtFineDetenzione(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtFineDetenzione").longValue()), systemDefault))
				.setDtInizioDetenzione(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtInizioDetenzione").longValue()), systemDefault))
				.setDtflagDelegatoIngresso(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("flagDelegatoIngresso").longValue()), systemDefault))
				.setDtUscita(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtUscita").longValue()), systemDefault))
				.setVaccaDtComAutoritaIngresso(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtComAutoritaIngresso").longValue()), systemDefault))
				.setVaccaDtIngresso(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtIngresso").longValue()), systemDefault))
				.setVaccaDtInserimentoBdnIngresso(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtInserimentoBdnIngresso").longValue()), systemDefault))
				.setVaccaDtInserimentoBdnUscita(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtInserimentoBdnUscita").longValue()), systemDefault));
	}

	public DetenzioneDto fromOvicaprino(JsonNode detenzione) {
		ZoneId systemDefault = ZoneId.systemDefault();
				return new DetenzioneDto()
				.setAziendaCodice(detenzione.get("aziendaCodice").asText())
				.setCuaa(detenzione.get("cuaa").asText())
				.setDtFineDetenzione(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtFineDetenzione").longValue()), systemDefault))
				.setDtInizioDetenzione(LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtInizioDetenzione").longValue()), systemDefault))
				.setVaccaDtIngresso(detenzione.path("dtIngresso").isMissingNode() ? null : LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtIngresso").longValue()), systemDefault))
				.setVaccaDtInserimentoBdnIngresso(detenzione.path("dtInserimentoBdnIngresso").isMissingNode() ? null : LocalDate.ofInstant(Instant.ofEpochMilli(detenzione.get("dtInserimentoBdnIngresso").longValue()), systemDefault));
	}
}

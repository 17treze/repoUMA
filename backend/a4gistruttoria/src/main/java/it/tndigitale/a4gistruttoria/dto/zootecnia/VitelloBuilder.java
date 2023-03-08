package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.fasterxml.jackson.databind.JsonNode;

public class VitelloBuilder {

	public VitelloDto from(JsonNode vitello) {
		ZoneId systemDefault = ZoneId.systemDefault();
		return new VitelloDto()
				.setMarcaAuricolare(vitello.path("codiceVitello").asText())
				.setDtInserimentoBdnNascita(LocalDate.ofInstant(Instant.ofEpochMilli(vitello.path("vitelloDtInserimentoBdnNascita").longValue()), systemDefault))
				.setDataNascita(LocalDate.ofInstant(Instant.ofEpochMilli(vitello.path("dtNascitaVitello").longValue()), systemDefault))
				.setFlagDelegatoNascitaVitello(vitello.path("flagDelegatoNascitaVitello").asText())
				.setFlagProrogaMarcatura(vitello.path("flagProrogaMarcatura").asText())
				.setTipoOrigine(vitello.path("vitelloTipoOrigine").asText())
				.setDtApplicazioneMarchio(LocalDate.ofInstant(Instant.ofEpochMilli(vitello.path("vitelloDtApplMarchio").longValue()), systemDefault));
	}

}

package it.tndigitale.a4gutente.dto.csv;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class UtenteA4gCsv {
	
	private static final String NOME = "NOME";
	private static final String COGNOME = "COGNOME";
	private static final String CODICE_FISCALE = "CODICE FISCALE";
	private static final String USERNAME = "USERNAME";
	private static final String DATA_ATTIVAZIONE = "DATA ATTIVAZIONE";
	private static final String UTENTE_SOSPESO = "UTENTE SOSPESO";
	private static final String MOTIVO_SOSPENSIONE = "MOTIVO SOSPENSIONE";
	private static final String PROFILO = "PROFILO";
	private static final String DESC_PROFILO = "DESCRIZIONE PROFILO";
	private static final String ENTE_APPARTENENZA = "ENTE APPARTENENZA";
	
	static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
	
	@JsonProperty(NOME)
	private String nome;

	@JsonProperty(COGNOME)
	private String cognome;

	@JsonProperty(CODICE_FISCALE)
	private String codiceFiscale;

	@JsonProperty(USERNAME)
	private String username;
	
	@JsonProperty(DATA_ATTIVAZIONE)
	private String dataAttivazione;

	@JsonProperty(UTENTE_SOSPESO)
	private String utenteSospeso;

	@JsonProperty(MOTIVO_SOSPENSIONE)
	private String motivoSospensione;
	
	@JsonProperty(PROFILO)
	private String profilo;
	
	@JsonProperty(DESC_PROFILO)
	private String descProfilo;
	
	@JsonProperty(ENTE_APPARTENENZA)
	private String enteAppartenenza;
	
	
	
	

	
	
	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public String getUsername() {
		return username;
	}

	

	public String getDataAttivazione() {
		return dataAttivazione;
	}

	public String getUtenteSospeso() {
		return utenteSospeso;
	}

	public String getMotivoSospensione() {
		return motivoSospensione;
	}

	public String getProfilo() {
		return profilo;
	}

	public String getDescProfilo() {
		return descProfilo;
	}

	public String getEnteAppartenenza() {
		return enteAppartenenza;
	}

	public static List<UtenteA4gCsv> fromDto(List<UtenteA4gDTO> dtos) {
		List<UtenteA4gCsv> csvList = new ArrayList<>();
		for (UtenteA4gDTO dto : dtos) {
			csvList.add(UtenteA4gCsv.buildFromDto(dto));
		}
		return csvList;
	}
	
	public static UtenteA4gCsv buildFromDto(UtenteA4gDTO dto,boolean sospeso,String motivazione) {
		UtenteA4gCsv csv = buildFromDto(dto);
		
		csv.utenteSospeso = UtenteA4gMapperUtility.buildUtenteSospeso(sospeso);
		csv.motivoSospensione = motivazione;
		
		return csv;
	} 
	
	private static UtenteA4gCsv buildFromDto(UtenteA4gDTO dto) {
		UtenteA4gCsv csv = new UtenteA4gCsv();
		
		csv.nome = dto.getNome();
		csv.cognome = dto.getCognome();
		csv.codiceFiscale = dto.getCodiceFiscale();
		csv.username = dto.getUserName();
		csv.dataAttivazione = dto.getDataAttivazione() != null ? dto.getDataAttivazione().format(formatter) : null;
		//csv.utenteSospeso = dto.getUtenteSospeso();
		//csv.motivoSospensione = dto.getMotivoSospensione();
		csv.profilo = dto.getProfilo();
		csv.descProfilo = dto.getDescProfilo();
		csv.enteAppartenenza = dto.getEnteAppartenenza();
		return csv;
	}
	
	public static CsvSchema getSchema() {
		return CsvSchema.builder()
				.addColumn(NOME, CsvSchema.ColumnType.STRING_OR_LITERAL)
				.addColumn(COGNOME, CsvSchema.ColumnType.STRING_OR_LITERAL)
				.addColumn(CODICE_FISCALE, CsvSchema.ColumnType.STRING_OR_LITERAL)
				.addColumn(USERNAME, CsvSchema.ColumnType.STRING_OR_LITERAL)
				.addColumn(DATA_ATTIVAZIONE, CsvSchema.ColumnType.STRING)
				.addColumn(UTENTE_SOSPESO, CsvSchema.ColumnType.STRING)
				.addColumn(MOTIVO_SOSPENSIONE, CsvSchema.ColumnType.STRING_OR_LITERAL)
				.addColumn(PROFILO, CsvSchema.ColumnType.STRING_OR_LITERAL)
				.addColumn(DESC_PROFILO, CsvSchema.ColumnType.STRING_OR_LITERAL)
				.addColumn(ENTE_APPARTENENZA, CsvSchema.ColumnType.STRING_OR_LITERAL)
				.build().withColumnSeparator(';').withoutQuoteChar().withHeader();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codiceFiscale == null) ? 0 : codiceFiscale.hashCode());
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
		result = prime * result + ((dataAttivazione == null) ? 0 : dataAttivazione.hashCode());
		result = prime * result + ((descProfilo == null) ? 0 : descProfilo.hashCode());
		result = prime * result + ((enteAppartenenza == null) ? 0 : enteAppartenenza.hashCode());
		result = prime * result + ((motivoSospensione == null) ? 0 : motivoSospensione.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((profilo == null) ? 0 : profilo.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((utenteSospeso == null) ? 0 : utenteSospeso.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UtenteA4gCsv other = (UtenteA4gCsv) obj;
		if (codiceFiscale == null) {
			if (other.codiceFiscale != null)
				return false;
		} else if (!codiceFiscale.equals(other.codiceFiscale))
			return false;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (dataAttivazione == null) {
			if (other.dataAttivazione != null)
				return false;
		} else if (!dataAttivazione.equals(other.dataAttivazione))
			return false;
		if (descProfilo == null) {
			if (other.descProfilo != null)
				return false;
		} else if (!descProfilo.equals(other.descProfilo))
			return false;
		if (enteAppartenenza == null) {
			if (other.enteAppartenenza != null)
				return false;
		} else if (!enteAppartenenza.equals(other.enteAppartenenza))
			return false;
		if (motivoSospensione == null) {
			if (other.motivoSospensione != null)
				return false;
		} else if (!motivoSospensione.equals(other.motivoSospensione))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (profilo == null) {
			if (other.profilo != null)
				return false;
		} else if (!profilo.equals(other.profilo))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (utenteSospeso == null) {
			if (other.utenteSospeso != null)
				return false;
		} else if (!utenteSospeso.equals(other.utenteSospeso))
			return false;
		return true;
	}
	
	
	
	

}

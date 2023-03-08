/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author S.DeLuca
 *
 */
public class DomandaIntegrativaDto {

	private List<AllevamentoDto> allevamenti;
	private String stato;
	private String identificativo;
	private LocalDateTime dtUltimoAggiornamento;
	protected Long id;

	public List<AllevamentoDto> getAllevamenti() {
		return allevamenti;
	}

	public void setAllevamenti(List<AllevamentoDto> allevamenti) {
		this.allevamenti = allevamenti;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public static class AllevamentoDto {

		private String descAllevamento;
		private String descProprietario;
		private List<CapoDto> capi;

		public String getDescAllevamento() {
			return descAllevamento;
		}

		public void setDescAllevamento(String descAllevamento) {
			this.descAllevamento = descAllevamento;
		}

		public String getDescProprietario() {
			return descProprietario;
		}

		public void setDescProprietario(String descProprientario) {
			this.descProprietario = descProprientario;
		}

		public List<CapoDto> getCapi() {
			return capi;
		}

		public void setCapi(List<CapoDto> capi) {
			this.capi = capi;
		}
	}

	public static class CapoDto {

		private String codCapo;
		private String codSpecie;
		private List<InterventoDto> interventi;

		public String getCodCapo() {
			return codCapo;
		}

		public void setCodCapo(String codCapo) {
			this.codCapo = codCapo;
		}

		public String getCodSpecie() {
			return codSpecie;
		}

		public void setCodSpecie(String codSpecie) {
			this.codSpecie = codSpecie;
		}

		public List<InterventoDto> getInterventi() {
			return interventi;
		}

		public void setInterventi(List<InterventoDto> interventi) {
			this.interventi = interventi;
		}
	}

	public static class InterventoDto {

		private String codice;
		private String esito;
		private Long idEsito;
		private Boolean selezionato;

		public String getCodice() {
			return codice;
		}

		public void setCodice(String codice) {
			this.codice = codice;
		}

		public String getEsito() {
			return esito;
		}

		public void setEsito(String esito) {
			this.esito = esito;
		}

		public Long getIdEsito() {
			return idEsito;
		}

		public void setIdEsito(Long idEsito) {
			this.idEsito = idEsito;
		}

		public Boolean getSelezionato() {
			return selezionato;
		}

		public void setSelezionato(Boolean selezionato) {
			this.selezionato = selezionato;
		}
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public LocalDateTime getDtUltimoAggiornamento() {
		return dtUltimoAggiornamento;
	}

	public void setDtUltimoAggiornamento(LocalDateTime dtUltimoAggiornamento) {
		this.dtUltimoAggiornamento = dtUltimoAggiornamento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}

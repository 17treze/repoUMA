package it.tndigitale.a4g.framework.pagination.model;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

@ApiModel("Rappresenta il modello per definire la paginazione di una ricerca " + 
				"(Se non definita, viene ritornato un orggetto di default con numero pagina " + 
				"0 e numero elementi per pagina 10")
public class Paginazione implements Serializable {

		private static final long serialVersionUID = 4268922379632858818L;

		private static final int NUMERO_ELEMENTI_DEFAULT = 10;
		private static final int PAGINA_DEFAULT = 0;

		public static final Paginazione PAGINAZIONE_DEFAULT = new Paginazione(NUMERO_ELEMENTI_DEFAULT, PAGINA_DEFAULT);

		@ApiParam(value = "Numero elementi per pagina. Valore di default: " + NUMERO_ELEMENTI_DEFAULT, required = false)
		private Integer numeroElementiPagina;
		@ApiParam(value = "Numero di pagina. Valore di default: " + PAGINA_DEFAULT, required = false)
		private Integer pagina;

		public Paginazione() {
				this(NUMERO_ELEMENTI_DEFAULT, PAGINA_DEFAULT);
		}

		public Paginazione(Integer numeroElementiPagina, Integer pagina) {
				super();
				this.numeroElementiPagina = numeroElementiPagina;
				this.pagina = pagina;
		}

		public static Paginazione getOrDefault(Paginazione paginazione) {
				Optional<Paginazione> optionalPaginazione = ofNullable(paginazione);
				Paginazione result = optionalPaginazione.orElse(new Paginazione());

				if (optionalPaginazione.isPresent() && (paginazione.getNumeroElementiPagina() == null || paginazione.getNumeroElementiPagina() <= 0)) {
						result.setNumeroElementiPagina(NUMERO_ELEMENTI_DEFAULT);
				}
				if (optionalPaginazione.isPresent() && (paginazione.getPagina() == null || paginazione.getPagina() < 0)) {
						result.setPagina(PAGINA_DEFAULT);
				}
				return result;
		}

		public static Paginazione of() {
				return new Paginazione();
		}

		public Integer getNumeroElementiPagina() {
				return numeroElementiPagina;
		}

		public Paginazione setNumeroElementiPagina(Integer numeroElementiPagina) {
				this.numeroElementiPagina = numeroElementiPagina;
				return this;
		}

		public Integer getPagina() {
				return pagina;
		}

		public Paginazione setPagina(Integer pagina) {
				this.pagina = pagina;
				return this;
		}

		public Boolean isValid() {
				return (numeroElementiPagina != null && numeroElementiPagina > 0 && pagina >= 0);
		}

		@Override
		public boolean equals(Object o) {
				if (this == o)
						return true;
				if (o == null || getClass() != o.getClass())
						return false;
				Paginazione that = (Paginazione) o;
				return Objects.equals(numeroElementiPagina, that.numeroElementiPagina) && Objects.equals(pagina, that.pagina);
		}

		@Override
		public int hashCode() {
				return Objects.hash(numeroElementiPagina, pagina);
		}
}

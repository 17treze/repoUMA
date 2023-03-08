package it.tndigitale.a4g.framework.pagination.model;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

@ApiModel("Rappresenta il modello per definire l'ordinamento di una ricerca")
public class Ordinamento implements Serializable {

		private static final String DEFAULT_PROPERTY = "id";
		
		// prendere gli ultimi o usare come il db il default asc?
		public static final Ordinamento DEFAULT_ORDER_BY = new Ordinamento(DEFAULT_PROPERTY, Ordine.DESC);

		private static final long serialVersionUID = 4711586120128894700L;
		@ApiParam(value = "Proprieta' dell'ordinamento (Default: " + DEFAULT_PROPERTY + ")", required = false)
		private String proprieta;
		@ApiParam(value = "Direzione dell'ordinamento (ASC o DESC). Valore di default: ASC", required = false)
		private Ordine ordine;

		public Ordinamento() {
				this.ordine = Ordine.ASC;
		}

		public Ordinamento(String proprieta, Ordine ordine) {
				this.proprieta = proprieta;
				this.ordine = ordine;
		}

		public static Ordinamento getOrDefault(Ordinamento ordinamento) throws Exception {
				Optional<Ordinamento> optionalOrdinamento = ofNullable(ordinamento);
				Ordinamento result = optionalOrdinamento.orElse(new Ordinamento(DEFAULT_PROPERTY, Ordine.ASC));
				if (optionalOrdinamento.isPresent() && (ordinamento.getProprieta() == null || ordinamento.getProprieta().isEmpty())) {
						result.setProprieta(DEFAULT_PROPERTY);
				}
				if (optionalOrdinamento.isPresent() && ordinamento.getOrdine() == null) {
						result.setOrdine(Ordine.ASC);
				}
				return result;
		}

		public String getProprieta() {
				return proprieta;
		}

		public Ordinamento setProprieta(String proprieta) {
				this.proprieta = proprieta;
				return this;
		}

		public Ordine getOrdine() {
				return ordine;
		}

		public Ordinamento setOrdine(Ordine ordine) {
				this.ordine = ordine;
				return this;
		}

		public static enum Ordine {
				ASC, DESC;
		}

		@Override
		public boolean equals(Object o) {
				if (this == o)
						return true;
				if (o == null || getClass() != o.getClass())
						return false;
				Ordinamento that = (Ordinamento) o;
				return Objects.equals(proprieta, that.proprieta) && ordine == that.ordine;
		}

		@Override
		public int hashCode() {
				return Objects.hash(proprieta, ordine);
		}
}

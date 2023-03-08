/**
 * 
 */
package it.tndigitale.a4g.ags.utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

/**
 * Classi di utility per il mapping tra i nuovi PAC e il vecchio SETTORE.
 * @author S.DeLuca
 *
 */
@Component
public class DecodificaPACSettore {

	private Map<PACKey, String> valori = new HashMap<>();

	@PostConstruct
	private void popolaValori() {
		valori.put(new PACKey("PAC1420", "DU"), "PI2014");
	}
	
	public Map<PACKey, String> getValori() {
		return valori;
	}

	/**
	 * Inner class per la chiave del PAC.
	 * 
	 * @author S.DeLuca
	 *
	 */
	static public class PACKey {
		private String pac;
		private String tipoDomanda;

		public PACKey() {
			// TODO Auto-generated constructor stub
		}

		public PACKey(String pac, String tipoDomanda) {
			super();
			this.pac = pac;
			this.tipoDomanda = tipoDomanda;
		}

		public String getPac() {
			return pac;
		}

		public void setPac(String pac) {
			this.pac = pac;
		}

		public String getTipoDomanda() {
			return tipoDomanda;
		}

		public void setTipoDomanda(String tipoDomanda) {
			this.tipoDomanda = tipoDomanda;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((pac == null) ? 0 : pac.hashCode());
			result = prime * result + ((tipoDomanda == null) ? 0 : tipoDomanda.hashCode());
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
			PACKey other = (PACKey) obj;
			if (pac == null) {
				if (other.pac != null)
					return false;
			} else if (!pac.equals(other.pac))
				return false;
			if (tipoDomanda == null) {
				if (other.tipoDomanda != null)
					return false;
			} else if (!tipoDomanda.equals(other.tipoDomanda))
				return false;
			return true;
		}
	}

}

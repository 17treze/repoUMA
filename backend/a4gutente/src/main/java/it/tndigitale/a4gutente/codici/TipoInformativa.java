package it.tndigitale.a4gutente.codici;

public enum TipoInformativa {

	GENERALE(Valori.GENERALE);

	  private TipoInformativa (String value) {
	     // force equality between name of enum instance, and value of constant
	     if (!this.name().equals(value))
	        throw new IllegalArgumentException("Incorrect use of ELanguage");
	  }

	  public static class Valori {
	     public static final String GENERALE = "GENERALE";
	  }
}

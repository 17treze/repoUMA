package it.tndigitale.a4g.proxy.dto.fascicolo.sian;

import java.util.Arrays;

public enum TipoDetentore {
	
	CAA("001"),
	OP("002"),
	REGIONE("003");
	
	private final String codTipoDetentore;
	
	TipoDetentore(String codTipoDetentore) {
        this.codTipoDetentore = codTipoDetentore;
    }

	public String getCodTipoDetentore() {
		return codTipoDetentore;
	}
	
    public static TipoDetentore fromCodStato(String codTipoDetentore) {
        return Arrays.asList(TipoDetentore.values())
                .stream()
                .filter(tipo -> tipo.getCodTipoDetentore().equals(codTipoDetentore))
                .findFirst()
                .orElse(null);
    }
    
}

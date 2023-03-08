package it.tndigitale.a4g.richiestamodificasuolo;

import it.tndigitale.a4g.framework.security.model.Ruolo;

public enum Ruoli implements Ruolo {
	
	VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI(Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD),
	VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE(Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD),
	EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE(Ruoli.EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE_COD),
	EDIT_MESSAGGIO_RICHIESTA_TUTTI(Ruoli.EDIT_MESSAGGIO_RICHIESTA_TUTTI_COD),
	EDIT_DOCUMENTO_RICHIESTA_TUTTI(Ruoli.EDIT_DOCUMENTO_RICHIESTA_TUTTI_COD),
	EDIT_LAVORAZIONE_SUOLO(Ruoli.EDIT_LAVORAZIONE_SUOLO_COD),
	VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI(Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD),
	VISUALIZZA_LAVORAZIONE_SUOLO_VITE(Ruoli.VISUALIZZA_LAVORAZIONE_SUOLO_VITE_COD),
	VISUALIZZA_POLIGONO_TUTTI(Ruoli.VISUALIZZA_POLIGONO_TUTTI_COD),
	VISUALIZZA_POLIGONO_VITE(Ruoli.VISUALIZZA_POLIGONO_VITE_COD),
	EDIT_POLIGONO_TUTTI(Ruoli.EDIT_POLIGONO_TUTTI_COD),
	EDIT_POLIGONO_VITE(Ruoli.EDIT_POLIGONO_VITE_COD),
	VISUALIZZA_LAYER_CAA(Ruoli.VISUALIZZA_LAYER_CAA_COD),
	VISUALIZZA_LAYER_BACKOFFICE(Ruoli.VISUALIZZA_LAYER_BACKOFFICE_COD),
	VISUALIZZA_LAYER_VITICOLO(Ruoli.VISUALIZZA_LAYER_VITICOLO_COD);
	
	public static final String VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD = "a4g.richiestamodificasuolo.richiestamodificasuolo.visualizza.tutti";
	public static final String VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD = "a4g.richiestamodificasuolo.richiestamodificasuolo.visualizza.filtroCuaa";
	public static final String EDIT_RICHIESTA_MODIFICA_SUOLO_ENTE_COD = "a4g.richiestamodificasuolo.richiestamodificasuolo.edit.filtroCuaa";
	public static final String EDIT_MESSAGGIO_RICHIESTA_TUTTI_COD = "a4g.richiestamodificasuolo.richiestamodificasuolo.messaggio.edit.tutti";
	public static final String EDIT_DOCUMENTO_RICHIESTA_TUTTI_COD = "a4g.richiestamodificasuolo.richiestamodificasuolo.documento.edit.tutti";
	public static final String EDIT_LAVORAZIONE_SUOLO_COD = "a4g.richiestamodificasuolo.lavorazione.edit";
	public static final String VISUALIZZA_LAVORAZIONE_SUOLO_TUTTI_COD = "a4g.richiestamodificasuolo.lavorazione.visualizza.tutti";
	public static final String VISUALIZZA_LAVORAZIONE_SUOLO_VITE_COD = "a4g.richiestamodificasuolo.lavorazione.visualizza.filtroVite";
	public static final String VISUALIZZA_POLIGONO_TUTTI_COD = "a4g.richiestamodificasuolo.poligonodichiarato.visualizza.tutti";
	public static final String VISUALIZZA_POLIGONO_VITE_COD = "a4g.richiestamodificasuolo.poligonodichiarato.visualizza.filtroVite";
	public static final String EDIT_POLIGONO_TUTTI_COD = "a4g.richiestamodificasuolo.poligonodichiarato.edit.tutti";
	public static final String EDIT_POLIGONO_VITE_COD = "a4g.richiestamodificasuolo.poligonodichiarato.edit.filtroVite";
	public static final String VISUALIZZA_LAYER_CAA_COD = "a4g.richiestamodificasuolo.layer.caa";
	public static final String VISUALIZZA_LAYER_BACKOFFICE_COD = "a4g.richiestamodificasuolo.layer.backoffice";
	public static final String VISUALIZZA_LAYER_VITICOLO_COD = "a4g.richiestamodificasuolo.layer.viticolo";
	
	private String codiceRuolo;

	Ruoli(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}
	
	public String getCodiceRuolo() {
		return codiceRuolo;
	}
}

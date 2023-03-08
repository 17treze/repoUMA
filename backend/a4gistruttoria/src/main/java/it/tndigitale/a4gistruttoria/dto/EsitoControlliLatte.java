package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.util.EsitoControlliLatteEnum;

public class EsitoControlliLatte {
	

	private EsitoControlliLatteEnum esito;
	private String messaggio;
	private RichiestaAllevamDu richiestaAllevamDu;
	
	public EsitoControlliLatte(EsitoControlliLatteEnum esito, String messaggio) {
		super();
		this.esito = esito;
		this.messaggio = messaggio;
	}
	
	public EsitoControlliLatteEnum getEsito() {
		return esito;
	}
	public void setEsito(EsitoControlliLatteEnum esito) {
		this.esito = esito;
	}
	public RichiestaAllevamDu getRichiestaAllevamDu() {
		return richiestaAllevamDu;
	}
	public void setRichiestaAllevamDu(RichiestaAllevamDu richiestaAllevamDu) {
		this.richiestaAllevamDu = richiestaAllevamDu;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

}

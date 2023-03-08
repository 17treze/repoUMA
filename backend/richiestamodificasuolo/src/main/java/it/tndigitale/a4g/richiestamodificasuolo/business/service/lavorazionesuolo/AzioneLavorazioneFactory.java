package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo;

public interface AzioneLavorazioneFactory<T, I> {

	public AzioneLavorazioneBase<T, I> inizializzaAzioneLavorazioneBase(String qualifierComponent);
}

package it.tndigitale.a4gistruttoria.service.businesslogic;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

import java.util.List;
import java.util.function.Consumer;

public interface GeneraDatiAnnualiStrategy<T extends Enum<T>>  {
	
	public T getTipoDatoAnnuale();
	
	public List<IstruttoriaModel> caricaIstruttorie(Integer annoCampagna);
	
	public void generaDatiPerIstruttoria(IstruttoriaModel istruttoria, Integer annoCampagna);
	
	public void cancellaDatiEsistenti(T tipoStatistica, Integer annoCampagna);
}
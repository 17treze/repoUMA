package it.tndigitale.a4gutente.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import it.tndigitale.a4gutente.repository.model.A4gtProfilo;

public interface IProfiloDao  extends IEntitaDominioRepository<A4gtProfilo> {
	
	public A4gtProfilo findByIdentificativo(String identificativo);
	
	@Query(nativeQuery = true, name = "A4gtProfilo.findAllUtente")
	public List<A4gtProfilo> findAllUtente();
}

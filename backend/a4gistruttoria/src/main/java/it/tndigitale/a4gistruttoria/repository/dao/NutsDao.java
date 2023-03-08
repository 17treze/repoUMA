package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gdNuts;

@Repository
public interface NutsDao extends JpaRepository<A4gdNuts, Long> {
	
	public Optional<A4gdNuts> findBySiglaProvincia(String siglaProvincia);
}

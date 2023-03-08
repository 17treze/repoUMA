package it.tndigitale.a4gutente.repository.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gutente.repository.model.LoadUtenteCaa;
import it.tndigitale.a4gutente.repository.model.LoadUtenteCaaPK;

@Repository
public interface ILoadUtenteCaaDao extends JpaRepository<LoadUtenteCaa, LoadUtenteCaaPK>{
}




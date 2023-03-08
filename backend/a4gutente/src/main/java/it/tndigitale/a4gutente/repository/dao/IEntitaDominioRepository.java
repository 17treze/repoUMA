package it.tndigitale.a4gutente.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public interface IEntitaDominioRepository<T extends EntitaDominio> extends JpaRepository<T, Long> {

}

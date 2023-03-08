package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.ClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FatturaClienteModel;


@Repository
public interface FattureClientiDao extends JpaRepository<FatturaClienteModel, Long> {
	public List<FatturaClienteModel> findByCliente (ClienteModel cliente);
	public void deleteByCliente_id(long idCliente);
}

package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.ClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsumiClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.LavorazioneModel;

@Repository
public interface ConsumiClientiDao extends JpaRepository<ConsumiClienteModel, Long> {

	public void deleteByLavorazioneModelAndCliente(LavorazioneModel lavorazioneModel, ClienteModel clienteModel);
	public List<ConsumiClienteModel> findByCliente_id(long idCliente);
	public void deleteByCliente_id(long idCliente);
}

package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiFiltroDomanda;

@Repository
public interface DatiFiltroDomandaDao extends JpaRepository<A4gtDatiFiltroDomanda, Long> {

	List<A4gtDatiFiltroDomanda> findByDomandaUnicaModel_id(Long id);

	A4gtDatiFiltroDomanda findByTipoFiltroAndDomandaUnicaModel_id(String tipoFiltro, Long id);

}

package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;

@Repository
public interface ElencoLiquidazioneDao extends JpaRepository<ElencoLiquidazioneModel, Long> {

	public ElencoLiquidazioneModel findByCodElenco(String codElenco);
}

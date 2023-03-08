package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.List;

import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;

@Repository
public interface PassoTransizioneDao extends JpaRepository<PassoTransizioneModel, Long> {

	public List<PassoTransizioneModel> findByTransizioneIstruttoria(
			TransizioneIstruttoriaModel transizione);
	
	@Query("select p from PassoTransizioneModel p "
			+ " where p.transizioneIstruttoria.istruttoria.domandaUnicaModel.id = :idDomanda "
			+ " and p.codicePasso = 'LIQUIDABILITA' "
			+ " and p.esito = 'KO'")
	public List<PassoTransizioneModel> findByIdDomandaNonAncoraLiquidata(Long idDomanda, Sort sort);
	
	@Query("select distinct p.transizioneIstruttoria.istruttoria.domandaUnicaModel "
			+ " from PassoTransizioneModel p "
			+ " where p.transizioneIstruttoria.istruttoria.domandaUnicaModel.cuaaIntestatario = :cuaa "
			+ " and p.codicePasso = 'LIQUIDABILITA' "
			+ " and p.esito = 'KO'")
	public List<DomandaUnicaModel> findDomandeByCuaaDomandaNonAncoraLiquidata(String cuaa);
}

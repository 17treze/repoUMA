package it.tndigitale.a4g.fascicolo.antimafia.repository.dao;

import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtAllegatoDicFamConv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AllegatoDicFamConvDao extends JpaRepository<A4gtAllegatoDicFamConv, Long> {
	@Query("SELECT allegato FROM A4gtAllegatoDicFamConv allegato WHERE allegato.cfSoggettoImpresa = :cfSoggettoImpresa AND allegato.codCarica = :codCarica")
	public A4gtAllegatoDicFamConv findOneByCfAndCodCarica(@Param("cfSoggettoImpresa")String cfSoggettoImpresa,@Param("codCarica")String codCarica);
}
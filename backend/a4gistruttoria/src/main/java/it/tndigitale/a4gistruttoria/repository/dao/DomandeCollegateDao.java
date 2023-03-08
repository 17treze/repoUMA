package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;
import it.tndigitale.a4gistruttoria.util.StatoDomandaCollegata;
import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

@Repository
public interface DomandeCollegateDao extends JpaRepository<A4gtDomandeCollegate, Long> {

	public List<A4gtDomandeCollegate> findByCuaaIn(List<String> cuaa);
	public List<A4gtDomandeCollegate> findByIdDomandaIn(List<Long> idDomande);
	public List<A4gtDomandeCollegate> findByCuaaInAndStatoBdnaInAndTipoDomandaAndA4gtTrasmissioneBdnaIsNull(List<String> cuaa, List<StatoDomandaCollegata> statoDomandaCollegata, TipoDomandaCollegata tipoDomanda);
	public List<A4gtDomandeCollegate> findByCuaaInAndStatoBdnaInAndTipoDomandaAndCampagnaInAndA4gtTrasmissioneBdnaIsNull(List<String> cuaa, List<StatoDomandaCollegata> statoDomandaCollegata, TipoDomandaCollegata tipoDomanda, List<Integer> campagna);

}

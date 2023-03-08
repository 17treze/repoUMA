package it.tndigitale.a4gistruttoria.repository.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

@Repository
public interface TransizioneIstruttoriaDao extends JpaRepository<TransizioneIstruttoriaModel, Long> {
	@Query("select t from TransizioneIstruttoriaModel t where "
			+ "t.istruttoria = :istruttoria and "
			+ "t.a4gdStatoLavSostegno2.identificativo in "
				+ "('RICHIESTO', 'CONTROLLI_CALCOLO_KO', 'CONTROLLI_CALCOLO_OK','CONTROLLI_INTERSOSTEGNO_OK', 'INTEGRATO') "
			+ "and t.a4gdStatoLavSostegno1.identificativo in "
			+ "	('CONTROLLI_CALCOLO_KO', 'CONTROLLI_CALCOLO_OK') ")
	public List<TransizioneIstruttoriaModel> findTransizioneCalcoloPremio(
			@Param("istruttoria") IstruttoriaModel istruttoria);

	@Query("select t from TransizioneIstruttoriaModel t where "
			+ "t.istruttoria = :istruttoria and "
			+ "t.a4gdStatoLavSostegno2.identificativo in "
				+ "('LIQUIDABILE', 'CONTROLLI_LIQUIDABILE_KO', 'CONTROLLI_CALCOLO_OK') "
			+ "and t.a4gdStatoLavSostegno1.identificativo in "
				+ "('LIQUIDABILE', 'CONTROLLI_LIQUIDABILE_KO','DEBITI')")
	public List<TransizioneIstruttoriaModel> findTransizioneLiquidabilita(
			@Param("istruttoria") IstruttoriaModel istruttoria);

	@Query("select t from TransizioneIstruttoriaModel t where "
			+ "t.istruttoria = :istruttoria and "
			+ "t.a4gdStatoLavSostegno2.identificativo in "
				+ "('PAGAMENTO_NON_AUTORIZZATO', 'LIQUIDABILE','CONTROLLI_INTERSOSTEGNO_OK') "
			+ "and t.a4gdStatoLavSostegno1.identificativo in "
				+ "('CONTROLLI_INTERSOSTEGNO_OK','PAGAMENTO_NON_AUTORIZZATO', 'NON_LIQUIDABILE')")
	public List<TransizioneIstruttoriaModel> findTransizioneControlloIntersostegno(
			@Param("istruttoria") IstruttoriaModel istruttoria);
	
	
	/* public List<TransizioneIstruttoriaModel> findTransizioniByIstruttoriaModel(
			@Param("istruttoria") IstruttoriaModel istruttoria);	*/
	
	@Query("select count (*) from TransizioneIstruttoriaModel t where "
			+ "t.istruttoria = :istruttoria and "
			+ "t.a4gdStatoLavSostegno2.identificativo in "
				+ "('RICHIESTO', 'CONTROLLI_CALCOLO_KO', 'CONTROLLI_CALCOLO_OK','CONTROLLI_INTERSOSTEGNO_OK', 'INTEGRATO') "
			+ "and t.a4gdStatoLavSostegno1.identificativo in "
				+ "('CONTROLLI_CALCOLO_KO', 'CONTROLLI_CALCOLO_OK') ")
	public BigDecimal countTransizioneCalcoloDisaccoppiato(
			@Param("istruttoria") IstruttoriaModel istruttoria);
}

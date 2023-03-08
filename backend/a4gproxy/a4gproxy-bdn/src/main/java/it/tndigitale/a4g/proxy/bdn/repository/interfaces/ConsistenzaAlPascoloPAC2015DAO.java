package it.tndigitale.a4g.proxy.bdn.repository.interfaces;

import java.math.BigDecimal;
import java.util.List;

import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAlPascoloPAC2015DO;
import it.tndigitale.a4g.proxy.bdn.dto.istruttoria.ConsistenzaPascolo2015Dto;

public interface ConsistenzaAlPascoloPAC2015DAO {

	public List<ConsistenzaAlPascoloPAC2015DO> getConsistenzaAlPascoloPAC2015(int annoCampagna, ConsistenzaAlPascoloPAC2015DO bdnData);

	public void closeRecordById(ConsistenzaAlPascoloPAC2015DO dataObject);

	public void insert(ConsistenzaAlPascoloPAC2015DO consistenza, int annoCampagna);

	public void closeRecordByCodicePascolo(String codicePascolo, int annoCampagna);

	/**
	 * Carica i dati di consistenza pascolo 2015 per l algoritmo man 4 di istruttoria
	 * 
	 * @param annoCampagna
	 * @param codicePascolo
	 * @return
	 */
	public List<ConsistenzaPascolo2015Dto> getConsistenzaPascoloPerIstruttoria(BigDecimal annoCampagna, String codicePascolo);

}

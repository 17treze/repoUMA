package it.tndigitale.a4g.proxy.bdn.repository.interfaces;

import java.math.BigDecimal;
import java.util.List;

import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAllevamentoDO;

public interface ConsistenzaAllevamentoDAO {

	public ConsistenzaAllevamentoDO getConsistenzaAllevamento(String cuaa, ConsistenzaAllevamentoDO bdnData);

	public void closeRecordById(ConsistenzaAllevamentoDO dataObject);

	public void insert(ConsistenzaAllevamentoDO consistenza, String cuaa, int annoCampagna);

	public void closeRecordByCuaa(String cuaa, int annoCampagna);

	public List<ConsistenzaAllevamentoDO> getConsAllevamento(String codiceFiscale, BigDecimal annoCampagna);
	
	public boolean esisteAziendaInCache(String codiceAzienda);
	
	public boolean esisteAziendaCollegataInCache(long idCaws);

	public void insertAziendaNuova(String codiceAzienda, String codiceComune);

	public void deleteAzienda(String codiceAzienda);
}

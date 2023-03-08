package it.tndigitale.a4g.srt.repository.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import it.tndigitale.a4g.srt.dto.ImportoRichiesto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class ImpreseDaoImpl extends JdbcDaoSupport implements ImpreseDao {

    private final String sqlGetImportoRichiesto = "SELECT CUAA as Cuaa, "
    												+ "MISURA as MisuraPSR, "
    												+ "ID_DOMANDA as IdDomanda, "
    												+ "IMPORTO_RICHIESTO_NON_PAG as ImportoRichiesto, "
    												+ "DATA_MODIFICA as DataDomanda "
    												+ "FROM vIMPORTO_RICHIESTO WHERE CUAA = ? AND DATA_MODIFICA > ?";

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<ImportoRichiesto> getImportoRichiesto(String cuaa, Date dataDa) {
        logger.trace(String.format("ImpreseDaoImpl getImportoRichiesto cuaa: %s", cuaa));
        
        List<Object> params = new ArrayList<>();
        params.add(cuaa);
        params.add(dataDa); 

        return getJdbcTemplate().<ImportoRichiesto>query(
        		sqlGetImportoRichiesto, 
        		params.toArray(), 
        		new BeanPropertyRowMapper<ImportoRichiesto>(ImportoRichiesto.class));
    }
}

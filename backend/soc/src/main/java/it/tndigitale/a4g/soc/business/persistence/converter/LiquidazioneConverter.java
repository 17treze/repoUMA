package it.tndigitale.a4g.soc.business.persistence.converter;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import it.tndigitale.a4g.soc.business.dto.CapitoloDisciplina;
import it.tndigitale.a4g.soc.business.dto.CausaleImporto;
import it.tndigitale.a4g.soc.business.dto.Debito;
import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.business.dto.TipoDomanda;
import it.tndigitale.a4g.soc.business.dto.TipoPagamento;
import org.springframework.util.StringUtils;

public class LiquidazioneConverter {

    public static ImportoLiquidato importoLiquidatoFrom(
            final ResultSet resultSet,
            final List<CapitoloDisciplina> capitoli, TipoDomanda tipoDomanda) throws SQLException {
    	int annoEsercizio = resultSet.getInt("annoEsercizio");
    	String numeroDomanda = resultSet.getString("numeroDomanda");
        return new ImportoLiquidato()
        		.setAnno(annoEsercizio)
        		.setIncassatoNetto(resultSet.getBigDecimal("incassatoNetto"))
			    .setProgressivo(resultSet.getLong("progressivoCredito"))
			    .setTipoBilancio(resultSet.getString("tipoBilancio"))
			    .setIdElencoLiquidazione(recuperaNumeroElenco(resultSet))
			    .setDataEsecuzionePagamento(creaData(resultSet, "dataEsecuzione"))
			    .setCausale(creaCausale(resultSet, capitoli, annoEsercizio))
			    .setNumeroAutorizzazione(resultSet.getString("numeroAutorizzazione"))
			    .setDataAutorizzazione(creaData(resultSet, "dataAutorizzazione"))
			    .setProgressivoPagamento(resultSet.getLong("progressivoPagamento"))
                .codiceProdotto(resultSet.getString("codprodotto"))
			    .setTipoPagamento(mapTipoPagamento(numeroDomanda, resultSet, tipoDomanda))
        		.setNumeroDomanda(numeroDomanda);
    }

    private static TipoPagamento mapTipoPagamento(final String numeroDomanda, ResultSet resultSet, TipoDomanda tipoDomanda) throws SQLException {
    	if (numeroDomanda.startsWith("SF")) {
    		return TipoPagamento.SALDO;
    	} else if (numeroDomanda.startsWith("S")) {
    		return TipoPagamento.ACCONTO;
    	} else if (numeroDomanda.startsWith("A")) {
    		return TipoPagamento.ANTICIPO;
    	} else if(tipoDomanda == TipoDomanda.DOMANDA_PSR_SUPERFICIE && !StringUtils.isEmpty(resultSet.getString("tipopagamento"))) {
    	    return TipoPagamento.valueOf(resultSet.getString("tipopagamento"));
        }
    	return null;
    }
    
    private static LocalDateTime creaData(ResultSet rs, String nomeCampo) throws SQLException {
    	Date dataEsecuzione = rs.getDate(nomeCampo);
    	if (rs.wasNull() || dataEsecuzione == null) {
    		return null;
    	}
		return dataEsecuzione.toLocalDate().atStartOfDay();
	}

	private static Long recuperaNumeroElenco(ResultSet rs) throws SQLException {
    	long numeroElenco = rs.getLong("numeroElenco");
    	if (rs.wasNull()) {
    		return null;
    	}
    	return numeroElenco;
	}

	private static CausaleImporto creaCausale(ResultSet rs, List<CapitoloDisciplina> capitoli, Integer annoEsercizio) throws SQLException {
		//recupero il capitoli per l'anno da capitoli.properties
		Optional<CapitoloDisciplina> cap = capitoli.stream()
			.filter(capitolo -> capitolo.getAnnoEsercizio() == annoEsercizio.intValue())
			.findFirst();
		//recupero il capitolo e prodotto dalla result della query
		String capitolo = rs.getString("capitolo");
		String codProdotto = rs.getString("codProdotto");
		//se il capitolo per l'anno di campagna coincide con quello contenuto nelle properties allora è una DISCIPLINA_FINANZIARIA
    	if (cap.isPresent()
    			&& cap.get().getCapitolo().equals(capitolo)
    			&& cap.get().getProdotto().equals(codProdotto)) {
    		return CausaleImporto.DISCIPLINA_FINANZIARIA;
    	}
    	return CausaleImporto.PREMIO;
    }

	public static Debito debitoFrom(ResultSet resultSet) throws SQLException {
        return new Debito()
                .setImporto(resultSet.getBigDecimal("importIncasso"))
                .setDescrizioneCapitolo(resultSet.getString("descrizioneCapitolo"));
    }

}

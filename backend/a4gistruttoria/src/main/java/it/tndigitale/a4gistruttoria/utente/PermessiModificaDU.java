package it.tndigitale.a4gistruttoria.utente;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.config.DateFormatConfig;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;

@Component("permessiModificaDU")
public class PermessiModificaDU {

    @Autowired
    private IstruttoriaDao istruttoriaDao;
    @Autowired
    private Clock clock;
    @Value("${a4gistruttoria.istruttoriadu.anticipi.datafinemodifica}")
    private String dtFineModifica;

    private static final Set<String> statiFinali = new HashSet<>(Arrays.asList(
            StatoIstruttoria.PAGAMENTO_AUTORIZZATO.getStatoIstruttoria(),
            StatoIstruttoria.NON_AMMISSIBILE.getStatoIstruttoria(),
            StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria()));
    private static Logger logger = LoggerFactory.getLogger(PermessiModificaDU.class);

	@Transactional(readOnly = true)
	public boolean checkPermessiDomandaUnica(Long idDomanda) throws Exception {
	    //TODO: approfondire il caso in cui dei permessi sulla domanda
		return true;
	}

    @Transactional(readOnly = true)
    public boolean checkPermessiIstruttorie(List<Long> idIstruttorie) throws Exception {
        return this.checkPermessiIstruttorie(idIstruttorie, null);
    }
    
    @Transactional(readOnly = true)
    public boolean checkPermessiIstruttorie(List<Long> idIstruttorie, TipoProcesso tipoProcesso) throws Exception {
        for (Long idIstruttoria : idIstruttorie) {
            if(!this.checkPermessiIstruttoria(idIstruttoria, tipoProcesso)){
                return false;
            }
        }

        return true;
    }

    @Transactional(readOnly = true)
    public boolean checkPermessiIstruttoria(Long idIstruttoria) throws Exception {
        return this.checkPermessiIstruttoria(idIstruttoria, null);
    }
    
    @Transactional(readOnly = true)
    public boolean checkPermessiIstruttoria(Long idIstruttoria, TipoProcesso tipoProcesso) throws Exception {
        Optional<IstruttoriaModel> istruttoriaOtp = istruttoriaDao.findById(idIstruttoria);

        if (!istruttoriaOtp.isPresent()) {
            logger.warn("Accesso negato: Nessun'istruttoria trovata con id {}", idIstruttoria);
            return Boolean.FALSE;
        }

        // controlla in cascata (in and) i vari check implementati nelle Function.
        // aggiungere in questo stream eventuali altri check da fare.
        return Stream.of(checkAnticipi, checkStatiFinali)
                .map(function -> function.apply(istruttoriaOtp.get(), tipoProcesso))
                .noneMatch(x -> x.equals(Boolean.FALSE));
    }

    /*
     * Verifica l'abilitazione a modificare una domanda unica in istruttoria di anticipo.
     * IDU-EVO-18-01: Dal 01/12 dell’anno di campagna i dati dell’istruttoria di anticipo non sono modificabili.
     *
     */
    private BiFunction<IstruttoriaModel, TipoProcesso, Boolean> checkAnticipi = (istruttoriaModel, tipoProcesso) -> {
        TipoIstruttoria tipoIstruttoria = istruttoriaModel.getTipologia();

        Integer campagna = istruttoriaModel.getDomandaUnicaModel().getCampagna();
        LocalDateTime dataFineModifica = DateFormatConfig.convertiToLocalDateTime(dtFineModifica.concat("/").concat(String.valueOf(campagna)));
        // per gli anticipi l'unico sostegno è il disaccoppiato.
        if (TipoIstruttoria.ANTICIPO.equals(tipoIstruttoria) && clock.now().isAfter(dataFineModifica)) {
            logger.warn("Accesso negato: tentata modifica istruttoria con id {} e tipologia {}",
                    istruttoriaModel.getId(),
                    tipoIstruttoria);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    };

    /*
     *  verifica che una domanda unica dato un sostegno in input è in uno stato finale
     *  le domande uniche in questi stati non possono essere movimentate.
     *  Il controllo viene saltato se non è specificato alcun sostegno.
     */
    private BiFunction<IstruttoriaModel, TipoProcesso, Boolean> checkStatiFinali = (istruttoriaModel, tipoProcesso) -> {
        // Condizione per movimentazione da NON_AMMISSIBILE a RICHIESTO IDU-EVO-27-08 ACZ
    	// Condizione per movimentazione da NON_LIQUIDABILE a RICHIESTO IDU-ANT-16-01
    	if (TipoProcesso.RESET_ISTRUTTORIA.equals(tipoProcesso)
    			&& (StatoIstruttoria.NON_AMMISSIBILE.equals(istruttoriaModel.getStato()) || StatoIstruttoria.NON_LIQUIDABILE.equals(istruttoriaModel.getStato()))) {
            return Boolean.TRUE;
        }
    	// Condizione per movimentazione da PAGAMENTO_AUTORIZZATO (SALDO DISACCOPPIATO) a RICHIESTO (INTEGRAZIONE DISACCOPPIATO) IDU-EVO-26-01 Avvio Istruttoria di integrazione
    	if (TipoProcesso.INTEGRAZIONE_ISTRUTTORIE.equals(tipoProcesso) && StatoIstruttoria.PAGAMENTO_AUTORIZZATO.equals(istruttoriaModel.getStato())) {
            return Boolean.TRUE;
        }
    	if (statiFinali.contains(istruttoriaModel.getA4gdStatoLavSostegno().getIdentificativo())) {
            logger.warn("Accesso negato: tentata modifica istruttoria con id {} e tipologia {}",
                    istruttoriaModel.getId(),
                    istruttoriaModel.getTipologia());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    };
  

}




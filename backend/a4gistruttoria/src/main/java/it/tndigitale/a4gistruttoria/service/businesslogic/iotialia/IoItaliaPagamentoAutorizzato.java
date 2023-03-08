package it.tndigitale.a4gistruttoria.service.businesslogic.iotialia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegno;
import it.tndigitale.a4gistruttoria.component.CaricaPremioCalcolatoSostegnoFactory;
import it.tndigitale.a4gistruttoria.dto.IoItaliaMessage;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.NumberUtil;

@Component(IoItaliaPagamentoAutorizzato.NOME_QUALIFICATORE)
public class IoItaliaPagamentoAutorizzato extends IoItaliaMessaggioByStato {
	
	private static final String PROP_MESS_PREFIX="pagamento-autorizzato";
	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "PAGAMENTO_AUTORIZZATO";
	
	@Autowired
	private CaricaPremioCalcolatoSostegnoFactory premioCalcolatoSostegnoFactory;
	
	@Override
	protected void buildMessage(IoItaliaMessage ioItaliaMessage, IstruttoriaModel istruttoriaModel) {
		CaricaPremioCalcolatoSostegno premioLoader =
				premioCalcolatoSostegnoFactory.getCaricatorePremioBySostegno(CaricaPremioCalcolatoSostegno.getNomeQualificatore(istruttoriaModel.getSostegno()));
		ioItaliaMessage.setMessaggio(
			String.format(ioItaliaMessage.getMessaggio(), 
					LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),//Il giorno <gg/mm/aaaa> 
					istruttoriaModel.getTipologia().name(),//<TIPO PAGAMENTO>
					istruttoriaModel.getSostegno().name(),// <DESCRIZIONE SOSTEGNO>
					istruttoriaModel.getDomandaUnicaModel().getCampagna(),//<CAMPAGNA DI RIFERIMENTO> 
					istruttoriaModel.getDomandaUnicaModel().getCuaaIntestatario(),//<CUAA>
					istruttoriaModel.getDomandaUnicaModel().getRagioneSociale(),//<DESCRIZIONE IMPRESA>
					istruttoriaModel.getDomandaUnicaModel().getNumeroDomanda(),//<NUMERO DOMANDA>
					NumberUtil.format(premioLoader.getPremio(istruttoriaModel)),// <VALORE AUTORIZZATO>
					istruttoriaModel.getDomandaUnicaModel().getIban()//<IBAN>
					)
		);
	}
	
	@Override
	protected String getPropPrefix() {
		return PROP_MESS_PREFIX;
	}

	@Override
	protected Boolean checkInvio(IstruttoriaModel istruttoriaModel) {
		return Boolean.TRUE;
	}

}

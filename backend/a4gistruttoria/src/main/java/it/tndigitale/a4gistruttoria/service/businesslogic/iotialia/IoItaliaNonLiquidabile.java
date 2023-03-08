package it.tndigitale.a4gistruttoria.service.businesslogic.iotialia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.IoItaliaMessage;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

@Component(IoItaliaNonLiquidabile.NOME_QUALIFICATORE)
public class IoItaliaNonLiquidabile extends IoItaliaMessaggioByStato {
	private static final String PROP_MESS_PREFIX="non-liquidabile";
	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "NON_LIQUIDABILE";
	
	@Override
	protected void buildMessage(IoItaliaMessage ioItaliaMessage, IstruttoriaModel istruttoriaModel) {
		
		ioItaliaMessage.setMessaggio(
			String.format(ioItaliaMessage.getMessaggio(), 
					LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),//Il giorno <gg/mm/aaaa> 
					istruttoriaModel.getSostegno().name(),// <DESCRIZIONE SOSTEGNO>
					istruttoriaModel.getDomandaUnicaModel().getCampagna(),//<CAMPAGNA DI RIFERIMENTO> 
					istruttoriaModel.getDomandaUnicaModel().getCuaaIntestatario(),//<CUAA>
					istruttoriaModel.getDomandaUnicaModel().getRagioneSociale(),//<DESCRIZIONE IMPRESA>
					istruttoriaModel.getDomandaUnicaModel().getNumeroDomanda()//<NUMERO DOMANDA>
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

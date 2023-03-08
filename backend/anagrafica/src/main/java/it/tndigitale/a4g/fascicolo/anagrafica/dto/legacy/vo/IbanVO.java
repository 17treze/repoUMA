package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ModoPagamentoModel;

public class IbanVO implements SQLData {

	private String codiceFiscale;
	private String deModPagamento;
	private String codIban;
	private String codPaese;
	private String checkDigit;
	private String codCin;
	private String codAbi;
	private String codCab;
	private String conto;
	private String deBanca;
	private String deFiliale;
	private String fgDefault;
	private Date dtInizio;
	private Date dtFine;

	private String sql_type = "AGS_IBAN";

	public String getSQLTypeName() {
		return sql_type;
	}

	public void readSQL(SQLInput stream, String type)
			throws SQLException {
		sql_type = type;
		codiceFiscale = stream.readString();
		deModPagamento = stream.readString();
		codIban = stream.readString();
		codPaese = stream.readString();
		checkDigit = stream.readString();
		codCin = stream.readString();
		codAbi = stream.readString();
		codCab = stream.readString();
		conto = stream.readString();
		deBanca = stream.readString();
		deFiliale = stream.readString();
		fgDefault = stream.readString();
		dtInizio = stream.readDate();
		dtFine = stream.readDate();
	}

	public void writeSQL(SQLOutput stream)
			throws SQLException {
		stream.writeString(codiceFiscale);
		stream.writeString(deModPagamento);
		stream.writeString(codIban);
		stream.writeString(codPaese);
		stream.writeString(checkDigit);
		stream.writeString(codCin);
		stream.writeString(codAbi);
		stream.writeString(codCab);
		stream.writeString(conto);
		stream.writeString(deBanca);
		stream.writeString(deFiliale);
		stream.writeString(fgDefault);
		stream.writeDate(dtInizio);
		stream.writeDate(dtFine);
	}

	public static List<IbanVO> mapper(FascicoloModel input, String cuaa) {
		if(input == null) {
			return null;
		}
		List<ModoPagamentoModel> modoPagamentoList = input.getModoPagamentoList();
		if (modoPagamentoList == null || modoPagamentoList.isEmpty()) {
			return null;
		}


		List<IbanVO> dto = new ArrayList<>();
		modoPagamentoList.forEach(modoPagamento -> {
			IbanVO iban = new IbanVO();
			iban.codiceFiscale = cuaa;
			iban.codIban = modoPagamento.getIban();
			iban.deModPagamento = modoPagamento.getDenominazioneIstituto();

			dto.add(iban);
		});

		return dto;
	}
}

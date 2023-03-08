package it.tndigitale.a4g.zootecnia.dto.legacy.vo;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import it.tndigitale.a4g.zootecnia.business.persistence.entity.AllevamentoModel;

public class AllevamentoVO implements SQLData {

	private Date dtInsert;
	private Date dtDelete;
	private Long idAllevamento;
	private String codIdAziendale;
	private String codIdBdn;
	private String codSpecie;
	private String codProdAllev;
	private String comune;
	private String tipoProduzione;
	private String localita;
	private String indirizzo;
	private String cap;
	private String fgSoccida;
	private String fgAutLatte;
	private Date dtInizio;
	private Date dtFine;
	private String codFiscPropr;
	private String denomPropr;
	private String codFiscDetent;
	private String denomDetent;

	private String sql_type = "AGS_ALLEVAMENTO";

	public String getSQLTypeName() {
		return sql_type;
	}

	public void readSQL(SQLInput stream, String type)
			throws SQLException {
		sql_type = type;
		dtInsert = stream.readDate();
		dtDelete = stream.readDate();
		idAllevamento = stream.readLong();
		codIdAziendale = stream.readString();
		codIdBdn = stream.readString();
		codSpecie = stream.readString();
		codProdAllev = stream.readString();
		comune = stream.readString();
		tipoProduzione = stream.readString();
		localita = stream.readString();
		indirizzo = stream.readString();
		cap = stream.readString();
		fgSoccida = stream.readString();
		fgAutLatte = stream.readString();
		dtInizio = stream.readDate();
		dtFine = stream.readDate();
		codFiscPropr = stream.readString();
		denomPropr = stream.readString();
		codFiscDetent = stream.readString();
		denomDetent = stream.readString();
	}

	public void writeSQL(SQLOutput stream)
			throws SQLException {
		stream.writeDate(dtInsert);
		stream.writeDate(dtDelete);
		stream.writeLong(idAllevamento);
		stream.writeString(codIdAziendale);
		stream.writeString(codIdBdn);
		stream.writeString(codSpecie);
		stream.writeString(codProdAllev);
		stream.writeString(comune);
		stream.writeString(tipoProduzione);
		stream.writeString(localita);
		stream.writeString(indirizzo);
		stream.writeString(cap);
		stream.writeString(fgSoccida);
		stream.writeString(fgAutLatte);
		stream.writeDate(dtInizio);
		stream.writeDate(dtFine);
		stream.writeString(codFiscPropr);
		stream.writeString(denomPropr);
		stream.writeString(codFiscDetent);
		stream.writeString(denomDetent);
	}

	public static List<AllevamentoVO> mapper(List<AllevamentoModel> allevamenti) {

		List<AllevamentoVO> dto = new ArrayList<>();

		allevamenti.forEach(item -> {
			var allevamento = new AllevamentoVO();

			allevamento.dtInsert = Date.valueOf(item.getDtInizioDetenzione());
			if(item.getDtFineDetenzione() != null) {
				allevamento.dtDelete = Date.valueOf(item.getDtFineDetenzione());
			}
			allevamento.idAllevamento = Long.valueOf(item.getIdentificativo());
			allevamento.codIdAziendale = item.getStrutturaAllevamento().getIdentificativo();
			allevamento.codIdBdn = item.getIdentificativo();
			allevamento.codSpecie = item.getSpecie();
			allevamento.codProdAllev = item.getOrientamentoProduttivo();
			allevamento.comune = item.getStrutturaAllevamento().getComune();
			allevamento.tipoProduzione = item.getTipologiaProduzione();
			allevamento.localita = item.getStrutturaAllevamento().getLocalita();
			allevamento.indirizzo = item.getStrutturaAllevamento().getIndirizzo();
			allevamento.cap = item.getStrutturaAllevamento().getCap();
			allevamento.fgSoccida = item.getSoccida();
			allevamento.fgAutLatte = item.getAutorizzazioneSanitariaLatte();
			allevamento.dtInizio = Date.valueOf(item.getDtAperturaAllevamento());
			if(item.getDtChiusuraAllevamento() != null) {
				allevamento.dtFine = Date.valueOf(item.getDtChiusuraAllevamento());
			}
			allevamento.codFiscPropr = item.getCfProprietario();
			allevamento.denomPropr = item.getDenominazioneProprietario();
			allevamento.codFiscDetent = item.getCfDetentore();
			allevamento.denomDetent = item.getDenominazioneDetentore();

			dto.add(allevamento);
		});

		return dto;
	}

}

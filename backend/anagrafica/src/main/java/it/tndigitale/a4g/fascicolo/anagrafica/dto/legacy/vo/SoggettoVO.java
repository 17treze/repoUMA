package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.List;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.*;

public class SoggettoVO implements SQLData {

	private String codiceFiscale;
	private Date dataInizioVal;
	private Date dataFineVal;
	private String naturaGiuridica;
	private PersonaFisicaVO datiPersFisica;
	private PersonaGiuridicaVO datiPersGiuridica;

	private String sql_type = "AGS_SOGGETTO";

	public String getSQLTypeName() {
		return sql_type;
	}

	public void readSQL(SQLInput stream, String type)
			throws SQLException {
		sql_type = type;
		codiceFiscale = stream.readString();
		dataInizioVal = stream.readDate();
		dataFineVal = stream.readDate();
		naturaGiuridica = stream.readString();
		datiPersFisica = (PersonaFisicaVO)stream.readObject();
		datiPersGiuridica = (PersonaGiuridicaVO)stream.readObject();
	}

	public void writeSQL(SQLOutput stream)
			throws SQLException {
		stream.writeString(codiceFiscale);
		stream.writeDate(dataInizioVal);
		stream.writeDate(dataFineVal);
		stream.writeString(naturaGiuridica);
		stream.writeObject(datiPersFisica);
		stream.writeObject(datiPersGiuridica);
	}

	public static SoggettoVO mapper(FascicoloModel fascicoloModel, PersonaFisicaModel personaFisica, List<CaricaModel> cariche, String cuaa, Connection connection) {
		if(fascicoloModel == null || personaFisica == null) {
			return null;
		}

		SoggettoVO dto = new SoggettoVO();

		dto.codiceFiscale = cuaa;
		if (fascicoloModel.getDataApertura() != null) {
			dto.dataInizioVal = Date.valueOf(fascicoloModel.getDataApertura());
		}
		dto.datiPersFisica = PersonaFisicaVO.mapper(personaFisica, cariche, cuaa, connection);

		return dto;
	}

	public static SoggettoVO mapperSenzaFascicolo(String cuaa, PersonaFisicaConCaricaModel personaFisicaConCarica, final List<CaricaModel> cariche, Connection connection) {
		SoggettoVO dto = new SoggettoVO();

		dto.codiceFiscale = personaFisicaConCarica.getCodiceFiscale();
		dto.datiPersFisica = PersonaFisicaVO.mapperSenzaFascicolo(personaFisicaConCarica, cariche, cuaa, connection);

		return dto;
	}

	public static SoggettoVO mapper(FascicoloModel fascicoloModel, PersonaGiuridicaModel personaGiuridica, String cuaa) {
		if(fascicoloModel == null || personaGiuridica == null) {
			return null;
		}

		SoggettoVO dto = new SoggettoVO();

		dto.codiceFiscale = cuaa;
		if (fascicoloModel.getDataApertura() != null) {
			dto.dataInizioVal = Date.valueOf(fascicoloModel.getDataApertura());
		}
		dto.naturaGiuridica = personaGiuridica.getFormaGiuridica();
		dto.datiPersGiuridica = PersonaGiuridicaVO.mapper(personaGiuridica, cuaa);

		return dto;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
}

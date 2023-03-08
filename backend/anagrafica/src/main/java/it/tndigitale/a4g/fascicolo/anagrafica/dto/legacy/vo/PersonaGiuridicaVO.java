package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IndirizzoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SedeModel;

public class PersonaGiuridicaVO implements SQLData {

	private String codiceFiscale;
	private String partIva;
	private String denominazione;
	private String siglaProvSede;
	private String localitaSede;
	private String capSede;
	private String indirizzoSede;
	private String telefonoSede;
	private String faxSede;
	private String cellulareSede;
	private String emailSede;
	private String codIstatRecapito;
	private String localitaRecapito;
	private String capRecapito;
	private String indirizzoRecapito;
	private String telefonoRecapito;
	private String faxRecapito;
	private String cellulareRecapito;
	private String emailRecapito;
	private String codiceFiscaleRl;

	private String sql_type = "AGS_PERSONA_GIURIDICA";

	public String getSQLTypeName() {
		return sql_type;
	}

	public void readSQL(SQLInput stream, String type)
			throws SQLException {
		sql_type = type;
		codiceFiscale = stream.readString();
		partIva = stream.readString();
		denominazione = stream.readString();
		siglaProvSede = stream.readString();
		localitaSede = stream.readString();
		capSede = stream.readString();
		indirizzoSede = stream.readString();
		telefonoSede = stream.readString();
		faxSede = stream.readString();
		cellulareSede = stream.readString();
		emailSede = stream.readString();
		codIstatRecapito = stream.readString();
		localitaRecapito = stream.readString();
		capRecapito = stream.readString();
		indirizzoRecapito = stream.readString();
		telefonoRecapito = stream.readString();
		faxRecapito = stream.readString();
		cellulareRecapito = stream.readString();
		emailRecapito = stream.readString();
		codiceFiscaleRl = stream.readString();
	}

	public void writeSQL(SQLOutput stream)
			throws SQLException {
		stream.writeString(codiceFiscale);
		stream.writeString(partIva);
		stream.writeString(denominazione);
		stream.writeString(siglaProvSede);
		stream.writeString(localitaSede);
		stream.writeString(capSede);
		stream.writeString(indirizzoSede);
		stream.writeString(telefonoSede);
		stream.writeString(faxSede);
		stream.writeString(cellulareSede);
		stream.writeString(emailSede);
		stream.writeString(codIstatRecapito);
		stream.writeString(localitaRecapito);
		stream.writeString(capRecapito);
		stream.writeString(indirizzoRecapito);
		stream.writeString(telefonoRecapito);
		stream.writeString(faxRecapito);
		stream.writeString(cellulareRecapito);
		stream.writeString(emailRecapito);
		stream.writeString(codiceFiscaleRl);
	}

	public static PersonaGiuridicaVO mapper(PersonaGiuridicaModel personaGiuridica, String cuaa) {
		if(personaGiuridica == null) {
			return null;
		}

		SedeModel sedeLegale = personaGiuridica.getSedeLegale();

		PersonaGiuridicaVO dto = new PersonaGiuridicaVO();
		dto.codiceFiscale = cuaa;
		dto.partIva = personaGiuridica.getPartitaIVA();
		dto.denominazione = personaGiuridica.getDenominazione();
		/*
		 * le informazioni *Sede sono settate con i dati provenienti da Anagrafe Tributaria
		 * le informazioni *Recapito sono settate con i dati provenienti da Camera di Commercio
		 */
		if (sedeLegale != null) {
			dto.telefonoSede = sedeLegale.getTelefono();
			dto.telefonoRecapito = sedeLegale.getTelefono();
			dto.emailSede = sedeLegale.getPec();
			dto.emailRecapito = sedeLegale.getPec();
			IndirizzoModel indirizzo = sedeLegale.getIndirizzo();
			if (indirizzo != null) {
				dto.capSede = indirizzo.getCap();
				dto.siglaProvSede = indirizzo.getProvincia();
				dto.indirizzoSede = indirizzo.getDescrizioneEstesa();
				dto.localitaSede = indirizzo.getComune();
			}
			IndirizzoModel indirizzoCameraCommercio = sedeLegale.getIndirizzoCameraCommercio();
			if (indirizzoCameraCommercio != null) {
				dto.capRecapito = indirizzoCameraCommercio.getCap();
				dto.codIstatRecapito = indirizzoCameraCommercio.getCodiceIstat();
				dto.indirizzoRecapito = indirizzoCameraCommercio.getDescrizioneEstesa();
				dto.localitaRecapito = indirizzoCameraCommercio.getComune();
			}
		}
		dto.codiceFiscaleRl = personaGiuridica.getCodiceFiscaleRappresentanteLegale();

		return dto;
	}
}

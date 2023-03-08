package it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.vo;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.*;

public class PersonaFisicaVO implements SQLData {

	private String codiceFiscale;
	private String partIva;
	private String cognome;
	private String nome;
	private String sesso;
	private Date dtNascita;
	private Date dtMorte;
	private String siglaProvNascita;
	private String comuneNascita;
	private String siglaProvResidenza;
	private String localitaResidenza;
	private String capResidenza;
	private String indirizzoResidenza;
	private String telefonoResidenza;
	private String faxResidenza;
	private String cellulareResidenza;
	private String emailResidenza;
	private String codIstatDomicilio;
	private String localitaDomicilio;
	private String capDomicilio;
	private String indirizzoDomicilio;
	private String telefonoDomicilio;
	private String faxDomicilio;
	private String cellulareDomicilio;
	private String emailDomicilio;
	private String fgResponsabile;
	private List<RuoloVO> ruoli;
	private String codScolarizzazione;
	private String scoScolarizzazione;

	private String sql_type = "AGS_PERSONA_FISICA";
	private Connection connection;

	public String getSQLTypeName() {
		return sql_type;
	}

	public void readSQL(SQLInput stream, String type)
			throws SQLException {
		sql_type = type;
		codiceFiscale = stream.readString();
		partIva = stream.readString();
		cognome = stream.readString();
		nome = stream.readString();
		sesso = stream.readString();
		dtNascita = stream.readDate();
		dtMorte = stream.readDate();
		siglaProvNascita = stream.readString();
		comuneNascita = stream.readString();
		siglaProvResidenza = stream.readString();
		localitaResidenza = stream.readString();
		capResidenza = stream.readString();
		indirizzoResidenza = stream.readString();
		telefonoResidenza = stream.readString();
		faxResidenza = stream.readString();
		cellulareResidenza = stream.readString();
		emailResidenza = stream.readString();
		codIstatDomicilio = stream.readString();
		localitaDomicilio = stream.readString();
		capDomicilio = stream.readString();
		indirizzoDomicilio = stream.readString();
		telefonoDomicilio = stream.readString();
		faxDomicilio = stream.readString();
		cellulareDomicilio = stream.readString();
		emailDomicilio = stream.readString();
		fgResponsabile = stream.readString();
		// https://stackoverflow.com/questions/25619803/java-sqldata-cast-to-user-object-with-a-list-array
		Array ruoliArray = stream.readArray();
		ruoli = new ArrayList<RuoloVO>();
		for (Object obj : (Object[])ruoliArray.getArray()) {
			ruoli.add((RuoloVO)obj);
		}
		codScolarizzazione = stream.readString();
		scoScolarizzazione = stream.readString();
	}

	public void writeSQL(SQLOutput stream)
			throws SQLException {
		stream.writeString(codiceFiscale);
		stream.writeString(partIva);
		stream.writeString(cognome);
		stream.writeString(nome);
		stream.writeString(sesso);
		stream.writeDate(dtNascita);
		stream.writeDate(dtMorte);
		stream.writeString(siglaProvNascita);
		stream.writeString(comuneNascita);
		stream.writeString(siglaProvResidenza);
		stream.writeString(localitaResidenza);
		stream.writeString(capResidenza);
		stream.writeString(indirizzoResidenza);
		stream.writeString(telefonoResidenza);
		stream.writeString(faxResidenza);
		stream.writeString(cellulareResidenza);
		stream.writeString(emailResidenza);
		stream.writeString(codIstatDomicilio);
		stream.writeString(localitaDomicilio);
		stream.writeString(capDomicilio);
		stream.writeString(indirizzoDomicilio);
		stream.writeString(telefonoDomicilio);
		stream.writeString(faxDomicilio);
		stream.writeString(cellulareDomicilio);
		stream.writeString(emailDomicilio);
		stream.writeString(fgResponsabile);
		if (connection != null && ruoli != null && !ruoli.isEmpty()) {
			var ruoliArray = connection.unwrap(oracle.jdbc.OracleConnection.class).createOracleArray("AGS_RUOLI", ruoli.toArray());
			stream.writeArray(ruoliArray);
		}
		stream.writeString(codScolarizzazione);
		stream.writeString(scoScolarizzazione);
	}

	public static PersonaFisicaVO mapper(PersonaFisicaModel personaFisica, List<CaricaModel> cariche, String cuaa, Connection connection) {
		if(personaFisica == null) {
			return null;
		}

		IndirizzoModel domicilioFiscale = personaFisica.getDomicilioFiscale();
		ImpresaIndividualeModel impresaIndividuale = personaFisica.getImpresaIndividuale();

		PersonaFisicaVO dto = new PersonaFisicaVO();
		dto.connection = connection;
		dto.codiceFiscale = cuaa;
		dto.cognome = personaFisica.getCognome();
		dto.nome = personaFisica.getNome();
		dto.sesso = "" + personaFisica.getSesso().toString().charAt(0);
		dto.dtNascita = Date.valueOf(personaFisica.getDataNascita());
		dto.comuneNascita = personaFisica.getComuneNascita();
		dto.siglaProvNascita = personaFisica.getProvinciaNascita();
		/*
		 * le informazioni *Residenza sono settate con i dati provenienti da Anagrafe Tributaria
		 * le informazioni *Domicilio sono settate con i dati provenienti da Camera di Commercio
		 */
		if (domicilioFiscale != null) {
			dto.siglaProvResidenza = domicilioFiscale.getProvincia();
			dto.localitaResidenza = domicilioFiscale.getComune();
			dto.capResidenza = domicilioFiscale.getCap();
			dto.indirizzoResidenza = domicilioFiscale.getDescrizioneEstesa();
			dto.emailResidenza = personaFisica.getPec();
		}
		if (impresaIndividuale != null) {
			dto.partIva = impresaIndividuale.getPartitaIVA();
			SedeModel sedeLegale = impresaIndividuale.getSedeLegale();
			if (sedeLegale != null) {
				dto.emailDomicilio = sedeLegale.getPec();
				dto.telefonoDomicilio = sedeLegale.getTelefono();
				IndirizzoModel indirizzoCameraCommercio = sedeLegale.getIndirizzoCameraCommercio();
				if (indirizzoCameraCommercio != null) {
					dto.codIstatDomicilio = indirizzoCameraCommercio.getCodiceIstat();
					dto.localitaDomicilio = indirizzoCameraCommercio.getComune();
					dto.capDomicilio = indirizzoCameraCommercio.getCap();
					dto.indirizzoDomicilio = indirizzoCameraCommercio.getDescrizioneEstesa();
				}
			}
		}
		if (cariche != null && !cariche.isEmpty()) {
			List<RuoloVO> output = new ArrayList<>();
			cariche.forEach(carica -> {
//				carica del detentore del fascicolo
				if (carica.getPersonaFisicaConCaricaModel().getCodiceFiscale().equalsIgnoreCase(cuaa)) {
					output.add(RuoloVO.mapper(carica, cuaa));
				}
			});
			dto.ruoli = output;
		}
		return dto;
	}

	public static PersonaFisicaVO mapperSenzaFascicolo(PersonaFisicaConCaricaModel personaFisicaConCarica, final List<CaricaModel> cariche, String cuaa, Connection connection) {

		IndirizzoModel domicilio = personaFisicaConCarica.getIndirizzo();

		var dto = new PersonaFisicaVO();
		dto.connection = connection;
		dto.codiceFiscale = personaFisicaConCarica.getCodiceFiscale();
		dto.cognome = personaFisicaConCarica.getCognome();
		dto.nome = personaFisicaConCarica.getNome();
		dto.sesso = "" + personaFisicaConCarica.getSesso().toString().charAt(0);
		dto.dtNascita = Date.valueOf(personaFisicaConCarica.getDataNascita());
		dto.comuneNascita = personaFisicaConCarica.getComuneNascita();
		dto.siglaProvNascita = personaFisicaConCarica.getProvinciaNascita();
		/*
		 * le informazioni *Residenza sono settate con i dati provenienti da Anagrafe Tributaria
		 * le informazioni *Domicilio sono settate con i dati provenienti da Camera di Commercio
		 */
		if (domicilio != null) {
			dto.siglaProvResidenza = domicilio.getProvincia();
			dto.localitaResidenza = domicilio.getComune();
			dto.capResidenza = domicilio.getCap();
			dto.indirizzoResidenza = domicilio.getDescrizioneEstesa();
		}
		List<RuoloVO> ruoloOutput = new ArrayList<>();
		for (CaricaModel caricaModel: cariche) {
			if (personaFisicaConCarica.getCodiceFiscale().equalsIgnoreCase(caricaModel.getPersonaFisicaConCaricaModel().getCodiceFiscale())) {
				ruoloOutput.add(RuoloVO.mapper(caricaModel, cuaa));
				dto.ruoli = ruoloOutput;
			}
		}
		return dto;
	}
}

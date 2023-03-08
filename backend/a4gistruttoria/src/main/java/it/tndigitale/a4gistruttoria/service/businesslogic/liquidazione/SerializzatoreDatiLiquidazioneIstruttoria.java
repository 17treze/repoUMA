package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.Beneficiario;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.Beneficiario.Sesso;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.DatiLiquidazioneIstruttoria.VoceSpesa;
import it.tndigitale.a4gistruttoria.util.TipoCampoElencoLiquidazione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializzatoreDatiLiquidazioneIstruttoria {
	private static final Logger logger = LoggerFactory.getLogger(SerializzatoreDatiLiquidazioneIstruttoria.class);

	
	public static String trasforma(DatiLiquidazioneIstruttoria datiIstruttoria) {
		StringBuilder sb = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); 
		insertCampoCostante(sb, CampoElencoLiquidazione.TIPO_RECORD);
		sb.insert(CampoElencoLiquidazione.PROGRESSIVO_RECORD.getStart(), fillField(CampoElencoLiquidazione.PROGRESSIVO_RECORD, String.valueOf(datiIstruttoria.getProgressivo())));
		insertCampoCostante(sb, CampoElencoLiquidazione.SETTORE);
		insertCampo(sb, CampoElencoLiquidazione.ANNO_CAMPAGNA, datiIstruttoria.getAnnoCampagna().toString());
		insertCampo(sb, CampoElencoLiquidazione.TIPO_PAGAMENTO, datiIstruttoria.getTipoPagamento().getCodice());
		insertCampo(sb, CampoElencoLiquidazione.DATA_PROTOCOLLO, formatter.format(datiIstruttoria.getDataProtocollazione()));
		insertCampo(sb, CampoElencoLiquidazione.NUMERO_DOMANDA, formatNumeroDomanda(datiIstruttoria));
		
		creaBeneficiario(datiIstruttoria.getBeneficiario(), sb);
		
		insertCampo(sb, CampoElencoLiquidazione.IMPORTO_TOTALE, valueToImporto(datiIstruttoria.getImportoTotale()));		

		insertVoceSpesa1(sb, datiIstruttoria.getVoce1());
		insertVoceSpesa2(sb, datiIstruttoria.getVoce2());
		insertVoceSpesa3(sb, datiIstruttoria.getVoce3());
		insertVoceSpesa4(sb, datiIstruttoria.getVoce4());
		insertVoceSpesa5(sb, datiIstruttoria.getVoce5());
		insertVoceSpesa6(sb, datiIstruttoria.getVoce6());
		insertVoceSpesa7(sb, datiIstruttoria.getVoce7());
		insertVoceSpesa8(sb, datiIstruttoria.getVoce8());
		insertVoceSpesa9(sb, datiIstruttoria.getVoce9());
		insertVoceSpesa10(sb, datiIstruttoria.getVoce10());
		insertVoceSpesa11(sb, datiIstruttoria.getVoce11());
		insertVoceSpesa12(sb, datiIstruttoria.getVoce12());
		insertVoceSpesa13(sb, datiIstruttoria.getVoce13());
		insertVoceSpesa14(sb, datiIstruttoria.getVoce14());

		insertCampo(sb, CampoElencoLiquidazione.NOTE_BENEFICIARIO, "");
		insertCampo(sb, CampoElencoLiquidazione.SETTORE_EFFETTIVO, CampoElencoLiquidazione.SETTORE.getCostante());
		insertCampo(sb, CampoElencoLiquidazione.IDENTIFICATIVO_ELENCO, String.valueOf(datiIstruttoria.getIdentificativoElenco()));
		insertCampo(sb, CampoElencoLiquidazione.CIG, "");
		insertCampo(sb, CampoElencoLiquidazione.CUP, "");
		insertCampo(sb, CampoElencoLiquidazione.FILLER, "");
		return sb.toString();
	}
	
	protected static void insertVoceSpesa1(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS1_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS1_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS1_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS1_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa2(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS2_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS2_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS2_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS2_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa3(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS3_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS3_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS3_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS3_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa4(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS4_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS4_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS4_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS4_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa5(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS5_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS5_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS5_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS5_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa6(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS6_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS6_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS6_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS6_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa7(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS7_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS7_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS7_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS7_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa8(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS8_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS8_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS8_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS8_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa9(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS9_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS9_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS9_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS9_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa10(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS10_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS10_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS10_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS10_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa11(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS11_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS11_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS11_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS11_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa12(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS12_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS12_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS12_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS12_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa13(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS13_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS13_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS13_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS13_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}

	protected static void insertVoceSpesa14(StringBuilder sb, VoceSpesa voceSpesa) {
		insertCampo(sb, CampoElencoLiquidazione.VS14_CODICE_PRODOTTO, valueToCodiceProdotto(voceSpesa.getCodiceProdotto()));
		insertCampo(sb, CampoElencoLiquidazione.VS14_CAPITOLO_SPESA, voceSpesa.getCapitolo());
		insertCampo(sb, CampoElencoLiquidazione.VS14_QUANTITA, valueToQuantita(voceSpesa.getQuantita()));
		insertCampo(sb, CampoElencoLiquidazione.VS14_IMPORTO, valueToImporto(voceSpesa.getImporto()));
	}
	
	protected static void insertCampo(StringBuilder sb, CampoElencoLiquidazione campo, BigDecimal valore) {
		String formatValore = formatNumber(valore);
		insertCampo(sb, campo, formatValore);
	}

	protected static void insertCampo(StringBuilder sb, CampoElencoLiquidazione campo, Double valore) {
		String formatValore = formatNumber(valore);
		insertCampo(sb, campo, formatValore);
	}

	protected static void insertCampo(StringBuilder sb, CampoElencoLiquidazione campo, String valore) {
		sb.insert(campo.getStart(), fillField(campo, valore));
	}
	
	protected static void insertCampoCostante(StringBuilder sb, CampoElencoLiquidazione campo) {
		sb.insert(campo.getStart(), campo.getCostante());
	}

	protected static String formatNumber(Number importo) {
		String valore = importo != null ? String.valueOf(importo) : "";
		return valore.replace(".", "");
	}
	
	private static String valueToCodiceProdotto(Double codiceProdotto) {
		return Optional.ofNullable(codiceProdotto).map(Double::intValue).map(c -> c.toString()).orElseGet(() -> "");
	}
	
	private static String valueToImporto(Double value) {
		BigDecimal importo = Optional.ofNullable(value).map(BigDecimal::valueOf).orElse(BigDecimal.ZERO);
		importo = importo.setScale(2, BigDecimal.ROUND_CEILING);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingUsed(false);
		char decSep = df.getDecimalFormatSymbols().getDecimalSeparator();
		return df.format(importo).replace(Character.toString(decSep), "");
	}

	private static String valueToQuantita(Double value) {
		BigDecimal quantita = Optional.ofNullable(value).map(BigDecimal::valueOf).orElse(BigDecimal.ZERO);
		quantita = quantita.setScale(4, BigDecimal.ROUND_CEILING);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);
		df.setGroupingUsed(false);
		char decSep = df.getDecimalFormatSymbols().getDecimalSeparator();
		String svalue = df.format(quantita).replace(Character.toString(decSep), "");
		return svalue;
	}

	protected static void creaBeneficiario(Beneficiario beneficiario,StringBuilder sb) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			sb.insert(CampoElencoLiquidazione.RAGIONE_SOCIALE.getStart(),
					beneficiario.getRagioneSociale().length() > CampoElencoLiquidazione.RAGIONE_SOCIALE.getDimensione()
							? beneficiario.getRagioneSociale().substring(0, CampoElencoLiquidazione.RAGIONE_SOCIALE.getDimensione())
							: fillField(CampoElencoLiquidazione.RAGIONE_SOCIALE, beneficiario.getRagioneSociale()));
			sb.insert(CampoElencoLiquidazione.COGNOME_BENEFICIARIO.getStart(),
					fillField(CampoElencoLiquidazione.COGNOME_BENEFICIARIO, beneficiario.getCognome() != null ? beneficiario.getCognome() : "NA"));
			sb.insert(CampoElencoLiquidazione.NOME_BENEFICIARIO.getStart(),
					fillField(CampoElencoLiquidazione.NOME_BENEFICIARIO, beneficiario.getNome() != null ? beneficiario.getNome() : "NA"));
			creaSesso(beneficiario.getSesso(), sb);
			sb.insert(CampoElencoLiquidazione.DATA_NASCITA.getStart(), beneficiario.getDataNascita() != null ? formatter.format(beneficiario.getDataNascita()) : "00000000");
			if (beneficiario != null && beneficiario.getIstatComuneNascita() != null)
				sb.insert(CampoElencoLiquidazione.ISTAT_COMUNE_NASCITA.getStart(),
						beneficiario.getIstatComuneNascita().length() == CampoElencoLiquidazione.ISTAT_COMUNE_NASCITA.getDimensione()
								? beneficiario.getIstatComuneNascita()
								: new StringBuilder().append("0").append(beneficiario.getIstatComuneNascita()));
			else
				sb.insert(CampoElencoLiquidazione.ISTAT_COMUNE_NASCITA.getStart(), fillField(CampoElencoLiquidazione.ISTAT_COMUNE_NASCITA, ""));
			sb.insert(CampoElencoLiquidazione.SIGLA_PROVINCIA_NASCITA.getStart(),
					fillField(CampoElencoLiquidazione.SIGLA_PROVINCIA_NASCITA, beneficiario.getSiglaProvinciaNascita() != null ? beneficiario.getSiglaProvinciaNascita() : ""));
			sb.insert(CampoElencoLiquidazione.CODICE_FISCALE.getStart(), fillField(CampoElencoLiquidazione.CODICE_FISCALE, beneficiario.getCodiceFiscale()));
			sb.insert(CampoElencoLiquidazione.INDIRIZZO.getStart(),
					fillField(CampoElencoLiquidazione.INDIRIZZO, beneficiario.getIndirizzo() != null ? beneficiario.getIndirizzo() : ""));
			if (beneficiario != null && beneficiario.getIstatComuneRecapito() != null)
				sb.insert(CampoElencoLiquidazione.ISTAT_COMUNE_RECAPITO.getStart(),
						beneficiario.getIstatComuneRecapito().toString().length() == CampoElencoLiquidazione.ISTAT_COMUNE_RECAPITO.getDimensione()
								? beneficiario.getIstatComuneRecapito().toString()
								: new StringBuilder().append("0").append(beneficiario.getIstatComuneRecapito()));
			else
				sb.insert(CampoElencoLiquidazione.ISTAT_COMUNE_RECAPITO.getStart(), fillField(CampoElencoLiquidazione.ISTAT_COMUNE_RECAPITO, ""));
			if (beneficiario != null && beneficiario.getSiglaProvinciaRecapito() != null)
				sb.insert(CampoElencoLiquidazione.SIGLA_PROVINCIA_RECAPITO.getStart(), new StringBuilder().append(" ").append(beneficiario.getSiglaProvinciaRecapito()));
			else
				sb.insert(CampoElencoLiquidazione.SIGLA_PROVINCIA_RECAPITO.getStart(), fillField(CampoElencoLiquidazione.SIGLA_PROVINCIA_RECAPITO, ""));
			sb.insert(CampoElencoLiquidazione.CAP.getStart(), fillField(CampoElencoLiquidazione.CAP, beneficiario.getCap().toString()));
			sb.insert(CampoElencoLiquidazione.MODALITA_PAGAMENTO.getStart(), CampoElencoLiquidazione.MODALITA_PAGAMENTO.getCostante());//sempre 01- bonifico bancario
			sb.insert(CampoElencoLiquidazione.CODICE_ABI.getStart(), beneficiario.getIban().substring(5, 10));
			sb.insert(CampoElencoLiquidazione.CODICE_CAB.getStart(), beneficiario.getIban().substring(10, 15));
			sb.insert(CampoElencoLiquidazione.NUMERO_CONTO.getStart(), fillField(CampoElencoLiquidazione.NUMERO_CONTO, getNumeroConto(beneficiario.getIban())));
		} catch (Throwable e) {
			logger.error("Errore nella generazione dei dati del beneficiario {} con IBAN {}", beneficiario.getCodiceFiscale(), beneficiario.getIban(), e);
			throw e;
		}

	}

	protected static void creaSesso(Sesso sesso,StringBuilder sb) {
		String codiceSesso = "";
		if (sesso != null) {
			switch (sesso) {
			case MASCHIO:
				codiceSesso = "M";
				break;
			case FEMMINA:
				codiceSesso = "F";
				break;

			default:
				break;
			}
		}
		insertCampo(sb, CampoElencoLiquidazione.SESSO, codiceSesso);
	}
	/**
	 * Metodo per la composizione del Numero Conto a partire dall'iban. N.B. NumeroConto = SIGLA_NAZIONE + CODICE_CONTROLLO_INTERNAZIONALE + CODICE_CONTROLLO_NAZIONALE + CONTO_CORRENTE
	 * 
	 * @param iban
	 * @return
	 */
	private static String getNumeroConto(String iban) {
		return iban.substring(0, 5).concat(iban.substring(15, 27));
	}
	
	
	/**
	 * Metodo per la formattazione del campo NumeroDomanda
	 * 
	 * @param domanda
	 * @return
	 */
	private static String formatNumeroDomanda(DatiLiquidazioneIstruttoria datiIstruttoria) {

		StringBuilder sbNumeroDomanda = new StringBuilder();
		sbNumeroDomanda.append(CampoElencoLiquidazione.SETTORE.getCostante()).append(datiIstruttoria.getAnnoCampagna().toString());

		while (sbNumeroDomanda.length() + datiIstruttoria.getNumeroDomanda().toString().length() < CampoElencoLiquidazione.NUMERO_DOMANDA.getDimensione()) {
			sbNumeroDomanda.append("0");
		}

		sbNumeroDomanda.append(datiIstruttoria.getNumeroDomanda());
		return sbNumeroDomanda.toString();
	}
	

	/**
	 * Metodo per la formattazione dei campi al fine di raggiungere la dimensione del campo esatta
	 * 
	 * @param campo
	 * @param valore
	 * @return
	 */
	protected static String fillField(CampoElencoLiquidazione campo, String valore) {

		StringBuilder sb = new StringBuilder();

		// N.B. I campi non valorizzati vanno riempiti di “blank“ se alfanumerici e di “0” se numerici

		if (campo.getTipoCampo().equals(TipoCampoElencoLiquidazione.ALFANUMERICO)) {
			sb.append(valore);
			while (sb.length() < campo.getDimensione()) {
				sb.append(" ");
			}
		} else if (campo.getTipoCampo().equals(TipoCampoElencoLiquidazione.NUMERICO)) {
			// I campi numerici devono contenere l’espansione di zeri a sinistra;
			while (sb.length() + valore.length() < campo.getDimensione()) {
				sb.append("0");
			}
			sb.append(valore);
		} else {
			sb.append(valore);
		}

		return sb.toString();

	}
}

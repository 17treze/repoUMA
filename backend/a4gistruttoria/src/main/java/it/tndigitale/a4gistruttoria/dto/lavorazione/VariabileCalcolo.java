package it.tndigitale.a4gistruttoria.dto.lavorazione;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.tndigitale.a4gistruttoria.util.FormatoVariabile;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.UnitaMisura;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@JsonInclude(Include.NON_EMPTY)
public class VariabileCalcolo {

	private TipoVariabile tipoVariabile;
	private Boolean valBoolean;
	private BigDecimal valNumber;
	private String valString;
	private Date date;
	private ArrayList<ParticellaColtura> valList;
	private HashMap<?, List<ParticellaColtura>> valMap;

	// TODO: togli
	public VariabileCalcolo(TipoVariabile tipoVariabile) {
		super();
		this.tipoVariabile = tipoVariabile;
	}

	public VariabileCalcolo(TipoVariabile tipoVariabile, Boolean valBoolean) {
		this.tipoVariabile = tipoVariabile;
		this.valBoolean = valBoolean;
	}

	public VariabileCalcolo(TipoVariabile tipoVariabile, Date date) {
		this.tipoVariabile = tipoVariabile;
		this.date = date;
	}

	public VariabileCalcolo() {

	}

	public VariabileCalcolo(TipoVariabile tipoVariabile, BigDecimal valNumber) {
		this.tipoVariabile = tipoVariabile;
		setValNumberRounded(valNumber);
	}

	public VariabileCalcolo(TipoVariabile tipoVariabile, Double valNumber) {
		this.tipoVariabile = tipoVariabile;
		BigDecimal valNumberBigDecimal = BigDecimal.valueOf(valNumber);
		setValNumberRounded(valNumberBigDecimal);
	}

	private void setValNumberRounded(BigDecimal valNumber1) {
		FormatoVariabile formato = this.tipoVariabile.getFormato();
		switch (formato) {
		case NUMERO2DECIMALI:
			this.valNumber = valNumber1.setScale(2, RoundingMode.HALF_UP);
			break;
		case NUMERO4DECIMALI:
			this.valNumber = valNumber1.setScale(4, RoundingMode.HALF_UP);
			break;
		case PERCENTUALE:
			this.valNumber = (valNumber1).setScale(4, RoundingMode.HALF_UP); // percentuale fra 0 e 1, + 2 decimali
			break;
		case PERCENTUALE6DECIMALI:
			this.valNumber = (valNumber1).setScale(8, RoundingMode.HALF_UP); // percentuale fra 0 e 1, + 6 decimali
			break;
		default:
			this.valNumber = valNumber1;
			break;
		}
	}

	public VariabileCalcolo(TipoVariabile tipoVariabile, String valString) {
		this.tipoVariabile = tipoVariabile;
		this.valString = valString;
	}

	public VariabileCalcolo(TipoVariabile tipoVariabile, ArrayList<ParticellaColtura> valList) {
		this.tipoVariabile = tipoVariabile;
		this.valList = valList;
	}

	public VariabileCalcolo(TipoVariabile tipoVariabile, HashMap<?, List<ParticellaColtura>> valMap) {
		this.tipoVariabile = tipoVariabile;
		this.valMap = valMap;
	}

	public TipoVariabile getTipoVariabile() {
		return tipoVariabile;
	}

	public Boolean getValBoolean() {
		return valBoolean;
	}

	public void setValBoolean(Boolean valBoolean) {
		this.valBoolean = valBoolean;
	}

	public BigDecimal getValNumber() {
		return valNumber;
	}

	public void setValNumber(BigDecimal valNumber) {
		setValNumberRounded(valNumber);
	}

	public String getValString() {
		return valString;
	}

	public void setValString(String valString) {
		this.valString = valString;
	}

	public ArrayList<ParticellaColtura> getValList() {
		return valList;
	}

	public void setValList(ArrayList<ParticellaColtura> valList) {
		this.valList = valList;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String recuperaValoreString() {
		return recuperaValoreString(false, true);
	}

	public String recuperaValoreString(boolean nullIfO, boolean ettari) {
		String val = "";
		FormatoVariabile formato = this.getTipoVariabile().getFormato();

		switch (formato) {
		case BOOL:
			if (this.getValBoolean() != null)
				val = this.getValBoolean().equals(false) ? "NO" : "SI";
			else
				val = null;
			break;
		case DATE:
			if (this.getDate() != null) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				val = df.format(this.getDate());
			} else
				val = null;
			break;
		case NUMERO2DECIMALI:
		case NUMERO4DECIMALI:
		case NUMEROINTERO:
		case PERCENTUALE:
		case PERCENTUALE6DECIMALI:
			val = recuperaValoreStringNumeric(nullIfO, ettari, this.getTipoVariabile(), this.getValNumber());
			break;
		case STRING:
			val = this.getValString();
			break;
		case LISTA:
			val = "";
			break;
		default:
			val = "";
			break;
		}
		return val;

	}

	public String recuperaValoreString(boolean nullIfO) {
		String val = "";
		FormatoVariabile formato = this.getTipoVariabile().getFormato();

		switch (formato) {
		case BOOL:
			if (this.getValBoolean() != null)
				val = this.getValBoolean().equals(false) ? "NO" : "SI";// d.getValBoolean().equals(new BigDecimal(0)) ? "NO" : "SI";
			else
				val = null;
			break;
		case DATE:
			if (this.getDate() != null) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				val = df.format(this.getDate());
			} else
				val = null;
			break;
		case NUMERO2DECIMALI:
		case NUMERO4DECIMALI:
		case NUMEROINTERO:
		case PERCENTUALE:
		case PERCENTUALE6DECIMALI:
			val = recuperaValoreStringNumeric(nullIfO, true, this.getTipoVariabile(), this.getValNumber());
			break;
		case STRING:
			val = this.getValString();
			break;
		case LISTA:
			val = "";
			break;
		default:
			val = "";
			break;
		}
		return val;
	}

	public static String recuperaValoreStringNumeric(boolean nullIfO, boolean ettari, TipoVariabile tipo, BigDecimal valIn) {
		String val = "";
		FormatoVariabile formato = tipo.getFormato();

		switch (formato) {
		case NUMERO2DECIMALI:
		case NUMERO4DECIMALI:
		case NUMEROINTERO:
			if (valIn != null) {
				if (nullIfO && (valIn.stripTrailingZeros().compareTo((new BigDecimal(0))) == 0)) {
					return null;
				} else {
					if (tipo.getUnitaMisura().equals(UnitaMisura.ETTARI)) {
						if (ettari) {
							val = valIn.stripTrailingZeros().toPlainString() + ' ' + UnitaMisura.ETTARI.getValue();

						} else {
							val = valIn.multiply(new BigDecimal(10000)).stripTrailingZeros().toPlainString() + ' ' + UnitaMisura.METRIQUADRI.getValue();
						}
					} else
						val = valIn.stripTrailingZeros().toPlainString() + ' ' + tipo.getUnitaMisura().getValue();
					return val;
				}
			} else
				val = null;
			break;

		case PERCENTUALE6DECIMALI:
			if (valIn != null)
				val = (valIn.multiply(new BigDecimal(100))).stripTrailingZeros().toPlainString() + ' ' + tipo.getUnitaMisura().getValue();
			else
				val = null;
			break;
		case PERCENTUALE:
			if (valIn != null)
				val = ((valIn.multiply(new BigDecimal(100))).setScale(2)).toPlainString() + ' ' + tipo.getUnitaMisura().getValue();
			else
				val = null;
			break;
		default:
			val = "";
			break;
		}
		return val;
	}

	public HashMap<?, List<ParticellaColtura>> getValMap() {
		return valMap;
	}

	public void setValMap(HashMap<?, List<ParticellaColtura>> valMap) {
		this.valMap = valMap;
	}
}

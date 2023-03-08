package it.tndigitale.a4gistruttoria.util;

import java.math.BigDecimal;

public final class ConversioniCalcoli {
	
	private ConversioniCalcoli() {
	}

	public static BigDecimal convertiMetriQuadriInEttaro(BigDecimal metriquadri) {
		if (metriquadri == null)
			return null;
		return metriquadri.divide(BigDecimal.valueOf(10000));
	}

	public static Float convertiMetriQuadriInEttaro(Float metriquadri) {
		if (metriquadri == null)
			return null;
		BigDecimal result = convertiMetriQuadriInEttaro(new BigDecimal(metriquadri.toString()));
		if (result == null) return null;
		result.setScale(4,BigDecimal.ROUND_HALF_UP);
		return result.floatValue();
	}

	public static BigDecimal convertiEttariInMetriQuadri(BigDecimal metriquadri) {
		if (metriquadri == null)
			return null;
		return metriquadri.multiply(BigDecimal.valueOf(10000));
	}
	
	public static Float convertiEttariInMetriQuadri(Float ettari) {
		if (ettari == null)
			return null;
		BigDecimal result = convertiEttariInMetriQuadri(new BigDecimal(ettari.toString()));
		if (result == null) return null;
		result.setScale(0,BigDecimal.ROUND_HALF_UP);
		return result.floatValue();
	}

	public static BigDecimal getImporto(BigDecimal premio){
		if (premio != null) {
			return premio.setScale(2,BigDecimal.ROUND_HALF_UP);
		}
		return null;
	}
	
	
	public static float sommaSuperfici(float sup1, float sup2) {
		return (new BigDecimal(Float.toString(sup1)).add(new BigDecimal(Float.toString(sup2)))).floatValue();
	}
	
	public static float sottrazioneSuperfici(float sup1, float sup2) {
		return (new BigDecimal(Float.toString(sup1)).subtract(new BigDecimal(Float.toString(sup2)))).floatValue();
	}
	
}

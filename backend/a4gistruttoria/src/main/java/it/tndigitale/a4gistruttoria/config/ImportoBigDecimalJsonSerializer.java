package it.tndigitale.a4gistruttoria.config;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ImportoBigDecimalJsonSerializer extends StdSerializer<BigDecimal> {

	public ImportoBigDecimalJsonSerializer() {
		this(null);
	}

	public ImportoBigDecimalJsonSerializer(Class<BigDecimal> t) {
		super(t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6221797002658154235L;
	
	private static final DecimalFormat FORMAT = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.ITALIAN));
	 
	 
	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		String stringValue = Optional.ofNullable(value).map(v -> FORMAT.format(v)).orElseGet(() -> "");
		gen.writeString(stringValue);
	}

}

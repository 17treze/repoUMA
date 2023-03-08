package it.tndigitale.a4g.srt.dto;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PsrProtocolloDataProgetto {
	
	private String protocollo;
	private LocalDate data;
	
	static DateTimeFormatter dataSegnaturaPatFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public static PsrProtocolloDataProgetto parseSegnaturaProgetto(final String segnatura) throws Exception {
		String dataString = null, protocollo = null;
		if (segnatura.startsWith("PAT")) {
			// formattazione PAT
			// PAT/RFS174-15/03/2019-0175011
			String[] split = segnatura.split("-");
			if (split.length == 3) {
				dataString = split[1];
				protocollo = split[2];
			}
		} else {
			// formattazione Marche
			// 0000004|24/05/2016|P_TN|PAT|S174|A|100/2016/S174/2
			String[] split = segnatura.split("\\|");
			dataString = split[1];
			protocollo = split[0];
		}
		LocalDate localDate = LocalDate.parse(dataString, dataSegnaturaPatFormatter);
		PsrProtocolloDataProgetto psrRet = new PsrProtocolloDataProgetto();
		psrRet.data = localDate;
		psrRet.protocollo = protocollo;
		return psrRet;
	}

	public String getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
}

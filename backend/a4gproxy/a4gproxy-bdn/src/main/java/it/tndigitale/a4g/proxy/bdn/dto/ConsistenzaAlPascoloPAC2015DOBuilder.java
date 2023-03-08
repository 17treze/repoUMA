package it.tndigitale.a4g.proxy.bdn.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiCONSISTENZAPASCOLO2015.CONSISTENZAPASCOLO2015;
import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.support.StringSupport;

@Component
public class ConsistenzaAlPascoloPAC2015DOBuilder {
	private ConsistenzaAlPascoloPAC2015DO consistenza;
	
	// gruppo - [codice - descrizione]
	public final Map<String, Map<String,String>> mappaGruppiSpecie = Map.ofEntries(
			// equidi
			Map.entry("0126", Map.ofEntries(
					Map.entry("0147", "MULI"),
					Map.entry("0148", "BARDOTTI"),
					Map.entry("0149", "ASINI"),
					Map.entry("0126", "CAVALLI")
					)),
			// ovi-caprini
			Map.entry("0124", Map.ofEntries(
					Map.entry("0124", "OVINI"),
					Map.entry("0125", "CAPRINI")
					)),
			// bovini
			Map.entry("0121", Map.ofEntries(
					Map.entry("0121", "BOVINI")
					))
			);
	
	public ConsistenzaAlPascoloPAC2015DOBuilder() {
		consistenza = new ConsistenzaAlPascoloPAC2015DO();
	}
	
	public ConsistenzaAlPascoloPAC2015DOBuilder newDto() {
		consistenza = new ConsistenzaAlPascoloPAC2015DO();
		return this;
	}
	
//	private ConsistenzaAlPascoloPAC2015DO consistenza = new ConsistenzaAlPascoloPAC2015DO();

	public ConsistenzaAlPascoloPAC2015DOBuilder with(String cuaa, Long campagna, String codicePascolo) {
		consistenza.setCodiFiscSogg(cuaa)
		.setNumeCamp(campagna)
		.setCodiPasc(codicePascolo);
		return this;
	}

	public ConsistenzaAlPascoloPAC2015DOBuilder with(CONSISTENZAPASCOLO2015 consistenzaPascolo) {
		consistenza.setCoorLati(consistenzaPascolo.getLATITUDINE() == null ? 0 : consistenzaPascolo.getLATITUDINE().longValue());
		consistenza.setCoorLong(consistenzaPascolo.getLONGITUDINE() == null ? 0 : consistenzaPascolo.getLONGITUDINE().longValue());
		consistenza.setNumeFogl(consistenzaPascolo.getFOGLIOCATASTALE() == null || !StringSupport.isNumber(consistenzaPascolo.getFOGLIOCATASTALE()) ? 9999 : Long.valueOf(consistenzaPascolo.getFOGLIOCATASTALE()));
		if ((consistenzaPascolo.getPARTICELLA() != null)) {
			if (consistenzaPascolo.getPARTICELLA().length() > 5) {
				consistenza.setNumePart("XXXXX");
			} else
				consistenza.setNumePart(consistenzaPascolo.getPARTICELLA());
		} else
			consistenza.setNumePart(null);
		consistenza.setCodiSezi(consistenzaPascolo.getSEZIONE());
		consistenza.setCodiSuba(consistenzaPascolo.getSUBALTERNO());
		consistenza.setCodiSiglProv(consistenzaPascolo.getSIGLA());
		// CodiProv da valorizzare in fase di insert sul DB usando query "select ISTATP
		// from SITI.CATA_PROV where SIGLA_PROV = ?;", record.getCodiSiglProv()
		// record.setCodiProv();
		// TODO: set datainizio / fine / aggiornamento
		consistenza.setCodiComu(consistenzaPascolo.getISTATCOMUNE());
		consistenza.setDescLoca(consistenzaPascolo.getLOCALITA());
		consistenza.setCodiFiscResp(consistenzaPascolo.getCODFISCALEDETEN());
		consistenza.setDecoStat(85L);
		consistenza.setUserName("A4G_BDN");
		return this;
	}
	
	public ConsistenzaAlPascoloPAC2015DOBuilder withFull(ConsistenzaAlPascoloPAC2015DO consistenzaPascolo) {
		consistenza.setCoorLati(consistenzaPascolo.getCoorLati());
		consistenza.setCoorLong(consistenzaPascolo.getCoorLong());
		consistenza.setNumeFogl(consistenzaPascolo.getNumeFogl());
		consistenza.setNumePart(consistenzaPascolo.getNumePart());
		consistenza.setCodiSezi(consistenzaPascolo.getCodiSezi());
		consistenza.setCodiSuba(consistenzaPascolo.getCodiSuba());
		consistenza.setCodiSiglProv(consistenzaPascolo.getCodiSiglProv());
		consistenza.setCodiProv(consistenzaPascolo.getCodiProv());
		consistenza.setDataFine(consistenzaPascolo.getDataFine());
		consistenza.setDataIniz(consistenzaPascolo.getDataIniz());
		consistenza.setDataAggi(consistenzaPascolo.getDataAggi());
		consistenza.setCodiComu(consistenzaPascolo.getCodiComu());
		consistenza.setDescLoca(consistenzaPascolo.getDescLoca());
		consistenza.setCodiFiscResp(consistenzaPascolo.getCodiFiscResp());
		consistenza.setDecoStat(consistenzaPascolo.getDecoStat());
		consistenza.setUserName(consistenzaPascolo.getUserName());
		
		// Campi presenti dalla seconda volta che invoco il builder
		// Sezione cuaa/codice/anno
		if (consistenzaPascolo.getCodiFiscSogg() != null) {
			consistenza.setCodiFiscSogg(consistenzaPascolo.getCodiFiscSogg());
		}
		if (consistenzaPascolo.getCodiPasc() != null) {
			consistenza.setCodiPasc(consistenzaPascolo.getCodiPasc());
		}
		if (consistenzaPascolo.getNumeCamp() != null) {
			consistenza.setNumeCamp(consistenzaPascolo.getNumeCamp());
		}
		// Sezione giorni Pascolamento
		if (consistenzaPascolo.getGiorPasc() != null) {
			consistenza.setGiorPasc(consistenzaPascolo.getGiorPasc());
		}
		if (consistenzaPascolo.getNumeCapiMedi() != null) {
			consistenza.setNumeCapiMedi(consistenzaPascolo.getNumeCapiMedi());
		}
		// Sezione fascia età
		if (consistenzaPascolo.getFascEtaa() != null) {
			consistenza.setFascEtaa(consistenzaPascolo.getFascEtaa());
		}
		
		// Sezione specie/gruppo
		if (consistenzaPascolo.getCodiSpec() != null) {
			consistenza.setCodiSpec(consistenzaPascolo.getCodiSpec());
		}
		if (consistenzaPascolo.getCodiGrupSpec() != null) {
			consistenza.setCodiGrupSpec(consistenzaPascolo.getCodiGrupSpec());
		}
		if (consistenzaPascolo.getDescSpec() != null) {
			consistenza.setDescSpec(consistenzaPascolo.getDescSpec());
		}
		return this;
	}
	
	public ConsistenzaAlPascoloPAC2015DOBuilder addNumeroCapi(Long numeroCapi) {
		consistenza.setNumeCapi(numeroCapi.doubleValue());
		return this;
	}
	
	public ConsistenzaAlPascoloPAC2015DOBuilder addGiorniPascolamento(Long totaleGiorniPascolamento) {
		consistenza.setNumeCapiMedi(BigDecimal.valueOf(totaleGiorniPascolamento.doubleValue() / 365).setScale(3, RoundingMode.HALF_UP).doubleValue());
		consistenza.setGiorPasc(totaleGiorniPascolamento);
		return this;
	}
	
	public ConsistenzaAlPascoloPAC2015DOBuilder addCodiceSpecie(String codiceSpecie) {

		Entry<String, Map<String, String>> mapEntry = this.mappaGruppiSpecie.entrySet()
				.stream()
				.filter(entry -> entry.getValue().containsKey(codiceSpecie))
				.collect(CustomCollectors.toSingleton());

		consistenza
		.setCodiSpec(codiceSpecie)
		.setCodiGrupSpec(mapEntry.getKey())
		.setDescSpec(mapEntry.getValue().get(codiceSpecie));
		return this;
	}

	public ConsistenzaAlPascoloPAC2015DOBuilder addFasciaEta(FasciaEtaConsistenzaPascolo fasciaEta) {
		consistenza.setFascEtaa(fasciaEta.name());
		return this;
	}
	
	public ConsistenzaAlPascoloPAC2015DO build() {
		return this.consistenza;
	}

}


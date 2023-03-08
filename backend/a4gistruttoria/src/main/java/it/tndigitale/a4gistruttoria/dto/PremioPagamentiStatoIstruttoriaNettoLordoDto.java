package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

enum NettoLordoEnum {
	NETTO, LORDO
}

public class PremioPagamentiStatoIstruttoriaNettoLordoDto {
	
	private static Logger log = LoggerFactory.getLogger(PremioPagamentiStatoIstruttoriaNettoLordoDto.class);

	private StatoIstruttoria statoIstruttoria;
	private NettoLordoEnum tipoPremio;
	private BigDecimal valorePremio;

	public StatoIstruttoria getStatoIstruttoria() {
		return statoIstruttoria;
	}

	public void setStatoIstruttoria(StatoIstruttoria statoIstruttoria) {
		this.statoIstruttoria = statoIstruttoria;
	}

	public NettoLordoEnum getTipoPremio() {
		return tipoPremio;
	}

	public void setTipoPremio(NettoLordoEnum tipoVariabile) {
		this.tipoPremio = tipoVariabile;
	}

	public BigDecimal getValorePremio() {
		return valorePremio;
	}

	public void setValorePremio(BigDecimal valorePremio) {
		this.valorePremio = valorePremio;
	}
	
	private static List<PremioPagamentiStatoIstruttoriaNettoLordoDto> reduce(List<PremioPagamentiStatoIstruttoriaNettoLordoDto> collect) {
		List<PremioPagamentiStatoIstruttoriaNettoLordoDto> collect2 = new ArrayList<>();
		for (PremioPagamentiStatoIstruttoriaNettoLordoDto c : collect) {
			Optional<PremioPagamentiStatoIstruttoriaNettoLordoDto> foundInC2Opt = collect2.stream()
					.filter(c2 -> c.getStatoIstruttoria().equals(c2.getStatoIstruttoria()) && c.getTipoPremio().equals(c2.getTipoPremio()))
					.findAny();
			if (foundInC2Opt.isPresent()) {
				PremioPagamentiStatoIstruttoriaNettoLordoDto foundInC2 = foundInC2Opt.get();
				foundInC2.setValorePremio(foundInC2.getValorePremio().add(c.getValorePremio()));
			} else {
				PremioPagamentiStatoIstruttoriaNettoLordoDto newElem = new PremioPagamentiStatoIstruttoriaNettoLordoDto();
				newElem.setStatoIstruttoria(c.getStatoIstruttoria());
				newElem.setValorePremio(c.getValorePremio());
				newElem.setTipoPremio(c.getTipoPremio());
				collect2.add(newElem);
			}
		}
		return collect2;
	}
	
	private static BigDecimal filtraValoriDisaccoppiato(
			final String tipoVariabile,
			final PremioPagamentiStatoIstruttoriaNettoLordoDto ret,
			BigDecimal valorePremio) {
		if (ret.statoIstruttoria.equals(StatoIstruttoria.CONTROLLI_CALCOLO_KO)) {
			if (!(tipoVariabile.equals("BPSIMPRIC")
					|| tipoVariabile.equals("GREIMPRIC")
					|| tipoVariabile.equals("GIOIMPRIC"))) {
				return BigDecimal.ZERO;
			}
		} else if (!tipoVariabile.equals("IMPCALCFINLORDO")) {
			return BigDecimal.ZERO;
		}
		return valorePremio;
	}
	
	private static BigDecimal filtraValoriSuperficie(
			final String tipoVariabile,
			final PremioPagamentiStatoIstruttoriaNettoLordoDto ret,
			BigDecimal valorePremio) {
		if ((tipoVariabile.equals("ACSIMPRICTOT") && !ret.statoIstruttoria.equals(StatoIstruttoria.CONTROLLI_CALCOLO_KO)) ||
				(tipoVariabile.equals("ACSIMPCALCLORDOTOT") && ret.statoIstruttoria.equals(StatoIstruttoria.CONTROLLI_CALCOLO_KO))) {
			return BigDecimal.ZERO;
		}
		return valorePremio;
	}
	
	private static BigDecimal filtraValoriZootecnia(
			final String tipoVariabile,
			final PremioPagamentiStatoIstruttoriaNettoLordoDto ret,
			BigDecimal valorePremio) {
		if (!tipoVariabile.equals("ACZIMPCALCLORDOTOT")) {
			return BigDecimal.ZERO;
		}
		return valorePremio;
	}

	private static PremioPagamentiStatoIstruttoriaNettoLordoDto mapper(
			final Sostegno tipoSostegno, 
			final String statoIstruttoria, final String tipoVariabile, BigDecimal valorePremio) {
		PremioPagamentiStatoIstruttoriaNettoLordoDto ret = new PremioPagamentiStatoIstruttoriaNettoLordoDto();
		
		ret.statoIstruttoria = StatoIstruttoria.valueOfByIdentificativo(statoIstruttoria);
		
		switch (tipoVariabile) {
		case "IMPCALCFIN":
		case "ACSIMPCALCTOT":
		case "ACZIMPCALCTOT":
			ret.tipoPremio = NettoLordoEnum.NETTO;
			break;
		case "BPSIMPRIC":
		case "GREIMPRIC":
		case "GIOIMPRIC":
		case "IMPCALCFINLORDO":
		case "ACSIMPRICTOT":
		case "ACSIMPCALCLORDOTOT":
		case "ACZIMPCALCLORDOTOT":
			switch(tipoSostegno) {
			case DISACCOPPIATO:
				valorePremio = filtraValoriDisaccoppiato(tipoVariabile, ret, valorePremio);
				break;
			case SUPERFICIE:
				valorePremio = filtraValoriSuperficie(tipoVariabile, ret, valorePremio);
				break;
			case ZOOTECNIA:
				valorePremio = filtraValoriZootecnia(tipoVariabile, ret, valorePremio);
				break;
			default:
				valorePremio = BigDecimal.ZERO;
			}
			ret.tipoPremio = NettoLordoEnum.LORDO;
			break;
		default:
			valorePremio = BigDecimal.ZERO;
		}
		ret.valorePremio = valorePremio;
		return ret;
	}
	
	public static List<PremioPagamentiStatoIstruttoriaNettoLordoDto> build(final Sostegno tipoSostegno, final List<Object[]> results) {
		List<PremioPagamentiStatoIstruttoriaNettoLordoDto> collect = results.stream()
		.map(tupla -> PremioPagamentiStatoIstruttoriaNettoLordoDto.mapper(tipoSostegno, (String)tupla[0], (String)tupla[1], (BigDecimal)tupla[2]))
		.collect(Collectors.toList());
		return reduce(collect);
	}
}
